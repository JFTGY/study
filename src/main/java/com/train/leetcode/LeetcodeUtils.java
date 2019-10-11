package com.train.leetcode;

/**
 * 一个工具类
 *
 * @author : JF
 * Date    : 2019.10.11
 * Time    : 23:50
 * Email   ：tangqibao_620@163.com
 */

public class LeetcodeUtils {

    public static ListNode createNode(int[] nums){
        ListNode head = new ListNode(0);
        ListNode last = head;
        for(int num : nums){
            last = last.next = new ListNode(num);
        }
        return head.next;
    }

    public static void traverse(ListNode head){
        ListNode node = head;
        while(node != null){
            System.out.print(node.val + " ");
            node = node.next;
        }
        System.out.println();
    }
}
