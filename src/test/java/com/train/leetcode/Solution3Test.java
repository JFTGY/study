package com.train.leetcode;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author : JF
 * Date    : 2019.10.13
 * Time    : 00:02
 * Email   ：tangqibao_620@163.com
 */
public class Solution3Test {

    private Solution3 solution;

    public Solution3Test() {
        solution = new Solution3();
    }

    @Test
    public void lengthOfLongestSubstring4() {
        assertEquals(solution.lengthOfLongestSubstring4("abcabcbb"), 3);
        assertEquals(solution.lengthOfLongestSubstring4("pwwkew"), 3);
    }

    @Test
    public void lengthOfLongestSubstring3() {
        assertEquals(solution.lengthOfLongestSubstring3("abcabcbb"), 3);
        assertEquals(solution.lengthOfLongestSubstring3("pwwkew"), 3);
    }

    @Test
    public void lengthOfLongestSubstring2() {
        assertEquals(solution.lengthOfLongestSubstring2("abcabcbb"), 3);
        assertEquals(solution.lengthOfLongestSubstring2("pwwkew"), 3);
    }

    @Test
    public void lengthOfLongestSubstring1() {
        assertEquals(solution.lengthOfLongestSubstring1("abcabcbb"), 3);
        assertEquals(solution.lengthOfLongestSubstring1("pwwkew"), 3);
    }
}