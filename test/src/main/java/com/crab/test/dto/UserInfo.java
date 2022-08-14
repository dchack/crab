package com.crab.test.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/8/14 10:02 AM
 */
@Data
public class UserInfo implements Serializable {

    private String name;

    private Long userId;

    private String headUrl;

    private double weight;
}
