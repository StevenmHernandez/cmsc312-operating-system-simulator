package Components;

import Gui.Gui;
import Gui.GuiScreen;
import Parsing.parseText;
import java.util.ArrayList;

public class CommandInterface extends Gui{
    private static final parseText pt = new parseText();

    public static boolean temporaryName(String input) {
        pt.parseLine(input);
        boolean valid = valid();
        if (valid) {
            if (pt.getValue() == null)
                chooseCommand(pt.getCommand());
            else
                chooseCommand(pt.getCommand(), pt.getValue());
        }
        return valid;
    }

    private static boolean valid() {
        if(pt.getCommand().equals("proc"))
            return true;
        if(pt.getCommand().equals("mem"))
            return true;
        if(pt.getCommand().equals("exe")) if(pt.getValue() == null)
            return true;
        if(pt.getCommand().equals("exe")) if(pt.getValue() != null) if(isInt(pt.getValue()))
            return true;
        if(pt.getCommand().equals("reset"))
            return true;
        if(pt.getCommand().equals("exit"))
            return true;
        if(pt.getCommand().equals("clear"))
            return true;
        if(pt.getCommand().equals("load")) if(pt.getValue() != null)
            return true;
        return false;
    }

    private static boolean isInt(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        int i = 0;
        if (value.charAt(0) == '-') {
            if (value.length() > 1) {
                i++;
            } else {
                return false;
            }
        }
        for (; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static void chooseCommand(String command) {
        switch(command) {
            case "proc": proc(); break;
            case "mem": mem(); break;
            case "exe": exe(); break;
            case "reset": reset(); break;
            case "exit": exit(); break;
            case "clear": clear(); break;
            default: break;
        }
    }

    private static void chooseCommand(String command, String value) {
        switch(command) {
            case "load": load(value); break;
            case "exe": exe(Integer.parseInt(value)); break;
            default: break;
        }
    }

    private static void proc() {
        ArrayList<Process> queue = Scheduler.getReadyQueue();
        if (queue.isEmpty())
            GuiScreen.println("No processes are current running.");
        String space = "     ";
        for (Process process: queue) {
            GuiScreen.print("Process: " + process.getName() + space);
            GuiScreen.print("State: " + process.getState() + space);
            GuiScreen.print("Runtime: " + process.getRunTime() + space);
            GuiScreen.print("Remaining Runtime: " + (process.getTotalRuntime()-process.getRunTime()) + space);
            GuiScreen.println("Io Requests: " + process.getIoRequests());
        }
    }

    private static void mem() {
        GuiScreen.println("Memory: " + String.valueOf(Memory.getUsedMemory()) + "/" + String.valueOf(Memory.getTotalMemory()) + "\n");
    }

    private static void load(String job) {
        pt.parseFile(job);
        if(!pt.getQueue().isEmpty()) {
            int size = Integer.valueOf(pt.getQueue().remove(0));
            Scheduler.insertPCB(new Process(job, pt.getQueue(), size));
        }
    }

    private static void exe() {
        Simulator.exeContinuously = true;
        Simulator.exeSteps = -1;
    }

    private static void exe(int steps) {
        Simulator.exeContinuously = false;
        Simulator.exeSteps = steps;
    }

    private static void reset() {
        Simulator.exeContinuously = false;
        Simulator.exeSteps = 0;
        Scheduler.reset();
        Clock.reset();
        EventQueue.reset();
    }

    private static void exit() {
        System.exit(0);
    }

    private static void clear() {
        textArea.clear();
    }

    public void promptUser(){

    }
}