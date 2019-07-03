import org.junit.Test;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ReadLogFileTest {
    ReadLogFile RLF = new ReadLogFile(LocalDateTime.now(),"","","");
    ArrayList<String> levels= new ArrayList<>();



    @Test
    public void getNumberOfLogLines() throws FileNotFoundException {

        String logFileName= "example.log";
        String strict = "";
        RLF.read(strict,logFileName);

        assertEquals(8,RLF.getNumberOfLogLines());
    }


//    @Test
//    public void numbersOfUniqueNames() throws FileNotFoundException {
//
//        String logFileName= "example.log";
//        String strict = "";
//        RLF.read(strict,logFileName);
//        int numbersOfUniqueNamesTest = 4;
//
//        assertEquals(numbersOfUniqueNamesTest,RLF.numbersOfUniqueNames());
//
//
//    }


}