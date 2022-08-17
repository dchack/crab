package com.crab.test.cache;

import com.crab.test.dto.UserInfo;
import com.crab.test.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/8/14 9:59 AM
 */
@SpringBootTest
public class MultiCacheTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserInfoCache userInfoCache;

    @Test
    public void test(){
        redisTemplate.boundValueOps("k").set("test");
        String value = redisTemplate.boundValueOps("k").get();
        assert "test".equals(value);
    }

    @Test
    public void getUserInfoTest(){
        UserInfo userInfo = userInfoService.getByUserId(1L);
        assert "test".equals(userInfo.getName());
    }


    @Test
    public void getUserInfoCacheableTest(){
        Long userId = 1L;
        UserInfo userInfo = userInfoCache.getCache(1L);
        assert ("test" + userId).equals(userInfo.getName());
    }


}
