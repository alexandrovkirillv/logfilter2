package com.readlog;

import com.logfilter.ReadLogFile;

import java.util.ArrayList;

public class MessageLog extends Log{


    @Override
    public ArrayList<ReadLogFile> filter(String mask, String field, ArrayList<ReadLogFile> arrayList) {

      
        ReadLogFile readLogFile = new ReadLogFile();

        return readLogFile.filter(mask,field,arrayList);

    }


}
