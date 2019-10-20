package com.train.leetcode;

import java.util.HashMap;

/**
 * 罗马数字包含以下七种字符: I， V， X， L，C，D 和 M。
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
 * 给定一个罗马数字，将其转换成整数。输入确保在 1 到 3999 的范围内。
 * <p>
 * 链接：https://www.leetcode.com/problems/roman-to-integer
 *
 * @author : JF
 * Date    : 2019.10.20
 * Time    : 17:50
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Solution13 {
    /**
     * 罗马数字和阿拉伯数字映射表，罗马数字为key，阿拉伯数字为value。
     */
    private static final HashMap<Character, Integer> romanMap = new HashMap<>(8);

    static {
        romanMap.put('I', 1);
        romanMap.put('V', 5);
        romanMap.put('X', 10);
        romanMap.put('L', 50);
        romanMap.put('C', 100);
        romanMap.put('D', 500);
        romanMap.put('M', 1000);
    }

    /**
     * 罗马数字和阿拉伯数字映射数组，罗马数字的Unicode为索引，阿拉伯数字为value。
     */
    private static final int[] romans = new int[89];

    static {
        romans[67] = 100;
        romans[68] = 500;
        romans[86] = 5;
        romans[88] = 10;
        romans[73] = 1;
        romans[76] = 50;
        romans[77] = 1000;
    }

    /**
     * 这个题目和{@see Solution12.intToRoman(int x)}类似。
     * <p>
     * 针对于并列的罗马字符，仅仅用后面字符的阿拉伯数字比前面字符大即可判别。
     * <p>
     * 针对映射问题，一般而言使用Map，但是这个题目的范围较小，使用数组作为映射反而效率更高，一切视情况而定。
     *
     * @param s 罗马数字字符串
     * @return 阿拉伯数字
     */
    public int romanToInt(String s) {
        int len = s.length();
        int result = 0;
        int cur = romans[s.charAt(0)];
        for (int i = 0, limit = len - 1; i < limit; ++i) {
            int next = romans[s.charAt(i + 1)];
            if (cur < next) {
                result -= cur;
            } else {
                result += cur;
            }
            cur = next;
        }
        return result + cur;
    }
}
