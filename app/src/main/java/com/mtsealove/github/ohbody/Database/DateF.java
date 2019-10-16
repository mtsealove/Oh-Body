package com.mtsealove.github.ohbody.Database;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateF {
    SimpleDateFormat dateFormat;
    Date date;

    public DateF() {
           dateFormat=new SimpleDateFormat("yyyyMMdd");
           date=new Date(System.currentTimeMillis());
    }

    public String getDate() {
        return dateFormat.format(date);
    }

}
