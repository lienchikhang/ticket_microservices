package com.ticketbox.auth_service.security;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ticketbox.auth_service.entity.KeyToken;
import com.ticketbox.auth_service.mappers.KeyTokenMapper;
import com.ticketbox.auth_service.service.AuthService;
import com.ticketbox.auth_service.utils.keyGenerator.RSAKeyGenerator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Objects;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JwtDecoderCustom implements JwtDecoder {

    AuthService authService;
    KeyTokenMapper keyTokenMapper;

    @NonFinal
    NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            if (!authService.introspect(token)) throw new JwtException("Token Invalid!");
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }

//        if (Objects.isNull(nimbusJwtDecoder)) {
//
//            //handling public key
//            PublicKey publicKey = RSAKeyGenerator.decodePublicKey(keyToken.getPublicKey());
//
//            nimbusJwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
//        }

        return nimbusJwtDecoder.decode(token);
    }
}
