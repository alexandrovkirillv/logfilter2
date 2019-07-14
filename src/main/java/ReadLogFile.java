import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 *
 * @return method read() is reading file and returns Set of objects with  fields (localdate, level, name, message);
 *         methods durationBetween2Dates(), logLinesWithStartAndPeriod(),
 *         logLinesWithEndAndPeriod() returns filtered by time period log lines;
 *         methods filter() and linesFilter() returns log filtered lines by level, name, message.
 *
 * @author Alexandrov Kirill

 * @version 0.2

 */

public class ReadLogFile implements Comparable<ReadLogFile>, Serializable {

    private LocalDateTime dateTime = LocalDateTime.now();
    private String level = "";
    private String name = "";
    private String message ="";
    private static int strictCount=0;
    private static Set<ReadLogFile> arrayLogLines = new TreeSet<>();

    public ReadLogFile() {
    }

    public ReadLogFile(LocalDateTime dateTime, String level, String name, String message) {
        this.dateTime = dateTime;
        this.level = level;
        this.name = name;
        this.message = message;
    }

    public static int getStrictCount() {
        return strictCount;
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

    public Set<ReadLogFile> getArrayLogLines() {
        return arrayLogLines;
    }



    public static Set<ReadLogFile> read(String status,String logFileName)  {

        String SIGINT = "SIGINT";


        try {
            Scanner sc = new Scanner(new File(logFileName));

            while (sc.hasNext()) {

                String str = (sc.nextLine().replaceAll("[\\s]{2,}", " "));
                getData(str, status);

            }
            sc.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
            System.exit(0);
        }

        if ((status.equals("-f"))||(status.equals("--follow"))) {
            String str;

            Scanner in = new Scanner(System.in);
            System.out.println("Input log line(print 'SIGINT' for result): ");
            while (!(str = in.nextLine()).equals(SIGINT)) {

                str = str.replaceAll("[\\s]{2,}", " ");

                getData(str, status);


            }
            in.close();
        }


        return arrayLogLines;
    }

    public static Set<ReadLogFile> getData (String Str, String status){

        String[] subStr= Str.split(" ");
        String messageStr = "";

        LocalDateTime dateTimeTemp = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            dateTimeTemp= (LocalDateTime.parse(subStr[0], formatter));
        }
        catch (Exception e) {

            strictCount++;

             }

        try{
            if ((!subStr[1].equals("TRACE"))&&(!subStr[1].equals("ERROR"))&&(!subStr[1].equals("DEBUG"))&&(!subStr[1].equals("INFO"))&&(!subStr[1].equals("WARN"))){
                strictCount++;
            }
        }catch (java.lang.ArrayIndexOutOfBoundsException e){

        }



        try {
            if ((!subStr[2].contains("["))||(!subStr[3].contains("]"))||(subStr[3].length()>17))
                strictCount++;
        }catch (java.lang.ArrayIndexOutOfBoundsException e){

        }
        try {
            for (int g = 4; g != subStr.length; g++) {

                messageStr += subStr[g];
                messageStr += " ";
            }
        }catch (java.lang.ArrayIndexOutOfBoundsException e){


        }



        if (strictCount==0)
            arrayLogLines.add(new ReadLogFile(dateTimeTemp,subStr[1],subStr[3].substring(0, subStr[3].length() - 1),messageStr));

        if (!status.equals("--strict"))
            strictCount=0;



        Set<ReadLogFile> arrayLogLinesNew = new TreeSet<ReadLogFile>(arrayLogLines);

        return arrayLogLinesNew;

    }


    public static ArrayList<ReadLogFile> durationBetween2Dates(String startDateTimeString, String endDateTimeString,ArrayList<ReadLogFile> arrayOfLogLines) {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeString, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeString, formatter);
        return linesBetween2Dates(startDateTime,endDateTime,arrayOfLogLines);

    }

    public static ArrayList<ReadLogFile> logLinesWithStartAndPeriod(String durationString, String startDateTimeString,ArrayList<ReadLogFile> arrayOfLogLines) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeString, formatter);

        LocalDateTime endDateTime = LocalDateTime.now();

        if (durationString.contains("W")) {

            try {
                Period p = Period.parse(durationString);
                endDateTime = startDateTime.plusDays(p.getDays());
            } catch (Exception e){
                System.out.println("Wrong period format");
                System.exit(0);
            }
        }

        else {

            try {
                Duration duration = Duration.parse(durationString);
                endDateTime = startDateTime.plusNanos(duration.toNanos());
            }catch (Exception e){
                System.out.println("Wrong period format");
                System.exit(0);
            }
        }

        return linesBetween2Dates(startDateTime,endDateTime,arrayOfLogLines);

    }


    public static ArrayList<ReadLogFile> logLinesWithEndAndPeriod(String durationString, String endDateTimeString, ArrayList<ReadLogFile> arrayOfLogLines) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime endDateTime = LocalDateTime.now();

        try{
           endDateTime = LocalDateTime.parse(endDateTimeString, formatter);
        }
        catch (java.time.format.DateTimeParseException e){
            System.out.println("Wrong DateTime format");
            System.exit(0);

        }


        LocalDateTime startDateTime = LocalDateTime.now();

        if (durationString.contains("W")) {
            try {
                Period p = Period.parse(durationString);
                startDateTime = endDateTime.minusDays(p.getDays());
            }
            catch (Exception e)
            {
                System.out.println("Wrong period format");
                System.exit(0);
            }

        }

        else {
                    try {
                        Duration duration = Duration.parse(durationString);
                        startDateTime = endDateTime.minusNanos(duration.toNanos());
                    }

                    catch (Exception e){
                        System.out.println("Wrong period format");
                        System.exit(0);
                    }
        }

        return linesBetween2Dates(startDateTime,endDateTime,arrayOfLogLines);

    }

    public static ArrayList<ReadLogFile> linesBetween2Dates(LocalDateTime startDateTime, LocalDateTime endDateTime, ArrayList<ReadLogFile> arrayOfLogLines){

        ArrayList<ReadLogFile> filteredList = new ArrayList<>();
        int g=0;
        int i = 0;

        for (int k = 0; k< arrayOfLogLines.size();k++) {
            if  (arrayOfLogLines.get(k).dateTime.isAfter(startDateTime)) {
                i = k;
                break;
            }
        }


        for (int k = 0; k< arrayOfLogLines.size();k++) {
            if (arrayOfLogLines.get(k).dateTime.isBefore(endDateTime))
                g=k;
        }

        if (i>g){
            System.out.println("Nothing found, End time is before or equal Start time");
            System.exit(1);}

        else {

            for (int b = i; b<=g; b++)
                filteredList.add(arrayOfLogLines.get(b));

        }

        return filteredList;
    }

    public static ArrayList<ReadLogFile> filter(String mask, String field, ArrayList<ReadLogFile> arrayOfLogLinesFiltered) {

        ArrayList<ReadLogFile> filteredList = new ArrayList<>();

        mask = convertMask(mask);
        Pattern p = Pattern.compile(mask);

        if (field.equals("name")) {

            for (int i = 0; i < arrayOfLogLinesFiltered.size(); i++) {

                if (!p.matcher(arrayOfLogLinesFiltered.get(i).name).matches()) {

                    filteredList.add(arrayOfLogLinesFiltered.get(i));

                }
            }
        }
        else if (field.equals("message")){

            for (int i = 0; i < arrayOfLogLinesFiltered.size(); i++) {

                if (!p.matcher(arrayOfLogLinesFiltered.get(i).message).matches()) {

                    filteredList.add(arrayOfLogLinesFiltered.get(i));

                }
            }

        }

        return filteredList;
    }

    @Override
    public int compareTo(ReadLogFile o) {

        int result = this.getDateTime().compareTo(o.getDateTime());

        return result;
    }

    public static ArrayList<ReadLogFile> levelFilter(String mask, ArrayList<ReadLogFile> arrayOfLogLinesFiltered) {


        String[] maskArray = mask.split(",");
        List<ReadLogFile> toRemove = new ArrayList<>();

            for (ReadLogFile logLine : arrayOfLogLinesFiltered){
                for(String m : maskArray) {
                    m = convertMask(m);
                    Pattern p = Pattern.compile(m);

                    if (p.matcher(logLine.getLevel()).matches())
                        toRemove.add(logLine);
                }

            }
            arrayOfLogLinesFiltered.removeAll(toRemove);


        return arrayOfLogLinesFiltered;
    }

    public static String convertMask(String mask){

        ArrayList<Character> charsA = new ArrayList<Character>();
        for (char c : mask.toCharArray()) {
            charsA.add(c);
        }

        if ((charsA.get(charsA.size()-1).equals('*'))||(charsA.get(0).equals('*'))){
            mask = mask.replaceAll("[*]", "\\.\\*");
        }

        else if((charsA.get(charsA.size()-1).equals('?'))||(charsA.get(0).equals('?'))){
            mask = mask.replaceAll("[?]", "\\.\\?");
        }


        return mask;

    }

}






