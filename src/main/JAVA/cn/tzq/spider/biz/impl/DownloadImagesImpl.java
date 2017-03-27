package cn.tzq.spider.biz.impl;

import cn.tzq.spider.biz.ApplicationContextProvider;
import cn.tzq.spider.biz.DownloadImages;
import cn.tzq.spider.biz.ImageDownTask;
import cn.tzq.spider.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-03-27 14:36
 **/
@Component("downloadImages")
public class DownloadImagesImpl implements DownloadImages {

    ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public DownloadImagesImpl(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /**
     * 传入要下载的图片的url列表，将url所对应的图片下载到本地
     *
     * @param imagemap
     */
    @Override
    public void downloadPicture(Map<String, List<String>> imagemap) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(imagemap.size());
        imagemap.forEach((key, imagelist) -> {
            try {
                taskExecutor.execute(new ImageDownTask(key, imagelist, countDownLatch));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("主线程开始等待...");
        countDownLatch.await();
        System.out.printf("所有线程执行完毕！");
    }
}
