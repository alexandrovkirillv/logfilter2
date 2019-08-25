import com.ReadFile.ReadLogFile;
import org.junit.After;
import org.junit.Before;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StatTest {
    private ArrayList<ReadLogFile> logFileArrayList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");

        logFileArrayList.add(new ReadLogFile(LocalDateTime.parse("19-05-23T20:57:20.564Z", formatter), "INFO", "main", "Starting up an application"));
        logFileArrayList.add(new ReadLogFile(LocalDateTime.parse("19-05-23T20:58:20.564Z", formatter), "INFO", "main", "Initializing a worker pool"));
        logFileArrayList.add(new ReadLogFile(LocalDateTime.parse("19-05-23T20:59:20.564Z", formatter), "WARN", "worker-1", "Run task #1241"));
        logFileArrayList.add(new ReadLogFile(LocalDateTime.parse("19-05-23T20:59:50.564Z", formatter), "ERROR", "worker-2", "Run task #717"));


    }

//    @Test
//    public void showStat() {
//
//        int numberOfLogLines = 4;
//        int averageTimeLines = 50000;
//        int numberOfUniqueNames = 3;
//        String mostEncounteredThreadName = "main";
//        String leastEncounteredThreadName = "worker-2";
//
//
//        ArrayList<Stat> expected = Stat.showStat(logFileArrayList);
//
//
//        assertEquals(Long.valueOf(expected.get(0).getId()), Long.valueOf(numberOfLogLines));
//        assertEquals(Long.valueOf(expected.get(1).getId()), Long.valueOf(averageTimeLines));
//        assertEquals(Long.valueOf(expected.get(2).getId()), Long.valueOf(numberOfUniqueNames));
//        assertEquals(expected.get(3).getInputName(), mostEncounteredThreadName);
//        assertEquals(expected.get(4).getInputName(), leastEncounteredThreadName);
//
//
//    }


    @After
    public void name() {

        logFileArrayList.clear();

    }

}


