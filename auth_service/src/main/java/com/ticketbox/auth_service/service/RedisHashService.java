package com.ticketbox.auth_service.service;

import java.util.Map;
import java.util.Set;

public interface RedisHashService {

     void saveToHas(String key, String field, Object value);

     String getFromHash(String key, String field);

     Map<String, Object> getAllFromHash(String key);

//     Object getFromHash(String key);

     Boolean exists(String key);

     void deleteFromHash(String key, String field);

     void deleteFromHash(String key);

     boolean hasField(String key, String field);

}
