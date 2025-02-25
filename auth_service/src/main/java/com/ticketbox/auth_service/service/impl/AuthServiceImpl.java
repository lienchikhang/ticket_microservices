package com.ticketbox.auth_service.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ticketbox.auth_service.dto.request.LoginReq;
import com.ticketbox.auth_service.dto.response.IntrospectRes;
import com.ticketbox.auth_service.dto.response.LoginRes;
import com.ticketbox.auth_service.dto.response.UserRes;
import com.ticketbox.auth_service.entity.KeyToken;
import com.ticketbox.auth_service.entity.User;
import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import com.ticketbox.auth_service.mappers.KeyTokenMapper;
import com.ticketbox.auth_service.mappers.UserMapper;
import com.ticketbox.auth_service.mapstruct.UserStruct;
import com.ticketbox.auth_service.service.AuthService;
import com.ticketbox.auth_service.service.RedisHashService;
import com.ticketbox.auth_service.utils.keyGenerator.RSAKeyGenerator;
import com.ticketbox.auth_service.utils.token.Token;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    //mappers
    UserMapper userMapper;
    KeyTokenMapper keyTokenMapper;

    //structs
    UserStruct userStruct;

    //others
    PasswordEncoder passwordEncoder;
    RedisHashService redisHashService;

    @Override
    @Transactional
    public LoginRes login(LoginReq req) throws NoSuchAlgorithmException, ParseException, JOSEException {


        //CHECKING: user exist
        User existedUser = userMapper.getUserByEmail(req.getEmail())
                .orElseThrow(() -> new AppException(ErrorEnum.INVALID_INFORMATION));

        //CHECKING: password
        if (!passwordEncoder.matches(req.getPassword(), existedUser.getPassword()))
            throw new AppException(ErrorEnum.INVALID_INFORMATION);

        /**
         * CREATE TOKEN
         * task: create a key pair for signing token
         * task: create access token & refresh token
         * task: save publicKey & refreshToken to dbms
         * task: send both tokens back to the client
         * */

        //task: create a key pair for signing token
        RSAKeyGenerator rsaKeyGenerator = new RSAKeyGenerator();
        Map<String, Object> keyPair = rsaKeyGenerator.generateKeys();
        PublicKey publicKey = (PublicKey) keyPair.get("publicKey");
        String publicKeyString = RSAKeyGenerator.encodeKey(publicKey);

        //task: create access token & refresh token
        Map<String, Object> tokens = Token.createTokenPair(existedUser, (PrivateKey) keyPair.get("privateKey"));

        //task: save publicKey to dbms
        //microtask: get refresh ID
        SignedJWT signedJWT = SignedJWT.parse((String)tokens.get("refreshToken"));
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        redisHashService.saveToHas(String.valueOf(existedUser.getId()), "refreshToken", claimsSet.getJWTID());
        redisHashService.saveToHas(String.valueOf(existedUser.getId()), "publicKey", publicKeyString);

//        keyTokenMapper.save(KeyToken.builder()
//                .publicKey(publicKeyString)
//                .userId(existedUser.getId())
//                .refreshToken((String) jwsObject.getPayload().toJSONObject().get("jti"))
//                .build());

        return LoginRes.builder()
                .at(LocalDateTime.now())
                .accessToken((String)tokens.get("accessToken"))
                .freshToken((String)tokens.get("refreshToken"))
                .user(userStruct.toProxyRes(existedUser))
                .build();
    }

    @Override
    public IntrospectRes introspect(String token) throws ParseException, NoSuchAlgorithmException {

        //init
        IntrospectRes res = IntrospectRes.builder()
                .isValid(false)
                .build();

        /**
         * parse token into JWSObject
         * get userId from sub and check in KeyStore table whether it existed or not
         * -> if not (user has never logged in before) -> false
         *
         * verify token
         * after verifying, check userId from decode vs sub
         * -> if equals -> true
         *
         */

        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        //check in KeyStore table whether it existed or not
        if (redisHashService.exists(String.valueOf(claimsSet.getSubject())).equals(0)) return res;

//        KeyToken keyToken = keyTokenMapper.getKeyTokenByUserId(Integer.parseInt(claimsSet.getSubject())).orElse(null);
//        if (Objects.isNull(keyToken)) return false;

        //handling public key
        PublicKey publicKey = RSAKeyGenerator.decodePublicKey(redisHashService
                .getFromHash(String.valueOf(claimsSet.getSubject()), "publicKey"));

        //get expirationTime
        Date expirationTime = claimsSet.getExpirationTime();

        JWSVerifier jwsVerifier = new RSASSAVerifier((RSAPublicKey) publicKey);

        try {
            if (signedJWT.verify(jwsVerifier) && expirationTime.after(Date.from(Instant.now()))) {
                res.setIsValid(true);
                return res;
            }
        } catch (Exception e) {
            log.error("error in introspect token: {}",e.getMessage());
        }

        return res;

    }

    @Override
    public void logout() {

    }
}
