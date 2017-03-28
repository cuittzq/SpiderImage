package cn.tzq.spider.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhiqiang on 2017/3/15.
 */
@Service
@Transactional
public class RedisTemplateUtils {

    private final RedisTemplate redisTemplate;

    @Autowired
    public RedisTemplateUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 写入缓存
     *
     * @param key    存入 key
     * @param value  存入 值
     * @param expire 过期时间 秒
     */
    public void set(String key, Object value, Long expire) {
        try {
            redisTemplate.opsForValue().set(key, Gson.class.newInstance().toJson(value), expire, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 读取缓存
     *
     * @param key
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        Object varobj = redisTemplate.boundValueOps(key).get();
        if (varobj == null || varobj.toString().isEmpty()) {
            return null;
        }
        return Gson.class.newInstance().fromJson(varobj.toString(), clazz);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object getObj(String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    /**
     * 删除，根据key精确匹配
     *
     * @param key
     */
    public void del(String... key) {
        redisTemplate.delete(Arrays.asList(key));
    }

    /**
     * 批量删除，根据key模糊匹配
     *
     * @param pattern
     */
    public void delpn(String... pattern) {
        for (String kp : pattern) {
            redisTemplate.delete(redisTemplate.keys(kp + "*"));
        }
    }

    /**
     * key是否存在
     *
     * @param key
     */
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }


    /* ----------- list --------- */
    public int size(String key) {
        return redisTemplate.opsForList().size(key).intValue();
    }

    public <T> List<T> range(String key, long start, long end, Class<T> clazz) {
        List<String> list = redisTemplate.opsForList().range(key, start, end);
        return jsonToList(list, clazz);
    }


    public <T> ArrayList<T> jsonToList(List<String> jsons, Class<T> classOfT) {
        ArrayList<T> listOfT = new ArrayList<>();
        for (String jsonObj : jsons) {
            listOfT.add(new Gson().fromJson(jsonObj, classOfT));
        }
        return listOfT;
    }
}
