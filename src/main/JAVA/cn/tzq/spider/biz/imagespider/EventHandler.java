package cn.tzq.spider.biz.imagespider;

import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.model.ImageDownEvent;
import cn.tzq.spider.proxypool.HttpProxy;
import cn.tzq.spider.service.BeautyGirlService;
import cn.tzq.spider.util.FileUtil;
import com.lmax.disruptor.WorkHandler;

import javax.annotation.Resource;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tzq139 on 2017/5/22.
 */

public class EventHandler implements WorkHandler<ImageDownEvent> {


    @Resource
    private BeautyGirlService beautyGirlService;

    final String rootPath = "D:/Images";

    public EventHandler(BeautyGirlService beautyGirlService) {
        this.beautyGirlService = beautyGirlService;
    }


    @Override
    public void onEvent(ImageDownEvent imageDownEvent) throws Exception {


        System.out.println(String.format("%s, 开始！", imageDownEvent.getImageTheme()));
        try {
            imageDownEvent.getImageList().forEach((imageurl) -> {
                try {
                    DataInputStream dataInputStream = getImageInputStream(imageurl, imageDownEvent.getHttpproxy());
                    if (dataInputStream == null) {
                        imageurl.setDownload(2);
                        this.beautyGirlService.upDate(imageurl);
                        return;
                    }

                    String imagePath = CreateFilePath(imageDownEvent.getImageTheme(), imageurl.getImageUrl());
                    synchronized (imagePath) {
                        // 保存文件
                        if (!FileUtil.writeFileFromInputStream(dataInputStream, imagePath)) {
                            System.out.println(String.format("%s, 文件保存 失败！", imageDownEvent.getImageTheme()));
                        }
                    }
                    dataInputStream.close();
                    imageurl.setDownload(1);
                    BeautyGirls savedBeautyGirls = this.beautyGirlService.upDate(imageurl);
                    if (savedBeautyGirls == null) {
                        System.out.println(String.format("%s, 数据状态更新 失败！", imageDownEvent.getImageTheme()));
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } finally {
        }

        System.out.println(String.format("%s, 结束！", imageDownEvent.getImageTheme()));
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
