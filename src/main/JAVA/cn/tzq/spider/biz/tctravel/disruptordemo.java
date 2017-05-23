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
public interface disruptordemo {

    void start() throws InterruptedException;
}
