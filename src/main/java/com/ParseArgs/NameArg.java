package com.ParseArgs;

import com.ReadFile.Filters;
import com.ReadFile.ReadLogFile;


public class NameArg extends Arg {


    @Override
    public ReadLogFile filter(String mask, String field, ReadLogFile arrayList, String startTime, String endTime, String period) {


        Filters filters = new Filters();

        return filters.filter(mask,field,arrayList);

    }


}
