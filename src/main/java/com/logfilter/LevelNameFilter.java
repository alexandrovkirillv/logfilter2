package com.logfilter;

import com.logfilter.ReadLogFile;

import java.util.regex.Pattern;

import static com.logfilter.ReadLogFile.*;

public class LevelNameFilter {

    public static ReadLogFile filter(String mask, String field, ReadLogFile logLine) {

        ReadLogFile filteredLine = new ReadLogFile();
        mask = convertMask(mask);
        Pattern p = Pattern.compile(mask);
        String startDateTime = "";
        String endDateTime = "";
        String periodTime = "";

        switch (field) {

            case "name":
                if (!p.matcher(logLine.getName()).matches()) {
                    filteredLine = logLine;

                }
                break;

            case "message":
                if (!p.matcher(logLine.getMessage()).matches()) {
                    filteredLine = logLine;
                }
                break;

            case "level":

                filteredLine = levelFilter(mask, logLine);

                break;

            case "start":

                startDateTime = mask;

                break;

            case "end":

                endDateTime = mask;

                break;

            case "period":

                periodTime = mask;

                break;

        }

        System.out.println("S = " + startDateTime);
        System.out.println("E = " + endDateTime);

        if ((!startDateTime.equals("")) && (!endDateTime.equals(""))) {
            filteredLine = durationBetween2Dates(startDateTime, endDateTime, filteredLine);
        }

        if ((!startDateTime.equals("")) && (!periodTime.equals(""))) {
            filteredLine = logLinesWithStartAndPeriod(periodTime, startDateTime, filteredLine);
        }
        if ((!endDateTime.equals("")) && (!periodTime.equals(""))) {
            filteredLine = logLinesWithEndAndPeriod(periodTime, endDateTime, filteredLine);
        }

        return filteredLine;
    }

    public static ReadLogFile levelFilter(String mask, ReadLogFile logLine) {

        ReadLogFile filteredLine = new ReadLogFile();
        String[] maskArray = mask.split(",");
        int numberOfFilters = maskArray.length;
        int filterCounter = 0;

        for (String m : maskArray) {
            m = convertMask(m);
            Pattern p = Pattern.compile(m);

            if (!p.matcher(logLine.getLevel()).matches()) {
                filterCounter++;
            }
        }
        if (filterCounter == numberOfFilters) {

            filteredLine = logLine;
        }

        return filteredLine;
    }
}
