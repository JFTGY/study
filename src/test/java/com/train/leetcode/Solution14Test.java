package com.train.leetcode;

import org.junit.Test;

import static org.junit.Assert.*;

/***
 * @author : JF
 * Date    : 2019.10.20
 * Time    : 18:49
 * Email   ：tangqibao_620@163.com
 */
public class Solution14Test {

    private Solution14 solution;

    public Solution14Test() {
        solution = new Solution14();
    }

    private static final String[][] TEST_CASE_PARAMS = {{"flower","flow","flight"}, {"dog","racecar","car"}, {"aaa","aa","aaa"}};
    private static final String[] TEST_CASE_RESULTS = {"fl", "", "aa"};

    @Test
    public void longestCommonPrefix() {
        for(int i = TEST_CASE_PARAMS.length - 1; i >= 0; --i){
            assertEquals(solution.longestCommonPrefix(TEST_CASE_PARAMS[i]), TEST_CASE_RESULTS[i]);
        }
    }
}