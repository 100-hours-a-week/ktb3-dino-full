// service/UserRepository.java
package com.example.spring_practice.service;

import com.example.spring_practice.dto.UserDto;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    public UserRepository() {
        save("admin","admin","admin");
        byIdMap.get(0L).authored.add(1L);
    }


    private static class UserEntity{
        Long id; String username;
        String password; String displayName;
        Set<Long> liked = ConcurrentHashMap.newKeySet();
        Set<Long> authored = ConcurrentHashMap.newKeySet();
        Set<Long> authoredComments = ConcurrentHashMap.newKeySet();
        UserDto toDto(){
            return new UserDto(id, username, password, displayName);
        }
    }

    private final Map<Long, UserEntity> byIdMap = new ConcurrentHashMap<>();
    private final Map<String, UserEntity> byUsernameMap = new ConcurrentHashMap<>();
    private final AtomicLong seqId = new AtomicLong(0);


    public boolean existsByUsername(String username){ return byUsernameMap.containsKey(username); }
    public UserDto findByUsername(String username){
        var u = byUsernameMap.get(username);
        if(u==null) return null;
        else return u.toDto();
    }

    public boolean checkAuthor(Long userid, Long postid){
        return byIdMap.get(userid).authored.contains(postid);
    }

    public UserDto save(String username, String password, String displayName) {
        var userEntity = new UserEntity();
        userEntity.id = seqId.getAndIncrement();
        userEntity.username = username;
        userEntity.password = password;
        userEntity.displayName = displayName;
        userEntity.liked = ConcurrentHashMap.newKeySet();
        userEntity.authored = ConcurrentHashMap.newKeySet();
        byUsernameMap.put(username, userEntity);
        byIdMap.put(userEntity.id, userEntity);
        return userEntity.toDto();
    }

    public void writePost(Long userid, Long postid){
        byIdMap.get(userid).authored.add(postid);
    }
    public void deletePost(Long userid,Long postid){
        byIdMap.get(userid).authored.remove(postid);
    }


    public boolean deleteById(Long id){
        var userEntity = byIdMap.get(id);
        if(userEntity == null) return false;
        byUsernameMap.remove(userEntity.username);
        return true;
    }

    public UserDto updateDisplayName(Long id, String newDisplayName){
        UserEntity userEntity = byIdMap.get(id);
        if(userEntity==null) return null;
        userEntity.displayName = newDisplayName;
        return userEntity.toDto();
    }

    public UserDto updatePassword(Long id, String newPassword){
        UserEntity userEntity = byIdMap.get(id);
        if(userEntity==null) return null;
        userEntity.password = newPassword;
        return userEntity.toDto();
    }

    public boolean liked(Long userid, Long postid){
        UserEntity userEntity = byIdMap.get(userid);
        if(userEntity==null) return false;
        if(userEntity.liked.contains(postid)) {
            userEntity.liked.remove(postid);
            return true;
        }
        else{
            userEntity.liked.add(postid);
            return false;
        }
    }
    public Set<Long> getAuthoredPosts(Long userid){
        return  byIdMap.get(userid).authored;
    }
    public Set<Long> getLikedPosts(Long userid){
        return  byIdMap.get(userid).liked;
    }
}
