package cn.tzq.spider.biz.imagespider;

import cn.tzq.spider.model.ImageDownEvent;
import reactor.event.Event;

/**
 * Created by tzq139 on 2017/4/23.
 */
public interface ImageDownHandle {

    void Download(Event<ImageDownEvent> ev);

}
