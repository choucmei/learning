package chouc.storm.logAnalyze.app.dao;

import java.util.HashMap;
import java.util.Map;

public class CacheData {
    private static Map<String, Integer> pvMap = new HashMap<>();
    private static Map<String, Long> uvMap = new HashMap<>();

    public static int getPv(int pv, String indexName) {
        Integer cacheValue = pvMap.get(indexName);

        if (cacheValue == null) {
            cacheValue = 0;
            pvMap.put(indexName, cacheValue);
        }
        if (pv > cacheValue.intValue()) {
            pvMap.put(indexName, pv); //将新的值赋值个cacheData
            return pv - cacheValue.intValue();
        }
        return 0;//如果新的值小于旧的值，直接返回0
    }

    public static long getUv(long uv, String indexName) {
        Long cacheValue = uvMap.get(indexName);
        if (cacheValue == null) {
            cacheValue = 0l;
            uvMap.put(indexName, cacheValue);
        }
        if (uv > cacheValue.longValue()) {
            uvMap.put(indexName, uv);//将新的值赋值给cachaData
            return uv - cacheValue;
        }
        return 0;//如果新的值小于旧的值，直接返回0
    }

    public static Map<String, Integer> getPvMap() {
        return pvMap;
    }

    public static void setPvMap(Map<String, Integer> pvMap) {
        CacheData.pvMap = pvMap;
    }

    public static Map<String, Long> getUvMap() {
        return uvMap;
    }

    public static void setUvMap(Map<String, Long> uvMap) {
        CacheData.uvMap = uvMap;
    }
}
