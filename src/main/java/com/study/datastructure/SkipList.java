package com.study.datastructure;


import java.util.Arrays;
import java.util.Random;

public class SkipList<K extends Comparable<K>, V> {
    private int size;
    private int high;
    private Node<K, V>[] headers;
    private Random rand;


    @SuppressWarnings("unchecked")
    public SkipList() {
        rand = new Random();
        headers = (Node<K, V>[]) new Node[10];
    }

    public boolean put(K key, V value) {
        if (key == null) {
            return false;
        }
        Node<K, V>[] hs = headers;
        if (hs[0] == null) {
            Node<K, V> newNode = new Node<K, V>(key, value, null, null, null, null);
            insertNode(hs[0] = newNode, null, null);
            ++size;
            return true;
        }
        int index = high, cmp = 0;
        Node<K, V> header = hs[index], node = header;
        while (index > 0) {
            cmp = key.compareTo(node.key);
            if (cmp > 0) {
                while (--index >= 0) {
                    if ((node = node.down).right != null) {
                        node = node.right;
                        break;
                    }
                }
            } else if (cmp < 0) {
                while (--index >= 0) {
                    if ((node = node.down).left != null) {
                        node = node.left;
                        break;
                    }
                }
            } else {
                fixRepeat(node, value);
                return false;
            }
        }
        cmp = key.compareTo(node.key);
        Node<K, V> left = null, right = null;
        if (cmp > 0) {
            while (node != null && key.compareTo(node.key) > 0) {
                left = node;
                node = node.right;
            }
            if (node == null) {
                right = null;
            } else if (key.compareTo(node.key) < 0) {
                right = node;
            } else {
                node.value = value;
                return false;
            }
        } else if (cmp < 0) {
            while (node != null && key.compareTo(node.key) < 0) {
                right = node;
                node = node.left;
            }
            if (node == null) {
                left = null;
            } else if (key.compareTo(node.key) > 0) {
                left = node;
            } else {
                node.value = value;
                return false;
            }
        } else {
            fixRepeat(node, value);
            return false;
        }
        Node<K, V> newNode = new Node<K, V>(key, value, left, right, null, null);
        insertNode(newNode, left, right);
        ++size;
        return true;
    }

    private void fixRepeat(Node<K, V> node, V value) {
        while (node != null) {
            node.value = value;
            node = node.down;
        }
    }

    private void insertNode(Node<K, V> newNode, Node<K, V> left, Node<K, V> right) {
        linkNode(newNode, left, right);
        Random random = rand;
        Node<K, V> nextNode = newNode;
        Node<K, V>[] hs = headers;
        int index = 0, capacity = hs.length;
        while (random.nextDouble() > 0.5) {
            ++index;
            Node<K, V> nextLeftNode = null, nextRightNode = null;
            left = right = nextNode;
            while ((left = left.left) != null) {
                if (left.up != null) {
                    nextLeftNode = left.up;
                    break;
                }
            }
            while ((right = right.right) != null) {
                if (right.up != null) {
                    nextRightNode = right.up;
                    break;
                }
            }
            nextNode = new Node<K, V>(nextNode.key, nextNode.value,
                    nextLeftNode, nextRightNode, null, nextNode);
            nextNode.down.up = nextNode;
            linkNode(nextNode, nextLeftNode, nextRightNode);
            if (nextLeftNode == null) {
                if (index == capacity) {
                    grow(capacity + 1);
                }
                hs[index] = nextNode;
            }
        }
        if (index > high) {
            high = index;
        }
    }

    private void grow(int minCapacity) {
        Node<K, V>[] hs = headers;
        int oldCapacity = hs.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }
        if (newCapacity < 0) {
            throw new OutOfMemoryError();
        }
        headers = Arrays.copyOf(hs, newCapacity);
    }

    public int size() {
        return size;
    }

    public void print() {
        Node<K, V>[] hs = headers;
        if (hs == null) {
            return;
        }
        Node<K, V> header = hs[0];
        if (header == null) {
            return;
        }
        Node<K, V> node = header;
        StringBuilder original = new StringBuilder();
        while (true) {
            original.append(node.key.toString());
            if ((node = node.right) != null) {
                original.append(" ");
            } else {
                break;
            }
        }
        original.trimToSize();
        int len = original.length();
        int h = high;
        Node<K, V> nextNode = null;
        for (int i = h; i > 0; --i) {
            StringBuilder sb = new StringBuilder(len);
            node = header;
            nextNode = hs[i];
            while (true) {
                if (node.key.compareTo(nextNode.key) == 0) {
                    sb.append(node.key);
                    if ((nextNode = nextNode.right) == null) {
                        sb.append(multipleWhitespaces(len - sb.length()));
                        break;
                    }
                } else {
                    sb.append(multipleWhitespaces(node.key.toString().length()));
                }
                if ((node = node.right) != null) {
                    sb.append(" ");
                } else {
                    break;
                }
            }
            System.out.println(sb.toString());
        }
        System.out.println(original.toString());
    }

    private void linkNode(Node<K, V> node, Node<K, V> left, Node<K, V> right) {
        if (left != null) {
            left.right = node;
        }
        if (right != null) {
            right.left = node;
        }
    }

    private String multipleWhitespaces(int num) {
        StringBuilder ws = new StringBuilder(num);
        while (--num >= 0) {
            ws.append(" ");
        }
        return ws.toString();
    }

    public V get(Object obj) {
        Node<K, V> node = getNode(obj);
        return node == null ? null : node.value;
    }

    private Node<K, V> getNode(Object obj) {
        if (obj == null) {
            return null;
        }
        Node<K, V>[] hs = headers;
        if (hs[0] == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Comparable<? super K> key = (Comparable<? super K>) obj;
        int index = high, cmp = 0;
        Node<K, V> header = hs[index];
        Node<K, V> node = header;
        while (index > 0) {
            cmp = key.compareTo(node.key);
            if (cmp > 0) {
                while (--index >= 0) {
                    if ((node = node.down).right != null) {
                        node = node.right;
                        break;
                    }
                }
            } else if (cmp < 0) {
                while (--index >= 0) {
                    if ((node = node.down).left != null) {
                        node = node.left;
                        break;
                    }
                }
            } else {
                return node;
            }
        }
        cmp = key.compareTo(node.key);
        if (cmp > 0) {
            while ((node = node.right) != null) {
                if (key.compareTo(node.key) == 0) {
                    return node;
                } else if (key.compareTo(node.key) < 0) {
                    return null;
                }
            }
        } else if (cmp < 0) {
            while ((node = node.left) != null) {
                if (key.compareTo(node.key) == 0) {
                    return node;
                } else if (key.compareTo(node.key) > 0) {
                    return null;
                }
            }
        } else {
            return node;
        }
        return null;
    }

    public boolean remove(Object obj) {
        Node<K, V> node = getNode(obj);
        if (node == null) {
            return false;
        }
        while (node.down != null) {
            node = node.down;
        }
        int index = 0;
        Node<K, V>[] hs = headers;
        Node<K, V> left = null, right = null;
        while (true) {
            left = node.left;
            right = node.right;
            if (left == null) {
                hs[index] = right;
            } else {
                left.right = right;
            }
            if (right != null) {
                right.left = left;
            }
            if ((node = node.up) == null) {
                break;
            }
            node.down = null;
            ++index;
        }
        --size;
        return true;
    }

    @SuppressWarnings("hiding")
    class Node<K extends Comparable<K>, V> {
        Node<K, V> left, right, up, down;
        K key;
        V value;

        public Node(K key, V value, Node<K, V> left, Node<K, V> right,
                    Node<K, V> up, Node<K, V> down) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.up = up;
            this.down = down;
        }

        @Override
        public String toString() {
            return key + " = " + value;
        }
    }

    public static void main(String[] args) {
        SkipList<Integer, Integer> list = new SkipList<>();
        for (int i = 1; i < 500; ++i) {
            list.put(i, i);
        }
        list.print();
        int del = new Random().nextInt(20);
        System.out.println(del);
        list.remove(del);
        list.print();
    }
}

