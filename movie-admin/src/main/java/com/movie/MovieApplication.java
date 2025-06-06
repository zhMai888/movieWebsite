package com.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MovieApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(MovieApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  观影斋启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " ________       ___    ___  ________     \n" +
                "|\\   ____\\     |\\  \\  /  /||\\_____  \\    \n" +
                "\\ \\  \\___|     \\ \\  \\/  / / \\|___/  /|   \n" +
                " \\ \\  \\  ___    \\ \\    / /      /  / /   \n" +
                "  \\ \\  \\|\\  \\    \\/  /  /      /  /_/__  \n" +
                "   \\ \\_______\\ __/  / /       |\\________\\\n" +
                "    \\|_______||\\___/ /         \\|_______|\n" +
                "              \\|___|/     \n");

    }
}
