package com.ticketbox.auth_service.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ticketbox.auth_service.dto.request.LoginReq;
import com.ticketbox.auth_service.dto.response.IntrospectRes;
import com.ticketbox.auth_service.dto.response.LoginRes;
import com.ticketbox.auth_service.entity.User;
import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import com.ticketbox.auth_service.mappers.KeyTokenMapper;
import com.ticketbox.auth_service.mappers.UserMapper;
import com.ticketbox.auth_service.mapstruct.UserStruct;
import com.ticketbox.auth_service.service.AuthService;
import com.ticketbox.auth_service.service.RedisHashService;
import com.ticketbox.auth_service.utils.KeyStoreUtil;
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
import java.util.Date;
import java.util.Map;

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
    public LoginRes login(LoginReq req) throws NoSuchAlgorithmException, ParseException {

        //CHECKING: user exist
        User existedUser = userMapper.getUserByEmail(req.getEmail())
                .orElseThrow(() -> new AppException(ErrorEnum.INVALID_INFORMATION));

        //CHECKING: password
        if (!passwordEncoder.matches(req.getPassword(), existedUser.getPassword()))
            throw new AppException(ErrorEnum.INVALID_INFORMATION);

        try {
            //get privateKey
            PrivateKey privateKey = KeyStoreUtil.getUserPrivateKey(String.valueOf(existedUser.getId()),
                    KeyStoreUtil.loadOrCreateKeyStore());

            //create new pair tokens
            Map<String, Object> tokens = Token.createTokenPair(existedUser, privateKey);
            SignedJWT signedJWT = SignedJWT.parse((String) tokens.get("refreshToken"));
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            //update refreshTokenID
            redisHashService.saveToHas(String.valueOf(existedUser.getId()), "refreshToken", claimsSet.getJWTID());

            return LoginRes.builder()
                    .at(LocalDateTime.now())
                    .accessToken((String) tokens.get("accessToken"))
                    .freshToken((String) tokens.get("refreshToken"))
                    .user(userStruct.toProxyRes(existedUser))
                    .build();

        } catch (Exception e) {
            throw new AppException(ErrorEnum.CANNOT_GET_PRIVATE_KEY);
        }
    }

    @Override
    @Transactional
    public IntrospectRes introspect(String token) throws ParseException {

        //init
        IntrospectRes res = IntrospectRes.builder()
                .isValid(false)
                .build();
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        //check whether user has logged in or not
        if (!redisHashService.exists(String.valueOf(claimsSet.getSubject()))) return res;

        //check current token is valid (only valid when token's refreshID == refreshID in redis)
        if (!redisHashService.getFromHash(String.valueOf(claimsSet.getSubject()), "refreshToken")
                .equals(claimsSet.getClaim("refreshId"))) return res;

        //retrieve public key
        String pbKeyStr = redisHashService
                .getFromHash(String.valueOf(claimsSet.getSubject()), "publicKey");
        PublicKey publicKey = RSAKeyGenerator.decodePublicKey(pbKeyStr);

        //get expirationTime
        Date expirationTime = claimsSet.getExpirationTime();

        //verify token
        try {
            JWSVerifier jwsVerifier = new RSASSAVerifier((RSAPublicKey) publicKey);
            if (signedJWT.verify(jwsVerifier) && expirationTime.after(Date.from(Instant.now()))) {
                res.setIsValid(true);
                return res;
            }
        } catch (Exception e) {
            log.error("error in introspect token: {}", e.getMessage());
        }

        return res;
    }

    @Override
    @Transactional
    public void logout(String token) throws ParseException {

        //parse token into claimsSet
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        //remove in keyStore
        redisHashService.deleteFromHash(String.valueOf(claimsSet.getSubject()));

    }
}
