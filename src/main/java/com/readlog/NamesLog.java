package com.readlog;

import com.logfilter.ReadLogFile;

import java.util.ArrayList;

public class NamesLog extends Log {


    @Override
    public ReadLogFile filter(String mask, String field, ReadLogFile arrayList) {

        ReadLogFile readLogFile = new ReadLogFile();

        return readLogFile.filter(mask,field,arrayList);

    }


}
