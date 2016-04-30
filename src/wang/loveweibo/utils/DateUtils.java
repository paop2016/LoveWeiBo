package wang.loveweibo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateFormat;
import android.widget.ListView;

public class DateUtils {
//	"created_at": "Tue May 31 17:46:55 +0800 2011"
	public static final long ONE_MINUTE_MILLIONS = 60 * 1000;
	public static final long ONE_HOUR_MILLONS = 60 * ONE_MINUTE_MILLIONS;
	public static final long ONE_DAY_MILLONS = 24 * ONE_HOUR_MILLONS;
	
	public static String getShortTime(String dateStr) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd kk:mm:ss z y", Locale.US);
		try {
			Date date = sdf.parse(dateStr);
			Date curDate = new Date();
			long durTime = curDate.getTime() - date.getTime();
			int dayStatus = calculateDayStatus(date, curDate);
			if(durTime <= 10*ONE_MINUTE_MILLIONS) {
				str = "刚刚";
			} else if(durTime < ONE_HOUR_MILLONS) {
				str = durTime / ONE_MINUTE_MILLIONS + "分钟前";
			} else if(dayStatus == 0) {
				str = durTime / ONE_HOUR_MILLONS + "小时前";
			} else if(dayStatus == 1) {
				str = "昨天" + DateFormat.format("kk:mm", date);
			} else if(isSameYear(date, curDate) && dayStatus > 1) {
				str = (String) DateFormat.format("MM-dd", date);
			} else {
				str =  (String) DateFormat.format("yyyy-MM", date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}
	private static int calculateDayStatus(Date tarDate, Date curDate) {
		Calendar tarCalendar = Calendar.getInstance();
		tarCalendar.setTime(tarDate);
		int tarDayOfYear = tarCalendar.get(Calendar.DAY_OF_YEAR);
		
		Calendar curCalendar = Calendar.getInstance();
		curCalendar.setTime(curDate);
		int curDateOfYear = curCalendar.get(Calendar.DAY_OF_YEAR);
		
		return curDateOfYear - tarDayOfYear;
	}
	private static boolean isSameYear(Date tarDate, Date curDate) {
		Calendar tarCalendar = Calendar.getInstance();
		tarCalendar.setTime(tarDate);
		int tarDayOfYear = tarCalendar.get(Calendar.YEAR);
		
		Calendar curCalendar = Calendar.getInstance();
		curCalendar.setTime(curDate);
		int curDateOfYear = curCalendar.get(Calendar.YEAR);
		
		return curDateOfYear == tarDayOfYear;
	}
}
