package cn.tzq.spider.facade.impl;

import cn.tzq.spider.facade.BeautyGirlDubboService;
import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.service.BeautyGirlService;
import cn.tzq.spider.service.impl.BeautyGirlServiceImpl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by tzq139 on 2017/3/22.
 */
@Service(version = "1.0.0")
public class BeautyGirlDubboServiceImpl implements BeautyGirlDubboService {

    private BeautyGirlService beautyGirlService;

    @Autowired
    public BeautyGirlDubboServiceImpl(BeautyGirlServiceImpl beautyGirlService) {
        this.beautyGirlService = beautyGirlService;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public BeautyGirls findOne(Integer id) {
        return this.beautyGirlService.findOne(id);
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<BeautyGirls> findbyPage(Integer pageNumber, Integer pageSize) {
        return this.beautyGirlService.findbyPage(pageNumber, pageSize);
    }

    /**
     * @param imageTheme
     * @param isdelete
     * @param isDownload
     * @return
     */
    @Override
    public List<BeautyGirls> findByImageTheme(String imageTheme, Integer isdelete, Integer isDownload) {
        return null;
    }
}
