package com.crab.util;


import java.math.BigDecimal;
import java.util.*;

/**
 * @author:linxianhao
 * @date:2019/11/11 7:54 PM
 * 随机工具类
 **/
public class RandomUtil {

    private static final int[] COIN_ARRAY = new int[]{1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static Random random = new Random(System.currentTimeMillis());
    private static final String CODE_STR = "POIUYTREWASDFGHJKLMNBVCXZ";
    private static final String CODE_WITH_NUM_STR = "C0A2B1C4D3E5F8G7H6I9J0K1L2M8N4O5P6Q7R8S9T0U4V2W3X9Y2ZXC80";

    public static int getInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static int getInt(int bound){
        return random.nextInt(bound);
    }
    /**
     * 传入概率，中签返回true，未中返回false
     * @param probability 概率
     * @return
     */
    public static boolean probability(BigDecimal probability) {
        int bound = 10;
        if (probability.compareTo(BigDecimal.ONE) < 0) {
            int seed = new Random().nextInt(bound);
            int prob = probability.multiply(new BigDecimal(bound)).intValue();

            if (prob <= seed) {
                return false;
            }
        }
        return true;
    }

    public static int rand(int start, int end) {
        return (int) (Math.random() * (end - start) + start);
    }

    /**
     * 摇一摇盘币中奖
     *
     * @param nums 次数
     * @return 中奖盘币
     */
    public static int shakeCoin(int nums) {
        int coin = 0;
        int bound = 2;
        int ten = 10;
        if (random.nextInt(bound + nums * ten) == 0) {
            coin = COIN_ARRAY[random.nextInt(COIN_ARRAY.length)];
        }
        return coin;
    }

    /**
     * 获取指定长度的随机码(纯数字)
     *
     * @param length 长度
     */
    public static String getRandCode(int length) {
        return RandomUtil.getRandCode(length, false);
    }

    /**
     * 获取指定长度的随机码
     *
     * @param length       长度
     * @param hasCharacter 是否包含字符
     */
    public static String getRandCode(int length, boolean hasCharacter) {
        StringBuilder code = new StringBuilder();
        if (hasCharacter) {
            int len = CODE_WITH_NUM_STR.length();
            for (int i = 0; i < length; i++) {
                code.append(CODE_WITH_NUM_STR.charAt(random.nextInt(len)));
            }
        } else {
            for (int i = 0; i < length; i++) {
                code.append(random.nextInt(10));
            }
        }
        return code.toString();
    }

    /**
     * 获取指定长度的随机码 偶数数字,奇数字幕
     *
     * @param length 长度
     */
    public static String getRandCodeStrNum(int length) {
        StringBuilder code = new StringBuilder();
        int len = CODE_STR.length();
        for (int i = 0; i < length; i++) {
            if(i % 2 == 1){
                code.append(random.nextInt(10));
            }else{
                code.append(CODE_STR.charAt(random.nextInt(len)));
            }

        }
        return code.toString();
    }


    /**
     * 红包随机类
     * 本地测试：coin=50000, amount=10000, min=2, cost=5ms
     */
    public static List<String> randomRedPacket(int coin, int amount, int min) {
        min = min > 1 ? min : 1;
        if (coin <= 0 || amount <= 0 || coin / amount < min) {
            return null;
        }
        if (amount == 1) {
            return Collections.singletonList(String.valueOf(coin));
        }
        List<String> result = new ArrayList<>(amount);
        while (amount > 0) {
            int item = min;
            if (amount == 1) {
                item = coin;
            } else {
                int max = coin / amount * 2 - min;
                if (max > min) {
                    item = random.nextInt(max - min + 1) + min;
                }
            }
            result.add(String.valueOf(item));
            coin -= item;
            amount--;
        }

        Collections.shuffle(result);
        return result;
    }

}
