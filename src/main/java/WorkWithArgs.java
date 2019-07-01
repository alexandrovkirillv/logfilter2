import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class WorkWithArgs {


    public String convertMask (String mask){

        ArrayList<Character> charsA = new ArrayList<Character>();
        for (char c : mask.toCharArray()) {
            charsA.add(c);
        }

        if ((charsA.get(charsA.size()-1).equals('*'))||(charsA.get(0).equals('*'))){

            mask = mask.replaceAll("[*]", "\\.\\*");
            System.out.println(mask);


        }

        else if((charsA.get(charsA.size()-1).equals('?'))||(charsA.get(0).equals('?'))){
            mask = mask.replaceAll("[?]", "\\.\\?");

        }


        return mask;

    }

    public String[] splitMask (String mask){

        String[] subStr;
        subStr = mask.split(",");

        WorkWithArgs WWA = new WorkWithArgs();

        for (int i=0; i<subStr.length; i++) {

            subStr[i]=WWA.convertMask(subStr[i]);

        }



        return subStr;
    }
}
