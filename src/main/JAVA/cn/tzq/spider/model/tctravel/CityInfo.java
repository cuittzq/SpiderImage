package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 16:03
 **/
@Getter
@Setter
public class CityInfo {
    /**
     * cityid : 36
     * cityname : 安庆
     * pid : 0
     * pname :
     * py : a
     * cw : cw2
     * CityAreaInfo : [{"CityAreaId":405,"CityAreaName":"安庆市区"}]
     */

    private Integer cityid;
    private String cityname;
    private Integer pid;
    private String pname;
    private String py;
    private String cw;
    private List<CityAreaInfo> CityAreaInfo;
}
