// src/main/java/com/example/spring_practice/api/AccountApi.java
package com.example.spring_practice.api;

import com.example.spring_practice.api.dto.DisplayNameUpdateIn;
import com.example.spring_practice.api.dto.ErrorResponse;
import com.example.spring_practice.api.dto.PasswordUpdateIn;
import com.example.spring_practice.dto.UserDto;
import com.example.spring_practice.service.AuthService;
import com.example.spring_practice.service.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping("/api")
public class AccountApi {

    private final AuthService authService;
    private final UserRepository users;

    public AccountApi(AuthService authService, UserRepository users) {
        this.authService = authService;
        this.users = users;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(HttpSession session) {
        UserDto me = (UserDto) session.getAttribute("loginUser");
        return ResponseEntity.ok(Map.of(
                "id", me.id(),
                "userName", me.userName(),
                "displayName", me.displayName()
        ));
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(HttpSession session) {
        authService.deleteCurrentAccount(session);
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/display-name")
    public ResponseEntity<?> changeDisplayName(@RequestBody DisplayNameUpdateIn in, HttpSession session) {
        if (in == null || in.displayName() == null || in.displayName().isBlank())
            return badRequest().body(new ErrorResponse("BAD_REQUEST","displayName은 필수입니다.", Instant.now()));
        var me = (UserDto) session.getAttribute("loginUser");
        var updated = authService.updateCurrentAccountDisplayName(session, in.displayName());
        session.invalidate();
        return ResponseEntity.ok(Map.of(
                "message", "DISPLAY_NAME_CHANGED_RELOGIN_REQUIRED",
                "username", updated.userName(),
                "displayName", updated.displayName()
        ));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordUpdateIn in, HttpSession session) {
        if (in == null || in.newPassword() == null || in.newPassword2() ==null || !in.newPassword().equals(in.newPassword2())
                || in.newPassword().isBlank())
            return badRequest().body(new ErrorResponse("BAD_REQUEST","Password 미입력 또는 불일치.", Instant.now()));
        var me = (UserDto) session.getAttribute("loginUser");
        var updated = users.updatePassword(me.id(), in.newPassword());
        session.invalidate();
        return ResponseEntity.ok(Map.of(
                "message", "PASSWORD_CHANGED_RELOGIN_REQUIRED",
                "username", updated.userName(),
                "displayName", updated.displayName()
        ));
    }

}
