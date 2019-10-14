package com.train.leetcode;

/**
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
 *
 * @author : JF
 * Date    : 2019.10.13
 * Time    : 17:43
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("WeakerAccess")
public class Solution5 {

    /**
     * 这也是一种暴力解法，原理和方法二类似，不同点是将奇偶回文合为一类进行处理。
     * <p>
     * 通过观察我们可以发现，偶数类与奇数类回文唯一区别就是，偶数类中间是两个相同字符，
     * 两者后续判断回文的方式都是一样，左右两边进行比较。
     * 故我们每次从左向右遍历时，可以先检查相同字符，并作为下次遍历开始索引，再进行回文判断即可。
     *
     * @param s 源字符串
     * @return 字符串中最长子回文字符串
     */
    public String longestPalindrome3(String s) {
        int len = s.length();
        char[] value = s.toCharArray();
        int longestLen = 0, from = 0;
        for (int i = 0; i < len; ) {
            int begin = i - 1;
            for (char ch = value[i]; ++i < len && s.charAt(i) == ch; ) ;
            int end = i;
            for (; begin >= 0 && end < len && value[begin] == value[end]; --begin, ++end) ;
            if (end - ++begin > longestLen) {
                longestLen = end - (from = begin);
            }
        }
        return new String(value, from, longestLen);
    }

    private boolean isPalindrome(String s, int from, int to) {
        while (++from < --to && s.charAt(from) == s.charAt(to)) ;
        return from >= to;
    }

    /**
     * 这也是一种暴力解法，只是搜索方式有所不同。
     * <p>
     * 任何回文都是两种形式，一种奇数情况，比如"aba"，一种是偶数情况"aa"。
     * 假设我们从左向右遍历，以遍历的索引作为回文的中心，则每次遍历仅仅只需要2次回文判断。
     * 而前面的一种暴力解法，最差的时候可能每次遍历都有接近len(数组长度)次的回文判断。
     *
     * @param s 源字符串
     * @return 字符串中最长子回文字符串
     */
    public String longestPalindrome2(String s) {
        int len = s.length();
        int[] values = new int[2];
        for (int i = 0; i < len; ++i) {
            // 奇数回文
            palindrome(s, i, i, Math.max(-1, 2 * i - len), values);
            // 偶数回文
            palindrome(s, i, i - 1, Math.max(-1, 2 * i - len - 1), values);
        }
        return s.substring(values[0], values[1]);
    }

    /**
     * 判断指定区域是否为回文，并记录回文字符串的首、末索引值。
     *
     * @param s      源字符串
     * @param left   指定区域的开始索引值
     * @param right  指定区域的结束索引值
     * @param limit  指定区域的开始索引限制值
     * @param values 记录最大子回文字符串首、末索引值的数组。
     */
    private void palindrome(String s, int left, int right, int limit, int[] values) {
        while (++right > 0 && --left > limit && s.charAt(left) == s.charAt(right)) ;
        if (right - left > values[1] - values[0]) {
            values[0] = left + 1;
            values[1] = right;
        }
    }

    /**
     * 这是一种暴力解法，类似于《希尔排序》的思想。
     * <p>
     * 它从前向后遍历，索引值作为回文长度(步长)，在整个数组中搜索，最后一个回文一定最长。
     *
     * @param s 字符串
     * @return 字符串中最长子回文字符串
     */
    public String longestPalindrome1(String s) {
        int len = s.length();
        int[] values = new int[4];
        for (int i = 0; i < len; i++) {
            for (int j = 0, limit = len - i; j < limit && palindrome(s, j - 1, i + j + 1, values); j++) ;
        }
        return s.substring(values[0], values[1]);
    }

    /**
     * 判断指定区间的字符串是否为回文，如果是回文，记录其首、末索引值。
     *
     * @param s      源字符串
     * @param from   首索引值
     * @param to     末索引值
     * @param values 记录回文首、末索引值的数组。数组的0、1记录真实的首、末索引值，2、3记录临时的首、末索引值。
     * @return 出于遍历方便的考虑，如果指定区域为回文则返回false，否则返回true。
     */
    private static boolean palindrome(String s, int from, int to, int[] values) {
        values[2] = from + 1;
        values[3] = to;
        while (++from <= --to && s.charAt(from) == s.charAt(to)) ;
        if (to < from) {
            values[0] = values[2];
            values[1] = values[3];
        }
        return to > from;
    }
}