package com.logfilter;

import com.ParseArgs.ParseArgs;
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

        if (argsList.get(0).getField().equals("stat")) {
            stat = 1;
        }

        try {
            LineIterator it = FileUtils.lineIterator(f, "UTF-8");

            while (it.hasNext()) {

                String str = (it.nextLine().replaceAll("[\\s]{2,}", " "));

                for (ParseArgs p : argsList) {
                    tempLog = p.getArg().filter(p.getMask(), p.getField(), getData(str, status), parseArgs.getStartDateTimeString(),parseArgs.getEndDateTimeString(),parseArgs.getPeriodTime());

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

    @Override
    public int compareTo(ReadLogFile o) {

        int result = this.getDateTime().compareTo(o.getDateTime());

        return result;
    }



}






