
import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class ReadLogFile {

    private ArrayList<LocalDateTime> dateTime = new ArrayList<>();
    private ArrayList<String> levels = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> message = new ArrayList<>();
    private ArrayList<String> numbersOfLinesByPercent = new ArrayList<>();
    private ArrayList<String> logLines = new ArrayList<>();
    private int numberOfLogLines = 0;

    public ArrayList<String> getNumbersOfLinesByPercent() {
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

             if ((!subStr[1].equals("TRCE"))&&(!subStr[1].equals("ERROR"))&&(!subStr[1].equals("DEBUG"))&&(!subStr[1].equals("INFO"))&&(!subStr[1].equals("WARN"))){
                 strictCount++;
                 continue;}


             if ((!subStr[2].contains("["))||(!subStr[3].contains("]"))||(subStr[3].length()>17)){
                 strictCount++;
                 continue;}





            dateTime.add(dateTimeTemp);

             if ((numberOfLogLines>0)&&(dateTime.get(numberOfLogLines-1).isAfter(dateTime.get(numberOfLogLines)))){
                 dateTime.remove(numberOfLogLines);
                 strictCount++;
                 continue;
             }

            if ((strictCount>0)&&(!strict.equals(""))){
                System.out.println("code 1");
                break;
            }




             levels.add(subStr[1]);
             names.add(subStr[3].substring(0, subStr[3].length() - 1));
             for (int g = 4; g != subStr.length; g++) {

                messageStr += subStr[g];
                messageStr += " ";
             }
             message.add(messageStr);
             messageStr = "";

             logLines.add(Str);
             numberOfLogLines++;
        }
        sc.close();

//
//        System.out.println("");
//        for(LocalDateTime s :dateTime)
//            System.out.println(s);
//        System.out.println("");

//        System.out.println(names);
//        System.out.println(message);
//        System.out.println(levels);
//        System.out.println("");


    }


    public ArrayList NumbersOfLinesGroupedByLogLevels() {


        ArrayList<String> numbersOfLinesByLevels = new ArrayList<>();
        int TRACE = 0;
        int DEBUG = 0;
        int INFO = 0;
        int WARN = 0;
        int ERROR = 0;


        for (String m : levels) {

            switch (m) {
                case "INFO":

                    INFO++;
                    break;
                case "TRACE":

                    TRACE++;
                    break;
                case "DEBUG":

                    DEBUG++;
                    break;
                case "WARN":

                    WARN++;
                    break;
                case "ERROR":

                    ERROR++;
                    break;
            }

        }


        numbersOfLinesByLevels.add("TRACE " + TRACE);
        numbersOfLinesByLevels.add("DEBUG " + DEBUG);
        numbersOfLinesByLevels.add("INFO " + INFO);
        numbersOfLinesByLevels.add("WARN " + WARN);
        numbersOfLinesByLevels.add("ERROR " + ERROR);

        int percent = INFO + TRACE + WARN + DEBUG + ERROR;


        numbersOfLinesByPercent.add("TRACE " + (TRACE * 100 / percent));
        numbersOfLinesByPercent.add("DEBUG " + (DEBUG * 100 / percent));
        numbersOfLinesByPercent.add("INFO " + (INFO * 100 / percent));
        numbersOfLinesByPercent.add("WARN " + (WARN * 100 / percent));
        numbersOfLinesByPercent.add("ERROR " + (ERROR * 100 / percent));

        return numbersOfLinesByLevels;
    }


    public int numbersOfUniqueNames() {

        ArrayList<String> uniqueNames = new ArrayList<>();

        for (String x : names) {
            if (!uniqueNames.contains(x))
                uniqueNames.add(x);
        }

        return uniqueNames.size();
    }

    public ArrayList mostEncounteredThreadName() {

        Set<String> unique = new HashSet<String>(names);

        ArrayList<String> max = new ArrayList<>();

        for (String x : unique) {


            max.add(x + " " + Collections.frequency(names, x));
        }


        return max;
    }


    public Long averageTimeBetweenTheLogLines() {

        ArrayList<Long> avTime = new ArrayList<>();
        long avTimeLog = 0;

        for (int i = 0; i < numberOfLogLines-1; i++) {

            System.out.println("1");
            avTime.add(Duration.between(dateTime.get(i), dateTime.get(i + 1)).toMillis());
            avTimeLog += avTime.get(i);

        }

        return avTimeLog / (numberOfLogLines - 1);

    }






    public void durationBetween2Dates(String startDateTimeString, String endDateTimeString) {



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeString, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeString, formatter);


        int i = 0;
        int g = 0;
        for (LocalDateTime l : dateTime) {
            if ((i < logLines.size())&&(l.isAfter(startDateTime))) {
                i = dateTime.indexOf(l);
                break;
            }

        }

        for (LocalDateTime l : dateTime) {
            if ((g < logLines.size())&&(l.isBefore(endDateTime))) {
                g = dateTime.indexOf(l);

            }
        }

        if (i>=g)
            System.out.println("Nothing found, End time is before or equal Start time");

        else {

            for (int n = i; n <= g; n++) {

                System.out.println(logLines.get(n));
            }

        }

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

        int i = 0;

        for (LocalDateTime l : dateTime) {
            if ((i < logLines.size()) && (l.isAfter(startDateTime))) {
                i = dateTime.indexOf(l);
                break;
            }
        }
        int g=0;

        for (LocalDateTime l : dateTime) {
            if ((g < logLines.size())&&(l.isBefore(endDateTime))) {
                g = dateTime.indexOf(l);
            }
        }

        if (i>=g)
            System.out.println("Nothing found, End time is before or equal Start time");

        else {

            for (int n = i; n <= g; n++) {

                System.out.println(logLines.get(n));
            }

        }

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

        int i = 0;

        for (LocalDateTime l : dateTime) {
            if ((i < logLines.size()) && (l.isAfter(startDateTime))) {
                i = dateTime.indexOf(l);
                break;
            }
        }
        int g=0;

        for (LocalDateTime l : dateTime) {
            if ((g < logLines.size())&&(l.isBefore(endDateTime))) {
                g = dateTime.indexOf(l);
            }
        }

        if (i>=g)
            System.out.println("Nothing found, End time is before or equal Start time");

        else {

            for (int n = i; n <= g; n++) {

                System.out.println(logLines.get(n));
            }

        }

    }

    public ArrayList<String> messageFilter(String mask) {

        ArrayList<String> filtredMessage = new ArrayList<>();
        Pattern p = Pattern.compile(mask);

        for (int i = 0; i < message.size(); i++) {
            if (!p.matcher(message.get(i)).matches()) {
                System.out.println(logLines.get(i));

            }
        }
        return filtredMessage;
    }

    public ArrayList<String> namesFilter(String mask) {

        Pattern p = Pattern.compile(mask);
        ArrayList<String> filtredNames = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            if (!p.matcher(names.get(i)).matches()) {
                filtredNames.add(logLines.get(i));

            }
        }
        return filtredNames;
    }

    public ArrayList<String> levelFilter(String [] maskArray) {

        ArrayList<String> filtredLevels = new ArrayList<>();

        for(String mask : maskArray) {

            Pattern p = Pattern.compile(mask);

            for (int i = 0; i < levels.size(); i++) {
                if (p.matcher(levels.get(i)).matches())
                    filtredLevels.add(logLines.get(i));
            }
        }


        return filtredLevels;
    }





}






