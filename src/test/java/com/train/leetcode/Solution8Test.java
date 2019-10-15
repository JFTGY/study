package com.train.leetcode;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author : JF
 * Date    : 2019.10.15
 * Time    : 23:31
 * Email   ：tangqibao_620@163.com
 */
public class Solution8Test {

    private static String[] TEST_CASE_PARAMS = {"-91283472332", "2147483648", "+1", "1234 34", "-1222223333 3", ""};
    private static int[] TEST_CASE_RESULT = {-2147483648, 2147483647, 1, 1234, -1222223333, 0};

    private Solution8 solution;

    public Solution8Test() {
        solution = new Solution8();
    }

    @Test
    public void myAtoi() {
        for(int i = 0; i < TEST_CASE_PARAMS.length; ++i){
            Assert.assertEquals(solution.myAtoi(TEST_CASE_PARAMS[i]), TEST_CASE_RESULT[i]);
        }
    }
}