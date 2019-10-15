package com.train.leetcode;

/**
 * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
 *
 * 链接：https://leetcode.com/problems/reverse-integer/
 *
 * @author : JF
 * Date    : 2019.10.15
 * Time    : 20:13
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("WeakerAccess")
public class Solution7 {
    /**
     * 获取整数反转后的结果
     *
     * 算法的思路基本和字符串转换为整数一致，只是方向相反罢了。
     * 具体细节可以参见{@see Integer.parseInt(String s, int radix)}。
     *
     * @param x 整数
     * @return 整数反转后的结果，如果结果溢出，返回0。
     */
    public int reverse(int x){
        boolean positive = x > 0;
        x = x > 0 ? x : -x;
        int value = 0, limit = Integer.MIN_VALUE / 10;
        while(x > 0){
            if(value < limit){
                return 0;
            }
            int y = x % 10;
            value = 10 * value - y;
            if(value > 0){
                return 0;
            }
            x /= 10;
        }
        return positive ? -value : value;
    }
}
