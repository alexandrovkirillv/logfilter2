package com.ParseArgs;

import com.logfilter.LevelNameFilter;
import com.logfilter.ReadLogFile;

public class EndDateArg extends Arg{

    @Override
    public ReadLogFile filter(String mask, String field, ReadLogFile arrayList) {


        LevelNameFilter levelNameFilter = new LevelNameFilter();

        return levelNameFilter.filter(mask,field,arrayList);

    }


}
