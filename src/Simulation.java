/**
 * info1103 - assignment 3
 * <your name>
 * <your unikey>
 */

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
    public Simulation(int width, int height) {
        this.width = width;
        this.height = height;
        // first day and no wind
        this.day = 1;
        this.wind = "none";
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
        boolean hasRange = true;

        int[] origin = new int[2];
        int[] range  = new int[2];

        // store the begin co-ords
        System.arraycopy(region, 0, origin, 0, 2);

        if (region.length == 4)
            System.arraycopy(region, 2, range, 0, 2);
        else
            hasRange = false;

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

    /**
     * prints the current damage and pollution data
     */
    public void printData() {
        System.out.printf("Damage: %.2f\\%\n", this.damage);
        System.out.printf("Pollution: %d\n", this.pollution);
        //test
    }

    /**
     * prints the current status
     */
    public void printStatus() {
        System.out.printf("Day: %d\n", this.day);
        System.out.printf("Wind: %s\n", this.wind);
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
