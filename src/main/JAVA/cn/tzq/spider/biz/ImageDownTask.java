package cn.tzq.spider.biz;

import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.proxypool.HttpProxy;
import cn.tzq.spider.proxypool.ProxyPool;
import cn.tzq.spider.service.BeautyGirlService;
import cn.tzq.spider.util.FileUtil;
import cn.tzq.spider.util.RedisTemplateUtils;
import com.sun.jndi.toolkit.ctx.Continuation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-03-27 16:07
 **/

@Component("imageDownTask")
@Scope("prototype")
@Getter
@Setter
public class ImageDownTask implements Runnable {

    private List<BeautyGirls> imageList;

    private String imageTheme;

    final String rootPath = "D:/Images";

    private CountDownLatch countDownLatch;

    private BeautyGirlService beautyGirlService;

    private RedisTemplateUtils redisTemplateUtils;

    private ProxyPool proxyPool;

    /**
     * 构造函数
     *
     * @param imageTheme 主题
     * @param imageList  图片列表
     */
    @Autowired
    public ImageDownTask(BeautyGirlService beautyGirlService, ProxyPool proxyPool, RedisTemplateUtils redisTemplateUtils) {
//        this.imageTheme = imageTheme;
//        this.imageList = imageList;
//        this.countDownLatch = countDownLatch;
        this.beautyGirlService = beautyGirlService;
        this.proxyPool = proxyPool;
        this.redisTemplateUtils = redisTemplateUtils;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.printf("%s, 开始！", this.imageTheme);
        this.imageList.forEach((imageurl) -> {
            try {
                DataInputStream dataInputStream = getImageInputStream(imageurl);
                if (dataInputStream == null) {
                    return;
                }
                String imagePath = CreateFilePath(this.imageTheme, imageurl.getImageUrl());
                // 保存文件
                FileUtil.writeFileFromInputStream(dataInputStream, imagePath);
                dataInputStream.close();
                imageurl.setDownload(1);
                this.beautyGirlService.upDate(imageurl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.printf("%s, 结束！", this.imageTheme);
        this.countDownLatch.countDown();
    }

    /**
     * 使用代理下载重试三次
     * @param beautyGirl
     * @return
     * @throws IOException
     */
    private DataInputStream getImageInputStream(BeautyGirls beautyGirl) {

        DataInputStream dataInputStream = null;
        for (int i = 0; i < 3; ) {
            try {
                // 初始化proxy对象
                HttpProxy httpproxy = this.proxyPool.borrow();
                Long time = System.currentTimeMillis();
                URL url = new URL(beautyGirl.getImageUrl());
                URLConnection conn = url.openConnection(httpproxy.getProxy());
                dataInputStream = new DataInputStream(conn.getInputStream());
                break;
            } catch (IOException ex) {
                i++;
            }
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
