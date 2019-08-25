package com.ReadFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @return methods durationBetween2Dates(), logLinesWithStartAndPeriod(),
 * logLinesWithEndAndPeriod() returns filtered by time period log lines;
 * <p>
 * methods filter() and linesFilter() returns log line filtered  by level, name, message.
 */


public class Filters extends ReadLogFile {

    public static ReadLogFile filter(String mask, String field, ReadLogFile logLine) {

        ReadLogFile filteredLine = new ReadLogFile();
        mask = convertMask(mask);
        Pattern p = Pattern.compile(mask);

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

    public static String convertMask(String mask) {

        ArrayList<Character> charsA = new ArrayList<Character>();
        for (char c : mask.toCharArray()) {
            charsA.add(c);
        }

        if ((charsA.get(charsA.size() - 1).equals('*')) || (charsA.get(0).equals('*'))) {
            mask = mask.replaceAll("[*]", "\\.\\*");
        } else if ((charsA.get(charsA.size() - 1).equals('?')) || (charsA.get(0).equals('?'))) {
            mask = mask.replaceAll("[?]", "\\.\\?");
        }

        return mask;

    }

    public static ReadLogFile durationBetween2Dates(String startDateTimeString, String endDateTimeString, String period, ReadLogFile logLine) {


        ReadLogFile logLineF = new ReadLogFile();

        if (period.equals("")) {
            logLineF = linesBetween2Dates(parseDate(startDateTimeString), parseDate(endDateTimeString), logLine);
        } else if (startDateTimeString.equals("")) {
            logLineF = logLinesWithEndAndPeriod(period, parseDate(endDateTimeString), logLine);
        } else if (endDateTimeString.equals("")) {

            logLineF = logLinesWithStartAndPeriod(period, parseDate(startDateTimeString), logLine);
        }

        return logLineF;

    }

    public static ReadLogFile logLinesWithStartAndPeriod(String durationString, LocalDateTime startDateTime, ReadLogFile logLine) {

        LocalDateTime endDateTime = LocalDateTime.now();

        if (durationString.contains("W")) {

            try {
                Period p = Period.parse(durationString);
                endDateTime = startDateTime.plusDays(p.getDays());

            } catch (Exception DateTimeParseException) {
                System.out.println("Wrong period\\date format");
                System.exit(0);
            }
        } else {

            try {
                Duration duration = Duration.parse(durationString);
                endDateTime = startDateTime.plusNanos(duration.toNanos());
            } catch (Exception DateTimeParseException) {
                System.out.println("Wrong period\\date format");
                System.exit(0);
            }
        }

        return linesBetween2Dates(startDateTime, endDateTime, logLine);

    }


    public static ReadLogFile logLinesWithEndAndPeriod(String durationString, LocalDateTime endDateTime, ReadLogFile logLine) {

        LocalDateTime startDateTime = LocalDateTime.now();

        if (durationString.contains("W")) {
            try {
                Period p = Period.parse(durationString);
                startDateTime = endDateTime.minusDays(p.getDays());

            } catch (Exception DateTimeParseException) {
                System.out.println("Wrong period format");
                System.exit(0);
            }

        } else {
            try {
                Duration duration = Duration.parse(durationString);
                startDateTime = endDateTime.minusNanos(duration.toNanos());
            } catch (Exception DateTimeParseException) {
                System.out.println("Wrong period format");
                System.exit(0);
            }
        }

        return linesBetween2Dates(startDateTime, endDateTime, logLine);

    }

    public static LocalDateTime parseDate(String dateString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime dateTime = LocalDateTime.now();

        try {
            dateTime = LocalDateTime.parse(dateString, formatter);


        } catch (Exception DateTimeParseException) {
            System.out.println("Wrong date format");
            System.exit(0);

        }
        return dateTime;
    }


    public static ReadLogFile linesBetween2Dates(LocalDateTime startDateTime, LocalDateTime endDateTime, ReadLogFile logLine) {

        ReadLogFile filteredList = new ReadLogFile();

        int g = 0;
        int i = 0;

        if (logLine.getDateTime().isBefore(startDateTime)) {
            i = 1;

        }

        if (logLine.getDateTime().isAfter(endDateTime)) {
            g = 1;

        }

        if ((i == 0) & (g == 0)) {

            filteredList = logLine;

        }

        return filteredList;
    }
}
