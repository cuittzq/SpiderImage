package cn.tzq.spider;

import cn.tzq.spider.biz.imagespider.DownloadImages;
import cn.tzq.spider.biz.tctravel.disruptordemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;


/**
 * Created by tzq139 on 2017/3/22.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class);
        DownloadImages downloadImages = (DownloadImages) ctx.getBean("downloadImages");
//        disruptordemo disruptordemo = (disruptordemo) ctx.getBean("disruptordemo");
        try {
            downloadImages.downloadPicture();
//            disruptordemo.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
