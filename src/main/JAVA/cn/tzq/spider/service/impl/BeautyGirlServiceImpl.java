package cn.tzq.spider.service.impl;

import cn.tzq.spider.model.BeautyGirls;
import cn.tzq.spider.repository.BeautyGirlRepository;
import cn.tzq.spider.service.BeautyGirlService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by tzq139 on 2017/3/22.
 */
@Service("beautyGirlService")
@Transactional
public class BeautyGirlServiceImpl implements BeautyGirlService {

    private final BeautyGirlRepository beautyGirlRepository;

    @Autowired
    public BeautyGirlServiceImpl(BeautyGirlRepository beautyGirlRepository) {
        this.beautyGirlRepository = beautyGirlRepository;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public BeautyGirls findOne(Integer id) {
        return this.beautyGirlRepository.findOne(id);
    }

    /**
     * @param beautyGirls
     * @return
     */
    @Override
    public BeautyGirls upDate(BeautyGirls beautyGirls) {
        return this.beautyGirlRepository.save(beautyGirls);
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<BeautyGirls> findbyPage(Integer pageNumber, Integer pageSize) {
        Pageable var1 = buildPageRequest(pageNumber, pageSize);

        Page<BeautyGirls> deptpageinfo = beautyGirlRepository.findAll(var1);

        PageInfo<BeautyGirls> deptPageInfo = new PageInfo(deptpageinfo.getContent());
        // 总数
        deptPageInfo.setTotal(deptpageinfo.getTotalElements());
        // 页大小
        deptPageInfo.setPageSize(var1.getPageSize());
        // 当前页
        deptPageInfo.setPageNum(var1.getPageNumber() + 1);
        // 总页数
        deptPageInfo.setPages(deptpageinfo.getTotalPages());
        deptPageInfo.setIsFirstPage(deptPageInfo.getPageNum() == 1);
        deptPageInfo.setIsLastPage(deptPageInfo.getPageNum() >= deptPageInfo.getPages());
        return deptPageInfo;
    }

    @Override
    public Page<BeautyGirls> findbeautygirls(Integer pageSize) {
        Pageable var1 = buildPageRequest(1, pageSize);
        return this.beautyGirlRepository.findbeautygirls(var1);
    }

    /**
     * 构建PageRequest
     *
     * @param pageNumber
     * @param pagzSize
     * @return
     */
    private PageRequest buildPageRequest(Integer pageNumber, Integer pagzSize) {
        return new PageRequest(pageNumber - 1, pagzSize, Sort.Direction.ASC, "id");
    }

}
