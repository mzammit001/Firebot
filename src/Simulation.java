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
    private double damage;
    private int pollution;

    //
    // TODO
    //

    private int width;
    private int height;
    private Tree[][] trees;

    /**
     * Create a simulation instance starting from day 1 with no wind
     * @param width
     * @param height
     */
    public Simulation(int width, int height, int seed) {
        this.width = width;
        this.height = height;
        // first day and no wind
        this.day = 1;
        this.wind = "none";

        this.trees = new Tree[height][width];
        generateTerrain(seed);
        // TODO
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

        Point p0 = new Point( region[0], region[1] );
        Point p1 = new Point( region[2], region[3] );

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

        Point p0 = new Point( region[0], region[1] );
        Point p1 = new Point( region[2], region[3] );

        if (extinguished)
            System.out.printf("Extinguished fires\n");
        else
            System.out.printf("No fires to extinguish\n");
    }

    /**
     * sets the current wind direction
     * accepts: north, south, east, west, all
     * @param direction
     */
    public void setWindDirection(String direction) {
        this.wind = direction;
        System.out.printf("Set wind to %s\n", this.wind);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
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
        System.out.printf("Damage: %.2f\\%\n", this.damage);
        System.out.printf("Pollution: %d\n", this.pollution);
    }

    /**
     * prints the current status
     */
    public void printStatus() {
        System.out.printf("Day: %d\n", this.day);
        System.out.printf("Wind: %s\n", this.wind);
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
