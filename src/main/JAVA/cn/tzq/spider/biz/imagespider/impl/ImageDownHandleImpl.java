package cn.tzq.spider.biz.imagespider.impl;

import cn.tzq.spider.biz.imagespider.ImageDownHandle;
import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.model.ImageDownEvent;
import cn.tzq.spider.proxypool.HttpProxy;
import cn.tzq.spider.service.BeautyGirlService;
import cn.tzq.spider.util.FileUtil;
import cn.tzq.spider.util.RedisTemplateUtils;
import org.springframework.stereotype.Component;
import reactor.event.Event;

import javax.annotation.Resource;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tzq139 on 2017/4/23.
 */
@Component
public class ImageDownHandleImpl implements ImageDownHandle {

    @Resource
    private BeautyGirlService beautyGirlService;

    @Resource
    private RedisTemplateUtils redisTemplateUtils;

    final String rootPath = "D:/Images";

    @Override
    public void Download(Event<ImageDownEvent> ev) {

        System.out.println(String.format("%s, 开始！", ev.getData().getImageTheme()));
        try {
            ev.getData().getImageList().forEach((imageurl) -> {
                try {
                    DataInputStream dataInputStream = getImageInputStream(imageurl, ev.getData().getHttpproxy());
                    if (dataInputStream == null) {
                        imageurl.setDownload(2);
                        this.beautyGirlService.upDate(imageurl);
                        return;
                    }

                    String imagePath = CreateFilePath(ev.getData().getImageTheme(), imageurl.getImageUrl());
                    synchronized (imagePath) {
                        // 保存文件
                        if (!FileUtil.writeFileFromInputStream(dataInputStream, imagePath)) {
                            System.out.println(String.format("%s, 文件保存 失败！", ev.getData().getImageTheme()));
                        }
                    }
                    dataInputStream.close();
                    imageurl.setDownload(1);
                    BeautyGirls savedBeautyGirls = this.beautyGirlService.upDate(imageurl);
                    if (savedBeautyGirls == null) {
                        System.out.println(String.format("%s, 数据状态更新 失败！", ev.getData().getImageTheme()));
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } finally {
        }

        System.out.println(String.format("%s, 结束！", ev.getData().getImageTheme()));
    }


    /**
     * 使用代理下载重试三次
     *
     * @param beautyGirl
     * @return
     * @throws IOException
     */
    private DataInputStream getImageInputStream(BeautyGirls beautyGirl, HttpProxy httpproxy) {

        DataInputStream dataInputStream = null;
        try {
            // 初始化proxy对象
            Long time = System.currentTimeMillis();
            URL url = new URL(beautyGirl.getImageUrl());
            URLConnection conn = null;
            if (httpproxy == null) {
                conn = url.openConnection();
                conn.setReadTimeout(10 * 1000);
            } else {
                conn = url.openConnection(httpproxy.getProxy());
            }

            dataInputStream = new DataInputStream(conn.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return dataInputStream;
    }

    /**
     * 构造文件路径
     *
     * @param folder
     * @param imageurl
     * @return
     */
    private String CreateFilePath(String folder, String imageurl) {
        return String.format("%s/%s/%s", rootPath, folder, imageurl.substring(imageurl.lastIndexOf("/") + 1, imageurl.length()));
    }
}
