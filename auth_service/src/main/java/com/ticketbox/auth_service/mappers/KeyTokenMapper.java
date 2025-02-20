package com.ticketbox.auth_service.mappers;

import com.ticketbox.auth_service.entity.KeyToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface KeyTokenMapper {

    int save(KeyToken keyToken);

    Optional<KeyToken> getKeyTokenByUserId(int userId);
}
