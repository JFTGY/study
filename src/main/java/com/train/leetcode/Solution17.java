package com.train.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 * <p>
 * 链接：https://www.leetcode.com/problems/letter-combinations-of-a-phone-number/
 *
 * @author : JF
 * Date    : 2019.10.21
 * Time    : 19:06
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("unused")
public class Solution17 {
    public static void main(String[] args) {
        new Solution17().letterCombinations2("23").forEach(System.out::println);
    }

    private static final String[] letterMap = {"abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    /**
     * 比较典型的深度搜索题目
     *
     * @param digits 数字字符所组成的字符串。
     * @return 笛卡尔积
     */
    public List<String> letterCombinations2(String digits) {
        if (digits == null || (digits = (normalize(digits))).length() < 1) {
            return new ArrayList<>();
        }
        int size = 1;
        for (int i = digits.length() - 1; i >= 0; --i) {
            size += letterMap[digits.charAt(i) - '2'].length();
        }
        List<String> result = new ArrayList<>(size);
        dfs(new StringBuilder(new String(new char[digits.length()])), 0, result, digits, 0);
        return result;
    }

    private void dfs(StringBuilder value, int valueIndex, List<String> result,
                     String digits, int digitsIndex) {
        if (valueIndex == value.length()) {
            result.add(value.toString());
            return;
        }
        String str = letterMap[digits.charAt(digitsIndex) - '2'];
        for (int i = 0, len = str.length(); i < len; ++i) {
            value.setCharAt(valueIndex, str.charAt(i));
            dfs(value, ++valueIndex, result, digits, ++digitsIndex);
            --valueIndex;
            --digitsIndex;
        }
    }

    /**
     * 使用暴力解法实现笛卡尔积。
     *
     * @param digits 数字字符所组成的字符串。
     * @return 笛卡尔积
     */
    public List<String> letterCombinations1(String digits) {
        digits = normalize(digits);
        int len;
        if ((len = digits.length()) == 0) {
            return new ArrayList<>();
        }
        StringBuilder value = new StringBuilder(len);
        int[] counts = new int[len], limits = new int[len];
        int size = 1;
        // 初始化
        for (int i = 0; i < len; ++i) {
            String letters = letterMap[digits.charAt(i) - '2'];
            value.append(letters.charAt(0));
            size *= limits[i] = letters.length();
        }
        List<String> result = new ArrayList<>(size);
        // 因为笛卡尔积中最后一个操作最为频繁，因此提前将相关变量计算出来。
        String lastStr = letterMap[len - 1];
        int lastStrLen = lastStr.length();
        int minusOneOfLen = len - 1;
        while (true) {
            // 添加最后一个数字的所有结果
            for (int i = 0; i < lastStrLen; ++i) {
                value.setCharAt(minusOneOfLen, lastStr.charAt(i));
                result.add(value.toString());
            }
            // 判断数字填满情况
            int j = len - 1;
            while (--j >= 0 && counts[j] == limits[j] - 1) {
                counts[j] = 0;
            }
            // 如果都填满，则说明处理结束
            if (j == -1) {
                break;
            }
            // 将下一个遍历的数字索引加一，修改原先字符
            value.setCharAt(j, letterMap[digits.charAt(j) - '2'].charAt(++counts[j]));
            // 将后面的数字索引全部置0，开始下次的迭代
            for (int i = j + 1; i < len; ++i) {
                value.setCharAt(i, letterMap[digits.charAt(i) - '2'].charAt(0));
            }
        }
        return result;
    }

    /**
     * 剔除数字字符串中的0。
     *
     * @param digits 数字字符串
     * @return 剔除数字字符串中的0的字符串
     */
    private String normalize(String digits) {
        StringBuilder value = new StringBuilder(digits.length());
        for (int i = 0, len = digits.length(); i < len; ++i) {
            char ch = digits.charAt(i);
            switch (ch) {
                case '1':
                    break;
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    value.append(ch);
                    break;
                default:
                    throw new IllegalArgumentException("非法字符:" + ch);
            }
        }
        return value.toString();
    }
}
