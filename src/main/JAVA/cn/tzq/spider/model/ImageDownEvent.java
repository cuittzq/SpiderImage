package cn.tzq.spider.model;

import cn.tzq.spider.proxypool.HttpProxy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by tzq139 on 2017/4/23.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDownEvent {

    private List<BeautyGirls> imageList;

    private String imageTheme;

    private HttpProxy httpproxy;
}
