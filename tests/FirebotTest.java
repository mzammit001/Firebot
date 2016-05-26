import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class FirebotTest {
    @Test
    public void firebotTest_command_Bye_result_2() {
        String command = "bye";
        List<String> res = Firebot.testCommand(command);

        assertEquals(2, res.size());
    }

    @Test
    public void firebotTest_command_Help_result_2() {
        String command = "help";
        List<String> res = Firebot.testCommand(command);

        assertEquals(2, res.size());
    }

    @Test
    public void firebotTest_command_Data_result_2() {
        String command = "data";
        List<String> res = Firebot.testCommand(command);

        assertEquals(2, res.size());
    }

    @Test
    public void firebotTest_command_Status_result_2() {
        String command = "status";
        List<String> res = Firebot.testCommand(command);

        assertEquals(2, res.size());
    }

    @Test
    public void firebotTest_command_Invalid_result_1() {
        String command = "invalidcommand";
        List<String> res = Firebot.testCommand(command);

        assertEquals(1, res.size());
    }

    @Test
    public void firebotTest_command_ShowHeight_result_3() {
        String command = "show height";
        List<String> res = Firebot.testCommand(command);

        assertEquals(3, res.size());
        assertEquals("height", res.get(2));
    }

    @Test
    public void firebotTest_command_ShowFire_result_3() {
        String command = "show fire";
        List<String> res = Firebot.testCommand(command);

        assertEquals(3, res.size());
        assertEquals("fire", res.get(2));
    }

    @Test
    public void firebotTest_command_ShowInvalid_result_1() {
        String command = "show width";
        List<String> res = Firebot.testCommand(command);

        assertEquals(1, res.size());
    }

    @Test
    public void firebotTest_command_ShowNoArgsInvalid_result_1() {
        String command = "show";
        List<String> res = Firebot.testCommand(command);

        assertEquals(1, res.size());
    }

    @Test
    public void firebotTest_command_windNone_result_3() {
        String command = "wind none";
        List<String> res = Firebot.testCommand(command);

        assertEquals(3, res.size());
        assertEquals("none", res.get(2));
    }

    @Test
    public void firebotTest_command_windEast_result_3() {
        String command = "wind east";
        List<String> res = Firebot.testCommand(command);

        assertEquals(3, res.size());
        assertEquals("east", res.get(2));
    }

    @Test
    public void firebotTest_command_windWest_result_3() {
        String command = "wind west";
        List<String> res = Firebot.testCommand(command);

        assertEquals(3, res.size());
        assertEquals("west", res.get(2));
    }

    @Test
    public void firebotTest_command_windNorth_result_3() {
        String command = "wind north";
        List<String> res = Firebot.testCommand(command);

        assertEquals(3, res.size());
        assertEquals("north", res.get(2));
    }

    @Test
    public void firebotTest_command_windSouth_result_3() {
        String command = "wind south";
        List<String> res = Firebot.testCommand(command);

        assertEquals(3, res.size());
        assertEquals("south", res.get(2));
    }

    @Test
    public void firebotTest_command_windAll_result_3() {
        String command = "wind all";
        List<String> res = Firebot.testCommand(command);

        assertEquals(3, res.size());
        assertEquals("all", res.get(2));
    }

    @Test
    public void firebotTest_command_windInvalid_result_1() {
        String command = "wind northeast";
        List<String> res = Firebot.testCommand(command);

        assertEquals(1, res.size());
    }

    @Test
    public void firebotTest_command_windNoArgsInvalid_result_1() {
        String command = "wind";
        List<String> res = Firebot.testCommand(command);

        assertEquals(1, res.size());
    }

    @Test
    public void firebotTest_command_nextNoArg_result_3() {
        String command = "next";
        List<String> res = Firebot.testCommand(command);

        assertEquals(3, res.size());
        assertEquals(1, Integer.parseInt(res.get(2)));
    }

    @Test
    public void firebotTest_command_nextDays_result_3() {
        String command = "next 4";
        List<String> res = Firebot.testCommand(command);

        assertEquals(3, res.size());
        assertEquals(4, Integer.parseInt(res.get(2)));
    }

    @Test
    public void firebotTest_command_nextInvalidZero_result_1() {
        String command = "next 0";
        List<String> res = Firebot.testCommand(command);

        assertEquals(1, res.size());
    }

    @Test
    public void firebotTest_command_nextInvalidNegative_result_1() {
        String command = "next -10";
        List<String> res = Firebot.testCommand(command);

        assertEquals(1, res.size());
    }

    @Test
    public void firebotTest_command_fireRegion_0_0_result_6() {
        String command = "fire 0 0";

        Firebot.testSetupSimulation(5,5);
        List<String> res = Firebot.testCommand(command);

        assertEquals(6, res.size());
        assertEquals(0, Integer.parseInt(res.get(2)));
        assertEquals(0, Integer.parseInt(res.get(3)));
        assertEquals(0, Integer.parseInt(res.get(4)));
        assertEquals(0, Integer.parseInt(res.get(5)));
    }


    @Test
    public void firebotTest_command_fireRegion_All_result_6() {
        String command = "fire all";

        Firebot.testSetupSimulation(5,5);
        List<String> res = Firebot.testCommand(command);

        assertEquals(6, res.size());
        assertEquals(0, Integer.parseInt(res.get(2)));
        assertEquals(0, Integer.parseInt(res.get(3)));
        assertEquals(4, Integer.parseInt(res.get(4)));
        assertEquals(4, Integer.parseInt(res.get(5)));
    }

    @Test
    public void firebotTest_command_fireRegion_Invalid_OutOfBounds_result_1() {
        String command = "fire 0 0 10 10";

        Firebot.testSetupSimulation(5,5);
        List<String> res = Firebot.testCommand(command);

        assertEquals(1, res.size());
    }

    @Test
    public void firebotTest_command_fireRegion_Invalid_Width0_Height0_result_1() {
        String command = "fire 0 0 0 0";

        Firebot.testSetupSimulation(5,5);
        List<String> res = Firebot.testCommand(command);

        assertEquals(1, res.size());
    }
}
