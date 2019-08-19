package com.ParseArgs;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ArgsReader {


    public static void main(String[] args) throws IOException {

        // write();
        // read();

        ParseArgs parseObj = new ParseArgs();
        ArrayList<ParseArgs> argsList = parseObj.parseArgs(args);

        System.out.println("mask is " + parseObj.getMask());
        System.out.println("field is " + parseObj.getField());

        System.out.println("");

        for (Arg l : argsList) {
      //      l.filter();
        }

    }


    public static void read() {

        File f = new File("out.log");
        long count = 0;

        try {
            LineIterator it = FileUtils.lineIterator(f, "UTF-8");

            while (it.hasNext()) {
                String line = it.nextLine();
                count++;


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(count);
    }

    public static void write() throws IOException {

        String text = "Hello world!";
        File file = new File("out.log");


        PrintWriter pw = new PrintWriter(file);
        for (long i = 0; i < 99999999; i++) {
            pw.write(text + "\n");
        }


        System.out.println("The file has been written");
    }
}


