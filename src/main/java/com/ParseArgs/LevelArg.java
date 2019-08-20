package com.ParseArgs;

import com.logfilter.Filters;
import com.logfilter.ReadLogFile;

public class LevelArg extends Arg{

    @Override
    public ReadLogFile filter(String mask, String field, ReadLogFile arrayList, String startTime, String endTime, String period) {


        Filters filters = new Filters();

        return filters.filter(mask, field, arrayList);
    }
}
