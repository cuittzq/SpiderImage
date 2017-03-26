package cn.tzq.spider;

import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.service.BeautyGirlService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by tzq139 on 2017/3/22.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        BeautyGirlService beautyGirlService = (BeautyGirlService) ctx.getBean("beautyGirlService");
        BeautyGirls beautyGirlInfo = beautyGirlService.findOne(200);

        System.out.print(String.format("%s", beautyGirlInfo.toString()));
    }
}
