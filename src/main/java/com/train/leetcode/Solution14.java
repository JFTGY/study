package com.train.leetcode;

/**
 * 编写一个函数来查找字符串数组中的最长公共前缀。如果不存在公共前缀，返回空字符串 ""。
 * <p>
 * 链接：https://www.leetcode.com/problems/longest-common-prefix/
 *
 * @author : JF
 * Date    : 2019.10.20
 * Time    : 18:25
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Solution14 {

    /**
     * 暴力解法，没啥技巧，一个一个比较即可。
     *
     * @param strs 字符串数组
     * @return 最长公共前缀
     */
    public String longestCommonPrefix(String[] strs) {
        int len;
        if (strs == null || (len = strs.length) == 0) {
            return "";
        }
        String firstStr = strs[0];
        int count = 0, firstStrLen = firstStr.length();
        for (int i = 0, j; i < firstStrLen; ++i) {
            char ch = firstStr.charAt(i);
            for (j = 0; ++j < len && i < strs[j].length() && strs[j].charAt(i) == ch; ) ;
            if (j == len) {
                ++count;
            } else {
                return firstStr.substring(0, count);
            }
        }
        return firstStr;
    }
}
