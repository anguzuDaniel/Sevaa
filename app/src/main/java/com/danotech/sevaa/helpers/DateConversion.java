package com.danotech.sevaa.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConversion {
    public static void main(String[] args) {

    }

    public static String convert(String d) {
        DateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");


        try {
            Date date = inputDateFormat.parse(d);
            DateFormat outputDateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
            assert date != null;
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d;
    }
}