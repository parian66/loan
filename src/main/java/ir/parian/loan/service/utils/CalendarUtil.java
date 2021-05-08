package ir.parian.loan.service.utils;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

import java.util.Date;

public class CalendarUtil {
    public static boolean isPast(final Date date) {
        return new Date().after(date);
    }

    public static Date addPersianMonth(final Date date, int value) {
        final Calendar persianCalendar = toPersianCalendar(date);
        persianCalendar.add(Calendar.MONTH, value);
        return persianCalendar.getTime();
    }

    public static Calendar getPersianCalendar() {
        return toPersianCalendar(new Date());
    }

    public static Calendar toPersianCalendar(final Date date) {
        final Calendar calendar = Calendar.getInstance(ULocale.forLanguageTag("fa"));
        calendar.setTime(date);
        return calendar;
    }
}
