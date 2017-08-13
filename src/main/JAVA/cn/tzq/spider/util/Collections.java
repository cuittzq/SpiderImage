package cn.tzq.spider.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author igotti
 *
 * 集合类的工具方法
 *
 * @since 1.0.0
 *
 * Created by igotti on 16-3-30.
 */
public abstract class Collections {

    /**
     * 集合是否为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * map是否为空
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

}
