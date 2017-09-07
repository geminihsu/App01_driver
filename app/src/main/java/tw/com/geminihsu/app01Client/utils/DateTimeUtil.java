package tw.com.geminihsu.app01Client.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {

	public DateTimeUtil() {
	
	}
	
	/**
	 * 傳入PosixTime(秒) 轉成  YYYY-MM-DD HH:mm:ss
	 * @param posixTime_second : 單位_秒
	 * */
	public static String convertPosixTimeToString_yyyymmdd_hhmmss(long posixTime_second)
	{
		Date myDate= new Date();;
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");;
		String timestr="";
		myDate.setTime((long) posixTime_second * 1000);
		timestr = formatter2.format(myDate);
		return timestr;
	}
	
	/**
	 * 傳入PosixTime(秒) 轉成  YYYY-MM-DD HH:mm:ss(不加TimeZone)
	 * @param posixTime_second : 單位_秒
	 * */
	public static String convertUTCPosixTimeToString_yyyymmdd_hhmmss(long posixTime_second)
	{
		Date myDate= new Date();;
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");;
		String timestr="";
		formatter2.setTimeZone(TimeZone.getTimeZone("UTC"));
		myDate.setTime((long) posixTime_second * 1000);
		timestr = formatter2.format(myDate);
		return timestr;
	}

	/**
	 * 傳入PosixTime(豪秒) 轉成  YYYY-MM-DD HH:mm:ss
	 * @param posixTime_second : 單位_豪秒
	 * */
	public static String convertMillisecondsTimeToString_yyyymmdd_hhmmss(long milliseconds)
	{
		Date myDate= new Date();;
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");;
		String timestr="";
		myDate.setTime((long) milliseconds);
		timestr = formatter2.format(myDate);
		return timestr;
	}
	
	/**
	 * 傳入PosixTime(豪秒) 轉成  YYYY-MM-DD
	 * @param milliseconds : 單位_豪秒
	 * */
	public static String convertMillisecondsTimeToString_yyyymmdd(long milliseconds)
	{
		Date myDate= new Date();;
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");;
		String timestr="";
		myDate.setTime(milliseconds);
		timestr = formatter2.format(myDate);
		return timestr;
	}
	
	/**
	 * 傳入字串日期 轉成PosixTime(秒)
	 * @param dt 日/月/年
	 * @return
	 */
	public static long convertString_yyyymmddToMillisecondsTime(String dt) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date;
	    long epoch=0;
		try {
			date = df.parse(dt);
			epoch = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
        return epoch;
    }
	
	
	public static Calendar convertPosixTimeToCalendar(long posixTime_second){
		Calendar calendar = Calendar.getInstance();
		Date myDate= new Date();;
		myDate.setTime((long) posixTime_second * 1000);
		calendar.setTime(myDate);
	    return calendar;
	}
	
	
	public static Calendar localTimeToGmtTime(Calendar localTime)
	{
		Date gmtDate = localDateToGmtDate(localTime.getTime());
		
		Calendar gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		gmtCalendar.setTime(gmtDate);
		
		return gmtCalendar;
	}
	
	public static  Date localDateToGmtDate( Date date ){
	    TimeZone tz = TimeZone.getDefault();
	    Date ret = new Date( date.getTime() - tz.getRawOffset() );

	    // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
	    if ( tz.inDaylightTime( ret )){
	        Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );

	        // check to make sure we have not crossed back into standard time
	        // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
	        if ( tz.inDaylightTime( dstDate )){
	            ret = dstDate;
	        }
	     }
	     return ret;
	}
	
	
	/**
	 * 傳入date 轉成  YYYY-MM-DD
	 * */
	public static String convertDateTimeToString_yyyymmdd(Date myDate)
	{
		
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");;
		//formatter2.setTimeZone(TimeZone.getTimeZone("GMT"));
		String timestr="";
		timestr = formatter2.format(myDate);
		return timestr;
	}
}
