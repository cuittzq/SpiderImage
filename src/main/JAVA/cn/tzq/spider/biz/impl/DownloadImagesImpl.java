package cn.tzq.spider.biz.impl;

import cn.tzq.spider.biz.ApplicationContextProvider;
import cn.tzq.spider.biz.DownloadImages;
import cn.tzq.spider.biz.ImageDownTask;
import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.proxypool.ProxyPool;
import cn.tzq.spider.service.BeautyGirlService;
import cn.tzq.spider.util.FileUtil;
import cn.tzq.spider.util.RedisTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-03-27 14:36
 **/
@Component("downloadImages")
public class DownloadImagesImpl implements DownloadImages {

    private ThreadPoolTaskExecutor taskExecutor;

    private BeautyGirlService beautyGirlService;

    private ProxyPool proxyPool;

    private RedisTemplateUtils redisTemplateUtils;

    private Map<String, List<BeautyGirls>> imagemap = new HashMap<>();

    @Autowired
    public DownloadImagesImpl(ThreadPoolTaskExecutor taskExecutor, BeautyGirlService beautyGirlService, RedisTemplateUtils redisTemplateUtils, ProxyPool proxyPool) {
        this.taskExecutor = taskExecutor;
        this.beautyGirlService = beautyGirlService;
        this.redisTemplateUtils = redisTemplateUtils;
        this.proxyPool = proxyPool;

    }

    @Override
    public void downloadPicture() throws InterruptedException {
        // 1获取最新的100条数据
        Page<BeautyGirls> beautyGirlslist = this.beautyGirlService.findbeautygirls(100);
        // 2按照主题分组
//        beautyGirlslist.forEach((beautgirls) -> {
//            if (!imagemap.containsKey(beautgirls.getImageTheme())) {
//                imagemap.put(beautgirls.getImageTheme(), new ArrayList<>());
//            }
//
//            imagemap.get(beautgirls.getImageTheme()).add(beautgirls);
//        });

        imagemap = beautyGirlslist.getContent().stream().distinct().collect(Collectors.groupingBy(BeautyGirls::getImageTheme));

        // 3 多线程下载
        final CountDownLatch countDownLatch = new CountDownLatch(imagemap.size());
        imagemap.forEach((key, imagelist) -> {
            try {
                imagelist = imagelist.stream().distinct().collect(Collectors.toList());
                taskExecutor.execute(new ImageDownTask(key, imagelist, countDownLatch, beautyGirlService, this.proxyPool, this.redisTemplateUtils));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("主线程开始等待...");
        countDownLatch.await();
        System.out.printf("所有线程执行完毕！");
    }
}
