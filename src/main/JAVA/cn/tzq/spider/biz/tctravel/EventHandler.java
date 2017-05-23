package cn.tzq.spider.biz.tctravel;

import cn.tzq.spider.model.tctravel.TcTravelBean;
import cn.tzq.spider.util.RedisTemplateUtils;
import cn.tzq.spider.util.http.Http;
import com.alibaba.fastjson.JSONObject;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 17:30
 **/
public class EventHandler implements WorkHandler<ObjectEvent> {

    private String baseurl = "http://www.ly.com/bustour/json/shorttoursearch.html?page=%d&PageSize=%d&keyword=\"%s\"&days=0&endcityids=&scityId=%d&provid=0&rsceneryId=0&districtAndCountyIds=&themeId=0&tourtype=0&SortType=0";

    private Integer pageSize = 10;

    private Integer handerid;

    private RedisTemplateUtils redisTemplateUtils;

    public EventHandler(Integer handerid, RedisTemplateUtils redisTemplateUtils) {
        this.handerid = handerid;
        this.redisTemplateUtils = redisTemplateUtils;
    }

    /**
     * Callback to indicate a unit of work needs to be processed.
     *
     * @param event published to the {@link RingBuffer}
     * @throws Exception if the {@link WorkHandler} would like the exception handled further up the chain.
     */
    @Override
    public void onEvent(ObjectEvent event) throws Exception {
        int total = 0;
        try {
            for (Integer pageIndex = 0; true; pageIndex++) {
                Http http = Http.get(String.format(baseurl, pageIndex, pageSize, event.getKeyWorlds(), event.getScityId()));
                http = http.readTimeout(30);
                String responces = http.request();
                TcTravelBean tctravelbean = JSONObject.parseObject(responces, TcTravelBean.class);
                if (tctravelbean.getSceneryinfo() != null) {
                    total += tctravelbean.getSceneryinfo().size();
                    String key = String.format("%s", event.getCityInfo().getCityid());

                    tctravelbean.getSceneryinfo().forEach(p -> {
                        try {
                            redisTemplateUtils.leftPush(key, p);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    });
                }
                if (tctravelbean.getSceneryinfo() != null && tctravelbean.getSceneryinfo().size() < pageSize) {
                    System.out.printf("%s,%s 产品数 %d,\n", event.getCityInfo().getCityname(), event.getKeyWorlds(), total);
                    break;
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
