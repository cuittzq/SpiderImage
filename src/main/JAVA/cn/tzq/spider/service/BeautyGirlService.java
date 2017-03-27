package cn.tzq.spider.service;

import cn.tzq.spider.model.BeautyGirls;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;

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
     * @return
     */
    BeautyGirls upDate(BeautyGirls beautyGirls);
    /**
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PageInfo<BeautyGirls> findbyPage(Integer pageNumber, Integer pageSize);

    Page<BeautyGirls> findbeautygirls(Integer top);
}
