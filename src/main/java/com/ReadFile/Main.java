package com.ReadFile;

import com.ParseArgs.ParseArgs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static com.ReadFile.ReadLogFile.read;

/**
 * This is a utility which allows to filter log lines and collect the statistics.
 *
 * @author Alexandrov Kirill
 * @version 0.2
 */

public class Main {
    public static void main(String[] args) throws IOException {


        read(args);

        checkStat(args);


    }

    public static void checkStat(String[] args) throws FileNotFoundException {

        ParseArgs parseArgs = new ParseArgs();
        ArrayList<ParseArgs> argsList = parseArgs.parseArgs(args);

        for (ParseArgs s : argsList) {
            if (s.getField().equals("stat")) {
                Stat stat = new Stat();
                stat.showStat();

            }
        }

    }




}
