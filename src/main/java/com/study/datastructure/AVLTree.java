package com.study.datastructure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * AVL树类
 *
 * @param <E> 关键字类型
 * @author 江峰
 * Date: 2018-1-8
 */
public class AVLTree<E extends Comparable<E>> {
    private Node<E> root;
    private int size;

    public boolean add(E e) {
        if (e == null) {
            return false;
        }
        Node<E> node = root;
        if (node == null) {
            root = new Node<>(e, null);
            size++;
            return true;
        }
        Node<E> parent = null;
        int cmp = 0;
        do {
            parent = node;
            cmp = e.compareTo(node.value);
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                return false;
            }
        } while (node != null);
        Node<E> newNode = new Node<>(e, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        fixAfterInsertion(parent);
        size++;
        return true;
    }

    private void fixAfterInsertion(Node<E> parent) {
        do {
            int leftHeight = height(parent.left);
            int rightHeight = height(parent.right);
            if (Math.abs(leftHeight - rightHeight) == 2) {
                if (leftHeight > rightHeight) {  // Add to the left subtree
                    Node<E> left = parent.left;
                    if (height(left.left) > height(left.right)) {
                        rotateRight(parent);
                    } else {
                        rotateLeftRight(parent);
                    }
                } else {
                    Node<E> right = parent.right;
                    if (height(right.right) > height(right.left)) {
                        rotateLeft(parent);
                    } else {
                        rotateRightLeft(parent);
                    }
                }
                break;
            }
            int newHeight = Math.max(leftHeight, rightHeight) + 1;
            if (parent.height == newHeight) {
                break;
            } else {
                parent.height = newHeight;
            }
            parent = parent.parent;
        } while (parent != null);
    }

    private void rotateLeftRight(Node<E> node) {
        rotateLeft(node.left);
        rotateRight(node);
    }

    private void rotateRightLeft(Node<E> node) {
        rotateRight(node.right);
        rotateLeft(node);
    }

    private void rotateLeft(Node<E> node) {
        assert node.right != null;
        Node<E> right = node.right;
        node.right = right.left;
        if (right.left != null) {
            right.left.parent = node;
        }
        Node<E> parent = node.parent;
        node.parent = right;
        right.left = node;
        right.parent = parent;
        if (parent == null) {
            root = right;
        } else if (parent.left == node) {
            parent.left = right;
        } else {
            parent.right = right;
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        right.height = Math.max(height(right.left), height(right.right)) + 1;
    }

    private void rotateRight(Node<E> node) {
        assert node.left != null;
        Node<E> left = node.left;
        node.left = left.right;
        if (left.right != null) {
            left.right.parent = node;
        }
        Node<E> parent = node.parent;
        left.right = node;
        node.parent = left;
        left.parent = parent;
        if (parent == null) {
            root = left;
        } else if (parent.left == node) {
            parent.left = left;
        } else {
            parent.right = left;
        }
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        left.height = Math.max(height(left.left), height(left.right)) + 1;
    }

    public boolean isValid() {
        Node<E> node = root;
        if (node == null) {
            return true;
        }
        ArrayDeque<Node<E>> nodeDeque = new ArrayDeque<Node<E>>();
        nodeDeque.addFirst(node);
        while (!nodeDeque.isEmpty()) {
            node = nodeDeque.removeLast();
            if (Math.abs(height(node.left) - height(node.right)) >= 2) {
                return false;
            }
            if (node.left != null) {
                nodeDeque.addFirst(node.left);
            }
            if (node.right != null) {
                nodeDeque.addFirst(node.right);
            }
        }
        return true;
    }

    private int height(Node<E> node) {
        return node == null ? 0 : node.height;
    }

    public boolean remove(Object e) {
        Node<E> replaced = getNode(e);
        if (replaced == null) {
            return false;
        } else {
            --size;
        }
        Node<E> removed = replaced;
        if (replaced.left != null && replaced.right != null) {
            if (height(replaced.left) > height(replaced.right)) {
                removed = predecessor(replaced);
            } else {
                removed = successor(replaced);
            }
        }
        Node<E> moved = null;
        if (removed.left != null) {
            moved = removed.left;
        } else {
            moved = removed.right;
        }
        Node<E> parent = removed.parent;
        if (moved != null) {
            moved.parent = parent;
        }
        if (parent == null) {
            root = moved;
            return true;
        } else if (removed == parent.left) {
            parent.left = moved;
        } else {
            parent.right = moved;
        }
        if (replaced != removed) {
            replaced.value = removed.value;
        }
        fixAfterDeletion(parent);
        return true;
    }

    private void fixAfterDeletion(Node<E> parent) {
        do {
            int leftHeight = height(parent.left);
            int rightHeight = height(parent.right);
            if (Math.abs(leftHeight - rightHeight) == 2) {
                if (leftHeight > rightHeight) { // 右子树被删除
                    Node<E> left = parent.left;
                    if (height(left.right) > height(left.left)) { // 左右旋
                        rotateLeftRight(parent);
                    } else {
                        rotateRight(parent);
                    }
                } else { // 左子树被删除
                    Node<E> right = parent.right;
                    if (height(right.left) > height(right.right)) { // 右左旋
                        rotateRightLeft(parent);
                    } else { // 右旋
                        rotateLeft(parent);
                    }
                }
                parent = parent.parent.parent; // 获取祖父节点
                continue;
            }
            int nextHeight = Math.max(leftHeight, rightHeight) + 1;
            if (parent.height == nextHeight) {
                break;
            } else {
                parent.height = nextHeight;
            }
            parent = parent.parent;
        } while (parent != null);
    }

    private Node<E> successor(Node<E> node) {
        if (node == null) {
            return null;
        } else if (node.right != null) {
            Node<E> right = node.right;
            while (right.left != null) {
                right = right.left;
            }
            return right;
        } else {
            Node<E> parent = node.parent;
            while (parent != null && node == parent.right) {
                node = parent;
                parent = parent.parent;
            }
            return parent;
        }
    }

    private Node<E> predecessor(Node<E> node) {
        if (node == null) {
            return null;
        } else if (node.left != null) {
            Node<E> left = node.left;
            while (left.right != null) {
                left = left.right;
            }
            return left;
        } else {
            Node<E> parent = node.parent;
            while (parent != null && node == parent.left) {
                node = parent;
                parent = parent.parent;
            }
            return parent;
        }
    }

    private Node<E> getNode(Object e) {
        if (e == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Comparable<? super E> value = (Comparable<? super E>) e;
        Node<E> node = root;
        while (node != null) {
            int cmp = value.compareTo(node.value);
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                return node;
            }
        }
        return null;
    }

    @SuppressWarnings("hiding")
    class Node<E extends Comparable<E>> {
        public int height;
        public Node<E> parent;
        public Node<E> left;
        public Node<E> right;
        public E value;

        public Node(E value, Node<E> parent) {
            this.value = value;
            this.parent = parent;
            this.height = 1;
        }

        @Override
        public String toString() {
            return value.toString() + "(" + height + ")";
        }
    }

    public void midOrder() {
        Node<E> node = root;
        if (node == null) {
            System.out.println("中序遍历：[]");
            return;
        }
        StringBuilder info = new StringBuilder();
        info.append("中序遍历：[");
        ArrayDeque<Node<E>> nodeStack = new ArrayDeque<Node<E>>();
        while (node != null || !nodeStack.isEmpty()) {
            if (node != null) {
                nodeStack.push(node);
                node = node.left;
            } else {
                node = nodeStack.pop();
                info.append(node.value.toString()).append(",");
                node = node.right;
            }
        }
        info.setLength(info.length() - 1);
        info.append("]");
        System.out.println(info.toString());
    }

    public void breadthFirstSearch() {
        Node<E> node = root;
        if (node == null) {
            System.out.println("广度优先搜索：[]");
            return;
        }
        StringBuilder info = new StringBuilder();
        info.append("广度优先搜索：[");
        ArrayDeque<Node<E>> nodeDeque = new ArrayDeque<Node<E>>();
        nodeDeque.addFirst(node);
        while (!nodeDeque.isEmpty()) {
            node = nodeDeque.removeLast();
            info.append(node).append(",");
            if (node.left != null) {
                nodeDeque.addFirst(node.left);
            }
            if (node.right != null) {
                nodeDeque.addFirst(node.right);
            }
        }
        info.setLength(info.length() - 1);
        info.append("]");
        System.out.println(info.toString());
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object e) {
        return getNode(e) == null;
    }
}