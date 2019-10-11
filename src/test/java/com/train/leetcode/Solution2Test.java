package com.train.leetcode;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/***
 * @author : JF
 * Date    : 2019.10.11
 * Time    : 00:38
 * Email   ：tangqibao_620@163.com
 */
public class Solution2Test {

    private Solution2 solution;

    public Solution2Test() {
        solution = new Solution2();
    }

    @Test
    public void addTwoNumbers1() {
        ListNode l1 = LeetcodeUtils.createNode(new int[]{2, 4, 3});
        ListNode l2 = LeetcodeUtils.createNode(new int[]{5, 6, 4});
        assertEquals(solution.addTwoNumbers1(l1, l2), LeetcodeUtils.createNode(new int[]{7, 0, 8}));
    }

    @Test
    public void addTwoNumbers2() {
        ListNode l1 = LeetcodeUtils.createNode(new int[]{2, 4, 3});
        ListNode l2 = LeetcodeUtils.createNode(new int[]{5, 6, 4});
        assertEquals(solution.addTwoNumbers2(l1, l2), LeetcodeUtils.createNode(new int[]{7, 0, 8}));
    }
}