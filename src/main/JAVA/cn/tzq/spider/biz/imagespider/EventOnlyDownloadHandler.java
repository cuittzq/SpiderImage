package cn.tzq.spider.biz.imagespider;

import cn.tzq.spider.exception.HttpException;
import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.model.ImageDownEvent;
import cn.tzq.spider.proxypool.HttpProxy;
import cn.tzq.spider.util.FileUtil;
import com.lmax.disruptor.WorkHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tzq139 on 2017/6/30.
 */
public class EventOnlyDownloadHandler implements WorkHandler<ImageDownEvent> {


    final String rootPath = "D:/Images";

    public EventOnlyDownloadHandler() {
    }

    @Override
    public void onEvent(ImageDownEvent imageDownEvent) throws Exception {
        System.out.println(String.format("%s---, 开始！", imageDownEvent.getImageTheme()));
        try {
            for (int i = 0; i < imageDownEvent.getImageList().size(); i++) {
                BeautyGirls imageurl = imageDownEvent.getImageList().get(i);
                try {
                    System.out.println(String.format("下载中%s--", imageurl.getImageUrl()));
                    DataInputStream dataInputStream = getImageInputStream(imageurl, imageDownEvent.getHttpproxy());
                    if (dataInputStream == null) {
                        break;
                    }

                    String imagePath = CreateFilePath(imageDownEvent.getImageTheme(), imageurl.getImageUrl());
                    synchronized (imagePath) {
                        // 保存文件
                        if (!FileUtil.writeFileFromInputStream(dataInputStream, imagePath)) {
                            System.out.println(String.format("%s, 文件保存 失败！", imageDownEvent.getImageTheme()));
                        }
                    }
                    dataInputStream.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
                conn.setConnectTimeout(3 * 1000);
            } else {
                conn = url.openConnection(httpproxy.getProxy());
            }

            dataInputStream = new DataInputStream(conn.getInputStream());
        } catch (HttpException httpex) {
            System.out.println(String.format("%s, %s！", beautyGirl.getImageUrl(), httpex.getMessage()));
        } catch (IOException ex) {
            System.out.println(String.format("%s, %s！", beautyGirl.getImageUrl(), ex.getMessage()));
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
