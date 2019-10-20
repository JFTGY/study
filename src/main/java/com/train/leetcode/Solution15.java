package com.train.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 给定一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？找出所有满足条件且不重复的三元组。
 * <p>
 * 注意：答案中不可以包含重复的三元组。
 * <p>
 * 链接：https://www.leetcode.com/problems/3sum
 *
 * @author : JF
 * Date    : 2019.10.20
 * Time    : 18:54
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Solution15 {
    public static void main(String[] args) {
        for (List<Integer> list : new Solution15().threeSum1(new int[]{-1, 0, 1, 2, -1, -4})) {
            System.out.println(list);
        }
    }

    /**
     * 这是我以前的一个解法，将三数相加分为三种情况：包含零；两正一负；两负一正。
     * <p>
     * 算法借用了桶排序的思想，实用性较差，三种情况讨论起来也比较麻烦，但是效率很高，远胜前两种方法。
     *
     * @param nums 数组
     * @return 所有三数相加为0的结果
     */
    public List<List<Integer>> threeSum3(int[] nums) {
        int len;
        if (nums == null || (len = nums.length) < 3) {
            return new ArrayList<>();
        }
        Arrays.sort(nums);
        // 借用桶排序思想，取最大值，求取数组中各值众数。
        int maxNum = Math.max(Math.abs(nums[0]), nums[len - 1]);
        byte[] hashes = new byte[(maxNum << 1) + 1];
        for (int num : nums) {
            hashes[num + maxNum]++;
        }
        // 找数组中0的位置
        int zero = Arrays.binarySearch(nums, 0);
        int positive = zero, negative = zero;
        List<List<Integer>> result = new ArrayList<>();
        // 包含零的情况
        if (zero > -1) {
            // 因为0的位置不确定，故左右遍历找寻0值的边界。
            while (++positive < len && nums[positive] == 0) ;
            while (--negative >= 0 && nums[negative] == 0) ;
            int zeroNum = positive - negative - 1;
            // 包含零的情况分两种，一个是0、0、0，一个是正、0、负
            if (zeroNum > 0) {
                if (zeroNum >= 3) {
                    result.add(Arrays.asList(0, 0, 0));
                }
                for (int i = 0; i <= negative; ) {
                    int negativeNum = nums[i];
                    if (hashes[-negativeNum + maxNum] > 0) {
                        result.add(Arrays.asList(negativeNum, 0, -negativeNum));
                    }
                    while (++i <= negative && nums[i] == negativeNum) ;
                }
            }
        } else {
            // 数组中没有0，则利用二分搜索结果来推算正数的首边界，偶数末边界
            positive = ~zero; // nums[low] > 0 and zero = -(low + 1)
            negative = positive - 1;
        }
        // 两个正数和一个负数的情况
        for (int i = 0; i <= negative; ) {
            // 计算两个正数的和
            int negativeNum = nums[i], target = -negativeNum;
            int half;
            // 找寻目标值的中间位置，便于无重复搜索结果。
            if ((negativeNum & 1) == 1) {
                half = (-negativeNum >> 1) + 1;
            } else {
                half = -negativeNum >> 1;
                // 两正数相同的情况
                if (hashes[half + maxNum] > 1) {
                    result.add(Arrays.asList(negativeNum, half, half));
                }
            }
            // 搜索方式类似"左右开弓反转数组"的算法
            for (int j = positive; j < len && nums[j] < half; ) {
                int positiveNum1 = nums[j];
                while (++j < len && nums[j] == positiveNum1) ;
                int positiveNum2 = target - positiveNum1;
                if (hashes[positiveNum2 + maxNum] > 0) {
                    result.add(Arrays.asList(negativeNum, positiveNum1, positiveNum2));
                }
            }
            while (++i <= negative && nums[i] == negativeNum) ;
        }
        // 两个负数和一个正数的情况，操作和前面类似
        for (int i = positive; i < len; ) {
            int positiveNum = nums[i], target = -positiveNum;
            int half;
            if ((positiveNum & 1) == 1) {
                half = -((positiveNum >> 1) + 1);
            } else {
                half = -(positiveNum >> 1);
                if (hashes[half + maxNum] > 1) {
                    result.add(Arrays.asList(half, half, positiveNum));
                }
            }
            for (int j = negative; j >= 0 && nums[j] > half; ) {
                int negativeNum1 = nums[j];
                while (--j >= 0 && nums[j] == negativeNum1) ;
                int negativeNum2 = target - negativeNum1;
                if (hashes[negativeNum2 + maxNum] > 0) {
                    result.add(Arrays.asList(negativeNum1, negativeNum2, positiveNum));
                }
            }
            while (++i < len && nums[i] == positiveNum) ;
        }
        return result;
    }

    /**
     * 该算法是属于前面的优化版本，做了一些"剪枝"操作，效率提升十分明显。
     * 消除了HashSet这个去重的骚操作。
     *
     * @param nums 数组
     * @return 所有三数相加为0的结果
     */
    public List<List<Integer>> threeSum2(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> result = new ArrayList<>();
        if (len < 3 || nums[0] > 0 || nums[len - 1] < 0) {
            return result;
        }
        int target = -nums[len - 1] - nums[len - 2];
        int first = -1, limit = len - 2;
        while (++first < limit && nums[first] < target) ;
        while (first < limit) {
            if (nums[first] + nums[first + 1] + nums[first + 2] > 0) {
                break;
            }
            int second = first + 1, third = len - 1, firstNum = nums[first];
            while (second < third) {
                int sum = firstNum + nums[second] + nums[third];
                if (sum > 0) {
                    --third;
                } else if (sum < 0) {
                    ++second;
                } else {
                    result.add(Arrays.asList(firstNum, nums[second], nums[third]));
                    while (++second < third && nums[second] == nums[second - 1]) ;
                    while (--third > second && nums[third] == nums[third + 1]) ;
                }
            }
            while (++first < limit && nums[first] == firstNum) ;
        }
        return result;
    }

    /**
     * 三个数相加，普通的三重遍历解法必定效率较低，因此可以先对数组排序，
     * 利用有序数组的性质，可以将其三重遍历降为二重遍历，效率会有所提升。
     * <p>
     * 该算法胜在逻辑清晰，便于理解算法思路。但是它太过于为暴力，未对遍历情况有任何"剪枝"，故执行效率很低。
     * 利用HashSet剔除重复结果，这骚操作不知道是该哭还是笑。😂😂😂
     *
     * @param nums 数组
     * @return 所有三数相加为0的结果
     */
    public List<List<Integer>> threeSum1(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        HashSet<List<Integer>> result = new HashSet<>();
        for (int first = 0, limit = len - 2; first < limit; ++first) {
            int second = first + 1, third = len - 1;
            while (second < third) {
                int sum = nums[first] + nums[second] + nums[third];
                if (sum > 0) {
                    --third;
                } else if (sum < 0) {
                    ++second;
                } else {
                    result.add(Arrays.asList(nums[first], nums[second++], nums[third--]));
                }
            }
        }
        return new ArrayList<>(result);
    }
}
