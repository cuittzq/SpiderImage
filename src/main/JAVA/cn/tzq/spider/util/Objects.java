package cn.tzq.spider.util;

/**
 * Created by igotti on 16-3-30.
 */
public abstract class Objects {

    /**
     * 判断对象是否是数组
     * @param obj
     * @return
     */
    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    /**
     * 判断数组对象是否为空
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

}
