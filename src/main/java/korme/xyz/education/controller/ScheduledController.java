package korme.xyz.education.controller;

import korme.xyz.education.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class ScheduledController {
    @Autowired
    ConfigMapper configMapper;
    @Value("${deleteCycle}")
    int deleteCycle;
    //todo: 无法启动
    @Scheduled(cron = "0 0 3 * * ?")
    public void dailyDeleteMessage(){
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 0-deleteCycle);
        String endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
        configMapper.deleteMessage(endDate);
    }

    @Scheduled(cron = "0 0 3 1 * ?")
    public void resetScore(){
        configMapper.resetScore();
    }
}
