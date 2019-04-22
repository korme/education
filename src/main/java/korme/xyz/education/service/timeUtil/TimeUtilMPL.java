package korme.xyz.education.service.timeUtil;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class TimeUtilMPL implements TimeUtils {
    @Override
    public String getNowTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = format.format(date);
        return nowtime;
    }
    /*
     * 返回年月日格式时间
     * */
    @Override
    public String getYearToDay(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nowtime = format.format(date);
        return nowtime;
    }
    public static String NOW(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowtime = format.format(date);
        return nowtime;
    }
}
