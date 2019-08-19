package com.ParseArgs;

import com.logfilter.Stat;
import java.util.ArrayList;

public class ParseArgs extends Arg {

    private String logFileName;
    private String status;
    private String mask;
    private String field;
    private ArrayList<ParseArgs> arrayList = new ArrayList<>();
    private Arg arg =new Arg();

    public Arg getLogs() {
        return arg;
    }

    public String getMask() {
        return mask;
    }

    public String getField() {
        return field;
    }

    public ParseArgs(){

    }

    public ParseArgs (Arg arg, String mask, String field){

        this.mask=mask;
        this.field=field;
        this.arg=arg;


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

                    NameArg nameArg = new NameArg();
                    arrayList.add(new ParseArgs(nameArg,args[i+1],"name"));

                    break;

                case "--thread":
                    nameArg = new NameArg();
                    arrayList.add(new ParseArgs(nameArg,args[i+1],"name"));
                    break;

                case "-m":

                    MessageArg messageArg = new MessageArg();
                    arrayList.add(new ParseArgs(messageArg,args[i+1],"message"));
                    break;

                case "--message":

                    messageArg = new MessageArg();
                    arrayList.add(new ParseArgs(messageArg,args[i+1],"message"));
                    break;

                case "-c":

                    Arg log = new Arg();
                    arrayList.add(new ParseArgs(arg,"","stat"));
                    break;

                case "--stats":

                    arg = new Arg();
                    arrayList.add(new ParseArgs(arg,"","stat"));
                    break;

                case "-l":

                    LevelArg levelArg = new LevelArg();
                    arrayList.add(new ParseArgs(levelArg,args[i+1],"level"));
                    break;

                case "--level":

                    levelArg = new LevelArg();
                    arrayList.add(new ParseArgs(levelArg,args[i+1],"level"));
                    break;

                case ("-s"):
                    StartDateArg startDateTime = new StartDateArg();
                    arrayList.add(new ParseArgs(startDateTime,args[i+1],"start"));
                    break;
                case "--start":
                    startDateTime = new StartDateArg();
                    arrayList.add(new ParseArgs(startDateTime,args[i+1],"start"));
                    break;

                case "-e":
                    EndDateArg endDateTime = new EndDateArg();
                    arrayList.add(new ParseArgs(endDateTime, args[i+1],"end" ));
                    break;
                case "--end":
                    endDateTime = new EndDateArg();
                    arrayList.add(new ParseArgs(endDateTime, args[i+1],"end" ));
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
