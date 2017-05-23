package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 9:58
 **/
@Getter
@Setter
public class GroupInfo implements Serializable {
    private static final long serialVersionUID = 23260012843343L;
    /**
     * GroupId : 5316657
     * Date : 20170425
     * Price : 445
     */

    private int GroupId;
    private int Date;
    private int Price;

}
