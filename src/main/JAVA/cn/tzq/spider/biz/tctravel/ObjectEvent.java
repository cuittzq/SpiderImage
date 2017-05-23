package cn.tzq.spider.biz.tctravel;

import cn.tzq.spider.model.tctravel.CityInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 10:09
 **/
@Getter
@Setter
public class ObjectEvent {
    Integer pageIndex = 1;
    String keyWorlds = "";
    /**
     * 游玩天数
     */
    Integer days = 0;

    /**
     * 出发地参团（到达城市）
     */
    Integer endcityids = null;
    /**
     * 目的地地参团（出发城市）
     */
    Integer scityId = 324;
    Integer provid = 0;
    Integer rsceneryId = 0;
    Integer districtAndCountyIds = null;
    Integer themeId = 0;
    Integer tourtype = 0;
    Integer SortType = 0;

    /**
     *
     */
    CityInfo cityInfo;
}
