package com.movie.quartz.task;

import com.movie.data.mapper.MoviesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("movieTask") // 注意这个名称在定时任务中要用
public class MovieTask {

    @Autowired
    private MoviesMapper moviesMapper;

    public void resetWeekCount() {
        moviesMapper.resetWeekCount(); // 执行清零
        System.out.println("电影 weekcount 已重置为0");
    }
    public void resetMonthCount() {
        moviesMapper.resetMonthCount(); // 执行清零
        System.out.println("电影 monthcount 已重置为0");
    }
}
