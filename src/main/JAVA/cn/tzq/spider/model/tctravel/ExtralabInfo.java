package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 10:00
 **/
@Getter
@Setter
public class ExtralabInfo implements Serializable {
    private static final long serialVersionUID = 23269012843343L;
    /**
     * labname : 铁定发班
     * labid : 169
     * Illustrate : 一人也成团，行程100%
     * LbType : 160
     * Pic : //pic3.40017.cn
     * StartTime : 20150617
     * EndTime : 20151001
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
