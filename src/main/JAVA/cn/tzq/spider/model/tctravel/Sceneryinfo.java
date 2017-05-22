package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 10:01
 **/
@Getter
@Setter
public class Sceneryinfo {
    /**
     */
    private String name;
    private String nameKey;
    private String nameMainTitle;
    private String nameSecondTitle;
    private String pic;
    private String areaname;
    private String special;
    private String srcityid;
    private String srcityname;
    private String price;
    private String marketprice;
    private String zheprice;
    private String pdtype;
    private String oldCityId;
    private String oldCityPy;
    private String oldProductId;
    private String id;
    private String grade;
    private String enableRedPacket;
    private int ContractSubjectId;
    private String ContractSubjectName;
    private double ReviewScore;
    private int ReviewCount;
    private int ReviewGood;
    private int ReviewMid;
    private int ReviewBad;
    private double ReviewGoodRate;
    private List<LabInfo> lablist;
    private List<ExtralabInfo> extralablist;
    private List<RsInfo> rslist;
    private List<GroupInfo> GroupList;
    private List<ProMarketing> ProMarketingList;
}
