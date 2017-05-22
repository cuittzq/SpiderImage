package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 9:53
 **/
@Getter
@Setter
public class TcTravelBean {
    /**
     * state : 100
     * IsNew : 1
     * sceneryinfo : [{"name":"【VIP超值购】<九寨沟-黄龙-宿景区隔壁酒店-跟团3日游>-享舒适之床-赠藏家家访土火锅","nameKey":"【VIP超值购】","nameMainTitle":"<九寨沟-黄龙-宿景区隔壁酒店-跟团3日游>","nameSecondTitle":"-享舒适之床-赠藏家家访土火锅","pic":"//pic4.40017.cn/scenery/destination/2016/08/04/15/6wXz5S.jpg","areaname":"阿坝藏族羌族自治州","special":"1、全程0自费(无升级、推荐)，避免看导游脸色交自费钱，省心、开心之旅。2、宿景区隔壁，住宿干净、舒适。3、赠送藏家家访土火锅，车位空座率保证10%，保证舒适。注：此行程可在预定时加钱选择观看晚会：一、加40元/人，观看九寨沟第6道风景线之称\u2014藏羌风情晚会，体验当地民俗风情文化。二、加120元/人，可观看九寨沟5D千古情晚会，极具艺术价值，震撼人心。确定后，团上不再升级变更套餐，敬请理解。【预定即返】 4月20日-6月20日，在此团期内报名出游的客人，满2位成人返60元每单。 下单正常出团后，完团第二天返现。","srcityid":"324","srcityname":"成都","price":"445","marketprice":"445","zheprice":"0","pdtype":"三日游","oldCityId":"0","oldCityPy":"","oldProductId":"0","id":"12901","grade":"0","enableRedPacket":"1","lablist":[{"labname":"山岳","labid":"131","Illustrate":"线路行程中含有知名山","LbType":93,"Pic":"//pic4.40017.cn","StartTime":"19000103","EndTime":"19001231","OrderBy":null}],"extralablist":[{"labname":"铁定发班","labid":"169","Illustrate":"一人也成团，行程100%","LbType":160,"Pic":"//pic3.40017.cn","StartTime":"20150617","EndTime":"20151001","OrderBy":null}],"rslist":[{"rsname":"九寨沟","rsid":"3850"}],"policyLabList":[],"GroupList":[{"GroupId":5316657,"Date":20170425,"Price":445}],"ProMarketingList":[{"PreferentialType":10,"PreferentialTypeList":[{"EndDate":"2017-12-31T23:55:00+08:00","IconColor":"#ff6d4f","IconContent":"vip超值购","IconHoverContent":"","IconId":63,"Notice":"VIP付费会员积分抵扣5%","PreAmount":23,"PreferentialModeType":0,"ReceiveUrl":"","Remark":"1，\u201cVIP超值购\u201d为VIP付费会员提供更优的积分抵扣政策：每个成人可享受线路预订金额的5%，不包含保险和增值产品，积分抵扣金额比例1:2 \n2，积分抵扣以成人为单位，帐户所剩积分须高于订单所有人的抵扣积分总和，才可使用此优惠。当前显示额度，仅为推荐，具体抵扣额度随出游日期变动，出游日期不同抵扣金额不同，最终抵扣额度以订单成功创建为准，请选择日期后，到订单填写页的\u201c优惠详情\u201d模块查看并选择，该优惠与其他优惠不可同时享受\n3，如有疑问请拨打VIP会员专属热线4001-807-777\n","RuleId":36616,"RuleTitle":"VIP付费会员享受积分抵扣","StartDate":"2017-03-17T16:30:00+08:00"}]}],"ContractSubjectId":2,"ContractSubjectName":"程游天下国际旅行社（苏州）有限公司","ReviewScore":0,"ReviewCount":85,"ReviewGood":84,"ReviewMid":1,"ReviewBad":0,"ReviewGoodRate":100}]
     * StationList : [{"carPointId":193,"carPointName":"蜀风园大酒店","Count":24}]
     */
    private String state;
    private int IsNew;
    private List<Sceneryinfo> sceneryinfo;
    private List<Station> StationList;
}
