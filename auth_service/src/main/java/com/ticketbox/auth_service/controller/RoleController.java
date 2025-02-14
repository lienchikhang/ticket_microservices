package com.ticketbox.auth_service.controller;

import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.request.RoleUpdateReq;
import com.ticketbox.auth_service.dto.response.AppResponse;
import com.ticketbox.auth_service.dto.response.RoleRes;
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

    @GetMapping("/{id}")
    public AppResponse<RoleRes> getById(
            @PathVariable(name = "id") int id
    ) {
        return AppResponse.<RoleRes>builder()
                .statusCode(200)
                .message("Get successful!")
                .data(roleService.get(id))
                .build();
    }

    @GetMapping
    public AppResponse<List<RoleRes>> getAllRoles(
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "6") int pageSize
    ) {
        return AppResponse.<List<RoleRes>>builder()
                .statusCode(200)
                .message("Get successful!")
                .data(roleService.getAll(direction, page, pageSize))
                .build();
    }

    @PostMapping("/create")
    public AppResponse<RoleRes> create(@RequestBody @Valid RoleCreateReq role) {
        return AppResponse.<RoleRes>builder()
                .statusCode(201)
                .data(roleService.add(role))
                .message("Role created!")
                .build();
    }

    @PatchMapping("/update/{id}")
    public AppResponse<RoleRes> update(
            @RequestBody @Valid RoleUpdateReq roleUpdateReq,
            @PathVariable(name = "id") int id
            ) {
        return AppResponse.<RoleRes>builder()
                .statusCode(200)
                .data(roleService.update(id, roleUpdateReq))
                .message("Role updated!")
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public AppResponse<RoleRes> delete(
            @PathVariable(name = "id") int id
    ) {
        return AppResponse.<RoleRes>builder()
                .statusCode(200)
                .message("Role deleted!")
                .data(roleService.delete(id))
                .build();
    }
}
