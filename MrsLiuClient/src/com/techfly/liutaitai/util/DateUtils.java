package com.techfly.liutaitai.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
   /**
    * 根据时间格式得到相应的格式时间
    * @param timeMills
    * @param format
    * @return
    */
   public static String getTime(long timeMills, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String time = sdf.format(new Date(timeMills));
        return time;
    }
   /**
    * 根据时间的得到周几
    * @param timeMills
    * @return
    */
    public static String getWeek(long timeMills) {
        Date date = new Date(timeMills);
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        return dateFm.format(date);
    }
    /**
     * 根据现在所处的时刻获取毫秒数
     * @param timeMills
     * @param time
     * @return
     */
    public static long currentMills(long timeMills,String time) {
        String ftime = getTime(timeMills, "yyyy-MM-dd");
        // 先把字符串转成Date类型
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 此处会抛异常
        Date date = null;
        try {
            date = sdf.parse(ftime+" "+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 获取毫秒数
        long longDate = date.getTime();
        return longDate;
    }
}
