import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.lang.reflect.Array;
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
    private int strictCount=0;

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


    public int getNumberOfLogLines() {
        return numberOfLogLines;
    }






    public void read(String status,String logFileName) throws FileNotFoundException {

        String messageStr = "";
        String SIGINT = "SIGINT";
        Scanner sc = new Scanner(new File(logFileName));


        while (sc.hasNext()) {

             String str = (sc.nextLine().replaceAll("[\\s]{2,}", " "));
             getData(str, status);

        }
        sc.close();

        if ((status.equals("-f"))||(status.equals("--follow"))) {

            Scanner in = new Scanner(System.in);
            System.out.println("Input log line(print 'SIGINT' for result): ");
            while (!SIGINT.equals(in.next())) {

                String str = (in.nextLine().replaceAll("[\\s]{2,}", " "));
                getData(str,status);


            }
            in.close();
        }


    }

    public void getData (String Str,String status){

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


        arrayLogLines.add(new ReadLogFile(dateTimeTemp,subStr[1],subStr[3].substring(0, subStr[3].length() - 1),messageStr));
        messageStr = "";

        if ((numberOfLogLines>0)&&(arrayLogLines.get(numberOfLogLines-1).getDateTime().isAfter(arrayLogLines.get(numberOfLogLines).getDateTime()))){
            strictCount++;
        }

        if ((strictCount>0)&&(status.equals("--strict"))){
            System.out.println("code 1");

        }
        numberOfLogLines++;

    }


    public ArrayList<ReadLogFile> durationBetween2Dates(String startDateTimeString, String endDateTimeString) {



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeString, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeString, formatter);

        return linesBetween2Dates(startDateTime,endDateTime);

    }

    public ArrayList<ReadLogFile> getLogLinesWithStartAndPeriod(String durationString, String startDateTimeString) {

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

        return linesBetween2Dates(startDateTime,endDateTime);

    }


    public ArrayList<ReadLogFile> getLogLinesWithEndAndPeriod(String durationString, String endDateTimeString) {

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

        return linesBetween2Dates(startDateTime,endDateTime);

    }

    public ArrayList<ReadLogFile> linesBetween2Dates(LocalDateTime startDateTime, LocalDateTime endDateTime){

        ArrayList<ReadLogFile> filteredList = new ArrayList<>();
        int g=0;
        int i = 0;


        System.out.println(startDateTime);
        System.out.println(endDateTime);


        for (int k = 0; k< arrayLogLines.size();k++) {
            if  (arrayLogLines.get(k).dateTime.isAfter(startDateTime)) {
                i = k;

                break;
            }
        }


        for (int k = 0; k< arrayLogLines.size();k++) {

            if (arrayLogLines.get(k).dateTime.isBefore(endDateTime)){
                g=k;
            }
        }

        if (i>=g)
            System.out.println("Nothing found, End time is before or equal Start time");

        else {

            for (int b = i ; b< arrayLogLines.size();b++) {

                filteredList.add(arrayLogLines.get(b));
            }

        }

        return filteredList;
    }

    public ArrayList<ReadLogFile> filter(String mask, String field,ArrayList<ReadLogFile> arrayOfLogLinesFiltered) {

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

        if (filteredList.isEmpty())
            System.out.println("Nothing found");

        return filteredList;
    }

    @Override
    public int compareTo(ReadLogFile o) {
        // return o.getDateTime().isBefore(getDateTime()) ? 1 : 0;

        int result = this.getDateTime().compareTo(o.getDateTime());

        return result;
    }

    public ArrayList<ReadLogFile> levelFilter(String mask, ArrayList<ReadLogFile> arrayOfLogLinesFiltered ) {

        ArrayList<ReadLogFile> filteredLevels = new ArrayList<>();
        WorkWithArgs checkMask = new WorkWithArgs();
        String[] maskArray = mask.split(",");


        for(String m : maskArray) {

            m=checkMask.convertMask(m);
            Pattern p = Pattern.compile(m);

            for (int i = 0; i < arrayOfLogLinesFiltered.size(); i++) {
                if (!p.matcher(arrayOfLogLinesFiltered.get(i).level).matches())
                    filteredLevels.add(arrayOfLogLinesFiltered.get(i));
            }
        }
        if (filteredLevels.isEmpty())
            System.out.println("Nothing found");
        filteredLevels.sort(Comparator.comparing(ReadLogFile::getDateTime));

        return filteredLevels;
    }

}






