import java.io.*;
import java.time.LocalDateTime;
import java.util.*;


public class Main {
        public static void main(String[] args) throws IOException {



//           Scanner sc = new Scanner(new File("help.txt"));
//           while (sc.hasNext()){
//           System.out.println(sc.nextLine());}
//           sc.close();


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


            ReadLogFile RLF = new ReadLogFile(LocalDateTime.now(),"","","");
            RLF.read(status,logFileName);

            ArrayList<ReadLogFile> finSort = new ArrayList<>(RLF.getArrayLogLines());
            ArrayList<ReadLogFile> sortTemp = new ArrayList<>();

            Stat stat = new Stat("",1);


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
                         sortTemp.addAll(RLF.levelFilter(mask,finSort));
                         finSort.clear();

                         finSort.addAll(sortTemp);
                         sortTemp.clear();
                         break;

                     case "-m":
                         mask = args[i+1];
                         String field = "message";
                         sortTemp.addAll(RLF.filter(mask,field,finSort));
                         finSort.clear();

                         finSort.addAll(sortTemp);
                         sortTemp.clear();

                         break;

                     case "-t":
                         mask = args[i+1];
                         field = "name";
                         sortTemp.addAll(RLF.filter(mask,field,finSort));
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




             if ((!startDateTime.equals(""))&&(!endDateTime.equals(""))) {

                 finSort=RLF.durationBetween2Dates(startDateTime, endDateTime);
             }


             if ((!startDateTime.equals(""))&&(!periodTime.equals(""))) {

                 finSort=RLF.getLogLinesWithStartAndPeriod(periodTime, startDateTime);
             }

            if ((!endDateTime.equals(""))&&(!periodTime.equals(""))) {

                finSort= RLF.getLogLinesWithEndAndPeriod(periodTime, endDateTime);
            }


            if (finSort.equals(RLF.getArrayLogLines()))
                finSort.clear();


            Set<ReadLogFile> readLogFileSet = new TreeSet<>();
            readLogFileSet.addAll(finSort);


            for(ReadLogFile s : readLogFileSet)
                System.out.println(s.getDateTime() + " " + s.getLevel() + " " + "[" + s.getName() + "]" + " " + s.getMessage());



            boolean contains = Arrays.stream(args).anyMatch("-o"::equals);

                if (contains) {

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
