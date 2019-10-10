package com.train.swordtojava;

import java.util.*;

@SuppressWarnings("unused")
public class SwordToJavaDemo {

    public static void main(String[] args) {
        System.out.println(lastRemaining1(3, 2));
        ListNode head = createNode(new int[]{1, 2, 3, 4, 5});
        printNode(head);
        head = reverse(head);
        printNode(head);
        for (int i = 1; i < 10; i++) {
            System.out.println(lastRemaining3(10, i) + " : " + lastRemaining2(10, i));
        }
    }

    public static ListNode reverse(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }

    public static int atoi(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        str = str.trim();
        int i = 0, len = str.length();
        char[] digits = str.toCharArray();
        int limit = -Integer.MAX_VALUE;
        int multiMin = 0, digit = 0;
        boolean negative = false;
        int result = 0;
        if (len > 0) {
            char first = digits[i];
            if (first < '0') {
                if (first == '-') {
                    negative = true;
                    limit = -Integer.MIN_VALUE;
                } else if (first != '+') {
                    throw new java.lang.NumberFormatException("For input string: \"" + str + "\"");
                }
                if (++i == len) {
                    throw new java.lang.NumberFormatException("For input string: \"" + str + "\"");
                }
            }
            multiMin = limit / 10;
            while (i < len) {
                digit = Character.digit(digits[i++], 10);
                if (digit < 0) {
                    throw new java.lang.NumberFormatException("For input string: \"" + str + "\"");
                }
                if (result < multiMin) {
                    throw new java.lang.NumberFormatException("For input string: \"" + str + "\"");
                }
                result *= 10;
                if (result < limit + digit) {
                    throw new java.lang.NumberFormatException("For input string: \"" + str + "\"");
                }
                result -= digit;
            }
        } else {
            throw new java.lang.NumberFormatException("For input string: \"" + str + "\"");
        }
        return negative ? result : -result;
    }

    // 位操作加法
    public static int add(int a, int b) {
        int sum = 0;
        do {
            sum = a ^ b;
            b = (a & b) << 1;
            a = sum;
        } while (b != 0);
        return sum;
    }

    //1+2+...+n，不用乘除符号，不用if等条件判断，不用for等循环
    public static int f(int n) {
        int t = 0;
        boolean b = (n > 0) && ((t = f(n - 1)) > 0);
        return n + t;
    }

    public static int lastRemaining4(int n, int m) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("m = " + m + ", n = " + n);
        }
        if (m == 1 || n == 1) {
            return n - 1;
        }
        int count = 0;
        int index = 0;
        while (list.size() > 1) {
            if (++count % m == 0) {
                list.remove(index--);
            }
            if (++index == list.size()) {
                index = 0;
            }
        }
        return list.get(0);
    }

    public static int lastRemaining3(int n, int m) {
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("m = " + m + ", n = " + n);
        }
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        int index = 0;
        while (list.size() > 1) {
            int limit = list.size() - 1;
            for (int i = 1; i < m; i++) {
                if (index == limit) {
                    index = 0;
                } else {
                    index++;
                }
            }
            list.remove(index);
            if (index == list.size()) {
                index = 0;
            }
        }
        return list.peekFirst();
    }

    public static int lastRemaining2(int n, int m) {
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("m = " + m + ", n = " + n);
        }
        int last = 0;
        for (int i = 2; i <= n; i++) {
            last = (last + m) % i;
        }
        return last;
    }

    // https://blog.csdn.net/qq_41822235/article/details/82382422
    public static int lastRemaining1(int n, int m) {
        if (n < 1 || m < 1)
            return -1;
        if (n == 1) {
            return 0;
        }
        return (lastRemaining1(n - 1, m) + m) % n;
    }

    public static int lastRemaining0(int n, int m) {
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("m = " + m + ", n = " + n);
        }
        ListNode head = new ListNode(0);
        ListNode node = head;
        for (int i = 1; i < n; ++i) {
            node = node.next = new ListNode(i);
        }
        node.next = head;
        node = head;
        while (node.next.value != node.value) {
            int count = m;
            while (--count > 0) {
                node = node.next;
            }
            if (node.next.value == node.value) {
                return node.value;
            }
            node.value = node.next.value;
            node.next = node.next.next;
        }
        return node.value;
    }

    public static String leftRotationString(String str, int n) {
        int len = 0;
        if (str == null || (len = str.length()) < 2 || n == 0 || len == n) {
            return str;
        }
        if (len < n) {
            throw new StringIndexOutOfBoundsException(n);
        }
        char[] chars = str.toCharArray();
        int begin = 0;
        int end = len - 1;
        reverse0(chars, begin, end);
        reverse0(chars, begin, end - n);
        reverse0(chars, len - n, end);
        return new String(chars);
    }

    private static void reverse0(char[] chars, int begin, int end) {
        while (begin < end) {
            swap(chars, begin++, end--);
        }
    }

    public static String reverseString0(String str) {
        int left = 0;
        int right = str.length() - 1;
        char[] values = str.toCharArray();
        while (left < right) {
            swap(values, left++, right--);
        }
        return new String(values);
    }

    public static String reverseString(String str) {
        if (str == null || str.length() < 2) {
            return str;
        }
        String[] values = str.split(" ");
        int len = values.length;
        int[] indexs = new int[len];
        for (int i = 0; i < len; i++) {
            indexs[i] = i;
        }
        int remaining = len - 1;
        for (int i = 0, limit = (remaining - 1) >> 1; i <= limit; ++i) {
            swap(indexs, i, remaining - i);
        }
        StringBuilder sb = new StringBuilder();
        for (int index : indexs) {
            sb.append(values[index] + " ");
        }
        return sb.toString();
    }

    public static void findContinuousSequence(int n) {
        if (n < 3) {
            return;
        }
        int left = 1;
        int right = 2;
        int middle = (1 + n) >>> 1;
        int sum = left + right;
        while (left < middle) {
            if (sum == n) {
                printSequence(left, right);
            }
            while (sum > n && left < middle) {
                sum -= left++;
                if (sum == n) {
                    printSequence(left, right);
                }
            }
            sum += ++right;
        }
    }

    public static void printSequence(int a, int b) {
        for (; a <= b; ++a) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public static boolean findNumbersWithSum(int[] array, int sum) {
        HashMap<Integer, Integer> numMap = new HashMap<Integer, Integer>();
        for (int value : array) {
            if (numMap.containsKey(sum - value)) {
                return true;
            }
            numMap.put(value, null);
        }
        return false;
    }

    public static int[] findNumsAppearOnce(int[] array) {
        if (array == null || array.length < 2) {
            return null;
        }
        int xor = 0;
        for (int value : array) {
            xor ^= value;
        }
        int value1 = 0;
        int value2 = 0;
        int lowestBits = xor & -xor;
        for (int value : array) {
            if ((lowestBits & value) == 0) {
                value1 ^= value;
            } else {
                value2 ^= value;
            }
        }
        return new int[]{value1, value2};
    }

    public static int numberOfKInSortedArray(int[] array, int k) {
        int len = 0;
        if (array == null || (len = array.length) == 0) {
            return -1;
        }
        int left = 0;
        int right = len - 1;
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (k > array[mid]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        int count = 0;
        if (array[left] == k) {
            count++;
            int index = left;
            while (--index >= 0 && array[index] == k) {
                count++;
            }
            while (++left < len && array[left] == k) {
                count++;
            }
        }
        return count;
    }

    public static ListNode findFirstCommonNode(ListNode head1, ListNode head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        Stack<ListNode> stack1 = new Stack<ListNode>();
        Stack<ListNode> stack2 = new Stack<ListNode>();
        ListNode node = head1;
        while (node != null) {
            stack1.push(node);
            node = node.next;
        }
        node = head2;
        while (node != null) {
            stack2.push(node);
            node = node.next;
        }
        while (!stack1.isEmpty() && !stack2.isEmpty()) {
            node = stack1.pop();
            if (node.value != stack2.pop().value) {
                break;
            }
        }
        return stack1.isEmpty() ? node : node.next;
    }

    public static void sort(int[] array) {
        int len = array.length;
        for (int gap = 1; gap < len; gap <<= 1) {
            int twoGaps = gap << 1;
            int i = 0;
            for (; i + twoGaps < len; i += twoGaps) {
                merge0(array, i, i + gap - 1, i + twoGaps - 1);
            }
            if (i < len - gap) {
                merge0(array, i, i + gap - 1, len - 1);
            }
        }
    }

    private static void merge0(int[] array, int begin, int mid, int end) {
        int len = end - begin + 1;
        int[] mergeArray = new int[len];
        int left = begin;
        int leftLimit = mid + 1;
        int right = leftLimit;
        int rightLimit = end + 1;
        int index = 0;
        while (left < leftLimit && right < rightLimit) {
            if (array[left] < array[right]) {
                mergeArray[index++] = array[left++];
            } else {
                mergeArray[index++] = array[right++];
            }
            if (left == leftLimit) {
                System.arraycopy(array, right, mergeArray, index, rightLimit - right);
            }
            if (right == rightLimit) {
                System.arraycopy(array, left, mergeArray, index, leftLimit - left);
            }
        }
        System.arraycopy(mergeArray, 0, array, begin, len);
    }

    public static int reverseNum1(int[] array) {
        int len = 0;
        if (array == null || (len = array.length) <= 1) {
            return 0;
        }
        int count = 0;
        for (int gap = 1; gap < len; gap <<= 1) {
            int twoGaps = gap << 1;
            int i = 0;
            for (; i + twoGaps < len; i += twoGaps) {
                count += merge(array, i, i + gap - 1, i + twoGaps - 1);
            }
            if (i < len - gap) {
                count += merge(array, i, i + gap - 1, len - 1);
            }
        }
        return count;
    }

    private static int merge(int[] array, int begin, int mid, int end) {
        int len = end - begin + 1;
        int[] mergeArray = new int[len];
        int left = begin;
        int leftLimit = mid + 1;
        int right = leftLimit;
        int rightLimit = end + 1;
        int count = 0;
        int index = 0;
        while (left < leftLimit && right < rightLimit) {
            if (array[left] <= array[right]) {
                mergeArray[index++] = array[left++];
            } else {
                mergeArray[index++] = array[right++];
                count += leftLimit - left;
            }
            if (left == leftLimit) {
                System.arraycopy(array, right, mergeArray, index, rightLimit - right);
            }
            if (right == rightLimit) {
                System.arraycopy(array, left, mergeArray, index, leftLimit - left);
            }
        }
        System.arraycopy(mergeArray, 0, array, begin, len);
        return count;
    }

    public static int reverseNum0(int[] array) {
        int len = 0;
        if (array == null || (len = array.length) <= 1) {
            return 0;
        }
        int count = 0;
        for (int i = 1; i < len; i++) {
            int index = i;
            int target = array[i];
            while (--index >= 0) {
                if (array[index] > target) {
                    count++;
                }
            }
        }
        return count;
    }

    public static char firstNotRepeatingChar(String str) {
        if (str == null || str.length() == 0) {
            return '\0';
        }
        HashMap<Character, Boolean> charMap = new HashMap<Character, Boolean>();
        for (char c : str.toCharArray()) {
            Boolean count = charMap.get(c);
            if (count == null || count) {
                charMap.put(c, count == null ? true : false);
            }
        }
        for (Map.Entry<Character, Boolean> entry : charMap.entrySet()) {
            if (entry.getValue()) {
                return entry.getKey();
            }
        }
        return '\0';
    }

    public static int nthUglyNumber(int n) {
        int[] ugly = new int[n];
        ugly[0] = 1;
        int index2 = 0, index3 = 0, index5 = 0;
        int factor2 = 2, factor3 = 3, factor5 = 5;
        for (int i = 1; i < n; i++) {
            int min = Math.min(Math.min(factor2, factor3), factor5);
            ugly[i] = min;
            if (factor2 == min) {
                factor2 = 2 * ugly[++index2];
            }
            if (factor3 == min) {
                factor3 = 3 * ugly[++index3];
            }
            if (factor5 == min) {
                factor5 = 5 * ugly[++index5];
            }
        }
        return ugly[n - 1];
    }

    public static int numberOfOneBetweenOneToN1(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("The value of N must be greater than or equal to one.");
        }
        int m = n;
        int sum = 0;
        int e = 1;
        while (n > 0) {
            int r = n % 10;
            n /= 10;
            if (r == 0) {
                sum += n * e;
            } else if (r > 1) {
                sum += (n + 1) * e;
            } else {
                sum += m - n * 9 * e - e + 1;
            }
            e *= 10;
        }
        return sum;
    }

    public static int numberOfOneBetweenOneToN0(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("The value of N must be greater than or equal to one.");
        }
        int count = 0;
        for (int i = 1; i <= n; i++) {
            int value = i;
            while (value >= 10) {
                count += (value % 10) == 1 ? 1 : 0;
                value = (value / 10);
            }
            count += value == 1 ? 1 : 0;
        }
        return count;
    }

    public static int findGreatestSumOfSubArray0(int[] array) {
        int len = 0;
        if (array == null || (len = array.length) == 0) {
            throw new IllegalArgumentException("The array don't meet the requirements.");
        }
        if (len == 1) {
            return array[0];
        }
        int sum = 0;
        int maxValue = Integer.MIN_VALUE;
        for (int value : array) {
            if (sum > 0) {
                sum += value;
            } else {
                sum = value;
            }
            if (sum > maxValue) {
                maxValue = sum;
            }
        }
        return maxValue;
    }

    public static int[] getLeastNumber1(int[] array, int k) {
        int len = array.length;
        if (array == null || (len = array.length) == 0 || len < k || k < 0) {
            throw new IllegalArgumentException();
        }
        PriorityQueue<Integer> leastQueue = new PriorityQueue<Integer>(k, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        for (int i = 0; i < k; i++) {
            leastQueue.add(array[i]);
        }
        for (int i = k; i < len; i++) {
            int maxValue = leastQueue.peek();
            if (array[i] < maxValue) {
                leastQueue.poll();
                leastQueue.add(array[i]);
            }
        }
        int[] leastNumbers = new int[k];
        int index = 0;
        for (Object obj : leastQueue.toArray()) {
            leastNumbers[index++] = (int) obj;
        }
        return leastNumbers;
    }

    public static int[] getLeastNumbers0(int[] array, int k) {
        int len = array.length;
        if (array == null || (len = array.length) == 0 || len < k || k < 0) {
            throw new IllegalArgumentException();
        }
        int[] leastNumbers = new int[k];
        int index = -1;
        int limit = k - 1;
        int end = len - 1;
        while (index != limit) {
            if (index < limit) {
                index = partition(array, index + 1, end);
            } else {
                index = partition(array, 0, index - 1);
            }
        }
        for (int i = limit; i >= 0; --i) {
            leastNumbers[i] = array[i];
        }
        return leastNumbers;
    }

    public static int moreThanHalfNum1(int[] array) {
        int len = 0;
        if (array == null || (len = array.length) == 0) {
            throw new IllegalArgumentException();
        }
        if (len == 1) {
            return array[0];
        }
        int index = -1;
        int mid = len >> 1;
        int end = len - 1;
        while (index != mid) {
            if (index < mid) {
                index = partition(array, index + 1, end);
            } else {
                index = partition(array, 0, index - 1);
            }
        }
        if ((mid - 1 < 0 || array[mid] != array[mid - 1]) && (mid + 1 >= len || array[mid] != array[mid + 1])) {
            throw new IllegalArgumentException("A number in an array does not appear "
                    + "more than half the length of the array.");
        }
        return array[mid];
    }

    private static int partition(int[] array, int begin, int end) {
        int rand = begin + new java.util.Random().nextInt(end - begin + 1);
        swap(array, begin, rand);
        int left = begin;
        int pivot = array[begin];
        int right = end;
        int index = begin + 1;
        while (index <= right) {
            if (array[index] > pivot) {
                swap(array, index, right--);
            } else if (array[index] == pivot) {
                index++;
            } else {
                swap(array, left++, index++);
            }
        }
        return left;
    }

    public static int moreThanHalfNum0(int[] array) {
        int len = 0;
        if (array == null || (len = array.length) == 0) {
            throw new IllegalArgumentException();
        }
        int number = 0;
        int time = 0;
        for (int i = 0; i < len; ++i) {
            if (time > len - i) {
                break;
            }
            if (time > 0) {
                if (number == array[i]) {
                    time++;
                } else {
                    time--;
                }
            } else {
                number = array[i];
                time = 1;
            }
        }
        if (time == 0) {
            throw new IllegalArgumentException("A number in an array does not appear "
                    + "more than half the length of the array.");
        }
        return number;
    }

    /**
     * 在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。
     * 请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     *
     * @param array 待查找的数组
     * @param target 要查找的数
     * @return 查找结果，true找到，false没有找到
     */
    public static boolean find(int[][] array, int target) {
        int columns = 0;
        if (array == null || (columns = array.length) == 0) {
            return false;
        }
        int rows = array[0].length;
        int column = columns - 1, row = 0;
        while (column >= 0 && row < rows) {
            int value = array[column][row];
            if (value > target) {
                --column;
            } else if (value < target) {
                ++row;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 请实现一个函数，把字符串中的每个空格替换成其他字符串，例如"We are happy."，则输出"We%20are%20happy."。
     *
     * @param str     需要转换的字符串
     * @param replace 空白替换的字符串
     * @return 替换后的字符串
     */
    public static String replaceBlank(String str, String replace) {
        if (str == null || str.length() == 0 || replace == null) {
            return str;
        }
        int blankCount = 0;
        char[] replaceChars = replace.toCharArray();
        int replaceCharsLen = replaceChars.length;
        char[] originalChars = str.toCharArray();
        int charsLen = originalChars.length;
        for (int i = 0; i < charsLen; i++) {
            if (originalChars[i] == ' ') {
                blankCount++;
            }
        }
        int newCharsLen = charsLen + blankCount * (replaceCharsLen - 1);
        char[] newChars = new char[newCharsLen];
        int index = 0;
        for (int i = 0; i < charsLen; i++) {
            if (originalChars[i] == ' ') {
                System.arraycopy(replaceChars, 0, newChars, index, replaceCharsLen);
                index += replaceCharsLen;
            } else {
                newChars[index++] = originalChars[i];
            }
        }
        return new String(newChars);
    }

    public static void inversePrintNode(ListNode root) {
        if (root == null) {
            return;
        }
        ListNode node = root;
        Stack<ListNode> nodeStack = new Stack<>();
        while (node != null) {
            nodeStack.add(node);
            node = node.next;
        }
        while (!nodeStack.isEmpty()) {
            System.out.print(nodeStack.pop().value + " ");
        }
        System.out.println();
    }

    static class ListNode {
        int value;
        ListNode next;

        public ListNode(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(this.value);
        }
    }

    private static void printNode(ListNode head) {
        if (head == null) {
            return;
        }
        ListNode node = head;
        while (node != null) {
            System.out.print(node.value + " ");
            node = node.next;
        }
        System.out.println();
    }

    private static ListNode createNode(int[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        ListNode zeroNode = new ListNode(0);
        ListNode node = zeroNode;
        for (int value : array) {
            node = node.next = new ListNode(value);
        }
        return zeroNode.next;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static void swap(char[] array, int i, int j) {
        char temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static boolean isPopOrder(int[] pushArray, int[] popArray) {
        if (pushArray == null || popArray == null) {
            return pushArray == popArray ? true : false;
        }
        int len = 0;
        if ((len = pushArray.length) != popArray.length) {
            return false;
        }
        Stack<Integer> stack = new Stack<Integer>();
        int pushIndex = 0;
        outer:
        for (int i = 0; i < len; i++) {
            int popValue = popArray[i];
            if (stack.isEmpty() || popValue != stack.lastElement()) {
                while (pushIndex < len) {
                    int pushValue = pushArray[pushIndex++];
                    if (pushValue == popValue) {
                        continue outer;
                    } else {
                        stack.push(pushValue);
                    }
                }
            } else {
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    public static void printMatrixInCicle(int[][] matrix) {
        int rowLen = 0;
        if (matrix == null || (rowLen = matrix.length) == 0) {
            return;
        }
        if (rowLen == 1) {
            System.out.println(Arrays.toString(matrix[0]));
            return;
        }
        int colLen = matrix[0].length;
        int rowLimit = (rowLen & 1) == 1 ? rowLen >> 1 : (rowLen >> 1) - 1;
        int colLimit = (colLen & 1) == 1 ? colLen >> 1 : (colLen >> 1) - 1;
        int row = 0, maxRow = rowLen - 1;
        int col = 0, maxCol = colLen - 1;
        while (row <= rowLimit && col <= colLimit) {
            for (int j = col; j <= maxCol; ++j) { // 上
                System.out.print(matrix[row][j] + " ");
            }
            for (int i = row + 1; i <= maxRow; ++i) { // 右下
                System.out.print(matrix[i][maxCol] + " ");
            }
            for (int j = maxCol - 1; j >= col; --j) { // 下
                System.out.print(matrix[maxRow][j] + " ");
            }
            for (int i = maxRow - 1; i >= row + 1; --i) { // 左上
                System.out.print(matrix[i][col] + " ");
            }
            ++row;
            ++col;
            --maxRow;
            --maxCol;
        }
        System.out.println();
    }

    public static ListNode mergeTwoSortedNode(ListNode head1, ListNode head2) {
        if (head1 == null) {
            return head2;
        }
        if (head2 == null) {
            return head1;
        }
        ListNode mergeHead = new ListNode(0);
        ListNode node = mergeHead;
        while (true) {
            if (head1.value > head2.value) {
                node.next = head2;
                head2 = head2.next;
            } else {
                node.next = head1;
                head1 = head1.next;
            }
            node = node.next;
            if (head1 == null) {
                node.next = head2;
                break;
            }
            if (head2 == null) {
                node.next = head1;
                break;
            }
        }
        return mergeHead.next;
    }

    public static ListNode reverseNode2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode reverseHead = reverseNode2(head.next);
        head.next.next = head;
        head.next = null;
        return reverseHead;
    }

    public static ListNode reverseNode1(ListNode head) {
        Stack<ListNode> stack = new Stack<ListNode>();
        ListNode node = head;
        while (node != null) {
            stack.push(node);
            node = node.next;
        }
        if (stack.isEmpty()) {
            return null;
        }
        ListNode reverseHead = stack.pop();
        node = reverseHead;
        while (!stack.isEmpty()) {
            node.next = stack.pop();
            node = node.next;
        }
        node.next = null;
        return reverseHead;
    }

    // 反转链表
    public static ListNode reverseNode0(ListNode head) {
        ListNode pre = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }
        return pre;
    }

    // 反转链表
    public static ListNode reverseNode(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode cur = head;
        ListNode next = null;
        ListNode previous = new ListNode(head.value);
        ListNode latter = null;
        while (cur != null) {
            next = cur.next;
            if (next != null) {
                latter = new ListNode(next.value);
                latter.next = previous;
                previous = latter;
            }
            cur = next;
        }
        return previous;
    }

    // 删除倒数第K个结点
    public static ListNode removeKthNodeFromTail(ListNode head, int k) {
        if (head == null || k <= 0) {
            return head;
        }
        ListNode zeroHead = new ListNode(0);
        zeroHead.next = head;
        ListNode aheadNode = zeroHead;
        ListNode behindNode = zeroHead;
        for (int i = 0; i < k; ++i) {
            aheadNode = aheadNode.next;
        }
        while ((aheadNode = aheadNode.next) != null) {
            behindNode = behindNode.next;
        }
        behindNode.next = behindNode.next.next;
        return zeroHead.next;
    }

    // 发现倒数第k个结点
    public static ListNode findKthNodeFromTail(ListNode head, int k) {
        if (head == null || k <= 0) {
            return null;
        }
        ListNode zeroHead = new ListNode(0);
        zeroHead.next = head;
        ListNode first = zeroHead;
        ListNode next = zeroHead;
        for (int i = 0; i < k; i++) {
            first = first.next;
        }
        while (first != null) {
            first = first.next;
            next = next.next;
        }
        if (next == null || next == zeroHead) {
            throw new IndexOutOfBoundsException("K exceeds the limit.");
        }
        head = zeroHead.next;
        return next;
    }

    // 重排列数组，使负数放在左，零在中，正数在右
    public static void reorderNegativePositive(int[] array) {
        int len = array.length;
        if (len < 2) {
            return;
        }
        int target = 0;
        int right = len - 1;
        for (int i = 0; i < right; ) {
            int value = array[i];
            if (value <= target) {
                i++;
            } else {
                swap(array, i, right--);
            }
        }
    }

    // 重排列数组，使奇数在左，偶数在右
    public static void reorderOddEven(int[] array) {
        int len = array.length;
        if (len < 2) {
            return;
        }
        int left = 0;
        int right = len - 1;
        while (left < right) {
            while (left < right && (array[left] & 1) == 1) {
                left++;
            }
            while (left < right && (array[right] & 1) == 0) {
                right--;
            }
            if (left < right) {
                int temp = array[left];
                array[left++] = array[right];
                array[right--] = temp;
            }
        }
    }

    // 找到位数的最大值
    public static int maxBitValue(int n) {
        if (n <= 0 || n >= 11) {
            throw new IllegalArgumentException("N must be greate than zero and less than eleven.");
        }
        if (n == 10) {
            return Integer.MAX_VALUE;
        }
        return (int) Math.pow(10, n) - 1;
    }

    // 计算数字幂乘
    public static double power(double base, int exp) {
        long longBits = Double.doubleToLongBits(base);
        if ((longBits | exp) == 0) {
            throw new IllegalArgumentException("The zero power of 0 is meaningless.");
        }
        if (longBits == 0L) {
            return 0.0;
        }
        if (exp == 0) {
            return 1.0;
        }
        return exp > 0 ? power0(base, exp) : 1 / power0(base, -exp);
    }

    private static double power0(double base, int exp) {
        if (exp == 1) {
            return base;
        }
        double powerVal = power0(base, exp >> 1);
        return (exp & 1) == 1 ? powerVal * powerVal * base : powerVal * powerVal;
    }

    // 找到旋转数组的最小值
    public static int findMinWithRotatedArray(int[] array) {
        int len = array.length;
        if (len == 0) {
            throw new IllegalArgumentException("The length of array is zero.");
        }
        int left = 0;
        int right = len - 1;
        while (left < right) {
            int mid = (left + right) >>> 1;
            int midVal = array[mid];
            int leftVal = array[left];
            int rightVal = array[right];
            if (midVal < rightVal) {
                right = mid;
            } else if (midVal == rightVal) {
                right--;
            } else if (midVal > leftVal) {
                left = mid;
            } else if (midVal == leftVal) {
                left++;
            } else {
                /* midVal > rightVal && midVal < leftVal */
                throw new IllegalArgumentException("The array isn't a rotated array.");
            }
        }
        return array[left];
    }

    static class MinStack extends Stack<Integer> {
        private static final long serialVersionUID = 1L;
        private Stack<Integer> minElements;
        private int min = Integer.MAX_VALUE;

        public MinStack() {
            minElements = new Stack<Integer>();
        }

        @Override
        public Integer push(Integer item) {
            if (item < min) {
                min = item;
            }
            minElements.push(min);
            return super.push(item);
        }

        @Override
        public synchronized Integer pop() {
            minElements.pop();
            return super.pop();
        }

        public Integer getMin() {
            return minElements.isEmpty() ? null : minElements.lastElement();
        }
    }


}

