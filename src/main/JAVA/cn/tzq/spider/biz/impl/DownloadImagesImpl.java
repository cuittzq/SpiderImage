package cn.tzq.spider.biz.impl;

import cn.tzq.spider.biz.DownloadImages;
import cn.tzq.spider.util.FileUtil;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-03-27 14:36
 **/
@Component("downloadImages")
public class DownloadImagesImpl implements DownloadImages {

    final String rootPath = "D:/Images";

    /**
     * 下载网络文件
     *
     * @param filePath 文件保存地址
     * @param imageUrl 图片地址
     * @throws MalformedURLException
     */
    @Override
    public boolean downloadImage(String filePath, String imageUrl) throws MalformedURLException {
        //
        int bytesum = 0;
        int byteread = 0;
        boolean isdownload = false;
        URL url = new URL(imageUrl);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(filePath);
            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }

            isdownload = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isdownload;
    }

    /**
     * 传入要下载的图片的url列表，将url所对应的图片下载到本地
     *
     * @param imagemap
     */
    @Override
    public void downloadPicture(Map<String, List<String>> imagemap) {
        imagemap.forEach((key, values) -> {
            if (values != null) {
                values.forEach((imageurl) -> {
                    try {
                        URL url = new URL(imageurl);
                        DataInputStream dataInputStream = new DataInputStream(url.openStream());
                        String imageName = String.format("%s/%s/%s", rootPath, key, imageurl.substring(imageurl.lastIndexOf("/") + 1, imageurl.length()));
                        // 创建文件
                        FileUtil.createNewFile(imageName);
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
                        byte[] buffer = new byte[1024];
                        int length;

                        while ((length = dataInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, length);
                        }

                        dataInputStream.close();
                        fileOutputStream.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
