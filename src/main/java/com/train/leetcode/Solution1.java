package com.train.leetcode;

import java.util.HashMap;

/**
 * 问题：给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那两个整数，并返回他们的数组下标，数字不能重复使用。
 *
 * @author : JF
 * Date    : 2019.10.11
 * Time    : 23:14
 * Email   ：tangqibao_620@163.com
 */

@SuppressWarnings("WeakerAccess")
public class Solution1 {
    /**
     * 动态规划思想，记录前面的加数，找后面的被加数即可。
     */
    public int[] twoSum(int[] nums, int target) {
        int len;
        if (nums == null || (len = nums.length) < 2) {
            return null;
        }
        HashMap<Integer, Integer> numMap = new HashMap<>();
        for (int cur = 0; cur < len; ++cur) {
            Integer prev;
            if ((prev = numMap.get(target - nums[cur])) != null) {
                return new int[]{prev, cur};
            }
            numMap.put(nums[cur], cur);
        }
        return null;
    }
}
