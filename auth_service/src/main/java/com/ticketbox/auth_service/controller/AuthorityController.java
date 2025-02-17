package com.ticketbox.auth_service.controller;

import com.ticketbox.auth_service.dto.request.AuthorityCreateReq;
import com.ticketbox.auth_service.dto.request.AuthorityUpdateReq;
import com.ticketbox.auth_service.dto.response.AppResponse;
import com.ticketbox.auth_service.dto.response.AuthorityRes;
import com.ticketbox.auth_service.dto.response.PageRes;
import com.ticketbox.auth_service.service.AuthorityService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authorities")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthorityController {

    AuthorityService authorityService;

    @GetMapping("/{id}")
    public AppResponse<AuthorityRes> getById(@PathVariable(name = "id") int id) {
        return AppResponse.<AuthorityRes>builder()
                .data(authorityService.findById(id))
                .message("Get successfully!")
                .statusCode(200)
                .build();
    }

    @GetMapping("/get-unSign-authorities-by-role/{id}")
    public AppResponse<List<AuthorityRes>> getUnsignedAuthoritiesByRoleId(
            @PathVariable(name = "id") int id
    ) {
        return AppResponse.<List<AuthorityRes>>builder()
                .statusCode(200)
                .data(authorityService.getUnsignedAuthoritiesByRoleId(id))
                .message("Get successfully!")
                .build();
    }

    @GetMapping
    public AppResponse<PageRes<List<AuthorityRes>>> getAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "6") int pageSize,
            @RequestParam(name = "sort", defaultValue = "created_at") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction

    ) {
        return AppResponse.<PageRes<List<AuthorityRes>>>builder()
                .statusCode(200)
                .data(authorityService.findAll(page, pageSize, sort, direction))
                .message("Get successfully!")
                .build();
    }

    @PostMapping("/create")
    public AppResponse<AuthorityRes> create(@RequestBody @Valid AuthorityCreateReq req) {
        return AppResponse.<AuthorityRes>builder()
                .statusCode(201)
                .data(authorityService.add(req))
                .message("Authority created!")
                .build();
    }

    @PatchMapping("/update/{id}")
    public AppResponse<AuthorityRes> update(
            @PathVariable(name = "id") int id,
            @RequestBody @Valid AuthorityUpdateReq req
    ) {
        return AppResponse.<AuthorityRes>builder()
                .statusCode(200)
                .data(authorityService.update(id, req))
                .message("Authority updated!")
                .build();
    }

    @DeleteMapping("delete/{id}")
    public AppResponse<AuthorityRes> delete(@PathVariable("id") int id) {
        return AppResponse.<AuthorityRes>builder()
                .message("Authority deleted!")
                .code(200)
                .data(authorityService.delete(id))
                .build();
    }
}
