package cn.tzq.spider;

import cn.tzq.spider.biz.DownloadImages;
import cn.tzq.spider.util.RedisTemplateUtils;
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
        try {
            downloadImages.downloadPicture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//
//    public static void main(String[] args) {
//        ApplicationContext ctx = SpringApplication.run(Application.class);
////        DownloadImages downloadImages = (DownloadImages) ctx.getBean("downloadImages");
//        RedisTemplateUtils redisTemplateUtils = (RedisTemplateUtils) ctx.getBean("redisTemplateUtils");
//        redisTemplateUtils.set("hellokey", "123213123", 1000L);
//        System.out.println(redisTemplateUtils.getObj("hellokey").toString());
//    }
//
//    private static String getHtml(String address) {
//        StringBuffer html = new StringBuffer();
//        String result = null;
//        try {
//            URL url = new URL(address);
//            URLConnection conn = url.openConnection();
//            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
//            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
//
//            try {
//                String inputLine;
//                byte[] buf = new byte[4096];
//                int bytesRead = 0;
//                while (bytesRead >= 0) {
//                    inputLine = new String(buf, 0, bytesRead, "ISO-8859-1");
//                    html.append(inputLine);
//                    bytesRead = in.read(buf);
//                    inputLine = null;
//                }
//                buf = null;
//            } finally {
//                in.close();
//                conn = null;
//                url = null;
//            }
//            result = new String(html.toString().trim().getBytes("ISO-8859-1"), "gb2312").toLowerCase();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            html = null;
//        }
//        return result;
//    }
}
