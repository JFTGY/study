package com.study.datastructure;

import java.util.ArrayDeque;


/**
 * 二叉搜索树类
 *
 * @param <E> 关键字类型
 * @author 江峰
 * @create 2018-1-8
 */
public class BinarySearchTree<E extends Comparable<E>> {
    private Node<E> root;
    private int size;

    public boolean add(E e) {
        if (e == null) {
            throw new IllegalArgumentException("The value of e connot be null.");
        }
        Node<E> node = root;
        if (node == null) {
            root = new Node<E>(e, null);
            size++;
            return true;
        }
        Node<E> parent = null;
        int cmp = 0;
        do {
            cmp = e.compareTo(node.value);
            parent = node;
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                return false;
            }
        } while (node != null);
        Node<E> newNode = new Node<E>(e, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;
        return true;
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
            removed = successor(replaced);
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
        } else if (parent.left == removed) {
            parent.left = moved;
        } else {
            parent.right = moved;
        }
        if (replaced != removed) {
            replaced.value = removed.value;
        }
        return true;
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
            if (node.left != null) {
                if (node.value.compareTo(node.left.value) <= 0) {
                    return false;
                }
                nodeDeque.addFirst(node.left);
            }
            if (node.right != null) {
                if (node.value.compareTo(node.right.value) >= 0) {
                    return false;
                }
                nodeDeque.addFirst(node.right);
            }
        }
        return true;
    }

    public void preOrder() {
        Node<E> node = root;
        if (node == null) {
            System.out.println("前序遍历：[]");
            return;
        }
        StringBuilder print = new StringBuilder();
        print.append("前序遍历：[");
        ArrayDeque<Node<E>> nodeStack = new ArrayDeque<Node<E>>();
        while (node != null || !nodeStack.isEmpty()) {
            if (node != null) {
                print.append(node.value.toString()).append(",");
                nodeStack.push(node);
                node = node.left;
            } else {
                node = nodeStack.pop().right;
            }
        }
        print.setLength(print.length() - 1);
        print.append("]");
        System.out.println(print.toString());
    }

    public void midOrder() {
        Node<E> node = root;
        if (node == null) {
            System.out.println("中序遍历：[]");
            return;
        }
        StringBuilder print = new StringBuilder();
        print.append("中序遍历：[");
        ArrayDeque<Node<E>> nodeStack = new ArrayDeque<Node<E>>();
        while (node != null || !nodeStack.isEmpty()) {
            if (node != null) {
                nodeStack.push(node);
                node = node.left;
            } else {
                node = nodeStack.pop();
                print.append(node.toString()).append(",");
                node = node.right;
            }
        }
        print.setLength(print.length() - 1);
        print.append("]");
        System.out.println(print.toString());
    }

    public void postOrder() {
        Node<E> node = root;
        if (node == null) {
            System.out.println("后序遍历：[]");
            return;
        }
        ArrayDeque<Node<E>> inStack = new ArrayDeque<Node<E>>();
        ArrayDeque<Node<E>> outStack = new ArrayDeque<Node<E>>();
        inStack.push(node);
        while (!inStack.isEmpty()) {
            outStack.push(node = inStack.pop());
            if (node.left != null) {
                inStack.push(node.left);
            }
            if (node.right != null) {
                inStack.push(node.right);
            }
        }
        StringBuilder print = new StringBuilder();
        print.append("后序遍历：[");
        while (outStack.size() > 1) {
            print.append(outStack.pop().toString()).append(",");
        }
        print.append(outStack.pop().toString()).append("]");
        System.out.println(print.toString());
    }

    public void breadthFirstSearch() {
        Node<E> node = root;
        if (node == null) {
            System.out.println("广度优先搜索：[]");
            return;
        }
        StringBuilder print = new StringBuilder();
        print.append("广度优先搜索：[");
        ArrayDeque<Node<E>> nodeStack = new ArrayDeque<Node<E>>();
        nodeStack.push(node);
        while (!nodeStack.isEmpty()) {
            node = nodeStack.pop();
            print.append(node.value.toString()).append(",");
            if (node.right != null) {
                nodeStack.push(node.right);
            }
            if (node.left != null) {
                nodeStack.push(node.left);
            }
        }
        print.setLength(print.length() - 1);
        print.append("]");
        System.out.println(print.toString());
    }

    public void depthFirstSearch() {
        Node<E> node = root;
        if (node == null) {
            System.out.println("深度优先搜索：[]");
            return;
        }
        StringBuilder print = new StringBuilder();
        print.append("深度优先搜索：[");
        ArrayDeque<Node<E>> nodeDeque = new ArrayDeque<Node<E>>();
        nodeDeque.addFirst(node);
        while (!nodeDeque.isEmpty()) {
            node = nodeDeque.removeLast();
            print.append(node.value.toString()).append(",");
            if (node.left != null) {
                nodeDeque.addFirst(node.left);
            }
            if (node.right != null) {
                nodeDeque.addFirst(node.right);
            }
        }
        print.setLength(print.length() - 1);
        print.append("]");
    }

    Node<E> getNode(Object value) {
        if (value == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Comparable<? super E> compare = (Comparable<? super E>) value;
        Node<E> node = root;
        while (node != null) {
            int cmp = compare.compareTo(node.value);
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

    Node<E> successor(Node<E> node) {
        if (node == null) {
            return null;
        } else if (node.right != null) {
            Node<E> right = node.right;
            while (right.left != null) {
                right = right.left;
            }
            return right;
        } else {
            Node<E> p = node.parent;
            while (p != null && node == p.right) {
                node = p;
                p = p.parent;
            }
            return p;
        }
    }

    Node<E> predecessor(Node<E> node) {
        if (node == null) {
            return null;
        } else if (node.left != null) {
            Node<E> left = node.left;
            while (left.right != null) {
                left = left.right;
            }
            return left;
        } else {
            Node<E> p = node.parent;
            while (p != null && node == p.left) {
                node = p;
                p = p.parent;
            }
            return node;
        }
    }

    public boolean contains(Object e) {
        return getNode(e) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    static class Node<E extends Comparable<E>> {
        public E value;
        public Node<E> left;
        public Node<E> right;
        public Node<E> parent;

        public Node(E value, Node<E> parent) {
            this.value = value;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return value.toString();
        }

    }
}