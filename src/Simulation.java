/**
 * info1103 - assignment 3
 * <your name>
 * <your unikey>
 */

import java.awt.Point;
import java.util.*;

public class Simulation {
    private int day;
    private String wind;

    //
    // TODO
    //

    private int width;
    private int height;
    private Tree[][] trees;

    private List<Integer[][]> treeData;
    private List<Integer[][]> fireData;
    private List<Integer> pollutionData;

    /**
     * Create a simulation instance starting from day 1 with no wind
     * @param width
     * @param height
     */
    public Simulation(int width, int height, int seed) {
        this.width  = width;
        this.height = height;
        this.wind   = "none";
        this.day    = 1;
        this.trees  = new Tree[height][width];

        generateTerrain(seed);
        // print the status
        printStatus();
    }

    /**
     * values for data as follows:
     *     0 = no tree or tree burnt down (both are equivalent)
     *   1-9 = tree exists and has a height
     */
    private void generateDailyTreeData() {
        Integer[][] data = new Integer[getHeight()][getWidth()];

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                data[y][x] = trees[y][x].isBurntDown() ? -1 : trees[y][x].getHeight();
            }
        }
    }

    /**
     * values for data as follows:
     *    -1 = no tree or tree burnt down (both are equivalent)
     *     0 = no fire
     *   1-9 = fire
     */
     private void generateDailyFireData() {
        Integer[][] data = new Integer[getHeight()][getWidth()];

        if (getDay() == 1) {
            for (int y = 0; y < getHeight(); y++)
                for (int x = 0; x < getWidth(); x++)
                    data[y][x] = (trees[y][x].getHeight() > 0) ? 0 : -1;

            fireData.add(data);
            return;
        }
    }

    private Integer[][] getFireData() {
        return getFireData(getDay());
    }

    private Integer[][] getFireData(int day) {
        if (getDay() == 1) {
            generateDailyFireData();
            return this.fireData.get(0);
        }

        return this.fireData.get(getDay()-2);
    }

    private void generateDailyPollutionData() {
        // if its the first day, then its 0 otherwise its yesterdays
        int prev  = getDay() == 1 ? 0 : getPollutionData( getDay() - 1);
        int today = 0;

        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++)
                today += trees[y][x].getPollution();

        pollutionData.add( (today-prev < 0) ? 0 : today-prev);
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
     * @return
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
     * @param days
     */
    public void next(int days) {
        // TODO

        if (days > 1)
            next(days - 1);
        else
            printStatus();
    }

    /**
     * starts a fire from region[1],region[0] and to region[3],region[2] if included
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
        int x = (int)p.getX();
        int y = (int)p.getY();

        // will i be lazy ??
        if (!isValidCoord(x,y))
            return false;

        // set the tree ablaze!
        if (trees[y][x].getHeight() > 0 && trees[y][x].getIntensity() == 0) {
            trees[y][x].setIntensity(1);
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
        int x = (int)p.getX();
        int y = (int)p.getY();

        // will i be lazy ??
        if (!isValidCoord(x,y))
            return false;

        // set the tree ablaze!
        if (trees[y][x].getHeight() > 0 && trees[y][x].getIntensity() > 0) {
            trees[y][x].setIntensity(0);
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
        this.wind = direction;
        System.out.printf("Set wind to %s\n", this.wind);
    }

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

    /**
     * check that the coordinate is within the bounds of the terrain
     */
    public boolean isValidCoord(int x, int y) {
        return ((x >= 0 && x < getHeight()) && (y >= 0 && y < getWidth()));
    }

    /**
     * prints the current damage and pollution data
     */
    public void printData() {
        double damage = getDamageData();
        // possible rounding errors?
        System.out.printf("Damage: %.2f\\%\n", damage > 100.0 ? 100.0 : damage);
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
        for (int y = 0; y < getHeight()+2; y++)
            for (int x = 0; x < getWidth()+2; x++)
                if (y == 0 || y == (getHeight()+2)-1)
                    map[y][x] = (x == 0 || x == (getWidth()+2)-1) ? '+' : '-';
                else
                if (x == 0 || x == (getWidth()+2)-1)
                    map[y][x] = '|';
    }

    /**
     * display the tree height map
     */
    public void printHeightMap() {
        char[][] map = new char[getHeight()+2][getWidth()+2];
        // draw borders
        drawMapBorder(map);

        // draw the height map for the terrain
        for (int y = 1; y < getHeight()+1; y++)
            for (int x = 1; x < getWidth()+1; x++)
                map[y][x] = trees[y-1][x-1].getPrintableHeight();

        // display the map
        for ( char[] c : map )
            System.out.printf("%s\n",String.valueOf(c));

        System.out.printf("\n");
    }

    /**
     * display the fire intensity map
     */
    public void printFireMap() {
        char[][] map = new char[getHeight()+2][getWidth()+2];
        // draw borders
        drawMapBorder(map);

        // draw the intensity map for the terrain
        for (int y = 1; y < getHeight()+1; y++)
            for (int x = 1; x < getWidth()+1; x++)
                map[y][x] = trees[y-1][x-1].getPrintableIntensity();

        // display the map
        for ( char[] c : map )
            System.out.printf("%s\n",String.valueOf(c));

        System.out.printf("\n");
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
