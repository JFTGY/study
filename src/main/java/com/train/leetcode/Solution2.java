package com.train.leetcode;

/**
 * 问题：给出两个<非空>的链表用来表示两个非负的整数。其中，它们各自的位数是按照<逆序>的方式存储的，并且它们的每个节点只能存储<一位>数字。
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 *
 * 链接：https://www.leetcode.com/problems/add-two-numbers/
 *
 * @author : JF
 * Date    : 2019.10.11
 * Time    : 23:32
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("WeakerAccess")
public class Solution2 {
    /**
     * 已存在的节点作为求和结果，效率最高，但实用性较差。
     */
    public ListNode addTwoNumbers1(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0), node = dummy;
        int carry = 0;
        while (l1 != null || l2 != null) {
            if (l1 != null && l2 != null) {
                node = node.next = l1;
                node.val += l2.val;
            } else {
                node = node.next = l1 == null ? l2 : l1;
            }
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
            if ((node.val += carry) > 9) {
                carry = 1;
                node.val -= 10;
            } else {
                carry = 0;
            }
        }
        if (carry == 1) {
            node.next = new ListNode(1);
        }
        return dummy.next;
    }

    /**
     * 创建新的节点作为求和结果，效率些许损失，但实用性较强。
     */
    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0), node = dummy;
        int carry = 0;
        while (l1 != null || l2 != null || carry == 1) {
            if (l1 != null) {
                carry += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                carry += l2.val;
                l2 = l2.next;
            }
            node = node.next = new ListNode(carry > 9 ? carry - 10 : carry);
            carry = carry > 9 ? 1 : 0;
        }
        return dummy.next;
    }
}
