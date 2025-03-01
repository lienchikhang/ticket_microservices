package com.ticketbox.auth_service.utils.keyGenerator;

import com.ticketbox.auth_service.entity.User;
import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import com.ticketbox.auth_service.utils.KeyStoreUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RSAKeyGenerator {

    private static final Logger log = LoggerFactory.getLogger(RSAKeyGenerator.class);
    KeyPairGenerator keyGenerator;

    public RSAKeyGenerator() throws NoSuchAlgorithmException {
        this.keyGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyGenerator.initialize(2048);
    }

    public Map<String, Key> generateKeys(User existedUser) {
        Map<String, Key> keys = new HashMap<>();
        KeyPair keyPair = this.keyGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        try {
            KeyStoreUtil.generateAndStoreUserKey(String.valueOf(existedUser.getId()), KeyStoreUtil.loadOrCreateKeyStore(), keyPair);
        } catch (Exception e) {
            log.error("Error in store keyStore {}", e.getMessage());
            throw new AppException(ErrorEnum.CANNOT_STORE_KEYSTORE);
        }

        keys.put("privateKey", privateKey);
        keys.put("publicKey", publicKey);

        return keys;
    }

    public static PublicKey decodePublicKey(String base64PublicKey){
        try {
            byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            throw new AppException(ErrorEnum.INVALID_DECODE_PUBLIC);
        }
    }

    public static String encodeKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
