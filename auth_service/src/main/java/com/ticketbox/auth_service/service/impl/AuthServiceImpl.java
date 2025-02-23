package com.ticketbox.auth_service.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.ticketbox.auth_service.dto.request.LoginReq;
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
import java.text.ParseException;
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

    @Override
    @Transactional
    public LoginRes login(LoginReq req) throws NoSuchAlgorithmException, ParseException, JOSEException {

        log.info("email {}", req.getEmail());
        log.info("pass {}", req.getPassword());

        //CHECKING: user exist
        User existedUser = userMapper.getUserByEmail(req.getEmail())
                .orElseThrow(() -> new AppException(ErrorEnum.INVALID_INFORMATION));

        log.info("user {}", existedUser.getCreatedAt());

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
        JWSObject jwsObject = Token.parseToken((String)tokens.get("refreshToken"));

        keyTokenMapper.save(KeyToken.builder()
                .publicKey(publicKeyString)
                .userId(existedUser.getId())
                .refreshToken((String) jwsObject.getPayload().toJSONObject().get("jti"))
                .build());

        return LoginRes.builder()
                .at(LocalDateTime.now())
                .accessToken((String)tokens.get("accessToken"))
                .freshToken((String)tokens.get("refreshToken"))
                .user(userStruct.toProxyRes(existedUser))
                .build();
    }

    @Override
    public Boolean introspect() {
        return null;
    }

    @Override
    public void logout() {

    }
}
