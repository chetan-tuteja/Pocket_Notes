package com.chetantuteja.pocketnotes.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampUtil {

    public static String getCurrentTimestamp(){
        String currentDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-yyyy");
            currentDate = dateFormat.format(new Date());
        } catch (Exception e) {
            return null;
        }

        return currentDate.toString();
    }
}
