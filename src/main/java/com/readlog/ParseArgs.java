package com.readlog;

import com.logfilter.Stat;
import java.util.ArrayList;

public class ParseArgs extends Log {

    private String logFileName;
    private String status;
    private String mask;
    private String field;
    private ArrayList<ParseArgs> arrayList = new ArrayList<>();
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
                    arrayList.add(new ParseArgs(namesLog,args[i+1],"name"));

                    break;

                case "--thread":
                    namesLog = new NamesLog();
                    arrayList.add(new ParseArgs(namesLog,args[i+1],"name"));
                    break;

                case "-m":

                    MessageLog messageLog = new MessageLog();
                    arrayList.add(new ParseArgs(messageLog,args[i+1],"message"));
                    break;

                case "--message":

                    messageLog = new MessageLog();
                    arrayList.add(new ParseArgs(messageLog,args[i+1],"message"));
                    break;

                case "-c":

                    Log log = new Log();
                    arrayList.add(new ParseArgs(log,"","stat"));
                    break;

                case "--stats":

                    log = new Log();
                    arrayList.add(new ParseArgs(log,"","stat"));
                    break;

                case "-l":

                    LevelLog levelLog = new LevelLog();
                    arrayList.add(new ParseArgs(levelLog,args[i+1],"level"));
                    break;

                case "--level":

                    levelLog = new LevelLog();
                    arrayList.add(new ParseArgs(levelLog,args[i+1],"level"));
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

        return arrayList;
    }

}
