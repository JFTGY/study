package com.train.leetcode;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author : JF
 * Date    : 2019.10.19
 * Time    : 23:12
 * Email   ：tangqibao_620@163.com
 */
public class Solution10Test {

    private static String[][] TEST_CASE_PARAMS = {{"", "c*c*"}, {"a", ".*.a*"}, {"aa", "a"}, {"", ""}, {"aa", "*a*"}};
    private static boolean[] TEST_CASE_RESULT = {true, true, false, true, false};

    private Solution10 solution;

    public Solution10Test() {
        solution = new Solution10();
    }

    @Test
    public void isMatch() {
        for(int i = TEST_CASE_PARAMS.length - 1; i >= 0; --i){
            Assert.assertEquals(solution.isMatch(TEST_CASE_PARAMS[i][0], TEST_CASE_PARAMS[i][1]), TEST_CASE_RESULT[i]);
        }
    }
}