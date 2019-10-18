package com.train.leetcode;

/**
 * 给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。
 * 请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。
 * 你可以假设 nums1 和 nums2 不会同时为空。
 *
 * 链接：https://www.leetcode.com/problems/median-of-two-sorted-arrays/
 *
 * @author : JF
 * Date    : 2019.10.13
 * Time    : 15:32
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("WeakerAccess")
public class Solution4 {
    /**
     * 该算法主要利用了《归并》思想。
     * <p>
     * 对于任意一个有序数组arr，令其长度为len，中点mid = len / 2。
     * 如果len为奇数，中位数为arr[mid]，否则中位数就为(arr[mid - 1] + arr[mid]) / 2。
     * <p>
     * 在一个有序数组中查找中位数固然简单，但如果是在两个有序数组中查找中位数，需分两种情况讨论。
     * 因为中位数在奇数情况需要两个数，所以使用cur记录当前值和pre记录前一个值。
     * 1、如果在归并搜索途中就找到中位数。
     * 若两数组之和为奇数，则中位数等于cur，否则中位数等于(cur + pre) / 2。
     * 2、如果归并搜索途中某一个数组arr1搜索到末端。
     * 如果两数组之和为奇数，则中位数在另一个数组arr2中，数学计算即可。
     * 否则，当pre和cur在两个不同数组中时，中位数等于(arr2[mid] + pre) / 2。
     * 当pre和cur在数组arr2中时，中位数等于(arr2[mid] + arr2[mid - 1]) / 2。
     *
     * @param nums1 有序数组1
     * @param nums2 有序数组2
     * @return 两有序数组的中位数
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length, len2 = nums2.length;
        int i1 = 0, i2 = 0, count = 0;
        int median = (len1 + len2) >> 1;
        int curValue, preValue = 0;
        boolean isOdd = ((len1 + len2) & 1) == 1; // 两数组长度之和的奇偶性
        while (i1 < len1 || i2 < len2) {
            // 如果其中一个数组已到末端，则中位数来自于另一个数组中。
            if (i1 == len1 || i2 == len2) {
                if (i1 == len1) {
                    i1 = i2;
                    nums1 = nums2;
                }
                // 获取中位数所处位置
                i1 += median - count;
                // 如果两数组和为奇数，则直接返回中位数即可。
                if (isOdd) {
                    return nums1[i1];
                }
                // 如果两数组和为偶数，则结果为中位数和它前一位数之和的一半。
                if (median == count) {
                    // 若中位数和它前一位数不在同一数组
                    return (double) (preValue + nums1[i1]) / 2;
                } else {
                    // 若中位数和它前一位数在同一数组
                    return (double) (nums1[i1] + nums1[i1 - 1]) / 2;
                }
            }
            curValue = nums1[i1] > nums2[i2] ? nums2[i2++] : nums1[i1++];
            // 如果搜索过程，直接找到中位数。
            if (count++ == median) {
                return isOdd ? curValue : (double) (preValue + curValue) / 2;
            }
            preValue = curValue;
        }
        throw new IllegalArgumentException("参数错误！");
    }
}
