package com.ticketbox.auth_service.service;

public interface RedisHashService {

     void savePublicKey(String key, String publicKey);

     void saveRefreshID(String key, String refreshID, Integer ttl);

     String getRefreshID(String key);

     String getPublicKey(String key);

     Boolean exists(String key);

     void deleteFromHash(String key);


}
