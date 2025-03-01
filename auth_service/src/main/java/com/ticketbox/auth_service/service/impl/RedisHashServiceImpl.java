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
    public void saveToHas(String key, String field, Object value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public String getFromHash(String key, String field) {
        return (String)hashOperations.get(key, field);
    }

    @Override
    public Map<String, Object> getAllFromHash(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }


    @Override
    public void deleteFromHash(String key, String field) {
        hashOperations.delete(key, field);
    }

    @Override
    public void deleteFromHash(String key) {
        String[] fields = {"refreshToken", "publicKey"};
        hashOperations.delete(key, (Object[]) fields);
    }

    @Override
    public boolean hasField(String key, String field) {
        return hashOperations.hasKey(key, field);
    }
}
