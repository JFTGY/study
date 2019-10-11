package com.study.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类主要是研究字符串匹配的三种算法，分别是暴力法、KMP法以及BM法。
 *
 * @author : JF
 * Date : 2019.10.10
 * Time : 22:58
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("WeakerAccess")
public class StringMatchAlgorithm {

    /**
     * 大海捞针，搜索子字符串首次出现在源字符串的位置。
     * <p>
     * 采用Boyer-Moore算法，基本搜索方式和暴力搜索一致，但却有两处不同。
     * 第一个是匹配方向不同，BM算法是从后向前匹配。
     * 第二个是跳跃步数不同，BM算法采用两套机制来保证跳跃步数的最大化。
     * 一套是坏字符机制，匹配到不一致的字符时，子字符串需要移动多大步数找到与之匹配的"坏字符"，具体参见{@see badCharacterMap}。
     * 一套是好后缀机制，匹配相同后缀时，子字符串需要移动多大步数找到与之匹配的后缀，具体参见{@see goodSuffixTable}。
     *
     * @param haystack 源字符串
     * @param needle   子字符串
     * @return 返回子字符串首次出现在源字符串的位置，如果返回-1，则表示源字符串中不包含字符串。
     */
    public int indexOfByBM(String haystack, String needle) {
        int haystackLen = haystack.length();
        int needleLen = needle.length();
        if (needleLen == 0) {
            return 0;
        }
        if (needleLen > haystackLen) {
            return -1;
        }
        int minusOneNeedleLen = needleLen - 1;
        char last = needle.charAt(minusOneNeedleLen);
        Map<Character, Integer> badCharacterMap = badCharacterMap(needle);
        int[] goodSuffixMoveSteps = goodSuffixTable(needle);
        for (int i = minusOneNeedleLen, j; i < haystackLen; ) {
            if (haystack.charAt(i) != last) {
                i += badCharacterMoveSteps(badCharacterMap, haystack, minusOneNeedleLen - i, minusOneNeedleLen);
                continue;
            }
            for (j = minusOneNeedleLen; --j >= 0 && haystack.charAt(i + j - minusOneNeedleLen) == needle.charAt(j); ) ;
            if (j == -1) {
                return i - minusOneNeedleLen;
            }
            i += Math.max(goodSuffixMoveSteps[j], badCharacterMoveSteps(badCharacterMap, haystack, minusOneNeedleLen - i, j));
        }
        return -1;
    }

    private int badCharacterMoveSteps(Map<Character, Integer> bcm, String haystack, int i, int j) {
        Integer pos = bcm.get(haystack.charAt(j - i));
        return pos == null ? j + 1 : Math.max(1, j - pos);
    }

    /**
     * 获取每个字符与其最后所出现索引的映射表。
     *
     * @param needle 字符串
     * @return 字符串中每个字符与其最后所出现索引的映射表
     */
    private Map<Character, Integer> badCharacterMap(String needle) {
        Map<Character, Integer> badCharMap = new HashMap<>();
        for (int i = 0, len = needle.length(); i < len; ++i) {
            badCharMap.put(needle.charAt(i), i);
        }
        return badCharMap;
    }

    /**
     * 获取每个字符移动到最长后缀的步数。
     * <p>
     * 因为只有出现匹配才调用该函数，所以从倒数第二个字符开始计算。
     *
     * @param needle 字符串
     * @return 字符串中每个字符移动到最长后缀的步数
     */
    private int[] goodSuffixTable(String needle) {
        int[] suffixes = suffixes(needle);
        int len = needle.length();
        int[] gst = new int[len];
        int firstCharMoveSteps = suffixes[0] + 1;
        outer:
        for (int i = len - 2; i >= 0; --i) {
            for (int j = i; j >= 0; --j) {
                if (suffixes[j] == i) {
                    gst[i] = i + 1 - j;
                    continue outer;
                }
            }
            gst[i] = firstCharMoveSteps < len ? firstCharMoveSteps : len;
        }
        return gst;
    }

    /**
     * 获取字符串后缀匹配表。
     * <p>
     * 对于字符串而言，后缀移动些许步数可能找到与之对应的字符。
     * 本方法主要是获取字符串中每个位置的字符匹配最长后缀的位置。
     * 如果该位置的值为len - 1，则说明该位置字符不可能匹配任何后缀。
     * <p>
     * 需要注意的是，该方法返回的匹配值，比真实的位置都要小1，后续操作需要格外小心。
     *
     * @param needle 字符串
     * @return 字符串后缀匹配表
     */
    private int[] suffixes(String needle) {
        int len = needle.length();
        int[] suffixes = new int[len];
        int minusOneLen = len - 1;
        suffixes[len - 1] = minusOneLen;
        for (int i = len - 2, j = minusOneLen; i >= 0; --i) {
            char ch = needle.charAt(i);
            while (j < minusOneLen && needle.charAt(j) != ch) {
                j = suffixes[j + 1];
            }
            if (ch == needle.charAt(j)) {
                --j;
            }
            suffixes[i] = j;
        }
        return suffixes;
    }

    /**
     * 大海捞针，搜索子字符串首次出现在源字符串的位置。
     * <p>
     * 采用Knuth-Morris-Pratt算法，基本搜索方式和暴力搜索一致。
     * 唯一的不同是当源字符串与子字符串出现相同前缀时，该方法跳跃的步数可能大于1。
     * 具体跳跃的步数与子字符串相关，具体可以参见{@see partialMatchTable}。
     *
     * @param haystack 源字符串
     * @param needle   子字符串
     * @return 返回子字符串首次出现在源字符串的位置，如果返回-1，则表示源字符串中不包含字符串。
     */
    public int indexOfByKMP(String haystack, String needle) {
        int haystackLen = haystack.length();
        int needleLen = needle.length();
        if (needleLen == 0) {
            return 0;
        }
        if (needleLen > haystackLen) {
            return -1;
        }
        char first = needle.charAt(0);
        int[] next = partialMatchTable(needle);
        for (int i = 0, j, limit = haystackLen - needleLen; i <= limit; ) {
            while (haystack.charAt(i) != first) {
                if (i++ == limit) {
                    return -1;
                }
            }
            for (j = 0; ++j < needleLen && haystack.charAt(i + j) == needle.charAt(j); ) ;
            if (j == needleLen) {
                return i;
            }
            i += j - next[j - 1];
        }
        return -1;
    }

    /**
     * 获取字符串前缀匹配表。
     * <p>
     * 对于字符串而言，前缀移动些许步数可能找到与之对应的字符。
     * 本方法主要是获取字符串中每个位置的字符匹配最长前缀的长度。
     * 如果长度为0，则说明该位置字符不可能匹配任何前缀。
     *
     * @param needle 字符串
     * @return 字符串前缀匹配表
     */
    private int[] partialMatchTable(String needle) {
        int len = needle.length();
        int[] pmt = new int[len];
        for (int i = 1, j = 0; i < len; ++i) {
            char ch = needle.charAt(i);
            while (j > 0 && needle.charAt(j) != ch) {
                j = pmt[j - 1];
            }
            if (ch == needle.charAt(j)) {
                ++j;
            }
            pmt[i] = j;
        }
        return pmt;
    }

    /**
     * 大海捞针，搜索子字符串首次出现在源字符串的位置。
     * <p>
     * 采用暴力搜索的方法，逐个字符进行比较，每次搜索的步数为1。
     *
     * @param haystack 源字符串
     * @param needle   子字符串
     * @return 返回子字符串首次出现在源字符串的位置，如果返回-1，则表示源字符串中不包含字符串。
     */
    public int indexOfByBF(String haystack, String needle) {
        int haystackLen = haystack.length();
        int needleLen = needle.length();
        if (needleLen == 0) {
            return 0;
        }
        if (needleLen > haystackLen) {
            return -1;
        }
        char first = needle.charAt(0);
        for (int i = 0, j, limit = haystackLen - needleLen; i <= limit; ++i) {
            while (haystack.charAt(i) != first) {
                if (i++ == limit) {
                    return -1;
                }
            }
            for (j = 0; ++j < needleLen && haystack.charAt(i + j) == needle.charAt(j); ) ;
            if (j == needleLen) {
                return i;
            }
        }
        return -1;
    }
}
