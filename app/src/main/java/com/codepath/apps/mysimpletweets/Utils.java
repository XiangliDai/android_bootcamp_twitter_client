package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static String getRelativeTimeString(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        long then;
        try {
           then = sf.parse(rawJsonDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            then = 0;
        }
       
        Date date = new Date(then);

        StringBuffer dateStr = new StringBuffer();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar now = Calendar.getInstance();

        int days = daysBetween(calendar.getTime(), now.getTime());
        int minutes = hoursBetween(calendar.getTime(), now.getTime());
        int hours = minutes / 60;
        if (days == 0) {

            int second = minuteBetween(calendar.getTime(), now.getTime());
            if (minutes > 60) {

                if (hours >= 1 && hours <= 24) {
                    dateStr.append(hours).append("h");
                }

            } else {

                if (second <= 10) {
                    dateStr.append("Now");
                } else if (second > 10 && second <= 30) {
                    dateStr.append("few seconds ago");
                } else if (second > 30 && second <= 60) {
                    dateStr.append(second).append("s");
                } else if (second >= 60 && minutes <= 60) {
                    dateStr.append(minutes).append("m");
                }
            }
        } else

        if (hours > 24 && days <= 7) {
            dateStr.append(days).append("d");
        } else {
            SimpleDateFormat sf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String dateText = sf2.format(date);
            dateStr.append(dateText);
        }

        return dateStr.toString();
    }

    public static int minuteBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / DateUtils.SECOND_IN_MILLIS);
    }

    public static int hoursBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / DateUtils.MINUTE_IN_MILLIS);
    }

    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / DateUtils.DAY_IN_MILLIS);
    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
