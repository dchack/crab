package com.crab.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Get host name and ip of the host.
 */
public final class HostNameUtil {

    private final static Logger logger = LoggerFactory.getLogger(HostNameUtil.class);

    private static String ip;
    private static String hostName;

    static {
        try {
            // Init the host information.
            resolveHost();
        } catch (Exception e) {
            logger.error("Failed to get local host", e);
        }
    }

    private static void resolveHost() throws Exception {
        InetAddress addr = InetAddress.getLocalHost();
        hostName = addr.getHostName();
        ip = addr.getHostAddress();
        if (addr.isLoopbackAddress()) {
            // find the first IPv4 Address that not loopback
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface in = interfaces.nextElement();
                Enumeration<InetAddress> addrs = in.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress address = addrs.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        ip = address.getHostAddress();
                    }
                }
            }
        }
    }

    public static String getIp() {
        return ip;
    }

    public static String getHostName() {
        return hostName;
    }

    public static String getConfigString() {
        return "{\n"
            + "\t\"machine\": \"" + hostName + "\",\n"
            + "\t\"ip\": \"" + ip + "\"\n"
            + "}";
    }

    private HostNameUtil() {}
}
