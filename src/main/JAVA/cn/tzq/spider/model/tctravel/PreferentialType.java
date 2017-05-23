package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 9:59
 **/
@Setter
@Getter
public class PreferentialType  implements Serializable {
    private static final long serialVersionUID = 232612012843343L;
    private String EndDate;
    private String IconColor;
    private String IconContent;
    private String IconHoverContent;
    private int IconId;
    private String Notice;
    private double PreAmount;
    private int PreferentialModeType;
    private String ReceiveUrl;
    private String Remark;
    private int RuleId;
    private String RuleTitle;
    private String StartDate;
}
