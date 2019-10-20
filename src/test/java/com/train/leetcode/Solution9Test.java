package com.train.leetcode;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author : JF
 * Date    : 2019.10.18
 * Time    : 16:29
 * Email   ：tangqibao_620@163.com
 */
public class Solution9Test {

    private static final int[] TEST_CASE_PARAMS = {-1, 2147483647, 1, 1234321, 1005001, 101, 2222};
    private static final boolean[] TEST_CASE_RESULTS = {false, false, true, true, true, true, true};

    private Solution9 solution;

    public Solution9Test() {
        solution = new Solution9();
    }
    
    @Test
    public void isPalindrome1() {
        for(int i = TEST_CASE_PARAMS.length - 1; i >= 0; --i){
            Assert.assertEquals(solution.isPalindrome1(TEST_CASE_PARAMS[i]), TEST_CASE_RESULTS[i]);
        }
    }

    @Test
    public void isPalindrome2() {
        for(int i = TEST_CASE_PARAMS.length - 1; i >= 0; --i){
            Assert.assertEquals(solution.isPalindrome2(TEST_CASE_PARAMS[i]), TEST_CASE_RESULTS[i]);
        }
    }

    @Test
    public void isPalindrome3() {
        for(int i = TEST_CASE_PARAMS.length - 1; i >= 0; --i){
            Assert.assertEquals(solution.isPalindrome3(TEST_CASE_PARAMS[i]), TEST_CASE_RESULTS[i]);
        }
    }
}