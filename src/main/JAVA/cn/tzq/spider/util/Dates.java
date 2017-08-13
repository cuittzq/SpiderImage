package cn.tzq.spider.util;

import java.lang.ref.SoftReference;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by igotti on 16-3-30.
 */
public abstract class Dates {


    /**
     * Date format pattern used to parse HTTP date headers in RFC 1123 format.
     */
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

    /**
     * Date format pattern used to parse HTTP date headers in RFC 1036 format.
     */
    public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";

    /**
     * Date format pattern used to parse HTTP date headers in ANSI C
     * {@code asctime()} format.
     */
    public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";

    /**
     *
     */
    public static final String PATTERN_CHINATIME = "yyyy年MM月dd日 HH:mm";

    /**
     *
     */
    public static final String PATTERN_CHINATIME_SIMPLFIED = "yyyy/MM/dd HH:mm";

    /**
     *
     */
    public static final String PATTERN_GENERALTIME = "yyyy-MM-dd HH:mm";

    /**
     *
     */
    public static final String PATTERN_GENERALTIME_FULL = "yyyy-MM-dd HH:mm:ss";

    private static final String[] DEFAULT_PATTERNS = new String[] {
            PATTERN_CHINATIME,
            PATTERN_CHINATIME_SIMPLFIED,
            PATTERN_GENERALTIME,
            PATTERN_GENERALTIME_FULL
    };

    private static final Date DEFAULT_TWO_DIGIT_YEAR_START;

    public static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    public static final TimeZone CST = TimeZone.getTimeZone("GMT+8:00");

    static {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(GMT);
        calendar.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
    }

    public static Date plusDay(Date date, int day) {
        return new Date(date.toInstant().plus(day, ChronoUnit.DAYS).toEpochMilli());
    }

    public static Date plusHour(Date date, int hour) {
        return new Date(date.toInstant().plus(hour, ChronoUnit.HOURS).toEpochMilli());
    }

    public static Date minusDay(Date date, int day) {
        return new Date(date.toInstant().minus(day, ChronoUnit.DAYS).toEpochMilli());
    }

    /**
     * 解析日期字符串到Java Date对象
     * @param dateValue 被转换日期字符串
     * @return 转换后的Date对象
     */
    public static Date parseDate(final String dateValue) {
        return parseDate(dateValue, null, null);
    }

    /**
     * 解析日期字符串到Java Date对象
     * @param dateValue 被转换日期字符串
     * @param dateFormats 指定的日期格式
     * @return 转换后的Date对象
     */
    public static Date parseDate(final String dateValue, final String[] dateFormats) {
        return parseDate(dateValue, dateFormats, null);
    }

    /**
     * 解析日期字符串到Java Date对象
     * @param dateValue 被转换日期字符串
     * @param dateFormats 指定的日期格式
     * @param startDate 当日期年份以2位表示时，相对的起始年份日期
     * @return 转换后的Date对象
     */
    public static Date parseDate(final String dateValue, final String[] dateFormats, final Date startDate) {
        Asserts.notNull(dateValue, "Date value");
        final String[] localDateFormats = dateFormats != null ? dateFormats : DEFAULT_PATTERNS;
        final Date localStartDate = startDate != null ? startDate : DEFAULT_TWO_DIGIT_YEAR_START;
        String v = dateValue;
        // trim single quotes around date if present
        // see issue #5279
        if (v.length() > 1 && v.startsWith("'") && v.endsWith("'")) {
            v = v.substring (1, v.length() - 1);
        }

        for (final String dateFormat : localDateFormats) {
            final SimpleDateFormat dateParser = DateFormatHolder.formatFor(dateFormat);
            dateParser.set2DigitYearStart(localStartDate);
            final ParsePosition pos = new ParsePosition(0);
            final Date result = dateParser.parse(v, pos);
            if (pos.getIndex() != 0) {
                return result;
            }
        }
        return null;
    }

    /**
     * 格式化日期字符串，默认按照格式 PATTERN_GENERALTIME
     * @param date 被格式化的Date对象
     * @return 格式化后的字符串
     */
    public static String formatDate(final Date date) {
        return formatDate(date, PATTERN_GENERALTIME);
    }

    /**
     * 格式化日期字符串，按照pattern指定的格式
     * @param date 被格式化的Date对象
     * @param pattern 格式化后的字符串
     * @return 格式化后的字符串
     */
    public static String formatDate(final Date date, final String pattern) {
        Asserts.notNull(date, "Date");
        Asserts.notNull(pattern, "Pattern");
        final SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
        return formatter.format(date);
    }

    /**
     * 获取指定日期月份的最大日期（当月最后一天的日期, 忽略时分秒毫秒）
     * @param date
     * @return
     */
    public static Date getMaxDayOfMonth(Date date) {
        Calendar instance = Calendar.getInstance();
        if (date != null) {
            instance.setTime(date);
        }
        instance.set(Calendar.DATE, 1);
        instance.roll(Calendar.DATE, -1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTime();
    }

    /**
     * 获取指定日期月份的最小日期（当月第一天日期, 忽略时分秒毫秒）
     * @param date
     * @return
     */
    public static Date getMinDayOfMonth(Date date) {
        Calendar instance = Calendar.getInstance();
        if (date != null) {
            instance.setTime(date);
        }
        instance.set(Calendar.DATE, 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTime();
    }

    /**
     * 获取当前月的第一天
     * 由石广路（shiguanglu@simpletour.com）添加于2016-5-7
     *
     * @return
     */
    public static Date getFistDateOfMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取当前月的最后一天
     * 由石广路（shiguanglu@simpletour.com）添加于2016-5-7
     *
     * @return
     */
    public static Date getLastDateOfMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 获取当前时间，并且时分秒，毫秒为0
     * @return
     */
    public static Date getCurrentDateWithoutHMSM(){
        Date now = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(now);
        // 将时分秒,毫秒域清零
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        return cal1.getTime();
    }

    /**
     *
     * @param begin 开始日期，精确到天
     * @param end 结束日期, 精确到天
     * @return 从开始日期到结束日期的日期集合，开始到结束日期以一天依次递增
     */
    public static List<Date> getContinuousDates(Date begin,Date end){
        List<Date> list = new ArrayList<>();
        if (begin != null && end != null){
            for(Date i = begin;i.before(end) || i.equals(end);){
                list.add(i);
                i = Date.from(i.toInstant().plus(1,ChronoUnit.DAYS));
            }
        }
        return list;
    }
    /**
     * 线程结束时应该调用该方法
     */
    public static void clearThreadLocal() {
        DateFormatHolder.clearThreadLocal();
    }

    /**
     * A factory for {@link SimpleDateFormat}s. The instances are stored in a
     * threadlocal way because SimpleDateFormat is not threadsafe as noted in
     * {@link SimpleDateFormat its javadoc}.
     *
     */
    final static class DateFormatHolder {

        private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>() {

            @Override
            protected SoftReference<Map<String, SimpleDateFormat>> initialValue() {
                return new SoftReference<Map<String, SimpleDateFormat>>(new HashMap<String, SimpleDateFormat>());
            }

        };

        /**
         * creates a {@link SimpleDateFormat} for the requested format string.
         *
         * @param pattern
         *            a non-{@code null} format String according to
         *            {@link SimpleDateFormat}. The format is not checked against
         *            {@code null} since all paths go through
         *            {@link Dates}.
         * @return the requested format. This simple dateformat should not be used
         *         to {@link SimpleDateFormat#applyPattern(String) apply} to a
         *         different pattern.
         */
        public static SimpleDateFormat formatFor(final String pattern) {
            final SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
            Map<String, SimpleDateFormat> formats = ref.get();
            if (formats == null) {
                formats = new HashMap<String, SimpleDateFormat>();
                THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
            }

            SimpleDateFormat format = formats.get(pattern);
            if (format == null) {
                format = new SimpleDateFormat(pattern, Locale.CHINA);
                format.setTimeZone(Dates.CST);
                formats.put(pattern, format);
            }

            return format;
        }

        public static void clearThreadLocal() {
            THREADLOCAL_FORMATS.remove();
        }

    }

}
