/**
 * info1103 - assignment 3
 * <your name>
 * <your unikey>
 */

import java.awt.Point;
import java.util.*;
import java.nio.*;

enum Wind {
    NONE(0,"none"), NORTH(1,"north"), SOUTH(2,"south"), EAST(4,"east"), WEST(8,"west"), ALL(1|2|4|8,"all");
    private final int id;
    private final String strid;
    private static Map<String,String> stow;

    Wind(final int id, final String strid) {
        this.id = id; this.strid = strid;
        addToTable(strid, strid.toUpperCase());
        addToTable(String.valueOf(id), strid.toUpperCase());
    }

    private void addToTable(String s1, String s2) {
        if (Wind.stow == null)
            Wind.stow = new HashMap<>();

        Wind.stow.put(s1,s2);
    }
    public static Wind mapFrom(String s) { return Wind.valueOf( Wind.stow.getOrDefault(s.toLowerCase(),"none") ); }

    @Override
    public String toString() { return this.strid; }
    public int id() { return this.id; }
    public static Wind[] directions() { return new Wind[]{ Wind.NORTH, Wind.SOUTH, Wind.EAST, Wind.WEST }; }
    public boolean contains(Wind component) { return (this.id() & component.id()) == component.id(); }
}

public class Simulation {
    private int day;
    private Wind wind = Wind.NONE;

    private int width;
    private int height;
    private Tree[][] trees;
    private List<Integer> pollutionData;
    /**
     * Create a simulation instance starting from day 1 with no wind
     * @param width
     * @param height
     */
    public Simulation(int width, int height, int seed) {
        this.width  = width;
        this.height = height;
        this.day    = 1;
        this.trees  = new Tree[height][width];
        this.pollutionData = new ArrayList<>();

        generateTerrain(seed);
        // print the status
        printStatus();
    }

    /**
     * generate daily pollution stats
     */
    private void generateDailyPollutionData() {
        // if its the first day, then its 0 otherwise its yesterdays
        int prev  = getDay() == 1 ? 0 : getPollutionData( getDay() - 1);
        int today = 0;

        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++)
                today += trees[y][x].getPollution();

        pollutionData.add( (prev+today < 0) ? 0 : today+prev);
    }

    private double getDamageData() {
        int totalTrees = 0;
        int totalBurnt = 0;

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                if (trees[y][x].getHeight() > 0 || trees[y][x].isBurntDown()) {
                    // is a tree or was a tree
                    totalTrees += 1;
                    if (trees[y][x].isBurntDown())
                        totalBurnt += 1;
                }
            }
        }

        return ((double)totalBurnt / (double)totalTrees) * 100.0;
    }

    /**
     * override to get the previous days data or 0 if first day
     * @return int
     */
    private int getPollutionData() {
        return (getDay() == 1) ? 0 : getPollutionData(getDay()-1);
    }

    /**
     * get the days pollution data
     * @param day day number
     * @return pollution data from yesterday
     */
    private int getPollutionData(int day) {
        return this.pollutionData.get(day-1);
    }

    /**
     * recursive next day function
     * @param days days to simulate
     */
    public void next(int days) {
        List<Point> burning = getBurningTreeCoordList();

        // first we do the burning of any burning trees
        for ( Point p : burning )
            trees[p.y][p.x].tryBurn();

        // spread the winds
        if (wind != Wind.NONE)
            for (Point p : burning)
                for (Wind w : Wind.directions())
                    spreadWithWind(p, w);

        // calculate the days pollution
        generateDailyPollutionData();
        nextDay();

        // recursive call
        if (days > 1)
            next(days - 1);
        else
            printStatus();
    }

    private void spreadWithWind(Point p, Wind component) {
        if ( ! wind.contains(component) )
            return;

        Point dp = new Point(p.x,p.y);

        switch(component) {
            case NORTH: dp.y -= 1; break;
            case SOUTH: dp.y += 1; break;
            case EAST: dp.x += 1; break;
            case WEST: dp.x -= 1; break;
            default: return;
        }

        if (isValidCoord(dp) && trees[dp.y][dp.x].getIntensity() == 0
                             && trees[dp.y][dp.x].getHeight()     > 0) {
            trees[dp.y][dp.x].setIntensity(1);
        }
    }

    /**
     * get coords of all burning trees
     * @return
     */
    private List<Point> getBurningTreeCoordList() {
        List<Point> lp = new ArrayList<>();

        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++)
                if (trees[y][x].getIntensity() > 0)
                    lp.add(new Point(x, y));

        return lp;
    }

    /**
     * starts a fire from x0, y0 to x1, y1
     * @param region
     */
    public void fire(int[] region) {
        boolean blazing = false;
        // build a co-ordinate list
        for (Point p : buildCoordList(region[0], region[1], region[2], region[3]))
            if (setFire(p))
                blazing = true;

        if (blazing)
            System.out.printf("Started a fire\n");
        else
            System.out.printf("No fires were started\n");

    }

    /**
     * extinguishes a fire from region[1],region[0] and to region[3],region[2] if included
     * @param region
     */
    public void extinguish(int[] region) {
        boolean extinguished = false;

        // build a co-ordinate list
        for (Point p : buildCoordList(region[0], region[1], region[2], region[3]))
            if (extinguishFire(p))
                extinguished = true;

        if (extinguished)
            System.out.printf("Extinguished fires\n");
        else
            System.out.printf("No fires to extinguish\n");

    }

    /**
     * start fire at Point x,y
     * @param p
     * @return
     */
    private boolean setFire(Point p) {
        // will i be lazy ??
        if (!isValidCoord(p.x,p.y))
            return false;

        Tree t = trees[p.y][p.x];

        // set the tree ablaze!
        if (t.getHeight() > 0 && t.getIntensity() == 0) {
            t.setIntensity(1);
            return true;
        }

        return false;
    }

    /**
     * put out fire at Point x,y
     * @param p
     * @return
     */
    private boolean extinguishFire(Point p) {
        // will i be lazy ??
        if (!isValidCoord(p))
            return false;

        Tree t = trees[p.y][p.x];

        // douse the fire, no more blazin
        if (t.getHeight() > 0 && t.getIntensity() > 0) {
            t.setIntensity(0);
            return true;
        }

        return false;
    }

    /**
     * sets the current wind direction
     * accepts: north, south, east, west, all, none
     * @param direction
     */
    public void setWindDirection(String direction) {
        this.wind = Wind.mapFrom(direction);
        System.out.printf("Set wind to %s\n", this.wind.toString());
    }

    public String getWindDirection() {  return this.wind.toString(); }

    /**
     * get the terrain width
     * @return
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * get the terrain height
     * @return
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * get the current day
     * @return
     */
    public int getDay() {
        return this.day;
    }
    private void nextDay() { this.day++; }

    /**
     * check that the coordinate is within the bounds of the terrain
     */
    public boolean isValidCoord(int x, int y) {
        return ((x >= 0 && x < getWidth()) && (y >= 0 && y < getHeight()));
    }
    public boolean isValidCoord(Point p) { return isValidCoord(p.x,p.y); }

    /**
     * prints the current damage and pollution data
     */
    public void printData() {
        double damage = getDamageData();
        // possible rounding errors?
        System.out.printf("Damage: %.2f%%\n", (int)damage == 100 ? 100.0 : damage);
        System.out.printf("Pollution: %d\n", getPollutionData());
    }

    /**
     * prints the current status
     */
    public void printStatus() {
        System.out.printf("Day: %d\n", this.day);
        System.out.printf("Wind: %s\n", this.wind);
    }

    /**
     * draw the border for the terrain map
     * @param map
     */
    private void drawMapBorder(char[][] map) {
        int w = getWidth() * 2 - 1;
        for (int y = 0; y < getHeight()+2; y++)
            for (int x = 0; x < w+2; x++)
                if (y == 0 || y == (getHeight()+2)-1) {
                    map[y][x] = (x == 0 || x == (w + 2) - 1) ? '+' : '-';
                }
                else
                if (x == 0 || x == (w+2)-1)
                    map[y][x] = '|';
    }

    /**
     * display the tree height map
     */
    public void printHeightMap() {
        int w = getWidth()*2 - 1;
        char[][] map = new char[getHeight()+2][w+2];
        // draw borders
        drawMapBorder(map);

        for (int y = 1; y < getHeight()+1; y++)
            for (int x = 1; x < w+1; x++)
                map[y][x] = ' ';

        // draw the height map for the terrain
        for (int y = 1; y < getHeight()+1; y++)
            for (int x = 1; x < w+1; x+=2)
                map[y][x] = trees[y-1][x/2].getPrintableHeight();

        // display the map
        for ( char[] c : map )
            System.out.printf("%s\n",String.valueOf(c));
    }

    /**
     * display the fire intensity map
     */
    public void printFireMap() {
        int w = getWidth()*2 - 1;
        char[][] map = new char[getHeight()+2][w+2];
        // draw borders
        drawMapBorder(map);

        for (int y = 1; y < getHeight()+1; y++)
            for (int x = 1; x < w+1; x++)
                map[y][x] = ' ';

        // draw the height map for the terrain
        for (int y = 1; y < getHeight()+1; y++)
            for (int x = 1; x < w+1; x+=2)
                map[y][x] = trees[y-1][x/2].getPrintableIntensity();

        // display the map
        for ( char[] c : map )
            System.out.printf("%s\n",String.valueOf(c));
    }

    /**
     * build a list of points from a single point
     * @param x
     * @param y
     * @return
     */
    public static List<Point> buildCoordList(int x, int y) {
        return buildCoordList(x, y, x, y);
    }

    /**
     * build a list of points from a region
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     * @return
     */
    public static List<Point> buildCoordList(int x0, int y0, int x1, int y1) {
        List<Point> lp = new ArrayList<>();

        for (int y = y0; y <= y1; y++)
            for (int x = x0; x <= x1; x++)
                lp.add(new Point(x,y));

        return lp;
    }

    /**
     * generates the terrain i suppose!
     * @param seed
     */
    private void generateTerrain(int seed) {

        // ###################################
        // ### DO NOT MODIFY THIS FUNCTION ###
        // ###################################

        Perlin perlin = new Perlin(seed);
        double scale = 10.0 / Math.min(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double height = perlin.noise(x * scale, y * scale, 0.5);
                height = Math.max(0, height * 1.7 - 0.7) * 10;
                trees[y][x] = new Tree((int) height);
            }
        }
    }
}
