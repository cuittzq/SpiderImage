package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang @simpletour.com
 * @create 2017 -05-22 15:56
 */
@Setter
@Getter
public class TcCiytResp {
    /**
     * state : 100
     * error : 查询成功
     * sceneryinfo : [{"cityid":"36","cityname":"安庆","pid":"0","pname":"","py":"a","cw":"cw2","CityAreaInfo":[{"CityAreaId":405,"CityAreaName":"安庆市区"}]}]
     */
    private String state;
    private String error;
    private List<CityInfo> sceneryinfo;
}
