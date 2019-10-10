package com.study.datastructure;

import java.util.*;


/**
 * 红黑树类
 *
 * @param <E> 关键字类型
 * @author 江峰
 * @create 2018-1-8
 */
public class RedBlackTree<E extends Comparable<E>> {
    private static final boolean BLACK = true;
    private static final boolean RED = false;
    private Node<E> root;
    private int size;

    public boolean add(E e) {
        Node<E> node = root;
        if (node == null) {
            root = new Node<E>(e, null);
            root.color = BLACK;
            ++size;
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
        Node<E> newNode = new Node<E>(e, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        fixAfterInsertion(newNode);
        ++size;
        return true;
    }

    private void fixAfterInsertion(Node<E> node) {
        while (colorOf(node) == RED && colorOf(node.parent) == RED) {
            Node<E> parent = node.parent;
            Node<E> grandParent = parent.parent;
            if (colorOf(grandParent.left) == colorOf(grandParent.right)) {
                setColor(grandParent.left, BLACK);
                setColor(grandParent.right, BLACK);
                setColor(grandParent, RED);
                node = grandParent;
                continue;
            }
            setColor(grandParent, RED);
            if (parent == grandParent.left) {
                if (node == parent.left) {
                    setColor(parent, BLACK);
                    rotateRight(grandParent);
                } else {
                    setColor(node, BLACK);
                    rotateLeftRight(grandParent);
                }
            } else {
                if (node == parent.right) {
                    setColor(parent, BLACK);
                    rotateLeft(grandParent);
                } else {
                    setColor(node, BLACK);
                    rotateRightLeft(grandParent);
                }
            }
            break;
        }
        setColor(root, BLACK);
    }

    private Node<E> rightOf(Node<E> node) {
        return node == null ? null : node.right;
    }

    private Node<E> leftOf(Node<E> node) {
        return node == null ? null : node.left;
    }

    private boolean colorOf(Node<E> node) {
        return node == null ? BLACK : node.color;
    }

    @SuppressWarnings("unused")
    private Node<E> parentOf(Node<E> node) {
        return node == null ? null : node.parent;
    }

    private void setColor(Node<E> node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }

    private void rotateRight(Node<E> node) {
        assert node.left != null;
        Node<E> left = node.left;
        node.left = left.right;
        if (left.right != null) {
            left.right.parent = node;
        }
        Node<E> parent = node.parent;
        left.parent = parent;
        left.right = node;
        node.parent = left;
        if (parent == null) {
            root = left;
        } else if (parent.left == node) {
            parent.left = left;
        } else {
            parent.right = left;
        }
    }

    private void rotateLeft(Node<E> node) {
        assert node.right != null;
        Node<E> right = node.right;
        node.right = right.left;
        if (right.left != null) {
            right.left.parent = node;
        }
        Node<E> parent = node.parent;
        right.left = node;
        node.parent = right;
        right.parent = parent;
        if (parent == null) {
            root = right;
        } else if (node == parent.left) {
            parent.left = right;
        } else {
            parent.right = right;
        }
    }

    private void rotateLeftRight(Node<E> node) {
        rotateLeft(node.left);
        rotateRight(node);
    }

    private void rotateRightLeft(Node<E> node) {
        rotateRight(node.right);
        rotateLeft(node);
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

    public boolean isValid() {
        Node<E> node = root;
        if (node == null) {
            return true;
        } else if (node.color == RED) {
            return false;
        }
        int blackNum = 0;
        while (node != null) {
            if (node.color == BLACK) {
                ++blackNum;
            }
            node = node.left;
        }
        ArrayDeque<Node<E>> nodeStack = new ArrayDeque<Node<E>>();
        nodeStack.push(root);
        Integer parentBlackNum = null;
        HashMap<Node<E>, Integer> nodeMap = new HashMap<Node<E>, Integer>();
        Node<E> parent = null;
        while (!nodeStack.isEmpty()) {
            node = nodeStack.pop();
            parent = node.parent;
            if (colorOf(node) == RED && colorOf(parent) == RED) {
                return false;
            }
            parentBlackNum = nodeMap.get(parent);
            if (node.left == null || node.right == null) {
                int blackCount = node.color == BLACK ? 1 : 0;
                if (parentBlackNum != null) {
                    blackCount += parentBlackNum.intValue();
                }
                if (blackCount != blackNum) {
                    return false;
                }
                if (node.left == node.right) {
                    continue;
                }
            }
            if (parentBlackNum == null) {
                nodeMap.put(node, 1);
            } else {
                nodeMap.put(node, parentBlackNum + (node.color == BLACK ? 1 : 0));
            }
            if (node.right != null) {
                nodeStack.push(node.right);
            }
            if (node.left != null) {
                nodeStack.push(node.left);
            }
        }
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
        Node<E> moved = removed.left != null ? removed.left : removed.right;
        Node<E> parent = removed.parent;
        if (moved != null) {
            moved.parent = parent;
        }
        if (parent == null) {
            root = moved;
        } else if (removed == parent.left) {
            parent.left = moved;
        } else {
            parent.right = moved;
        }
        if (replaced != removed) {
            replaced.value = removed.value;
        }
        if (removed.color == BLACK) {
            fixAfterDeletion(moved, parent);
        }
        return true;
    }

    private void fixAfterDeletion(Node<E> node, Node<E> parent) {
        while (colorOf(node) == BLACK && parent != null) {
            Node<E> sibling = null;
            if (node == parent.left) {
                sibling = parent.right;
                if (colorOf(sibling) == RED) {
                    setColor(sibling, BLACK);
                    setColor(parent, RED);
                    rotateLeft(parent);
                    sibling = parent.right;
                }
                if (colorOf(leftOf(sibling)) == BLACK &&
                        colorOf(rightOf(sibling)) == BLACK) {
                    setColor(sibling, RED);
                    node = parent;
                    parent = node.parent;
                } else {
                    if (colorOf(sibling.right) == BLACK) {
                        setColor(sibling.left, parent.color);
                        rotateRightLeft(parent);
                    } else {
                        setColor(sibling.right, BLACK);
                        setColor(sibling, parent.color);
                        rotateLeft(parent);
                    }
                    setColor(parent, BLACK);
                    return;
                }
            } else {
                sibling = parent.left;
                if (colorOf(sibling) == RED) {
                    setColor(sibling, BLACK);
                    setColor(parent, RED);
                    rotateRight(parent);
                    sibling = parent.left;
                }
                if (colorOf(leftOf(sibling)) == BLACK &&
                        colorOf(rightOf(sibling)) == BLACK) {
                    setColor(sibling, RED);
                    node = parent;
                    parent = node.parent;
                } else {
                    if (colorOf(sibling.left) == BLACK) {
                        setColor(sibling.right, parent.color);
                        rotateLeftRight(parent);
                    } else {
                        setColor(sibling.left, BLACK);
                        setColor(sibling, parent.color);
                        rotateRight(parent);
                    }
                    setColor(parent, BLACK);
                    return;
                }
            }
        }
        setColor(node, BLACK);
    }

    @SuppressWarnings("unused")
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
                parent = node.parent;
            }
            return parent;
        }
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
                parent = node.parent;
            }
            return parent;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node<E> getNode(Object e) {
        if (e == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Comparable<? super E> comparator = (Comparable<? super E>) e;
        Node<E> node = root;
        int cmp = 0;
        do {
            cmp = comparator.compareTo(node.value);
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                return node;
            }
        } while (node != null);
        return null;
    }

    public boolean contains(Object e) {
        return getNode(e) != null;
    }

    public void midOrder() {
        midOrder(root);
        System.out.println();
    }

    private void midOrder(Node<E> node) {
        if (node != null) {
            midOrder(node.left);
            System.out.print(node + ",");
            midOrder(node.right);
        }
    }

    @SuppressWarnings("hiding")
    class Node<E extends Comparable<E>> {
        public Node<E> parent;
        public Node<E> left;
        public Node<E> right;
        public E value;
        public boolean color;

        public Node(E value, Node<E> parent) {
            this.value = value;
            this.parent = parent;
        }

        public String toString() {
            return value.toString() + "(" + color + ")";
        }
    }
}