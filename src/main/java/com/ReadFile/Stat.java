package com.ReadFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class Stat extends ReadLogFile {

    private String inputName;
    private int id;
    private static int TRACE = 0;
    private static int DEBUG = 0;
    private static int INFO = 0;
    private static int WARN = 0;
    private static int ERROR = 0;
    private static int countLogLine = 0;
    private static ArrayList<String> numbersOfLinesGroupedByPercent = new ArrayList<>();
    private static ArrayList<String> numbersOfLinesByLevels = new ArrayList<>();
    private static ArrayList<String> names = new ArrayList<>();
    private static Set<String> unique = new HashSet<>();
    private static List<Stat> arrayList = new ArrayList<>();
    private static ArrayList<LocalDateTime> dateTimes = new ArrayList<>();


    public Stat(String inputName, int id) {

        this.inputName = inputName;
        this.id = id;
    }

    public Stat() {

    }

    public String getInputName() {
        return inputName;
    }

    public Integer getId() {
        return id;
    }


    public static void showStat() {

        calcLevelsToShow();
        calcNames();

        System.out.println("Number Of Log Lines: " + countLogLine);
        System.out.println("Average time between the log lines(Millis): " + averageTimeBetweenTheLogLines(dateTimes));
        System.out.println("Numbers Of Lines Grouped By Log Levels: " + numbersOfLinesByLevels);
        System.out.println("Numbers Of Lines Grouped By Percent: " + numbersOfLinesGroupedByPercent);
        System.out.println("Number Of Unique Names: " + unique.size());
        System.out.println("Most encountered thread name: " + arrayList.get(arrayList.size() - 1).getInputName());
        System.out.println("Least encountered thread name: " + arrayList.get(0).getInputName());

//        arrayListStat.add(new Stat("Number Of Log Lines: ", logFileArrayList.size()));
//        arrayListStat.add(new Stat("Average time between the log lines(Millis): ", averageTimeBetweenTheLogLines(logFileArrayList)));
//        arrayListStat.add(new Stat("Number Of Unique Names: ", unique.size()));
//        arrayListStat.add(new Stat(arrayList.get(arrayList.size() - 1).getInputName(), 0));
//        arrayListStat.add(new Stat(arrayList.get(0).getInputName(), 0));

    }

    public static void calcStat(ReadLogFile logLine) {


        countLogLine++;

        calcLevels(logLine);

        names.add(logLine.getName());

        unique.add(logLine.getName());

        dateTimes.add(logLine.getDateTime());

    }

    public static void calcNames() {

        for (String s : unique) {
            arrayList.add(new Stat(s, Collections.frequency(names, s)));
        }
        arrayList.sort(Comparator.comparing(Stat::getId));

    }

    public static void calcLevels(ReadLogFile logLine) {

        switch (logLine.getLevel()) {

            case "INFO":
                INFO++;
                break;
            case "TRACE":

                TRACE++;
                break;
            case "DEBUG":

                DEBUG++;
                break;
            case "WARN":

                WARN++;
                break;
            case "ERROR":

                ERROR++;
                break;
        }

    }

    public static void calcLevelsToShow() {

        numbersOfLinesByLevels.add("TRACE " + TRACE);
        numbersOfLinesByLevels.add("DEBUG " + DEBUG);
        numbersOfLinesByLevels.add("INFO " + INFO);
        numbersOfLinesByLevels.add("WARN " + WARN);
        numbersOfLinesByLevels.add("ERROR " + ERROR);

        int percent = INFO + TRACE + WARN + DEBUG + ERROR;

        numbersOfLinesGroupedByPercent.add("TRACE " + (TRACE * 100 / percent));
        numbersOfLinesGroupedByPercent.add("DEBUG " + (DEBUG * 100 / percent));
        numbersOfLinesGroupedByPercent.add("INFO " + (INFO * 100 / percent));
        numbersOfLinesGroupedByPercent.add("WARN " + (WARN * 100 / percent));
        numbersOfLinesGroupedByPercent.add("ERROR " + (ERROR * 100 / percent));
    }


    public static int averageTimeBetweenTheLogLines(ArrayList<LocalDateTime> dateTimes) {

        int avTimeLog = 0;

        for (int i = 0; i < dateTimes.size() - 1; i++) {

            avTimeLog += (Duration.between(dateTimes.get(i), dateTimes.get(i + 1)).toMillis());

        }

        return avTimeLog / (dateTimes.size() - 1);

    }

}

