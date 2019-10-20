package com.train.leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/***
 * @author : JF
 * Date    : 2019.10.20
 * Time    : 23:57
 * Email   ：tangqibao_620@163.com
 */
public class Solution15Test {

    private Solution15 solution;

    public Solution15Test() {
        solution = new Solution15();
    }

    private static final int[] TEST_CASE_PARAMS = {-1, 0, 1, 2, -1, -4};

    private static final List<List<Integer>> TEST_CASE_RESULTS = new ArrayList<>(2);

    static {
        TEST_CASE_RESULTS.add(Arrays.asList(-1, -1, 2));
        TEST_CASE_RESULTS.add(Arrays.asList(-1, 0, 1));
    }

    @Test
    public void threeSum3() {
        List<List<Integer>> result = new ArrayList<>(TEST_CASE_RESULTS);
        List<Integer> temp = result.get(0);
        result.set(0, result.get(1));
        result.set(1, temp);
        assertEquals(solution.threeSum3(TEST_CASE_PARAMS), result);
    }

    @Test
    public void threeSum2() {
        assertEquals(solution.threeSum2(TEST_CASE_PARAMS), TEST_CASE_RESULTS);
    }

    @Test
    public void threeSum1() {
        assertEquals(solution.threeSum1(TEST_CASE_PARAMS), TEST_CASE_RESULTS);
    }
}