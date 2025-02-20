package com.ticketbox.auth_service.controller;

import com.ticketbox.auth_service.dto.request.UserCreateReq;
import com.ticketbox.auth_service.dto.request.UserUpdateReq;
import com.ticketbox.auth_service.dto.request.UserUpdateStatusReq;
import com.ticketbox.auth_service.dto.response.AppResponse;
import com.ticketbox.auth_service.dto.response.PageRes;
import com.ticketbox.auth_service.dto.response.RegisterRes;
import com.ticketbox.auth_service.dto.response.UserRes;
import com.ticketbox.auth_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    //service
    UserService userService;

    //routes

    @GetMapping("/{id}")
    public AppResponse<UserRes> getUserById(@PathVariable(name = "id") int id) {
        return AppResponse.<UserRes>builder()
                .statusCode(200)
                .message("Get successfully!")
                .data(userService.getUserById(id))
                .build();
    }

    @GetMapping
    public AppResponse<PageRes<List<UserRes>>> getAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "fullName", required = false) String hasName,
            @RequestParam(name = "phone", required = false) String hasPhone,
            @RequestParam(name = "gender", required = false) String hasGender,
            @RequestParam(name = "isActive", required = false) String hasActive
    ) {
        //create user filter
        Map<String, Object> filter = new HashMap<>();
        filter.put("fullName", hasName);
        filter.put("phone", hasPhone);
        filter.put("gender", hasGender);
        filter.put("isActive", hasActive);

        return AppResponse.<PageRes<List<UserRes>>>builder()
                .code(200)
                .message("Get successfully!")
                .data(userService.getUsers(page, pageSize, sort, direction, filter))
                .build();
    }

    @PostMapping("/create")
    public AppResponse<RegisterRes> createUser(
            @RequestBody @Valid UserCreateReq req
            ) {
        return AppResponse.<RegisterRes>builder()
                .code(201)
                .data(userService.createUser(req))
                .message("User created!")
                .build();
    }

    @PatchMapping("/update/{id}")
    public AppResponse<UserRes> updateUser(
            @PathVariable(name = "id") int id,
            @RequestBody @Valid UserUpdateReq req
            ) {
        return AppResponse.<UserRes>builder()
                .code(200)
                .data(userService.updateUser(id, req))
                .message("User updated!")
                .build();
    }

    @PatchMapping("/change-status/{id}")
    public AppResponse<Void> changeStatusUser(
            @PathVariable(name = "id") int id,
            @RequestBody @Valid UserUpdateStatusReq req
            ) {
        userService.changeUserStatus(id, req.getIsActive());
        return AppResponse.<Void>builder()
                .code(200)
                .message("User changed!")
                .build();
    }
}
