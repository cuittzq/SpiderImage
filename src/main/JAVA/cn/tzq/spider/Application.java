package cn.tzq.spider;

import cn.tzq.spider.biz.DownloadImages;
import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.service.BeautyGirlService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

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
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        BeautyGirlService beautyGirlService = (BeautyGirlService) ctx.getBean("beautyGirlService");
        DownloadImages downloadImages = (DownloadImages) ctx.getBean("downloadImages");

        Map<String, List<String>> imagemap = new HashMap<>();
        imagemap.put("桌面壁纸", new ArrayList<>());
        for (int i = 1; i < 100; i++) {
            imagemap.get("桌面壁纸").add(String.format("http://tupian.enterdesk.com/2012/0608/gha/7/%d.jpg", i));
        }

        downloadImages.downloadPicture(imagemap);
    }
}
