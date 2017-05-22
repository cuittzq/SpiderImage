package cn.tzq.spider.biz.imagespider.impl;

import cn.tzq.spider.biz.imagespider.DownloadImages;
import cn.tzq.spider.biz.imagespider.EventHandler;
import cn.tzq.spider.biz.imagespider.ImageDownHandle;
import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.model.ImageDownEvent;
import cn.tzq.spider.proxypool.HttpProxy;
import cn.tzq.spider.proxypool.ProxyPool;
import cn.tzq.spider.service.BeautyGirlService;
import cn.tzq.spider.util.RedisTemplateUtils;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private Map<String, List<BeautyGirls>> imagemap = new HashMap<>();

    @Override
    public void downloadPicture() throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        int bufferSize = 1024;
        Disruptor<ImageDownEvent> disruptor = new Disruptor<>(ImageDownEvent::new, bufferSize, executor,
                ProducerType.SINGLE, new LiteBlockingWaitStrategy());

        disruptor.handleEventsWithWorkerPool(new EventHandler(beautyGirlService), new EventHandler(beautyGirlService),
                new EventHandler(beautyGirlService), new EventHandler(beautyGirlService));
        disruptor.start();
        produceEvents(disruptor);
    }

    private void produceEvents(Disruptor<ImageDownEvent> disruptor) throws InterruptedException {
        RingBuffer<ImageDownEvent> ringBuffer = disruptor.getRingBuffer();
        try {
            while (true) {
                // 1获取最新的100条数据
                Page<BeautyGirls> beautyGirlslist = this.beautyGirlService.findbeautygirls(100);
                // 2按照主题分组
                imagemap = beautyGirlslist.getContent().stream().distinct().collect(Collectors.groupingBy(BeautyGirls::getImageTheme));
                // httpproxy =proxyPool.getIdleNum()
                imagemap.forEach((key, imagelist) -> ringBuffer.publishEvent((event, sequence) -> {
                    event.setImageList(imagelist.stream().distinct().collect(Collectors.toList()));
                    event.setImageTheme(key);
                }));

                Thread.sleep(1000 * 60 * 2);
            }

        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
            // ex.printStackTrace();
        }
    }

}
