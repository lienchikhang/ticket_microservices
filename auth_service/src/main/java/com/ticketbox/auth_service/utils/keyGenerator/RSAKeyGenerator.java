package com.ticketbox.auth_service.utils.keyGenerator;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import java.security.*;
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

    public static String encodeKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
