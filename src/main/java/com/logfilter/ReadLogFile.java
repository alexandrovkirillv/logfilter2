package com.logfilter;

import com.readlog.ParseArgs;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Alexandrov Kirill
 * @version 0.2
 * @return method read() is reading file and returns Set of objects with  fields (localdate, level, name, message);
 * methods durationBetween2Dates(), logLinesWithStartAndPeriod(),
 * logLinesWithEndAndPeriod() returns filtered by time period log lines;
 * methods filter() and linesFilter() returns log filtered lines by level, name, message.
 */

public class ReadLogFile implements Comparable<ReadLogFile>, Serializable {

    private LocalDateTime dateTime = LocalDateTime.now();
    private String level = "";
    private String name = "";
    private String message = "";
    private static int stat = 0;
    private static int strictCount = 0;
    private static Set<ReadLogFile> arrayLogLines = new TreeSet<>();

    public ReadLogFile() {
    }

    public ReadLogFile(LocalDateTime dateTime, String level, String name, String message) {
        this.dateTime = dateTime;
        this.level = level;
        this.name = name;
        this.message = message;
    }

    public static int getStrictCount() {
        return strictCount;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public Set<ReadLogFile> getArrayLogLines() {
        return arrayLogLines;
    }


    public static int getStat() {
        return stat;
    }

    public static Set<ReadLogFile> read(String status, String[] args) {


        ParseArgs parseArgs = new ParseArgs();

        String SIGINT = "SIGINT";
        ArrayList<ParseArgs> argsList = parseArgs.parseArgs(args);
        String logFileName = parseArgs.getLogFileName();
        File f = new File("./src/main/resources/" + logFileName);
        ReadLogFile tempLog = new ReadLogFile();
        int numberOfFilters = argsList.size();
        int filterCounter = 0;

        if (argsList.get(0).getField().equals("stat")){
            stat =1;
        }

        try {
            LineIterator it = FileUtils.lineIterator(f, "UTF-8");

            while (it.hasNext()) {

                String str = (it.nextLine().replaceAll("[\\s]{2,}", " "));

                for (ParseArgs p : argsList) {
                    tempLog = p.getLogs().filter(p.getMask(), p.getField(), getData(str, status));

                    if (!tempLog.getLevel().equals("")) {

                        filterCounter++;

                    }
                }



                if (filterCounter == numberOfFilters) {
                    arrayLogLines.add(tempLog);
                }
                filterCounter = 0;

            }
            it.close();


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ((status.equals("-f")) || (status.equals("--follow"))) {
            String str;

            Scanner in = new Scanner(System.in);
            System.out.println("Input log line(print 'SIGINT' for result): ");
            while (!(str = in.nextLine()).equals(SIGINT)) {

                str = str.replaceAll("[\\s]{2,}", " ");

                getData(str, status);


            }
            in.close();
        }

        return arrayLogLines;
    }

    public static ReadLogFile getData(String Str, String status) {

        ReadLogFile logLine = new ReadLogFile();

        String[] subStr = Str.split(" ");
        String messageStr = "";

        LocalDateTime dateTimeTemp = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            dateTimeTemp = (LocalDateTime.parse(subStr[0], formatter));
        } catch (Exception e) {

            strictCount++;

        }

        try {
            if ((!subStr[1].equals("TRACE")) && (!subStr[1].equals("ERROR")) && (!subStr[1].equals("DEBUG")) && (!subStr[1].equals("INFO")) && (!subStr[1].equals("WARN"))) {
                strictCount++;
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {

        }


        try {
            if ((!subStr[2].contains("[")) || (!subStr[3].contains("]")) || (subStr[3].length() > 17))
                strictCount++;
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {

        }
        try {
            for (int g = 4; g != subStr.length; g++) {

                messageStr += subStr[g];
                messageStr += " ";
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
        }


        if (strictCount == 0) {
            logLine = new ReadLogFile(dateTimeTemp, subStr[1], subStr[3].substring(0, subStr[3].length() - 1), messageStr);
        }

        if (!status.equals("--strict")) {
            strictCount = 0;
        }

        return logLine;

    }


    public static ArrayList<ReadLogFile> durationBetween2Dates(String startDateTimeString, String endDateTimeString, ArrayList<ReadLogFile> arrayOfLogLines) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeString, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeString, formatter);
        return linesBetween2Dates(startDateTime, endDateTime, arrayOfLogLines);

    }

    public static ArrayList<ReadLogFile> logLinesWithStartAndPeriod(String durationString, String startDateTimeString, ArrayList<ReadLogFile> arrayOfLogLines) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeString, formatter);

        LocalDateTime endDateTime = LocalDateTime.now();

        if (durationString.contains("W")) {

            try {
                Period p = Period.parse(durationString);
                endDateTime = startDateTime.plusDays(p.getDays());
            } catch (Exception e) {
                System.out.println("Wrong period format");
                System.exit(0);
            }
        } else {

            try {
                Duration duration = Duration.parse(durationString);
                endDateTime = startDateTime.plusNanos(duration.toNanos());
            } catch (Exception e) {
                System.out.println("Wrong period format");
                System.exit(0);
            }
        }

        return linesBetween2Dates(startDateTime, endDateTime, arrayOfLogLines);

    }


    public static ArrayList<ReadLogFile> logLinesWithEndAndPeriod(String durationString, String endDateTimeString, ArrayList<ReadLogFile> arrayOfLogLines) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime endDateTime = LocalDateTime.now();

        try {
            endDateTime = LocalDateTime.parse(endDateTimeString, formatter);
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("Wrong DateTime format");
            System.exit(0);

        }


        LocalDateTime startDateTime = LocalDateTime.now();

        if (durationString.contains("W")) {
            try {
                Period p = Period.parse(durationString);
                startDateTime = endDateTime.minusDays(p.getDays());
            } catch (Exception e) {
                System.out.println("Wrong period format");
                System.exit(0);
            }

        } else {
            try {
                Duration duration = Duration.parse(durationString);
                startDateTime = endDateTime.minusNanos(duration.toNanos());
            } catch (Exception e) {
                System.out.println("Wrong period format");
                System.exit(0);
            }
        }

        return linesBetween2Dates(startDateTime, endDateTime, arrayOfLogLines);

    }

    public static ArrayList<ReadLogFile> linesBetween2Dates(LocalDateTime startDateTime, LocalDateTime endDateTime, ArrayList<ReadLogFile> arrayOfLogLines) {

        ArrayList<ReadLogFile> filteredList = new ArrayList<>();
        int g = 0;
        int i = 0;

        for (int k = 0; k < arrayOfLogLines.size(); k++) {
            if (arrayOfLogLines.get(k).dateTime.isAfter(startDateTime)) {
                i = k;
                break;
            }
        }


        for (int k = 0; k < arrayOfLogLines.size(); k++) {
            if (arrayOfLogLines.get(k).dateTime.isBefore(endDateTime))
                g = k;
        }

        if (i > g) {
            System.out.println("Nothing found, End time is before or equal Start time");
            System.exit(1);
        } else {

            for (int b = i; b <= g; b++)
                filteredList.add(arrayOfLogLines.get(b));

        }

        return filteredList;
    }

    public static ReadLogFile filter(String mask, String field, ReadLogFile logLine) {

        ReadLogFile filteredLine = new ReadLogFile();
        mask = convertMask(mask);
        Pattern p = Pattern.compile(mask);

        if (field.equals("name")) {

            if (!p.matcher(logLine.name).matches()) {
                filteredLine = logLine;

            }
        } else if (field.equals("message")) {

            if (!p.matcher(logLine.message).matches()) {
                filteredLine = logLine;

            }
        } else if (field.equals("level")) {
            filteredLine = levelFilter(mask, logLine);
        }

        return filteredLine;
    }

    @Override
    public int compareTo(ReadLogFile o) {

        int result = this.getDateTime().compareTo(o.getDateTime());

        return result;
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

}






