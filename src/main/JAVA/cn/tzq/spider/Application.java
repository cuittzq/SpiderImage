package cn.tzq.spider;

import cn.tzq.spider.biz.DownloadImages;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by tzq139 on 2017/3/22.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class);
        DownloadImages downloadImages = (DownloadImages) ctx.getBean("downloadImages");
        try {
            downloadImages.downloadPicture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
