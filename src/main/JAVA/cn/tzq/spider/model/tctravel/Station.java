package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 9:55
 **/
@Getter
@Setter
public class Station implements Serializable {
    private static final long serialVersionUID = 2826191843343L;
    /**
     * carPointId : 193
     * carPointName : 蜀风园大酒店
     * Count : 24
     */
    private int carPointId;
    private String carPointName;
    private int Count;
}
