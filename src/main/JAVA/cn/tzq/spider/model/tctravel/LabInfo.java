package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 9:56
 **/
@Getter
@Setter
public class LabInfo {
    /**
     * labname : 山岳
     * labid : 131
     * Illustrate : 线路行程中含有知名山
     * LbType : 93
     * Pic : //pic4.40017.cn
     * StartTime : 19000103
     * EndTime : 19001231
     * OrderBy : null
     */

    private String labname;
    private String labid;
    private String Illustrate;
    private int LbType;
    private String Pic;
    private String StartTime;
    private String EndTime;
    private Object OrderBy;
}
