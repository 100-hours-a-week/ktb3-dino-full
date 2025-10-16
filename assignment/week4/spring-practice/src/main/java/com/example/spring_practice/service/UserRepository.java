// service/UserRepository.java
package com.example.spring_practice.service;

import com.example.spring_practice.dto.UserDto;
import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final ConcurrentHashMap<String, UserDto> byUsername = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, UserDto> byId = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    public UserRepository() {
        save("admin","admin","admin");
    }

    public boolean existsByUsername(String username){ return byUsername.containsKey(username); }
    public UserDto findByUsername(String username){ return byUsername.get(username); }

    public UserDto save(String username, String password, String displayName){
        // var liked = ConcurrentHashMap.<Long>newKeySet();
        // var authored = ConcurrentHashMap.<Long>newKeySet();
        var u = new UserDto(seq.incrementAndGet(), username, password, displayName);
        byUsername.put(username, u);
        byId.put(u.id(),u);
        return u;
    }

    public boolean deleteById(Long id){
        var u= byId.remove(id);
        if(u == null) return false;
        byUsername.remove(u.userName());
        return true;
    }

    public UserDto updateDisplayName(Long id, String newDisplayName){
        UserDto old = byId.get(id);
        if (old == null) return null;
        UserDto updated = new UserDto(old.id(), old.userName(), old.password(), newDisplayName);
        byId.put(id, updated);
        byUsername.put(old.userName(), updated);
        return updated;
    }

    public UserDto updatePassword(Long id, String newPassword){
        UserDto old = byId.get(id);
        if (old == null) return null;
        UserDto updated = new UserDto(old.id(), old.userName(), newPassword, old.displayName());
        byId.put(id, updated);
        byUsername.put(old.userName(), updated);
        return updated;
    }

}
