import com.ReadFile.Filters;
import com.ReadFile.ReadLogFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.*;

public class FiltersTest {

    private List<ReadLogFile> logFileArrayList = new ArrayList<>();

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
    public void filter() {

        String mask = "Run*";
        String field = "message";

        Filters filters = new Filters();
        ReadLogFile readLogFile;


        readLogFile = filters.filter(mask,field,logFileArrayList.get(2));

        String expected = "";

        assertEquals("LogLine is filtered by message (Run*)",expected, readLogFile.getLevel());


        mask = "main";
        field = "name";

        readLogFile = filters.filter(mask,field,logFileArrayList.get(1));

        assertEquals("LogLine is filtered by name (main)",expected, readLogFile.getLevel());

        mask = "INFO";
        field = "level";

        readLogFile = filters.filter(mask,field,logFileArrayList.get(0));

        assertEquals("LogLine is filtered by level (INFO)",expected, readLogFile.getLevel());

        mask = "INFO,WARN";
        field = "level";

        readLogFile = filters.filter(mask,field,logFileArrayList.get(3));

        expected = "ERROR";
        assertEquals("LogLine is filtered by level (INFO), but level is ERROR",expected, readLogFile.getLevel());





    }



    @Test
    public void durationBetween2Dates() {

        Filters filters = new Filters();

        String startTime = "19-05-23T20:56:20.564Z";
        String endTime = "19-05-23T20:58:20.564Z";
        String period = "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime expected = LocalDateTime.parse("19-05-23T20:57:20.564Z",formatter);

        ReadLogFile readLogFile= filters.durationBetween2Dates(startTime,endTime,period,logFileArrayList.get(0));

        assertEquals("start time + end time",expected,readLogFile.getDateTime());


        startTime = "19-05-23T20:56:20.564Z";
        endTime = "";
        period = "PT2M";

        readLogFile= filters.durationBetween2Dates(startTime,endTime,period,logFileArrayList.get(0));
        assertEquals("start time + period",expected,readLogFile.getDateTime());

        startTime = "";
        endTime = "19-05-23T20:58:20.564Z";
        period = "P2W";

        readLogFile= filters.durationBetween2Dates(startTime,endTime,period,logFileArrayList.get(0));
        assertEquals("end time + period",expected,readLogFile.getDateTime());

    }

}