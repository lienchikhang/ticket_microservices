package com.ticketbox.auth_service.utils.keyGenerator;

import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RSAKeyGenerator {

    KeyPairGenerator keyGenerator;

    public RSAKeyGenerator() throws NoSuchAlgorithmException {
        this.keyGenerator = KeyPairGenerator.getInstance("RSA");
        this.keyGenerator.initialize(4096);
    }

    public Map<String, Object> generateKeys() {
        Map<String, Object> keys = new HashMap<String, Object>();
        KeyPair keyPair = this.keyGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

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
