package com.ParseArgs;

import com.ReadFile.Filters;
import com.ReadFile.ReadLogFile;

public class StartDateArg extends Arg{

    @Override
    public ReadLogFile filter(String mask, String field, ReadLogFile logFile, String startTime, String endTime, String period) {

        Filters filters = new Filters();

        return filters.durationBetween2Dates(startTime, endTime, period, logFile);

    }

}
