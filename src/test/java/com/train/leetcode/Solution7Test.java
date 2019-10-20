package com.train.leetcode;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author : JF
 * Date    : 2019.10.15
 * Time    : 22:22
 * Email   ：tangqibao_620@163.com
 */
public class Solution7Test {

    private static final int[] TEST_CASE_PARAMS = {0, Integer.MIN_VALUE, Integer.MAX_VALUE, -1, 123456789, 1234567890, 1234567898};
    private static final int[] TEST_CASE_RESULTS = {0, 0, 0, -1, 987654321, 987654321, 0};

    private Solution7 solution;

    public Solution7Test() {
        solution = new Solution7();
    }

    @Test
    public void reverse() {
        for(int i = 0; i < TEST_CASE_PARAMS.length; ++i){
            Assert.assertEquals(solution.reverse(TEST_CASE_PARAMS[i]), TEST_CASE_RESULTS[i]);
        }
    }
}