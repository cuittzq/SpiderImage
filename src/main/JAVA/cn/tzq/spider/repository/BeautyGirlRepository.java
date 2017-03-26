package cn.tzq.spider.repository;

import cn.tzq.spider.model.BeautyGirls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by tzq139 on 2017/3/22.
 */
public interface BeautyGirlRepository extends JpaRepository<BeautyGirls, Integer> {

    /**
     *
     * @param imageTheme
     * @param isdelete
     * @param isDownload
     * @return
     */
//    @Query("FROM BeautyGirls e WHERE e.imageTheme = ?1 AND e.deleted = ?2 AND e.download = ?3")
//    List<BeautyGirls> findByImageTheme(String imageTheme, Integer isdelete, Integer isDownload);
}
