package com.train.leetcode;

/**
 * 请你来实现一个 atoi 函数，使其能将字符串转换成整数。
 * <p>
 * 首先，该函数会根据需要丢弃无用的开头空格字符，直到寻找到第一个非空格的字符为止。
 * 当我们寻找到的第一个非空字符为正或者负号时，则将该符号与之后面尽可能多的连续数字组合起来，作为该整数的正负号；假如第一个非空字符是数字，则直接将其与之后连续的数字字符组合起来，形成整数。
 * 该字符串除了有效的整数部分之后也可能会存在多余的字符，这些字符可以被忽略，它们对于函数不应该造成影响。
 * <p>
 * 注意：假如该字符串中的第一个非空格字符不是一个有效整数字符、字符串为空或字符串仅包含空白字符时，则你的函数不需要进行转换。
 * 在任何情况下，若函数不能进行有效的转换时，请返回 0。
 * <p>
 * 说明：
 * 假设我们的环境只能存储 32 位大小的有符号整数，那么其数值范围为 [−231,  231 − 1]。如果数值超过这个范围，请返回  INT_MAX (231 − 1) 或 INT_MIN (−231) 。
 * <p>
 * 链接：https://www.leetcode.com/problems/string-to-integer-atoi
 *
 * @author : JF
 * Date    : 2019.10.15
 * Time    : 22:30
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("WeakerAccess")
public class Solution8 {
    /**
     * 字符串转换为整数。
     * <p>
     * 算法的核心部分可以参见{@see Integer.parseInt(String s, int radix)}。
     * <p>
     * 这个问题难度不大，但是细节比较多，特别是最后一句说明，我就在这上面栽过跟头。
     *
     * @param str 字符串
     * @return 字符串转换后的整数值，不满足要求的皆为0。
     */
    public int myAtoi(String str) {
        int len;
        // 题目默认，字符串长度为0时，返回0。
        if (str == null || (len = str.length()) == 0) {
            return 0;
        }
        int i = 0;
        // 忽略前面的空白字符
        for (; i < len && str.charAt(i) == ' '; ++i) ;
        if (i == len) {
            return 0;
        }
        char first = str.charAt(i);
        int value = 0;
        // 获取正负号
        boolean positive = true;
        if (first == '-') {
            positive = false;
        } else if (first != '+' && (value = -Character.digit(first, 10)) == 1) { // 非正负号或者数字返回0
            return 0;
        }
        for (int limit = Integer.MIN_VALUE / 10; ++i < len; ) {
            int digit = Character.digit(str.charAt(i), 10);
            // 遇到非数字符，直接返回结果
            if (digit == -1) {
                return positive ? -value : value;
            }
            // 题目说明的最后一句，正整数溢出返回整型最大值，负整数溢出返回整型最小值
            if (value < limit || (value = 10 * value - digit) == Integer.MIN_VALUE || value > 0) {
                return positive ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
        }
        return positive ? -value : value;
    }
}
