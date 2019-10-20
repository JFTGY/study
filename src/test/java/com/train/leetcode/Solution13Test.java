package com.train.leetcode;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/***
 * @author : JF
 * Date    : 2019.10.20
 * Time    : 17:52
 * Email   ：tangqibao_620@163.com
 */
public class Solution13Test {

    private Solution13 solution;

    public Solution13Test() {
        solution = new Solution13();
    }

    private static final String[] TEST_CASE_PARAMS = {"MMCML", "LIV", "MMMDCCXLVI", "MMDCLXV", "MDCVII", "MMDL", "MMMCCC", "MMDCCXCIII", "CD", "MMMCDLXXXVI"};
    private static final int[] TEST_CASE_RESULTS = {2950, 54, 3746, 2665, 1607, 2550, 3300, 2793, 400, 3486};

    @Test
    public void romanToInt() {
        for(int i = TEST_CASE_PARAMS.length - 1; i >= 0; --i){
            assertEquals(solution.romanToInt(TEST_CASE_PARAMS[i]),TEST_CASE_RESULTS[i]);
        }
    }
}