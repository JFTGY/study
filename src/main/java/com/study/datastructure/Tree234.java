package com.study.datastructure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 2、3、4树类
 *
 * @param <T> 关键字的数据类型
 * @author 江峰
 * @create 2018-1-3
 */
public class Tree234<T extends Comparable<T>> {
    private Node<T> rootNode;
    private int size;

    public Tree234() {
        this.rootNode = new Node<T>();
    }

    /**
     * 删除指定关键字所对应的数据项
     *
     * @param key 指定关键字对象
     * @return 指定关键字对应数据项的值
     */
    public Object remove(T key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Node<T> removeNode = findNode(key);
        if (removeNode == null) {
            return null;
        }
        int itemIndex = removeNode.findItem(key);  // 需要删除的数据项处于该节点的索引值
        Object oldValue = removeNode.getItem(itemIndex).value; // 需要删除的数据项所对应的值，作为返回值使用
        // 如果需要删除的数据项所处的节点不是叶子节点，则先获取该节点的后继节点，
        // 得到后继节点上的相应的数据项，再删除后继节点上的数据项，最后用该数据项替换需要删除的数据项
        // 这能保证需要删除的数据项所处的节点都是叶节点，后续删除时，可以忽略这方面的处理。
        Node<T> replaceNode = removeNode;
        T oldKey = key;
        T newKey = key;
        Object newValue = null;
        if (!removeNode.isLeaf()) {
            replaceNode = getSuccessorNode(removeNode, itemIndex);
            DataItem<T> replaceItem = replaceNode.getItem(0);
            newKey = replaceItem.key;
            newValue = replaceItem.value;
        }
        // 如果需要删除的数据项所处的叶节点其数据项为1，节点处在在双叶子节点中，且兄弟节点的数据项为1，则使用合并式删除，其余情况都使用简单删除
        if (removeMode(replaceNode)) {
            simpleRemove(replaceNode, newKey);
        } else {
            mergeRemove(rootNode, newKey);
        }
        // 如果删除了后继节点上的数据项，则用该数据项替换需要删除的数据项
        if (oldKey.compareTo(newKey) != 0) {
            replace(oldKey, newKey, newValue);
        }
        size--;
        return oldValue;
    }

    /**
     * 需要删除的数据项与后继节点的数据项进行替换
     *
     * @param oldKey   需要删除的数据项所对应的关键字对象
     * @param newKey   后继节点的数据项所对应的关键字对象
     * @param newValue 后继节点的数据项所对应的值
     */
    private void replace(T oldKey, T newKey, Object newValue) {
        Node<T> node = findNode(oldKey);
        DataItem<T> item = node.getItem(node.findItem(oldKey));
        item.key = newKey;
        item.value = newValue;
    }

    /**
     * 判断指定是否为单节点所对应的叶节点，单节点即父节点为一个数据项，它的两个孩子节点的数据项都为1，且都是叶节点。
     *
     * @param node 需要判断的节点对象
     * @return 返回true 该节点为单节点所对应的叶节点
     * 返回false 该节点不是单节点所对应的叶节点
     */
    private boolean isSingleTwoLeafNode(Node<T> node) {
        Node<T> parentNode = node.getParent();
        if (parentNode == rootNode && parentNode.getItemNums() == 1 &&
                parentNode.getChild(0).getItemNums() == 1 && parentNode.getChild(1).getItemNums() == 1) {
            return true;
        }
        return false;
    }

    /**
     * 通过融合相应数据项，达到节点合并的目的。
     *
     * @param parentNode      父节点对象
     * @param parentItemIndex 父节点中需要删除的数据项，其对应的索引值
     * @param childIndex      当前节点为父节点的孩子节点时，其所处的索引值
     * @param brotherNode     当前节点的的兄弟节点
     * @param curNode         当前节点
     * @return 节点合并后的新节点对象
     */
    private Node<T> conbineNodeByMerge(Node<T> parentNode, int parentItemIndex, int childIndex, Node<T> brotherNode, Node<T> curNode) {
        parentNode.removeChild(childIndex);
        DataItem<T> parentItem = parentNode.removeItem(parentItemIndex);
        Node<T> newNode = new Node<T>();
        newNode.insertItem(brotherNode.getItem(0));
        newNode.insertItem(parentItem);
        newNode.insertItem(curNode.getItem(0));
        if (curNode.getChildIndex() == parentNode.getItemNums() + 1) {
            newNode.connectChild(0, brotherNode.getChild(0));
            newNode.connectChild(1, brotherNode.getChild(1));
            newNode.connectChild(2, curNode.getChild(0));
            newNode.connectChild(3, curNode.getChild(1));
        } else {
            newNode.connectChild(0, curNode.getChild(0));
            newNode.connectChild(1, curNode.getChild(1));
            newNode.connectChild(2, brotherNode.getChild(0));
            newNode.connectChild(3, brotherNode.getChild(1));
        }
        if (parentNode.getItemNums() == 0) {
            rootNode = newNode;
        } else {
            parentNode.connectChild(parentItemIndex, newNode);
        }
        return newNode;
    }

    /**
     * 通过移动相应数据项，达到节点合并的目的。
     *
     * @param parentNode       父节点对象
     * @param parentIndex  父节点中需要删除的数据项，其对应的索引值
     * @param brotherNode      当前节点的的兄弟节点
     * @param brotherIndex 兄弟节点需要删除的孩子节点，其对应的索引值
     * @param removeIndex  兄弟节点需要删除的数据项，其对应的索引值
     * @param childIndex       当前节点为父节点的孩子节点时，其所处的索引值
     * @param curNode          当前节点
     * @return 节点合并后的新节点对象
     */
    public Node<T> conbineNodeByMove(Node<T> parentNode, int parentIndex, Node<T> brotherNode,
                                     int brotherIndex, int removeIndex, int childIndex, Node<T> curNode) {
        DataItem<T> parentItem =
                parentNode.removeItem(parentIndex);
        Node<T> brotherChildNode =
                brotherNode.removeChild(brotherIndex);
        parentNode.insertItem(brotherNode.removeItem(removeIndex));
        Node<T> newNode = new Node<T>();
        newNode.insertItem(parentItem);
        newNode.insertItem(curNode.getItem(0));
        if (curNode.getChildIndex() == parentNode.getItemNums()) {
            newNode.connectChild(0, brotherChildNode);
            newNode.connectChild(1, curNode.getChild(0));
            newNode.connectChild(2, curNode.getChild(1));
        } else {
            newNode.connectChild(0, curNode.getChild(0));
            newNode.connectChild(1, curNode.getChild(1));
            newNode.connectChild(2, brotherChildNode);
        }
        parentNode.connectChild(childIndex, newNode);
        return newNode;
    }

    /**
     * 合并式删除指定数据项
     * 当删除两个节点所对应的叶节点时，删除后，需要大幅度调整树的平衡，
     * 因此在删除过程中，先对搜索过程中两节点的节点进行合并，最后通过simpleRemove方法删除指定的数据项。
     *
     * @param node 搜索过程中，所到达的节点对象
     * @param key  需要删除的数据项所对应的关键字
     */
    public void mergeRemove(Node<T> node, T key) {
        if (node.isLeaf() && !isSingleTwoLeafNode(node)) {
            simpleRemove(node, key);
            return;
        }
        int itemNums = node.getItemNums();
        if (itemNums > 1) {
            mergeRemove(getNextChild(node, key), key);
        } else {
            Node<T> parentNode = node.getParent();
            if (parentNode == null) {
                mergeRemove(getNextChild(node, key), key);
                return;
            }
            Node<T> newNode = new Node<T>();
            int parentItemNums = parentNode.getItemNums();
            int childIndex = node.getChildIndex();
            if (childIndex == parentItemNums) {
                Node<T> brotherNode = parentNode.getChild(childIndex - 1);
                int brotherItemNums = brotherNode.getItemNums();
                if (brotherItemNums == 1) {
                    newNode = conbineNodeByMerge(parentNode, childIndex - 1, childIndex, brotherNode, node);
                } else {
                    newNode = conbineNodeByMove(parentNode, childIndex - 1, brotherNode, brotherItemNums, brotherItemNums - 1, childIndex, node);
                }
            } else {
                Node<T> brotherNode = parentNode.getChild(childIndex + 1);
                int brotherItemNums = brotherNode.getItemNums();
                if (brotherItemNums == 1) {
                    newNode = conbineNodeByMerge(parentNode, childIndex, childIndex + 1, brotherNode, node);
                } else {
                    newNode = conbineNodeByMove(parentNode, childIndex, brotherNode, 0, 0, childIndex, node);
                }
            }
            if (!newNode.isLeaf()) {
                mergeRemove(getNextChild(newNode, key), key);
            } else {
                newNode.removeItem(newNode.findItem(key));
            }
        }
    }

    /**
     * 简单删除指定数据项
     * 该方法不需要大幅度调整树的平衡，仅仅需要父节点、兄弟节点以及当前节点之间进行简单的数据项替换或者删除，就可完成指定数据项的删除。
     *
     * @param node 需要删除的数据项所处的节点对象
     * @param key  需要删除的数据项所对应的关键字
     */
    private void simpleRemove(Node<T> node, T key) {
        Node<T> removeNode = node;
        Node<T> parentNode = removeNode.getParent();
        int removeItemIndex = removeNode.findItem(key);
        int removeItemNums = removeNode.getItemNums();
        if (parentNode == null || removeItemNums > 1) {
            removeNode.removeItem(removeItemIndex);
            return;
        }
        int parentItemNums = parentNode.getItemNums();
        int childIndex = removeNode.getChildIndex();
        Node<T> brotherNode = null;
        int brotherItemNums = 0;
        if (parentItemNums == 1) {
            int brotherIndex = childIndex ^ 1;
            brotherNode = parentNode.getChild(brotherIndex);
            int brotherItemIndex = (brotherIndex == 0) ? brotherNode.getItemNums() - 1 : 0;
            removeItemByReplace(parentNode, 0, node, brotherNode, brotherItemIndex);
        } else {
            if (childIndex == 0) {
                brotherNode = parentNode.getChild(1);
                brotherItemNums = brotherNode.getItemNums();
                if (brotherItemNums == 1) {
                    removeItemByDelete(parentNode, 0, childIndex, brotherNode);
                } else {
                    removeItemByReplace(parentNode, 0, removeNode, brotherNode, 0);
                }
            } else {
                int index = childIndex - 1;
                brotherNode = parentNode.getChild(index);
                brotherItemNums = brotherNode.getItemNums();
                if (brotherItemNums == 1) {
                    removeItemByDelete(parentNode, index, childIndex, brotherNode);
                } else {
                    removeItemByReplace(parentNode, index, removeNode, brotherNode, brotherItemNums - 1);
                }
            }
        }
    }

    /**
     * 通过各数据项之间的相互删除，来达到删除指定数据项的目的
     *
     * @param parentNode      父节点对象
     * @param parentItemIndex 父节点中需要删除的数据项，其对应的索引值
     * @param childIndex      需要删除数据项所对应的节点，处于父节点的孩子数组中的索引值
     * @param brotherNode     需要删除数据项所对应节点对象的兄弟节点
     */
    private void removeItemByDelete(Node<T> parentNode, int parentItemIndex,
                                    int childIndex, Node<T> brotherNode) {
        parentNode.removeChild(childIndex);
        DataItem<T> item = parentNode.removeItem(parentItemIndex);
        brotherNode.insertItem(item);
    }

    /**
     * 通过各数据项之间相互取代，来达到删除指定数据项的目的
     *
     * @param parentNode       父节点对象
     * @param parentItemIndex  父节点中需要删除的数据项，其对应的索引值
     * @param removeNode       需要删除的数据项多对应的节点对象
     * @param brotherNode      需要删除数据项所对应节点对象的兄弟节点
     * @param brotherItemIndex 兄弟节点需要删除的数据项，其对应的索引值
     */
    private void removeItemByReplace(Node<T> parentNode, int parentItemIndex, Node<T> removeNode,
                                     Node<T> brotherNode, int brotherItemIndex) {
        DataItem<T> replaceItem = brotherNode.removeItem(brotherItemIndex);
        DataItem<T> parentItem = parentNode.getItem(parentItemIndex);
        DataItem<T> removeItem = removeNode.getItem(0);
        removeItem.key = parentItem.key;
        removeItem.value = parentItem.value;
        parentItem.key = replaceItem.key;
        parentItem.value = replaceItem.value;
    }

    /**
     * 移除指定节点所需要的模式
     *
     * @param removeNode 需要移除的节点对象
     * @return 返回true 则使用简单删除模式进行节点删除;
     * 返回false则使用合并式删除模式进行节点删除
     */
    public boolean removeMode(Node<T> removeNode) {
        Node<T> parentNode = removeNode.getParent();
        if (parentNode != null && parentNode.getItemNums() == 1 && removeNode.getItemNums() == 1) {
            int childIndex = removeNode.getChildIndex();
            int index = childIndex ^ 1;
            Node<T> brotherNode = parentNode.getChild(index);
            int brotherItemNums = brotherNode.getItemNums();
            if (brotherItemNums == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取指定节点的下一个孩子节点
     * 在搜索过程中，若该该节点不存在指定的数据项，则找到该节点下的孩子节点，进行后续搜索。
     *
     * @param curNode 指定节点对象
     * @param key     需要搜索关键字对象
     * @return 该节点下的孩子节点，作为后续搜索节点
     */
    private Node<T> getNextChild(Node<T> curNode, T key) {
        int index = 0;
        int itemNums = curNode.getItemNums();
        for (; index < itemNums; index++) {
            if (key.compareTo(curNode.getItem(index).key) < 0) {
                return curNode.getChild(index);
            }
        }
        return curNode.getChild(index);
    }

    /**
     * 找到指定关键字所对应的数据项
     * 通过调用findNode方法，获取数据项所处的节点对象，再通过findItem方法找到指定关键字所对应的数据项。
     *
     * @param key 指定关键字
     * @return 指定关键字所对应的数据项
     */
    private DataItem<T> findItem(T key) {
        Node<T> node = findNode(key);
        if (node == null) {
            return null;
        }
        int itemIndex = node.findItem(key);
        return node.getItem(itemIndex);
    }

    /**
     * 找到指定关键字所对应的节点对象
     *
     * @param key 指定关键字对象
     * @return 指定关键字所对应的节点对象
     */
    private Node<T> findNode(T key) {
        Node<T> node = rootNode;
        while (true) {
            if (node.findItem(key) != -1) {
                return node;
            } else if (node.isLeaf()) {
                return null;
            } else {
                node = getNextChild(node, key);
            }
        }
    }

    /**
     * 获取指定关键字所对应的值
     *
     * @param key 指定的关键值对象
     * @return 指定关键字所对应的值
     */
    public Object get(T key) {
        DataItem<T> item = findItem(key);
        return (item != null) ? item.value : null;
    }

    /**
     * 获取节点中某数据项的后继节点
     * 通过数据项所处的索引值获取相应的孩子节点，向下遍历孩子节点，每次都选取该节点的首数据项，直到节点为叶节点。
     * 与之对应的后继数据项为，该节点的首数据项。
     *
     * @param node      搜索后继节点的起始节点
     * @param itemIndex 起始节点中数据项位于该节点的索引值
     * @return 起始节点中某数据项的后继节点对象
     */
    private Node<T> getSuccessorNode(Node<T> node, int itemIndex) {
        Node<T> curNode = node;
        if (itemIndex >= 0 && !curNode.isLeaf()) {
            curNode = curNode.getChild(itemIndex + 1);
        }
        while (!curNode.isLeaf()) {
            curNode = curNode.getChild(0);
        }
        return curNode;
    }

    /**
     * 返回树元素的数量
     *
     * @return 树元素数量
     */
    public int size() {
        return size;
    }

    /**
     * 深度优先搜索
     */
    public void depthFirstSearch() {
        if (rootNode == null) {
            return;
        }
        ArrayDeque<Node<T>> nodeStack = new ArrayDeque<Node<T>>();
        nodeStack.push(rootNode);
        while (!nodeStack.isEmpty()) {
            Node<T> node = nodeStack.pop();
            int itemNums = node.getItemNums();
            int itemIndex = itemNums;
            while (itemIndex-- > 0) {
                System.out.println(node.getItem(itemIndex).toString());
            }
            if (!node.isLeaf()) {
                while (itemNums >= 0) {
                    nodeStack.push(node.getChild(itemNums--));
                }
            }
        }
    }

    /**
     * 广度优先搜索
     */
    public void breathFirstSearch() {
        if (rootNode == null) {
            return;
        }
        ArrayDeque<Node<T>> nodeDeque = new ArrayDeque<Node<T>>();
        nodeDeque.add(rootNode);
        while (!nodeDeque.isEmpty()) {
            Node<T> node = nodeDeque.remove();
            int itemNums = node.getItemNums();
            int itemIndex = 0;
            while (itemNums-- > 0) {
                System.out.print(node.getItem(itemIndex++).key + " ");
            }
            if (!node.isLeaf()) {
                for (int i = 0; i <= itemIndex; i++) {
                    nodeDeque.add(node.getChild(i));
                }
            }
        }
        System.out.println();
    }

    /**
     * 插入新的数据项
     *
     * @param key   数据项所对应的关键字
     * @param value 数据项所对应的值
     */
    public void insert(T key, Object value) {
        Node<T> curNode = rootNode;
        DataItem<T> newItem = new DataItem<T>(key, value);
        int index = 0;
        while (true) {
            index = curNode.findItem(key);
            if (index != -1) {
                curNode.getItem(index).value = value;
                return;
            }
            if (curNode.isFull()) {
                split(curNode);
                curNode = curNode.getParent();
                curNode = getNextChild(curNode, key);
            } else if (curNode.isLeaf()) {
                break;
            } else {
                curNode = getNextChild(curNode, key);
            }
        }
        curNode.insertItem(newItem);
        size++;
    }

    /**
     * 分裂满节点
     * 插入过程中，遇到满节点，就分裂该节点
     *
     * @param node 满节点对象
     */
    private void split(Node<T> node) {
        // assert node.isFull();操作中节点一定是满节点，否则不会执行该操作
        // 移除最右边两数据项
        DataItem<T> tailItem = node.removeTailItem();
        DataItem<T> midItem = node.removeTailItem();
        Node<T> parentNode = null;
        // 断开最右边两子节点的连接
        Node<T> childTwoNode = node.disconnectChild(2);
        Node<T> childThreeNode = node.disconnectChild(3);
        Node<T> newNode = new Node<T>(); // 新建一个节点，作为当前节点的兄弟节点
        if (node == rootNode)  // 根节点为满根
        {
            rootNode = new Node<T>(); // 新建节点
            parentNode = rootNode;  // 将新根设为父节点
            rootNode.connectChild(0, node); // 连接父节点和子节点
        } else {
            parentNode = node.getParent();
        }
        int itemIndex = parentNode.insertItem(midItem); // 将中间节点插入父节点，返回插入位置
        int itemNums = parentNode.getItemNums();
        for (int i = itemNums - 1; i > itemIndex; i--) {
            Node<T> tempNode = parentNode.disconnectChild(i);
            parentNode.connectChild(i + 1, tempNode);
        }
        parentNode.connectChild(itemIndex + 1, newNode);
        newNode.insertItem(tailItem);
        newNode.connectChild(0, childTwoNode);
        newNode.connectChild(1, childThreeNode);
    }
}

/**
 * 2、3、4树的节点类
 *
 * @param <T> 节点关键字的数据类型
 * @author 江峰
 * @create 2018-1-3
 */
@SuppressWarnings("unchecked")
class Node<T extends Comparable<T>> {
    private static final int ORDER = 4;
    private int itemNums; // 节点中实际存储的数据项数量，其值一定不大于3
    private Node<T> parent;
    private Node<T>[] childArray = new Node[ORDER]; // 子节点数组
    private DataItem<T>[] itemArray = new DataItem[ORDER - 1]; // 存储数据项数组
    private int childIndex;

    // 将参数中的节点作为子节点，与当前节点进行连接
    public void connectChild(int childNum, Node<T> child) {
        childArray[childNum] = child;
        if (child != null) {
            child.childIndex = childNum;
            child.parent = this; // 当前节点作为父节点
        }
    }

    // 获取节点所处的孩子索引
    public int getChildIndex() {
        return childIndex;
    }

    // 设置孩子节点的索引值
    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }

    // 断开指定的节点与当前节点的连接，这个节点一定是当前节点的子节点
    public Node<T> disconnectChild(int childNum) {
        Node<T> node = childArray[childNum];
        childArray[childNum] = null;
        return node;
    }

    // 根据相应索引值移除孩子节点
    public Node<T> removeChild(int childIndex) {
        Node<T> oldNode = childArray[childIndex];
        for (int i = childIndex; i < itemNums; i++) {
            childArray[i] = childArray[i + 1];
            childArray[i].childIndex = i;
        }
        childArray[itemNums] = null;
        return oldNode;
    }

    // 获取相应子节点
    public Node<T> getChild(int childNum) {
        return childArray[childNum];
    }

    // 获取父节点
    public Node<T> getParent() {
        return parent;
    }

    // 判断是否是叶节点
    public boolean isLeaf() {
        return (childArray[0] == null) ? true : false;
    }

    // 获取实际存储的数据项数量
    public int getItemNums() {
        return itemNums;
    }

    // 获取具体数据项
    public DataItem<T> getItem(int index) {
        return itemArray[index];
    }

    // 判断节点是否满了
    public boolean isFull() {
        return (itemNums == ORDER - 1) ? true : false;
    }

    // 查找
    public int findItem(T key) {
        for (int i = 0; i < itemNums; i++) {
            if (itemArray[i] == null) {
                break;
            } else if (itemArray[i].key.compareTo(key) == 0) {
                return i;
            }
        }
        return -1;
    }

    // 未满节点的插入
    public int insertItem(DataItem<T> newItem) {
        T newKey = newItem.key;
        for (int i = itemNums - 1; i >= 0; i--) {
            T key = itemArray[i].key;
            if (newKey.compareTo(key) < 0) {
                itemArray[i + 1] = itemArray[i];
            } else if (newKey.compareTo(key) > 0) {
                itemNums++;
                itemArray[i + 1] = newItem;
                return i + 1;
            } else {
                itemArray[i].value = newItem.value;
                return i;
            }
        }
        // 若上述代码没有执行返回操作，那么这是空节点
        itemNums++;
        itemArray[0] = newItem;
        return 0;
    }

    // 移除数据项，从后向前移除
    public DataItem<T> removeTailItem() {
        DataItem<T> item = itemArray[itemNums - 1];
        itemArray[--itemNums] = null;
        return item;
    }

    // 移除指定索引处的数据项
    public DataItem<T> removeItem(int index) {
        if (index < 0 || index >= itemNums) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        DataItem<T> item = itemArray[index];
        int i = index;
        for (; i < itemNums - 1; i++) {
            itemArray[i] = itemArray[i + 1];
        }
        itemArray[i] = null;
        itemNums--;
        return item;
    }

    @Override
    // 重写toString()方法
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < itemNums; i++) {
            sb.append(itemArray[i].toString() + " | ");
        }
        sb.setCharAt(sb.length() - 2, ']');
        return sb.toString();
    }
}

/**
 * 插入2、3、4树的数据结构类
 *
 * @param <T> 节点关键字的数据类型
 * @author 江峰
 * @create 2018-1-3
 */
class DataItem<T extends Comparable<T>> {
    public T key;
    public Object value;

    public DataItem(T key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key = null");
        }
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "key = " + key + " , value = " + value;
    }
}

/**
 * 2、3、4树的演示类
 *
 * @author 江峰
 * @create 2018-1-3
 */
class Tree234Demo {
    public static void main(String[] args) {
        int seed = 10;
        while (seed-- > 0) {
            int len = new Random().nextInt(100);
            List<Integer> list = new ArrayList<Integer>(len);
            for (int i = 0; i < len; i++) {
                list.add(i + 1);
            }
            Tree234<Integer> tree = new Tree234<Integer>();
            for (Integer i : list) {
                tree.insert(i, i);
            }
            System.out.print("   ------>  ");
            tree.breathFirstSearch();
            java.util.Collections.shuffle(list);
            for (Integer c : list) {
                System.out.print(c + "  ------>  ");
                tree.remove(c);
                tree.breathFirstSearch();
            }
        }
    }
}

