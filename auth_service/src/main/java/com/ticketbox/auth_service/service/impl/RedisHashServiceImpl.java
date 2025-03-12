package com.ticketbox.auth_service.service.impl;

import com.ticketbox.auth_service.service.RedisHashService;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RedisHashServiceImpl implements RedisHashService {

    RedisTemplate<String, Object> redisTemplate;

    @NonFinal
    HashOperations<String, String, Object> hashOperations;

    @PostConstruct
    private void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }


    @Override
    public void savePublicKey(String key, String publicKey) {
        redisTemplate.opsForValue().set("user:publicKey:" + key, publicKey);
    }

    @Override
    public void saveRefreshID(String key, String refreshID, Integer ttl) {
        if (Objects.isNull(ttl)) ttl = 1000 * 60 * 60 * 24 * 7;
        redisTemplate.opsForValue().set("user:refreshID:" + key, refreshID, ttl, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getRefreshID(String key) {
        return (String) redisTemplate.opsForValue().get("user:refreshID:" + key);
    }

    @Override
    public String getPublicKey(String key) {
        return (String) redisTemplate.opsForValue().get("user:publicKey:" + key);
    }


    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey("user:refreshID:" + key);
    }


    @Override
    public void deleteFromHash(String key) {
        redisTemplate.delete("user:refreshID:" + key);
    }

}
