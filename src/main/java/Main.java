import java.io.*;
import java.time.LocalDateTime;
import java.util.*;


public class Main {
        public static void main(String[] args) throws IOException {



           Scanner sc = new Scanner(new File("help.txt"));
           while (sc.hasNext()){
           System.out.println(sc.nextLine());}
           sc.close();


            String startDateTime = "";
            String endDateTime = "";
            String periodTime = "";
            String outputFileName = "";
            String status= "";
            String logFileName = "";

            for(String s : args){
                if(s.contains(".log")){

                    logFileName=s;
                }
                else if (s.equals("--strict")){
                    status=s;
                }

                else if ((s.equals("-f"))||(s.equals("--follow"))){

                    status=s;
                }
            }


            ReadLogFile RLF = new ReadLogFile();
            ReadLogFile.read(status,logFileName);

            ArrayList<ReadLogFile> finSort = new ArrayList<>(RLF.getArrayLogLines());


            ArrayList<ReadLogFile> sortTemp = new ArrayList<>();

            Stat stat = new Stat();


             for (int i=0; i <args.length; i++){

                 switch (args[i]){

                     case "-s":
                         startDateTime= args[i+1];
                         break;

                     case "-p":
                         periodTime = args[i+1];
                         break;

                     case "-e":
                         endDateTime = args[i+1];
                         break;

                     case "-o":
                         outputFileName= args[i+1];
                         break;

                     case "-l":
                         String mask = args[i+1];
                         sortTemp.addAll(ReadLogFile.levelFilter(mask,finSort));
                         finSort.clear();

                         finSort.addAll(sortTemp);
                         sortTemp.clear();
                         break;

                     case "-m":
                         mask = args[i+1];
                         String field = "message";
                         sortTemp.addAll(ReadLogFile.filter(mask,field,finSort));
                         finSort.clear();

                         finSort.addAll(sortTemp);
                         sortTemp.clear();

                         break;

                     case "-t":
                         mask = args[i+1];
                         field = "name";
                         sortTemp.addAll(ReadLogFile.filter(mask,field,finSort));
                         finSort.clear();

                         finSort.addAll(sortTemp);
                         sortTemp.clear();
                         break;

                     case "-c" :
                         stat.showStat(finSort);
                         break;
                     case "--stats":
                         stat.showStat(finSort);
                         break;

                 }

             }




             if ((!startDateTime.equals(""))&&(!endDateTime.equals("")))
                 finSort = RLF.durationBetween2Dates(startDateTime, endDateTime,finSort);


             if ((!startDateTime.equals(""))&&(!periodTime.equals("")))
                 finSort = RLF.logLinesWithStartAndPeriod(periodTime, startDateTime,finSort);

            if ((!endDateTime.equals(""))&&(!periodTime.equals("")))
                finSort = RLF.logLinesWithEndAndPeriod(periodTime, endDateTime,finSort);




            if (finSort.isEmpty())
                System.out.println("Nothing found");

            if ((Arrays.asList(args).contains("-c"))||(Arrays.asList(args).contains("--stats")))
                finSort.clear();

            Set<ReadLogFile> readLogFileSet = new TreeSet<>();
            readLogFileSet.addAll(finSort);



            for(ReadLogFile s : readLogFileSet)
                System.out.println(s.getDateTime() + " " + s.getLevel() + " " + "[" + s.getName() + "]" + " " + s.getMessage());


                if (Arrays.asList(args).contains("-o")) {

                    try(FileWriter writer = new FileWriter(outputFileName))
                    {
                        for(ReadLogFile s : readLogFileSet){
                        writer.write(s.getDateTime() + " " + s.getLevel() + " " + "[" + s.getName() + "]" + " " + s.getMessage());
                        writer.write('\n');
                        }
                        writer.flush();
                    }
                    catch(IOException ex){

                        System.out.println(ex.getMessage());
                    }
                }


        }

    }
