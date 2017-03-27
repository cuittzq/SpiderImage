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
        Map<String, List<String>> imagemap = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            String key = String.format("桌面壁纸%d", i);
            imagemap.put(key, new ArrayList<>());
            for (int j = 1; j < 12; j++) {
                imagemap.get(key).add(String.format("http://tupian.enterdesk.com/2012/0608/gha/7/%d.jpg", j));
            }
        }
        try {
            downloadImages.downloadPicture(imagemap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
