
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadLogFile {

    private ArrayList<LocalDateTime> dateTime = new ArrayList<>();
    private ArrayList<String> levels = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> message = new ArrayList<>();
    private ArrayList<String> numbersOfLinesByPercent = new ArrayList<>();
    private ArrayList<String> logLines = new ArrayList<>();

    public ArrayList<String> getNumbersOfLinesByPercent() {
        return numbersOfLinesByPercent;
    }

    private int numberOfLogLines = 0;


    public ArrayList<LocalDateTime> getDateTime() {
        return dateTime;
    }

    public ArrayList<String> getLevels() {
        return levels;
    }

    public ArrayList<String> getLogLines() {
        return logLines;
    }

    public ArrayList<String> getName() {
        return names;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public int getNumberOfLogLines() {
        return numberOfLogLines;
    }

    public void read() throws FileNotFoundException {

        String messageStr = "";

        Scanner sc = new Scanner(new File("file.log"));
        int i = 0;

        while (sc.hasNext()) {


            logLines.add(sc.nextLine().replaceAll("[\\s]{2,}", " "));
            String[] subStr = logLines.get(i).split(" ");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateTime.add(LocalDateTime.parse(subStr[0], formatter));
            levels.add(subStr[1]);
            names.add(subStr[3].substring(0, subStr[3].length() - 1));

            for (int g = 4; g != subStr.length; g++) {

                messageStr += subStr[g];
                messageStr += " ";
            }
            message.add(messageStr);
            messageStr = "";

            i++;

        }
        sc.close();

        numberOfLogLines = i;

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

        for (int i = 0; i < numberOfLogLines - 1; i++) {

            avTime.add(Duration.between(dateTime.get(i), dateTime.get(i + 1)).toMillis());
            avTimeLog += avTime.get(i);

        }

        return avTimeLog / (numberOfLogLines - 1);

    }


    public void levelFilter(String [] maskArray) {

        int g=0;
        ArrayList<String> filtredLevels = new ArrayList<>();

        for(String mask : maskArray) {

            Pattern p = Pattern.compile(mask);

            for (int i = 0; i < levels.size(); i++) {
                if (p.matcher(levels.get(i)).matches())
                    filtredLevels.add(logLines.get(i));
            }
        }

        if (filtredLevels.isEmpty())
            System.out.println("Nothing found");
        else {
            for(String s: filtredLevels)
            System.out.println(s);
        }
    }

    public void namesFilter(String mask) {


        int g=0;
        Pattern p = Pattern.compile(mask);

        for (int i = 0; i < names.size(); i++) {
            if (!p.matcher(names.get(i)).matches()) {
                System.out.println(logLines.get(i));
                g++;
            }
        }

            if (g == 0)
                System.out.println("Nothing found");

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


        System.out.println(startDateTime);
        System.out.println(endDateTime);
        System.out.println(i);
        System.out.println(g);
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

        System.out.println(startDateTime);
        System.out.println(endDateTime);

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

        System.out.println(startDateTime);
        System.out.println(endDateTime);

        if (i>=g)
            System.out.println("Nothing found, End time is before or equal Start time");

        else {

            for (int n = i; n <= g; n++) {

                System.out.println(logLines.get(n));
            }

        }

    }

    public void messageFilter(String mask) {

        Pattern p = Pattern.compile(mask);

        for (int i = 0; i < message.size(); i++) {
            if (!p.matcher(message.get(i)).matches()) {
                System.out.println(logLines.get(i));
            }
            else {

                System.out.println("Nothing found");
            }
        }


    }





}






