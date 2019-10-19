package com.train.leetcode;

/**
 * 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
 * '.' 匹配任意单个字符
 * '*' 匹配零个或多个前面的那一个元素
 * 所谓匹配，是要涵盖 整个 字符串 s的，而不是部分字符串。
 * <p>
 * 说明:
 * s 可能为空，且只包含从 a-z 的小写字母。
 * p 可能为空，且只包含从 a-z 的小写字母，以及字符 . 和 *。
 * <p>
 * 链接：https://www.leetcode.com/problems/regular-expression-matching
 *
 * @author : JF
 * Date    : 2019.10.18
 * Time    : 16:34
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Solution10 {
    public static void main(String[] args) {

    }

    public boolean isMatch(String s, String p) {
        return isMatchHelper(s, 0, p, 0, new byte[(s.length() + 1) * (p.length() + 1)]);
    }

    /**
     * 前面优雅版本的优化版本，核心思想：动态规划。
     * <p>
     * 当看到索引变化的递归方法，我们都可以往动态规划的方向思考。
     * 通过前面的暴力递归版本，我们可以知道是它是两个索引变化的递归，因此可以使用一个二维数组来记录匹配信息。
     * 与一维数组相比，二维数组寻址效率较低，还是空间占用还较大，所以这里我利用一维数组替换二维数组来记录匹配信息。
     * 又因为我们仅仅需要用-1记录此情况匹配失败，0记录该情况还未记录，1记录此情况匹配成功。
     * 仅仅只需要三种值即可记录匹配信息，因此一维数组的类型为byte。
     *
     * @param s       字符串
     * @param sIndex  字符串索引值
     * @param p       正则式
     * @param pIndex  正则式索引值
     * @param matches 储存匹配信息的一维字节数组
     * @return 匹配结果
     */
    private boolean isMatchHelper(String s, int sIndex, String p, int pIndex, byte[] matches) {
        if (matches[sIndex * p.length() + pIndex] != 0) {
            return matches[sIndex * p.length() + pIndex] == 1;
        }
        if (pIndex == p.length()) {
            return sIndex == s.length();
        }
        boolean match;
        boolean firstMatch = sIndex < s.length() && (p.charAt(pIndex) == '.' || p.charAt(pIndex) == s.charAt(sIndex));
        if (pIndex + 1 < p.length() && p.charAt(pIndex + 1) == '*') {
            match = isMatchHelper(s, sIndex, p, pIndex + 2, matches) || (firstMatch && isMatchHelper(s, sIndex + 1, p, pIndex, matches));
        } else {
            match = firstMatch && isMatchHelper(s, sIndex + 1, p, pIndex + 1, matches);
        }
        matches[sIndex * p.length() + pIndex] = (byte) (match ? 1 : 2);
        return match;
    }

    /**
     * 前面暴力解法的优化版，合并了很多公共分类。代码逻辑变得异常清晰，但是执行效率却并无太大改观。
     *
     * @param s      字符串
     * @param sIndex 字符串索引值
     * @param p      正则式
     * @param pIndex 正则式索引值
     * @return 匹配结果
     */
    private boolean isMatchHelper2(String s, int sIndex, String p, int pIndex) {
        // 如果p搜索结束，则s一定也要搜索结束。
        if (pIndex == p.length()) {
            return sIndex == s.length();
        }
        // 首字符是否匹配
        boolean firstMatch = sIndex < s.length() && (p.charAt(pIndex) == '.' || p.charAt(pIndex) == s.charAt(sIndex));
        // 如果下个字符为"*"
        if (pIndex + 1 < p.length() && p.charAt(pIndex + 1) == '*') {
            // 可以执行两种情况，一种是直接匹配0次的情况；一种是首字符匹配成功，执行匹配一次的情况。
            // 因为无论首字符是否匹配，都需要执行第一种情况，所以这里默认将第一种情况放在前面进行判断。
            return isMatchHelper2(s, sIndex, p, pIndex + 2) || (firstMatch && isMatchHelper2(s, sIndex + 1, p, pIndex));
        } else {
            // 下个字符不为"*"，只能寄希望于首字符匹配，s和p都向后退一位。
            return firstMatch && isMatchHelper2(s, sIndex + 1, p, pIndex + 1);
        }
    }

    /**
     * 纯暴力解法，分多种讨论，具体细节参见代码中的注释。(也不知道当时我是咋想到这个傻办法的，病急乱投医的搞法！！！)
     *
     * @param s      字符串
     * @param sIndex 字符串索引值
     * @param p      正则式
     * @param pIndex 正则式索引值
     * @return 匹配结果
     */
    private boolean isMatchHelper1(String s, int sIndex, String p, int pIndex) {
        // 1、当s搜索结束，则p要么也搜索结束，即""，要么后面全部是*结尾，即"a*.*b*"，不能有"**"的结构。
        if (sIndex == s.length()) {
            while (pIndex + 1 < p.length() && p.charAt(pIndex + 1) == '*' && p.charAt(pIndex) != '*') {
                pIndex += 2;
            }
            return pIndex == p.length();
        }
        char ch;
        // 2、当p搜索结束，说明s搜索未结束；搜索到"*"，不满足"*"必须在其他字符之前的要求。这两种情况都将匹配失败。
        if (pIndex == p.length() || (ch = p.charAt(pIndex)) == '*') {
            return false;
        }
        boolean firstMatch = ch == '.' || ch == s.charAt(sIndex);
        // 3、下一个字符为"*"时
        if (pIndex + 1 < p.length() && p.charAt(pIndex + 1) == '*') {
            // 4、如果前面字符和s的字符匹配，则有两种选择：要么匹配该字符1次；要么匹配该字符0次。
            if (firstMatch) {
                if (isMatchHelper1(s, sIndex + 1, p, pIndex) || isMatchHelper1(s, sIndex, p, pIndex + 2)) {
                    return true;
                }
            } else {
                // 5、前面字符和s的字符不匹配，只能期待匹配该字符0次的结果了。
                if (isMatchHelper1(s, sIndex, p, pIndex + 2)) {
                    return true;
                }
            }
        }
        // 当下一个字符为"*"时，失败后的条件4会被下一次的条件2干掉，失败后的条件5将直接被firstMatch干掉。
        // 当下一个字符部位"*"，只能期待当前字符匹配，s和p都向后退一位。
        return firstMatch && isMatchHelper1(s, sIndex + 1, p, pIndex + 1);
    }
}
