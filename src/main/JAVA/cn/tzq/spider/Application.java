package cn.tzq.spider;

import cn.tzq.spider.biz.DownloadImages;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by tzq139 on 2017/3/22.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
//    public static void main(String[] args) {
//        ApplicationContext ctx = SpringApplication.run(Application.class);
//        DownloadImages downloadImages = (DownloadImages) ctx.getBean("downloadImages");
//
//        // run in a second
//        final long timeInterval = 1000 * 60 * 5;
//        Runnable runnable = new Runnable() {
//            public void run() {
//                while (true) {
//                    try {
//                        System.out.println("开始下载美女图片=======》》》");
//                        downloadImages.downloadPicture();
//                        Thread.sleep(timeInterval);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        Thread thread = new Thread(runnable);
//        thread.start();
//    }

    public static void main(String[] args) throws IOException {
        System.setProperty("http.maxRedirects", "50");
        System.getProperties().setProperty("proxySet", "true");
        String ip = "106.59.121.164";
        System.getProperties().setProperty("http.proxyHost", ip);
        System.getProperties().setProperty("http.proxyPort", "8998");

        //确定代理是否设置成功
        String urlinfo = getHtml("http://www.ip138.com/ip2city.asp");
        System.out.println(urlinfo);
    }

    private static String getHtml(String address) {
        StringBuffer html = new StringBuffer();
        String result = null;
        try {
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());

            try {
                String inputLine;
                byte[] buf = new byte[4096];
                int bytesRead = 0;
                while (bytesRead >= 0) {
                    inputLine = new String(buf, 0, bytesRead, "ISO-8859-1");
                    html.append(inputLine);
                    bytesRead = in.read(buf);
                    inputLine = null;
                }
                buf = null;
            } finally {
                in.close();
                conn = null;
                url = null;
            }
            result = new String(html.toString().trim().getBytes("ISO-8859-1"), "gb2312").toLowerCase();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            html = null;
        }
        return result;
    }
}
