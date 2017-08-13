package cn.tzq.spider.biz.imagespider.impl;

import cn.tzq.spider.biz.imagespider.DownloadImages;
import cn.tzq.spider.biz.imagespider.EventHandler;
import cn.tzq.spider.biz.imagespider.EventOnlyDownloadHandler;
import cn.tzq.spider.biz.imagespider.ImageDownHandle;
import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.model.ImageDownEvent;
import cn.tzq.spider.proxypool.HttpProxy;
import cn.tzq.spider.proxypool.ProxyPool;
import cn.tzq.spider.service.BeautyGirlService;
import cn.tzq.spider.util.Dates;
import cn.tzq.spider.util.RedisTemplateUtils;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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

    @Resource
    private BeautyGirlService beautyGirlService;

    @Resource
    private ProxyPool proxyPool;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Resource
    ImageDownHandle imageDownHandle;

    private HttpProxy httpproxy;

    @Override
    public void downloadPicture() throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        int bufferSize = 1024;
        Disruptor<ImageDownEvent> disruptor = new Disruptor<>(ImageDownEvent::new, bufferSize, executor,
                ProducerType.SINGLE, new LiteBlockingWaitStrategy());
//        EventOnlyDownloadHandler(disruptor);
        EventHandler(disruptor);
    }


    /**
     * 下载事件处理器
     *
     * @param disruptor
     * @throws InterruptedException
     */
    private void EventHandler(Disruptor<ImageDownEvent> disruptor) throws InterruptedException {

        disruptor.handleEventsWithWorkerPool(new EventHandler(beautyGirlService), new EventHandler(beautyGirlService),
                new EventHandler(beautyGirlService), new EventHandler(beautyGirlService));
        disruptor.start();

        produceEvents(disruptor);
    }


    /**
     * 按照存储规则下载处理器
     *
     * @param disruptor
     * @throws InterruptedException
     */
    private void EventOnlyDownloadHandler(Disruptor<ImageDownEvent> disruptor) throws InterruptedException {

        disruptor.handleEventsWithWorkerPool(new EventOnlyDownloadHandler(), new EventOnlyDownloadHandler(),
                new EventOnlyDownloadHandler(), new EventOnlyDownloadHandler());
        disruptor.start();
        produceEventsforImage(disruptor);
    }

    /**
     * 发布下载事件（从数据库中获取连接下载）
     *
     * @param disruptor
     * @throws InterruptedException
     */
    private void produceEvents(Disruptor<ImageDownEvent> disruptor) throws InterruptedException {
        RingBuffer<ImageDownEvent> ringBuffer = disruptor.getRingBuffer();
        try {
            while (true) {
                // 1获取最新的100条数据
                Page<BeautyGirls> beautyGirlslist = this.beautyGirlService.findbeautygirls(100);
                // 2按照主题分组
                Map<String, List<BeautyGirls>> imagemap = beautyGirlslist.getContent().stream().distinct().collect(Collectors.groupingBy(BeautyGirls::getImageTheme));
                // httpproxy =proxyPool.getIdleNum()
                imagemap.forEach((key, imagelist) -> ringBuffer.publishEvent((event, sequence) -> {
                    event.setImageList(imagelist.stream().distinct().collect(Collectors.toList()));
                    event.setImageTheme(key);
                }));
                Thread.sleep(1000 * 60);
            }

        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
            // ex.printStackTrace();
        }
    }

    /**
     * 按照存储规则下载
     *
     * @param disruptor
     * @throws InterruptedException
     */
    private void produceEventsforImage(Disruptor<ImageDownEvent> disruptor) throws InterruptedException {
        RingBuffer<ImageDownEvent> ringBuffer = disruptor.getRingBuffer();
        try {
            Date startdate = new Date(2016, 5, 3);
            for (int k = 0; k < 200; k++) {
                System.out.println(String.format("开始下载第%s天的数据。。。", Dates.formatDate(startdate, "yyMMdd")));
                List<BeautyGirls> beautyGirlsList = new ArrayList<>();

                for (int i = 1; i < 10; i++) {
                    for (int j = 1; j < 50; j++) {
                        BeautyGirls beautyGirl = new BeautyGirls();
                        beautyGirl.setImageTheme(String.format("%s-pic%d", Dates.formatDate(startdate, "yyMMdd"), i));
                        String imageUrl = String.format("http://img15.yixiu8.com:8080/picture/%s/pic%d/%d.jpg", Dates.formatDate(startdate, "yyMMdd"), i, j);
                        beautyGirl.setImageUrl(imageUrl);
                        beautyGirlsList.add(beautyGirl);
                    }

                    if (beautyGirlsList.size() == 0) {
                        continue;
                    }

                    // 2按照主题分组
                    Map<String, List<BeautyGirls>> imagemap = beautyGirlsList.stream().distinct().collect(Collectors.groupingBy(BeautyGirls::getImageTheme));
                    // httpproxy =proxyPool.getIdleNum()
                    imagemap.forEach((key, imagelist) -> ringBuffer.publishEvent((event, sequence) -> {
                        event.setImageList(imagelist.stream().distinct().collect(Collectors.toList()));
                        event.setImageTheme(key);
                        event.setHttpproxy(proxyPool.borrow());
                    }));

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Thread.sleep(1000 * 60);
                startdate = Dates.plusDay(startdate, 1);
            }
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
            // ex.printStackTrace();
        }

    }

}
