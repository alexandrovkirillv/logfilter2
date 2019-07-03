//import java.io.FileNotFoundException;
//import java.time.Duration;
//import java.util.*;
//
//
//public class Stat extends ReadLogFile {
//
//    private String inputRecipeName;
//    private int id;
//
//    public Stat(String inputRecipeName, int id){
//        this.id= id;
//        this.inputRecipeName =inputRecipeName;
//    }
//
//    public String getInputRecipeName() {
//        return inputRecipeName;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//
//public  void showStat(String logFileName) throws FileNotFoundException {
//
//
//        List<Stat> arrayList = new ArrayList<>();
//
//        read("",logFileName);
//        ArrayList names = getNames();
//        Set<String> unique = new HashSet<String>(names);
//
//        for (String s : unique){
//            arrayList.add(new Stat(s,Collections.frequency(names,s)));
//        }
//        arrayList.sort(Comparator.comparing(Stat::getId));
//
//
//
//        System.out.println("Number Of Log Lines: " + getNumberOfLogLines());
//        System.out.println("Average time between the log lines(Millis): " + averageTimeBetweenTheLogLines());
//        System.out.println("Numbers Of Lines Grouped By Log Levels: " + NumbersOfLinesGroupedByLogLevels());
//        System.out.println("Numbers Of Lines Grouped By Percent: " + getNumbersOfLinesByPercent());
//        System.out.println("Number Of Unique Names: " + numbersOfUniqueNames());
//        System.out.println("Most encountered thread name: " + arrayList.get(arrayList.size()-1).getInputRecipeName());
//        System.out.println("Least encountered thread name: " + arrayList.get(0).getInputRecipeName());
//
//    }
//
//    public ArrayList NumbersOfLinesGroupedByLogLevels() {
//
//
//        ArrayList<String> numbersOfLinesByLevels = new ArrayList<>();
//        int TRACE = 0;
//        int DEBUG = 0;
//        int INFO = 0;
//        int WARN = 0;
//        int ERROR = 0;
//
//
//        for (String m : getLevels()) {
//
//            switch (m) {
//                case "INFO":
//
//                    INFO++;
//                    break;
//                case "TRACE":
//
//                    TRACE++;
//                    break;
//                case "DEBUG":
//
//                    DEBUG++;
//                    break;
//                case "WARN":
//
//                    WARN++;
//                    break;
//                case "ERROR":
//
//                    ERROR++;
//                    break;
//            }
//
//        }
//
//
//        numbersOfLinesByLevels.add("TRACE " + TRACE);
//        numbersOfLinesByLevels.add("DEBUG " + DEBUG);
//        numbersOfLinesByLevels.add("INFO " + INFO);
//        numbersOfLinesByLevels.add("WARN " + WARN);
//        numbersOfLinesByLevels.add("ERROR " + ERROR);
//
//        int percent = INFO + TRACE + WARN + DEBUG + ERROR;
//
//
//        getNumbersOfLinesByPercent().add("TRACE " + (TRACE * 100 / percent));
//        getNumbersOfLinesByPercent().add("DEBUG " + (DEBUG * 100 / percent));
//        getNumbersOfLinesByPercent().add("INFO " + (INFO * 100 / percent));
//        getNumbersOfLinesByPercent().add("WARN " + (WARN * 100 / percent));
//        getNumbersOfLinesByPercent().add("ERROR " + (ERROR * 100 / percent));
//
//        return numbersOfLinesByLevels;
//    }
//
//
//    public int numbersOfUniqueNames() {
//
//        ArrayList<String> uniqueNames = new ArrayList<>();
//
//        for (String x : getNames()) {
//            if (!uniqueNames.contains(x))
//                uniqueNames.add(x);
//        }
//
//        return uniqueNames.size();
//    }
//
//
//
//
//    public Long averageTimeBetweenTheLogLines() {
//
//        ArrayList<Long> avTime = new ArrayList<>();
//        long avTimeLog = 0;
//
//        for (int i = 0; i < getNumberOfLogLines()-1; i++) {
//
//            avTime.add(Duration.between(getDateTime().get(i), getDateTime().get(i + 1)).toMillis());
//            avTimeLog += avTime.get(i);
//
//        }
//
//        return avTimeLog / (getNumberOfLogLines() - 1);
//
//    }
//
//}
//
