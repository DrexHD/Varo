package me.drex.varo.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeDifferenceUtil {

    private static final int MAX_YEARS = 100000;

    public static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return "-";
        }
        if (toDate.after(fromDate)) {
            future = true;
        }
        StringBuilder sb = new StringBuilder();
        int[] types = new int[]{Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND};
        String[] names = new String[]{"jahr", "jahre",
                "monat", "monate", "tag",
                "tag", "stunde", "stunden",
                "minute", "minuten", "sekunden",
                "sekunde"};
        int accuracy = 0;
        for (int i = 0; i < types.length; i++) {
            if (accuracy > 2) {
                break;
            }
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                accuracy++;
                sb.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
            }
        }
        if (sb.length() == 0) {
            return "-";
        }
        return sb.toString().trim();
    }

    static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int year = Calendar.YEAR;

        int fromYear = fromDate.get(year);
        int toYear = toDate.get(year);
        if (Math.abs(fromYear - toYear) > MAX_YEARS) {
            toDate.set(year, fromYear + (future ? MAX_YEARS : -MAX_YEARS));
        }

        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        diff--;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }

    public static String formatDiff(long diff) {
        Calendar from = new GregorianCalendar();
        Calendar to = new GregorianCalendar();
        from.setTimeInMillis(0);
        to.setTimeInMillis(diff);
        return formatDateDiff(from, to);
    }

}
