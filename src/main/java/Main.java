import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
        public static void main(String[] args) throws IOException {


//            Scanner sc = new Scanner(new File("help.txt"));
////            while (sc.hasNext()){
////                System.out.println(sc.nextLine());}
////            sc.close();



            String startDateTime = "";
            String endDateTime = "";
            String periodTime = "";
            String mask = "";
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

            ReadLogFile RLF = new ReadLogFile();
            RLF.read(strict,logFileName);


             for (int i=0; i <args.length; i++){

                 switch (args[i]){

                     case "-s":
                         startDateTime= args[i+1];
                         break;

                     case "-o":
                         outputFileName= args[i+1];
                         break;


                     case "-e":
                         endDateTime = args[i+1];
                         break;
                     case "-m":
                         mask = args[i+1];
                         WorkWithArgs maskCheck = new WorkWithArgs();
                         mask= maskCheck.convertMask(mask);


                         break;

                     case "-p":
                         periodTime = args[i+1];
                         break;


                     case "-t":
                         mask = args[i+1];
                         maskCheck = new WorkWithArgs();
                         mask= maskCheck.convertMask(mask);
                         RLF.namesFilter(mask);
                         break;
                     case "-l":
                         mask = args[i+1];
                         maskCheck = new WorkWithArgs();
                         String[] maskArray = maskCheck.splitMask(mask);
                         RLF.levelFilter(maskArray);
                         break;
//                     case "-f":

                     case "-c" :
                         RLF.showStat();
                         break;
                     case "--stats":
                         RLF.showStat();
                         break;

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





            boolean contains = Arrays.stream(args).anyMatch("-o"::equals);

                if (contains) {
                    FileWriter nFile = new FileWriter(outputFileName);
                    nFile.write("asd");
                    nFile.close();
                }


        }

    }
