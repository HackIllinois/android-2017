package org.hackillinois.app2017.Home;

import android.util.Log;

import java.util.GregorianCalendar;
import java.util.Locale;

public class HomeTime {
    private GregorianCalendar targetTime;

    public HomeTime(GregorianCalendar targetTime) {
        this.targetTime = targetTime;
    }

    public int getHours() {
        long epoch = targetTime.getTimeInMillis();
        long epochNow = new GregorianCalendar().getTimeInMillis();
        long calculatedEpoch = epoch - epochNow;

        return (int)(calculatedEpoch / 3600000);
    }
    public int getMinutes() {
        long epoch = targetTime.getTimeInMillis();
        long epochNow = new GregorianCalendar().getTimeInMillis();
        long calculatedEpoch = epoch - epochNow;

        return (int)((calculatedEpoch / 60000) % 60);
    }
    public int getSeconds() {
        long epoch = targetTime.getTimeInMillis();
        long epochNow = new GregorianCalendar().getTimeInMillis();
        long calculatedEpoch = epoch - epochNow;

        return (int)((calculatedEpoch / 1000) % 60);
    }
    public String getTime() {
        StringBuilder time = new StringBuilder();
        time.append("@ ");
        switch(targetTime.get(GregorianCalendar.DAY_OF_WEEK)) {
            case 1:
                time.append("Sunday");
                break;
            case 2:
                time.append("Monday");
                break;
            case 3:
                time.append("Tuesday");
                break;
            case 4:
                time.append("Wednesday");
                break;
            case 5:
                time.append("Thursday");
                break;
            case 6:
                time.append("Friday");
                break;
            case 7:
                time.append("Saturday");
                break;
        }

        time.append(" ");
        time.append(targetTime.get(GregorianCalendar.HOUR));
        time.append(":");
        time.append(String.format(Locale.US, "%02d", targetTime.get(GregorianCalendar.MINUTE)));
        time.append(" ");

        if (targetTime.get(GregorianCalendar.HOUR_OF_DAY) >= 12) {
            time.append("pm");
        } else {
            time.append("am");
        }

        return time.toString();
    }
}
