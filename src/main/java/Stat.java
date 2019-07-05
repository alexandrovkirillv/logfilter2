import java.time.Duration;
import java.util.*;


public class Stat extends ReadLogFile{

    private String inputName;
    private int id;
    private static ArrayList<String> numbersOfLinesGroupedByPercent = new ArrayList<>();

    public Stat( String inputName, int id) {

        this.inputName = inputName;
        this.id = id;
    }
    public Stat(){

    }

    public String getInputName() {
        return inputName;
    }

    public Integer getId() {
        return id;
    }


public static ArrayList<Stat> showStat (ArrayList<ReadLogFile> logFileArrayList) {

        List<Stat> arrayList = new ArrayList<>();
        ArrayList<Stat> arrayListStat = new ArrayList<>();

        ArrayList names = new ArrayList();

        for (ReadLogFile r : logFileArrayList){

            names.add(r.getName());

        }

        Set<String> unique = new HashSet<String>(names);

        for (String s : unique){
            arrayList.add(new Stat(s,Collections.frequency(names,s)));
        }
        arrayList.sort(Comparator.comparing(Stat::getId));

        System.out.println("Number Of Log Lines: " + logFileArrayList.size());
        System.out.println("Average time between the log lines(Millis): " + averageTimeBetweenTheLogLines(logFileArrayList));
        System.out.println("Numbers Of Lines Grouped By Log Levels: " + numbersOfLinesGroupedByLogLevels(logFileArrayList));
        System.out.println("Numbers Of Lines Grouped By Percent: " + numbersOfLinesGroupedByPercent);
        System.out.println("Number Of Unique Names: " + unique.size());
        System.out.println("Most encountered thread name: " + arrayList.get(arrayList.size()-1).getInputName());
        System.out.println("Least encountered thread name: " + arrayList.get(0).getInputName());

        return arrayListStat;
    }

    public static ArrayList numbersOfLinesGroupedByLogLevels(ArrayList<ReadLogFile> logFileArrayList) {


        ArrayList<String> numbersOfLinesByLevels = new ArrayList<>();

        int TRACE = 0;
        int DEBUG = 0;
        int INFO = 0;
        int WARN = 0;
        int ERROR = 0;


        for (ReadLogFile m : logFileArrayList) {

            switch (m.getLevel()) {

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


        numbersOfLinesGroupedByPercent.add("TRACE " + (TRACE * 100 / percent));
        numbersOfLinesGroupedByPercent.add("DEBUG " + (DEBUG * 100 / percent));
        numbersOfLinesGroupedByPercent.add("INFO " + (INFO * 100 / percent));
        numbersOfLinesGroupedByPercent.add("WARN " + (WARN * 100 / percent));
        numbersOfLinesGroupedByPercent.add("ERROR " + (ERROR * 100 / percent));

        return numbersOfLinesByLevels;
    }


    public static Long averageTimeBetweenTheLogLines(ArrayList<ReadLogFile> logFileArrayList) {


        ArrayList<Long> avTime = new ArrayList<>();
        long avTimeLog = 0;

        for (int i = 0; i < logFileArrayList.size()-1; i++) {

            avTime.add(Duration.between(logFileArrayList.get(i).getDateTime(), logFileArrayList.get(i + 1).getDateTime()).toMillis());
            avTimeLog += avTime.get(i);

        }

        return avTimeLog / (logFileArrayList.size() - 1);

    }

}

