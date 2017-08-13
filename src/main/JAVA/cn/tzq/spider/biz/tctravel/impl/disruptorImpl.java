package cn.tzq.spider.biz.tctravel.impl;

import cn.tzq.spider.biz.tctravel.EventHandler;
import cn.tzq.spider.biz.tctravel.ObjectEvent;
import cn.tzq.spider.biz.tctravel.disruptordemo;
import cn.tzq.spider.model.tctravel.CityInfo;
import cn.tzq.spider.model.tctravel.TcCiytResp;
import cn.tzq.spider.util.RedisTemplateUtils;
import cn.tzq.spider.util.http.Http;
import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-23 11:08
 **/
@Component("disruptordemo")
public class disruptorImpl implements disruptordemo {
    private String baseurl = "http://www.ly.com/bustour/json/shorttoursearch.html?page=%d&PageSize=%d&keyword=\"%s\"&days=0&endcityids=&scityId=%d&provid=0&rsceneryId=0&districtAndCountyIds=&themeId=0&tourtype=0&SortType=0";

    private String cityurl = "http://www.ly.com/bustour/json/getallstartcity.html?action=GETYRYCITYLIST&isReadCache=0";

    private Integer pageSize = 10;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    @Override
    public void start() throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        int bufferSize = 1024;
        Disruptor<ObjectEvent> disruptor = new Disruptor<>(ObjectEvent::new, bufferSize, executor,
                ProducerType.SINGLE, new LiteBlockingWaitStrategy());
        disruptor.handleEventsWithWorkerPool(
                new EventHandler(0, redisTemplateUtils), new EventHandler(1, redisTemplateUtils),
                new EventHandler(2, redisTemplateUtils), new EventHandler(3, redisTemplateUtils));
        disruptor.start();
        produceEvents(disruptor);
    }

    private void produceEvents(Disruptor<ObjectEvent> disruptor) throws InterruptedException {
        RingBuffer<ObjectEvent> ringBuffer = disruptor.getRingBuffer();
        Http http = Http.get(cityurl);
        String responces = http.request();
        try {
            TcCiytResp tcCiytResp = JSONObject.parseObject(responces, TcCiytResp.class);
            String keyWorlds = "";
            if (tcCiytResp == null || tcCiytResp.getSceneryinfo() == null) {
                return;
            }
            tcCiytResp.getSceneryinfo().sort(Comparator.comparing(CityInfo::getCityid));
            tcCiytResp.getSceneryinfo().forEach(city -> ringBuffer.publishEvent((event, sequence) -> {
                event.setCityInfo(city);
                event.setScityId(city.getCityid());
                event.setKeyWorlds(keyWorlds);
            }));
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
            // ex.printStackTrace();
        }
    }
}
