package com.train.leetcode;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author : JF
 * Date    : 2019.10.13
 * Time    : 19:34
 * Email   ：tangqibao_620@163.com
 */
public class Solution5Test {

    private Solution5 solution;

    public Solution5Test() {
        solution = new Solution5();
    }

    @Test
    public void longestPalindrome1() {
        Assert.assertEquals(solution.longestPalindrome1("babad"), "bab");
    }

    @Test
    public void longestPalindrome2() {
        Assert.assertEquals(solution.longestPalindrome2("babad"), "aba");
    }

    @Test
    public void longestPalindrome3() {
        Assert.assertEquals(solution.longestPalindrome3("babad"), "bab");
    }

}