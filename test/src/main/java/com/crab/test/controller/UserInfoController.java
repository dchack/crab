package com.crab.test.controller;

import com.crab.test.dto.UserInfo;
import com.crab.test.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/8/18 5:58 PM
 */
@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/get/id")
    public String getById(@RequestParam(required = false) Long userId) {
        UserInfo userInfo = userInfoService.getByUserId(userId);
        return "result:" + userInfo.getName();
    }

}
