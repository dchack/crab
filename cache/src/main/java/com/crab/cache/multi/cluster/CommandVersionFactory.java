package com.crab.cache.multi.cluster;

/**
 * Factory for command version
 *
 * @author hopehack
 * @Date 2022/11/15 4:49 PM
 */
public class CommandVersionFactory {

    /**
     * Default return current time millis
     * @return
     */
    public static String getVersion(){
        return String.valueOf(System.currentTimeMillis());
    }

}
