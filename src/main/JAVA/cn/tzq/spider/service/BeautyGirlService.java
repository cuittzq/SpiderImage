package cn.tzq.spider.service;

import cn.tzq.spider.model.BeautyGirls;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by tzq139 on 2017/3/22.
 */
public interface BeautyGirlService {

    /**
     * @param id
     * @return
     */
    BeautyGirls findOne(Integer id);

    /**
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PageInfo<BeautyGirls> findbyPage(Integer pageNumber, Integer pageSize);

    /**
     *
     * @param imageTheme
     * @param isdelete
     * @param isDownload
     * @return
     */
    List<BeautyGirls> findByImageTheme(String imageTheme, Integer isdelete, Integer isDownload);
}
