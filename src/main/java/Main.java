import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
            String strict= "";
            String logFileName = "";






            for(String s : args){
                if(s.contains(".log")){

                    logFileName=s;
                }
                else if (s.equals("--strict")){
                    strict=s;
                }

            }

            ReadLogFile RLF = new ReadLogFile(LocalDateTime.now(),"","","");
            RLF.read(strict,logFileName);

            ArrayList<ReadLogFile> finSort = new ArrayList<>(RLF.getArrayLogLines());
            ArrayList<ReadLogFile> sortTemp = new ArrayList<>();

           // Stat stat = new Stat("",1);


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

//                     case "-l":
//                         String mask = args[i+1];
//                         sortTemp.addAll(RLF.levelFilter(mask,finSort));
//                         finSort.clear();
//                         finSort.addAll(sortTemp);
//                         break;

                     case "-m":
                         String mask = args[i+1];
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

//                     case "-f":

//                     case "-c" :
//                         stat.showStat(logFileName);
//                         break;
//                     case "--stats":
//                         stat.showStat(logFileName);
//                         break;

                 }

             }




             if ((!startDateTime.equals(""))&&(!endDateTime.equals(""))) {

                 RLF.durationBetween2Dates(startDateTime, endDateTime);
             }


             if ((!startDateTime.equals(""))&&(!periodTime.equals(""))) {

                 RLF.getLogLinesWithStartAndPeriod(periodTime, startDateTime);
             }

            if ((!endDateTime.equals(""))&&(!periodTime.equals(""))) {

                RLF.getLogLinesWithEndAndPeriod(periodTime, endDateTime);
            }




            Set<ReadLogFile> readLogFileSet = new TreeSet<>();
            readLogFileSet.addAll(finSort);



          for(ReadLogFile s : readLogFileSet)
                 System.out.println(s.getDateTime() + " " + s.getLevel() + " " + "[" + s.getName() + "]" + " " + s.getMessage());



            boolean contains = Arrays.stream(args).anyMatch("-o"::equals);

                if (contains) {
                    FileWriter nFile = new FileWriter(outputFileName);
                    nFile.write("asd");
                    nFile.close();
                }


        }

    }
