import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class ReadLogFile implements Comparable<ReadLogFile> {


    private LocalDateTime dateTime = LocalDateTime.now();
    private String level = "";
    private String name = "";
    private String message ="";
    private String numbersOfLinesByPercent = "";
    private List<ReadLogFile> arrayLogLines = new ArrayList<>();
    private int numberOfLogLines = 0;

    public ReadLogFile(LocalDateTime dateTime, String level, String name, String message) {
        this.dateTime = dateTime;
        this.level = level;
        this.name = name;
        this.message = message;
    }


    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public List<ReadLogFile> getArrayLogLines() {
        return arrayLogLines;
    }

    public String getNumbersOfLinesByPercent() {
        return numbersOfLinesByPercent;
    }
    public int getNumberOfLogLines() {
        return numberOfLogLines;
    }






    public void read(String strict,String logFileName) throws FileNotFoundException {

        String messageStr = "";

        Scanner sc = new Scanner(new File(logFileName));
        int strictCount=0;

        while (sc.hasNext()) {

             String Str = (sc.nextLine().replaceAll("[\\s]{2,}", " "));
             String[] subStr= Str.split(" ");

             LocalDateTime dateTimeTemp = LocalDateTime.now();

             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
             try {
                dateTimeTemp= (LocalDateTime.parse(subStr[0], formatter));
             }
             catch (Exception e) {

                 strictCount++;
                continue; }

             if ((!subStr[1].equals("TRACE"))&&(!subStr[1].equals("ERROR"))&&(!subStr[1].equals("DEBUG"))&&(!subStr[1].equals("INFO"))&&(!subStr[1].equals("WARN"))){
                 strictCount++;
                 continue;}


             if ((!subStr[2].contains("["))||(!subStr[3].contains("]"))||(subStr[3].length()>17)){
                 strictCount++;
                 continue;}





            if ((strictCount>0)&&(!strict.equals(""))){
                System.out.println("code 1");
                break;
            }

             for (int g = 4; g != subStr.length; g++) {

                messageStr += subStr[g];
                messageStr += " ";
             }


             arrayLogLines.add(new ReadLogFile(dateTimeTemp,subStr[1],subStr[3].substring(0, subStr[3].length() - 1),messageStr));
             messageStr = "";

            if ((numberOfLogLines>0)&&(arrayLogLines.get(numberOfLogLines-1).getDateTime().isAfter(arrayLogLines.get(numberOfLogLines).getDateTime()))){

                strictCount++;
                continue;
            }


             numberOfLogLines++;
        }
        sc.close();

    }


    public void durationBetween2Dates(String startDateTimeString, String endDateTimeString) {



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeString, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeString, formatter);

        linesBetween2Dates(startDateTime,endDateTime);

    }

    public void getLogLinesWithStartAndPeriod(String durationString, String startDateTimeString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeString, formatter);

        LocalDateTime endDateTime = LocalDateTime.now();

        if (durationString.contains("W")) {
            Period p = Period.parse(durationString);
            endDateTime = startDateTime.plusDays(p.getDays());

        }

        else {

            Duration duration = Duration.parse(durationString);
            endDateTime = startDateTime.plusNanos(duration.toNanos());
        }

        linesBetween2Dates(startDateTime,endDateTime);

    }


    public void getLogLinesWithEndAndPeriod(String durationString, String endDateTimeString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeString, formatter);

        LocalDateTime startDateTime = LocalDateTime.now();

        if (durationString.contains("W")) {
            Period p = Period.parse(durationString);
            startDateTime = endDateTime.minusDays(p.getDays());

        }

        else {

            Duration duration = Duration.parse(durationString);
            startDateTime = endDateTime.minusNanos(duration.toNanos());
        }

        linesBetween2Dates(startDateTime,endDateTime);

    }

    public void linesBetween2Dates(LocalDateTime startDateTime, LocalDateTime endDateTime){

        int g=0;
        int i = 0;


        System.out.println(startDateTime);
        System.out.println(endDateTime);


        for (int k = 0; k< arrayLogLines.size();k++) {
            if  (arrayLogLines.get(k).dateTime.isAfter(startDateTime)) {
                i = k;
                System.out.println("i " + i);
                break;
            }
        }




        for (int k = 0; k< arrayLogLines.size();k++) {

            if (arrayLogLines.get(k).dateTime.isBefore(endDateTime)){
                g=k;
                System.out.println("g " + g);
            }
        }

        if (i>=g)
            System.out.println("Nothing found, End time is before or equal Start time");

        else {

            for (int b = i ; b< arrayLogLines.size();b++) {

                System.out.println(arrayLogLines.get(b).dateTime+ " " +arrayLogLines.get(b).getLevel()+ " " +"["+arrayLogLines.get(b).getName()+"]" + " " +arrayLogLines.get(b).getMessage());
            }

        }

    }

    public ArrayList<ReadLogFile> filter(String mask, String field,ArrayList<ReadLogFile> arrayLogLinesFiltered) {

        ArrayList<ReadLogFile> filteredList = new ArrayList<>();
        WorkWithArgs checkMask = new WorkWithArgs();



        mask = checkMask.convertMask(mask);
        Pattern p = Pattern.compile(mask);

        if (field.equals("name")) {

            for (int i = 0; i < arrayLogLinesFiltered.size(); i++) {

                if (!p.matcher(arrayLogLinesFiltered.get(i).name).matches()) {

                    filteredList.add(arrayLogLinesFiltered.get(i));

                }
            }
        }
        else if (field.equals("message")){

            for (int i = 0; i < arrayLogLinesFiltered.size(); i++) {

                if (!p.matcher(arrayLogLinesFiltered.get(i).message).matches()) {

                    filteredList.add(arrayLogLinesFiltered.get(i));

                }
            }

        }


        return filteredList;
    }

    @Override
    public int compareTo(ReadLogFile o) {
        // return o.getDateTime().isBefore(getDateTime()) ? 1 : 0;

        int result = this.getDateTime().compareTo(o.getDateTime());

        return result;
    }

//    public ArrayList<String> levelFilter(String mask, ArrayList<String> arrayOfLogLines ) {
//
//        ArrayList<String> filteredLevels = new ArrayList<>();
//        WorkWithArgs checkMask = new WorkWithArgs();
//        String[] maskArray = mask.split(",");
//
//
//
//        for(String m : maskArray) {
//
//            m=checkMask.convertMask(m);
//            Pattern p = Pattern.compile(m);
//
//            for (int i = 0; i < levels.size(); i++) {
//                if (p.matcher(levels.get(i)).matches())
//                    filteredLevels.add(arrayOfLogLines.get(i));
//            }
//        }
//
//        filteredLevels.sort(String::compareTo);
//
//
//        return filteredLevels;
//    }

}






