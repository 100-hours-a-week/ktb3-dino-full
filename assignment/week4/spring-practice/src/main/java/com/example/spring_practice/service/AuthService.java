// service/AuthService.java
package com.example.spring_practice.service;

import com.example.spring_practice.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users;
    public AuthService(UserRepository users){ this.users = users; }

    public UserDto login(String username, String password){
        var u = users.findByUsername(username);
        if (u==null) return null;
        return u.password().equals(password) ? u : null;
    }

    public boolean deleteCurrentAccount(HttpSession session) {
        var me = (UserDto) session.getAttribute("loginUser");
        if (me == null) return false;                 // 비로그인 상태
        boolean ok = users.deleteById(me.id());       // 계정 삭제
        if (ok) session.invalidate();                 // 바로 로그아웃(세션 만료)
        return ok;
    }

}
