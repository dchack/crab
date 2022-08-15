package com.crab.test.service;

import com.crab.test.dto.UserInfo;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/8/15 2:08 PM
 */
public interface UserInfoService {

    UserInfo getByUserId(Long userId);

}
