package cn.tzq.spider.biz.imagespider.impl;

import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.proxypool.HttpProxy;
import cn.tzq.spider.proxypool.ProxyPool;
import cn.tzq.spider.service.BeautyGirlService;
import cn.tzq.spider.util.FileUtil;
import cn.tzq.spider.util.RedisTemplateUtils;
import com.sun.jndi.toolkit.ctx.Continuation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-03-27 16:07
 **/


@Getter
@Setter
@Component("imageDownTask")
@Scope("prototype")
public class ImageDownTask implements Runnable {

    private List<BeautyGirls> imageList;

    private String imageTheme;

    final String rootPath = "D:/Images";

    private CountDownLatch countDownLatch;

    private BeautyGirlService beautyGirlService;

    private RedisTemplateUtils redisTemplateUtils;

    private HttpProxy httpproxy;

    /**
     * 构造函数
     */
    @Autowired
    public ImageDownTask(BeautyGirlService beautyGirlService, RedisTemplateUtils redisTemplateUtils) {
        this.beautyGirlService = beautyGirlService;
        this.redisTemplateUtils = redisTemplateUtils;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println(String.format("%s, 开始！", this.imageTheme));
        try {
            this.imageList.forEach((imageurl) -> {
                try {
                    DataInputStream dataInputStream = getImageInputStream(imageurl);
                    if (dataInputStream == null) {
                        imageurl.setDownload(2);
                        this.beautyGirlService.upDate(imageurl);
                        return;
                    }

                    String imagePath = CreateFilePath(this.imageTheme, imageurl.getImageUrl());
                    synchronized (imagePath) {
                        // 保存文件
                        FileUtil.writeFileFromInputStream(dataInputStream, imagePath);
                    }
                    dataInputStream.close();
                    imageurl.setDownload(1);
                    this.beautyGirlService.upDate(imageurl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } finally {
            this.countDownLatch.countDown();
        }

        System.out.println(String.format("%s, 结束！", this.imageTheme));
    }

    /**
     * 使用代理下载重试三次
     *
     * @param beautyGirl
     * @return
     * @throws IOException
     */
    private DataInputStream getImageInputStream(BeautyGirls beautyGirl) {

        DataInputStream dataInputStream = null;
        try {
            // 初始化proxy对象
            Long time = System.currentTimeMillis();
            URL url = new URL(beautyGirl.getImageUrl());
            URLConnection conn = url.openConnection(httpproxy.getNoProxy());
            conn.setReadTimeout(10 * 1000);
            dataInputStream = new DataInputStream(conn.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return dataInputStream;
    }

    /**
     * 构造文件路径
     *
     * @param folder
     * @param imageurl
     * @return
     */
    private String CreateFilePath(String folder, String imageurl) {
        return String.format("%s/%s/%s", rootPath, folder, imageurl.substring(imageurl.lastIndexOf("/") + 1, imageurl.length()));
    }
}
