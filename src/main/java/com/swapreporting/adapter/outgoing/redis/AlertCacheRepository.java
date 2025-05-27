package com.swapreporting.adapter.outgoing.redis;

import com.swapreporting.domain.alert.model.Alert;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AlertCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public AlertCacheRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveAlert(Alert alert) {
        redisTemplate.opsForValue().set("alert:" + alert.getTradeId(), alert);
    }
}
