package com.crab.test.cache;

import com.crab.test.dto.UserInfo;
import com.crab.test.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * Test
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
        UserInfo userInfo = userInfoCache.getCache(userId);
        assert ("test" + userId).equals(userInfo.getName());
    }

    @Test
    public void getUserInfoCacheableMultiTest(){
        Long userId = 1L;
        UserInfo userInfo = userInfoCache.getCache(userId);
        UserInfo userInfo1 = userInfoCache.getCache(userId);
        assert ("test" + userId).equals(userInfo.getName());
        assert ("test" + userId).equals(userInfo1.getName());
    }

    @Test
    public void getUserInfoCacheableNullTest(){
        Long userId = 2L;
        UserInfo userInfo = userInfoCache.getCacheNull(userId);
        UserInfo userInfo1 = userInfoCache.getCacheNull(userId);
        assert userInfo == null;
        assert userInfo1 == null;
    }

    @Test
    public void evictUserInfoCacheTest() {
        Long userId = 1L;
//        UserInfo userInfo = userInfoCache.getCache(1L);
        userInfoCache.evict(userId);
//        userInfoCache.getCache(1L);
    }

    @Test
    public void getAllUserInfoTest() {
        List<UserInfo> list = userInfoCache.getAll();
        assert list.size() > 0;
    }


}
