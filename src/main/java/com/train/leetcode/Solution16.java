package com.train.leetcode;

import java.util.Arrays;

/**
 * 给定一个包括 n 个整数的数组 nums 和 一个目标值 target。找出 nums 中的三个整数，使得它们的和与 target 最接近。返回这三个数的和。假定每组输入只存在唯一答案。
 * <p>
 * 链接：https://www.leetcode.com/problems/3sum-closest
 *
 * @author : JF
 * Date    : 2019.10.21
 * Time    : 19:06
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("unused")
public class Solution16 {

    /**
     * 算法原理和{@see Solution15.threeSum2(int[] nums)}一样。
     *
     * @param nums 整型数组
     * @param target 目标值
     * @return 最接近目标值的三数之和
     */
    public int threeSumClosest(int[] nums, int target) {
        int len;
        if (nums == null || (len = nums.length) < 3) {
            return 0;
        }
        Arrays.sort(nums);
        int closestSum = nums[0] + nums[1] + nums[2];
        for (int first = 0, limit = len - 2; first < limit; ) {
            int firstNum = nums[first], preSum;
            if((preSum = firstNum + nums[len - 2] + nums[len - 1]) < target){
                closestSum = closestSum(closestSum, preSum, target);
                while (++first < limit && nums[first] == firstNum) ;
                continue;
            }
            if((preSum = firstNum + nums[first + 1] + nums[first + 2]) > target){
                closestSum = closestSum(closestSum, preSum, target);
                break;
            }
            int second = first + 1, third = len - 1;
            while (second < third) {
                int secondNum = nums[second], thirdNum = nums[third];
                int sum = firstNum + secondNum + thirdNum;
                if (sum == target) {
                    return sum;
                } else if (sum > target) {
                    while (--third > second && nums[third] == thirdNum) ;
                } else {
                    while (++second < third && nums[second] == secondNum) ;
                }
                closestSum = closestSum(closestSum, sum, target);
            }
            while (++first < limit && nums[first] == firstNum) ;
        }
        return closestSum;
    }

    /**
     * 判断最接近目标值的值
     *
     * @param closestSum 原先最接近目标值的值
     * @param sum 三数之和
     * @param target 目标值
     * @return 最接近目标值的值
     */
    private int closestSum(int closestSum, int sum, int target){
        return Math.abs(target - sum) < Math.abs(target - closestSum) ? sum : closestSum;
    }
}
