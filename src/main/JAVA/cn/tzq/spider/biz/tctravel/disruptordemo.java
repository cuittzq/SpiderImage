package cn.tzq.spider.biz.tctravel;

import cn.tzq.spider.model.tctravel.CityInfo;
import cn.tzq.spider.model.tctravel.TcCiytResp;
import cn.tzq.spider.model.tctravel.TcTravelBean;
import cn.tzq.spider.util.http.Http;
import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 10:08
 **/
public class disruptordemo {

    private static String baseurl = "http://www.ly.com/bustour/json/shorttoursearch.html?page=%d&PageSize=%d&keyword=\"%s\"&days=0&endcityids=&scityId=%d&provid=0&rsceneryId=0&districtAndCountyIds=&themeId=0&tourtype=0&SortType=0";

    private static String cityurl = "http://www.ly.com/bustour/json/getallstartcity.html?action=GETYRYCITYLIST&isReadCache=0";
    private static Integer pageSize = 10;

    public static void handleEvent1(ObjectEvent event, long sequence, boolean endOfBatch) {
        int total = 0;
        for (Integer pageIndex = 0; true; pageIndex++) {
            Http http = Http.get(String.format(baseurl, pageIndex, pageSize, event.getKeyWorlds(), event.getScityId()));
            http = http.readTimeout(30);
            String responces = http.request();
            TcTravelBean tctravelbean = JSONObject.parseObject(responces, TcTravelBean.class);
            if (tctravelbean.getSceneryinfo() != null) {
                total += tctravelbean.getSceneryinfo().size();
            }
            if (tctravelbean.getSceneryinfo() != null && tctravelbean.getSceneryinfo().size() < pageSize) {
                System.out.printf("CityId:%d, 产品数 %d,\n", event.getScityId(), total);
                break;
            }
        }
    }

    private static void produceEvents(Disruptor<ObjectEvent> disruptor) throws InterruptedException {
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
                event.setScityId(city.getCityid());
                event.setKeyWorlds(keyWorlds);
            }));
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
            // ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        int bufferSize = 1024;
        Disruptor<ObjectEvent> disruptor = new Disruptor<>(ObjectEvent::new, bufferSize, executor,
                ProducerType.SINGLE, new LiteBlockingWaitStrategy());
        disruptor.handleEventsWithWorkerPool(new EventHandler(0), new EventHandler(1),
                new EventHandler(2), new EventHandler(3));
        disruptor.start();
        produceEvents(disruptor);
    }
}
