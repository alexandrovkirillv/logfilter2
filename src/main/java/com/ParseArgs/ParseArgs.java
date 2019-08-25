package com.ParseArgs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseArgs extends Arg {

    private String logFileName;
    private int status;
    private String mask;
    private String field;
    private ArrayList<ParseArgs> arrayList = new ArrayList<>();
    private Arg arg = new Arg();
    private String startDateTimeString = "";
    private String endDateTimeString = "";
    private String periodTime = "";
    private String outputFileName = "";
    private int stats = 0;

    public ParseArgs(Arg arg, String mask, String field) {

        this.mask = mask;
        this.field = field;
        this.arg = arg;


    }

    public int getStats() {
        return stats;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public Arg getArg() {
        return arg;
    }

    public String getPeriodTime() {
        return periodTime;
    }

    public String getMask() {
        return mask;
    }

    public String getField() {
        return field;
    }

    public ParseArgs() {

    }

    public int getStatus() {
        return status;
    }


    public String getLogFileName() {
        return logFileName;
    }

    public String getStartDateTimeString() {
        return startDateTimeString;
    }

    public String getEndDateTimeString() {
        return endDateTimeString;
    }

    public ArrayList<ParseArgs> parseArgs(String[] args) throws FileNotFoundException {

        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {

                case "-t":

                    NameArg nameArg = new NameArg();
                    arrayList.add(new ParseArgs(nameArg, args[i + 1], "name"));

                    break;

                case "--thread":
                    nameArg = new NameArg();
                    arrayList.add(new ParseArgs(nameArg, args[i + 1], "name"));
                    break;

                case "-m":

                    MessageArg messageArg = new MessageArg();
                    arrayList.add(new ParseArgs(messageArg, args[i + 1], "message"));
                    break;

                case "--message":

                    messageArg = new MessageArg();
                    arrayList.add(new ParseArgs(messageArg, args[i + 1], "message"));
                    break;

                case "-c":

                    Arg arg = new Arg();
                    stats = 1;
                    arrayList.add(new ParseArgs(arg, "", "stat"));
                    break;

                case "--stats":

                    arg = new Arg();
                    stats = 1;
                    arrayList.add(new ParseArgs(arg, "", "stat"));
                    break;

                case "-l":

                    LevelArg levelArg = new LevelArg();
                    arrayList.add(new ParseArgs(levelArg, args[i + 1], "level"));
                    break;

                case "--level":

                    levelArg = new LevelArg();
                    arrayList.add(new ParseArgs(levelArg, args[i + 1], "level"));
                    break;

                case ("-s"):
                    StartDateArg startDateTime = new StartDateArg();
                    startDateTimeString = args[i + 1];
                    arrayList.add(new ParseArgs(startDateTime, args[i + 1], "start"));

                    break;
                case "--start":
                    startDateTime = new StartDateArg();
                    startDateTimeString = args[i + 1];
                    arrayList.add(new ParseArgs(startDateTime, args[i + 1], "start"));
                    break;

                case "-e":

                    endDateTimeString = args[i + 1];

                    break;

                case "--end":

                    endDateTimeString = args[i + 1];

                    break;

                case "-p":

                    StartDateArg startDateArg = new StartDateArg();
                    periodTime = args[i + 1];
                    arrayList.add(new ParseArgs(startDateArg, args[i + 1], "start"));
                    break;
                case "-period":

                    startDateArg = new StartDateArg();
                    periodTime = args[i + 1];
                    arrayList.add(new ParseArgs(startDateArg, args[i + 1], "start"));
                    break;

                case "-o":
                    outputFileName = args[i + 1];
                    break;

                case "--out":


                    outputFileName = args[i + 1];
                    break;

                case "--strict":

                    status = 1;
                    break;

                case "-f":
                    status = 2;
                    break;

                case "--follow":
                    status = 2;
                    break;


            }
        }


        for (String s : args) {

            if (s.contains(".log")) {
                logFileName = s;
            }

            if ((s.contains("-h")) || (s.contains("--help"))) {

                Scanner sc = new Scanner(new File("./src/main/resources/help.txt"));
                while (sc.hasNext())
                    System.out.println(sc.nextLine());
                sc.close();
            }
        }


        return arrayList;
    }

}
