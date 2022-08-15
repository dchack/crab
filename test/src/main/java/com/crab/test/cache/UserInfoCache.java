package com.crab.test.cache;

import com.crab.cache.multi.MultiCache;
import com.crab.test.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
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

}
