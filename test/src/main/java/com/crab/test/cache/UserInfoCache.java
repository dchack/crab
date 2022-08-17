package com.crab.test.cache;

import com.crab.cache.multi.MultiCache;
import com.crab.test.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

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

    @Cacheable(cacheNames = "userInfo")
    public UserInfo getCache(Long userId) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setName("test" + userId);
        userInfo.setWeight(120);
        userInfo.setHeadUrl("/static/head1.jpg");
        return userInfo;
    }

}
