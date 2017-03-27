package cn.tzq.spider.repository;

import cn.tzq.spider.model.BeautyGirls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by tzq139 on 2017/3/22.
 */
public interface BeautyGirlRepository extends JpaRepository<BeautyGirls, Integer> {

    /**
     * @return
     */
    @Query("FROM BeautyGirls e WHERE e.deleted = 0 AND e.download = 0  Order by e.keyid ")
    Page<BeautyGirls> findbeautygirls(Pageable pageable);
}
