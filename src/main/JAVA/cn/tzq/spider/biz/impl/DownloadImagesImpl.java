package cn.tzq.spider.biz.impl;

import cn.tzq.spider.biz.DownloadImages;
import cn.tzq.spider.biz.ImageDownHandle;
import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.model.ImageDownEvent;
import cn.tzq.spider.proxypool.HttpProxy;
import cn.tzq.spider.proxypool.ProxyPool;
import cn.tzq.spider.service.BeautyGirlService;
import cn.tzq.spider.util.RedisTemplateUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;
import reactor.event.Event;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static reactor.event.selector.Selectors.$;

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
        Environment env = new Environment();
        Reactor reactor = Reactors.reactor().env(env).dispatcher(Environment.THREAD_POOL).get();

        while (true) {
            // 1获取最新的100条数据
            Page<BeautyGirls> beautyGirlslist = this.beautyGirlService.findbeautygirls(100);
            // 2按照主题分组
            imagemap = beautyGirlslist.getContent().stream().distinct().collect(Collectors.groupingBy(BeautyGirls::getImageTheme));
            httpproxy = proxyPool.borrow();
            imagemap.forEach((key, imagelist) -> {
                try {

                    reactor.on($(key), imageDownHandle::Download);

                    imagelist = imagelist.stream().distinct().collect(Collectors.toList());

                    reactor.notify(key, Event.wrap(new ImageDownEvent(imagelist, key, httpproxy)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread.sleep(1000 * 60 * 2);
        }
    }
}
