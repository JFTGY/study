package com.train.leetcode;

import java.util.HashMap;

/**
 * 问题：给定一个字符串，请你找出其中不含有重复字符的"最长子串"的长度。
 *
 * @author : JF
 * Date    : 2019.10.12
 * Time    : 21:59
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("WeakerAccess")
public class Solution3 {

    /**
     * 这个题目还有一个隐藏条件，就是字符串的所有字符都在0～255之间。
     *
     * 我们可以用一个长度为256的整型数组来取代{@see lengthOfLongestSubstring3}中的HashMap
     * 字符串的字符记作数组的索引，字符所处字符串索引记作数组值，因此数组也能成为一个高效的映射表。
     *
     * 这个方法其实有钻空子的嫌疑，实用性太低。
     *
     * @param s 源字符串
     * @return 最长非重复子字符串的长度
     */
    public int lengthOfLongestSubstring4(String s) {
        int len = s.length();
        int longestLen = 0;
        int[] map = new int[256];
        int begin = 0;
        for (int i = 0; i < len; ++i) {
            char ch = s.charAt(i);
            if (map[ch] > 0) {
                longestLen = Math.max(longestLen, i - begin);
                begin = Math.max(begin, map[ch]);
            }
            map[ch] = i + 1;
        }
        return Math.max(len - begin, longestLen);
    }

    /**
     * 该算法核心思想是动态规划。
     * <p>
     * 前面暴力解法是从后向前遍历，去寻找子字符串，换言之就是从后向前找相同字符。
     * 如果我们将前面所有字符以及它们最后的索引都存起来，我们就能省去遍历的操作，效率也将大大提升。
     * 该方法采用HashMap来实现存储字符及其索引。
     *
     * @param s 源字符串
     * @return 最长非重复子字符串的长度
     */
    public int lengthOfLongestSubstring3(String s) {
        int len = s.length();
        int longestLen = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        int begin = 0;
        for (int i = 0; i < len; ++i) {
            char ch = s.charAt(i);
            Integer pos;
            if ((pos = map.get(ch)) != null) {
                longestLen = Math.max(longestLen, i - begin);
                begin = Math.max(begin, pos + 1);
            }
            map.put(ch, i);
        }
        return Math.max(len - begin, longestLen);
    }

    /**
     * 该方法是对以前{@see lengthOfLongestSubstring1}方法的优化。
     * 无论是代码量还是效率都得到极大提升，代码思路也更清晰。
     *
     * @param s 源字符串
     * @return 最长非重复子字符串的长度
     */
    public int lengthOfLongestSubstring2(String s) {
        int len = s.length();
        if (len < 2) {
            return len;
        }
        int longestLen = 1;
        for (int i = 1, subLen = 1; i < len; i++) {
            char ch = s.charAt(i);
            int begin = i - 1;
            for (int limit = begin - subLen; begin > limit && s.charAt(begin) != ch; --begin) ;
            longestLen = Math.max(longestLen, subLen = i - begin);
        }
        return longestLen;
    }

    /**
     * 这是我第一次通过时的代码，尽管运行通过，但是代码比较丑陋，优化空间很大。
     * <p>
     * 这个算法是暴力解法，核心思想是从后向前遍历，记录子字符串的长度，作为遍历的截止条件，选择子字符串中最长一个即可。
     *
     * @param s 源字符串
     * @return 最长非重复子字符串的长度
     */
    public int lengthOfLongestSubstring1(String s) {
        int len = s.length();
        if (len == 0) {
            return 0;
        }
        char[] chars = s.toCharArray();
        int maxVal = 1;
        int searchLen = 1;
        for (int i = 1; i < len; i++) {
            int target = chars[i];
            int nextSearchLen = 1;
            for (int j = i - 1, limit = j - searchLen; j > limit; j--) {
                if (chars[j] == target) {
                    break;
                } else {
                    nextSearchLen++;
                }
            }
            searchLen = nextSearchLen;
            if (searchLen > maxVal) {
                maxVal = searchLen;
            }
        }
        return maxVal;
    }
}
