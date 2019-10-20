package com.train.leetcode;

/**
 * 罗马数字包含以下七种字符： I， V， X， L，C，D 和 M。
 * <p>
 * 字符          数值
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 * 例如， 罗马数字 2 写做 II ，即为两个并列的 1。12 写做 XII ，即为 X + II 。 27 写做  XXVII, 即为 XX + V + II 。
 * <p>
 * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 IIII，而是 IV。数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 IX。这个特殊的规则只适用于以下六种情况：
 * <p>
 * I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
 * X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。 
 * C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
 * 给定一个整数，将其转为罗马数字。输入确保在 1 到 3999 的范围内。
 * <p>
 * 链接：https://www.leetcode.com/problems/integer-to-roman
 *
 * @author : JF
 * Date    : 2019.10.20
 * Time    : 17:10
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Solution12 {

    private static final int[] nums = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

    private static final String[] romans = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    /**
     * 这个题目有点像整数划分(从大到小)。
     * <p>
     * 难点在4、9、90等需要并列的罗马字上，因为题目只需要转换1～3999，所以直接手工转换即可，工作量极小。
     *
     * @param x 正整数
     * @return 罗马字符串
     */
    public String intToRoman(int x) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 13 && x > 0; ) {
            if (x >= nums[i]) {
                x -= nums[i];
                result.append(romans[i]);
            } else {
                ++i;
            }
        }
        return result.toString();
    }
}
