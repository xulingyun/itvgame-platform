package cn.ohyeah.itvgame.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;

public class DateUtil {

	public static final String PATTERN_DEFAULT = "yyyyMMddHHmmss";

	public static final String PATTERN_DATETIME = "yyyy-MM-dd";
	
	public static final String PATTERN_1 = "yyyy-MM-dd HH:mm:ss";
	
	public static final String PATTERN_2 = "MM月dd日HH时mm分";
	
	public static final String PATTERN_3 = "yyyy-MM-dd HH:mm";
	
	public static final String PATTERN_4 = "yyyyMMdd";
	
	public static final String PATTERN_5 = "yyyyMMddHHmm";
	
	/**
	 * 返回当时间戳,默认格式为yyyyMMddHHmmss
	 * 
	 * @param patternFormat
	 * 
	 *            返回时间格式
	 * @return
	 */
	public static String createTimeId(String patternFormat) {
		Date now = new Date();
		return new SimpleDateFormat(
				StringUtils.isEmpty(patternFormat) ? PATTERN_DEFAULT
						: patternFormat).format(now);
	}

	public static Date parseDate(String datestring, String pattern) {
		try {
			if(datestring==null||datestring.equals("")){
				return null;
			}else
				return new SimpleDateFormat(pattern).parse(datestring);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatDate(Date date, String pattern) {
		if(date==null){
			return "";
		}else
			return new SimpleDateFormat(
				StringUtils.isEmpty(pattern) ? PATTERN_DEFAULT : pattern)
				.format(date);
	}
	
	public static Timestamp convertToSqlDate(Date date) {
		return date==null?null:new Timestamp(date.getTime());
	}
	
	public static Timestamp convertToSqlDate(Timestamp date) {
		return date;
	}
	
	public static Date convertToUtilDate(Timestamp date) {
		return date==null?null:new Date(date.getTime());
	}
	
	public static Date convertToUtilDate(Date date) {
		return date;
	}
	
	public static Date getMonthStartTime(Date t) {
		Calendar c = Calendar.getInstance();
		c.setTime(t);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		c.set(Calendar.MILLISECOND, c.getActualMinimum(Calendar.MILLISECOND));
		return c.getTime();
	}
	
	public static Date getMonthEndTime(Date t) {
		Calendar c = Calendar.getInstance();
		c.setTime(t);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
		c.set(Calendar.MILLISECOND, c.getActualMaximum(Calendar.MILLISECOND));
		return c.getTime();
	}
	
}
