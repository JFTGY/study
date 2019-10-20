package com.train.leetcode;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/***
 * @author : JF
 * Date    : 2019.10.20
 * Time    : 17:44
 * Email   ：tangqibao_620@163.com
 */
public class Solution11Test {

    private Solution11 solution;

    public Solution11Test() {
        solution = new Solution11();
    }

    @Test
    public void maxArea() {
        assertEquals(solution.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}), 49);
    }
}