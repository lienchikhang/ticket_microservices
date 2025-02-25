package com.ticketbox.auth_service.security;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ticketbox.auth_service.entity.KeyToken;
import com.ticketbox.auth_service.mappers.KeyTokenMapper;
import com.ticketbox.auth_service.service.AuthService;
import com.ticketbox.auth_service.service.RedisHashService;
import com.ticketbox.auth_service.utils.keyGenerator.RSAKeyGenerator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JwtDecoderCustom implements JwtDecoder {

    AuthService authService;
    RedisHashService redisHashService;

    @NonFinal
    NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            Boolean isValidToken = authService.introspect(token).getIsValid();
            if (!isValidToken) throw new AuthenticationException("INVALID_TOKEN"){};
        } catch (Exception e) {
            throw new AuthenticationException("INVALID_TOKEN"){};
        }


        if (Objects.isNull(nimbusJwtDecoder)) {
            try {
                SignedJWT signedJWT = SignedJWT.parse(token);
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

                PublicKey publicKey = RSAKeyGenerator.decodePublicKey(redisHashService
                        .getFromHash(String.valueOf(claimsSet.getSubject()), "publicKey"));

                nimbusJwtDecoder = NimbusJwtDecoder.withPublicKey((RSAPublicKey) publicKey)
                        .build();

            } catch (Exception e) {
                log.error("JWT Decoding Error", e.getMessage());
            }
        }

        return nimbusJwtDecoder.decode(token);
    }
}
