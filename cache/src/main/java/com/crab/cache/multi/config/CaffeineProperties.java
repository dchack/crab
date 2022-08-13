package com.crab.cache.multi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author hackdc
 * @Date 2022/7/29 11:40 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaffeineProperties {

    private int size;
    private int expire;
}
