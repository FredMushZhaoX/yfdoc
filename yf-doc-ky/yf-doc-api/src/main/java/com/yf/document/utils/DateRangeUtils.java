package com.yf.document.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间段计算工具类
 * @author bool
 */
public class DateRangeUtils {


    /**
     * 时间格式
     */
    public static SimpleDateFormat startFmt = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    public static SimpleDateFormat endFmt = new SimpleDateFormat("yyyy-MM-dd 23:59:59");


    /**
     * 根据标识构建时间段，1为今日，2为本周，3为本月
     * @param rankingType
     * @return
     */
    public static String [] generateRange(int rankingType){

        switch (rankingType){
            case 1:
                return generateToday();
            case 2:
                return generateWeek();
            case 3:
                return generateMonth();
            default:
                return generateYear();

        }
    }


    /**
     * 今天的开始结束时间
     * @return
     */
    public static String [] generateToday(){

        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());

        String startTime = startFmt.format(cl.getTime());
        String endTime = endFmt.format(cl.getTime());

        return new String[]{startTime, endTime};
    }


    /**
     * 本周的开始结束时间
     * @return
     */
    public static String [] generateWeek(){

        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());

        cl.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String startTime = startFmt.format(cl.getTime());

        cl.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String endTime = endFmt.format(cl.getTime());

        return new String[]{startTime, endTime};
    }

    /**
     * 本月的的开始结束时间
     * @return
     */
    public static String [] generateMonth(){

        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());

        cl.set(Calendar.DAY_OF_MONTH, cl.getActualMinimum(Calendar.DAY_OF_MONTH));
        String startTime = startFmt.format(cl.getTime());

        cl.set(Calendar.DAY_OF_MONTH, cl.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endTime = endFmt.format(cl.getTime());

        return new String[]{startTime, endTime};
    }

    /**
     * 获取近一年的时间
     * @return
     */
    public static String [] generateYear(){

        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());

        String endTime = endFmt.format(cl.getTime());
        cl.add(Calendar.DATE, -365);
        String startTime = startFmt.format(cl.getTime());

        return new String[]{startTime, endTime};
    }
}
