package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 16:05
 **/
@Getter
@Setter
public class CityAreaInfo implements Serializable {
    private static final long serialVersionUID = 28261912843343L;
    /**
     * CityAreaId : 405
     * CityAreaName : 安庆市区
     */

    private Integer CityAreaId;
    private String CityAreaName;
}
