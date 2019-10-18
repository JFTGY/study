package com.train.leetcode;

/**
 * 将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。
 * 比如输入字符串为 "0123456789" 行数为 4 时，排列如下：
 * 0      6
 * 1    5 7
 * 2  4   8
 * 3      9
 * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："0615724839"。
 *
 * 链接：https://www.leetcode.com/problems/zigzag-conversion/
 *
 * @author : JF
 * Date    : 2019.10.14
 * Time    : 23:29
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("WeakerAccess")
public class Solution6 {

    /**
     * 将字符串转换为Z字形排列，提取逐行数据，获取新的字符串。
     * <p>
     * 这个题目并无任何技巧，类似小学的找规律题。
     * <p>
     * 仔细观察上面例子，我们可以发现，对于竖列而言，其每行的距离都是固定的。
     * 抛开首和尾行中间没有数据，其余行中间都有一个数据，上面排列可以看出中间数据和左边(或右边)数据的距离呈线性递减(增)。
     * 经过计算，首位两行数据的距离为2 * (numRows - 1)。
     * 对于中间行，左边数据和中间数据的距离变化为2 * (numRows - 2) : 2 : 2，中间数据和右边数据的距离变化刚好与其相反。
     *
     * @param s       源字符串
     * @param numRows 列长
     * @return Z形变化后的字符串
     */
    public String convert(String s, int numRows) {
        if (numRows < 2) {
            return s;
        }
        int len = s.length();
        char[] result = new char[len];
        int index = 0, maxGap = 2 * (numRows - 1);
        for (int i = 0; i < numRows; ++i) {
            int gap = maxGap - 2 * i;
            for (int j = i; j < len; j += maxGap) {
                result[index++] = s.charAt(j);
                if (gap > 0 && gap < maxGap && j + gap < len) {
                    result[index++] = s.charAt(j + gap);
                }
            }
        }
        return new String(result);
    }
}
