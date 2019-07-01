import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import static org.junit.Assert.*;

public class ReadLogFileTest {
    ReadLogFile RLF = new ReadLogFile();
    ArrayList<String> levels= new ArrayList<>();


    @Test
    public void getMessage() throws FileNotFoundException {

        RLF.read();
        levels.add("Starting up an application ");
        levels.add("Initializing a worker pool ");
        levels.add("Run task #1241 ");
        levels.add("Run task #717 ");
        levels.add("Connecting to storage:3121 ");
        levels.add("Task #717 has been completed ");
        levels.add("Connection established ");
        levels.add("Run task #467 ");


        assertEquals(levels,RLF.getMessage());


    }


    @Test
    public void getNumberOfLogLines() throws FileNotFoundException {

        RLF.read();

        assertEquals(8,RLF.getNumberOfLogLines());
    }


    @Test
    public void numbersOfUniqueNames() throws FileNotFoundException {

        RLF.read();
        int numbersOfUniqueNamesTest = 4;

        assertEquals(numbersOfUniqueNamesTest,RLF.numbersOfUniqueNames());


    }


}