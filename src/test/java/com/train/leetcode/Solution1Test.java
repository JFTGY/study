package com.train.leetcode;

import org.junit.Assert;
import org.junit.Test;

/***
 * @author : JF
 * Date    : 2019.10.11
 * Time    : 00:18
 * Email   ：tangqibao_620@163.com
 */
public class Solution1Test {

    private Solution1 solution;

    public Solution1Test() {
        solution = new Solution1();
    }

    @Test
    public void twoSum() {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        Assert.assertEquals(solution.twoSum(nums, target), new int[]{0, 1});
    }
}