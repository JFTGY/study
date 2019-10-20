package com.train.leetcode;

/**
 * 给定 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。
 * 在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * <p>
 * 说明：你不能倾斜容器，且 n 的值至少为 2。
 * <p>
 * 链接：https://www.leetcode.com/problems/container-with-most-water
 *
 * @author : JF
 * Date    : 2019.10.19
 * Time    : 23:36
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Solution11 {

    /**
     * 这个题目很容易让人想到二重循环的暴力解法，但是却有一些容易让人忽视的地方。
     *
     * 题目中水槽的面积area = min(a[left], a[right]) * (right - left)。
     * 根据面积公式，要想找到最大值，我们可以从right - left入手，即两个指针遍历方向从最左边和最右边开始。
     *
     * 以题目的实例[1,8,6,2,5,4,8,3,7]为参考。
     * 当我们选择最左边的1和最右边的7时，area = min(1, 7) * 8 = 8，此时的面积是长度为1水槽最大的面积。
     * 则我们可以将左边的1前进一位变成8，area = min(8, 7) * 7 = 49，此时的面积是长度为7水槽最大的面积。
     * 接着再将右边的7后退一位，以此类推，最终索引值left = right，即可找到最大值(因为每一次移动都找到局部最大值，汇总所有局部最大值，就是整体最大值)。
     *
     * <ps> 这个题目有点像小学做的课外拓展题，小学时代真的让人无比怀念呀。
     *
     * @param height 水槽挡板高度数组
     * @return 最大水槽面积
     */
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;
        while(left < right){
            int area = Math.min(height[left], height[right]) * (right - left);
            if(area > maxArea){
                maxArea = area;
            }
            if(height[left] > height[right]){
                --right;
            }else{
                ++left;
            }
        }
        return maxArea;
    }
}
