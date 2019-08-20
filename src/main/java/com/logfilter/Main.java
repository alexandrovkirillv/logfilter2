package com.logfilter;

import com.ParseArgs.ParseArgs;

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

        ArrayList<ReadLogFile> finSort = new ArrayList<>(read(status, args));

        checkStat(finSort,args);

        showResult(finSort);

        if (finSort.isEmpty()) {
            System.out.println("Nothing found");
        }



//        if ((readLogFiles.getStrictCount() > 0) && (status.equals("--strict"))) {
//            System.exit(1);
//        }




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

    public static void checkStat ( ArrayList<ReadLogFile> finSort, String[] args) throws FileNotFoundException {

        ParseArgs parseArgs = new ParseArgs();
        ArrayList<ParseArgs> argsList = parseArgs.parseArgs(args);

        if (argsList.get(0).getField().equals("stat")) {
            Stat stat = new Stat();
            stat.showStat(finSort);
            finSort.clear();
        }




    }

    public static void showResult (ArrayList<ReadLogFile> finSort){

        for (ReadLogFile s : finSort) {
            System.out.println(s.getDateTime() + " " + s.getLevel() + " " + "[" + s.getName() + "]" + " " + s.getMessage());
        }

    }

}
