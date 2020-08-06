package com.zhishu.config;

import com.zhishu.common.dto.Page;
import org.apache.ibatis.cache.CacheKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分页缓存配置
 *
 * @author huangfu
 */
public class CachePageConfig {
    public final static Map<CacheKey, Page> PAGE_CACHE = new ConcurrentHashMap<>(32);
}
