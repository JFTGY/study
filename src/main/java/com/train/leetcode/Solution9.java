package com.train.leetcode;

/**
 * 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * <p>
 * 链接：https://www.leetcode.com/problems/palindrome-number/
 *
 * @author : JF
 * Date    : 2019.10.18
 * Time    : 15:59
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("WeakerAccess")
public class Solution9 {

    /**
     * 利用了判断回文数组的思路。
     * <p>
     * 将整数转换为字符串，接着再判断回文，算法的低效主要体现在整数转换为字符串这一步。
     *
     * @param x 整数
     * @return 整数是否是回文数
     */
    public boolean isPalindrome1(int x) {
        if (x < 0) {
            return false;
        }
        String number = String.valueOf(x);
        int left = -1;
        int right = number.length();
        while (++left < --right && number.charAt(left) == number.charAt(right)) ;
        return left == right || left > right;
    }

    /**
     * 判断思路和前面那个一致，区别是这个无需转换为字符串，直接在数字上计算并判断。
     *
     * @param x 整数
     * @return 整数是否是回文数
     */
    public boolean isPalindrome2(int x) {
        if (x < 0) {
            return false;
        }
        int leftPow = maxPow(x);
        int rightPow = 1;
        while (leftPow > rightPow) {
            if ((x / leftPow) % 10 != (x / rightPow) % 10) {
                return false;
            }
            leftPow /= 10;
            rightPow *= 10;
        }
        return true;
    }

    private final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999};

    /**
     * 获取一个数最大的十幂次值，比如maxPow(12)=10，maxPow(987)=100。
     *
     * @param x 整数
     * @return 整数最大的十幂次值
     */
    private int maxPow(int x) {
        int i = sizeTable.length;
        while (--i >= 0 && x <= sizeTable[i]) ;
        return i >= 0 ? sizeTable[i] + 1 : 1;
    }

    /**
     * 利用左右两边数值相等来判断回文数。
     *
     * @param x 整数
     * @return 整数是否是回文数
     */
    public boolean isPalindrome3(int x) {
        if (x < 0 || (x > 0 && (x % 10) == 0)) {
            return false;
        }
        int reverse = 0;
        while (x > reverse) {
            int ones = x % 10;
            x /= 10;
            reverse = reverse * 10 + ones;
        }
        return x == reverse || (x == reverse / 10);
    }
}
