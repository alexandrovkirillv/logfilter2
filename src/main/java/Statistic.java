import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Statistic  {


    public void showStat() throws FileNotFoundException {

        ReadLogFile RLF = new ReadLogFile();
        RLF.read();

        System.out.println("Number Of Log Lines: " + RLF.getNumberOfLogLines());
        System.out.println("Average time between the log lines(Millis): " + RLF.averageTimeBetweenTheLogLines());
        System.out.println("Numbers Of Lines Grouped By Log Levels: " + RLF.NumbersOfLinesGroupedByLogLevels());
        System.out.println("Numbers Of Lines Grouped By Percent: " + RLF.getNumbersOfLinesByPercent());
        System.out.println("Number Of Unique Names: " + RLF.numbersOfUniqueNames());
        System.out.println("Most encountered thread name: " + RLF.mostEncounteredThreadName().get(0));
        System.out.println("Least encountered thread name: " + RLF.mostEncounteredThreadName().get(RLF.mostEncounteredThreadName().size()-1));

    }

    public void showHelp ()throws FileNotFoundException {





    }



}