package com.crab.cache.multi.cluster;

/**
 *
 * @author hackdc
 * @Date 2022/8/7 4:26 PM
 */
public interface ClusterStrategy {

    void connect();

    void publish(Command cmd);

    void handleCommand(Command cmd);

}
