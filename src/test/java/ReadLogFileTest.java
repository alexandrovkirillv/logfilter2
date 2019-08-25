import com.ReadFile.ReadLogFile;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class ReadLogFileTest {

    @Test
    public void getData() {

        String string = "19-05-23T20:57:20.564Z INFO [ main] Starting up an application";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd'T'HH:mm:ss.SSS'Z'");

        ReadLogFile actual = new ReadLogFile();
        ReadLogFile readLogFile = new ReadLogFile();

        actual = readLogFile.getData(string);

        ReadLogFile expected = new ReadLogFile(LocalDateTime.parse("19-05-23T20:57:20.564Z", formatter), "INFO", "main", "Starting up an application ");

        System.out.println(actual.getDateTime() + " " + actual.getLevel() + " " + "[" + actual.getName() + "]" + " " + actual.getMessage());
        System.out.println(expected.getDateTime() + " " + expected.getLevel() + " " + "[" + expected.getName() + "]" + " " + expected.getMessage());
        assertEquals(actual.getDateTime(), expected.getDateTime());
        assertEquals(actual.getLevel(), expected.getLevel());
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getMessage(), expected.getMessage());

    }


    @Test
    public void read() {
    }

    @Test
    public void checkOutFile() {
    }

    @Test
    public void showLine() {
    }

    @Test
    public void getData1() {
    }
}