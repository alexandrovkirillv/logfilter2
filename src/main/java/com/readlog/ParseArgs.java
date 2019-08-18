package com.readlog;

import com.logfilter.Stat;
import java.util.ArrayList;
import java.util.List;

public class ParseArgs extends Log {

    private String logFileName;
    private String status;
    private String mask;
    private String field;
    private ArrayList<ParseArgs> finalArgs = new ArrayList<>();
    private Log log =new Log();

    public Log getLogs() {
        return log;
    }

    public String getMask() {
        return mask;
    }

    public String getField() {
        return field;
    }

    public ParseArgs(){

    }

    public ParseArgs (Log log, String mask, String field){

        this.mask=mask;
        this.field=field;
        this.log=log;


    }

    public String getStatus() {
        return status;
    }


    public String getLogFileName() {
        return logFileName;
    }

    public ArrayList<ParseArgs> parseArgs(String[] args) {

        Stat stat = new Stat();

        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {

                case "-t":

                    NamesLog namesLog = new NamesLog();
                    finalArgs.add(new ParseArgs(namesLog,args[i+1],"name"));

                    break;

                case "--thread":
                    namesLog = new NamesLog();
                    finalArgs.add(new ParseArgs(namesLog,args[i+1],"name"));
                    break;

                case "-m":

                    MessageLog messageLog = new MessageLog();
                    finalArgs.add(new ParseArgs(messageLog,args[i+1],"message"));
                    break;

                case "--message":

                    messageLog = new MessageLog();
                    finalArgs.add(new ParseArgs(messageLog,args[i+1],"message"));
                    break;

                case "-c":
                  //  stat.showStat(finSort);
                    break;

                case "--stats":
                  //  stat.showStat(finSort);
                    break;

                  /*  case ("-s"):
                        startDateTime = args[i + 1];
                        break;
                    case "--start":
                        startDateTime = args[i + 1];
                        break;

                    case "-p":
                        periodTime = args[i + 1];
                        break;
                    case "-period":
                        periodTime = args[i + 1];
                        break;

                    case "-e":
                        endDateTime = args[i + 1];
                        break;
                    case "--end":
                        endDateTime = args[i + 1];
                        break;

                    case "-o":
                        outputFileName = args[i + 1];
                        break;
                    case "--out":
                        outputFileName = args[i + 1];
                        break;

                    case "-l":
                        String mask = args[i + 1];
                        sortTemp.addAll(ReadLogFile.levelFilter(mask, finSort));
                        finSort.clear();

                        finSort.addAll(sortTemp);
                        sortTemp.clear();
                        break;
                    case "--level":
                        mask = args[i + 1];
                        sortTemp.addAll(ReadLogFile.levelFilter(mask, finSort));
                        finSort.clear();

                        finSort.addAll(sortTemp);
                        sortTemp.clear();
                        break;

                    case "-m":
                        mask = args[i + 1];
                        String field = "message";
                        sortTemp.addAll(ReadLogFile.filter(mask, field, finSort));
                        finSort.clear();

                        finSort.addAll(sortTemp);
                        sortTemp.clear();
                        break;

                    case "--message":
                        mask = args[i + 1];
                        field = "message";
                        sortTemp.addAll(ReadLogFile.filter(mask, field, finSort));
                        finSort.clear();

                        finSort.addAll(sortTemp);
                        sortTemp.clear();

                        break;

                    case "-t":
                        mask = args[i + 1];
                        field = "name";
                        sortTemp.addAll(ReadLogFile.filter(mask, field, finSort));
                        finSort.clear();

                        finSort.addAll(sortTemp);
                        sortTemp.clear();
                        break;
                    case "--thread":
                        mask = args[i + 1];
                        field = "name";
                        sortTemp.addAll(ReadLogFile.filter(mask, field, finSort));
                        finSort.clear();

                        finSort.addAll(sortTemp);
                        sortTemp.clear();
                        break;


                    */

            }

        }


        for (String s : args) {
            if (s.contains(".log")) {
                logFileName = s;
            }
        }

        for (String s : args) {
            if (s.equals("--strict")) {

                status = s;
            } else if ((s.equals("-f")) || (s.equals("--follow"))) {

                status = s;
            }
        }

        return finalArgs;
    }

}
