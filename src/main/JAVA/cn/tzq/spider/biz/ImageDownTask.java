package cn.tzq.spider.biz;

import cn.tzq.spider.util.FileUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
public class ImageDownTask implements Runnable {

    private List<String> imageList;

    private String imageTheme;

    final String rootPath = "D:/Images";

    CountDownLatch countDownLatch;

    /**
     * 构造函数
     *
     * @param imageTheme 主题
     * @param imageList  图片列表
     */
    public ImageDownTask(String imageTheme, List<String> imageList, CountDownLatch countDownLatch) {
        this.imageTheme = imageTheme;
        this.imageList = imageList;
        this.countDownLatch = countDownLatch;
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
                URL url = new URL(imageurl);
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                String imagePath = CreateFilePath(this.imageTheme, imageurl);
                // 保存文件
                FileUtil.writeFileFromInputStream(dataInputStream, imagePath);
                dataInputStream.close();
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
