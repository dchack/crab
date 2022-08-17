package com.crab.test.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/8/14 10:02 AM
 */
@Data
public class UserInfo implements Serializable {

    public UserInfo(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }

    public UserInfo() {
    }

    private String name;

    private Long userId;

    private String headUrl;

    private double weight;
}
