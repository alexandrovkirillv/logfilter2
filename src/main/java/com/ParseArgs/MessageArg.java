package com.ParseArgs;

import com.logfilter.ReadLogFile;

public class MessageArg extends Arg{


    @Override
    public ReadLogFile filter(String mask, String field, ReadLogFile arrayList) {

      
        ReadLogFile readLogFile = new ReadLogFile();

        return readLogFile.filter(mask,field,arrayList);

    }


}