import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class ReadLogFile implements Comparable<ReadLogFile>, Serializable {

    public ReadLogFile() {
    }

    private LocalDateTime dateTime = LocalDateTime.now();
    private String level = "";
    private String name = "";
    private String message ="";
    private static int strictCount=0;
    private static int numberOfLogLines = 0;
    private static Set<ReadLogFile> arrayLogLines = new TreeSet<>();

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



    public static Set<ReadLogFile> read(String status,String logFileName) throws FileNotFoundException {

        String messageStr = "";
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
            System.out.println("File Not Found");
            System.exit(0);
        }

        if ((status.equals("-f"))||(status.equals("--follow"))) {

            Scanner in = new Scanner(System.in);
            System.out.println("Input log line(print 'SIGINT' for result): ");
            while (!SIGINT.equals(in.next())) {

                String str = (in.nextLine().replaceAll("[\\s]{2,}", " "));
                if (strictCount==0)
                getData(str,status);


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


        if ((!subStr[1].equals("TRACE"))&&(!subStr[1].equals("ERROR"))&&(!subStr[1].equals("DEBUG"))&&(!subStr[1].equals("INFO"))&&(!subStr[1].equals("WARN"))){
            strictCount++;
            }


        if ((!subStr[2].contains("["))||(!subStr[3].contains("]"))||(subStr[3].length()>17)){
            strictCount++;
            }

        for (int g = 4; g != subStr.length; g++) {

            messageStr += subStr[g];
            messageStr += " ";
        }


        if (strictCount==0)
            arrayLogLines.add(new ReadLogFile(dateTimeTemp,subStr[1],subStr[3].substring(0, subStr[3].length() - 1),messageStr));

        if (!status.equals("--strict"))
            strictCount=0;



        messageStr = "";
        numberOfLogLines++;

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
            }

        }

        else {

            try {


                Duration duration = Duration.parse(durationString);
                endDateTime = startDateTime.plusNanos(duration.toNanos());
            }catch (Exception e){
                System.out.println("Wrong period format");
            }
        }

        return linesBetween2Dates(startDateTime,endDateTime,arrayOfLogLines);

    }


    public static ArrayList<ReadLogFile> logLinesWithEndAndPeriod(String durationString, String endDateTimeString, ArrayList<ReadLogFile> arrayOfLogLines) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeString, formatter);

        LocalDateTime startDateTime = LocalDateTime.now();

        if (durationString.contains("W")) {
            try {
                Period p = Period.parse(durationString);
                startDateTime = endDateTime.minusDays(p.getDays());
            }
            catch (Exception e)
            {
                System.out.println("Wrong period format");
            }

        }

        else {
                    try {
                        Duration duration = Duration.parse(durationString);
                        startDateTime = endDateTime.minusNanos(duration.toNanos());
                    }

                    catch (Exception e){
                        System.out.println("Wrong period format");
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

            if (arrayOfLogLines.get(k).dateTime.isBefore(endDateTime)){
                g=k;


            }
        }

        if (i>g)
            System.out.println("Nothing found, End time is before or equal Start time");

        else {

            for (int b = i; b<=g; b++) {

                filteredList.add(arrayOfLogLines.get(b));
            }


        }

        return filteredList;
    }

    public static ArrayList<ReadLogFile> filter(String mask, String field, ArrayList<ReadLogFile> arrayOfLogLinesFiltered) {

        ArrayList<ReadLogFile> filteredList = new ArrayList<>();
        WorkWithArgs checkMask = new WorkWithArgs();



        mask = checkMask.convertMask(mask);
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

        WorkWithArgs checkMask = new WorkWithArgs();
        String[] maskArray = mask.split(",");
        List<ReadLogFile> toRemove = new ArrayList<>();

            for (ReadLogFile logLine : arrayOfLogLinesFiltered){
                for(String m : maskArray) {
                    m = checkMask.convertMask(m);
                    Pattern p = Pattern.compile(m);

                    if (p.matcher(logLine.getLevel()).matches())
                        toRemove.add(logLine);
                }

            }
            arrayOfLogLinesFiltered.removeAll(toRemove);


        return arrayOfLogLinesFiltered;
    }

    public static ArrayList<ReadLogFile> filterForMass (String mask, ArrayList<ReadLogFile> arrayOfLogLinesFiltered){

        WorkWithArgs checkMask = new WorkWithArgs();
        ArrayList<ReadLogFile> filteredLevels = new ArrayList<>();

        for (ReadLogFile logLine : arrayOfLogLinesFiltered) {

            mask = checkMask.convertMask(mask);
            Pattern p = Pattern.compile(mask);

            if (!p.matcher(logLine.getLevel()).matches())
                filteredLevels.add(logLine);


        }
        return filteredLevels;
    }

}






