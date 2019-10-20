package com.train.leetcode;

import org.junit.Test;

import static org.junit.Assert.*;

/***
 * @author : JF
 * Date    : 2019.10.20
 * Time    : 17:44
 * Email   ：tangqibao_620@163.com
 */
public class Solution12Test {

    private Solution12 solution;

    public Solution12Test() {
        solution = new Solution12();
    }

    private static final int[] TEST_CASE_PARAMS = {2950, 54, 3746, 2665, 1607, 2550, 3300, 2793, 400, 3486};
    private static final String[] TEST_CASE_RESULTS = {"MMCML", "LIV", "MMMDCCXLVI", "MMDCLXV", "MDCVII", "MMDL", "MMMCCC", "MMDCCXCIII", "CD", "MMMCDLXXXVI"};

    @Test
    public void intToRoman() {
        for(int i = TEST_CASE_PARAMS.length - 1; i >= 0; --i){
            assertEquals(solution.intToRoman(TEST_CASE_PARAMS[i]), TEST_CASE_RESULTS[i]);
        }
    }
}