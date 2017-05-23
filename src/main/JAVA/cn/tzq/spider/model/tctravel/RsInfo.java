package cn.tzq.spider.model.tctravel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @author Tanzhiqiang
 * @mail tanzhiqiang@simpletour.com
 * @create 2017-05-22 9:57
 **/
@Getter
@Setter
public class RsInfo implements Serializable {
    private static final long serialVersionUID = 232012853343L;
    /**
     * rsname : 九寨沟
     * rsid : 3850
     */

    private String rsname;
    private String rsid;
}
