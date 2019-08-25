package com.ReadFile;

import com.ParseArgs.ParseArgs;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ReadFile.Stat.calcStat;

/**
 * @author Alexandrov Kirill
 * @version 0.2
 * @return method read() is reading file and returns log line with fields, witch already filtered (localdate, level, name, message);
 */

public class ReadLogFile implements Comparable<ReadLogFile>, Serializable {

    private LocalDateTime dateTime = LocalDateTime.now();
    private String level = "";
    private String name = "";
    private String message = "";
    private static int strictCount = 0;
    private static PrintWriter writer;


    public ReadLogFile() {
    }

    public ReadLogFile(LocalDateTime dateTime, String level, String name, String message) {
        this.dateTime = dateTime;
        this.level = level;
        this.name = name;
        this.message = message;
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


    public static void read(String[] args) throws FileNotFoundException {


        ParseArgs parseArgs = new ParseArgs();

        ArrayList<ParseArgs> argsList = parseArgs.parseArgs(args);
        String logFileName = parseArgs.getLogFileName();
        String outFileName = parseArgs.getOutputFileName();
        File f = new File("./src/main/resources/" + logFileName);
        ReadLogFile tempLog = new ReadLogFile();
        int numberOfFilters = argsList.size();
        int filterCounter = 0;
        int status = parseArgs.getStatus();
        int stats = parseArgs.getStats();

        if (!outFileName.equals("")) {
            try {
                writer = new PrintWriter("./src/main/resources/" + outFileName);
            } catch (FileNotFoundException e) {

            }
        }


        try {
            LineIterator it = FileUtils.lineIterator(f, "UTF-8");

            while (it.hasNext()) {

                String str = (it.nextLine().replaceAll("[\\s]{2,}", " "));


                for (ParseArgs p : argsList) {
                    tempLog = p.getArg().filter(p.getMask(), p.getField(), getData(str), parseArgs.getStartDateTimeString(), parseArgs.getEndDateTimeString(), parseArgs.getPeriodTime());

                    if (!tempLog.getLevel().equals("")) {

                        filterCounter++;

                    }
                }


                if ((filterCounter == numberOfFilters) & (filterCounter != 0) & (stats==0)) {
                    showLine(tempLog);
                    checkOutFile(outFileName, tempLog);
                }
                filterCounter = 0;

                if ((strictCount > 0) & (status == 1)) {
                    System.exit(1);
                }

            }
            it.close();


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void checkOutFile(String outputFileName, ReadLogFile logLine) {

        if (!outputFileName.equals("")) {


            writer.append(logLine.getDateTime() + " " + logLine.getLevel() + " " + "[" + logLine.getName() + "]" + " " + logLine.getMessage());
            writer.append('\n');

            writer.flush();

        }

    }

    public static void follow(int status) {

        if (status == 2) {
            String str;

            Scanner in = new Scanner(System.in);
            System.out.println("Input log line(print 'SIGINT' for result): ");
            while (!(str = in.nextLine()).equals("SIGINT")) {

                str = str.replaceAll("[\\s]{2,}", " ");

                getData(str);

            }
            in.close();
        }

    }

    public static void showLine(ReadLogFile logLine) {



            System.out.println(logLine.getDateTime() + " " + logLine.getLevel() + " " + "[" + logLine.getName() + "]" + " " + logLine.getMessage());

    }

    public static ReadLogFile getData(String Str) {

        strictCount = 0;

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

            calcStat(logLine);
        }


        return logLine;

    }

    @Override
    public int compareTo(ReadLogFile o) {

        int result = this.getDateTime().compareTo(o.getDateTime());

        return result;
    }


}






