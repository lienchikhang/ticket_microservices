package com.ticketbox.auth_service.controller;

import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.response.AppResponse;
import com.ticketbox.auth_service.dto.response.RoleRes;
import com.ticketbox.auth_service.entity.Role;
import com.ticketbox.auth_service.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleController {

    RoleService roleService;


//    @GetMapping
//    public List<Role> getAllRoles() {
//        return roleService.getAll();
//    }

    @PostMapping("/create")
    public AppResponse<RoleRes> create(@RequestBody @Valid RoleCreateReq role) {
        return AppResponse.<RoleRes>builder()
                .statusCode(201)
                .data(roleService.add(role))
                .message("Role created")
                .build();
    }

}
