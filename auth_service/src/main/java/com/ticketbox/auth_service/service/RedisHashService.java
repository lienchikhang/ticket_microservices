package com.ticketbox.auth_service.service;

import java.util.Map;
import java.util.Set;

public interface RedisHashService {

     void saveToHas(String key, String field, Object value);

     Object getFromHash(String key, String field);

     Map<String, Object> getAllFromHash(String key);

     void deleteFromHash(String key, String field);

     void deleteFromHash(String key);

     boolean hasField(String key, String field);

}
