package com.crab.test.service.impl;

import com.crab.test.cache.UserInfoCache;
import com.crab.test.dto.UserInfo;
import com.crab.test.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/8/15 2:09 PM
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoCache userInfoCache;

    @Override
    public UserInfo getByUserId(Long userId) {
        return userInfoCache.get(userId);
    }
}
