package cn.tzq.spider.biz;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-03-27 16:33
 **/
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    private ApplicationContextProvider() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(String name, Class<T> aClass) {
        return context.getBean(name, aClass);
    }

    public static <T> T getBean(Class<T> aClass) {
        return context.getBean(aClass);
    }
}
