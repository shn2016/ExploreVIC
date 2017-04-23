package jzha442.explorevic.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Jiao on 24/03/2017.
 */

public class Timeconversion {
    private static  DateFormat dateformat = new SimpleDateFormat("yyyy MM dd");
    private static DateFormat timeformat = new SimpleDateFormat("HH:mm");
    private static DateFormat weekformat = new SimpleDateFormat("EEE");

    public static Long time2unix(String time)
    {
        setTimeZone();
        long unixtime = -1;
        try
        {
            unixtime = timeformat.parse(time).getTime();
            unixtime=unixtime/1000L;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return unixtime;
    }

    public static String unix2time(Long unix){
        setTimeZone();
        Date date = new java.util.Date(unix*1000L);
        String time = timeformat.format(date);
        return time;
    }

    private static void setTimeZone(){
        dateformat.setTimeZone(TimeZone.getTimeZone("GMT"));
        timeformat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /*
        // testing conversion of time string and unix timestamp

        long unixTime = System.currentTimeMillis() / 1000L;

        SimpleDateFormat s = new SimpleDateFormat("yyyy MM dd HH:mma");
        s.setTimeZone(TimeZone.getTimeZone("GMT"));
        String t = s.format(new java.util.Date());

        String time = Timeconversion.unix2time(unixTime);
        long unix = Timeconversion.time2unix(time);
        String time2 = Timeconversion.unix2time(unix);
        System.out.print(unixTime);
        */

    public static String unix2day(Long unix){
        setTimeZone();
        Date date = new Date(unix*1000L);
        String day = dateformat.format(date);
        return day;
    }

    public static String unix2week(Long unix){
        setTimeZone();
        Date date = new Date(unix*1000L);
        String week = weekformat.format(date);
        return week;
    }
}
