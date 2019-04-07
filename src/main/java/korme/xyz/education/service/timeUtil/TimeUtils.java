package korme.xyz.education.service.timeUtil;

import org.springframework.stereotype.Service;

@Service
public interface TimeUtils {
    //yyyy-MM-dd HH:mm:ss
    String getNowTime();
    //yyyy-MM-dd
    String getYearToDay();
}
