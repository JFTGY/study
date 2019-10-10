package com.common.utils;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

@SuppressWarnings("unused")
public class ArrayUtils{
    private ArrayUtils(){throw new IllegalAccessError();}

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        System.out.println(date.getMonth().getValue());
        System.out.println(date.getDayOfYear());
        System.out.println(date.getDayOfWeek());
        for(int i = 1; i < 20; ++i){
            System.out.print(LocalDate.of(2019,1,i).get(WeekFields.ISO.weekOfYear()) + " ");
        }
        System.out.println();
        System.out.println(date.get(WeekFields.ISO.weekOfYear()));
    }

    public static void shuffle(int[] array){
        Random rand = new Random();
        int len = array.length;
        for (int i = len; i > 1; --i) {
            ArrayUtils.swap(array, i - 1, rand.nextInt(i));
        }
    }

    /**
     * 利用二分法在升序数组中找到比目标值大的最小数。
     *
     * https://en.cppreference.com/w/cpp/algorithm/upper_bound
     *
     * @param array 升序整型数组
     * @param target 目标值
     * @return 返回比目标值大的最小值索引，索引值可能超出数组范围。
     */
    public static int upperBound(int[] array, int target) {
        int low = 0, high = array.length;
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (array[mid] <= target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    public static int upperBound(double[] array, double target) {
        int low = 0, high = array.length;
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (array[mid] <= target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    /**
     * 利用二分法在升序数组中找到大于等于目标值的最小数。
     *
     * https://en.cppreference.com/w/cpp/algorithm/lower_bound
     *
     * @param array 升序整型数组
     * @param target 目标值
     * @return 返回大于等于目标值的最小值索引，索引值可能超出数组范围。
     */
    public static int lowerBound(int[] array, int target){
        int low = 0, high = array.length;
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (array[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    public static int lowerBound(double[] array, double target){
        int low = 0, high = array.length;
        while (low < high) {
            int mid = (low + high) >>> 1;
            if (array[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    /**
     * 获取数组中不重复元素所组成的数组
     *
     * 使用HashMap的唯一性实现对数组非重复数据的提取。
     *
     * @param array 待唯一化操作的数组
     * @return 数组中不重复元素所组成的数组
     */
    public static int[] unique(int[] array){
        int len = -1;
        if(array == null || (len = array.length) < 2){
            return len == -1 ? null : Arrays.copyOf(array, len);
        }
        HashMap<Integer, Object> map = new HashMap<Integer, Object>();
        for(int a : array){
            map.put(a, null);
        }
        int uniqueSize = map.size();
        int[] uniqueArray = new int[uniqueSize];
        for(Integer value : map.keySet()){
            uniqueArray[--uniqueSize] = value;
        }
        Arrays.sort(uniqueArray);
        return uniqueArray;
    }
    /**
     * 获取数组中不重复元素所组成的数组
     *
     * 首先对数组进行排序，然后比较数组前后元素，若不相等，则记作非重复值，
     * 为了避免扩充unique数组，使用boolean数组保存diff操作，非重复索引处的值记为true。
     * 最后遍历diff数组，逐一添加相应值到unique数组。
     *
     * 详情参照Matlab的unique函数
     *
     * @param array 待唯一化操作的数组
     * @return 数组中不重复元素所组成的数组
     */
    public static int[] unique0(int[] array){
        if(array == null){
            return null;
        }
        int len = array.length;
        if(len == 0){
            return new int[0];
        }
        array = Arrays.copyOf(array, len);
        Arrays.sort(array);
        boolean[] diffs = new boolean[len];
        diffs[0] = true;
        int count = 1;
        for(int i = 1; i < len; i++){
            if(array[i] != array[i - 1]){
                count++;
                diffs[i] = true;
            }
        }
        int[] uniqueArray = new int[count];
        for(int i = 0, index = 0; i < len; i++){
            if(diffs[i]){
                uniqueArray[index++] = array[i];
            }
        }
        return uniqueArray;
    }
    /**
     * 获取两数组的交集
     *
     * 将两数组加入HashMap中，利用HashMap的唯一性，获取两数组的交集。
     *
     * @param a 待操作数组
     * @param b 待操作数组
     * @return 两个数组的交集
     */
    public static int[] intersect(int[] a, int[] b){
        if(a == null || b == null){
            return null;
        }
        int aLen = 0, bLen = 0;
        if((aLen = a.length) == 0 || (bLen = b.length) == 0){
            return new int[0];
        }
        HashMap<Integer, Boolean> intersectMap = new HashMap<>();
        for(int aValue : a){
            intersectMap.put(aValue, true);
        }
        int intersectLen = (aLen > bLen) ? bLen : aLen;
        int[] intersectArray = new int[intersectLen];
        int iCount = 0;
        for(int bValue : b){
            Boolean isAdd = intersectMap.get(bValue);
            if(isAdd != null && isAdd){
                intersectArray[iCount++] = bValue;
                intersectMap.put(bValue, false);
            }
        }
        intersectArray = Arrays.copyOf(intersectArray, iCount);
        Arrays.sort(intersectArray);
        return intersectArray;
    }
    /**
     * 获取两数组的交集
     *
     * 首先对数组a和b进行unique处理，再将两数组拼接起来，对其进行排序。
     * 如果a和b存在并集，则在排序好的拼接数组中，会出现相邻两元素（最多只有两个）相等的情况。
     * 最后比较前后两元素值，相等，则为并集，作为优化，索引在再另外多加一。
     *
     * 详情参照Matlab的intersect函数
     *
     * @param a 待操作数组
     * @param b 待操作数组
     * @return 两个数组的交集
     */
    private static int[] intersect0(int[] a, int[] b){
        if(a == null || b == null){
            return null;
        }
        int[] uA = unique0(a);
        int[] uB = unique0(b);
        int aLen = uA.length;
        int bLen = uB.length;
        int sortLen = aLen + bLen;
        int[] sortUaUb = new int[sortLen]; // May throw OutOfMemoryError and NegativeArraySizeException
        System.arraycopy(uA, 0, sortUaUb, 0, aLen);
        System.arraycopy(uB, 0, sortUaUb, aLen, bLen);
        Arrays.sort(sortUaUb);
        int interLen = aLen > bLen ? bLen : aLen;
        int[] interAB = new int[interLen];
        int count = 0;
        for(int i = 1; i < sortLen; i++){
            if(sortUaUb[i] == sortUaUb[i - 1]){
                interAB[count++] = sortUaUb[i];
                i++;
            }
        }
        return Arrays.copyOf(interAB, count);
    }
    /**
     * 获取两数组并集
     *
     * 首先将数组a加入HashMap中，然后判断HashMap中是否包含数组b中数据，包含则添加到并集数组中，
     * 利用HashMap的boolean值，使并集数据只添加一次。
     *
     * @param a 待操作数组
     * @param b 待操作数组
     * @return 两个数组的并集
     */
    public static int[] union(int[] a, int[] b){
        if(a == null || a.length == 0){
            return unique(b);
        }
        if(b == null || b.length == 0){
            return unique(a);
        }
        HashMap<Integer, Object> unionMap = new HashMap<>();
        for(int aValue : a){
            unionMap.put(aValue, null);
        }
        for(int bValue : b){
            unionMap.put(bValue, null);
        }
        int unionLen = unionMap.size();
        int[] unionArray = new int[unionLen];
        for(int key : unionMap.keySet()){
            unionArray[--unionLen] = key;
        }
        Arrays.sort(unionArray);
        return unionArray;
    }
    /**
     * 获取两数组并集
     *
     * 拼接两个待操作数组，然后交由unique函数来获取并集。
     *
     * 拼接后的数组长度等于两待操作数组长度之和，若数组长度过大，可能抛出OutOfMemoryError，
     * 若数组长度发生溢出，可能抛出NegativeArraySizeException。
     *
     * 详情参照Matlab的union函数
     *
     * @param a 待操作数组
     * @param b 待操作数组
     * @return 两个数组的并集
     */
    private static int[] union0(int[] a, int[] b){
        if(a == null || b == null){
            return null;
        }
        int aLen = a.length;
        int bLen = b.length;
        int[] merge = new int[aLen + bLen]; // May throw OutOfMemoryError and NegativeArraySizeException
        System.arraycopy(a, 0, merge, 0, aLen);
        System.arraycopy(b, 0, merge, aLen, bLen);
        return unique0(merge); // Call unique to do all the work.
    }
    /**
     * 交换数组中两索引处的值
     * @param array 待交换数组对象
     * @param i 需要被交换的索引值
     * @param j 需要被交换的索引值
     */
    public static void swap(int[] array, int i, int j){
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    public static void swap(char[] array, int i, int j){
        char temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    /**
     * 反转指定范围的数组数据
     * @param array 待反转的数组对象
     * @param fromIndex 开始反转的索引值
     * @param toIndex 结束反转的索引值
     */
    public static void reverse(int[] array, int fromIndex, int toIndex){
        int len = 0;
        if(array == null || (len = array.length) < 2){
            return;
        }
        if((fromIndex | toIndex) < 0 || fromIndex > toIndex || toIndex >= len){
            throw new IndexOutOfBoundsException("begin = " + fromIndex + ", end = " + toIndex);
        }
        int range = fromIndex + toIndex;
        for(int i = fromIndex, limit = (range - 1) >>> 1; i <= limit; i++){
            swap(array, i, range - i);
        }
    }

    public static void reverse(char[] array, int fromIndex, int toIndex){
        int len = 0;
        if(array == null || (len = array.length) < 2){
            return;
        }
        if((fromIndex | toIndex) < 0 || fromIndex > toIndex || toIndex >= len){
            throw new IndexOutOfBoundsException("begin = " + fromIndex + ", end = " + toIndex);
        }
        int range = fromIndex + toIndex;
        for(int i = fromIndex, limit = (range - 1) >>> 1; i <= limit; i++){
            swap(array, i, range - i);
        }
    }

    /**
     * 反转整个数组
     * @param array 待反转数组对象
     */
    public static void reverse(int[] array){
        reverse(array, 0, array.length - 1);
    }
    /**
     * 反转整个字符串
     *
     * 如果字符的Unicode码值大于65535，则根据字符代理规则，由两个char组成。
     * 反转时需要将这两个char再次反转，才能符合Java字符规则，组成一个字符。
     *
     * @param str 待反转的字符串对象
     * @return 反转后的字符串对象
     */
    public static String reverse(String str){
        int len = 0;
        if(str == null || (len = str.length()) < 2){
            return str;
        }
        char[] values = str.toCharArray();
        boolean hasSurrogates = false;
        int n = len - 1;
        for(int i = 0, limit = (n - 1) >>> 1; i <= limit; i++){
            int j = n - i;
            char c1 = values[i];
            char c2 = values[j];
            if(Character.isSurrogate(c1) || Character.isSurrogate(c2)){
                hasSurrogates = true;
            }
            values[i] = c2;
            values[j] = c1;
        }
        if(hasSurrogates){
            for(int i = 0; i < len; i++){
                char c1 = values[i];
                if(Character.isLowSurrogate(c1)){
                    char c2 = values[i + 1];
                    if(Character.isHighSurrogate(c2)){
                        values[i++] = c2;
                        values[i] = c1;
                    }
                }
            }
        }
        return new String(values);
    }

    /**
     * 检查fromIndex和toIndex是否在指定范围内，如果不在，则抛出异常。
     */
    private static void rangeCheck(int arrayLength, int fromIndex, int toIndex){
        if(fromIndex < 0){
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if(fromIndex > toIndex){
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ").");
        }
        if(toIndex >= arrayLength){
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }
    }
}

