package com.logfilter;

import java.time.Duration;
import java.util.*;


public class Stat extends ReadLogFile {

    private String inputName;
    private int id;
    private static ArrayList<String> numbersOfLinesGroupedByPercent = new ArrayList<>();
    private static ArrayList<String> numbersOfLinesByLevels = new ArrayList<>();
    public static ArrayList<String> getNumbersOfLinesGroupedByPercent() {
        return numbersOfLinesGroupedByPercent;
    }
    public ArrayList<String> getNumbersOfLinesByLevels() {
        return numbersOfLinesByLevels;
    }

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


    public static ArrayList<Stat> showStat(ArrayList<ReadLogFile> logFileArrayList) {

        List<Stat> arrayList = new ArrayList<>();
        ArrayList<Stat> arrayListStat = new ArrayList<>();

        ArrayList <String> names = new ArrayList<>();

        for (ReadLogFile r : logFileArrayList) {

            names.add(r.getName());

        }

        Set<String> unique = new HashSet<String>(names);

        for (String s : unique) {
            arrayList.add(new Stat(s, Collections.frequency(names, s)));
        }
        arrayList.sort(Comparator.comparing(Stat::getId));

        System.out.println("Number Of Log Lines: " + logFileArrayList.size());
        System.out.println("Average time between the log lines(Millis): " + averageTimeBetweenTheLogLines(logFileArrayList));
        System.out.println("Numbers Of Lines Grouped By Log Levels: " + numbersOfLinesGroupedByLogLevels(logFileArrayList));
        System.out.println("Numbers Of Lines Grouped By Percent: " + numbersOfLinesGroupedByPercent);
        System.out.println("Number Of Unique Names: " + unique.size());
        System.out.println("Most encountered thread name: " + arrayList.get(arrayList.size() - 1).getInputName());
        System.out.println("Least encountered thread name: " + arrayList.get(0).getInputName());

        arrayListStat.add(new Stat("Number Of Log Lines: ", logFileArrayList.size()));
        arrayListStat.add(new Stat("Average time between the log lines(Millis): ", averageTimeBetweenTheLogLines(logFileArrayList)));
        arrayListStat.add(new Stat("Number Of Unique Names: ", unique.size()));
        arrayListStat.add(new Stat(arrayList.get(arrayList.size() - 1).getInputName(), 0));
        arrayListStat.add(new Stat(arrayList.get(0).getInputName(), 0));


        return arrayListStat;
    }

    public static ArrayList<String> numbersOfLinesGroupedByLogLevels(ArrayList<ReadLogFile> logFileArrayList) {

        int TRACE = 0;
        int DEBUG = 0;
        int INFO = 0;
        int WARN = 0;
        int ERROR = 0;

        for (ReadLogFile m : logFileArrayList) {

            switch (m.getLevel()) {

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

        return numbersOfLinesByLevels;
    }


    public static int averageTimeBetweenTheLogLines(ArrayList<ReadLogFile> logFileArrayList) {

        ArrayList<Long> avTime = new ArrayList<>();
        int avTimeLog = 0;

        for (int i = 0; i < logFileArrayList.size() - 1; i++) {

            avTime.add(Duration.between(logFileArrayList.get(i).getDateTime(), logFileArrayList.get(i + 1).getDateTime()).toMillis());
            avTimeLog += avTime.get(i);

        }

        return avTimeLog / (logFileArrayList.size() - 1);

    }

}

