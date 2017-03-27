package cn.tzq.spider.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-03-27 17:29
 **/
@Data
@ConfigurationProperties(prefix = "spring.threadPool")
public class MyConfigurationProperties {
    private int maxThreads;
    private int keepAliveSeconds;
    private int corePoolSize;
}
