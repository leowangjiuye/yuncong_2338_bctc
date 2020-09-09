package com.cloudwalk.livenesscameraproject.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
}
