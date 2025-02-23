package com.ticketbox.auth_service.utils.token;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.ticketbox.auth_service.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.security.PrivateKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
public class Token {

        public static Map<String, Object> createTokenPair(User payload, PrivateKey privateKey) throws JOSEException, ParseException {

        //initialize
        Map<String, Object> result = new HashMap<>();

        //refreshToken
        JWSObject jwsObjectRefresh = createRefreshToken(payload, privateKey);
        String refreshedToken = jwsObjectRefresh.serialize();

        //accessToken
        JWSObject jwsObjectAccess = createAccessToken(payload,jwsObjectRefresh, privateKey);

        result.put("refreshToken", refreshedToken);
        result.put("accessToken", jwsObjectAccess.serialize());
        return result;
    }

    private static String buildScope(User user) {
        StringJoiner scope = new StringJoiner(" ");
        scope.add(user.getRole().getRoleName());
        user.getRole().getAuthorities().forEach((authority -> {
            scope.add(authority.getAuthorityName());
        }));
        return scope.toString();
    }

    public static JWSObject parseToken(String token) throws ParseException {
            return JWSObject.parse(token);
    }


    private static JWSObject createAccessToken(User payload, JWSObject jwsObjectRefresh, PrivateKey privateKey) throws JOSEException, ParseException {
        //get refreshTokenId
        JWTClaimsSet jwtClaimsSetRefreshToken = JWTClaimsSet.parse(jwsObjectRefresh.getPayload().toString());

        //header
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);

        /*
        * PURPOSE: create payload
        * STEP:
        *   + create jwtClaimsSet
        *   + create Payload from jwtClaimsSet
        * */
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("auth-service")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(Instant.now().plus(50, ChronoUnit.MINUTES).toEpochMilli()))
                .claim("refreshId", jwtClaimsSetRefreshToken.getJWTID())
                .claim("scope", buildScope(payload))
                .subject(payload.getId().toString())
                .build();
        Payload payloadToken = new Payload(jwtClaimsSet.toJSONObject());


        JWSObject jwsObject = new JWSObject(header, payloadToken);
        jwsObject.sign(new RSASSASigner(privateKey));

        //return jwsObject
        return jwsObject;
    }

    private static JWSObject createRefreshToken(User payload, PrivateKey privateKey) throws JOSEException {
        //header
        JWSHeader header = new JWSHeader(JWSAlgorithm.RS256);

        /*
         * PURPOSE: create payload
         * STEP:
         *   + create jwtClaimsSet
         *   + create Payload from jwtClaimsSet
         * */
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("auth-service")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(Instant.now().plus(7, ChronoUnit.DAYS).toEpochMilli()))
                .subject(payload.getId().toString())
                .build();
        Payload payloadToken = new Payload(jwtClaimsSet.toJSONObject());


        JWSObject jwsObject = new JWSObject(header, payloadToken);
        jwsObject.sign(new RSASSASigner(privateKey));

        //return jwsObject
        return jwsObject;
    }
}
