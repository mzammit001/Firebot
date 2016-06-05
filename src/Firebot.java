/**
 * info1103 - assignment 3
 * <your name>
 * <your unikey>
 */

import java.util.*;
import java.util.stream.*;

public class Firebot{
    private static Scanner scan;
    private static Simulation sim;

    public static void main(String[] args) {
        int seed = 0, width = 0, height = 0;

        try {
            if (args.length != 3)
                throw new NumberFormatException();

            seed = Integer.parseInt(args[0]);
            width = Integer.parseInt(args[1]);
            height = Integer.parseInt(args[2]);

            if (seed <= 0 || width < 1 || height < 1)
                throw new NumberFormatException();

        } catch (NumberFormatException e) {
            System.out.printf("Usage: java Firebot <seed> <width> <height>\n");
            System.exit(1);
        }

        // create a simulation
        sim = new Simulation(width, height, seed);
        // create a scanner
        scan = new Scanner(System.in);
        // start reading the user input
        readInputLoop();
    }

    public static void getCmdFromCmdString(String command, List<String> out) {
        String[] tmp = command.toLowerCase().split("\\s");
        String cmd = tmp[0];
        out.add(String.valueOf(false));

        switch(cmd) {
            case "bye":
            case "help":
            case "data":
            case "status":
                if (tmp.length > 1)
                    return;

                out.set(0, String.valueOf(true));
                out.add(cmd);
                break;

            case "next":
                try {
                    if (tmp.length > 2)
                        throw new NumberFormatException();

                    if (tmp.length == 1) {
                        out.set(0, String.valueOf(true));
                        out.add(cmd);
                        out.add(String.valueOf(1));
                    } else {
                        if ( Integer.parseInt(tmp[1]) >= 1 ) {
                            out.set(0, String.valueOf(true));
                            out.add(cmd);
                            out.add(tmp[1]);
                        } else {
                            throw new NumberFormatException();
                        }
                    }
                } catch (NumberFormatException e) {
                    return;
                }

                break;
            case "fire":
            case "extinguish":
                if (tmp.length == 2 && tmp[1].equals("all")) {
                    out.set(0, String.valueOf(true));
                    out.add(cmd);
                    out.add(String.valueOf(0));
                    out.add(String.valueOf(0));
                    out.add(String.valueOf(sim.getWidth()-1));
                    out.add(String.valueOf(sim.getHeight()-1));

                } else if (tmp.length == 3 || tmp.length == 5) {
                    try {
                        int x0 = Integer.parseInt(tmp[1]);
                        int y0 = Integer.parseInt(tmp[2]);

                        if (! sim.isValidCoord(x0, y0))
                            throw new NumberFormatException();

                        out.set(0, String.valueOf(true));
                        out.add(cmd);
                        out.add(tmp[1]); out.add(tmp[2]);
                        out.add(tmp[1]); out.add(tmp[2]);

                        if (tmp.length == 5) {
                            int x1 = x0 + Integer.parseInt(tmp[3]) - 1;
                            int y1 = y0 + Integer.parseInt(tmp[4]) - 1;

                            if (! sim.isValidCoord(x1, y1) || x1 < x0 || y1 < y0)
                                throw new NumberFormatException();

                            out.set(4, String.valueOf(x1));
                            out.set(5, String.valueOf(y1));
                        }
                    } catch (NumberFormatException e) {
                        out.clear();
                        out.add(String.valueOf(false));
                        return;
                    }
                }

                break;
            case "wind":
                if (tmp.length != 2 ||
                        ! Arrays.asList(new String[]{"east", "west", "north", "south", "all", "none"})
                                .contains(tmp[1])) {
                    return;
                }
                out.set(0, String.valueOf(true));
                out.add(cmd);
                out.add(tmp[1]);

                break;
            case "show":
                if (tmp.length == 2 && (tmp[1].equals("fire") || tmp[1].equals("height"))) {
                    out.set(0, String.valueOf(true));
                    out.add(cmd);
                    out.add(tmp[1]);
                }
            default:
                break;
        }

        return;
    }

    public static void readInputLoop() {
        System.out.print("\n> ");

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            runCommand(line);
            System.out.print("\n> ");
        }
    }

    public static void runCommand(String command) {
        List<String> res = new ArrayList<>();
        getCmdFromCmdString(command, res);

        if (! Boolean.valueOf(res.get(0)) ) {
            System.out.printf("Invalid command\n");
            return;
        }

        String cmd = res.get(1);
        String[] args = new String[res.size()-2];

        if (args.length > 0)
            res.subList(2, res.size()).toArray(args);

        switch(cmd) {
            case "bye"   : bye();
            case "help"  : help(); break;
            case "data"  : sim.printData(); break;
            case "status": sim.printStatus(); break;
            case "next"  : sim.next(Integer.parseInt(args[0])); break;
            case "wind"  : sim.setWindDirection(args[0]); break;

            case "show":
                switch (args[0]) {
                    case "height": sim.printHeightMap(); break;
                    case "fire"  : sim.printFireMap(); break;
                }
                break;

            case "fire":
                sim.fire(new int[]{
                        Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]), Integer.parseInt(args[3]) });
                break;

            case "extinguish":
                sim.extinguish(new int[]{
                        Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]), Integer.parseInt(args[3]) });
                break;

            default:
                System.out.printf("Reached the unreachable for command\n");
                System.exit(1);
        }
    }

    /**
     * returns the result, cmd and args as a list for junit testing
     * @return
     */
    public static List<String> testCommand(String command) {
        List<String> res = new ArrayList<>();
        getCmdFromCmdString(command, res);

        return res;
    }

    public static void testSetupSimulation(int width, int height) {
        sim = new Simulation(width, height, 0);
    }

    public static void help() {
        System.out.printf("BYE\n");
        System.out.printf("HELP\n");
        System.out.printf("\n");
        System.out.printf("DATA\n");
        System.out.printf("STATUS\n");
        System.out.printf("\n");
        System.out.printf("NEXT <days>\n");
        System.out.printf("SHOW <attribute>\n");
        System.out.printf("\n");
        System.out.printf("FIRE <region>\n");
        System.out.printf("WIND <direction>\n");
        System.out.printf("EXTINGUISH <region>\n");
    }

    public static void bye() {
        System.out.printf("bye\n");
        System.exit(0);
    }

}
