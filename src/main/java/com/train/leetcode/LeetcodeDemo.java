package com.train.leetcode;

import com.common.utils.ArrayUtils;
import com.common.utils.MathUtils;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;

@SuppressWarnings("unused")
public class LeetcodeDemo {
    public static void main(String[] args) {

    }

    public static int rotatedDigits(int N) {
        int count = 0;
        for (int i = 1; i <= N; ++i) {
            if (isGoodNumber(i)) {
                ++count;
            }
        }
        return count;
    }

    private static boolean isGoodNumber(int n) {
        boolean isGood = false;
        while (n > 0) {
            switch (n % 10) {
                case 0:
                case 1:
                case 8:
                    break;
                case 3:
                case 4:
                case 7:
                    return false;
                default:
                    isGood = true;
            }
            n /= 10;
        }
        return isGood;
    }

    // 748. Shortest Completing Word
    // https://leetcode.com/problems/shortest-completing-word/
    public static String shortestCompletingWord(String licensePlate, String[] words) {
        int[] lowercaseLetterCounts = new int[26];
        int num = 0;
        for (int i = licensePlate.length() - 1; i >= 0; --i) {
            int c = licensePlate.charAt(i);
            if (c >= 65) {
                lowercaseLetterCounts[c <= 90 ? c - 65 : c - 97]++;
                ++num;
            }
        }
        int[] letters = new int[num], counts = new int[num];
        num = 0;
        for (int i = 0; i < 26; ++i) {
            if (lowercaseLetterCounts[i] > 0) {
                letters[num] = i;
                counts[num++] = lowercaseLetterCounts[i];
            }
        }
        int targetWordLen = 2147483647;
        String result = "";
        outer:
        for (String word : words) {
            int len = word.length();
            if (len >= targetWordLen) {
                continue;
            }
            for (int i = 0; i < num; ++i) {
                lowercaseLetterCounts[letters[i]] = counts[i];
            }
            for (int i = 0; i < len; ++i) {
                --lowercaseLetterCounts[word.charAt(i) - 'a'];
            }
            for (int i = 0; i < num; ++i) {
                if (lowercaseLetterCounts[letters[i]] > 0) {
                    continue outer;
                }
            }
            targetWordLen = len;
            result = word;
            if (targetWordLen == num) {
                break;
            }
        }
        return result;
    }

    private static String shortestCompletingWord0(String licensePlate, String[] words) {
        int[] lowercaseLetterCounts = new int[26];
        for (int i = licensePlate.length() - 1; i >= 0; --i) {
            int c = licensePlate.charAt(i);
            if (c >= 65 && c <= 90) {
                lowercaseLetterCounts[c - 65]++;
            }
            if (c >= 97 && c <= 122) {
                lowercaseLetterCounts[c - 97]++;
            }
        }
        int targetWordLen = 2147483647;
        String result = "";
        for (String word : words) {
            int len = word.length();
            if (len >= targetWordLen) {
                continue;
            }
            int[] counts = Arrays.copyOf(lowercaseLetterCounts, 26);
            for (int i = 0; i < len; ++i) {
                int c = word.charAt(i) - 'a';
                if (c >= 0 && c <= 25 && counts[c] > 0) {
                    --counts[c];
                }
            }
            int index = -1;
            while (++index < 26 && counts[index] == 0) ;
            if (index == 26) {
                targetWordLen = len;
                result = word;
            }
        }
        return result;
    }

    // 953. Verifying an Alien Dictionary
    // https://leetcode.com/problems/verifying-an-alien-dictionary/
    public static boolean isAlienSorted(String[] words, String order) {
        int[] grades = new int[26];
        for (int i = order.length() - 1; i >= 0; --i) {
            grades[order.charAt(i) - 97] = i;
        }
        int len = words.length;
        outer:
        for (int i = words.length - 1; i > 0; --i) {
            String bigWord = words[i], smallWord = words[i - 1];
            int limit = Math.min(bigWord.length(), smallWord.length());
            for (int j = 0; j < limit; ++j) {
                int g1 = grades[bigWord.charAt(j) - 97];
                int g2 = grades[smallWord.charAt(j) - 97];
                if (g1 > g2) {
                    continue outer;
                }
                if (g1 < g2) {
                    return false;
                }
            }
            if (bigWord.length() <= smallWord.length()) {
                return false;
            }
        }
        return true;
    }

    // 1022. Sum of Root To Leaf Binary Numbers
    // https://leetcode.com/problems/sum-of-root-to-leaf-binary-numbers/
    private static int sum = 0;

    public static int sumRootToLeaf(TreeNode root) {
        if (root == null) {
            return 0;
        }
        sumRootToLeafHelper(root, 0);
        return sum;
    }

    private static void sumRootToLeafHelper(TreeNode root, int value) {
        if (root.left == root.right) {
            sum += (value << 1) + root.val;
            return;
        }
        value = (value << 1) + root.val;
        if (root.left != null) {
            sumRootToLeafHelper(root.left, value);
        }
        if (root.right != null) {
            sumRootToLeafHelper(root.right, value);
        }
    }

    private static int sumRootToLeaf0(TreeNode root) {
        if (root == null) {
            return 0;
        }
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        HashMap<TreeNode, Integer> map = new HashMap<>();
        if (root.left == root.right) {
            return root.val;
        }
        if (root.left != null) {
            stack.push(root.left);
            map.put(root.left, root.val);
        }
        if (root.right != null) {
            stack.push(root.right);
            map.put(root.right, root.val);
        }
        int sum = 0, value = 0;
        while (!stack.isEmpty()) {
            root = stack.removeLast();
            value = (map.get(root) << 1) + root.val;
            if (root.left == root.right) {
                sum += value;
                continue;
            }
            if (root.left != null) {
                stack.push(root.left);
                map.put(root.left, value);
            }
            if (root.right != null) {
                stack.push(root.right);
                map.put(root.right, value);
            }
        }
        return sum;
    }

    // 917. Reverse Only Letters
    // https://leetcode.com/problems/reverse-only-letters/
    public static String reverseOnlyLetters(String S) {
        int len;
        if (S == null || (len = S.length()) < 2) {
            return S;
        }
        char[] letters = S.toCharArray();
        int left = 0, right = len - 1;
        while (left < right) {
            char c1 = '0';
            while (left < right) {
                c1 = letters[left];
                if (c1 < 65 || (c1 > 90 && c1 < 97)) {
                    ++left;
                } else {
                    break;
                }
            }
            char c2 = '0';
            while (left < right) {
                c2 = letters[right];
                if (c2 < 65 || (c2 > 90 && c2 < 97)) {
                    --right;
                } else {
                    break;
                }
            }
            if (left == right) {
                break;
            }
            letters[left++] = c2;
            letters[right--] = c1;
        }
        return new String(letters);
    }

    // 824. Goat Latin
    // https://leetcode.com/problems/goat-latin/
    public static String toGoatLatin(String S) {
        List<String> wordList = new ArrayList<>();
        simpleSplit(S, wordList);
        int wordNum = wordList.size();
        int newCapacity = 2 * wordNum + S.length() + wordNum * (wordNum + 1) / 2;
        StringBuilder result = new StringBuilder(newCapacity);
        long mask = 4575140898685201L;
        for (int i = 0; i < wordNum; ++i) {
            String word = wordList.get(i);
            char c = word.charAt(0);
            if ((mask & (1L << (c - 65))) > 0) {
                result.append(word);
            } else {
                result.append(word.substring(1, word.length())).append(c);
            }
            result.append('m').append('a');
            for (int j = i; j >= 0; --j) {
                result.append('a');
            }
            if (i + 1 < wordNum) {
                result.append(' ');
            }
        }
        return result.toString();
    }

    // 226. Invert Binary Tree
    // https://leetcode.com/problems/invert-binary-tree/
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        helper(root);
        return root;
    }

    private static void helper(TreeNode root) {
        TreeNode node = root.left;
        root.left = root.right;
        root.right = node;
        if (root.left != null) {
            helper(root.left);
        }
        if (root.right != null) {
            helper(root.right);
        }
    }

    // 1089. Duplicate Zeros
    // https://leetcode.com/problems/duplicate-zeros/
    public static void duplicateZeros(int[] arr) {
        int len = arr.length, i1 = 0, i2 = 0;
        int[] result = new int[len];
        while (i2 < len) {
            for (; i1 < len; ++i1) {
                result[i2++] = arr[i1];
                if (i2 == len || arr[i1] == 0) {
                    ++i1;
                    break;
                }
            }
            ++i2;
        }
        System.arraycopy(result, 0, arr, 0, len);
    }

    private void duplicateZeros2(int[] arr) {
        int len = arr.length, j = len;
        for (int a : arr) {
            if (a == 0) ++j;
        }
        for (int i = len - 1; i >= 0; --i) {
            if (--j < len) {
                arr[j] = arr[i];
            }
            if (arr[i] == 0 && --j < len) {
                arr[j] = 0;
            }
        }
    }

    private static void duplicateZeros1(int[] arr) {
        int len = arr.length, index = 0, count = 0;
        ArrayDeque<Integer> deque = new ArrayDeque<>(len);
        while (count != len) {
            for (; index < len; ++index) {
                deque.addLast(arr[index]);
                if (++count == len || arr[index] == 0) {
                    ++index;
                    break;
                }
            }
            if (count < len) {
                ++count;
                deque.addLast(0);
            }
        }
        index = 0;
        for (int value : deque) {
            arr[index++] = value;
        }
    }

    private static void duplicateZeros0(int[] arr) {
        int len = arr.length, index = 0;
        while (index < len) {
            while (index < len && arr[index] != 0) {
                if (++index == len) {
                    return;
                }
            }
            for (int i = len - 1; i > index; --i) {
                arr[i] = arr[i - 1];
            }
            index += 2;
        }
    }

    // 637. Average of Levels in Binary Tree
    // https://leetcode.com/problems/average-of-levels-in-binary-tree/
    public static List<Double> averageOfLevels(TreeNode root) {
        List<Double> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        ArrayDeque<TreeNode> deque = new ArrayDeque<>();
        deque.push(root);
        while (!deque.isEmpty()) {
            int size = deque.size();
            double sum = 0;
            for (int i = 1; i <= size; ++i) {
                root = deque.removeLast();
                sum += root.val;
                if (root.left != null) {
                    deque.push(root.left);
                }
                if (root.right != null) {
                    deque.push(root.right);
                }
            }
            result.add(sum / size);
        }
        return result;
    }

    // 412. Fizz Buzz
    // https://leetcode.com/problems/fizz-buzz/
    public List<String> fizzBuzz(int n) {
        List<String> result = new ArrayList<>(n);
        for (int i = 1; i <= n; ++i) {
            if ((i % 15) == 0) {
                result.add("FizzBuzz");
            } else if ((i % 3) == 0) {
                result.add("Fizz");
            } else if ((i % 5) == 0) {
                result.add("Buzz");
            } else {
                result.add(Integer.toString(i));
            }
        }
        return result;
    }

    private static List<String> fizzBuzz1(int n) {
        List<String> result = new ArrayList<String>(n);
        for (int i = 1, fizz = 3, buzz = 5; i <= n; i++) {
            if (i == fizz && i == buzz) {
                result.add("FizzBuzz");
                fizz += 3;
                buzz += 5;
            } else if (i == fizz) {
                result.add("Fizz");
                fizz += 3;
            } else if (i == buzz) {
                result.add("Buzz");
                buzz += 5;
            } else {
                result.add(String.valueOf(i));
            }
        }
        return result;
    }

    private static List<String> fizzBuzz0(int n) {
        List<String> result = new ArrayList<String>(n);
        for (int i = 1, fizz = 0, buzz = 0; i <= n; i++) {
            ++fizz;
            ++buzz;
            if (fizz == 3 && buzz == 5) {
                result.add("FizzBuzz");
                fizz = buzz = 0;
            } else if (fizz == 3) {
                result.add("Fizz");
                fizz = 0;
            } else if (buzz == 5) {
                result.add("Buzz");
                buzz = 0;
            } else {
                result.add(String.valueOf(i));
            }
        }
        return result;
    }

    // 496. Next Greater Element I
    // https://leetcode.com/problems/next-greater-element-i/
    public static int[] nextGreaterElement(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> map = new HashMap<>();
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        for (int num : nums2) {
            while (!stack.isEmpty() && stack.peek() < num) {
                map.put(stack.pop(), num);
            }
            stack.push(num);
        }
        for (int i = nums1.length - 1; i >= 0; --i) {
            nums1[i] = map.getOrDefault(nums1[i], -1);
        }
        return nums1;
    }

    private static int[] nextGreaterElement0(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int len2 = nums2.length;
        for (int i = 0; i < len2; ++i) {
            map.put(nums2[i], i);
        }
        int len1 = nums1.length;
        int[] result = new int[len1];
        for (int i = 0; i < len1; ++i) {
            int value = nums1[i], index = map.get(value);
            while (++index < len2 && nums2[index] < value) ;
            result[i] = index < len2 ? nums2[index] : -1;
        }
        return result;
    }

    // 429. N-ary Tree Level Order Traversal
    // https://leetcode.com/problems/n-ary-tree-level-order-traversal/
    public static List<List<Integer>> levelOrder(MultiNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        helper(new ArrayList<MultiNode>(Arrays.asList(root)), result);
        return result;
    }

    private static void helper(List<MultiNode> nodes, List<List<Integer>> result) {
        if (nodes.size() == 0) {
            return;
        }
        List<MultiNode> nodeList = new ArrayList<MultiNode>();
        List<Integer> valList = new ArrayList<Integer>();
        for (MultiNode node : nodes) {
            if (node.children != null) {
                for (MultiNode n : node.children) {
                    nodeList.add(n);
                }
            }
            valList.add(node.val);
        }
        result.add(valList);
        helper(nodeList, result);
    }

    private static List<List<Integer>> levelOrder0(MultiNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        ArrayDeque<MultiNode> stack = new ArrayDeque<>();
        stack.push(root);
        result.add(new ArrayList<>(Arrays.asList(root.val)));
        int size = 1;
        while (!stack.isEmpty()) {
            List<Integer> list = new ArrayList<>(size * 3);
            while (size-- > 0) {
                List<MultiNode> nodes = stack.pop().children;
                if (nodes != null) {
                    for (MultiNode node : nodes) {
                        stack.addLast(node);
                        list.add(node.val);
                    }
                }
            }
            size = stack.size();
            if (list.size() > 0) {
                result.add(list);
            }
        }
        return result;
    }

    // 575. Distribute Candies
    // https://leetcode.com/problems/distribute-candies/
    public static int distributeCandies(int[] candies) {
        int maxNum = candies.length >> 1;
        boolean[] exists = new boolean[200001];
        int uniqueNum = 0;
        for (int candy : candies) {
            if (!exists[candy + 100000]) {
                exists[candy + 100000] = true;
                if (++uniqueNum == maxNum) {
                    return maxNum;
                }
            }
        }
        return uniqueNum;
    }

    private static int distributeCandies2(int[] candies) {
        int len = candies.length;
        int minValue = 2147483647, maxValue = -2147483648;
        for (int candy : candies) {
            if (candy > maxValue) {
                maxValue = candy;
            }
            if (candy < minValue) {
                minValue = candy;
            }
        }
        boolean[] exists = new boolean[maxValue - minValue + 1];
        int count = 0;
        for (int candy : candies) {
            if (!exists[candy - minValue]) {
                exists[candy - minValue] = true;
                ++count;
            }
        }
        return count > len / 2 ? len / 2 : count;
    }

    private static int distributeCandies1(int[] candies) {
        int maxNum = candies.length / 2;
        HashSet<Integer> set = new HashSet<>((int) (candies.length / 0.75f));
        for (int candy : candies) {
            set.add(candy);
        }
        return set.size() > maxNum ? maxNum : set.size();
    }

    private static int distributeCandies0(int[] candies) {
        int maxNum = candies.length / 2;
        HashSet<Integer> set = new HashSet<>();
        for (int candy : candies) {
            set.add(candy);
        }
        return set.size() > maxNum ? maxNum : set.size();
    }

    // 669. Trim a Binary Search Tree
    // https://leetcode.com/problems/trim-a-binary-search-tree/
    public static TreeNode trimBST(TreeNode root, int L, int R) {
        while (root != null) {
            int value = root.val;
            if (value < L) {
                root = root.right;
            } else if (value > R) {
                root = root.left;
            } else {
                break;
            }
        }
        if (root == null) {
            return null;
        }
        TreeNode node = root.left, head = root;
        while (node != null) {
            if (node.val > L) {
                head = node;
                node = node.left;
            } else if (node.val == L) {
                node.left = null;
                break;
            } else {
                head.left = node = node.right;
            }
        }
        node = root.right;
        head = root;
        while (node != null) {
            if (node.val < R) {
                head = node;
                node = node.right;
            } else if (node.val == R) {
                node.right = null;
                break;
            } else {
                head.right = node = node.left;
            }
        }
        return root;
    }

    // 104. Maximum Depth of Binary Tree
    // https://leetcode.com/problems/maximum-depth-of-binary-tree/
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    // 884. Uncommon Words from Two Sentences
    // https://leetcode.com/problems/uncommon-words-from-two-sentences/
    public static String[] uncommonFromSentences(String A, String B) {
        HashMap<String, Integer> map = new HashMap<>();
        List<String> wordList = new ArrayList<>();
        simpleSplit(A, wordList);
        simpleSplit(B, wordList);
        int wordNum = wordList.size(), count = 0;
        boolean[] commons = new boolean[wordNum];
        for (int i = 0; i < wordNum; ++i) {
            String word = wordList.get(i);
            Integer b = map.get(word);
            if (b == null) {
                map.put(word, i);
                commons[i] = true;
                ++count;
            } else {
                if (b != -1) {
                    --count;
                    commons[b] = false;
                    map.replace(word, b, -1);
                }
            }
        }
        String[] result = new String[count];
        for (int i = 0, index = 0; i < wordNum; ++i) {
            if (commons[i]) {
                result[index++] = wordList.get(i);
            }
        }
        return result;
    }

    private static void simpleSplit(String str, List<String> wordList) {
        int len = str.length(), begin = 0, end = 0;
        while (begin < len) {
            end = begin;
            while (++end < len && str.charAt(end) != ' ') ;
            wordList.add(str.substring(begin, end));
            begin = end + 1;
        }
    }

    private static String[] uncommonFromSentences1(String A, String B) {
        HashMap<String, Integer> map = new HashMap<>();
        String[] words = (A + " " + B).split("\\s+");
        int wordNum = words.length, count = 0;
        boolean[] commons = new boolean[wordNum];
        for (int i = 0; i < wordNum; ++i) {
            Integer b = map.get(words[i]);
            if (b == null) {
                map.put(words[i], i);
                commons[i] = true;
                ++count;
            } else {
                if (b != -1) {
                    --count;
                    commons[b] = false;
                    map.replace(words[i], b, -1);
                }
            }
        }
        String[] result = new String[count];
        for (int i = 0, index = 0; i < wordNum; ++i) {
            if (commons[i]) {
                result[index++] = words[i];
            }
        }
        return result;
    }

    private static String[] uncommonFromSentences0(String A, String B) {
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        for (String s : (A + " " + B).split("\\s+")) {
            if (map.containsKey(s)) {
                map.replace(s, true, false);
            } else {
                map.put(s, true);
            }
        }
        List<String> result = new ArrayList<String>(map.size());
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            if (entry.getValue()) {
                result.add(entry.getKey());
            }
        }
        return result.toArray(new String[0]);
    }

    // 463. Island Perimeter
    // https://leetcode.com/problems/island-perimeter/
    public static int islandPerimeter(int[][] grid) {
        int perimeter = 0;
        int rowLen = grid.length, colLen = grid[0].length;
        for (int row = 0; row < rowLen; ++row) {
            for (int col = 0; col < colLen; ++col) {
                if (grid[row][col] == 1) {
                    perimeter += 4;
                    if (row > 0 && grid[row - 1][col] == 1) {
                        perimeter -= 2;
                    }
                    if (col > 0 && grid[row][col - 1] == 1) {
                        perimeter -= 2;
                    }
                }
            }
        }
        return perimeter;
    }

    private static int islandPerimeter0(int[][] grid) {
        int perimeter = 0;
        int rowLen = grid.length, colLen = grid[0].length;
        for (int row = 0; row < rowLen; ++row) {
            for (int col = 0; col < colLen; ++col) {
                if (grid[row][col] == 1) {
                    if (row == 0 || grid[row - 1][col] == 0) {
                        ++perimeter;
                    }
                    if (row == rowLen - 1 || grid[row + 1][col] == 0) {
                        ++perimeter;
                    }
                    if (col == 0 || grid[row][col - 1] == 0) {
                        ++perimeter;
                    }
                    if (col == colLen - 1 || grid[row][col + 1] == 0) {
                        ++perimeter;
                    }
                }
            }
        }
        return perimeter;
    }

    // 682. Baseball Game
    // https://leetcode.com/problems/baseball-game/
    public static int calPoints(String[] ops) {
        int[] scores = new int[ops.length];
        int index = 0;
        for (String op : ops) {
            switch (op.charAt(0)) {
                case '+':
                    scores[index] = scores[index - 1] + scores[index - 2];
                    ++index;
                    break;
                case 'C':
                    --index;
                    break;
                case 'D':
                    scores[index] = 2 * scores[index - 1];
                    ++index;
                    break;
                default:
                    scores[index++] = Integer.parseInt(op);
                    break;
            }
        }
        int sum = 0;
        for (int i = 0; i < index; ++i) {
            sum += scores[i];
        }
        return sum;
    }

    // 897. Increasing Order Search Tree
    // https://leetcode.com/problems/increasing-order-search-tree/
    public static TreeNode increasingBST(TreeNode root) {
        TreeNode node = head = new TreeNode(0);
        increasingBSTHelper(root);
        return node.right;
    }

    private static TreeNode head;

    private static void increasingBSTHelper(TreeNode root) {
        if (root != null) {
            increasingBSTHelper(root.left);
            head = head.right = root;
            root.left = null;
            increasingBSTHelper(root.right);
        }
    }

    private static TreeNode increasingBST0(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode head = new TreeNode(0), node = head;
        ArrayDeque<TreeNode> nodeStack = new ArrayDeque<>();
        while (root != null || !nodeStack.isEmpty()) {
            if (root != null) {
                nodeStack.push(root);
                root = root.left;
            } else {
                root = nodeStack.pop();
                node = node.right = root;
                node.left = null;
                root = root.right;
            }
        }
        return head.right;
    }

    // 559. Maximum Depth of N-ary Tree
    // https://leetcode.com/problems/maximum-depth-of-n-ary-tree/
    public static int maxDepth(MultiNode root) {
        if (root == null) {
            return 0;
        }
        List<MultiNode> nodes = root.children;
        if (nodes == null) {
            return 1;
        }
        int max = 0;
        for (MultiNode node : nodes) {
            max = Math.max(maxDepth(node), max);
        }
        return max + 1;
    }

    private static int maxDepth0(MultiNode root) {
        return depthHelper(root, 0);
    }

    private static int depthHelper(MultiNode root, int depth) {
        if (root == null) {
            return depth;
        }
        List<MultiNode> nodes = root.children;
        int initDepth = depth + 1;
        if (nodes != null) {
            int maxDepth = initDepth;
            for (MultiNode node : nodes) {
                depth = depthHelper(node, initDepth);
                if (depth > maxDepth) {
                    maxDepth = depth;
                }
            }
            return maxDepth;
        }
        return initDepth;
    }

    // 811. Subdomain Visit Count
    // https://leetcode.com/problems/subdomain-visit-count/
    public static List<String> subdomainVisits(String[] cpdomains) {
        HashMap<String, Integer> map = new HashMap<>();
        for (String cpdomain : cpdomains) {
            int cpdomainLen = cpdomain.length(), index = -1;
            while (cpdomain.charAt(++index) != ' ') ;
            int cp = Integer.parseInt(cpdomain.substring(0, index));
            String domain = cpdomain.substring(index + 1);
            map.put(domain, map.getOrDefault(domain, 0) + cp);
            for (int i = index + 1; i < cpdomainLen; ++i) {
                if (cpdomain.charAt(i) == '.') {
                    domain = cpdomain.substring(i + 1);
                    map.put(domain, map.getOrDefault(domain, 0) + cp);
                }
            }
        }
        List<String> result = new ArrayList<>(map.size());
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getValue()).append(' ').append(entry.getKey());
            result.add(sb.toString());
        }
        return result;
    }

    // 965. Univalued Binary Tree
    // https://leetcode.com/problems/univalued-binary-tree/
    public static boolean isUnivalTree(TreeNode root) {
        if (root == null) {
            return true;
        }
        return helper(root.left, root.val) && helper(root.right, root.val);
    }

    private static boolean helper(TreeNode root, int target) {
        if (root == null) {
            return true;
        }
        if (root.val == target) {
            return helper(root.left, target) && helper(root.right, target);
        }
        return false;
    }

    // 9. Palindrome Number
    // https://leetcode.com/problems/palindrome-number/
    public static boolean isPalindrome(int x) {
        if (x < 0 || (x > 0 && (x % 10) == 0)) {
            return false;
        }
        int reverse = 0;
        while (x > reverse) {
            int ones = x % 10;
            x /= 10;
            reverse = reverse * 10 + ones;
        }
        return x == reverse || (x == reverse / 10);
    }

    private static boolean isPalindrome1(int x) {
        if (x < 0) {
            return false;
        } else if (x < 10) {
            return true;
        }
        final int[] stringSizes = {0, 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999};
        int stringSize = 0;
        for (int i = 9; i >= 0; --i) {
            if (x > stringSizes[i]) {
                stringSize = i + 1;
                break;
            }
        }
        int radix = (int) Math.pow(10, stringSize - 1);
        for (int i = (stringSize - 1) >>> 1; i >= 0; --i) {
            if (x / radix != x % 10) {
                return false;
            }
            x = (x % radix) / 10;
            radix /= 100;
        }
        return true;
    }

    private static boolean isPalindrome0(int x) {
        if (x < 0) {
            return false;
        }
        char[] digits = String.valueOf(x).toCharArray();
        int len = digits.length;
        int limit = (len >> 1) - 1;
        for (int i = 0, remaining = len - 1; i <= limit; i++) {
            if (digits[i] != digits[remaining - i]) {
                return false;
            }
        }
        return true;
    }

    // 7. Reverse Integer
    // https://leetcode.com/problems/reverse-integer/
    public static int reverse(int value) {
        if (value == Integer.MIN_VALUE) {
            return 0;
        }
        boolean negative = false;
        if (value < 0) {
            value = -value;
            negative = true;
        }
        int result = 0, limit = Integer.MIN_VALUE, multmin = limit / 10;
        while (value > 0) {
            if (result < multmin) {
                return 0;
            }
            int digit = value % 10;
            result = result * 10;
            if (result < limit + digit) {
                return 0;
            }
            result -= digit;
            value /= 10;
        }
        return negative ? result : -result;
    }

    private static int reverse0(int x) {
        if (x == Integer.MIN_VALUE || x == 0) {
            return 0;
        }
        boolean negative = false;
        if (x < 0) {
            x = -x;
            negative = true;
        }
        while (x % 10 == 0) {
            x /= 10;
        }
        char[] chars = Integer.toString(x).toCharArray();
        int len = chars.length;
        int remaining = len - 1;
        int limit = (len & 1) == 1 ? (remaining >>> 1) - 1 : remaining >>> 1;
        for (int i = 0; i <= limit; i++) {
            char c = chars[i];
            chars[i] = chars[remaining - i];
            chars[remaining - i] = c;
        }
        if (chars.length == 10) {
            char[] intChars = {'2', '1', '4', '7', '4', '8', '3', '6', '4', '7'};
            for (int i = 0; i < 10; i++) {
                if (chars[i] > intChars[i]) {
                    return 0;
                } else if (chars[i] < intChars[i]) {
                    break;
                }
            }
        }
        return negative ? -Integer.parseInt(new String(chars)) : Integer.parseInt(new String(chars));
    }

    // 500. Keyboard Row
    // https://leetcode.com/problems/keyboard-row/
    public static String[] findWords(String[] words) {
        byte[] rows = new byte[128];
        for (char c : new char[]{'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'}) {
            rows[c] = 1;
            rows[c + 32] = 1;
        }
        for (char c : new char[]{'Z', 'X', 'C', 'V', 'B', 'N', 'M'}) {
            rows[c] = 2;
            rows[c + 32] = 2;
        }
        List<String> result = new ArrayList<>(words.length);
        for (String word : words) {
            int len = word.length();
            if (len < 2) {
                result.add(word);
                continue;
            }
            int row = rows[word.charAt(0)], index = 0;
            while (++index < len && rows[word.charAt(index)] == row) ;
            if (index == len) {
                result.add(word);
            }
        }
        return result.toArray(new String[0]);
    }

    // 893. Groups of Special-Equivalent Strings
    // https://leetcode.com/problems/groups-of-special-equivalent-strings/
    public static int numSpecialEquivGroups(String[] A) {
        int len = A[0].length();
        Set<String> set = new HashSet<>();
        for (String s : A) {
            char[] vs = s.toCharArray();
            for (int i = 0; i < len; i += 2) {
                vs[i] += 26;
            }
            Arrays.sort(vs);
            set.add(new String(vs));
        }
        return set.size();
    }

    private static int numSpecialEquivGroups0(String[] A) {
        int len = A.length, strLen = A[0].length();
        int zeroLen = (strLen + 1) >>> 1, oneLen = strLen - zeroLen;
        boolean[] exists = new boolean[len];
        int num = 0, index = -1;
        while (true) {
            while (++index < len && exists[index]) ;
            if (index == len) {
                return num;
            }
            ++num;
            exists[index] = true;
            int[] zeros = new int[26];
            int[] ones = new int[26];
            letterCount(A[index], strLen, zeros, ones);
            outer:
            for (int i = index + 1; i < len; ++i) {
                int[] zs = new int[26];
                int[] os = new int[26];
                letterCount(A[i], strLen, zs, os);
                for (int j = 0; j < 26; ++j) {
                    if (zeros[j] != zs[j] || ones[j] != os[j]) {
                        continue outer;
                    }
                }
                exists[i] = true;
            }
        }
    }

    private static void letterCount(String str, int len, int[] zeros, int[] ones) {
        for (int i = 0; i < len; i++) {
            zeros[str.charAt(i++) - 97]++;
            if (i < len) {
                ones[str.charAt(i) - 97]++;
            }
        }
    }

    // 1046. Last Stone Weight
    // https://leetcode.com/problems/last-stone-weight/
    public static int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        for (int stone : stones) {
            queue.add(stone);
        }
        int weight = 0;
        while (queue.size() > 0) {
            weight = queue.remove();
            if (queue.size() > 0) {
                weight -= queue.remove();
                if (weight > 0) {
                    queue.add(weight);
                }
            }
        }
        return weight;
    }

    private static int lastStoneWeight0(int[] stones) {
        int[] counts = new int[1001];
        for (int stone : stones) {
            counts[stone]++;
        }
        int weight = 0, index = 1000;
        while (true) {
            while ((counts[index] & 1) == 0) {
                if (--index < 1) {
                    return 0;
                }
            }
            weight = index;
            while (--index > 0 && counts[index] == 0) ;
            if (index < 1) {
                break;
            }
            counts[index]--;
            weight -= index;
            if (weight > 0) {
                if (weight > index) {
                    index = weight;
                }
                counts[weight]++;
            }
        }
        return weight;
    }

    // 1047. Remove All Adjacent Duplicates In String
    // https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/
    public static String removeDuplicates(String S) {
        char[] result = S.toCharArray();
        int n = S.length(), m = n;
        for (int i = 0, j = 1; j < n; j++) {
            if (i >= 0 && result[j] == result[i]) {
                m -= 2;
                --i;
            } else {
                result[++i] = result[j];
            }
        }
        return new String(result, 0, m);
    }

    private static String removeDuplicates2(String S) {
        int len;
        if (S == null || (len = S.length()) < 2) {
            return S;
        }
        char[] result = new char[len];
        int index = 0;
        for (int i = 0; i < len; ++i) {
            if (index > 0 && result[index - 1] == S.charAt(i)) {
                --index;
            } else {
                result[index++] = S.charAt(i);
            }
        }
        return new String(result, 0, index);
    }

    private static String removeDuplicates1(String S) {
        int len;
        if (S == null || (len = S.length()) < 2) {
            return S;
        }
        char[] result = new char[len];
        char first, second = S.charAt(0);
        result[0] = second;
        int index = 0;
        for (int i = 1; i < len; ++i) {
            first = second;
            second = S.charAt(i);
            if (first == second) {
                second = --index < 0 ? '0' : result[index];
            } else {
                result[++index] = second;
            }
        }
        return new String(result, 0, index + 1);
    }

    private static String removeDuplicates0(String S) {
        int len;
        if (S == null || (len = S.length()) < 2) {
            return S;
        }
        char first, second = S.charAt(0);
        ArrayDeque<Character> stack = new ArrayDeque<>();
        stack.push(second);
        for (int i = 1; i < len; ++i) {
            first = second;
            second = S.charAt(i);
            if (first == second) {
                stack.pop();
                second = stack.isEmpty() ? '0' : stack.peek();
            } else {
                stack.push(second);
            }
        }
        len = stack.size();
        char[] result = new char[len];
        while (--len >= 0) {
            result[len] = stack.pollFirst();
        }
        return new String(result);
    }

    // 806. Number of Lines To Write String
    // https://leetcode.com/problems/number-of-lines-to-write-string/
    public static int[] numberOfLines(int[] widths, String S) {
        int len = S.length();
        int widthLine = 0, numLine = 0;
        for (int i = 0; i < len; ++i) {
            if (numLine == 0) {
                ++numLine;
            }
            int width = widths[S.charAt(i) - 97];
            widthLine += width;
            if (widthLine > 100) {
                widthLine = width;
                ++numLine;
            }
        }
        return new int[]{numLine, widthLine};
    }

    // 872. Leaf-Similar Trees
    // https://leetcode.com/problems/leaf-similar-trees/
    public boolean leafSimilar(TreeNode root1, TreeNode root2) {
        ArrayDeque<TreeNode> stack1 = new ArrayDeque<>();
        ArrayDeque<TreeNode> stack2 = new ArrayDeque<>();
        while (root1 != null || !stack1.isEmpty()) {
            if (root1 != null) {
                if (root1.left == root1.right) {
                    int value = root1.val;
                    if (root2 == null && stack2.isEmpty()) {
                        return false;
                    }
                    while (root2 != null || !stack2.isEmpty()) {
                        if (root2 != null) {
                            TreeNode node = root2;
                            stack2.push(root2);
                            root2 = root2.left;
                            if (node.left == node.right) {
                                if (node.val != value) {
                                    return false;
                                }
                                break;
                            }
                        } else {
                            root2 = stack2.pop().right;
                        }
                    }
                }
                stack1.push(root1);
                root1 = root1.left;
            } else {
                root1 = stack1.pop().right;
            }
        }
        return root2 == null && stack1.isEmpty();
    }

    // 821. Shortest Distance to a Character
    // https://leetcode.com/problems/shortest-distance-to-a-character/
    public static int[] shortestToChar(String S, char C) {
        int len = S.length(), left = -1;
        int[] result = new int[len];
        while (++left < len && S.charAt(left) != C) ;
        for (int i = 0; i < left; ++i) {
            result[i] = left - i;
        }
        int right = len;
        while (--right >= 0 && S.charAt(right) != C) ;
        for (int i = right + 1; i < len; ++i) {
            result[i] = i - right;
        }
        int begin, end = left;
        for (int i = left + 1; i <= right; ++i) {
            if (S.charAt(i) == C) {
                begin = end;
                end = i;
                int mid = (begin + end) >>> 1;
                for (int j = begin + 1; j <= mid; ++j) {
                    result[j] = j - begin;
                }
                for (int j = mid + 1; j < end; ++j) {
                    result[j] = end - j;
                }
            }
        }
        return result;
    }

    private static int[] shortestToChar0(String S, char C) {
        int len = S.length();
        List<Integer> list = new ArrayList<>();
        int[] result = new int[len];
        for (int i = 0; i < len; ++i) {
            if (S.charAt(i) == C) {
                list.add(i);
            }
        }
        int size = list.size();
        int first = list.get(0), end = list.get(size - 1);
        for (int i = 0; i < first; ++i) {
            result[i] = first - i;
        }
        for (int remaining = len - 1, i = remaining; i > end; --i) {
            result[i] = i - end;
        }
        int left, right = first;
        for (int i = 1; i < size; ++i) {
            left = right;
            right = list.get(i);
            int mid = (left + right) >>> 1;
            for (int j = left + 1; j <= mid; ++j) {
                result[j] = j - left;
            }
            for (int j = mid + 1; j < right; ++j) {
                result[j] = right - j;
            }
        }
        return result;
    }

    // 700. Search in a Binary Search Tree
    // https://leetcode.com/problems/search-in-a-binary-search-tree/
    public TreeNode searchBST(TreeNode root, int val) {
        if (root == null) {
            return null;
        }
        do {
            int v = root.val;
            if (v > val) {
                root = root.left;
            } else if (v < val) {
                root = root.right;
            } else {
                return root;
            }
        } while (root != null);
        return null;
    }

    // 944. Delete Columns to Make Sorted
    // https://leetcode.com/problems/delete-columns-to-make-sorted/
    public int minDeletionSize(String[] A) {
        int len = A.length, count = 0;
        for (int i = A[0].length() - 1; i >= 0; --i) {
            int j = 0;
            char first = A[0].charAt(i), second;
            while (++j < len && (second = A[j].charAt(i)) >= first) {
                first = second;
            }
            if (j < len) {
                ++count;
            }
        }
        return count;
    }

    private int minDeletionSize0(String[] A) {
        int len = A.length, count = 0;
        for (int i = A[0].length() - 1; i >= 0; --i) {
            int j = 0;
            while (++j < len && A[j].charAt(i) >= A[j - 1].charAt(i)) ;
            if (j < len) {
                ++count;
            }
        }
        return count;
    }

    // 1030. Matrix Cells in Distance Order
    // https://leetcode.com/problems/matrix-cells-in-distance-order/
    public int[][] allCellsDistOrder(int R, int C, int r0, int c0) {
        int r = R - 1, c = C - 1;
        int size = max(r0 + c0, max(r0 + c - c0, max(r - r0 + c0, c + r - r0 - c0)));
        int[][] coors = new int[R * C][];
        coors[0] = new int[]{r0, c0};
        int index = 1;
        for (int sum = 1; sum <= size; ++sum) {
            int begin = max(r0 - sum, 0), end = min(r0 + sum, r);
            for (int i = begin; i <= end; ++i) {
                int remaining = sum - abs(r0 - i);
                if (c0 - remaining >= 0) {
                    coors[index++] = new int[]{i, c0 - remaining};
                }
                if (remaining > 0 && c0 + remaining <= c) {
                    coors[index++] = new int[]{i, c0 + remaining};
                }
            }
        }
        return coors;
    }

    private int[][] allCellsDistOrder0(int R, int C, int r0, int c0) {
        int r = R - 1, c = C - 1;
        int size = max(r0 + c0, max(r0 + c - c0, max(r - r0 + c0, c + r - r0 - c0)));
        @SuppressWarnings("unchecked")
        List<int[]>[] coorLists = new ArrayList[size + 1];
        for (int i = 0; i < R; ++i) {
            for (int j = 0; j < C; ++j) {
                int index = abs(r0 - i) + abs(c0 - j);
                List<int[]> list = coorLists[index];
                if (list == null) {
                    coorLists[index] = list = new ArrayList<int[]>();
                }
                list.add(new int[]{i, j});
            }
        }
        ArrayList<int[]> result = new ArrayList<>(R * C);
        for (int i = 0; i <= size; ++i) {
            for (int[] array : coorLists[i]) {
                result.add(array);
            }
        }
        return result.toArray(new int[0][]);
    }

    // 1078. Occurrences After Bigram
    // https://leetcode.com/problems/occurrences-after-bigram/
    public String[] findOcurrences(String text, String first, String second) {
        int firstLen = first.length(), secondLen = second.length();
        int textLen = text.length();
        if (firstLen + secondLen >= textLen) {
            return new String[0];
        }
        char[] fs = first.toCharArray(), ss = second.toCharArray();
        char c1 = fs[0], c2 = ss[0];
        int limit1 = textLen - firstLen - secondLen - 2;
        int limit2 = textLen - secondLen - 1;
        ArrayList<String> result = new ArrayList<>();
        outer:
        for (int i = 0; i < limit1; ++i) {
            while (i < limit1 && text.charAt(i) != c1) {
                if (++i == limit1) {
                    break outer;
                }
            }
            int firstIndex = i, j = 1;
            for (++i; j < firstLen && fs[j] == text.charAt(i); ++i, ++j) ;
            if (j == firstLen && text.charAt(i) == ' ') {
                while (++i < limit2 && text.charAt(i) == ' ') ;
                int secondIndex = i;
                if (i < limit2 && text.charAt(i) == c2) {
                    int k = 1;
                    for (++i; k < secondLen && ss[k] == text.charAt(i); ++i, ++k) ;
                    if (k == secondLen && text.charAt(i) == ' ') {
                        while (++i < textLen && text.charAt(i) == ' ') ;
                        if (i < textLen) {
                            int begin = i;
                            while (++i < textLen && text.charAt(i) != ' ') ;
                            result.add(text.substring(begin, i));
                        }
                    }
                }
                i = secondIndex - 1;
            } else {
                i = firstIndex;
            }
        }
        return result.toArray(new String[0]);
    }

    // 617. Merge Two Binary Trees
    // https://leetcode.com/problems/merge-two-binary-trees/
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1 == null) return t2;
        if (t2 == null) return t1;
        TreeNode node = new TreeNode(t1.val + t2.val);
        if (t1.left != null && t2.left != null) {
            node.left = mergeTrees(t1.left, t2.left);
        } else if (t1.left != null) {
            node.left = t1.left;
        } else if (t2.left != null) {
            node.left = t2.left;
        } else {
            node.left = null;
        }
        if (t1.right != null && t2.right != null) {
            node.right = mergeTrees(t1.right, t2.right);
        } else if (t1.right != null) {
            node.right = t1.right;
        } else if (t2.right != null) {
            node.right = t2.right;
        } else {
            node.right = null;
        }
        return node;
    }

    private TreeNode mergeTrees1(TreeNode t1, TreeNode t2) {
        if (t1 == null) {
            return t2;
        }
        if (t2 == null) {
            return t1;
        }
        TreeNode node = new TreeNode(t1.val + t2.val);
        node.left = mergeTrees(t1.left, t2.left);
        node.right = mergeTrees(t1.right, t2.right);
        return node;
    }

    private TreeNode mergeTrees0(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) {
            return null;
        }
        TreeNode node = t1 == null ? t2 : t1;
        if (t1 != null && t2 != null) {
            node.val = t1.val + t2.val;
            node.left = mergeTrees(node.left, t2.left);
            node.right = mergeTrees(node.right, t2.right);
        }
        return node;
    }

    // 1021. Remove Outermost Parentheses
    // https://leetcode.com/problems/remove-outermost-parentheses/
    public String removeOuterParentheses(String s) {
        int len = s.length();
        StringBuilder sb = new StringBuilder(len);
        int num = 0;
        for (int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            if (c == '(') {
                if (++num > 1) {
                    sb.append(c);
                }
            } else {
                if (--num > 0) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    private String removeOuterParentheses0(String s) {
        int len = s.length();
        StringBuilder sb = new StringBuilder(len);
        ArrayDeque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            if (stack.isEmpty()) {
                stack.push(c);
            } else if (c == '(') {
                stack.push(c);
                sb.append(c);
            } else {
                stack.pop();
                if (!stack.isEmpty()) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }


    // 938. Range Sum of BST
    // https://leetcode.com/problems/range-sum-of-bst/
    public int rangeSumBST(TreeNode root, int L, int R) {
        return helper(root, L, R, 0);
    }

    private int helper(TreeNode node, int L, int R, int sum) {
        if (node != null) {
            int value = node.val;
            if (value > R) {
                sum = helper(node.left, L, R, sum);
            } else if (value < L) {
                sum = helper(node.right, L, R, sum);
            } else {
                sum = helper(node.left, L, R, sum + value);
                sum = helper(node.right, L, R, sum);
            }
        }
        return sum;
    }

    private int rangeSumBST0(TreeNode root, int L, int R) {
        TreeNode node = root;
        int sum = 0;
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        while (node != null || !stack.isEmpty()) {
            if (node != null) {
                stack.push(node);
                node = node.left;
            } else {
                node = stack.pop();
                int value = node.val;
                if (value >= L) {
                    if (value <= R) {
                        sum += value;
                    } else {
                        break;
                    }
                }
                node = node.right;
            }
        }
        return sum;
    }

    // 478. Generate Random Point in a Circle
    // https://leetcode.com/problems/generate-random-point-in-a-circle/
    private static double[] randPoint1(double radius, double x_center, double y_center) {
        Random rand = new Random();
        double x = rand.nextDouble();
        double y = rand.nextDouble();
        while (x * x + y * y > 1.0) {
            x = rand.nextDouble();
            y = rand.nextDouble();
        }
        int flagX = rand.nextInt(2) == 0 ? 1 : -1;
        int flagY = rand.nextInt(2) == 0 ? 1 : -1;
        return new double[]{x_center + x * flagX * radius, y_center + y * flagY * radius};
    }

    // https://www.cnblogs.com/yunlambert/p/10161339.html
    private static double[] randPoint0(double r, double x, double y) {
        Random rand = new Random();
        r = Math.sqrt(rand.nextDouble()) * r;
        double t = rand.nextDouble() * 2 * Math.PI;
        return new double[]{x + r * Math.cos(t), y + r * Math.sin(t)};
    }

    // 470. Implement Rand10() Using Rand7()
    // https://leetcode.com/problems/implement-rand10-using-rand7/
    private static int rand = rand7();

    public static int rand10() {
        rand += rand7();
        rand %= 10;
        return rand + 1;
    }

    // https://leetcode.com/problems/implement-rand10-using-rand7/discuss/210021/Special-and-Amazing-mathematical-solution-1-line
    private static int rand10_4() {
        return (rand7() + rand7() + rand7() + rand7() + rand7()) % 10 + 1;
    }

    public static int rand10_3() {
        int value = 7 * (rand7() - 1) + rand7();
        while (value > 40) {
            value = 7 * (rand7() - 1) + rand7();
        }
        return value % 10 + 1;
    }

    public static int rand10_2() {
        int i, j;
        while ((i = rand7()) > 6) ;
        while ((j = rand7()) > 5) ;
        return (i & 1) == 0 ? j : j + 5;
    }

    private static int rand10_1() {
        int first = rand7();
        while (first > 1) {
            if (first == 2) {
                int second = rand7();
                if (second < 4) {
                    return 7 + second;
                }
            }
            first = rand7();
        }
        return rand7();
    }

    private static int rand10_0() {
        int rand = 7 * (rand7() - 1) + rand7();
        while (rand > 10) {
            rand = 7 * (rand7() - 1) + rand7();
        }
        return rand;
    }

    private static int rand7() {
        return new Random().nextInt(7) + 1;
    }

    // 1031. Maximum Sum of Two Non-Overlapping Subarrays
    // https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/submissions/
    public static int maxSumTwoNoOverlap(int[] A, int L, int M) {
        int len = A.length, maxSum = -2147483648;
        if (L < M) {
            int temp = L;
            L = M;
            M = temp;
        }
        int sum = 0;
        for (int i = 0; i < L; ++i) {
            sum += A[i];
        }
        for (int i = L - 1; i < len; ++i) {
            if (i >= L) {
                sum = sum - A[i - L] + A[i];
            }
            int twoSum = sum + Math.max(maxSumOneNoOverlap(A, 0, i - L, M),
                    maxSumOneNoOverlap(A, i + 1, len, M));
            if (twoSum > maxSum) {
                maxSum = twoSum;
            }
        }
        return maxSum;
    }

    private static int maxSumOneNoOverlap(int[] A, int begin, int end, int L) {
        if (end - begin < L) {
            return -2147483648;
        }
        int sum = 0;
        int nextBegin = begin + L;
        for (int i = begin; i < nextBegin; ++i) {
            sum += A[i];
        }
        int maxSum = sum;
        for (int i = nextBegin; i < end; ++i) {
            sum = sum + A[i] - A[i - L];
            if (sum > maxSum) {
                maxSum = sum;
            }
        }
        return maxSum;
    }

    // 1051. Height Checker
    // https://leetcode.com/problems/height-checker/
    public static int heightChecker(int[] heights) {
        int[] counts = new int[101];
        int len = heights.length;
        for (int i = 0; i < len; ++i) {
            counts[heights[i]]++;
        }
        int num = 0, i1 = -1, i2 = 0;
        while (i2 < len) {
            while (counts[++i1] == 0) ;
            int count = counts[i1], height = i1;
            for (; --count >= 0; i2++) {
                if (heights[i2] != height) {
                    ++num;
                }
            }
        }
        return num;
    }

    // 898. Bitwise ORs of Subarrays
    // https://leetcode.com/problems/bitwise-ors-of-subarrays/
    public static int subarrayBitwiseORs0(int[] A) {
        int[] cur = new int[33], next = new int[33];
        int n1 = 0;
        HashSet<Integer> set = new HashSet<>();
        for (int a : A) {
            int n2 = 0;
            set.add(next[n2++] = a);
            for (int i = 0; i < n1; i++) {
                int a2 = a | cur[i];
                if (a2 != a) {
                    set.add(next[n2++] = a = a2);
                }
            }
            int[] temp = cur;
            cur = next;
            next = temp;
            n1 = n2;
        }
        return set.size();
    }

    public static int subarrayBitwiseORs(int[] nums) {
        HashSet<Integer> result = new HashSet<>();
        HashSet<Integer> cur = new HashSet<Integer>();
        for (int num : nums) {
            HashSet<Integer> next = new HashSet<Integer>();
            next.add(num);
            for (int value : cur) {
                next.add(value | num);
            }
            result.addAll(cur = next);
        }
        return result.size();
    }

    // 784. Letter Case Permutation
    // https://leetcode.com/problems/letter-case-permutation/
    public static List<String> letterCasePermutation(String S) {
        char[] letters = S.toCharArray();
        int len = letters.length;
        int count = 0;
        for (char c : letters) {
            if (c >= 'a' || c >= 'A') {
                ++count;
            }
        }
        List<String> result = new ArrayList<>(1 << count);
        result.add(S);
        if (count == 0) {
            return result;
        }
        helper(letters, 0, len, result);
        return result;
    }

    private static void helper(char[] letters, int begin, int end, List<String> result) {
        for (int i = begin; i < end; ++i) {
            char c = letters[i];
            if (c >= 'a' || c >= 'A') {
                helper(Arrays.copyOf(letters, end), i + 1, end, result);
                letters[i] = (char) (c >= 'a' ? c - 32 : c + 32);
                result.add(new String(letters));
                helper(Arrays.copyOf(letters, end), i + 1, end, result);
                return;
            }
        }
    }

    // 762. Prime Number of Set Bits in Binary Representation
    // https://leetcode.com/problems/prime-number-of-set-bits-in-binary-representation/
    public static int countPrimeSetBits(int L, int R) {
        int cnt = 0;
        while (L <= R) {
            cnt += 665772 >> Integer.bitCount(L++) & 1;
        }
        return cnt;
    }

    private static int countPrimeSetBits1(int L, int R) {
        boolean[] isPrimes = new boolean[33];
        for (int prime : new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31}) {
            isPrimes[prime] = true;
        }
        int count = 0;
        for (int i = L; i <= R; ++i) {
            if (isPrimes[Integer.bitCount(i)]) {
                ++count;
            }
        }
        return count;
    }

    private static int countPrimeSetBits0(int L, int R) {
        boolean[] isPrimes = new boolean[33];
        for (int prime : new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31}) {
            isPrimes[prime] = true;
        }
        int[] bits = countBits(R);
        int count = 0;
        for (int i = L; i <= R; ++i) {
            if (isPrimes[bits[i]]) {
                ++count;
            }
        }
        return count;
    }

    // 693. Binary Number with Alternating Bits
    // https://leetcode.com/problems/binary-number-with-alternating-bits/
    public boolean hasAlternatingBits(int n) {
        n = n + (n >> 1);
        return (n & (n + 1)) == 0;
    }

    // 477. Total Hamming Distance
    // https://leetcode.com/problems/total-hamming-distance/
    public static int totalHammingDistance(int[] nums) {
        int result = 0, len = nums.length;
        for (int i = 0; i < 32; i++) {
            int count = 0;
            for (int num : nums) {
                count += (num >> i) & 1;
            }
            result += count * (len - count);
        }
        return result;
    }

    private static int totalHammingDistance1(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            Integer count = map.get(num);
            map.put(num, count == null ? 1 : count + 1);
        }
        int size = map.size();
        int[] counts = new int[size];
        int[] values = new int[size];
        Iterator<Entry<Integer, Integer>> iter = map.entrySet().iterator();
        for (int i = 0; i < size; ++i) {
            Entry<Integer, Integer> e = iter.next();
            values[i] = e.getKey();
            counts[i] = e.getValue();
        }
        int result = 0;
        for (int i = size - 1; i > 0; --i) {
            int value = values[i], count = counts[i];
            for (int j = i - 1; j >= 0; --j) {
                result += count * counts[j] * Integer.bitCount(value ^ values[j]);
            }
        }
        return result;
    }

    private static int totalHammingDistance0(int[] nums) {
        int result = 0;
        for (int i = nums.length - 1; i > 0; --i) {
            int num = nums[i];
            for (int j = i - 1; j >= 0; --j) {
                result += Integer.bitCount(num ^ nums[j]);
            }
        }
        return result;
    }

    // 476. Number Complement
    // https://leetcode.com/problems/number-complement/
    public int findComplement(int num) {
        return ~num & ((Integer.highestOneBit(num) << 1) - 1);
    }

    public static int findMaximumXOR(int[] nums) {
        int maxXor = 0;
        for (int i = nums.length - 1; i > 0; --i) {
            for (int j = i - 1, num = nums[i]; j >= 0; --j) {
                int xor = num ^ nums[j];
                if (xor > maxXor) {
                    maxXor = xor;
                }
            }
        }
        return maxXor;
    }

    // 405. Convert a Number to Hexadecimal
    // https://leetcode.com/problems/convert-a-number-to-hexadecimal/
    private static final char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String toHex(int num) {
        if (num == 0) {
            return "0";
        }
        StringBuilder result = new StringBuilder(8);
        int mask = 0xF0000000;
        for (int i = 0; i < 8; ++i) {
            int index = (num & mask) >>> (28 - 4 * i);
            if (index > 0 || result.length() > 0) {
                result.append(hexChars[index]);
            }
            mask >>>= 4;
        }
        return result.toString();
    }

    // 401. Binary Watch
    // https://leetcode.com/problems/binary-watch/
    private static final char[] cs = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    @SuppressWarnings("unchecked")
    public static List<String> readBinaryWatch(int num) {
        int[] bits = countBits(59);
        List<Integer>[] hourLists = new ArrayList[4];
        for (int i = 0; i < 12; ++i) {
            int b = bits[i];
            if (hourLists[b] == null) {
                hourLists[b] = new ArrayList<Integer>();
            }
            hourLists[b].add(i);
        }

        List<Integer>[] minuteLists = new ArrayList[6];
        for (int i = 0; i < 60; ++i) {
            int b = bits[i];
            if (minuteLists[b] == null) {
                minuteLists[b] = new ArrayList<Integer>();
            }
            minuteLists[b].add(i);
        }
        List<String> result = new ArrayList<>();
        char[] watch = new char[5];
        for (int i = Math.min(num, 3); i >= 0; --i) {
            for (int hour : hourLists[i]) {
                int i1 = 0;
                if (hour > 9) {
                    watch[i1++] = cs[(hour / 10) % 10];
                }
                watch[i1++] = cs[hour % 10];
                watch[i1++] = ':';
                if (num - i > 5) {
                    return result;
                }
                for (int minute : minuteLists[num - i]) {
                    int i2 = i1;
                    if (minute > 9) {
                        watch[i2++] = cs[(minute / 10) % 10];
                    } else {
                        watch[i2++] = '0';
                    }
                    watch[i2++] = cs[minute % 10];
                    result.add(new String(Arrays.copyOf(watch, i2)));
                }
            }
        }
        return result;
    }

    // 393. UTF-8 Validation
    // https://leetcode.com/problems/utf-8-validation/
    public static boolean validUtf8(int[] data) {
        int len = data.length, index = 0;
        while (index < len) {
            int mask = (data[index] >> 4) & 0xF;
            switch (mask) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    break;
                case 12:
                case 13:
                    if (++index == len || (data[index] & 0xC0) != 0x80) {
                        return false;
                    }
                    break;
                case 14:
                    if (index + 2 >= len) {
                        return false;
                    }
                    if ((data[++index] & 0xC0) != 0x80 || (data[++index] & 0xC0) != 0x80) {
                        return false;
                    }
                    break;
                case 15:
                    if ((data[index] & 0xF8) != 0xF0 || index + 3 >= len) {
                        return false;
                    }
                    if ((data[++index] & 0xC0) != 0x80 || (data[++index] & 0xC0) != 0x80
                            || (data[++index] & 0xC0) != 0x80) {
                        return false;
                    }
                    break;
                default:
                    return false;
            }
            ++index;
        }
        return true;
    }

    // 389. Find the Difference
    // https://leetcode.com/problems/find-the-difference/
    public static char findTheDifference(String s, String t) {
        char result = t.charAt(t.length() - 1);
        for (int i = s.length() - 1; i >= 0; --i) {
            result ^= s.charAt(i) ^ t.charAt(i);
        }
        return result;
    }

    private static char findTheDifference0(String s, String t) {
        int result = 0;
        for (int i = s.length() - 1; i >= 0; --i) {
            result ^= s.charAt(i);
        }
        for (int i = t.length() - 1; i >= 0; --i) {
            result ^= t.charAt(i);
        }
        return (char) result;
    }

    // 371. Sum of Two Integers
    // https://leetcode.com/problems/sum-of-two-integers/
    public int getSum(int a, int b) {
        if (a == 0 || b == 0) {
            return a == 0 ? b : a;
        }
        return getSum(a ^ b, (a & b) << 1);
    }

    // 342. Power of Four
    // https://leetcode.com/problems/power-of-four/
    public boolean isPowerOfFour(int num) {
        return num > 0 && (num & (num - 1)) == 0 && (num & 0x55555555) != 0;
    }

    private static boolean isPowerOfFour0(int num) {
        if (num <= 0 || (num & (num - 1)) != 0) {
            return false;
        }
        return (MathUtils.log2(num) & 1) == 0;
    }

    // 338. Counting Bits
    // https://leetcode.com/problems/counting-bits/submissions/
    public static int[] countBits(int num) {
        int[] result = Arrays.copyOf(new int[]{0, 1, 1, 2, 1}, num + 1);
        int radix = 2;
        while (num > (radix <<= 1)) {
            int diff = num - radix;
            if (diff >= radix) {
                for (int i = 1; i < radix; ++i) {
                    result[radix + i] = 1 + result[i];
                }
                result[radix + radix] = 1;
            } else {
                for (int i = 1; i <= diff; ++i) {
                    result[radix + i] = 1 + result[i];
                }
            }
        }
        return num >= 4 ? result : Arrays.copyOf(result, num + 1);
    }

    // 318. Maximum Product of Word Lengths
    // https://leetcode.com/problems/maximum-product-of-word-lengths/
    public static int maxProduct(String[] words) {
        int len = words.length;
        int[] masks = new int[len];
        for (int i = 0; i < len; ++i) {
            String word = words[i];
            int mask = 0;
            for (int j = word.length() - 1; j >= 0; --j) {
                mask |= 1 << (word.charAt(j) - 'a');
            }
            masks[i] = mask;
        }
        int maxProduct = 0;
        for (int i = len - 1; i > 0; --i) {
            int mask = masks[i], wordLen = words[i].length();
            for (int j = i - 1; j >= 0; --j) {
                if ((mask & masks[j]) == 0) {
                    int product = wordLen * words[j].length();
                    if (product > maxProduct) {
                        maxProduct = product;
                    }
                }
            }
        }
        return maxProduct;
    }

    // 260. Single Number III
    // https://leetcode.com/problems/single-number-iii/
    public static int[] singleNumberIII(int[] nums) {
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }
        int lowestOneBit = xor & -xor;
        int[] result = new int[2];
        for (int num : nums) {
            if ((num & lowestOneBit) == 0) {
                result[0] ^= num;
            } else {
                result[1] ^= num;
            }
        }
        return result;
    }

    private static int[] singleNumberIII0(int[] nums) {
        HashMap<Integer, Boolean> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.get(num) != null);
        }
        int[] result = new int[2];
        int index = 0;
        for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            if (entry.getValue() == false) {
                result[index++] = entry.getKey();
            }
        }
        return result;
    }

    // 231. Power of Two
    // https://leetcode.com/problems/power-of-two/
    public static boolean isPowerOfTwo(int n) {
        return (n > 0) && (n & (n - 1)) == 0;
    }

    // 201. Bitwise AND of Numbers Range
    // https://leetcode.com/problems/bitwise-and-of-numbers-range/
    public static int rangeBitwiseAnd(int m, int n) {
        int result = 0;
        while (m != n) {
            m >>= 1;
            n >>= 1;
            ++result;
        }
        return m << result;
    }

    private static int rangeBitwiseAnd1(int m, int n) {
        if (m == n) {
            return m;
        }
        int high = Integer.highestOneBit(m);
        if (high <= n >> 1) {
            return 0;
        }
        int result = 0;
        while (high > 0 && (high & m) == (high & n)) {
            result |= high & m;
            high >>= 1;
        }
        return result;
    }

    private static int rangeBitwiseAnd0(int m, int n) {
        if (m == n) {
            return m;
        }
        int result = 0;
        while ((m & n) > 0) {
            int high = Integer.highestOneBit(m);
            if (high <= n >> 1) {
                return result;
            }
            result += high;
            m -= high;
            n -= high;
        }
        return result;
    }

    // 191. Number of 1 Bits
    // https://leetcode.com/problems/number-of-1-bits/
    public static int bitCount(int n) {
        n = n - ((n >>> 1) & 0x55555555);
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);
        n = (n + (n >>> 4)) & 0x0f0f0f0f;
        n = n + (n >>> 8);
        n = n + (n >>> 16);
        return n & 0x3f;
    }

    // 190. Reverse Bits
    // https://leetcode.com/problems/reverse-bits/
    public static int reverseBits(int n) {
        n = (n & 0x55555555) << 1 | (n >>> 1) & 0x55555555;
        n = (n & 0x33333333) << 2 | (n >>> 2) & 0x33333333;
        n = (n & 0x0f0f0f0f) << 4 | (n >>> 4) & 0x0f0f0f0f;
        n = (n << 24) | ((n & 0xff00) << 8) |
                ((n >>> 8) & 0xff00) | (n >>> 24);
        return n;
    }

    private static int reverseBits0(int n) {
        n = ((n >>> 1) & 0x55555555) | ((n & 0x55555555) << 1);
        n = ((n >>> 2) & 0x33333333) | ((n & 0x33333333) << 2);
        n = ((n >>> 4) & 0x0F0F0F0F) | ((n & 0x0F0F0F0F) << 4);
        n = ((n >>> 8) & 0x00FF00FF) | ((n & 0x00FF00FF) << 8);
        n = ((n >>> 16) & 0x0000FFFF) | ((n & 0x0000FFFF) << 16);
        return n;
    }

    // 852. Peak Index in a Mountain Array
    // https://leetcode.com/problems/peak-index-in-a-mountain-array/
    public int peakIndexInMountainArray(int[] A) {
        int len = A.length;
        if (len == 1) {
            return 0;
        }
        if (A[1] > A[0] && A[len - 1] > A[len - 2]) {
            return len - 1;
        }
        if (A[0] > A[1]) {
            return 0;
        }
        int left = 0, right = len - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            int midValue = A[mid];
            if (midValue > A[mid - 1]) {
                if (midValue > A[mid + 1]) {
                    return mid;
                } else {
                    left = mid + 1;
                }
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    // 91. Decode Ways
    // https://leetcode.com/problems/decode-ways/
    public static int numDecodings(String s) {
        if (s.charAt(0) == '0') {
            return 0;
        }
        int len = s.length();
        int prevPrev = 1, prev = 1;
        for (int i = 1; i < len; ++i) {
            int cur = 0;
            int onePlace = s.charAt(i) - '0';
            int twoPlace = (s.charAt(i - 1) - '0') * 10 + onePlace;
            if (twoPlace >= 10 && twoPlace <= 26) {
                cur = prevPrev;
            }
            if (onePlace >= 1 && onePlace <= 9) {
                cur += prev;
            }
            if (cur == 0) {
                return 0;
            }
            prevPrev = prev;
            prev = cur;
        }
        return prev;
    }

    private static int numDecodings2(String s) {
        int len;
        if (s == null || (len = s.length()) == 0 || s.charAt(0) == '0') {
            return 0;
        }
        int prevPrev = 1, prev = 1;
        char first = s.charAt(0), second;
        for (int i = 1; i < len; ++i) {
            int cur = 0;
            second = s.charAt(i);
            if (second == '0') {
                if (first > '2' || first == '0') {
                    return 0;
                }
                cur = prevPrev;
            } else {
                int value = (first - '0') * 10 + (second - '0');
                if (value < 10 || value > 26) {
                    cur = prev;
                } else {
                    cur = prevPrev + prev;
                }
            }
            prevPrev = prev;
            prev = cur;
            first = second;
        }
        return prev;
    }

    private static int numDecodings1(String s) {
        int len;
        if (s == null || (len = s.length()) == 0 || s.charAt(0) == '0') {
            return 0;
        }
        int[] nums = new int[len];
        nums[0] = 1;
        char first = s.charAt(0), second;
        for (int i = 1; i < len; ++i) {
            second = s.charAt(i);
            if (second == '0') {
                if (first > '2' || first == '0') {
                    return 0;
                }
                nums[i] = nums[i - 1] = i > 1 ? nums[i - 2] : 1;
            } else {
                int value = (first - '0') * 10 + (second - '0');
                if (value < 10 || value > 26) {
                    nums[i] = nums[i - 1];
                } else {
                    nums[i] = nums[i - 1] + (i > 1 ? nums[i - 2] : 1);
                }
            }
            first = second;
        }
        return nums[len - 1];
    }

    private static int numDecodings0(String s) {
        int len;
        if (s == null || (len = s.length()) == 0 || s.charAt(0) == '0') {
            return 0;
        }
        int[] nums = new int[len];
        nums[0] = 1;
        char first = s.charAt(0), second;
        for (int i = 1; i < len; ++i) {
            second = s.charAt(i);
            if (second == '0') {
                if (first > '2' || first == '0') {
                    return 0;
                }
                nums[i] = nums[i - 1] = i > 1 ? nums[i - 2] : 1;
            } else if (first == '0' || first > '2') {
                nums[i] = nums[i - 1];
            } else if (first == '2' && second > '6') {
                nums[i] = nums[i - 1];
            } else {
                nums[i] = nums[i - 1] + (i > 1 ? nums[i - 2] : 1);
            }
            first = second;
        }
        return nums[len - 1];
    }

    private static int trap0(int[] heights) {
        int maxHeight = 0;
        for (int height : heights) {
            if (height > maxHeight) {
                maxHeight = height;
            }
        }
        int len = heights.length;
        int trappedNum = 0;
        for (int height = maxHeight; height > 0; --height) {
            int begin = -1;
            while (++begin < len && heights[begin] < height) ;
            while (begin < len) {
                int end = begin;
                while (++end < len && heights[end] < height) ;
                if (end < len) {
                    trappedNum += end - begin - 1;
                }
                begin = end;
            }
        }
        return trappedNum;
    }

    // 40. Combination Sum II
    // https://leetcode.com/problems/combination-sum-ii/
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        int len = candidates.length;
        if (candidates[0] > target) {
            return new ArrayList<List<Integer>>();
        }
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        int size = target / candidates[0] + 1;
        helper(candidates, target, new int[size], 0, 0, 0, len - 1, result);
        return result;
    }

    private void helper(int[] candidates, int target, int[] nums,
                        int index, int sum, int begin, int end, List<List<Integer>> result) {
        for (int i = begin; i <= end; ) {
            int candidate = candidates[i];
            sum += nums[index++] = candidate;
            if (sum >= target) {
                if (sum == target) {
                    List<Integer> list = new ArrayList<>(index);
                    for (int j = 0; j < index; ++j) {
                        list.add(nums[j]);
                    }
                    result.add(list);
                }
                return;
            }
            helper(candidates, target, nums, index, sum, i + 1, end, result);
            while (++i <= end && candidates[i] == candidate) ;
            sum -= candidate;
            --index;
        }
    }

    // 137. Single Number II
    // https://leetcode.com/problems/single-number-ii/
    public static int singleNumberII(int[] nums) {
        int one = 0, two = 0, three;
        for (int num : nums) {
            two |= one & num;
            one ^= num;
            three = ~(one & two);
            one &= three;
            two &= three;
        }
        return one;
    }

    private static int singleNumberII0(int[] array) {
        int len = 0;
        if (array == null || (len = array.length) == 0) {
            throw new IllegalArgumentException();
        }
        int result = 0;
        int[] count = new int[32];
        for (int i = 0; i < 32; i++) {
            for (int j = len - 1; j >= 0; --j) {
                count[i] += (array[j] >> i) & 1;
            }
            result |= (count[i] % 3) << i;
        }
        return result;
    }

    // 136. Single Number
    // https://leetcode.com/problems/single-number/
    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }

    // 90. Subsets II
    // https://leetcode.com/problems/subsets-ii/
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        int len;
        if (nums == null || (len = nums.length) == 0) {
            return new ArrayList<List<Integer>>();
        }
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        subsetsWithDupHelper(nums, new int[len], 0, result, 0, len - 1);
        return result;
    }

    private static void subsetsWithDupHelper(int[] nums, int[] array, int index, List<List<Integer>> result, int begin, int end) {
        List<Integer> list = new ArrayList<>(index);
        for (int i = 0; i < index; ++i) {
            list.add(array[i]);
        }
        result.add(list);
        for (int i = begin; i <= end; ) {
            int num = nums[i];
            array[index++] = num;
            subsetsWithDupHelper(nums, array, index, result, i + 1, end);
            --index;
            while (++i <= end && nums[i] == num) ;
        }
    }

    // 70. Climbing Stairs
    // https://leetcode.com/problems/climbing-stairs/
    public static int climbStairs(int n) {
        if (n < 3) {
            return n;
        }
        int first = 1, second = 2;
        for (int i = 3; i <= n; ++i) {
            int temp = first + second;
            first = second;
            second = temp;
        }
        return second;
    }

    // 64. Minimum Path Sum
    // https://leetcode.com/problems/minimum-path-sum/
    public static int minPathSum(int[][] grid) {
        int rowLen = grid.length, colLen = grid[0].length;
        int[][] nums = new int[rowLen][colLen];
        for (int row = 0; row < rowLen; ++row) {
            for (int col = 0; col < colLen; ++col) {
                if (row == 0 && col == 0) {
                    nums[0][0] = grid[0][0];
                    continue;
                }
                if (row == 0) {
                    nums[0][col] = grid[0][col] + nums[0][col - 1];
                } else if (col == 0) {
                    nums[row][0] = grid[row][0] + nums[row - 1][0];
                } else {
                    nums[row][col] = grid[row][col] + Math.min(nums[row][col - 1], nums[row - 1][col]);
                }
            }
        }
        return nums[rowLen - 1][colLen - 1];
    }

    public static int minPathSum0(int[][] grid) {
        int rowLen = grid.length, colLen = grid[0].length;
        int[][] nums = new int[rowLen][colLen];
        nums[0][0] = grid[0][0];
        for (int col = 1; col < colLen; ++col) {
            nums[0][col] = nums[0][col - 1] + grid[0][col];
        }
        for (int row = 1; row < rowLen; ++row) {
            nums[row][0] = nums[row - 1][0] + grid[row][0];
        }
        for (int row = 1; row < rowLen; ++row) {
            for (int col = 1; col < colLen; ++col) {
                nums[row][col] = grid[row][col] + Math.min(nums[row - 1][col], nums[row][col - 1]);
            }
        }
        return nums[rowLen - 1][colLen - 1];
    }

    // 63. Unique Paths II
    // https://leetcode.com/problems/unique-paths-ii/
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int rowLen = obstacleGrid.length, colLen = obstacleGrid[0].length;
        if (obstacleGrid[rowLen - 1][colLen - 1] == 1 || obstacleGrid[0][0] == 1) {
            return 0;
        }
        int[][] nums = new int[rowLen][colLen];
        for (int col = 0; col < colLen; ++col) {
            if (obstacleGrid[0][col] == 0) {
                nums[0][col] = 1;
            } else {
                break;
            }
        }
        for (int row = 1; row < rowLen; ++row) {
            if (obstacleGrid[row][0] == 0) {
                nums[row][0] = 1;
            } else {
                break;
            }
        }
        for (int row = 1; row < rowLen; ++row) {
            for (int col = 1; col < colLen; ++col) {
                if (obstacleGrid[row][col] == 0) {
                    nums[row][col] = nums[row - 1][col] + nums[row][col - 1];
                } else {
                    nums[row][col] = 0;
                }
            }
        }
        return nums[rowLen - 1][colLen - 1];
    }

    // 69. Sqrt(x)
    // https://leetcode.com/problems/sqrtx/
    public static int mySqrt0(int x) {
        int begin = 1, end = x;
        while (begin < end) {
            int mid = (begin + end) >>> 1;
            if (mid < x / mid) {
                begin = mid + 1;
            } else {
                end = mid;
            }
        }
        return begin > x / begin ? begin - 1 : begin;
    }

    // 62. Unique Paths
    // https://leetcode.com/problems/unique-paths/
    public static int uniquePaths(int m, int n) {
        if (m == 1 || n == 1) {
            return 1;
        }
        int k = Math.min(--m, --n), size = k + 1;
        n = m + n;
        int[] yhs = new int[size];
        yhs[0] = 1;
        yhs[1] = 1;
        for (int i = 2; i <= n; ++i) {
            int[] temp = new int[size];
            for (int j = k; j >= 1; ) {
                temp[j] = yhs[j--] + yhs[j];
            }
            System.arraycopy(temp, 1, yhs, 1, k);
        }
        return yhs[k];
    }

    // 47. Permutations II
    // https://leetcode.com/problems/permutations-ii/
    public static List<List<Integer>> permuteUnique(int[] nums) {
        int size = 1, len = nums.length;
        List<List<Integer>> permList = new ArrayList<>();
        permuteUniqueHelper(nums, permList, 0, len - 1);
        return permList;
    }

    private static void permuteUniqueHelper(int[] array, List<List<Integer>> permList, int begin, int end) {
        if (begin == end) {
            List<Integer> list = new ArrayList<>(end + 1);
            for (int i = 0; i <= end; ++i) {
                list.add(array[i]);
            }
            permList.add(list);
            return;
        }
        outer:
        for (int i = begin, value = array[begin]; i <= end; ++i) {
            int cur = array[i];
            for (int j = i - 1; j >= begin; --j) {
                if (array[j] == cur) {
                    continue outer;
                }
            }
            array[i] = value;
            array[begin] = cur;
            permuteUniqueHelper(array, permList, begin + 1, end);
            array[i] = cur;
            array[begin] = value;
        }
    }

    // 89. Gray Code
    // https://leetcode.com/problems/gray-code/
    public List<Integer> grayCode(int n) {
        if (n == 0) {
            return new ArrayList<Integer>(Arrays.asList(0));
        }
        List<Integer> result = new ArrayList<>(1 << n);
        result.add(0);
        for (int i = 0; i < n; ++i) {
            int radix = 1 << i;
            for (int j = radix - 1; j >= 0; --j) {
                result.add(result.get(j) + radix);
            }
        }
        return result;
    }

    // 81. Search in Rotated Sorted Array II
    // https://leetcode.com/problems/search-in-rotated-sorted-array-ii/submissions/
    public static boolean searchII(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1, midVal = nums[mid];
            if (midVal == target) {
                return true;
            }
            if (midVal > nums[left]) {
                if (target >= nums[left] && target < midVal) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else if (midVal < nums[left]) {
                if (target > midVal && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else {
                ++left;
            }
        }
        return false;
    }

    private static boolean searchII0(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1, midNum = nums[mid];
            if (midNum == target) {
                return true;
            }
            if (midNum > target) {
                if (target >= nums[left]) {
                    return binarySearch(nums, left, mid - 1, target) > -1;
                } else {
                    if (nums[left] < midNum) {
                        left = mid + 1;
                    } else {
                        ++left;
                    }
                }
            } else {
                if (target <= nums[right]) {
                    return binarySearch(nums, mid, right, target) > -1;
                } else {
                    if (nums[right] > midNum) {
                        right = mid - 1;
                    } else {
                        --right;
                    }
                }
            }
        }
        return false;
    }

    // 80. Remove Duplicates from Sorted Array II
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/
    public static int removeDuplicatesII(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int i = 1;
        for (int j = 2; j < nums.length; j++) {
            if (nums[j] != nums[i - 1]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }

    private static int removeDuplicatesII0(int[] nums) {
        int len = nums.length;
        int slow = 0, fast = 0, size = 0;
        while (fast < len) {
            int num = nums[fast], begin = fast;
            while (++fast < len && nums[fast] == num) ;
            if (fast - begin >= 2) {
                size += 2;
                nums[slow++] = nums[slow++] = num;
            } else {
                size += 1;
                nums[slow++] = num;
            }
        }
        return size;
    }

    //77. Combinations
    // https://leetcode.com/problems/combinations/
    public static List<List<Integer>> combine(int n, int k) {
        if (k == 0) {
            return new ArrayList<>();
        }
        List<List<Integer>> combList = new ArrayList<>();
        int[] array = new int[n];
        for (int i = 0; i < n; ++i) {
            array[i] = i + 1;
        }
        nchoosek(array, n, new int[k], k, 0, 0, combList);
        return combList;
    }

    private static void nchoosek(int[] array, int n, int[] comb, int k,
                                 int aIndex, int cIndex, List<List<Integer>> combList) {
        if (cIndex == k) {
            List<Integer> list = new ArrayList<>(k);
            for (int i = 0; i < k; ++i) {
                list.add(comb[i]);
            }
            combList.add(list);
            return;
        }
        for (int i = aIndex, limit = n - (k - cIndex); i <= limit; i++) {
            comb[cIndex++] = array[i];
            nchoosek(array, n, comb, k, i + 1, cIndex, combList);
            cIndex--;
        }
    }

    // 75. Sort Colors
    // https://leetcode.com/problems/sort-colors/
    public static void sortColors(int[] nums) {
        int len;
        if (nums == null || (len = nums.length) < 2) {
            return;
        }
        int first = nums[0], pivot = nums[0] = 1;
        int left = 0, right = len - 1;
        for (int index = 1; index <= right; ) {
            int value = nums[index];
            if (value < pivot) {
                nums[index++] = nums[left];
                nums[left++] = value;
            } else if (value > pivot) {
                nums[index] = nums[right];
                nums[right--] = value;
            } else {
                index++;
            }
        }
        if (first == 0) {
            nums[left] = 0;
        } else if (first == 2) {
            nums[left] = nums[right];
            nums[right] = 2;
        }
    }

    // 74. Search a 2D Matrix
    // https://leetcode.com/problems/search-a-2d-matrix/
    public static boolean searchMatrix(int[][] matrix, int target) {
        int rowLen;
        if (matrix == null || (rowLen = matrix.length) == 0) {
            return false;
        }
        int colLen = matrix[0].length;
        for (int row = rowLen - 1, col = 0; row >= 0 && col < colLen; ) {
            int value = matrix[row][col];
            if (value < target) {
                ++col;
            } else if (value > target) {
                --row;
            } else {
                return true;
            }
        }
        return false;
    }

    // 73. Set Matrix Zeroes
    // https://leetcode.com/problems/set-matrix-zeroes/
    public static void setZeroes(int[][] matrix) {
        int rowLen;
        if (matrix == null || (rowLen = matrix.length) == 0) {
            return;
        }
        int colLen = matrix[0].length;
        boolean firstColZero = false;
        for (int row = 0; row < rowLen; ++row) {
            if (matrix[row][0] == 0) {
                firstColZero = true;
            }
            for (int col = 1; col < colLen; ++col) {
                if (matrix[row][col] == 0) {
                    matrix[0][col] = 0;
                    matrix[row][0] = 0;
                }
            }
        }
        for (int row = rowLen - 1; row >= 0; --row) {
            for (int col = colLen - 1; col > 0; --col) {
                if (matrix[0][col] == 0 || matrix[row][0] == 0) {
                    matrix[row][col] = 0;
                }
            }
            if (firstColZero) {
                matrix[row][0] = 0;
            }
        }
    }

    private static void setZeroes1(int[][] matrix) {
        int rowLen;
        if (matrix == null || (rowLen = matrix.length) == 0) {
            return;
        }
        int colLen = matrix[0].length;
        boolean exist = false;
        for (int col = 0; col < colLen; ++col) {
            if (matrix[0][col] == 0) {
                exist = true;
                break;
            }
        }
        for (int row = 1; row < rowLen; ++row) {
            for (int col = 0; col < colLen; ++col) {
                if (matrix[row][col] == 0) {
                    matrix[0][col] = 0;
                    matrix[row][0] = 0;
                }
            }
            if (matrix[row][0] == 0) {
                for (int col = 0; col < colLen; ++col) {
                    matrix[row][col] = 0;
                }
            }
        }
        for (int col = 0; col < colLen; ++col) {
            if (matrix[0][col] == 0) {
                for (int row = 0; row < rowLen; ++row) {
                    matrix[row][col] = 0;
                }
            }
        }
        if (exist) {
            for (int col = colLen - 1; col >= 0; --col) {
                matrix[0][col] = 0;
            }
        }
    }

    private static void setZeroes0(int[][] matrix) {
        int rowLen;
        if (matrix == null || (rowLen = matrix.length) == 0) {
            return;
        }
        int colLen = matrix[0].length;
        boolean[] rows = new boolean[rowLen];
        boolean[] cols = new boolean[colLen];
        for (int row = 0; row < rowLen; ++row) {
            for (int col = 0; col < colLen; ++col) {
                if (matrix[row][col] == 0) {
                    rows[row] = true;
                    cols[col] = true;
                }
            }
        }
        for (int row = 0; row < rowLen; ++row) {
            if (rows[row]) {
                for (int col = 0; col < colLen; ++col) {
                    matrix[row][col] = 0;
                }
            }
        }
        for (int col = 0; col < colLen; ++col) {
            if (cols[col]) {
                for (int row = 0; row < rowLen; ++row) {
                    matrix[row][col] = 0;
                }
            }
        }
    }

    // 59. Spiral Matrix II
    // https://leetcode.com/problems/spiral-matrix-ii/
    public static int[][] generateMatrix(int n) {
        if (n < 1) {
            return new int[][]{};
        }
        int[][] matrix = new int[n][n];
        int rowLen = n, colLen = n;
        int rowLimit = (rowLen & 1) == 1 ? rowLen >> 1 : (rowLen >> 1) - 1;
        int colLimit = (colLen & 1) == 1 ? colLen >> 1 : (colLen >> 1) - 1;
        int row = 0, maxRow = rowLen - 1;
        int col = 0, maxCol = colLen - 1;
        int count = 0;
        while (row <= rowLimit && col <= colLimit) {
            for (int j = col; j <= maxCol; ++j) { // top
                matrix[row][j] = ++count;
            }
            for (int i = ++row; i <= maxRow; ++i) { // lower-right
                matrix[i][maxCol] = ++count;
            }
            if (row <= maxRow) {
                for (int j = --maxCol; j >= col; --j) { // down
                    matrix[maxRow][j] = ++count;
                }
            }
            if (col <= maxCol) {
                for (int i = --maxRow; i >= row; --i) { // upper-left
                    matrix[i][col] = ++count;
                }
            }
            ++col;
        }
        return matrix;
    }

    // 58. Length of Last Word
    // https://leetcode.com/problems/length-of-last-word/
    public static int lengthOfLastWord(String s) {
        int index = s.length();
        while (--index >= 0 && s.charAt(index) == ' ') ;
        if (index == -1) {
            return 0;
        }
        int count = index;
        while (--index >= 0 && s.charAt(index) != ' ') ;
        return count - index;
    }

    // 57. Insert Interval
    // https://leetcode.com/problems/insert-interval/
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        int len;
        if (intervals == null || (len = intervals.length) == 0) {
            return new int[][]{newInterval};
        }
        sort(intervals, 0, len - 1);
        int[][] result = new int[len + 1][2];
        int index = 0, left = newInterval[0], right = newInterval[1];
        for (int i = 0; ; ) {
            if (right < intervals[i][0]) {
                result[index++] = newInterval;
                System.arraycopy(intervals, i, result, index, len - i);
                return Arrays.copyOf(result, index + len - i);
            }
            if (left > intervals[i][1]) {
                result[index++] = intervals[i++];
                if (i == len) {
                    result[index++] = newInterval;
                    return Arrays.copyOf(result, index);
                }
                continue;
            }
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            for (int j = i + 1; j < len; ++j) {
                if (newInterval[1] < intervals[j][0]) {
                    result[index++] = newInterval;
                    System.arraycopy(intervals, j, result, index, len - j);
                    return Arrays.copyOf(result, index + len - j);
                }
                if (newInterval[1] <= intervals[j][1]) {
                    newInterval[1] = intervals[j][1];
                }
            }
            result[index++] = newInterval;
            return Arrays.copyOf(result, index);
        }
    }

    // 56. Merge Intervals
    // https://leetcode.com/problems/merge-intervals/
    public static int[][] merge(int[][] intervals) {
        int len;
        if (intervals == null || (len = intervals.length) < 2) {
            return intervals;
        }
        sort(intervals, 0, len - 1);
        int[][] result = new int[len][];
        int index = 0;
        result[0] = intervals[0];
        for (int i = 1; i < len; ++i) {
            if (intervals[i][0] > result[index][1]) {
                result[++index] = intervals[i];
            } else {
                result[index][1] = Math.max(intervals[i][1], result[index][1]);
            }
        }
        return Arrays.copyOf(result, index + 1);
    }

    private static void sort(int[][] matrix, int begin, int end) {
        if (begin >= end) {
            return;
        }
        int mid = (begin + end) >>> 1;
        sort(matrix, begin, mid);
        sort(matrix, mid + 1, end);
        merge(matrix, begin, mid, end);
    }

    private static void merge(int[][] matrix, int begin, int mid, int end) {
        int left = begin, leftLimit = mid + 1;
        int right = leftLimit, rightLimit = end + 1;
        int index = 0, len = end - begin + 1;
        int[][] mergeMatrix = new int[len][];
        while (left < leftLimit && right < rightLimit) {
            if (matrix[left][0] > matrix[right][0]) {
                mergeMatrix[index++] = matrix[right++];
            } else {
                mergeMatrix[index++] = matrix[left++];
            }
            if (left == leftLimit) {
                System.arraycopy(matrix, right, mergeMatrix, index, len - index);
                break;
            }
            if (right == rightLimit) {
                System.arraycopy(matrix, left, mergeMatrix, index, len - index);
                break;
            }
        }
        System.arraycopy(mergeMatrix, 0, matrix, begin, len);
    }

    // 54. Spiral Matrix
    // https://leetcode.com/problems/spiral-matrix/
    public static List<Integer> spiralOrder(int[][] matrix) {
        int rowLen = 0;
        if (matrix == null || (rowLen = matrix.length) == 0) {
            return new ArrayList<Integer>();
        }
        int colLen = matrix[0].length;
        List<Integer> result = new ArrayList<Integer>(rowLen * colLen);
        int rowLimit = (rowLen & 1) == 1 ? rowLen >> 1 : (rowLen >> 1) - 1;
        int colLimit = (colLen & 1) == 1 ? colLen >> 1 : (colLen >> 1) - 1;
        int row = 0, maxRow = rowLen - 1;
        int col = 0, maxCol = colLen - 1;
        while (row <= rowLimit && col <= colLimit) {
            for (int j = col; j <= maxCol; ++j) { // top
                result.add(matrix[row][j]);
            }
            for (int i = ++row; i <= maxRow; ++i) { // lower-right
                result.add(matrix[i][maxCol]);
            }
            if (row <= maxRow) {
                for (int j = --maxCol; j >= col; --j) { // down
                    result.add(matrix[maxRow][j]);
                }
            }
            if (col <= maxCol) {
                for (int i = --maxRow; i >= row; --i) { // upper-left
                    result.add(matrix[i][col]);
                }
            }
            ++col;
        }
        return result;
    }

    // 49. Group Anagrams
    // https://leetcode.com/problems/group-anagrams/
    // This solution is trick but not rigorous.(39个a和34个a会出现冲突)
    public static List<List<String>> groupAnagrams(String[] strs) {
        final byte[] prime = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101};
        HashMap<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        List<List<String>> result = new ArrayList<List<String>>();
        for (String str : strs) {
            int prod = 1;
            for (int j = str.length() - 1; j >= 0; --j) {
                prod *= prime[str.charAt(j) - 'a'];
            }
            List<String> list = map.get(prod);
            if (list == null) {
                list = new ArrayList<String>();
                map.put(prod, list);
                result.add(list);
            }
            list.add(str);
        }
        return result;
    }

    private static List<List<String>> groupAnagrams1(String[] strs) {
        int strLen = strs.length;
        List<List<String>> result = new ArrayList<>();
        HashMap<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] letters = str.toCharArray();
            Arrays.sort(letters);
            String s = new String(letters);
            List<String> list = map.get(s);
            if (list == null) {
                list = new ArrayList<String>();
                map.put(s, list);
                result.add(list);
            }
            list.add(str);
        }
        return result;
    }

    private static List<List<String>> groupAnagrams0(String[] strs) {
        int strLen = strs.length;
        int[][] letterCounts = new int[strLen][26];
        for (int i = 0; i < strLen; ++i) {
            String str = strs[i];
            for (int j = 0, len = str.length(); j < len; ++j) {
                ++letterCounts[i][str.charAt(j) - 97];
            }
        }
        boolean[] exist = new boolean[strLen];
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < strLen; ++i) {
            if (exist[i] == false) {
                List<String> list = new ArrayList<>();
                list.add(strs[i]);
                for (int j = i + 1; j < strLen; ++j) {
                    if (exist[j] == false && equals(letterCounts[i], letterCounts[j])) {
                        exist[j] = true;
                        list.add(strs[j]);
                    }
                }
                result.add(list);
            }
        }
        return result;
    }

    private static boolean equals(int[] array1, int[] array2) {
        for (int i = array1.length - 1; i >= 0; --i) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    // 48. Rotate Image
    // https://leetcode.com/problems/rotate-image/
    public static void rotate(int[][] image) {
        int len = image.length;
        for (int begin = 0, halfLen = len >> 1; begin < halfLen; ++begin) {
            int subLen = len - begin * 2;
            for (int col = 0, colLimit = subLen - 1; col < colLimit; ++col) {
                int value = image[begin][col + begin], i = col, j = colLimit;
                while (!(i == 0 && j == col)) {
                    int temp = image[i + begin][j + begin];
                    image[i + begin][j + begin] = value;
                    value = temp;
                    temp = i;
                    i = j;
                    j = colLimit - temp;
                }
                image[begin][col + begin] = value;
            }
        }
    }

    // 38. Count and Say
    // https://leetcode.com/problems/count-and-say/
    public String countAndSay(int n) {
        String s = "1";
        for (int i = 2; i <= n; ++i) {
            StringBuilder say = new StringBuilder();
            for (int len = s.length(), j = 0; j < len; ) {
                char count = 48, target = s.charAt(j);
                while (j < len && s.charAt(j) == target) {
                    ++count;
                    ++j;
                }
                say.append(count).append(target);
            }
            s = say.toString();
        }
        return s;
    }

    // 36. Valid Sudoku
    // https://leetcode.com/problems/valid-sudoku/
    public boolean isValidSudoku(char[][] board) {
        // Horizontal direction
        for (int row = 0; row < 9; ++row) {
            boolean[] exist = new boolean[10];
            for (int col = 0; col < 9; ++col) {
                if (board[row][col] != '.') {
                    if (exist[board[row][col] - '0']) {
                        return false;
                    } else {
                        exist[board[row][col] - '0'] = true;
                    }
                }
            }
        }
        // Vertical direction
        for (int col = 0; col < 9; ++col) {
            boolean[] exist = new boolean[10];
            for (int row = 0; row < 9; ++row) {
                if (board[row][col] != '.') {
                    if (exist[board[row][col] - '0']) {
                        return false;
                    } else {
                        exist[board[row][col] - '0'] = true;
                    }
                }
            }
        }
        // sub-boxes
        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) {
                boolean[] exist = new boolean[10];
                for (int i = row; i < row + 3; ++i) {
                    for (int j = col; j < col + 3; ++j) {
                        if (board[i][j] != '.') {
                            if (exist[board[i][j] - '0']) {
                                return false;
                            } else {
                                exist[board[i][j] - '0'] = true;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    // 34. Find First and Last Position of Element in Sorted Array
    // https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
    public int[] searchRange(int[] nums, int target) {
        int begin = 0, len = nums.length, end = len - 1;
        while (begin <= end) {
            int mid = (begin + end) >>> 1;
            int midNum = nums[mid];
            if (midNum > target) {
                end = mid - 1;
            } else if (midNum < target) {
                begin = mid + 1;
            } else {
                int left = mid, right = mid;
                while (--left >= 0 && nums[left] == target) ;
                while (++right < len && nums[right] == target) ;
                return new int[]{left + 1, right - 1};
            }
        }
        return new int[]{-1, -1};
    }

    // 33. Search in Rotated Sorted Array
    // https://leetcode.com/problems/search-in-rotated-sorted-array/
    public static int search(int[] nums, int target) {
        int len = nums.length;
        int begin = 0, end = len - 1;
        while (begin <= end) { // rotated
            if (nums[begin] < nums[end]) {
                return binarySearch(nums, begin, end, target);
            }
            int mid = (begin + end) >>> 1;
            int midVal = nums[mid];
            if (midVal == target) {
                return mid;
            }
            if (midVal >= nums[begin]) {
                if (target >= nums[begin] && target <= midVal) {
                    return binarySearch(nums, begin, mid, target);
                } else {
                    begin = mid + 1;
                }
            } else {
                if (target >= midVal && target <= nums[end]) {
                    return binarySearch(nums, mid, end, target);
                } else {
                    end = mid - 1;
                }
            }
        }
        return -1;
    }

    private static int binarySearch(int[] nums, int begin, int end, int target) {
        while (begin <= end) {
            int mid = (begin + end) >>> 1;
            if (nums[mid] > target) {
                --end;
            } else if (nums[mid] < target) {
                ++begin;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // 31. Next Permutation
    // https://leetcode.com/problems/next-permutation/
    public static void nextPermutation(int[] nums) {
        int len = nums.length;
        if (len < 2) {
            return;
        }
        int replace = len - 1;
        while (replace >= 1 && nums[replace] <= nums[--replace]) ;
        if (nums[replace] >= nums[replace + 1]) {
            reverse(nums, 0, len - 1);
            return;
        }
        int replaced = replace;
        while (++replaced < len && nums[replaced] > nums[replace]) ;
        ArrayUtils.swap(nums, replace++, --replaced);
        ArrayUtils.reverse(nums, replace, len - 1);
    }

    // 22. Generate Parentheses
    // https://leetcode.com/problems/generate-parentheses/
    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        if (n <= 0) {
            return result;
        }
        dfs(n, n, new StringBuilder(), result);
        return result;
    }

    private static void dfs(int left, int right, StringBuilder sb, List<String> result) {
        if (left == 0 && right == 0) {
            result.add(sb.toString());
            return;
        }
        if (left > 0) {
            dfs(left - 1, right, sb.append("("), result);
            sb.deleteCharAt(sb.length() - 1);
        }
        if (right > left) {
            dfs(left, right - 1, sb.append(")"), result);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    public static List<String> generateParenthesis0(int n) {
        if (n < 1) {
            return new ArrayList<String>(Arrays.asList(""));
        }
        ArrayDeque<String> result = new ArrayDeque<>();
        result.addLast("()");
        int count = 1;
        for (int i = 2; i <= n; ++i) {
            while (count-- > 0) {
                String value = result.pollFirst();
                result.addLast("()" + value);
                for (int index = -1, limit = value.length() - 1; index < limit; ) {
                    while (++index <= limit && value.charAt(index) == '(') ;
                    result.addLast("(" + value.substring(0, index) + ")" + value.substring(index, limit + 1));
                    while (++index <= limit && value.charAt(index) == ')') ;
                }
            }
            count = result.size();
        }
        return new ArrayList<String>(result);
    }

    private static void threeSumHelper(int[] nums, List<List<Integer>> result, int... vs) {
        int first = vs[0], begin = vs[1], end = vs[2], sumTarget = vs[3];
        for (int i = begin; i < end; i++) {
            while (i > begin && i < end && nums[i] == nums[i - 1]) {
                i++;
            } // avoid duplicates
            if (i == end) {
                break;
            }
            int left = i + 1, right = end - 1, target = sumTarget - nums[i];
            while (left < right) {
                int sum = nums[left] + nums[right];
                if (sum < target) {
                    left++;
                } else if (sum > target) {
                    right--;
                } else {
                    result.add(Arrays.asList(first, nums[i], nums[left++], nums[right--]));
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    } // avoid duplicates
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    } // avoid duplicates
                }
            }
        }
    }

    public static List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0, limit = len - 3; i < limit; ++i) {
            int first = nums[i];
            if (i > 0 && first == nums[i - 1]) {
                continue;
            }
            threeSumHelper(nums, result, first, i + 1, len, target - first);
        }
        return new ArrayList<List<Integer>>(result);
    }

    private static List<List<Integer>> fourSum0(int[] nums, int target) {
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> result = new ArrayList<>();
        f:
        for (int first = 0, fLimit = len - 3; first < fLimit; ++first) {
            int fNum = nums[first];
            if (first > 0) {
                for (int prev = nums[first - 1]; first < fLimit && nums[first] == prev; ++first) ;
                if (first < fLimit) {
                    fNum = nums[first];
                } else {
                    break;
                }
            }
            if (target < fNum * 4) {
                break;
            }
            s:
            for (int second = first + 1, sBegin = second, sLimit = len - 2; second < sLimit; ++second) {
                int sNum = nums[second];
                if (second > sBegin) {
                    for (int prev = nums[second - 1]; second < sLimit && nums[second] == prev; ++second) ;
                    if (second < sLimit) {
                        sNum = nums[second];
                    } else {
                        break;
                    }
                }
                if (target < (fNum + sNum) * 2) {
                    continue;
                }
                t:
                for (int third = second + 1, tBegin = third, tLimit = len - 1; third < tLimit; ++third) {
                    int tNum = nums[third];
                    if (third > tBegin) {
                        for (int prev = nums[third - 1]; third < tLimit && nums[third] == prev; ++third) ;
                        if (third < tLimit) {
                            tNum = nums[third];
                        } else {
                            break;
                        }
                    }
                    if (target < fNum + sNum + tNum * 2) {
                        continue s;
                    }
                    for (int fourth = third + 1; fourth < len; ++fourth) {
                        int sum = fNum + sNum + tNum + nums[fourth];
                        if (sum >= target) {
                            if (sum == target) {
                                result.add(createList(fNum, sNum, tNum, nums[fourth]));
                            }
                            continue t;
                        }
                    }
                }
            }
        }
        return result;
    }

    private static List<Integer> createList(int... vs) {
        List<Integer> list = new ArrayList<>(4);
        list.add(vs[0]);
        list.add(vs[1]);
        list.add(vs[2]);
        list.add(vs[3]);
        return list;
    }


    // 60. Permutation Sequence
    // https://leetcode.com/problems/permutation-sequence/
    public static String getPermutation(int n, int k) {
        boolean[] choosen = new boolean[n + 1];
        char[] result = new char[n];
        int factorial = 1;
        for (int i = n - 1; i > 1; --i) {
            factorial *= i;
        }
        int index = 0;
        for (int i = n - 1; i >= 1; --i) {
            int num = k > 0 ? (k - 1) / factorial + 1 : 1;
            for (int j = 1, count = 0; j <= n; ++j) {
                if (choosen[j] == false && ++count == num) {
                    result[index++] = (char) (j + '0');
                    choosen[j] = true;
                    break;
                }
            }
            k = k - (num - 1) * factorial;
            factorial /= i;
        }
        for (int j = 1, count = 0; j <= n; ++j) {
            if (choosen[j] == false) {
                result[index] = (char) (j + '0');
                break;
            }
        }
        return new String(result);
    }

    // 43. Multiply Strings
    // https://leetcode.com/problems/multiply-strings/
    public static String multiply(String num1, String num2) {
        int len1 = num1.length(), len2 = num2.length();
        int len = len1 + len2;
        int[] multiplicand = new int[len1];
        for (int i = 0; i < len1; ++i) {
            multiplicand[i] = num1.charAt(i) - '0';
        }
        char[] result = new char[len];
        for (int i = len2 - 1; i >= 0; --i) {
            int multiplier = num2.charAt(i) - '0', index = len1 + i;
            for (int j = len1 - 1; j >= 0; --j) {
                result[index--] += multiplicand[j] * multiplier;
            }
        }
        int carry = 0;
        for (int i = len - 1; i >= 0; --i) {
            int value = result[i] + carry;
            if (value > 9) {
                carry = value / 10;
                value %= 10;
            } else {
                carry = 0;
            }
            result[i] = (char) value;
        }
        int index = -1;
        while (++index < len && result[index] == 0) ;
        for (int i = 0; i < len; ++i) {
            result[i] += '0';
        }
        return index == len ? "0" : new String(result, index, len - index);
    }

    private static String multiply0(String num1, String num2) {
        int len1 = num1.length(), len2 = num2.length();
        int len = len1 + len2;
        int[] multiplicand = new int[len1];
        for (int i = 0; i < len1; ++i) {
            multiplicand[i] = num1.charAt(i) - '0';
        }
        char[] result = new char[len];
        for (int i = len2 - 1; i >= 0; --i) {
            int multiplier = num2.charAt(i) - '0';
            int multiplyCarry = 0, sumCarry = 0;
            int index = len1 + i;
            for (int j = len1 - 1; j >= 0; --j) {
                int prod = multiplicand[j] * multiplier + multiplyCarry;
                if (prod < 10) {
                    multiplyCarry = 0;
                } else {
                    multiplyCarry = prod / 10;
                    prod = prod % 10;
                }
                int sum = result[index] + prod + sumCarry;
                if (sum < 10) {
                    sumCarry = 0;
                } else {
                    sumCarry = 1;
                    sum -= 10;
                }
                result[index--] = (char) sum;
            }
            if (multiplyCarry > 0) {
                int sum = result[index] + multiplyCarry + sumCarry;
                if (sum < 10) {
                    sumCarry = 0;
                } else {
                    sumCarry = 1;
                    sum -= 10;
                }
                result[index--] = (char) sum;
            }
            while (sumCarry > 0) {
                int sum = result[index] + sumCarry;
                if (sum < 10) {
                    sumCarry = 0;
                } else {
                    sumCarry = 1;
                    sum -= 10;
                }
                result[index--] = (char) sum;
            }
        }
        int index = -1;
        while (++index < len && result[index] == 0) ;
        for (int i = 0; i < len; ++i) {
            result[i] += '0';
        }
        return index == len ? "0" : new String(result, index, len - index);
    }

    // 961. N-Repeated Element in Size 2N Array
    // https://leetcode.com/problems/n-repeated-element-in-size-2n-array/
    public static int repeatedNTimes(int[] A) {
        for (int gap = 1, len = A.length; gap <= 2; ++gap) {
            for (int i = 0, limit = len - gap; i < limit; ++i) {
                if (A[i] == A[i + gap]) {
                    return A[i];
                }
            }
        }
        throw null;
    }

    private static int repeatedNTimes1(int[] A) {
        boolean[] exists = new boolean[10000];
        for (int a : A) {
            if (exists[a]) {
                return a;
            }
            exists[a] = true;
        }
        throw null;
    }

    private static int repeatedNTimes0(int[] A) {
        HashSet<Integer> set = new HashSet<>();
        for (int a : A) {
            if (set.contains(a)) {
                return a;
            }
            set.add(a);
        }
        throw null;
    }

    // 39. Combination Sum
    // https://leetcode.com/problems/combination-sum/
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        int size = target / candidates[0];
        if (size == 0) {
            return new ArrayList<List<Integer>>(0);
        }
        int[] nums = new int[size];
        List<List<Integer>> sumList = new ArrayList<>();
        combinationSumHelper(candidates, sumList, 0, target, nums, 0, 0, candidates.length);
        return sumList;
    }

    private static void combinationSumHelper(int[] candidates, List<List<Integer>> sumList,
                                             int sum, int target, int[] nums, int index, int begin, int end) {
        if (sum == target) {
            List<Integer> list = new ArrayList<>(index);
            for (int i = 0; i < index; ++i) {
                list.add(nums[i]);
            }
            sumList.add(list);
            return;
        }
        for (int i = begin; i < end; ++i) {
            int value = candidates[i];
            sum += value;
            if (sum > target) {
                return;
            }
            nums[index] = value;
            combinationSumHelper(candidates, sumList, sum, target, nums, index + 1, i, end);
            sum -= value;
        }
    }

    //292. Nim Game
    // https://leetcode.com/problems/nim-game/
    public static boolean canWinNim(int n) {
        return n % 4 != 0;
    }

    // 46. Permutations
    // https://leetcode.com/problems/permutations/
    public static List<List<Integer>> permute(int[] nums) {
        int size = 1, len = nums.length;
        for (int i = 2; i <= len; ++i) {
            size *= i;
        }
        List<List<Integer>> permList = new ArrayList<>(size);
        permHelper(nums, permList, 0, len - 1);
        return permList;
    }

    private static void permHelper(int[] array, List<List<Integer>> permList, int begin, int end) {
        if (begin == end) {
            List<Integer> list = new ArrayList<>(end + 1);
            for (int i = 0; i <= end; ++i) {
                list.add(array[i]);
            }
            permList.add(list);
            return;
        }
        for (int i = begin, value = array[begin]; i <= end; ++i) {
            int cur = array[i];
            array[i] = value;
            array[begin] = cur;
            permHelper(array, permList, begin + 1, end);
            array[i] = cur;
            array[begin] = value;
        }
    }

    private static List<List<Integer>> permute0(int[] array) {
        int len = array.length;
        if (len < 2) {
            if (len == 0) {
                return new ArrayList<List<Integer>>(0);
            }
            List<List<Integer>> permList = new ArrayList<List<Integer>>(1);
            permList.add(Arrays.asList(array[0]));
            return permList;
        }
        int size = 1;
        for (int i = 2; i <= len; ++i) {
            size *= i;
        }
        List<List<Integer>> permList = new ArrayList<>(len);
        Arrays.sort(array);
        int remaining = len - 1;
        while (true) {
            List<Integer> list = new ArrayList<>(len);
            for (int i = 0; i < len; ++i) {
                list.add(array[i]);
            }
            permList.add(list);
            int replace = remaining;
            while (replace >= 1 && array[replace] <= array[--replace]) ;
            if (array[replace] >= array[replace + 1]) {
                break;
            }
            int replaced = replace;
            while (++replaced < len && array[replaced] > array[replace]) ;
            ArrayUtils.swap(array, replace++, --replaced);
            ArrayUtils.reverse(array, replace, remaining);
        }
        return permList;
    }

    // 876. Middle of the Linked List
    // https://leetcode.com/problems/middle-of-the-linked-list/
    public ListNode middleNode(ListNode head) {
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    // 445. Add Two Numbers II
    // https://leetcode.com/problems/add-two-numbers-ii/
    public static ListNode addTwoNumbersII(ListNode l1, ListNode l2) {
        if (l1 == null || l2 == null) {
            return l1 == null ? l2 : l1;
        }
        ArrayDeque<ListNode> stack1 = new ArrayDeque<>();
        for (ListNode iter = l1; iter != null; iter = iter.next) {
            stack1.push(iter);
        }
        ArrayDeque<ListNode> stack2 = new ArrayDeque<>();
        for (ListNode iter = l2; iter != null; iter = iter.next) {
            stack2.push(iter);
        }
        ListNode node = null;
        int carry = 0;
        while (!stack1.isEmpty() || !stack2.isEmpty()) {
            int sum = carry;
            if (!stack1.isEmpty()) {
                sum += stack1.pop().val;
            }
            if (!stack2.isEmpty()) {
                sum += stack2.pop().val;
            }
            if (sum > 9) {
                carry = 1;
                sum -= 10;
            } else {
                carry = 0;
            }
            ListNode newNode = new ListNode(sum);
            newNode.next = node;
            node = newNode;
        }
        if (carry == 1) {
            ListNode newNode = new ListNode(1);
            newNode.next = node;
            node = newNode;
        }
        return node;
    }

    // 328. Odd Even Linked List
    // https://leetcode.com/problems/odd-even-linked-list/
    public static ListNode oddEvenList(ListNode head) {
        ListNode first = new ListNode(0), second = new ListNode(0);
        first.next = second;
        ListNode oddNode = first, evenNode = second;
        for (ListNode iter = head, next = null; iter != null; iter = next.next) {
            next = iter.next;
            oddNode = oddNode.next = iter;
            oddNode.next = second;
            evenNode = evenNode.next = next;
            if (next == null) {
                break;
            }
        }
        oddNode.next = oddNode.next.next;
        return first.next;
    }

    // 234. Palindrome Linked List
    // https://leetcode.com/problems/palindrome-linked-list/
    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        if (head.next.next == null) {
            return head.val == head.next.val;
        }
        ListNode p1 = head;
        ListNode p2 = head.next;
        while (p2.next != null) {
            if (p1.val == p2.next.val) {
                if (p2.next.next != null) {
                    return false;
                }
                p2.next = null;
                p1 = p1.next;
                p2 = p1.next;
                if (p2 == null || p1.val == p2.val)
                    return true;
            } else {
                p2 = p2.next;
            }
        }
        return false;
    }

    private static boolean isPalindrome0(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        ArrayDeque<ListNode> stack = new ArrayDeque<>();
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            stack.push(slow);
            slow = slow.next;
        }
        if (fast != null) {
            slow = slow.next;
        }
        for (ListNode iter = slow; iter != null; iter = iter.next) {
            if (stack.isEmpty() || stack.pop().val != iter.val) {
                return false;
            }
        }
        return stack.isEmpty();
    }

    // 206. Reverse Linked List
    // https://leetcode.com/problems/reverse-linked-list/
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null, cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    // 203. Remove Linked List Elements
    // https://leetcode.com/problems/remove-linked-list-elements/
    public static ListNode removeElements(ListNode head, int val) {
        ListNode root = new ListNode(0);
        root.next = head;
        for (ListNode iter = root; iter != null; ) {
            while (iter.next != null && iter.next.val != val) {
                iter = iter.next;
            }
            if (iter.next != null) {
                iter.next = iter.next.next;
            } else {
                iter = iter.next;
            }
        }
        return root.next;
    }

    // 160. Intersection of Two Linked Lists
    // https://leetcode.com/problems/intersection-of-two-linked-lists/submissions/
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        int lenA = 0;
        for (ListNode iter = headA; iter != null; iter = iter.next) {
            ++lenA;
        }
        int lenB = 0;
        for (ListNode iter = headB; iter != null; iter = iter.next) {
            ++lenB;
        }
        if (lenA > lenB) {
            int diff = lenA - lenB;
            while (diff-- > 0) {
                headA = headA.next;
            }
        } else {
            int diff = lenB - lenA;
            while (diff-- > 0) {
                headB = headB.next;
            }
        }
        ListNode result = headA;
        while (headA != headB) {
            result = headA = headA.next;
            headB = headB.next;
        }
        return result;
    }

    private static ListNode getIntersectionNode1(ListNode headA, ListNode headB) {
        HashSet<ListNode> set = new HashSet<>();
        for (ListNode iter = headA; iter != null; iter = iter.next) {
            set.add(iter);
        }
        for (ListNode iter = headB; iter != null; iter = iter.next) {
            if (set.contains(iter)) {
                return iter;
            }
        }
        return null;
    }

    private static ListNode getIntersectionNode0(ListNode headA, ListNode headB) {
        if (headA == headB) {
            return headA;
        }
        ArrayDeque<ListNode> stack1 = new ArrayDeque<>();
        for (ListNode iter = headA; iter != null; iter = iter.next) {
            stack1.push(iter);
        }
        ArrayDeque<ListNode> stack2 = new ArrayDeque<>();
        for (ListNode iter = headB; iter != null; iter = iter.next) {
            stack2.push(iter);
        }
        ListNode result = null, cur = null;
        if (stack1.size() == 0 || stack2.size() == 0 ||
                (result = stack1.pop()) != stack2.pop()) {
            return null;
        }
        while (stack1.size() > 0 && stack2.size() > 0) {
            cur = stack1.pop();
            if (cur != stack2.pop()) {
                return result;
            }
            result = cur;
        }
        return result;
    }

    // 148. Sort List
    // https://leetcode.com/problems/sort-list/
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode fast = head, slow = head, prev = null;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            prev = slow;
            slow = slow.next;
        }
        prev.next = null;
        return mergeList(sortList(head), sortList(slow));
    }

    private static ListNode mergeList(ListNode head1, ListNode head2) {
        ListNode root = new ListNode(0), node = root;
        while (head1 != null && head2 != null) {
            if (head1.val > head2.val) {
                node = node.next = head2;
                head2 = head2.next;
            } else {
                node = node.next = head1;
                head1 = head1.next;
            }
        }
        node.next = head1 == null ? head2 : head1;
        return root.next;
    }

    // 147. Insertion Sort List
    // https://leetcode.com/problems/insertion-sort-list/
    public static ListNode insertionSortList(ListNode head) {
        ListNode root = new ListNode(0);
        for (ListNode iter = head, next = null; iter != null; iter = next) {
            next = iter.next;
            int target = iter.val;
            ListNode prev = root;
            while (prev.next != null && prev.next.val < target) {
                prev = prev.next;
            }
            iter.next = prev.next;
            prev.next = iter;
        }
        return root.next;
    }

    private static ListNode insertionSortList0(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        for (ListNode iter = head.next, prev = head; iter != null; ) {
            ListNode next = iter.next;
            int target = iter.val;
            ListNode insertedPrev = null, insertedCur = head;
            while (insertedCur != iter && insertedCur.val <= target) {
                insertedPrev = insertedCur;
                insertedCur = insertedCur.next;
            }
            if (insertedCur != iter) {
                prev.next = next;
                if (insertedPrev == null) {
                    head = iter;
                } else {
                    insertedPrev.next = iter;
                }
                iter.next = insertedCur;
            } else {
                prev = iter;
            }
            iter = next;
        }
        return head;
    }

    // 143. Reorder List
    // https://leetcode.com/problems/reorder-list/
    public static void reorderList(ListNode head) {
        if (head == null) {
            return;
        }
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode cur = slow.next, prev = null;
        slow.next = null;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        ListNode first = head, second = prev;
        while (first != null && second != null) {
            ListNode firstNext = first.next;
            ListNode secondNext = second.next;
            first.next = second;
            first = second.next = firstNext;
            second = secondNext;
        }
    }

    private static void reorderList0(ListNode head) {
        ArrayDeque<ListNode> stack = new ArrayDeque<>();
        for (ListNode iter = head; iter != null; iter = iter.next) {
            stack.push(iter);
        }
        ListNode iter = head, inserted = null;
        while (iter != inserted && (inserted = stack.pop()) != iter) {
            ListNode next = iter.next;
            iter.next = inserted;
            inserted.next = next;
            iter = next;
        }
        if (iter != null) {
            iter.next = null;
        }
    }

    // 142. Linked List Cycle II
    // https://leetcode.com/problems/linked-list-cycle-ii/
    public ListNode detectCycle(ListNode head) {
        ListNode rabbit = head, tortoise = head;
        while (rabbit != null && tortoise != null) {
            rabbit = rabbit.next;
            if (rabbit == null || (tortoise = tortoise.next) == null) {
                return null;
            }
            tortoise = tortoise.next;
            if (tortoise == rabbit) {
                tortoise = head;
                while (tortoise != rabbit) {
                    tortoise = tortoise.next;
                    rabbit = rabbit.next;
                }
                return tortoise;
            }
        }
        return null;
    }

    // 141. Linked List Cycle
    // https://leetcode.com/problems/linked-list-cycle/
    public static boolean hasCycle(ListNode head) {
        if (head == null) {
            return false;
        }
        ListNode rabbit = head, tortoise = head;
        while (rabbit != null && tortoise != null) {
            rabbit = rabbit.next;
            if (rabbit == null || (tortoise = tortoise.next) == null) {
                return false;
            }
            tortoise = tortoise.next;
            if (tortoise == rabbit) {
                return true;
            }
        }
        return false;
    }

    // 138. Copy List with Random Pointer
    // https://leetcode.com/problems/copy-list-with-random-pointer/
    public static Node copyRandomList(Node head) {
        for (Node iter = head; iter != null; ) {
            Node next = iter.next;
            iter = iter.next = new Node(iter.val, null, null);
            iter.next = next;
            iter = next;
        }
        for (Node iter = head; iter != null; iter = iter.next.next) {
            if (iter.random != null) {
                iter.next.random = iter.random.next;
            }
        }
        Node root = new Node();
        for (Node iter = head, copy = root; iter != null; ) {
            Node next = iter.next.next;
            copy = copy.next = iter.next;
            iter.next = next;
            iter = next;
        }
        return root.next;
    }

    private static Node copyRandomList1(Node head) {
        HashMap<Node, Node> map = new HashMap<>();
        for (Node iter = head; iter != null; iter = iter.next) {
            map.put(iter, new Node(iter.val, null, null));
        }
        Node root = new Node();
        for (Node iter = head, copy = root; iter != null; iter = iter.next) {
            Node newNode = map.get(iter);
            newNode.next = map.get(iter.next);
            newNode.random = map.get(iter.random);
            copy = copy.next = newNode;
        }
        return root.next;
    }

    private static Node copyRandomList0(Node head) {
        HashMap<Integer, Node> map = new HashMap<>();
        Node node = head, root = new Node(), copyNode = root;
        for (; node != null; node = node.next) {
            if (map.get(node.val) == null) {
                map.put(node.val, new Node(node.val, null, null));
            }
            if (node.next != null && map.get(node.next.val) == null) {
                map.put(node.next.val, new Node(node.next.val, null, null));
            }
            if (node.random != null && map.get(node.random.val) == null) {
                map.put(node.random.val, new Node(node.random.val, null, null));
            }
        }
        for (node = head; node != null; node = node.next) {
            Node newNode = map.get(node.val);
            if (node.next != null) {
                newNode.next = map.get(node.next.val);
            }
            if (node.random != null) {
                newNode.random = map.get(node.random.val);
            }
            copyNode = copyNode.next = newNode;
        }
        return root.next;
    }

    static class Node {
        public int val;
        public Node next;
        public Node random;

        public Node() {
        }

        public Node(int _val, Node _next, Node _random) {
            val = _val;
            next = _next;
            random = _random;
        }
    }

    // 92. Reverse Linked List II
    // https://leetcode.com/problems/reverse-linked-list-ii/
    public static ListNode reverseBetween(ListNode head, int m, int n) {
        int begin = m;
        ListNode prev = null, cur = head;
        while (--begin > 0) {
            prev = cur;
            cur = cur.next;
        }
        int limit = n - m, count = -1;
        ListNode leftTail = prev, rightTail = cur;
        while (++count <= limit) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        if (leftTail != null) {
            leftTail.next = prev;
        } else {
            head = prev;
        }
        rightTail.next = cur;
        return head;
    }

    // 86. Partition List
    // https://leetcode.com/problems/partition-list/
    public ListNode partition(ListNode head, int x) {
        ListNode lessHead = new ListNode(0);
        ListNode greatHead = new ListNode(0);
        ListNode lessTail = lessHead, greatTail = greatHead;
        ListNode node = head;
        while (node != null) {
            if (node.val < x) {
                lessTail = lessTail.next = node;
            } else {
                greatTail = greatTail.next = node;
            }
            node = node.next;
        }
        greatTail.next = null;
        lessTail.next = greatHead.next;
        return lessHead.next;
    }

    // 82. Remove Duplicates from Sorted List II
    // https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/
    public static ListNode deleteDuplicatesII(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode root = new ListNode(head.val - 1);
        root.next = head;
        ListNode prev = root, cur = null, next = null;
        while (prev != null) {
            cur = prev.next;
            while (cur != null && (next = cur.next) != null && cur.val != next.val) {
                prev = cur;
                cur = next;
            }
            if (cur == null || next == null) {
                break;
            }
            int target = cur.val;
            while ((cur = cur.next) != null && cur.val == target) ;
            prev.next = cur;
            if (cur == null || (next = cur.next) == null) {
                break;
            }
            if (cur.val != next.val) {
                prev = cur;
            }
        }
        return root.next;
    }

    // 83. Remove Duplicates from Sorted List
    // https://leetcode.com/problems/remove-duplicates-from-sorted-list/
    public static ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        head.next = deleteDuplicates(head.next);
        return head.val == head.next.val ? head.next : head;
    }

    private static ListNode deleteDuplicates0(ListNode head) {
        ListNode node = head;
        while (node != null) {
            int target = node.val;
            ListNode next = node;
            while ((next = next.next) != null && next.val == target) ;
            node = node.next = next;
        }
        return head;
    }

    // 61. Rotate List
    // https://leetcode.com/problems/rotate-list
    public static ListNode rotateRight(ListNode head, int k) {
        if (k < 0 || head == null) {
            return null;
        }
        int num = 0;
        ListNode node = head;
        while (node != null) {
            ++num;
            node = node.next;
        }
        k = k % num;
        if (k == 0) {
            return head;
        }
        int moveNum = num - k;
        ListNode tail = head;
        while (--moveNum > 0) {
            tail = tail.next;
        }
        ListNode root = tail.next;
        node = root;
        while (node.next != null) {
            node = node.next;
        }
        node.next = head;
        tail.next = null;
        return root;
    }

    // 28. Implement strStr()
    // https://leetcode.com/problems/implement-strstr/
    public static int strStr(String haystack, String needle) {
        int len1 = haystack.length();
        int len2 = needle.length();
        char[] cs1 = haystack.toCharArray();
        char[] cs2 = needle.toCharArray();
        if (len2 == 0) {
            return 0;
        }
        int limit = len1 - len2;
        char first = cs2[0];
        for (int i = -1; i <= limit; ) {
            while (++i <= limit && cs1[i] != first) ;
            if (i <= limit) {
                int k = 0, j = i;
                while (++k < len2 && cs1[++j] == cs2[k]) ;
                if (k == len2) {
                    return i;
                }
            }
        }
        return -1;
    }

    // 25. Reverse Nodes in k-Group
    // https://leetcode.com/problems/reverse-nodes-in-k-group/
    public static ListNode reverseKGroup(ListNode head, int k) {
        int len = 0;
        ListNode node = head;
        while (node != null) {
            ++len;
            node = node.next;
        }
        int num = 0;
        ListNode root = head;
        ListNode prev = null, cur = head, next = null, lastHead = null;
        while ((num += k) <= len) {
            int count = -1;
            head = cur;
            while (++count < k) {
                next = cur.next;
                cur.next = prev;
                prev = cur;
                cur = next;
            }
            if (num == k) {
                root = prev;
            }
            if (lastHead != null) {
                lastHead.next = prev;
            }
            head.next = cur;
            prev = lastHead = head;
        }
        return root;
    }

    private static ListNode reverse(ListNode node) {
        ListNode prev = null, cur = node, next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    // 24. Swap Nodes in Pairs
    // https://leetcode.com/problems/swap-nodes-in-pairs/
    public static ListNode swapPairs(ListNode head) {
        ListNode root = new ListNode(0), prev = root;
        ListNode first = prev.next = head, second = null;
        while (first != null && (second = first.next) != null) {
            prev.next = second;
            first.next = second.next;
            second.next = first;
            prev = first;
            first = first.next;
        }
        return root.next;
    }

    // 23.  Merge k Sorted Lists
    // https://leetcode.com/problems/merge-k-sorted-lists/
    public static ListNode mergeKLists(ListNode[] lists) {
        int len = lists.length;
        if (len == 0) {
            return null;
        }
        return mergeLists(lists, 0, lists.length - 1);
    }

    private static ListNode mergeLists(ListNode[] lists, int begin, int end) {
        if (begin >= end) {
            return lists[end];
        }
        int mid = (begin + end) >>> 1;
        ListNode firstNode = mergeLists(lists, begin, mid);
        ListNode secondNode = mergeLists(lists, mid + 1, end);
        ListNode root = new ListNode(0), node = root;
        while (firstNode != null && secondNode != null) {
            if (firstNode.val > secondNode.val) {
                node = node.next = secondNode;
                secondNode = secondNode.next;
            } else {
                node = node.next = firstNode;
                firstNode = firstNode.next;
            }
        }
        if (firstNode == null) {
            node.next = secondNode;
        }
        if (secondNode == null) {
            node.next = firstNode;
        }
        return root.next;
    }

    private static ListNode mergeKLists3(ListNode[] lists) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (ListNode node : lists) {
            Integer count = null;
            int value = 0;
            while (node != null) {
                value = node.val;
                count = map.get(value);
                if (count == null) {
                    map.put(value, 1);
                } else {
                    map.put(value, count + 1);
                }
                node = node.next;
            }
        }
        ListNode root = new ListNode(0), node = root;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int value = entry.getKey(), count = entry.getValue();
            while (--count >= 0) {
                node = node.next = new ListNode(value);
            }
        }
        return root.next;
    }

    private static ListNode mergeKLists2(ListNode[] lists) {
        int len = lists.length;
        if (len == 0) {
            return null;
        }
        ListNode root = new ListNode(-2147483648);
        root.next = lists[0];
        for (int i = 1; i < len; ++i) {
            ListNode node = lists[i], preNode = root, curNode = null;
            while (node != null) {
                curNode = preNode;
                int target = node.val;
                while (curNode != null && target >= curNode.val) {
                    preNode = curNode;
                    curNode = curNode.next;
                }
                if (curNode == null) {
                    preNode.next = node;
                    break;
                }
                preNode = preNode.next = node;
                node = node.next;
                preNode.next = curNode;
            }
        }
        return root.next;
    }

    private static ListNode mergeKLists1(ListNode[] lists) {
        ListNode root = new ListNode(0), node = root;
        int len = lists.length, index = 0;
        while (true) {
            int minValue = 2147483647, minIndex = -1;
            for (int i = 0; i < len; ++i) {
                ListNode curNode = lists[i];
                if (curNode != null && curNode.val < minValue) {
                    minValue = curNode.val;
                    minIndex = i;
                }
            }
            if (minIndex == -1) {
                return root.next;
            }
            node = node.next = lists[minIndex];
            lists[minIndex] = lists[minIndex].next;
        }
    }

    private static ListNode mergeKLists0(ListNode[] lists) {
        ListNode root = new ListNode(0), node = root;
        int minValue = 0, len = lists.length, index = 0;
        while (true) {
            --index;
            while (++index < len && lists[index] == null) ;
            if (index == len) {
                break;
            }
            int minIndex = index;
            minValue = lists[minIndex].val;
            for (int i = index + 1; i < len; ++i) {
                ListNode curNode = lists[i];
                if (curNode != null && curNode.val < minValue) {
                    minValue = curNode.val;
                    minIndex = i;
                }
            }
            node = node.next = new ListNode(minValue);
            lists[minIndex] = lists[minIndex].next;
        }
        return root.next;
    }

    // 21. Merge Two Sorted Lists
    // https://leetcode.com/problems/merge-two-sorted-lists/
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode root = new ListNode(0), node = root;
        while (l1 != null && l2 != null) {
            if (l1.val > l2.val) {
                node = node.next = l2;
                l2 = l2.next;
            } else {
                node = node.next = l1;
                l1 = l1.next;
            }
        }
        node.next = l1 == null ? l2 : l1;
        return root.next;
    }

    private static ListNode mergeTwoLists1(ListNode l1, ListNode l2) {
        if (l1 == null || l2 == null) {
            return l1 == null ? l2 : l1;
        }
        ListNode root = l1;
        if (l2.val < l1.val) {
            root = l2;
            l2 = l1;
        }
        ListNode preNode = root, curNode = null;
        while (l2 != null) {
            curNode = preNode;
            int target = l2.val;
            while (curNode != null && curNode.val <= target) {
                preNode = curNode;
                curNode = curNode.next;
            }
            if (curNode == null) {
                preNode.next = l2;
                break;
            }
            preNode = preNode.next = l2;
            l2 = l2.next;
            preNode.next = curNode;
        }
        return root;
    }

    private static ListNode mergeTwoLists0(ListNode l1, ListNode l2) {
        ListNode root = new ListNode(0), node = root;
        while (true) {
            if (l1 == null || l2 == null) {
                node.next = l1 == null ? l2 : l1;
                return root.next;
            }
            if (l1.val < l2.val) {
                node = node.next = new ListNode(l1.val);
                l1 = l1.next;
            } else {
                node = node.next = new ListNode(l2.val);
                l2 = l2.next;
            }
        }
    }

    // 6. ZigZag Conversion
    // https://leetcode.com/problems/zigzag-conversion/
    public static String convert(String s, int numRows) {
        int len = s.length();
        if (numRows < 2 || len <= numRows) {
            return s;
        }
        StringBuilder result = new StringBuilder(len);
        int maxGap = 2 * numRows - 2;
        int limit = numRows - 1;
        for (int i = 0; i < len; i += maxGap) {
            result.append(s.charAt(i));
        }
        for (int i = 1, leftGap = maxGap - 2; i < limit; ++i) {
            int index = i, rightGap = maxGap - leftGap;
            result.append(s.charAt(index));
            while ((index += leftGap) < len) {
                result.append(s.charAt(index));
                if ((index += rightGap) < len) {
                    result.append(s.charAt(index));
                }
            }
            leftGap -= 2;
        }
        for (int i = limit; i < len; i += maxGap) {
            result.append(s.charAt(i));
        }
        return result.toString();
    }

    // 667. Beautiful Arrangement II
    // https://leetcode.com/problems/beautiful-arrangement-ii/
    public int[] constructArray(int n, int k) {
        int[] array = new int[n];
        array[0] = 1;
        for (int i = k + 1; i < n; ) {
            array[i++] = i;
        }
        int addend = k;
        for (int i = 1; i <= k; ++i) {
            array[i] = array[i - 1] + addend;
            addend = addend > 0 ? 1 - addend : ~addend;
        }
        return array;
    }

    // 78. Subsets
    // https://leetcode.com/problems/subsets/
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(nums, res, new ArrayList<>(), 0);
        return res;
    }

    private static void dfs(int[] nums, List<List<Integer>> res, List<Integer> list, int s) {
        res.add(new ArrayList<Integer>(list));
        for (int i = s, len = nums.length; i < len; i++) {
            list.add(nums[i]);
            dfs(nums, res, list, i + 1);
            list.remove(list.size() - 1);
        }
    }

    private static List<List<Integer>> subsets0(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        List<List<Integer>> result = new ArrayList<>(1 << len);
        for (int num : nums) {
            List<Integer> list = new ArrayList<>(1);
            list.add(num);
            result.add(list);
        }
        int begin = 0, end = len - 1;
        for (int i = 0, limit = len - 1; i < limit; ++i) {
            int size = i + 2;
            for (int j = begin; j <= end; ++j) {
                List<Integer> preSubList = result.get(j);
                int target = preSubList.get(i);
                int index = len;
                while (nums[--index] > target) {
                    List<Integer> list = new ArrayList<>(size);
                    list.addAll(preSubList);
                    list.add(nums[index]);
                    result.add(list);
                }
            }
            begin = end + 1;
            end = result.size() - 1;
        }
        result.add(new ArrayList<>(0));
        return result;
    }

    // 495. Teemo Attacking
    // https://leetcode.com/problems/teemo-attacking/
    public int findPoisonedDuration(int[] timeSeries, int duration) {
        int len = timeSeries.length, poisonedTime = len * duration;
        for (int i = 1; i < len; ++i) {
            int timeDiff = timeSeries[i] - timeSeries[i - 1];
            if (timeDiff < duration) {
                poisonedTime -= duration - timeDiff;
            }
        }
        return poisonedTime;
    }

    // 565. Array Nesting
    // https://leetcode.com/problems/array-nesting/
    public int arrayNesting(int[] nums) {
        int len = nums.length;
        int maxCount = 1;
        boolean[] exist = new boolean[len];
        for (int i = 0; i < len; ++i) {
            if (exist[i]) {
                continue;
            }
            int count = 1, num = i;
            exist[num] = true;
            while (nums[num] != i) {
                exist[num = nums[num]] = true;
                ++count;
            }
            if (count > maxCount) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    // 238. Product of Array Except Self
    // https://leetcode.com/problems/product-of-array-except-self/
    public static int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        int[] tails = new int[len - 1];
        tails[0] = nums[len - 1];
        for (int i = 1, limit = len - 1; i < limit; ++i) {
            tails[i] = nums[limit - i] * tails[i - 1];
        }
        int[] result = new int[len];
        result[0] = tails[len - 2];
        int prod = 1;
        for (int i = 1, limit = len - 2; i <= limit; ++i) {
            result[i] = (prod *= nums[i - 1]) * tails[limit - i];
        }
        result[len - 1] = prod * nums[len - 2];
        return result;
    }

    public static int[] productExceptSelf0(int[] nums) {
        int len = nums.length;
        int[] heads = new int[len - 1];
        heads[0] = nums[0];
        int[] tails = new int[len - 1];
        tails[0] = nums[len - 1];
        for (int i = 1, limit = len - 1; i < limit; ++i) {
            heads[i] = nums[i] * heads[i - 1];
            tails[i] = nums[limit - i] * tails[i - 1];
        }
        int[] result = new int[len];
        result[0] = tails[len - 2];
        result[len - 1] = heads[len - 2];
        for (int i = 1, limit = len - 2; i <= limit; ++i) {
            result[i] = heads[i - 1] * tails[limit - i];
        }
        return result;
    }

    // 695. Max Area of Island
    // https://leetcode.com/problems/max-area-of-island/
    // Seed-Filling Algorithm
    public static int maxAreaOfIsland(int[][] grid) {
        int maxArea = 0;
        int rowLen = grid.length;
        int colLen = grid[0].length;
        int[][] marks = new int[rowLen][colLen];
        int mark = 0;
        for (int row = 0; row < rowLen; row++) {
            for (int col = 0; col < colLen; col++) {
                if (grid[row][col] == 1 && marks[row][col] == 0) {
                    int area = getIslandAreaHelper(grid, row, col, marks, ++mark);
                    if (area > maxArea) {
                        maxArea = area;
                    }
                }
            }
        }
        return maxArea;
    }

    private static int getIslandAreaHelper(int[][] grid, int row, int col, int[][] marks, int mark) {
        int area = 0;
        marks[row][col] = mark;
        if (row - 1 >= 0 && grid[row - 1][col] == 1 && marks[row - 1][col] == 0) {
            area += getIslandAreaHelper(grid, row - 1, col, marks, mark);
        }
        if (row + 1 < grid.length && grid[row + 1][col] == 1 && marks[row + 1][col] == 0) {
            area += getIslandAreaHelper(grid, row + 1, col, marks, mark);
        }
        if (col - 1 >= 0 && grid[row][col - 1] == 1 && marks[row][col - 1] == 0) {
            area += getIslandAreaHelper(grid, row, col - 1, marks, mark);
        }
        if (col + 1 < grid[row].length && grid[row][col + 1] == 1 && marks[row][col + 1] == 0) {
            area += getIslandAreaHelper(grid, row, col + 1, marks, mark);
        }
        return ++area;
    }

    // Two-Pass Algorithm(优化为并查集)
    private static int maxAreaOfIsland0(int[][] grid) {
        int rowLen = grid.length;
        int colLen = grid[0].length;
        int[][] mark = new int[rowLen][colLen];
        int parentLen = ((rowLen * colLen) >> 1) + 2;
        int[] parents = new int[parentLen];
        int count = 0;
        for (int row = 0; row < rowLen; ++row) {
            for (int col = 0; col < colLen; ++col) {
                if (grid[row][col] == 1) {
                    // 上
                    if (row > 0 && mark[row - 1][col] > 0) {
                        mark[row][col] = mark[row - 1][col];
                    }
                    // 左
                    if (col > 0 && mark[row][col - 1] > 0) {
                        if (mark[row][col] > 0) {
                            int leftParent = find(mark[row][col - 1], parents);
                            int upParent = find(mark[row - 1][col], parents);
                            if (leftParent > upParent) {
                                parents[leftParent] = upParent;
                                mark[row][col] = upParent;
                            } else if (leftParent < upParent) {
                                parents[upParent] = leftParent;
                                mark[row][col] = leftParent;
                            }
                        } else {
                            mark[row][col] = mark[row][col - 1];
                        }
                    }
                    if (mark[row][col] == 0) {
                        mark[row][col] = ++count;
                    }
                }
            }
        }
        for (int i = count; i > 0; --i) {
            if (parents[i] > 0) {
                update(i, parents);
            }
        }
        int[] areas = new int[count + 1];
        for (int row = 0; row < rowLen; ++row) {
            for (int col = 0; col < colLen; ++col) {
                if (mark[row][col] > 0) {
                    if (parents[mark[row][col]] > 0) {
                        mark[row][col] = parents[mark[row][col]];
                    }
                    areas[mark[row][col]]++;
                }
            }
        }
        int maxArea = -1;
        for (int area : areas) {
            if (area > maxArea) {
                maxArea = area;
            }
        }
        return maxArea;
    }

    private static int find(int value, int[] parents) {
        while (parents[value] > 0) {
            value = parents[value];
        }
        return value;
    }

    private static void update(int value, int[] parents) {
        int minValue = value;
        while (parents[minValue] > 0) {
            minValue = parents[minValue];
        }
        while (parents[value] > 0) {
            int temp = value;
            value = parents[value];
            parents[temp] = minValue;
        }
    }

    // Two-Pass Algorithm(最原始版本，此时并不知道并查集)
    public static int maxAreaOfIsland1(int[][] grid) {
        int rowLen = grid.length;
        int colLen = grid[0].length;
        int[][] mark = new int[rowLen][colLen];
        int replaceLen = ((rowLen * colLen) >> 1) + 2;
        int[] replace = new int[replaceLen];
        int count = 0;
        for (int row = 0; row < rowLen; ++row) {
            for (int col = 0; col < colLen; ++col) {
                if (grid[row][col] == 1) {
                    // 上
                    if (row - 1 >= 0 && mark[row - 1][col] > 0) {
                        mark[row][col] = mark[row - 1][col];
                    }
                    // 左
                    if (col - 1 >= 0 && mark[row][col - 1] > 0) {
                        if (mark[row][col] > 0) {
                            if (mark[row][col - 1] == mark[row - 1][col]) {
                                continue;
                            }
                            int leftMinValue = mark[row][col - 1];
                            while (replace[leftMinValue] > 0 && replace[leftMinValue] < leftMinValue) {
                                leftMinValue = replace[leftMinValue];
                            }
                            int upMinValue = mark[row - 1][col];
                            while (replace[upMinValue] > 0 && replace[upMinValue] < upMinValue) {
                                upMinValue = replace[upMinValue];
                            }
                            int minValue = leftMinValue > upMinValue ? upMinValue : leftMinValue;
                            for (int value : new int[]{mark[row][col - 1], mark[row - 1][col]}) {
                                while (replace[value] > 0 && replace[value] > minValue) {
                                    int temp = value;
                                    value = replace[value];
                                    replace[temp] = minValue;
                                }
                                if (value > minValue && replace[value] == 0) {
                                    replace[value] = minValue;
                                }
                            }
                            mark[row][col] = minValue;
                        } else {
                            mark[row][col] = mark[row][col - 1];
                        }
                    }
                    if (mark[row][col] == 0) {
                        mark[row][col] = ++count;
                    }
                }
            }
        }
        for (int i = count; i > 0; --i) {
            int minValue = replace[i];
            if (minValue > 0) {
                while (replace[minValue] > 0 && replace[minValue] < minValue) {
                    minValue = replace[minValue];
                }
                int value = replace[i];
                while (replace[value] > 0 && replace[value] < minValue) {
                    int temp = value;
                    value = replace[value];
                    replace[temp] = minValue;
                }
                replace[i] = minValue;
            }
        }
        int[] areas = new int[count + 1];
        for (int row = 0; row < rowLen; ++row) {
            for (int col = 0; col < colLen; ++col) {
                if (mark[row][col] > 0) {
                    if (replace[mark[row][col]] > 0) {
                        mark[row][col] = replace[mark[row][col]];
                    }
                    areas[mark[row][col]]++;
                }
            }
        }
        int maxArea = -1;
        for (int area : areas) {
            if (area > maxArea) {
                maxArea = area;
            }
        }
        return maxArea;
    }

    // 969. Pancake Sorting
    // https://leetcode.com/problems/pancake-sorting/
    public static List<Integer> pancakeSort(int[] A) {
        int len = A.length;
        int count = len;
        List<Integer> list = new ArrayList<Integer>();
        while (len > 0) {
            int maxValue = A[0];
            int index = 0;
            for (int i = 1; i < len; ++i) {
                if (A[i] > maxValue) {
                    maxValue = A[i];
                    index = i;
                }
            }
            list.add(index + 1);
            list.add(len);
            int begin = 0, end = index;
            while (begin < end) {
                ArrayUtils.swap(A, begin++, end--);
            }
            begin = 0;
            end = --len;
            while (begin < end) {
                ArrayUtils.swap(A, begin++, end--);
            }
        }
        return list;
    }

    private static void reverse(int[] array, int begin, int end) {
        while (begin < end) {
            ArrayUtils.swap(array, begin++, end--);
        }
    }

    // 442. Find All Duplicates in an Array
    // https://leetcode.com/problems/find-all-duplicates-in-an-array/
    public List<Integer> findDuplicates(int[] nums) {
        int len = nums.length;
        boolean[] exists = new boolean[len + 1];
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            if (exists[num]) {
                list.add(num);
            } else {
                exists[num] = true;
            }
        }
        return list;
    }

    // Without extra space and in O(n) runtime.
    private static List<Integer> findDuplicates0(int[] nums) {
        int len = nums.length;
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            int index = Math.abs(num) - 1;
            if (nums[index] > 0) {
                nums[index] = -nums[index];
            } else {
                list.add(index + 1);
            }
        }
        return list;
    }

    public static int[] deckRevealedIncreasing0(int[] deck) {
        int N = deck.length;
        Arrays.sort(deck);
        int[] result = new int[N];
        int i = 0, step = 2, start = 1;
        for (int num : deck) {
            result[i] = num;
            i += step;
            if (i >= N) {
                if (i - (step >> 1) < N) {
                    i = start;
                    start += step;
                } else {
                    i = start + step;
                    if (i >= N) {
                        i = start;
                    }
                }
                step *= 2;
            }
        }
        return result;
    }

    // 950. Reveal Cards In Increasing Order
    // https://leetcode.com/problems/reveal-cards-in-increasing-order/
    public static int[] deckRevealedIncreasing(int[] deck) {
        Arrays.sort(deck);
        int len = deck.length;
        if (len < 3) {
            return deck;
        }
        ArrayDeque<Integer> deque = new ArrayDeque<>(len);
        deque.addLast(deck[len - 1]);
        deque.addLast(deck[len - 2]);
        for (int i = len - 3; i >= 0; --i) {
            deque.addFirst(deque.removeLast());
            deque.addFirst(deck[i]);
        }
        int index = 0;
        for (Object obj : deque.toArray()) {
            deck[index++] = (int) obj;
        }
        return deck; // Arrays.stream(deque.toArray(new Integer[0])).mapToInt(Integer::valueOf).toArray();
    }

    // 665. Non-decreasing Array
    // https://leetcode.com/problems/non-decreasing-array/
    public static boolean checkPossibility(int[] nums) {
        int len = nums.length;
        int index = 0;
        while (++index < len && nums[index] >= nums[index - 1]) ;
        if (index == len) {
            return true;
        }
        if (index - 2 < 0 || nums[index - 2] <= nums[index]) {
            nums[index - 1] = nums[index];
        } else {
            nums[index] = nums[index - 1];
        }
        while (++index < len && nums[index] >= nums[index - 1]) ;
        return index == len;
    }

    // 414. Third Maximum Number
    // https://leetcode.com/problems/third-maximum-number/
    public static int thirdMax(int[] nums) {
        int len = nums.length;
        final int minValue = Integer.MIN_VALUE;
        int first = minValue;
        int second = minValue, third = minValue;
        boolean exist = false;
        for (int num : nums) {
            if (num == Integer.MIN_VALUE) {
                exist = true;
                continue;
            }
            if (num > first) {
                third = second;
                second = first;
                first = num;
            } else if (num > second && num < first) {
                third = second;
                second = num;
            } else if (num > third && num < second) {
                third = num;
            }
        }
        return (second > minValue && (third != minValue || (exist && third == minValue))) ? third : first;
    }

    private static int thirdMax0(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        int count = 3;
        int index = len - 1;
        int maxValue = nums[index];
        int target = 0;
        while (--count >= 0) {
            target = nums[index];
            while (--index >= 0 && nums[index] == target) ;
            if (index == -1 && count > 0) {
                return maxValue;
            }
        }
        return target;
    }

    // 189. Rotate Array
    // https://leetcode.com/problems/rotate-array/
    public static void rotate(int[] nums, int k) {
        int len = nums.length;
        k = k % len;
        if (k == 0) {
            return;
        }
        if (k > (len >> 1)) { // 左移到右
            int leftLen = len - k;
            int[] leftArray = new int[leftLen];
            System.arraycopy(nums, 0, leftArray, 0, leftLen);
            System.arraycopy(nums, len - k, nums, 0, k);
            System.arraycopy(leftArray, 0, nums, k, leftLen);
        } else { // 右移到左
            int[] rightArray = new int[k];
            System.arraycopy(nums, len - k, rightArray, 0, k);
            System.arraycopy(nums, 0, nums, k, len - k);
            System.arraycopy(rightArray, 0, nums, 0, k);
        }
    }

    private static void rotate0(int[] nums, int k) {
        int len = nums.length;
        k = k % len;
        int limit = len - k;
        int index = limit;
        for (; index < len; ++index) {
            int value = nums[index];
            int bound = index - limit;
            for (int i = index; i > bound; --i) {
                nums[i] = nums[i - 1];
            }
            nums[bound] = value;
        }
    }

    // 532. K-diff Pairs in an Array
    // https://leetcode.com/problems/k-diff-pairs-in-an-array/
    public static int findPairs(int[] nums, int k) {
        int len = 0;
        if (nums == null || (len = nums.length) < 2 || k < 0) {
            return 0;
        }
        Arrays.sort(nums);
        int small = 0, large = 0;
        int count = 0;
        int target = nums[small] + k;
        int left = 1, right = len - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            int midVal = nums[mid];
            if (midVal > target) {
                right = mid - 1;
            } else if (midVal < target) {
                left = mid + 1;
            } else {
                left = mid;
                ++count;
                break;
            }
        }
        large = left;
        int value = nums[small];
        while (true) {
            while (++small < len && nums[small] == value) ;
            if (small == len) {
                break;
            }
            target = (value = nums[small]) + k;
            if (large <= small) {
                large = small + 1;
            }
            while (large < len && nums[large] < target) {
                ++large;
            }
            if (large == len) {
                break;
            }
            if (nums[large] == target) {
                ++count;
            }
        }
        return count;
    }

    private static int findPairs0(int[] nums, int k) {
        if (k < 0) {
            return 0;
        }
        HashMap<Integer, Boolean> map = new HashMap<>();
        Boolean repeat = false;
        for (int num : nums) {
            repeat = map.get(num);
            if (repeat == null) {
                map.put(num, false);
            } else if (repeat == false) {
                map.put(num, true);
            }
        }
        int count = 0;
        if (k == 0) {
            for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
                if (entry.getValue()) {
                    ++count;
                }
            }
        } else {
            for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
                int num = entry.getKey();
                if (map.containsKey(num + k)) {
                    ++count;
                }
            }
        }
        return count;
    }

    // 581. Shortest Unsorted Continuous Subarray
    // https://leetcode.com/problems/shortest-unsorted-continuous-subarray/
    public static int findUnsortedSubarray(int[] nums) {
        int len = nums.length;
        int index = 0;
        while (++index < len && nums[index] >= nums[index - 1]) ;
        if (index == len) {
            return 0;
        }
        int maxIndex = index, maxValue = nums[index - 1];
        int target = nums[index];
        int minIndex = index - 1, minValue = nums[index];
        while (--minIndex >= 0 && nums[minIndex] > target) ;
        ++minIndex;
        for (index = index + 1; index < len; ++index) {
            int value = nums[index];
            if (value > maxValue) {
                maxValue = value;
            } else if (value < maxValue) {
                if (value < minValue) {
                    minValue = value;
                    while (--minIndex >= 0 && nums[minIndex] > minValue) ;
                    ++minIndex;
                }
                maxIndex = index;
            }
        }
        return maxIndex - minIndex + 1;
    }

    private static int findUnsortedSubarray0(int[] nums) {
        int len = nums.length;
        int minIndex = len, maxIndex = -1;
        for (int i = 1; i < len; ++i) {
            if (nums[i] >= nums[i - 1]) {
                continue;
            }
            int target = nums[i];
            int index = i;
            while (--index >= 0 && nums[index] > target) {
                nums[index + 1] = nums[index];
            }
            nums[++index] = target;
            if (index < minIndex) {
                minIndex = index;
            }
            maxIndex = i;
        }
        return maxIndex == -1 ? 0 : maxIndex - minIndex + 1;
    }

    // 605. Can Place Flowers
    // https://leetcode.com/problems/can-place-flowers/
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        int len = flowerbed.length;
        int left = -1, right = len;
        while (++left < len && flowerbed[left] == 0) ;
        if (left == len) {
            return (len + 1) / 2 >= n;
        }
        int maxFlowerNums = left / 2;
        if (maxFlowerNums >= n) {
            return true;
        }
        while (flowerbed[--right] == 0) ;
        maxFlowerNums += (len - 1 - right) / 2;
        if (maxFlowerNums >= n) {
            return true;
        }
        if (left == right) {
            return false;
        }
        while (left < right) {
            int count = left;
            while (flowerbed[++left] == 0) ;
            count = left - 1 - count;
            maxFlowerNums += (count - 1) / 2;
            if (maxFlowerNums >= n) {
                return true;
            }
        }
        return false;
    }

    // 914. X of a Kind in a Deck of Cards
    // https://leetcode.com/problems/x-of-a-kind-in-a-deck-of-cards
    public static boolean hasGroupsSizeX(int[] deck) {
        int len = 0;
        if (deck == null || (len = deck.length) < 2) {
            return false;
        }
        int[] counts = new int[10001];
        for (int i = 0; i < len; ++i) {
            ++counts[deck[i]];
        }
        int index = -1;
        while (++index < 10001 && counts[index] == 0) ;
        if (counts[index] == 1) {
            return false;
        }
        HashSet<Integer> set = new HashSet<Integer>();
        for (int factor : MathUtils.factor(counts[index])) {
            set.add(factor);
        }
        for (index = index + 1; index < 10001; ++index) {
            int count = counts[index];
            if (count == 0) {
                continue;
            }
            if (count == 1) {
                return false;
            }
            Iterator<Integer> iter = set.iterator();
            while (iter.hasNext()) {
                if (count % iter.next() != 0) {
                    iter.remove();
                }
            }
            if (set.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // 941. Valid Mountain Array
    // https://leetcode.com/problems/valid-mountain-array/
    public boolean validMountainArray(int[] A) {
        int len = 0;
        if (A == null || (len = A.length) < 3) {
            return false;
        }
        int index = 0;
        while (++index < len && A[index] > A[index - 1]) ;
        if (index == 1 || index == len) {
            return false;
        }
        while (index < len && A[index] < A[index - 1]) {
            ++index;
        }
        if (index != len) {
            return false;
        }
        return true;
    }

    // 219. Contains Duplicate II
    // https://leetcode.com/problems/contains-duplicate-ii/
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        int len = 0;
        if (nums == null || k < 0 || (len = nums.length) < 2) {
            return false;
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        Integer index = null;
        int value = 0;
        for (int i = 0; i < len; ++i) {
            index = map.get(value = nums[i]);
            if (index == null || i - index > k) {
                map.put(value, i);
            } else {
                return true;
            }
        }
        return false;
    }

    // 88. Merge Sorted Array
    // https://leetcode.com/problems/merge-sorted-array/
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        if (m == 0 || n == 0) {
            if (m == 0) {
                System.arraycopy(nums2, 0, nums1, 0, n);
            }
            return;
        }
        int[] tempArray = new int[m];
        System.arraycopy(nums1, 0, tempArray, 0, m);
        int index = 0;
        int index1 = 0, index2 = 0;
        while (index1 < m && index2 < n) {
            if (tempArray[index1] > nums2[index2]) {
                nums1[index++] = nums2[index2++];
            } else {
                nums1[index++] = tempArray[index1++];
            }
            if (index1 == m) {
                System.arraycopy(nums2, index2, nums1, index, n - index2);
                return;
            }
            if (index2 == n) {
                System.arraycopy(tempArray, index1, nums1, index, m - index1);
                return;
            }
        }
    }

    // 840. Magic Squares In Grid
    // https://leetcode.com/problems/magic-squares-in-grid/
    public static int numMagicSquaresInside(int[][] grid) {
        int mask = (-1 >>> 22) - 1;
        int len = grid.length;
        int num = 0;
        for (int i = 0, iLimit = len - 2; i < iLimit; ++i) {
            outer:
            for (int j = 0, jLimit = grid[i].length - 2; j < jLimit; ++j) {
                int leftDiagonal = grid[i][j] + grid[i + 1][j + 1] + grid[i + 2][j + 2];
                int rightDiagonal = grid[i][j + 2] + grid[i + 1][j + 1] + grid[i + 2][j];
                if (leftDiagonal != rightDiagonal) {
                    continue;
                }
                int bit = 0;
                int[] rowSums = new int[3];
                for (int row = i + 2; row >= i; --row) {
                    int colSum = 0;
                    for (int col = j + 2; col >= j; --col) {
                        int value = grid[row][col];
                        bit += 1 << value;
                        colSum += value;
                        rowSums[col - j] += value;
                    }
                    if (colSum != leftDiagonal) {
                        continue outer;
                    }
                }
                if (bit != mask) {
                    continue;
                }
                if (rowSums[0] == leftDiagonal &&
                        rowSums[1] == leftDiagonal &&
                        rowSums[2] == leftDiagonal) {
                    ++num;
                }
            }
        }
        return num;
    }

    // 624. Maximum Distance in Arrays
    public static int maxDistance(List<List<Integer>> array) {
        int len = array.size();
        int firstLargestValIndex = -1, secondLargestValIndex = -1;
        int firstSmallestValIndex = -1, secondSmallestValIndex = -1;
        int firstSmallestValue = Integer.MAX_VALUE, firstLargestValue = Integer.MIN_VALUE;
        int secondSmallestValue = Integer.MAX_VALUE, secondLargestValue = Integer.MIN_VALUE;
        for (int i = 0; i < len; ++i) {
            List<Integer> list = array.get(i);
            int small = list.get(0); // small
            int large = list.get(list.size() - 1); // large
            if (small < firstSmallestValue) {
                secondSmallestValue = firstSmallestValue;
                secondSmallestValIndex = firstSmallestValIndex;
                firstSmallestValue = small;
                firstSmallestValIndex = i;
            } else {
                if (small < firstSmallestValue) {
                    secondSmallestValue = small;
                    secondSmallestValIndex = i;
                }
            }
            if (large > firstLargestValue) {
                secondLargestValIndex = firstLargestValIndex;
                secondLargestValue = firstLargestValue;
                firstLargestValIndex = i;
                firstLargestValue = large;
            } else {
                if (large > secondLargestValue) {
                    secondLargestValue = large;
                    secondLargestValIndex = i;
                }
            }
        }
        if (firstSmallestValIndex != firstLargestValIndex) {
            return firstLargestValue - firstSmallestValue;
        }
        if (secondLargestValue - firstSmallestValue > firstLargestValue - secondSmallestValue) {
            return secondLargestValue - firstSmallestValue;
        } else {
            return firstLargestValue - secondSmallestValue;
        }
    }

    // 643. Maximum Average Subarray I
    // https://leetcode.com/problems/maximum-average-subarray-i/
    public static double findMaxAverage(int[] nums, int k) {
        int len = nums.length;
        int sum = 0;
        for (int i = 0; i < k; ++i) {
            sum += nums[i];
        }
        int maxSum = sum;
        for (int i = k; i < len; ++i) {
            sum = sum - nums[i - k] + nums[i];
            if (sum > maxSum) {
                maxSum = sum;
            }
        }
        return (double) maxSum / k;
    }

    // 26. Remove Duplicates from Sorted Array
    // https://leetcode.com/problems/remove-duplicates-from-sorted-array/
    public int removeDuplicates(int[] nums) {
        int len = 0;
        if (nums == null || (len = nums.length) < 2) {
            return len;
        }
        int target = nums[0];
        int count = 1, index = 0, left = 0;
        while (true) {
            while (++index < len && nums[index] == target) ;
            if (index == len) {
                break;
            }
            nums[++left] = target = nums[index];
            ++count;
        }
        return count;
    }

    // 747. Largest Number At Least Twice of Others
    // https://leetcode.com/problems/largest-number-at-least-twice-of-others/
    public static int dominantIndex(int[] nums) {
        int len = nums.length;
        if (len == 1) {
            return 0;
        }
        int max = nums[0];
        int second = -2147483648;
        int index = 0;
        for (int i = 1; i < len; ++i) {
            int num = nums[i];
            if (num > max) {
                second = max;
                max = num;
                index = i;
            } else if (num > second) {
                second = num;
            }
        }
        return max < second * 2 ? -1 : index;
    }

    private static int dominantIndex0(int[] nums) {
        int len = nums.length;
        if (len == 1) {
            return 0;
        }
        int[] buckets = new int[101];
        for (int num : nums) {
            buckets[num]++;
        }
        int index = 101;
        while (--index >= 0 && buckets[index] == 0) ;
        if (buckets[index] > 1) {
            return -1;
        }
        int maxValue = index;
        while (--index >= 0 && buckets[index] == 0) ;
        if (index < 0 || maxValue < index * 2) {
            return -1;
        }
        index = -1;
        while (nums[++index] != maxValue) ;
        return index;
    }

    // 849. Maximize Distance to Closest Person
    // https://leetcode.com/problems/maximize-distance-to-closest-person/
    public static int maxDistToClosest(int[] seats) {
        int len = seats.length;
        int left = -1;
        while (++left < len && seats[left] == 0) ;
        int maxCount = left;
        int right = len;
        while (--right >= 0 && seats[right] == 0) ;
        if (len - 1 - right > left) {
            maxCount = len - 1 - right;
        }
        while (left < right) {
            int count = left;
            while (++left < right && seats[left] == 0) ;
            count = (left - count) >> 1;
            if (count > maxCount) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    // 724. Find Pivot Index
    // https://leetcode.com/problems/find-pivot-index/
    public static int pivotIndex(int[] nums) {
        int len = nums.length;
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        int leftSum = 0;
        for (int i = 0; i < len; ++i) {
            int num = nums[i];
            if (totalSum - num == (leftSum << 1)) {
                return i;
            }
            leftSum += num;
        }
        return -1;
    }

    // 119. Pascal's Triangle II
    // https://leetcode.com/problems/pascals-triangle-ii/
    public List<Integer> getRow(int rowIndex) {
        if (rowIndex <= 0) {
            return Arrays.asList(new Integer[]{1});
        }
        int len = rowIndex + 1;
        List<Integer> result = new ArrayList<>(len);
        int[] pascal = new int[len + 1];
        int[] temp = null;
        pascal[0] = 1;
        for (int row = 1; row < len; ++row) {
            temp = new int[row + 1];
            for (int i = 1; i <= row; ++i) {
                temp[i] = pascal[i] + pascal[i - 1];
            }
            System.arraycopy(temp, 1, pascal, 1, row);
        }
        for (int i = 0; i < len; ++i) {
            result.add(pascal[i]);
        }
        return result;
    }

    // 53. Maximum Subarray
    // https://leetcode.com/problems/maximum-subarray/
    public static int maxSubArray(int[] nums) {
        int len = nums.length;
        int sum = nums[0];
        int maxSum = sum;
        for (int i = 1; i < len; ++i) {
            int num = nums[i];
            if (sum < 0) {
                sum = num;
            } else {
                sum += num;
            }
            if (sum > maxSum) {
                maxSum = sum;
            }
        }
        return maxSum;
    }

    // 1010. Pairs of Songs With Total Durations Divisible by 60
    // https://leetcode.com/problems/pairs-of-songs-with-total-durations-divisible-by-60/
    public static int numPairsDivisibleBySixty(int[] time) {
        if (time == null || time.length <= 1) return 0;
        int n = time.length, result = 0;
        int[] counts = new int[60];
        int[] times = new int[501];
        for (int t : time) times[t]++;
        for (int i = 0; i < 60; ++i) {
            for (int j = i; j <= 500; j += 60) {
                counts[i] += times[j];
            }
        }
        int count = 0;
        if ((count = counts[0]) > 1) {
            result += count * (count - 1) / 2;
        }
        if ((count = counts[30]) > 1) {
            result += count * (count - 1) / 2;
        }
        for (int i = 1; i < 30; ++i) {
            result += counts[i] * counts[60 - i];
        }
        return result;
    }

    private static int numPairsDivisibleBySixty0(int[] time) {
        int result = 0;
        int[] mods = new int[60];
        for (int t : time) {
            mods[t % 60]++;
        }
        int mod = 0;
        if ((mod = mods[0]) > 1) {
            result += mod * (mod - 1) / 2;
        }
        if ((mod = mods[30]) > 1) {
            result += mod * (mod - 1) / 2;
        }
        for (int i = 1; i < 30; ++i) {
            result += mods[i] * mods[60 - i];
        }
        return result;
    }

    // 674. Longest Continuous Increasing Subsequence
    // https://leetcode.com/problems/longest-continuous-increasing-subsequence/
    public static int findLengthOfLCIS(int[] nums) {
        int len = 0;
        if (nums == null || (len = nums.length) == 0) {
            return 0;
        }
        int maxCount = 1;
        int index = 0;
        while (index < len) {
            int count = 1;
            int num = nums[index];
            while (++index < len && nums[index] > nums[index - 1]) {
                ++count;
            }
            if (count > maxCount) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    // 1018. Binary Prefix Divisible By 5
    // https://leetcode.com/problems/binary-prefix-divisible-by-5/
    public static List<Boolean> prefixesDivByFive(int[] A) {
        int len = A.length;
        Boolean[] result = new Boolean[len];
        for (int i = 0, value = 0; i < len; ++i) {
            value = (2 * value + A[i]) % 5;
            result[i] = value == 0;
        }
        return Arrays.asList(result);
    }

    private static List<Boolean> prefixesDivByFive0(int[] A) {
        List<Boolean> list = new ArrayList<>(A.length);
        int value = 0;
        for (int a : A) {
            value = (2 * value + a) % 10;
            list.add(value == 0 || value == 5);
        }
        return list;
    }

    // 989. Add to Array-Form of Integer : https://leetcode.com/problems/add-to-array-form-of-integer/
    public static List<Integer> addToArrayForm(int[] A, int K) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = A.length - 1; i >= 0 || K > 0; i--, K /= 10) {
            if (i >= 0) {
                K += A[i];
            }
            list.addFirst(K % 10);
        }
        return list;
    }

    private static List<Integer> addToArrayForm0(int[] A, int K) {
        LinkedList<Integer> result = new LinkedList<>();
        int radix = 1;
        int len = A.length;
        int index = len - 1;
        int carry = 0;
        do {
            int sum = carry;
            if (K >= radix) {
                sum += K / radix % 10;
                radix *= 10;
            }
            if (index >= 0) {
                sum += A[index--];
            }
            if (sum > 9) {
                carry = 1;
                sum -= 10;
            } else {
                carry = 0;
            }
            result.addFirst(sum);
        } while (index >= 0 || K >= radix);
        if (carry == 1) {
            result.addFirst(1);
        }
        return result;
    }

    // 118. Pascal's Triangle : https://leetcode.com/problems/pascals-triangle
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>(numRows);
        int[] tempNums = new int[numRows + 1];
        int[] nums = new int[numRows + 1];
        List<Integer> list = null;
        if (numRows >= 1) {
            list = new ArrayList<Integer>(1);
            list.add(1);
            result.add(list);
            tempNums[0] = nums[0] = 1;
        }
        for (int row = 2; row <= numRows; ++row) {
            list = new ArrayList<Integer>(row);
            list.add(1);
            for (int i = 1; i < row; ++i) {
                list.add(tempNums[i] = nums[i] + nums[i - 1]);
            }
            result.add(list);
            System.arraycopy(tempNums, 1, nums, 1, row - 1);
        }
        return result;
    }

    // 	628. Maximum Product of Three Numbers : https://leetcode.com/problems/maximum-product-of-three-numbers
    public static int maximumProduct(int[] nums) {
        int len = nums.length;
        Arrays.sort(nums);
        int a = nums[len - 1] * nums[len - 2] * nums[len - 3];
        int b = nums[0] * nums[1] * nums[len - 1];
        return a > b ? a : b;
    }

    private static int maximumProduct0(int[] nums) {
        java.util.Arrays.sort(nums);
        int size = nums.length - 1;
        int first = nums[size] * nums[size - 1] * nums[size - 2];
        int second = 0;
        if (nums[0] < 0) {
            second = nums[0] * nums[size - 1] * nums[size];
        }
        if (second > first) {
            first = second;
        }
        if (nums[0] < 0 && nums[1] < 0) {
            second = nums[0] * nums[1] * nums[size];
        }
        if (second > first) {
            first = second;
        }
        return first;
    }

    // 746. Min Cost Climbing Stairs : https://leetcode.com/problems/min-cost-climbing-stairs
    public int minCostClimbingStairs(int[] cost) {
        int len = cost.length;
        int[] minCost = new int[len];
        for (int i = 2; i < len; ++i) {
            minCost[i] = Math.min(minCost[i - 2] + cost[i - 2], minCost[i - 1] + cost[i - 1]);
        }
        return Math.min(minCost[--len] + cost[len--], minCost[len] + cost[len]);
    }

    // 121. Best Time to Buy and Sell Stock : https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
    public static int maxProfit(int[] prices) {
        int len = prices.length;
        int profit = 0, minValue = Integer.MAX_VALUE;
        for (int price : prices) {
            if (price < minValue) {
                minValue = price;
                continue;
            }
            int diff = price - minValue;
            if (diff > profit) {
                profit = diff;
            }
        }
        return profit;
    }

    private static int maxProfit1(int[] prices) {
        int len = prices.length;
        int profit = 0, minValue = Integer.MAX_VALUE;
        for (int price : prices) {
            int diff = price - minValue;
            if (diff > profit) {
                profit = diff;
            }
            if (minValue > price) {
                minValue = price;
            }
        }
        return profit;
    }

    private static int maxProfit0(int[] prices) {
        int limit = prices.length - 1;
        int profit = 0;
        for (int i = 0; i < limit; ++i) {
            for (int j = i + 1; j <= limit; ++j) {
                int diff = prices[j] - prices[i];
                if (diff > profit) {
                    profit = diff;
                }
            }
        }
        return profit;
    }

    // 	830. Positions of Large Groups : https://leetcode.com/problems/positions-of-large-groups
    public List<List<Integer>> largeGroupPositions(String S) {
        char[] letters = S.toCharArray();
        int len = letters.length;
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> list = null;
        int index = 1;
        while (true) {
            int begin = index;
            while (index < len && letters[index] == letters[index - 1]) {
                ++index;
            }
            if (index - begin >= 2) {
                list = new ArrayList<Integer>(2);
                list.add(begin - 1);
                list.add(index - 1);
                result.add(list);
            }
            if (index++ == len) {
                break;
            }
        }
        return result;
    }

    // 	661. Image Smoother : https://leetcode.com/problems/image-smoother
    public int[][] imageSmoother(int[][] original) {
        int rowLimit = original.length - 1;
        int colLimit = original[0].length - 1;
        int[][] result = new int[rowLimit + 1][colLimit + 1];
        for (int row = 0; row <= rowLimit; ++row) {
            for (int col = 0; col <= colLimit; ++col) {
                int sum = 0;
                int iBegin = row - 1 >= 0 ? row - 1 : 0;
                int iEnd = row + 1 > rowLimit ? rowLimit : row + 1;
                int jBegin = col - 1 >= 0 ? col - 1 : 0;
                int jEnd = col + 1 > colLimit ? colLimit : col + 1;
                for (int i = iBegin; i <= iEnd; ++i) {
                    for (int j = jBegin; j <= jEnd; ++j) {
                        sum += original[i][j];
                    }
                }
                int count = (iEnd - iBegin + 1) * (jEnd - jBegin + 1);
                result[row][col] = (int) Math.floor((double) sum / count);
            }
        }
        return result;
    }

    // 717. 1-bit and 2-bit Characters : https://leetcode.com/problems/1-bit-and-2-bit-characters/
    public static boolean isOneBitCharacter(int[] bits) {
        int len = bits.length;
        if (len < 2) {
            return true;
        }
        int index = 0;
        while (index < len) {
            int bit = bits[index];
            if (bit == 0) {
                if (++index == len) {
                    return true;
                }
            } else {
                if ((index += 2) == len) {
                    return false;
                }
            }
        }
        return false;
    }

    // 697. Degree of an Array : https://leetcode.com/problems/degree-of-an-array/
    public static int findShortestSubArray(int[] nums) {
        int len = nums.length;
        int[] counts = new int[50000];
        int[] begins = new int[50000];
        int[] ends = new int[50000];
        int value = nums[0], count = 1;
        counts[value] = 1;
        for (int i = 1; i < len; ++i) {
            int num = nums[i];
            if (counts[num]++ > 0) {
                ends[num] = i;
                if (counts[num] > count) {
                    count = counts[value = num];
                } else if (counts[num] == count) {
                    if (ends[num] - begins[num] < ends[value] - begins[value]) {
                        value = num;
                    }
                }
            } else {
                begins[num] = i;
            }
        }
        return ends[value] - begins[value] + 1;
    }

    private static int findShortestSubArray0(int[] nums) {
        int len = nums.length;
        if (len == 1) {
            return 1;
        }
        int maxCount = 1, index = 0, tempCount = 0;
        Count count = null;
        HashMap<Integer, Count> map = new HashMap<>();
        map.put(nums[0], new Count(0, 1));
        for (int i = 1; i < len; ++i) {
            int num = nums[i];
            count = map.get(num);
            if (count == null) {
                map.put(num, new Count(i, 1));
            } else {
                count.count = tempCount = count.count + 1;
                if (tempCount > maxCount) {
                    maxCount = tempCount;
                    index = i;
                } else if (tempCount == maxCount) {
                    if (index - map.get(nums[index]).index > i - count.index) {
                        index = i;
                    }
                }
                map.put(num, count);
            }
        }
        return index - map.get(nums[index]).index + 1;
    }

    static class Count {
        public int index;
        public int count;

        public Count(int index, int count) {
            this.index = index;
            this.count = count;
        }
    }

    // 167. Two Sum II - Input array is sorted
    public static int[] twoSumII(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum > target) {
                --right;
            } else if (sum < target) {
                ++left;
            } else {
                return new int[]{left + 1, right + 1};
            }
        }
        return null;
    }

    // 	217. Contains Duplicate : https://leetcode.com/problems/contains-duplicate/
    // 排序可能破坏输入的数组顺序，利用boolean桶记录，可能会出现异常
    public boolean containsDuplicate(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false;
        }
        HashSet<Integer> set = new HashSet<Integer>();
        for (int num : nums) {
            if (set.contains(num)) {
                return true;
            }
            set.add(num);
        }
        return true;
    }

    // 122. Best Time to Buy and Sell Stock II : https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii
    public static int maxProfitII(int[] prices) {
        int len = 0;
        if (prices == null || (len = prices.length) < 2) {
            return 0;
        }
        int profit = 0, buy = prices[0];
        for (int price : prices) {
            if (price < buy) {
                buy = price;
            } else {
                profit += price - buy;
                buy = price;
            }
        }
        return profit;
    }


    private static int maxProfitII0(int[] prices) {
        int len = 0;
        if (prices == null || (len = prices.length) < 2) {
            return 0;
        }
        int profit = 0, index = 0;
        int limit = len - 1;
        int buy = 0, sell = 0;
        while (true) {
            while (index + 1 < len && prices[index] >= prices[index + 1]) {
                ++index;
            }
            if (index >= limit) {
                return profit;
            }
            buy = prices[index];
            while (++index < len && prices[index] >= prices[index - 1]) ;
            if ((sell = prices[index - 1]) == buy) {
                return profit;
            }
            profit += sell - buy;
        }
    }

    // 	169. Majority Element : https://leetcode.com/problems/majority-element/
    public int majorityElement(int[] nums) {
        int value = 0, count = 1;
        for (int num : nums) {
            if (num == value) {
                count = count + 1;
            } else {
                if (count > 1) {
                    count = count - 1;
                } else {
                    value = num;
                    count = 1;
                }
            }
        }
        return value;
    }

    // 	448. Find All Numbers Disappeared in an Array : https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
    public static List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> list = new ArrayList<Integer>();
        if (nums == null) {
            return list;
        }
        int len = nums.length;
        for (int i = 0; i < len; ++i) {
            int value = Math.abs(nums[i]) - 1;
            if (nums[value] > 0) {
                nums[value] = -nums[value];
            }
        }
        for (int i = 0; i < len; ++i) {
            if (nums[i] > 0) {
                list.add(i + 1);
            }
        }
        return list;
    }

    private static List<Integer> findDisappearedNumbers0(int[] nums) {
        List<Integer> list = new ArrayList<Integer>();
        if (nums == null) {
            return list;
        }
        int len = nums.length;
        for (int i = 0; i < len; ++i) {
            int index = nums[i] - 1;
            if (index < 0) {
                continue;
            }
            while (nums[index] != -1) {
                int temp = nums[index] - 1;
                nums[index] = -1;
                index = temp;
            }
        }
        for (int i = 0; i < len; ++i) {
            if (nums[i] != -1) {
                list.add(i + 1);
            }
        }
        return list;
    }

    // 1013. Partition Array Into Three Parts With Equal Sum : https://leetcode.com/problems/partition-array-into-three-parts-with-equal-sum/submissions/
    public static boolean canThreePartsEqualSum(int[] A) {
        int len = A.length;
        int totalSum = 0;
        for (int a : A) {
            totalSum += a;
        }
        int limit = totalSum / 3;
        int left = 0, right = len - 1;
        int leftSum = A[left], rightSum = A[right];
        while (leftSum != limit && left + 1 < len) {
            leftSum += A[++left];
        }
        if (len - left <= 2) {
            return false;
        }
        while (rightSum != limit && right - 1 >= 0) {
            rightSum += A[--right];
        }
        if (right == len || left >= right) {
            return false;
        }
        return true;
    }

    // 283. Move Zeroes : https://leetcode.com/problems/move-zeroes/
    public static void moveZeroes(int[] nums) {
        int len = 0;
        if (nums == null || (len = nums.length) < 2) {
            return;
        }
        int pos = -1;
        for (int num : nums) {
            if (num != 0) {
                nums[++pos] = num;
            }
        }
        while (++pos < len) {
            nums[pos] = 0;
        }
    }

    private static void moveZeroes1(int[] nums) {
        int len = 0;
        if (nums == null || (len = nums.length) < 2) {
            return;
        }
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < len; ++i) {
            if (nums[i] != 0 && !deque.isEmpty()) {
                nums[deque.remove()] = nums[i];
                nums[i] = 0;
                deque.add(i);
            } else if (nums[i] == 0) {
                deque.add(i);
            }
        }
    }

    private static void moveZeroes0(int[] nums) {
        int len = 0;
        if (nums == null || (len = nums.length) < 2) {
            return;
        }
        int zeroIndex = -1, nonZeroIndex = 0;
        while (true) {
            while (++zeroIndex < len && nums[zeroIndex] != 0) ;
            if (zeroIndex == len) {
                return;
            }
            nonZeroIndex = zeroIndex;
            while (++nonZeroIndex < len && nums[nonZeroIndex] == 0) ;
            if (nonZeroIndex == len) {
                return;
            }
            nums[zeroIndex] = nums[nonZeroIndex];
            nums[nonZeroIndex] = 0;
        }
    }

    // 485. Max Consecutive Ones : https://leetcode.com/problems/max-consecutive-ones/
    public static int findMaxConsecutiveOnes(int[] nums) {
        int len = 0;
        if (nums == null || (len = nums.length) == 0) {
            return len;
        }
        int maxLen = 0;
        int index = -1;
        while (++index < len && nums[index] == 0) ;
        if (index < len) {
            maxLen = 1;
        }
        while (index < len) {
            int count = 1;
            while (++index < len && nums[index] == 1) {
                ++count;
            }
            if (count > maxLen) {
                maxLen = count;
            }
            while (++index < len && nums[index] == 0) ;
        }
        return maxLen;
    }

    // 896. Monotonic Array : https://leetcode.com/problems/monotonic-array/
    public boolean isMonotonic(int[] A) {
        int len = 0;
        if (A == null || (len = A.length) < 3) {
            return true;
        }
        boolean increse = true;
        int index = 1;
        for (; index < len; ++index) {
            if (A[index] > A[index - 1]) {
                break;
            }
            if (A[index] < A[index - 1]) {
                increse = false;
                break;
            }
        }
        while (++index < len) {
            if (A[index] != A[index - 1] && (A[index] > A[index - 1]) != increse) {
                return false;
            }
        }
        return true;
    }

    // 888. Fair Candy Swap : https://leetcode.com/problems/fair-candy-swap/
    public int[] fairCandySwap(int[] A, int[] B) {
        if (A == null || B == null) {
            return null;
        }
        int aSum = 0, bSum = 0;
        for (int a : A) {
            aSum += a;
        }
        for (int b : B) {
            bSum += b;
        }
        boolean swap = false;
        int diff = aSum - bSum;
        if (bSum > aSum) {
            int[] temp = A;
            A = B;
            B = temp;
            diff = -diff;
            swap = true;
        }
        if ((diff & 1) == 1) {
            return null;
        }
        diff >>= 1;
        java.util.HashSet<Integer> set = new java.util.HashSet<>();
        for (int b : B) {
            set.add(b);
        }
        for (int a : A) {
            if (set.contains(a - diff)) {
                return swap ? new int[]{a - diff, a} : new int[]{a, a - diff};
            }
        }
        return null;
    }

    // 	243. Shortest Word Distance
    public static int shortestDistance(String[] words, String word1, String word2) {
        int len = words.length;
        int index = -1, distance = Integer.MAX_VALUE;
        for (int i = 0; i < len; ++i) {
            String word = words[i];
            if (word.equals(word1) || word.equals(word2)) {
                if (index != -1 && !words[index].equals(word)) {
                    distance = Math.min(distance, i - index);
                }
                index = i;
            }
        }
        return distance;
    }

    private static int shortestDistance0(String[] words, String word1, String word2) {
        int len = words.length;
        int index1 = -1, index2 = -1, distance = Integer.MAX_VALUE;
        for (int i = 0; i < len; ++i) {
            String word = words[i];
            if (word.equals(word1)) {
                index1 = i;
            } else if (word.equals(word2)) {
                index2 = i;
            }
            if ((index1 | index2) >= 0) {
                distance = Math.min(distance, index1 > index2 ? index1 - index2 : index2 - index1);
            }
        }
        return distance;
    }

    // 566. Reshape the Matrix : https://leetcode.com/problems/reshape-the-matrix/submissions/
    public int[][] matrixReshape(int[][] nums, int r, int c) {
        int rowLen = nums.length;
        int colLen = nums[0].length;
        if (rowLen * colLen != r * c) {
            return nums;
        }
        int[][] reshapeNums = new int[r][c];
        int row = 0, col = 0;
        for (int i = 0; i < rowLen; ++i) {
            for (int j = 0; j < colLen; ++j) {
                reshapeNums[row][col++] = nums[i][j];
                if (col == c) {
                    col = 0;
                    row = row + 1;
                }
            }
        }
        return reshapeNums;
    }

    // 766. Toeplitz Matrix : https://leetcode.com/problems/toeplitz-matrix/submissions/
    public static boolean isToeplitzMatrix(int[][] matrix) {
        int rowLimit = matrix.length - 1;
        if (rowLimit <= 0) {
            return true;
        }
        int columnLimit = matrix[0].length - 1;
        for (int row = 0; row < rowLimit; ++row) {
            int value = matrix[row][0];
            int minCol = Math.min(rowLimit - row, columnLimit);
            for (int col = 1; col <= minCol; ++col) {
                if (matrix[row + col][col] != value) {
                    return false;
                }
            }
        }
        for (int col = 1; col < columnLimit; ++col) {
            int value = matrix[0][col];
            int minRow = Math.min(columnLimit - col, rowLimit);
            for (int row = 1; row <= minRow; ++row) {
                if (matrix[row][col + row] != value) {
                    return false;
                }
            }
        }
        return true;
    }

    // 867. Transpose Matrix : https://leetcode.com/problems/transpose-matrix/submissions/
    public static int[][] transpose(int[][] A) {
        int rowLen = A.length;
        int columnLen = A[0].length;
        int[][] AA = new int[columnLen][rowLen];
        for (int i = 0; i < rowLen; ++i) {
            int[] array = A[i];
            for (int j = 0; j < columnLen; ++j) {
                AA[j][i] = array[j];
            }
        }
        return AA;
    }

    // 922. Sort Array By Parity II : https://leetcode.com/problems/sort-array-by-parity-ii/
    public static int[] sortArrayByParityII(int[] array) {
        int len = 0;
        if (array == null || ((len = array.length) & 1) == 1) {
            return null;
        }
        int even = 0;
        int odd = len - 1;
        while (true) {
            while (even < len && (array[even] & 1) == 0) {
                even += 2;
            }
            if (even == len) {
                break;
            }
            while (odd > -1 && (array[odd] & 1) == 1) {
                odd -= 2;
            }
            if (odd == -1) {
                break;
            }
            ArrayUtils.swap(array, even, odd);
        }
        return array;
    }

    // 509. Fibonacci Number : https://leetcode.com/problems/fibonacci-number/
    public static int fib(int N) {
        if (N < 0 || N > 30) {
            throw new IllegalArgumentException();
        }
        if (N < 2) {
            return N;
        }
        int first = 1;
        int second = 1;
        for (int i = 3; i <= N; ++i) {
            second = first + (first = second);
        }
        return second;
    }

    // 985. Sum of Even Numbers After Queries : https://leetcode.com/problems/sum-of-even-numbers-after-queries/submissions/
    public static int[] sumEvenAfterQueries(int[] A, int[][] queries) {
        int len = queries.length;
        int[] sumEvens = new int[len];
        int sum = 0;
        for (int a : A) {
            if ((a & 1) == 0) {
                sum += a;
            }
        }
        for (int i = 0; i < len; ++i) {
            int added = queries[i][0];
            int index = queries[i][1];
            int value = A[index];
            if ((value & 1) == 1 && ((A[index] = value + added) & 1) == 0) { // odd + even
                sumEvens[i] = sum += A[index];
                continue;
            }
            if ((value & 1) == 0 && ((A[index] = value + added) & 1) == 1) { // even + odd
                sumEvens[i] = sum -= value;
                continue;
            }
            if ((value & 1) == 0 && ((A[index] = value + added) & 1) == 0) { // even + even
                sumEvens[i] = sum += added;
                continue;
            }
            if ((value & 1) == 1 && ((A[index] = value + added) & 1) == 1) { // even + even
                sumEvens[i] = sum;
                continue;
            }
        }
        return sumEvens;
    }

    public static int getNum0(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than 0.");
        }
        int[] nums = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            if ((i & 1) == 1) {
                nums[i] = Math.min(nums[(i + 1) >> 1], nums[(i - 1) >> 1]) + 2;
            } else {
                nums[i] = nums[i >> 1] + 1;
            }
        }
        return nums[n];
    }

    public static int getNum1(int n) {
        if (n <= 1) {
            return 0;
        }
        if ((n & 1) == 1) {
            return Math.min(getNum1((n + 1) >>> 1), getNum1((n - 1) >>> 1)) + 2;
        } else {
            return getNum1(n >> 1) + 1;
        }
    }

    public boolean exist0(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;
        //这个数组记录了每个点是否使用过。这里不同于N皇后问题中只用了三个set，因为这里要精确到点，而不是整条边。
        boolean[][] used = new boolean[m][n];
        //得到word的字符数组，在之后的dfs过程中也是一直传递这个数组
        char[] wordChars = word.toCharArray();
        //对于每个点分别调用dfs,只有头字符相同时才调用，可以减少时间
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (wordChars[0] == board[i][j]) {
                    boolean res = search(board, wordChars, used, i, j, 0);
                    if (res) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean search(char[][] board, char[] word, boolean[][] used, int x, int y, int level) {
        //截止到此层之前都没有被终止（层数等于word长度），证明可以找到匹配的单词
        if (level == word.length) {
            return true;
        }
        //判断是否当前要判断的char根本不存在board中
        if (x >= board.length || x < 0 || y >= board[0].length || y < 0) {
            return false;
        }
        //当前char与当前word对应层的char不相同则直接返回
        if (word[level] != board[x][y]) {
            return false;
        }
        //如果当前元素被使用过了，则直接返回
        if (used[x][y] == true) {
            return false;
        }
        //表记当前元素为使用过
        used[x][y] = true;
        //注意这是有返回值情况下dfs的标准写法，dfs递归调用，只要有一个是true, 则最终结果是true!
        boolean result = search(board, word, used, x + 1, y, level + 1)
                || search(board, word, used, x, y + 1, level + 1)
                || search(board, word, used, x - 1, y, level + 1)
                || search(board, word, used, x, y - 1, level + 1);
        //恢复现场，删除标记
        used[x][y] = false;
        //返回结果
        return result;
    }

    public static boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0) {
            return false;
        }
        boolean found = false;
        char first = word.charAt(0);
        for (int row = 0, rowLimit = board.length; row < rowLimit; row++) {
            for (int col = 0, colLimit = board[row].length; col < colLimit; col++) {
                if (board[row][col] == first) {
                    found = search(board, word, 0, row, col);
                    if (found) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean search(char[][] board, String word, int index, int row, int col) {
        char cur = 0;
        if ((cur = board[row][col]) != word.charAt(index)) {
            return false;
        }
        if (index == word.length() - 1) {
            return true;
        }
        boolean found = false;
        board[row][col] = 0;
        if (col < board[row].length - 1) {
            found = search(board, word, index + 1, row, col + 1);
        }
        if (!found && row < board.length - 1) {
            found = search(board, word, index + 1, row + 1, col);
        }
        if (!found && col > 0) {
            found = search(board, word, index + 1, row, col - 1);
        }
        if (!found && row > 0) {
            found = search(board, word, index + 1, row - 1, col);
        }
        board[row][col] = cur;
        return found;
    }

    public static String reverseString(String s) {
        char[] letters = s.toCharArray();
        int len = letters.length;
        int remaining = len - 1;
        int limit = (remaining - 1) >> 1;
        for (int i = 0; i <= limit; i++) {
            char c1 = letters[i];
            char c2 = letters[remaining - i];
            letters[i] = c2;
            letters[remaining - i] = c1;
        }
        return String.valueOf(letters);
    }

    public static boolean canJump(int[] nums) {
        int maxIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (maxIndex >= i) {
                if (maxIndex < i + nums[i]) {
                    maxIndex = i + nums[i];
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public static int numJewelsInStones0(String J, String S) {
        boolean[] jewels = new boolean[123];
        for (char c : J.toCharArray()) {
            jewels[c] = true;
        }
        int count = 0;
        for (char c : S.toCharArray()) {
            if (jewels[c]) {
                count++;
            }
        }
        return count;
    }

    public static int numJewelsInStones(String J, String S) {
        HashMap<Character, Object> jewelMap = new HashMap<Character, Object>();
        for (int i = 0, limit = J.length(); i < limit; i++) {
            jewelMap.put(J.charAt(i), null);
        }
        int count = 0;
        for (int i = 0, limit = S.length(); i < limit; i++) {
            if (jewelMap.containsKey(S.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    public static int removeElement0(int[] nums, int val) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (nums[i] == val) {
                count++;
            } else {
                nums[i - count] = nums[i];
            }
        }
        return len - count;
    }

    public static int removeElement(int[] nums, int val) {
        int len = nums.length;
        int left = 0;
        int right = len - 1;
        while (left < right) {
            while (left < right && nums[left] != val) {
                left++;
            }
            while (left < right && nums[right] == val) {
                right--;
            }
            if (left < right) {
                nums[left++] = nums[right];
                nums[right--] = val;
            }
        }
        return nums[left] == val ? left : left + 1;
    }

    public static int searchInsert(int[] nums, int target) {
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            if (nums[i] >= target) {
                return i;
            }
        }
        return len;
    }

    public static boolean canMeasureWater(int x, int y, int z) {
        if (z > x && z > y) {
            return false;
        }
        return z % MathUtils.greatestCommonDivisor(x, y) == 0;
    }

    public int superPow(int a, int[] b) {
        // Build the list of possible mod values
        // list(i - 1) = a^i mod 1337
        long mod = (long) a;
        final long div = 1337L;
        final List<Integer> list = new ArrayList<>();
        final Set<Integer> used = new HashSet<>();
        int key;
        while (true) {
            mod %= div;
            key = (int) mod;
            // Is the list starting to repeat?
            if (used.contains(key)) break;
            list.add(key);
            used.add(key);
            mod *= a;
        }
        // Find the actual mod value from the list
        // BigInteger was used below to simplify (b mod list_size)
        // since this problem wasn't about large number division
        final StringBuilder sb = new StringBuilder();
        for (int d : b) sb.append(d);
        key = new java.math.BigInteger(sb.toString()).mod(java.math.BigInteger.valueOf(list.size())).intValue();
        if (key == 0) key = list.size();
        return list.get(key - 1);
    }

    public static int maximumSwap0(int num) {
        char[] digits = Integer.toString(num).toCharArray();
        int[] buckets = new int[10];
        for (int i = 0; i < digits.length; i++) {
            buckets[digits[i] - '0'] = i;
        }
        for (int i = 0; i < digits.length; i++) {
            for (int k = 9; k > digits[i] - '0'; k--) {
                if (buckets[k] > i) {
                    char tmp = digits[i];
                    digits[i] = digits[buckets[k]];
                    digits[buckets[k]] = tmp;
                    return Integer.valueOf(new String(digits));
                }
            }
        }
        return num;
    }

    public static int maximumSwap(int num) {
        char[] digits = Integer.toString(num).toCharArray();
        int len = digits.length;
        for (int i = 0, limit = len - 1; i < limit; i++) {
            int target = i;
            for (int j = i + 1; j < len; j++) {
                if (digits[j] >= digits[target]) {
                    target = j;
                }
            }
            if (target > i && digits[target] > digits[i]) {
                char c = digits[i];
                digits[i] = digits[target];
                digits[target] = c;
                return Integer.parseInt(new String(digits), 10);
            }
        }
        return num;
    }

    // 四条直线和两条直线相等
    public static boolean validSquare0(int[] p1, int[] p2, int[] p3, int[] p4) {
        HashSet<Integer> hs = new HashSet<>(Arrays.asList(distance(p1, p2), distance(p1, p3),
                distance(p1, p4), distance(p2, p3), distance(p2, p4), distance(p3, p4)));
        return !hs.contains(0) && hs.size() == 2;
    }

    private static int distance(int[] a, int[] b) {
        return (a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]);
    }

    public static boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
        int[] distance = new int[6];
        distance[0] = (int) (Math.pow((p1[0] - p2[0]), 2) + Math.pow((p1[1] - p2[1]), 2));
        distance[1] = (int) (Math.pow((p1[0] - p3[0]), 2) + Math.pow((p1[1] - p3[1]), 2));
        distance[2] = (int) (Math.pow((p1[0] - p4[0]), 2) + Math.pow((p1[1] - p4[1]), 2));
        distance[3] = (int) (Math.pow((p2[0] - p3[0]), 2) + Math.pow((p2[1] - p3[1]), 2));
        distance[4] = (int) (Math.pow((p2[0] - p4[0]), 2) + Math.pow((p2[1] - p4[1]), 2));
        distance[5] = (int) (Math.pow((p3[0] - p4[0]), 2) + Math.pow((p3[1] - p4[1]), 2));
        Arrays.sort(distance);
        int d1 = distance[0];
        for (int i = 1; i <= 3; i++) {
            if (distance[i] != d1) {
                return false;
            }
        }
        if (distance[4] != distance[5] || distance[4] == d1) {
            return false;
        }
        return true;
    }

    private static final String[] to20 = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    private static final String[] tees = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

    public static String numberToWords(int num) {
        if (num == 0) {
            return "Zero";
        }
        return numberToWords0(num);
    }

    public static String numberToWords0(int num) {
        if (0 <= num && num < 20) {
            return to20[num];
        } else if (20 <= num && num < 100) {
            return (tees[num / 10] + " " + to20[num % 10]).trim();
        } else if (100 <= num && num < 1000) {
            return (to20[num / 100] + " Hundred " + numberToWords0(num % 100)).trim();
        } else if (1000 <= num && num < 1000000) {
            return (numberToWords0(num / 1000) + " Thousand " + numberToWords0(num % 1000)).trim();
        } else if (1000000 <= num && num < 1000000000) {
            return (numberToWords0(num / 1000000) + " Million " + numberToWords0(num % 1000000)).trim();
        } else if (1000000000 <= num) {
            return (numberToWords0(num / 1000000000) + " Billion " + numberToWords0(num % 1000000000)).trim();
        }
        throw new IllegalArgumentException("The value of num exceeds the program limit.");
    }

    public static int integerReplacement1(int n) {
        int count = 0;
        while (n != 1) {
            if ((n & 1) == 1) {
                int n1 = n - 1;
                int n2 = n + 1;
                if (n == 3 || ((n >>> 1) & 1) == 0) { // Integer.bitCount(n2) > Integer.bitCount(n1)
                    n = n1;
                } else {
                    n = n2;
                }
            } else {
                n >>>= 1;
            }
            ++count;
        }
        return count;
    }

    public static int integerReplacement0(int n) {
        int count = 0;
        while (n != 1) {
            if ((n & 1) == 1) {
                int n1 = n - 1;
                int n2 = n + 1;
                if (Integer.bitCount(n2) < Integer.bitCount(n1) ||
                        (Integer.bitCount(n2) == Integer.bitCount(n1) &&
                                Integer.lowestOneBit(n2) >= Integer.lowestOneBit(n1) &&
                                Integer.highestOneBit(n2) == Integer.highestOneBit(n1))) {
                    n = n2;
                } else {
                    n = n1;
                }
            } else {
                n >>>= 1;
            }
            ++count;
        }
        return count;
    }

    public static int integerReplacement(int n) {
        if (n == Integer.MAX_VALUE) {
            return 32;
        }
        if (n <= 1) {
            return 0;
        }
        if ((n & 1) == 1) {
            return 1 + Math.min(integerReplacement(n + 1), integerReplacement(n - 1));
        } else {
            return 1 + integerReplacement(n >>> 1);
        }
    }

    public static int integerBreak0(int n) {
        // You may assume that n is not less than 2 and not larger than 58.
        if (n < 2 || n > 58) {
            throw new IllegalArgumentException("N is not less than 2 and not larger than 58.");
        }
        int[] dp = new int[n + 1];
        dp[1] = 1;
        for (int i = 2; i <= n; ++i) {
            for (int j = 1; j <= i; ++j) {
                dp[i] = Math.max(dp[i], Math.max(j * (i - j), j * dp[i - j]));
            }
        }
        return dp[n];
    }

    public static int integerBreak(int n) {
        // You may assume that n is not less than 2 and not larger than 58.
        if (n < 2 || n > 58) {
            throw new IllegalArgumentException("N is not less than 2 and not larger than 58.");
        }
        if (n <= 3) {
            return n - 1;
        }
        int count = n / 3;
        int accum = n % 3;
        if (accum == 0) {
            return (int) Math.pow(3, count);
        }
        if (accum == 1) {
            count--;
            accum = 4;
        }
        return (int) (Math.pow(3, count) * accum);
    }

    public static int findNthDigit0(int n) {
        long[] s = {1, 10, 190, 2890, 38890, 488890, 5888890, 68888890, 788888890, 8888888890L};
        int b = (int) Math.log10(n);
        int i = n >= s[b] ? b : b - 1;
        long r = (n - s[i]) % (i + 1);
        return (int) ((r == 0 ? 1 : 0) + (n - s[i]) / (i + 1) / Math.pow(10, i - r)) % 10;
    }

    public static int findNthDigit(int n) {
        int bit = 1;
        int range = 9;
        int tenPower = 1;
        while (n > range && range > 0) {
            n = n - range;
            range = (9 * (tenPower = (int) Math.pow(10, bit)) * ++bit); // range = 9 * (bit + 1) * 10 ^ bit
        }
        n = n - 1;
        int num = tenPower + n / bit;  // 第几个数字
        int shiftBit = n % bit; // 数字的第几位
        return (num / (int) Math.pow(10, bit - shiftBit - 1)) % 10;
    }

    public static int missingNumber(int[] nums) {
        int len = nums.length;
        if (len == 0) {
            return 0;
        }
        boolean[] exists = new boolean[len + 1];
        for (int num : nums) {
            exists[num] = true;
        }
        for (int i = 0; i <= len; i++) {
            if (!exists[i]) {
                return i;
            }
        }
        return len + 1;
    }

    public static boolean isPowerOfThree(int n) {
        return n > 0 && 1162261467 % n == 0;
    }

    public static int mySqrt(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("The value of x must be a positive integer.");
        }
        if (x <= 1) {
            return x;
        }
        int left = 0;
        int right = x - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            int midValue = mid == 0 ? 1 : (x / mid) - mid; // mid * mid会发生精度溢出
            if (midValue > 0) {
                left = mid + 1;
            } else if (midValue == 0) {
                return mid;
            } else {
                right = mid - 1;
            }
        }
        return right;
    }

    public static long countDigitOne(int n) {
        long ones = 0;
        int limit = Integer.MAX_VALUE / 10;
        for (long m = 1; m <= n; m *= 10) {
            long a = n / m;
            long b = n % m;
            ones += (a + 8) / 10 * m + (a % 10 == 1 ? b + 1 : 0);
            if (m > limit) {
                break;
            }
        }
        return ones;
    }

    public static int countPrimes0(int n) {
        if (n <= 1) {
            return 0;
        }
        if (n == 2) {
            return 0;
        }
        boolean[] prime = new boolean[n];
        int count = 1;
        for (int i = 3, square = (int) Math.sqrt(n); i < n; i += 2) {
            count += prime[i] ? 0 : 1;
            if (i > square) {
                continue;
            }
            for (int j = i * i; j < n; j += i) {
                prime[j] = true;
            }
        }
        return count;
    }

    public static int countPrimes(int n) {
        if (n <= 1) {
            return 0;
        }
        if (n == 2) {
            return 0;
        }
        boolean[] prime = new boolean[n + 1];
        for (int i = 3, sqrtN = (int) Math.sqrt(n); i <= sqrtN; i += 2) {
            for (int j = i * i; j <= n; j += i) {
                prime[j] = true;
            }
        }
        int count = n - (n >> 1);
        for (int i = 3; i <= n; i += 2) {
            if (prime[i]) {
                --count;
            }
        }
        return !prime[n] && (n & 1) == 1 ? count - 1 : count;
    }

    // breadth first search
    public static int numSquares2(int n) {
        if (n <= 0) {
            return 0;
        }
        int count = 0;
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.offer(n);
        while (!q.isEmpty()) {
            for (int size = q.size(); size > 0; --size) {
                int num = q.poll();
                for (int i = 1, square; (square = i * i) <= num; ++i) {
                    if (num == square) {
                        return ++count;
                    }
                    q.offer(num - square);
                }
            }
            ++count;
        }
        return -1;
    }

    public static int numSquares1(int n) {
        if (n <= 0) {
            return 0;
        }
        int[] nums = new int[n + 1];
        for (int i = 1, minLen; i <= n; ++i) {
            minLen = i;
            for (int j = 1, square, len; (square = j * j) <= i; ++j) {
                len = nums[i - square] + 1;
                if (len < minLen) {
                    minLen = len;
                }
            }
            nums[i] = minLen;
        }
        return nums[n];
    }

    public static int numSquares0(int n) {
        // n = x ^ 2
        if (isPerfectSquareNum(n)) {
            return 1;
        }
        // n = 4 ^ k * (8 * m + 7)
        while ((n & 3) == 0) {// n % 4 == 0
            n >>= 2;
        }
        if ((n & 7) == 7) {// n % 8 == 7
            return 4;
        }
        // n = x ^ 2 + y ^ 2
        for (int i = 1, sqrtN = (int) (Math.sqrt(n >> 1)); i <= sqrtN; i++) {
            if (isPerfectSquareNum(n - i * i)) {
                return 2;
            }
        }
        return 3;
    }

    public static int numSquares(int n) {
        int sqrtN = (int) Math.sqrt(n);
        return numSquares(sqrtN, n);
    }

    private static int numSquares(int begin, int remaining) {
        int square = 1;
        int minLen = remaining;
        int len = 0;
        for (int i = begin; remaining > 0 && i >= 1; --i) {
            square = i * i;
            remaining -= square;
            len = numSquares((int) Math.sqrt(remaining), remaining) + 1;
            if (len < minLen) {
                minLen = len;
            }
            remaining += square;
        }
        return minLen;
    }

    private static boolean isPerfectSquareNum(int n) {
        //		if(n <= 0){
        //			throw new IllegalArgumentException("The value of n must be a positive integer.");
        //		}
        //		// 0,1,4,5,6,9
        //		int unitDigit = n % 10;
        //		if(unitDigit == 2 || unitDigit == 3 || unitDigit == 7 || unitDigit == 8){
        //			return false;
        //		}
        //		for(int i = 1; n > 0; i += 2){
        //			n -= i;
        //		}
        //		return n == 0;
        int sqrt = (int) Math.sqrt(n);
        return sqrt * sqrt == n;
    }

    public static int nthUglyNumber0(int n) {
        if (n == 1) return 1;
        PriorityQueue<Integer> q = new PriorityQueue<Integer>();
        q.add(1);

        for (long i = 1; i < n; i++) {
            int tmp = q.poll();
            while (!q.isEmpty() && q.peek() == tmp) {
                tmp = q.poll();
            }
            q.add(tmp * 2);
            q.add(tmp * 3);
            q.add(tmp * 5);
        }
        return q.poll().intValue();
    }

    // https://www.geeksforgeeks.org/ugly-numbers/
    public static int nthUglyNumber(int n) {
        int[] uglyNums = new int[n];
        uglyNums[0] = 1;
        int i2 = 0, i3 = 0, i5 = 0;
        int nextMultipleOfTwo = 2, nextMultipleOfThree = 3, nextMultipleOfFive = 5;
        int nextUglyNumber = 1;
        for (int i = 1; i < n; i++) {
            nextUglyNumber = Math.min(Math.min(nextMultipleOfTwo, nextMultipleOfThree), nextMultipleOfFive);
            uglyNums[i] = nextUglyNumber;
            if (nextUglyNumber == nextMultipleOfTwo) {
                nextMultipleOfTwo = uglyNums[++i2] * 2;
            }
            if (nextUglyNumber == nextMultipleOfThree) {
                nextMultipleOfThree = uglyNums[++i3] * 3;
            }
            if (nextUglyNumber == nextMultipleOfFive) {
                nextMultipleOfFive = uglyNums[++i5] * 5;
            }
        }
        return nextUglyNumber;
    }

    // Ugly numbers are positive numbers whose prime factors only include 2, 3, 5.
    public static boolean isUgly(int num) {
        if (num <= 0) {
            return false;
        }
        final int[] array = {2, 3, 5};
        for (int prime : array) {
            while (num % prime == 0) {
                num /= prime;
            }
        }
        return num == 1;
    }

    public static String addBinary(String a, String b) {
        if (a.length() < b.length()) {
            String t = a;
            a = b;
            b = t;
        }
        int maxLen = a.length();
        int minLen = b.length();
        int diff = maxLen - minLen;
        char[] aBinary = a.toCharArray();
        char[] bBinary = new char[maxLen];
        Arrays.fill(bBinary, 0, diff, '0');
        System.arraycopy(b.toCharArray(), 0, bBinary, diff, minLen);
        char carry = 0;
        for (int i = maxLen - 1; i >= 0; --i) {
            char c1 = aBinary[i];
            char c2 = bBinary[i];
            int sum = c1 + c2 + carry - 96;
            if (sum > 2) {
                aBinary[i] = '1';
                carry = 1;
            } else if (sum == 2) {
                aBinary[i] = '0';
                carry = 1;
            } else {
                aBinary[i] = (char) (48 + sum);
                carry = 0;
            }
        }
        if (carry == 1) {
            char[] newBinary = new char[maxLen + 1];
            System.arraycopy(aBinary, 0, newBinary, 1, maxLen);
            newBinary[0] = '1';
            return String.valueOf(newBinary);
        }
        return String.valueOf(aBinary);
    }

    public static boolean isNumber(String s) {
        /**boolean isE = false, isDot = false, isDigit = false;
         s = s.trim();
         for(int i = 0; i < s.length(); i++) {
         char c = s.charAt(i);
         if(c == 'e') {
         if(!isDigit || isE) return false;
         isDigit = false;
         isE = true;
         } else if(c == '+' || c == '-') {
         if(i != 0 && s.charAt(i-1) != 'e') return false;
         } else if(c == '.') {
         if(isE || isDot) return false;
         isDot = true;
         } else if(Character.isDigit(c)) {
         isDigit = true;
         } else {
         return false;
         }
         }
         return s.length() > 0 && isDigit;*/
        return s.matches("[+-]?\\d+(e-?)?\\d+");
    }

    public static boolean isHappy0(int n) {
        HashSet<Integer> map = new HashSet<>(Arrays.asList(noHappyNumber));
        if (map.contains(n)) {
            return false;
        }
        int fast = n, slow = n;
        do {
            slow = digitSquareSum(slow);
            fast = digitSquareSum(fast);
            fast = digitSquareSum(fast);
        } while (fast != slow && !(map.contains(slow) || map.contains(fast)));
        return slow == 1 ? true : false;
    }

    private static int digitSquareSum(int n) {
        int digit = 0;
        int sum = 0;
        while (n > 0) {
            digit = n % 10;
            sum += digit * digit;
            n = n / 10;
        }
        return sum;
    }

    //  4 → 16 → 37 → 58 → 89 → 145 → 42 → 20 → 4
    private static final Integer[] noHappyNumber = {4, 16, 37, 58, 89, 145, 42, 20};

    public static boolean isHappy(int n) {
        HashSet<Integer> map = new HashSet<>(Arrays.asList(noHappyNumber));
        while (!map.contains(n)) {
            char[] cs = Integer.toString(n).toCharArray();
            n = 0;
            for (int i = 0, limit = cs.length; i < limit; i++) {
                int value = Character.getNumericValue(cs[i]);
                n += value * value;
            }
            if (n == 1) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValid(String s) {
        int len = s.length();
        if (len == 0) {
            return true;
        }
        if ((len & 1) == 1) {
            return false;
        }
        LinkedList<Character> list = new LinkedList<Character>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                list.add(c);
                continue;
            }
            if (list.size() == 0) {
                return false;
            }
            if (c == ')' || c == ']' || c == '}') {
                int diff = c - list.pollLast();
                if (diff != 1 && diff != 2) {
                    return false;
                }
            }
        }
        return list.size() == 0 ? true : false;
    }

    public static int[] plusOne0(int[] digits) {
        int len = digits.length;
        for (int i = len - 1; i >= 0; i--) {
            if (digits[i] < 9) {
                digits[i]++;
                return digits;
            }
            digits[i] = 0;
        }
        int[] newDigits = new int[len + 1];
        System.arraycopy(digits, 0, newDigits, 1, len);
        newDigits[0] = 1;
        return newDigits;
    }

    public static int[] plusOne(int[] digits) {
        int len = digits.length;
        if (digits[len - 1] + 1 < 10) {
            digits[len - 1] += 1;
            return digits;
        }
        digits[len - 1] = 0;
        int isAdd = 1;
        for (int i = len - 2; i >= 0; i--) {
            int digit = digits[i];
            int newDigit = digit + isAdd;
            if (newDigit == 10) {
                digits[i] = 0;
            } else {
                digits[i] = newDigit;
                return digits;
            }
        }
        if (isAdd == 1) {
            int[] newDigits = new int[len + 1];
            System.arraycopy(digits, 0, newDigits, 1, len);
            newDigits[0] = 1;
            return newDigits;
        }
        return digits;
    }

    public static ListNode removeNthFromEnd0(ListNode head, int n) {
        ListNode start = new ListNode(0);
        ListNode slow = start, fast = start;
        slow.next = head;
        //Move fast in front so that the gap between slow and fast becomes n
        for (int i = 1, limit = n + 1; i <= limit; i++) {
            fast = fast.next;
        }
        //Move fast to the end, maintaining the gap
        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
        }
        //Skip the desired node
        slow.next = slow.next.next;
        return start.next;
    }

    public static ListNode removeNthFromEnd(ListNode head, int n) {
        if (n == 0) {
            return head;
        }
        int len = 0;
        ListNode traverse = head;
        while (traverse != null) {
            traverse = traverse.next;
            len = len + 1;
        }
        int pos = len - n + 1;
        ListNode last = pos == 1 ? null : head;
        ListNode target = pos == 1 ? head : head;
        while (--pos >= 1) {
            last = target;
            target = last.next;
        }
        ListNode next = target.next;
        if (last == null) {
            head = next;
        } else {
            last.next = next;
        }
        return head;
    }

    public static int firstMissingPositive1(int[] nums) {
        int len = nums.length;
        int[] extraArray = new int[len + 1];
        for (int num : nums) {
            if (num > 0 || num <= len) {
                extraArray[num] = 1;
            }
        }
        for (int i = 1; i <= len; i++) {
            if (extraArray[i] == 0) {
                return i;
            }
        }
        return len + 1;
    }

    public static int firstMissingPositive0(int[] nums) {
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int num : nums) {
            if (num > 0) {
                set.add(num);
            }
        }
        int missingPositive = 1;
        for (int num : set) {
            if (num > missingPositive) {
                return missingPositive;
            } else if (num == missingPositive) {
                missingPositive++;
            }
        }
        return missingPositive;
    }

    public static int firstMissingPositive(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        int compare = 1;
        for (int i = 0; i < len; i++) {
            int num = nums[i];
            if (num <= 0) {
                continue;
            }
            if (num > compare) {
                return compare;
            } else if (num == compare) {
                compare++;
            }
        }
        return compare;
    }

    public static double myPow0(double x, int n) {
        if (n == 0) {
            return 1;
        }
        double halfPow = myPow0(x, n / 2);
        if ((n & 1) == 0)
            return halfPow * halfPow;
        else if (n > 0) {
            return x * halfPow * halfPow;
        } else {
            return (halfPow * halfPow) / x;
        }
    }

    public static double myPow(double x, int n) {
        if (x <= -100.0 || x >= 100.0) {
            throw new IllegalArgumentException("x = " + x);
        }
        if (n == 0) {
            return 1;
        }
        if (x == 0) {
            return 0;
        }
        int absN = Math.abs(n);
        double result = 1;
        while (absN != 0) {
            if ((absN & 1) == 1) {
                result *= x;
            }
            x *= x;
            absN >>>= 1;
        }
        return n > 0 ? result : 1 / result;
    }

    public static int divide1(int dividend, int divisor) {
        if (dividend == -2147483648 && divisor == -1) {
            // This code is required by Leetcode.
            // The result is not accord with the fact.
            return 2147483647;
        }
        if (divisor == 0) {
            throw new ArithmeticException(" / by zero");
        }
        int signum = dividend ^ divisor;
        int absDividend = Math.abs(dividend);
        int absDivisor = Math.abs(divisor);
        int quotient = 0;
        while (absDividend - absDivisor >= 0) {
            int twoPowMulDivisor = absDivisor, multiple = 1;
            while (absDividend - (twoPowMulDivisor << 1) >= 0) {
                twoPowMulDivisor <<= 1;
                multiple <<= 1;
            }
            absDividend -= twoPowMulDivisor;
            quotient += multiple;
        }
        return signum >= 0 ? quotient : -quotient;
    }

    public static int divide0(int dividend, int divisor) {
        if (dividend == -2147483648 && divisor == -1) {
            // This code is required by Leetcode.
            // The result is not accord with the fact.
            return 2147483647;
        }
        if (divisor == 0) {
            throw new ArithmeticException(" / by zero");
        }
        if (dividend == 0 || (divisor == -divisor && divisor != dividend)) {
            return 0;
        }
        int signum = dividend ^ divisor;
        dividend = dividend < 0 ? -dividend : dividend;
        divisor = divisor < 0 ? -divisor : divisor;
        if (dividend != -dividend && divisor > dividend) {
            return 0;
        }
        if (divisor == 1) {
            return signum < 0 ? -dividend : dividend;
        }
        // dividend > divisor or dividend = -2147482648
        int twoExp = 0;
        int twoPowMulDivisor = divisor;
        while (dividend - twoPowMulDivisor >= 0) {
            twoPowMulDivisor = (divisor << ++twoExp);
        }
        // The exponent of two must be greater than 0.
        int quotient = 1 << (--twoExp);
        dividend -= (twoPowMulDivisor >>>= 1);
        for (int temp = 0; ; ) {
            if ((temp = dividend - twoPowMulDivisor) >= 0) {
                dividend = temp;
                quotient += (1 << twoExp);
            } else {
                twoPowMulDivisor >>>= 1;
                if (--twoExp < 0) {
                    break;
                }
            }
        }
        return signum < 0 ? -quotient : quotient;
    }

    public static int divide(int dividend, int divisor) {
        if (divisor == 0) {
            throw new ArithmeticException(" / by zero");
        }
        if (dividend == 0 || divisor == -divisor) {
            return 0;
        }
        int and = dividend ^ divisor;
        dividend = dividend < 0 ? -dividend : dividend;
        divisor = divisor < 0 ? -divisor : divisor;
        if (dividend != -dividend && divisor > dividend) {
            return 0;
        }
        int result = 0;
        for (int temp = 0; (temp = dividend - divisor) >= 0; ++result) {
            dividend = temp;
        }
        return and < 0 ? -result : result;
    }


    public static List<String> letterCombinations(String digits) {
        int len = digits.length();
        if (len == 0) {
            return new ArrayList<String>();
        }
        if (!digits.matches("[2-9]+")) {
            throw new IllegalArgumentException("The string of digits contains an illegal digit.");
        }
        String[] digitLetterArray = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        if (len == 1) {
            String letters = digitLetterArray[digits.charAt(0) - 48];
            List<String> list = new ArrayList<String>(letters.length());
            for (char c : letters.toCharArray()) {
                list.add(String.valueOf(c));
            }
            return list;
        }
        char[] chars = digits.toCharArray();
        int size = 1;
        List<char[]> letterList = new ArrayList<char[]>(len);
        for (char digit : chars) {
            String letter = digitLetterArray[digit - 48];
            size *= letter.length();
            letterList.add(letter.toCharArray());
        }
        int counterIndex = len - 1;
        int[] counter = new int[len];
        List<String> list = new ArrayList<String>(size);
        char[] ch = new char[len];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < len; j++) {
                ch[j] = letterList.get(j)[counter[j]];
            }
            handle(counter, counterIndex, letterList);
            list.add(new String(ch));
        }
        return list;
    }

    private static void handle(int[] counter, int counterIndex, List<char[]> list) {
        counter[counterIndex]++;
        if (counter[counterIndex] >= list.get(counterIndex).length) {
            counter[counterIndex] = 0;
            counterIndex--;
            if (counterIndex >= 0) {
                handle(counter, counterIndex, list);
            }
            counterIndex = list.size() - 1;
        }
    }

    public static int threeSumClosest0(int[] nums, int target) {
        int len = nums.length;
        if (len < 3) {
            throw new IllegalArgumentException("The length of nums must be greater than or equal to three.");
        }
        Arrays.sort(nums);
        int diff = (nums[0] + nums[1] + nums[2] - target);
        for (int one = 0; one < len; one++) {
            while (one > 0 && one < len && nums[one] == nums[one - 1]) {
                one++;
            }
            if (one == len) {
                break;
            }
            int two = one + 1;
            int three = len - 1;
            while (two < three) {
                int sum = nums[one] + nums[two] + nums[three];
                if (sum == target) {
                    return target;
                } else if (sum > target) {
                    diff = Math.abs(diff - target) > Math.abs(sum - target) ? sum : diff;
                    three--;
                    while (two < three && nums[three + 1] == nums[three]) {
                        three--;
                    }
                } else {
                    diff = Math.abs(diff - target) > Math.abs(sum - target) ? sum : diff;
                    two++;
                    while (two < three && nums[two - 1] == nums[two]) {
                        two++;
                    }
                }
            }
        }
        return diff;
    }

    public static int threeSumClosest(int[] nums, int target) {
        int len = nums.length;
        if (len < 3) {
            throw new IllegalArgumentException("The length of nums must be greater than or equal to three.");
        }
        Arrays.sort(nums);
        int diff = (nums[0] + nums[1] + nums[2] - target);
        for (int one = 0, limit = len - 3; one <= limit; one++) {
            int two = one + 1;
            int three = len - 1;
            while (two < three) {
                int sum = nums[one] + nums[two] + nums[three];
                if (sum == target) {
                    return target;
                } else if (sum > target) {
                    diff = Math.abs(diff - target) > Math.abs(sum - target) ? sum : diff;
                    three--;
                } else {
                    diff = Math.abs(diff - target) > Math.abs(sum - target) ? sum : diff;
                    two++;
                }
            }
        }
        return diff;
    }

    public static List<List<Integer>> threeSum1(int[] nums) {
        int len = nums.length;
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            while (i > 0 && i < len && nums[i] == nums[i - 1]) {
                i++;
            } // avoid duplicates
            if (i == len) {
                break;
            }
            int left = i + 1, right = len - 1, target = -nums[i];
            while (left < right) {
                int sum = nums[left] + nums[right];
                if (sum < target) {
                    left++;
                } else if (sum > target) {
                    right--;
                } else {
                    result.add(Arrays.asList(nums[i], nums[left++], nums[right--]));
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    } // avoid duplicates
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    } // avoid duplicates
                }
            }
        }
        return result;
    }

    public static List<List<Integer>> threeSum0(int[] nums) {
        Set<List<Integer>> res = new HashSet<>();
        if (nums.length == 0) return new ArrayList<>(res);
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            int j = i + 1;
            int k = nums.length - 1;
            while (j < k) {
                int sum = nums[i] + nums[j] + nums[k];
                if (sum == 0) res.add(Arrays.asList(nums[i], nums[j++], nums[k--]));
                else if (sum > 0) k--;
                else if (sum < 0) j++;
            }
        }
        return new ArrayList<>(res);
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        boolean hasZero = false;
        HashMap<Integer, Integer> positiveMap = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> negativeMap = new HashMap<Integer, Integer>();
        int zeroNum = 0;
        for (int num : nums) {
            if (num == 0) {
                hasZero = true;
                zeroNum++;
            } else if (num > 0) {
                Integer count = positiveMap.get(num);
                positiveMap.put(num, count != null ? count + 1 : 1);
            } else {
                Integer count = negativeMap.get(num);
                negativeMap.put(num, count != null ? count + 1 : 1);
            }
        }
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if (hasZero) {
            for (Map.Entry<Integer, Integer> entry : positiveMap.entrySet()) {
                int positive = entry.getKey();
                if (negativeMap.containsKey(-positive)) {
                    result.add(toList(new int[]{positive, 0, -positive}));
                }
            }
            if (zeroNum >= 3) {
                result.add(toList(new int[]{0, 0, 0}));
            }
        }
        for (Map.Entry<Integer, Integer> nEntry : negativeMap.entrySet()) {
            int negative = nEntry.getKey();
            int positive = -nEntry.getKey();
            HashMap<Integer, Object> verifyMap = new HashMap<Integer, Object>();
            for (Map.Entry<Integer, Integer> pEntry : positiveMap.entrySet()) {
                int one = pEntry.getKey();
                if (verifyMap.containsKey(one)) {
                    continue;
                }
                int two = positive - one;
                if (one == two) {
                    if (pEntry.getValue() >= 2) {
                        verifyMap.put(two, null);
                        result.add(toList(new int[]{negative, one, one}));
                    }
                    continue;
                }
                if (positiveMap.containsKey(two)) {
                    result.add(toList(new int[]{negative, one, two}));
                }
                verifyMap.put(two, null);
            }
        }
        for (Map.Entry<Integer, Integer> nEntry : positiveMap.entrySet()) {
            int positive = nEntry.getKey();
            int negative = -nEntry.getKey();
            HashMap<Integer, Object> verifyMap = new HashMap<Integer, Object>();
            for (Map.Entry<Integer, Integer> pEntry : negativeMap.entrySet()) {
                int one = pEntry.getKey();
                if (verifyMap.containsKey(one)) {
                    continue;
                }
                int two = negative - one;
                if (one == two) {
                    if (pEntry.getValue() >= 2) {
                        verifyMap.put(two, null);
                        result.add(toList(new int[]{one, one, positive}));
                    }
                    continue;
                }
                if (negativeMap.containsKey(two)) {
                    result.add(toList(new int[]{one, two, positive}));
                }
                verifyMap.put(two, null);
            }
        }
        return result;
    }

    private static List<Integer> toList(int[] array) {
        List<Integer> list = new ArrayList<Integer>(3);
        for (int a : array) {
            list.add(a);
        }
        return list;
    }

    public static String longestCommonPrefix(String[] strs) {
        int strNum = strs.length;
        if (strNum <= 1) {
            return strNum == 0 ? "" : strs[0];
        }
        char[] firsts = strs[0].toCharArray();
        int index = 0;
        for (int i = 0, limit = firsts.length; i < limit; i++) {
            char first = firsts[i];
            for (int j = 1; j < strNum; j++) {
                String others = strs[j];
                if (others.length() <= i || others.charAt(i) != first) {
                    return String.valueOf(Arrays.copyOf(firsts, index));
                }
            }
            index++;
        }
        return String.valueOf(Arrays.copyOf(firsts, index));
    }

    private static final char[] Symbols = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};

    private static final int[] Values = {1, 5, 10, 50, 100, 500, 1000};

    public static int romanToInt(String s) {
        char[] chars = s.toCharArray();
        int len = chars.length;
        HashMap<Character, Integer> svMap = new HashMap<Character, Integer>();
        for (int i = 0; i < 7; i++) {
            svMap.put(Symbols[i], Values[i]);
        }
        int result = 0;
        for (int i = len - 1; i >= 0; i--) {
            char curRomanSymbol = chars[i];
            Integer curValue = svMap.get(curRomanSymbol);
            if (curValue == null) {
                throw new IllegalArgumentException("This string contains non-Roman characters.");
            }
            int next = i - 1;
            if (next >= 0) {
                char nextRomanSymbol = chars[next];
                Integer nextValue = svMap.get(nextRomanSymbol);
                if (nextValue == null) {
                    throw new IllegalArgumentException("This string contains non-Roman characters.");
                }
                if (nextValue < curValue) { // 可能是4或9
                    if (nextValue * 5 == curValue) { // 4
                        result -= curValue - (nextValue << 2);
                    } else { // 9
                        result -= curValue - (nextValue * 9);
                    }
                    i--;
                }
            }
            result += curValue;
        }
        return result;
    }

    public static String intToRoman(int num) {
        if (num < 1 || num > 3999) {
            throw new IllegalArgumentException("The value of num must be within the range from 1 to 3999.");
        }
        char[] digits = String.valueOf(num).toCharArray();
        int len = digits.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0, remaining = len - 1; i < len; i++) {
            int digit = digits[i] - 48;
            int index = (remaining - i) << 1;
            char one = Symbols[index];
            char five = index == 6 ? '0' : Symbols[index + 1];
            char nextOne = index == 6 ? '0' : Symbols[index + 2];
            if (digit == 9) {
                sb.append(one);
                sb.append(nextOne);
            } else if (digit >= 5) {
                sb.append(five);
                int count = digit - 5;
                while (--count >= 0) {
                    sb.append(one);
                }
            } else if (digit == 4) {
                sb.append(one);
                sb.append(five);
            } else {
                while (--digit >= 0) {
                    sb.append(one);
                }
            }
        }
        return sb.toString();
    }

    public static int maxArea(int[] height) {
        int len = height.length;
        int maxArea = 0;
        int left = 0, right = len - 1;
        while (left < right) {
            int leftHeight = height[left];
            int rightHeight = height[right];
            int area = (right - left) * (leftHeight > rightHeight ? rightHeight : leftHeight);
            maxArea = maxArea < area ? area : maxArea;
            if (leftHeight > rightHeight) {
                right--;
            } else {
                left++;
            }
        }
        return maxArea;
    }

    // '.' Matches any single character.
    // '*' Matches zero or more of the preceding element.
    public static boolean isMatch(String s, String p) {
        String dotReplace = "([\\s\\S]{1})";
        char[] chars = p.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = chars.length; i < len; i++) {
            char c = chars[i];
            if (c == '.') {
                sb.append(dotReplace);
            } else {
                sb.append(c);
            }
        }
        System.out.println(sb);
        return s.matches(sb.toString());
    }

    public static int myAtoi(String str) {
        str = str.trim();
        String regex = "^([\\-|\\+]?)(0+)?(\\d+)(.*?)$";
        Matcher matcher = Pattern.compile(regex).matcher(str);
        String sign = null;
        String num = null;
        while (matcher.find()) {
            sign = matcher.group(1);
            num = matcher.group(3);
        }
        boolean negative = false;
        if (sign != null && sign.length() == 1 && sign.charAt(0) == '-') {
            negative = true;
        }
        int numLen = 0;
        if (num == null || (numLen = num.length()) == 0) {
            return 0;
        }
        if (numLen > 10) {
            return negative ? -2147483648 : 2147483647;
        }
        if (num.length() == 10) {
            char[] intChars = {'2', '1', '4', '7', '4', '8', '3', '6', '4', '8'};
            for (int i = 0; i < 10; i++) {
                if (num.charAt(i) > intChars[i]) {
                    return negative ? -2147483648 : 2147483647;
                } else if (num.charAt(i) < intChars[i]) {
                    return negative ? -Integer.parseInt(num) : Integer.parseInt(num);
                }
            }
            if (num.charAt(9) == '8') {
                return negative ? -2147483648 : 2147483647;
            }
        }
        return negative ? -Integer.parseInt(num) : Integer.parseInt(num);
    }

    public static boolean isPalindrome(String str) {
        char[] chars = str.toCharArray();
        int len = chars.length;
        if (len <= 1) {
            return true;
        }
        int limit = (len >> 1) - 1;
        for (int i = 0, remaining = len - 1; i <= limit; i++) {
            if (chars[i] != chars[remaining - i]) {
                return false;
            }
        }
        return true;
    }

    public static String longestPalindrome(String s) {
        int len = s.length();
        if (len <= 1) {
            return s;
        }
        char[] chars = s.toCharArray();
        int from = 0;
        int to = 0;
        for (int i = 1; i < len; i++) {
            for (int j = 0; j + i < len; j++) {
                if (isPalindrome(chars, j, j + i)) {
                    from = j;
                    to = j + i;
                    break;
                }
            }
        }
        return new String(chars, from, to - from + 1);
    }

    public static boolean isPalindrome(char[] chars, int from, int to) {
        for (int i = from, sum = to + from; i <= to; i++) {
            if (chars[i] != chars[sum - i]) {
                return false;
            }
        }
        return true;
    }

    static int lengthOfLongestSubstring(char[] s) {
        int len = s.length;
        int maxSubStringLen = 0;
        int[] bucket = new int[128];
        Arrays.fill(bucket, -1);
        for (int i = 0, begin = 0; i < len; i++) {
            int letter = s[i];
            int bucketVal = bucket[letter];
            if (bucketVal == -1) {
                bucket[letter] = i;
            } else {
                if (begin < bucketVal + 1) {
                    begin = bucketVal + 1;
                }
                bucket[letter] = i;
            }
            int subStringLen = i - begin + 1;
            if (subStringLen > maxSubStringLen) {
                maxSubStringLen = subStringLen;
            }
        }
        return maxSubStringLen;
    }

    public double findMedianSortedArrays0(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        if (m > n) { // to ensure m <= n
            int[] temp = nums1;
            nums1 = nums2;
            nums2 = temp;
            int tmp = m;
            m = n;
            n = tmp;
        }
        int iMin = 0, iMax = m, halfLen = (m + n + 1) / 2;
        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;
            int j = halfLen - i;
            if (i < iMax && nums2[j - 1] > nums1[i]) {
                iMin = i + 1; // i is too small
            } else if (i > iMin && nums1[i - 1] > nums2[j]) {
                iMax = i - 1; // i is too big
            } else { // i is perfect
                int maxLeft = 0;
                if (i == 0) {
                    maxLeft = nums2[j - 1];
                } else if (j == 0) {
                    maxLeft = nums1[i - 1];
                } else {
                    maxLeft = Math.max(nums1[i - 1], nums2[j - 1]);
                }
                if ((m + n) % 2 == 1) {
                    return maxLeft;
                }

                int minRight = 0;
                if (i == m) {
                    minRight = nums2[j];
                } else if (j == n) {
                    minRight = nums1[i];
                } else {
                    minRight = Math.min(nums2[j], nums1[i]);
                }

                return (maxLeft + minRight) / 2.0;
            }
        }
        return 0.0;
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;
        if ((len1 | len2) == 0) {
            throw new IllegalArgumentException("Both array lengths are zero.");
        }
        int mid;
        if (len1 == 0 || len2 == 0) {
            int[] nums = len1 == 0 ? nums2 : nums1;
            int len = nums.length;
            mid = len >>> 1;
            if ((len & 1) == 1) {
                return nums[mid];
            } else {
                return (double) (nums[mid] + nums[mid - 1]) / 2;
            }
        }
        int twoLens = len1 + len2;
        mid = twoLens >>> 1;
        int index1 = 0;
        int index2 = 0;
        int index = 0;
        while (true) {
            int num;
            if (nums1[index1] > nums2[index2]) {
                num = nums2[index2++];
            } else {
                num = nums1[index1++];
            }
            index++;
            if (index1 == len1 || index2 == len2) {
                if (index1 == len1) {
                    int remaining = mid - index + 1;
                    mid = index2 - 1 + remaining;
                    if (remaining >= 2) {
                        return (twoLens & 1) == 1 ? nums2[mid] : (double) (nums2[mid - 1] + nums2[mid]) / 2;
                    } else {
                        return (twoLens & 1) == 1 ? nums2[mid] : (double) (nums2[mid] + num) / 2;
                    }
                } else {
                    int remaining = mid - index + 1;
                    mid = index1 - 1 + remaining;
                    if (remaining >= 2) {
                        return (twoLens & 1) == 1 ? nums1[mid] : (double) (nums1[mid - 1] + nums1[mid]) / 2;
                    } else {
                        return (twoLens & 1) == 1 ? nums1[mid] : (double) (nums1[mid] + num) / 2;
                    }
                }
            }
            if (index == mid) {
                int nextNum = (nums1[index1] > nums2[index2]) ? nums2[index2] : nums1[index1];
                return (twoLens & 1) == 1 ? nextNum : (double) (num + nextNum) / 2;
            }
        }
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        java.util.ArrayList<Integer> list = new java.util.ArrayList<Integer>();
        while (l1 != null || l2 != null) {
            int sum = 0;
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            list.add(sum);
        }
        for (int i = 0, limit = list.size() - 1; i <= limit; i++) {
            int sum = list.get(i);
            if (sum >= 10) {
                if (i + 1 <= limit) {
                    list.set(i + 1, list.get(i + 1) + 1);
                } else {
                    list.add(1);
                }
                list.set(i, sum - 10);
            }
        }
        ListNode sumNode = null;
        int len = list.size();
        if (len > 0) {
            sumNode = new ListNode(list.get(0));
        }
        for (int i = 1; i < len; i++) {
            ListNode current = sumNode;
            ListNode next = sumNode;
            while (true) {
                if ((next = current.next) == null) {
                    current.next = new ListNode(list.get(i));
                    break;
                }
                current = next;
            }
        }
        return sumNode;
    }

    public static int[] twoSum(int[] nums, int target) {
        int len = nums.length;
        if (len < 2) {
            throw new IllegalArgumentException("The length of nums must be greater than or equal to two.");
        }
        HashMap<Integer, Integer> numMap = new HashMap<>();
        for (int i = 0; i < len; i++) {
            numMap.put(nums[i], i);
        }
        for (int i = 0; i < len; i++) {
            Integer next = null;
            if ((next = numMap.get(target - nums[i])) != null && i != next) {
                return new int[]{i, next};
            }
        }
        return null;
    }

    private static void traverse(ListNode head) {
        ListNode node = head;
        while (node != null) {
            System.out.print(node.val + " ");
            node = node.next;
        }
        System.out.println();
    }

    private static ListNode createNode(int[] nums) {
        ListNode head = new ListNode(0);
        ListNode last = head;
        for (int num : nums) {
            last = last.next = new ListNode(num);
        }
        return head.next;
    }

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }

    private static class MultiNode {
        public int val;
        public List<MultiNode> children;

        public MultiNode() {
        }

        public MultiNode(int _val, List<MultiNode> _children) {
            val = _val;
            children = _children;
        }

        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }
}

