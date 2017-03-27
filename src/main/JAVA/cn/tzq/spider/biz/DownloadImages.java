package cn.tzq.spider.biz;

import java.util.List;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-03-27 14:34
 **/
public interface DownloadImages {
    /**
     * 传入要下载的图片的url列表，将url所对应的图片下载到本地
     *
     * @param imagemap
     */
    void downloadPicture(Map<String, List<String>> imagemap) throws InterruptedException;
}
