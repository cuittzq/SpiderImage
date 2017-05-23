package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 10:01
 **/
@Setter
@Getter
public class ProMarketing implements Serializable {
    private static final long serialVersionUID = 232012843343L;
    /**
     * PreferentialType : 10
     * PreferentialTypeList : [{"EndDate":"2017-12-31T23:55:00+08:00","IconColor":"#ff6d4f","IconContent":"vip超值购","IconHoverContent":"","IconId":63,"Notice":"VIP付费会员积分抵扣5%","PreAmount":23,"PreferentialModeType":0,"ReceiveUrl":"","Remark":"1，\u201cVIP超值购\u201d为VIP付费会员提供更优的积分抵扣政策：每个成人可享受线路预订金额的5%，不包含保险和增值产品，积分抵扣金额比例1:2 \n2，积分抵扣以成人为单位，帐户所剩积分须高于订单所有人的抵扣积分总和，才可使用此优惠。当前显示额度，仅为推荐，具体抵扣额度随出游日期变动，出游日期不同抵扣金额不同，最终抵扣额度以订单成功创建为准，请选择日期后，到订单填写页的\u201c优惠详情\u201d模块查看并选择，该优惠与其他优惠不可同时享受\n3，如有疑问请拨打VIP会员专属热线4001-807-777\n","RuleId":36616,"RuleTitle":"VIP付费会员享受积分抵扣","StartDate":"2017-03-17T16:30:00+08:00"}]
     */
    private int PreferentialType;
    private List<PreferentialType> PreferentialTypeList;
}
