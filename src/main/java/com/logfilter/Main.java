package com.logfilter;

import com.readlog.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import static com.logfilter.ReadLogFile.read;

/**
 * This is a utility which allows to filter log lines and collect the statistics.
 *
 * @author Alexandrov Kirill
 * @version 0.2
 */

public class Main {
    public static void main(String[] args) throws IOException {


        String startDateTime = "";
        String endDateTime = "";
        String periodTime = "";
        String outputFileName = "";
        String status = "";


        ParseArgs parseArgs = new ParseArgs();

        ArrayList<ParseArgs> argsList = parseArgs.parseArgs(args);
        String logFileName = parseArgs.getLogFileName();

        ReadLogFile readLogFiles = new ReadLogFile();

        read(status, logFileName);

        ArrayList<ReadLogFile> finSort = new ArrayList<>(readLogFiles.getArrayLogLines());
        ArrayList<ReadLogFile> sortTemp = new ArrayList<>();
        sortTemp.addAll(finSort);

        System.out.println("mask is " + argsList.get(0).getMask() + ", field is " + argsList.get(0).getField());

        for (int i = 0; i < argsList.size(); i++) {

            finSort.clear();
            finSort.addAll(argsList.get(i).getLogs().filter(argsList.get(i).getMask(), argsList.get(i).getField(), sortTemp));

            for (ReadLogFile s : finSort) {
                System.out.println(s.getDateTime() + " " + s.getLevel() + " " + "[" + s.getName() + "]" + " " + s.getMessage());
            }
            System.out.println("------");

            sortTemp.clear();
            sortTemp.addAll(finSort);

        }


        if ((!startDateTime.equals("")) && (!endDateTime.equals(""))) {
            finSort = readLogFiles.durationBetween2Dates(startDateTime, endDateTime, finSort);
        }

        if ((!startDateTime.equals("")) && (!periodTime.equals(""))) {
            finSort = readLogFiles.logLinesWithStartAndPeriod(periodTime, startDateTime, finSort);
        }
        if ((!endDateTime.equals("")) && (!periodTime.equals(""))) {
            finSort = readLogFiles.logLinesWithEndAndPeriod(periodTime, endDateTime, finSort);
        }


        if (finSort.isEmpty()) {
            System.out.println("Nothing found");
        }

        if ((Arrays.asList(args).contains("-c")) || (Arrays.asList(args).contains("--stats"))) {
            finSort.clear();
        }

        for (String s : args) {
            if ((s.contains("-h")) || (s.contains("--help"))) {

                Scanner sc = new Scanner(new File("help.txt"));
                while (sc.hasNext())
                    System.out.println(sc.nextLine());
                sc.close();
            }
        }


        // Set<ReadLogFile> readLogFileSet = new TreeSet<>();
        //  readLogFileSet.addAll(finSort);


        for (ReadLogFile s : finSort) {
            System.out.println(s.getDateTime() + " " + s.getLevel() + " " + "[" + s.getName() + "]" + " " + s.getMessage());
        }

        if ((readLogFiles.getStrictCount() > 0) && (status.equals("--strict"))) {
            System.exit(1);
        }


//        if (Arrays.asList(args).contains("-o")) {
//
//            try (PrintWriter writer = new PrintWriter(outputFileName)) {
//                for (ReadLogFile s : readLogFileSet) {
//                    writer.write(s.getDateTime() + " " + s.getLevel() + " " + "[" + s.getName() + "]" + " " + s.getMessage());
//                    writer.write('\n');
//                }
//                writer.flush();
//            } catch (IOException ex) {
//
//                System.out.println(ex.getMessage());
//            }
//        }


    }

}
