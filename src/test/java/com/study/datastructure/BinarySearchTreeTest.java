package com.study.datastructure;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author : JF
 * Date    : 2019.10.10
 * Time    : 20:02
 * Email   ：tangqibao_620@163.com
 */
public class BinarySearchTreeTest {
    @Test
    public void simpleTest() {
        int seed = 10;
        while (seed-- > 0) {
            int len = new Random().nextInt(100) + 1;
            List<Integer> list = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                list.add(i + 1);
            }
            java.util.Collections.shuffle(list);
            BinarySearchTree<Integer> tree = new BinarySearchTree<>();
            System.out.println(Arrays.toString(list.toArray()));
            for (Integer i : list) {
                tree.add(i);
                if (!tree.isValid()) {
                    System.out.println("插入出现错误");
                    return;
                }
            }
            System.out.print("   ------>  ");
            tree.midOrder();
            java.util.Collections.shuffle(list);
            for (Integer c : list) {
                System.out.print(c + "  ------>  ");
                tree.remove(c);
                tree.midOrder();
                if (!tree.isValid()) {
                    System.out.println("删除出现错误");
                    return;
                }
            }
            System.out.println();
        }
    }

    @Test
    public void pressureTest() {
        int seed = 10;
        while (seed-- > 0) {
            int len = 10000;
            List<Integer> list = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                list.add(i + 1);
            }
            java.util.Collections.shuffle(list);
            BinarySearchTree<Integer> tree = new BinarySearchTree<>();
            long begin1 = System.currentTimeMillis();
            for (Integer i : list) {
                tree.add(i);
				if(!tree.isValid()){
					System.out.println("插入出现错误");
					return;
				}
            }
            long end1 = System.currentTimeMillis();
            java.util.Collections.shuffle(list);
            long begin2 = System.currentTimeMillis();
            for (Integer c : list) {
                tree.remove(c);
				if(!tree.isValid()){
					System.out.println("删除出现错误");
					return;
				}
            }
            long end2 = System.currentTimeMillis();
            if (!tree.isEmpty()) {
                System.out.println("出现错误");
            } else {
                System.out.println(list.size() + " : " + (end1 - begin1) + " : " + (end2 - begin2));
            }
        }
    }
}