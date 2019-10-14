package com.train.leetcode;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author : JF
 * Date    : 2019.10.13
 * Time    : 15:34
 * Email   ：tangqibao_620@163.com
 */
public class Solution4Test {

    private Solution4 solution;

    public Solution4Test() {
        solution = new Solution4();
    }

    @Test
    public void findMedianSortedArrays() {
        int[] nums1 = {1, 2}, nums2 = {3, 4};
        Assert.assertEquals(solution.findMedianSortedArrays(nums1, nums2), 2.5, 0.00001);
    }
}