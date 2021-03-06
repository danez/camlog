package de.tschinder.camlog.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;

public class DateFormater
{

    public static String formatUTCDateTime(Context context, String timeToFormat)
    {

        String finalDateTime = "";

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date date = null;
        if (timeToFormat != null) {
            try {
                date = iso8601Format.parse(timeToFormat);
            } catch (ParseException e) {
                date = null;
            }

            if (date != null) {
                finalDateTime = formatUTCDateTime(context, date);
            }
        }
        return finalDateTime;
    }

    public static String formatUTCDateTime(Context context, Date date)
    {
        long when = date.getTime() + TimeZone.getDefault().getOffset(date.getTime());
        return formatLocalDateTime(context, new Date(when));
    }

    public static String formatLocalDateTime(Context context, Date date)
    {
        String finalDateTime = "";

        long when = date.getTime();
        int flags = 0;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
        flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
        flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

        finalDateTime = android.text.format.DateUtils.formatDateTime(context, when, flags);
        return finalDateTime;
    }
}
