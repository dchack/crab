package rate;

import lombok.Data;

/**
 * 限制速率信息
 * 例如 10个一分钟 ：rate = 10 capacity = 10 unit = 2
 * @author hackdc
 */
@Data
public class RateInfo {

    /**
     * 限流业务名称 使用枚举唯一
     */
    private String name;

    /**
     * 速率
     */
    private int rate;

    /**
     *
     */
    private int capacity;

    /**
     * 1:秒 2：分钟 3：小时 4：天
     */
    private int unit;


    public long getSecondByUnit() {
        switch (this.getUnit()) {
            case 2:
                return 60;
            case 3:
                return 3600;
            case 4:
                return 86400;
            default:
                return 1;
        }
    }

    public RateInfo(int rate, int capacity, int unit, String name) {
        this.rate = rate;
        this.capacity = capacity;
        this.unit = unit;
        this.name = name;
    }
}
