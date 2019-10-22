package com.mtsealove.github.ohbody.Database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

//내부 DB에 저장될 날짜 형태를 통일하기 위해 별도 클래스 제작
public class DateF {
    SimpleDateFormat dateFormat;
    Date date;

    public DateF() {
        dateFormat = new SimpleDateFormat("yyyyMMdd");
        date = new Date(System.currentTimeMillis());
    }

    public String getDate() {
        return dateFormat.format(date);
    }

    public String getYDate() {
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.DATE, 0 - 1);
        return dateFormat.format(calendar.getTime());
    }

}
