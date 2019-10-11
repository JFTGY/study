package com.train.leetcode;

/**
 * 一个链表类。
 * @author : JF
 * Date    : 2019.10.11
 * Time    : 23:29
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("WeakerAccess")
public class ListNode {
    public int val;
    public ListNode next;
    public ListNode(int x) { val = x; }
    @Override
    public String toString() {
        return String.valueOf(val);
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof ListNode){
            ListNode l1 = (ListNode)obj;
            ListNode l2 = this;
            while(l1 != null && l2 != null){
                if(l1.val != l2.val){
                    return false;
                }
                l1 = l1.next;
                l2 = l2.next;
            }
            return l1 == null && l2 == null;
        }
        return false;
    }
}
