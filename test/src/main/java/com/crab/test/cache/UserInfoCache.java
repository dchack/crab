package com.crab.test.cache;

import com.crab.cache.multi.MultiCache;
import com.crab.test.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/8/15 2:10 PM
 */
@Component
public class UserInfoCache {

    @Autowired
    private MultiCache<UserInfo> userInfoMultiCache;

    public UserInfo get(Long userId) {
        return userInfoMultiCache.get(String.valueOf(userId));
    }

    @Cacheable(cacheNames = "userInfo", key = "#userId")
    public UserInfo getCache(Long userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setName("test" + userId);
        userInfo.setWeight(120);
        userInfo.setHeadUrl("/static/head1.jpg");
        return userInfo;
    }

    @Cacheable(cacheNames = "userInfo-null", key = "#userId")
    public UserInfo getCacheNull(Long userId) {
        return null;
    }

    @CacheEvict(cacheNames = "userInfo", key = "#userId")
    public void evict(Long userId) {
    }

    @Cacheable(cacheNames = "allUserInfo")
    public List<UserInfo> getAll() {
        List<UserInfo> list = new ArrayList<>();
        list.add(new UserInfo("crab", 99L));
        list.add(new UserInfo("crab1", 100L));
        list.add(new UserInfo("crab2", 101L));
        return list;
    }


}
