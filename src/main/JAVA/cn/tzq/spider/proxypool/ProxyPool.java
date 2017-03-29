package cn.tzq.spider.proxypool;


import cn.tzq.spider.model.ProxyIp;
import cn.tzq.spider.util.RedisTemplateUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * Created by tzq139 on 2017/3/28.
 */
@Component
@Log4j2
public class ProxyPool {

    private RedisTemplateUtils redisTemplateUtils;

    private BlockingQueue<HttpProxy> idleQueue = new DelayQueue<HttpProxy>(); // 存储空闲的Proxy

    private Map<String, HttpProxy> totalQueue = new ConcurrentHashMap<String, HttpProxy>(); // 存储所有的Proxy

    @Autowired
    public ProxyPool(RedisTemplateUtils redisTemplateUtils) {
        this.redisTemplateUtils = redisTemplateUtils;
        log.info("初始化代理！");
        List<ProxyIp> proxyIps = this.redisTemplateUtils.range("ProxyIps", 0, 100, ProxyIp.class);
        if (proxyIps != null && proxyIps.size() > 0) {
            proxyIps.forEach((p) -> add(p.getIp(), p.getPort()));
        }
    }

    /**
     * 添加Proxy
     *
     * @param httpProxies
     */
    public void add(HttpProxy... httpProxies) {
        for (HttpProxy httpProxy : httpProxies) {
            if (totalQueue.containsKey(httpProxy.getKey())) {
                continue;
            }
            idleQueue.add(httpProxy);
            totalQueue.put(httpProxy.getKey(), httpProxy);
        }
    }

    public void add(String address, int port) {
        this.add(new HttpProxy(address, port));
    }

    /**
     * 得到Proxy
     *
     * @return
     */
    public HttpProxy borrow() {
        HttpProxy httpProxy = null;
        try {
            Long time = System.currentTimeMillis();
            // 阻塞直到队列中有可用的代理
            httpProxy = idleQueue.take();
            double costTime = (System.currentTimeMillis() - time) / 1000.0;
            HttpProxy p = totalQueue.get(httpProxy.getKey());
            p.borrow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (httpProxy == null) {
            throw new NoSuchElementException();
        }
        return httpProxy;
    }

    /**
     * 反馈 Proxy
     *
     * @param httpProxy
     * @param httpStatus
     */
    public void reback(HttpProxy httpProxy, HttpStatus httpStatus) {
        switch (httpStatus) {
            case SC_OK:
                httpProxy.success();
                httpProxy.setReuseTimeInterval(HttpProxy.DEFAULT_REUSE_TIME_INTERVAL);
                break;
            case SC_FORBIDDEN:
                httpProxy.fail(httpStatus);
                httpProxy.setReuseTimeInterval(HttpProxy.DEFAULT_REUSE_TIME_INTERVAL * (httpProxy.getFailedNum() + 1)); // 被网站禁止，调节更长时间的访问频率
//                logger.info(httpProxy.getProxy() + " >>>> reuseTimeInterval is >>>> " + TimeUnit.SECONDS.convert(httpProxy.getReuseTimeInterval(), TimeUnit.MILLISECONDS));
                break;
            default:
                httpProxy.fail(httpStatus);
                httpProxy.setReuseTimeInterval(HttpProxy.DEFAULT_REUSE_TIME_INTERVAL * (httpProxy.getFailedNum() + 1)); // Ip可能无效，调节更长时间的访问频率
                break;
        }
        if (httpProxy.getFailedNum() > 5) { // 连续失败超过 5 次，移除代理池队列
            httpProxy.setReuseTimeInterval(HttpProxy.FAIL_REVIVE_TIME_INTERVAL);
//            logger.error("remove proxy >>>> " + httpProxy.getProxy() + ">>>>" + httpProxy.countErrorStatus() + " >>>> remain proxy >>>> " + idleQueue.size());
            return;
        }
        if (httpProxy.getFailedNum() > 0 && httpProxy.getFailedNum() % 5 == 0) { //失败超过 5次，10次，15次，检查本机与Proxy的连通性
            if (!httpProxy.check()) {
                httpProxy.setReuseTimeInterval(HttpProxy.FAIL_REVIVE_TIME_INTERVAL);
//                logger.error("remove proxy >>>> " + httpProxy.getProxy() + ">>>>" + httpProxy.countErrorStatus() + " >>>> remain proxy >>>> " + idleQueue.size());
                return;
            }
        }
        if (httpProxy.getSucceedNum() > 5) {
            //持久化到磁盘,提供代理ip服务
            this.redisTemplateUtils.set("ipproxy", httpProxy, 100L);//连续成功超过 5次，移除代理池队列,存储到redis
            return;
        }
        try {
            idleQueue.put(httpProxy);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void allProxyStatus() {
        String re = "all proxy info >>>> \n";
        for (HttpProxy httpProxy : idleQueue) {
            re += httpProxy.toString() + "\n";

        }
        System.out.print(re);
//        logger.info(re);
    }

    /**
     * 获取当前空闲的Proxy
     *
     * @return
     */
    public int getIdleNum() {
        return idleQueue.size();
    }
}
