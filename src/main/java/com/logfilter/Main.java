package com.logfilter;

import java.io.*;
import java.util.*;

import static com.logfilter.ReadLogFile.read;
import static com.logfilter.Stat.showStat;

/**
 * This is a utility which allows to filter log lines and collect the statistics.
 *
 * @author Alexandrov Kirill
 * @version 0.2
 */

public class Main {
    public static void main(String[] args) throws IOException {


        String status = "";

        ReadLogFile readLogFiles = new ReadLogFile();

        ArrayList<ReadLogFile> finSort = new ArrayList<>(read(status, args));

        if (readLogFiles.getStat()==1){
            showStat(finSort);
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
