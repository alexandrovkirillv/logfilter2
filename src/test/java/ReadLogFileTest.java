import com.ReadFile.ReadLogFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.ReadFile.ReadLogFile.getData;

public class ReadLogFileTest {

    private ArrayList<ReadLogFile> logFileArrayList = new ArrayList<>();


    @Before
    public void setUp() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");

        logFileArrayList.add(new ReadLogFile(LocalDateTime.parse("19-05-23T20:57:20.564Z", formatter), "INFO", "main", "Starting up an application"));
        logFileArrayList.add(new ReadLogFile(LocalDateTime.parse("19-05-23T20:58:20.564Z", formatter), "INFO", "main", "Initializing a worker pool"));
        logFileArrayList.add(new ReadLogFile(LocalDateTime.parse("19-05-23T20:59:20.564Z", formatter), "WARN", "worker-1", "Run task #1241"));
        logFileArrayList.add(new ReadLogFile(LocalDateTime.parse("19-05-23T20:59:50.564Z", formatter), "ERROR", "worker-2", "Run task #717"));


    }


    @After
    public void tearDown() throws Exception {

        logFileArrayList.clear();
    }




    @Test
    public void getData1(String string) {

        string = "19-05-23T20:57:20.564Z INFO [ main] Starting up an application";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");

        ReadLogFile actual = new ReadLogFile();
        actual = getData(string);

        ReadLogFile expected = new ReadLogFile(LocalDateTime.parse("19-05-23T20:57:20.564Z",formatter), "INFO", "main" , "Starting up an application");


    }
//    @Test
//    public void read() throws FileNotFoundException {
//
//
//        String status = "";
//        String logFileName = "";
//
//        ArrayList<ReadLogFile> actual = new ArrayList<>();
//
//        actual.add(logFileArrayList.get(0));
//        actual.add(logFileArrayList.get(1));
//        actual.add(logFileArrayList.get(2));
//        actual.add(logFileArrayList.get(3));
//
//


//        ArrayList<ReadLogFile> expected = new ArrayList<>(ReadLogFile.read(status, logFileName));
//
//
//        assertEquals(expected.get(0).getDateTime(), actual.get(0).getDateTime());
//        assertEquals(expected.get(1).getDateTime(), actual.get(1).getDateTime());
//        assertEquals(expected.get(2).getDateTime(), actual.get(2).getDateTime());
//        assertEquals(expected.get(3).getDateTime(), actual.get(3).getDateTime());
//
//
//        actual.clear();
//        expected.clear();
//
//
//    }
//
//    @Ignore
//    public void filter() {
//
//        String mask = "mai*";
//        String field = "name";
//
//
//        ArrayList<ReadLogFile> actual = new ArrayList<>();
//
//        actual.add(logFileArrayList.get(2));
//        actual.add(logFileArrayList.get(3));
//
//
//        ArrayList<ReadLogFile> expected = ReadLogFile.filter(mask, field, logFileArrayList);
//
//
//        assertEquals(actual, expected);
//
//        actual.clear();
//        expected.clear();
//
//
//        mask = "Run*";
//        field = "message";
//        actual = new ArrayList<>();
//
//
//        actual.add(logFileArrayList.get(0));
//        actual.add(logFileArrayList.get(1));
//
//
//        expected = ReadLogFile.filter(mask, field, logFileArrayList);
//
//        assertEquals(actual, expected);
//
//        System.out.println(actual.get(0).getMessage());
//        System.out.println(actual.get(1).getMessage());
//        System.out.println(expected.get(0).getMessage());
//        System.out.println(expected.get(1).getMessage());
//
//
//    }
//
//    @Test
//    public void levelFilter() {
//
//        String mask = "INFO";
//
//        ArrayList<ReadLogFile> actual = new ArrayList<>();
//
//        actual.add(logFileArrayList.get(2));
//        actual.add(logFileArrayList.get(3));
//
//        ArrayList<ReadLogFile> expected = ReadLogFile.levelFilter(mask, logFileArrayList);
//
//        assertEquals(actual, expected);
//
//        actual.clear();
//        expected.clear();
//
//
//        mask = "INFO,WARN,ERROR";
//
//
//        expected = ReadLogFile.levelFilter(mask, logFileArrayList);
//
//        assertEquals(actual, expected);
//
//
//    }


//    @Test
//    public void durationBetween2Dates() {
//
//        String startDateTimeString = "19-05-23T20:56:20.564Z";
//        String endDateTimeString = "19-05-23T20:58:55.564Z";
//
//
//        ArrayList<ReadLogFile> actual = new ArrayList<>();
//
//        actual.add(logFileArrayList.get(0));
//        actual.add(logFileArrayList.get(1));
//
//
//        ArrayList<ReadLogFile> expected = ReadLogFile.durationBetween2Dates(startDateTimeString, endDateTimeString, logFileArrayList);
//
//
//        assertEquals(actual, expected);
//        actual.clear();
//        expected.clear();
//
//
//        startDateTimeString = "19-05-23T19:56:20.564Z";
//        endDateTimeString = "19-05-23T21:58:55.564Z";
//
//
//        actual.add(logFileArrayList.get(0));
//        actual.add(logFileArrayList.get(1));
//        actual.add(logFileArrayList.get(2));
//        actual.add(logFileArrayList.get(3));
//
//
//        expected = ReadLogFile.durationBetween2Dates(startDateTimeString, endDateTimeString, logFileArrayList);
//
//
//        assertEquals("all list", actual, expected);
//        actual.clear();
//        expected.clear();
//
//
//        startDateTimeString = "19-05-23T19:56:20.564Z";
//        endDateTimeString = "19-05-23T19:58:55.564Z";
//
//
//        expected = ReadLogFile.durationBetween2Dates(startDateTimeString, endDateTimeString, logFileArrayList);
//
//
//        assertEquals("Nothing", expected, expected);
//
//    }
//
//    @Test
//    public void logLinesWithStartAndPeriod() {
//
//
//        String startDateTimeString = "19-05-23T20:58:15.564Z";
//        String period = "PT1M";
//
//
//        ArrayList<ReadLogFile> actual = new ArrayList<>();
//        actual.add(logFileArrayList.get(1));
//        ArrayList<ReadLogFile> expected = ReadLogFile.logLinesWithStartAndPeriod(period, startDateTimeString, logFileArrayList);
//
//        assertEquals(actual, expected);
//        System.out.println("--------");
//        actual.clear();
//        expected.clear();
//
//        startDateTimeString = "19-05-23T20:58:15.564Z";
//        period = "P1D";
//
//
//        actual.add(logFileArrayList.get(1));
//        actual.add(logFileArrayList.get(2));
//        actual.add(logFileArrayList.get(3));
//
//
//        expected = ReadLogFile.logLinesWithStartAndPeriod(period, startDateTimeString, logFileArrayList);
//
//
//        assertEquals(actual, expected);
//        System.out.println("--------");
//        actual.clear();
//        expected.clear();
//
//
//        startDateTimeString = "19-05-23T20:58:15.564Z";
//        period = "P1W";
//
//
//        actual.add(logFileArrayList.get(1));
//        actual.add(logFileArrayList.get(2));
//        actual.add(logFileArrayList.get(3));
//
//
//        expected = ReadLogFile.logLinesWithStartAndPeriod(period, startDateTimeString, logFileArrayList);
//
//
//        assertEquals(actual, expected);
//        System.out.println("--------");
//        actual.clear();
//        expected.clear();
//
//
//    }
//
//    @Test
//    public void logLinesWithEndAndPeriod() {
//
//
//        String startDateTimeString = "19-05-23T20:59:15.564Z";
//        String period = "PT2M";
//
//
//        ArrayList<ReadLogFile> actual = new ArrayList<>();
//
//        actual.add(logFileArrayList.get(0));
//        actual.add(logFileArrayList.get(1));
//
//
//        ArrayList<ReadLogFile> expected = ReadLogFile.logLinesWithEndAndPeriod(period, startDateTimeString, logFileArrayList);
//
//        for (ReadLogFile s : expected)
//            System.out.println(s.getDateTime() + " " + s.getLevel() + " " + "[" + s.getName() + "]" + " " + s.getMessage());
//
//
//        assertEquals(expected, actual);
//        actual.clear();
//        expected.clear();
//
//
//    }

}