package com.study.algorithm;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author : JF
 * Date    : 2019.10.10
 * Time    : 20:44
 * Email   ：tangqibao_620@163.com
 */
public class StringMatchAlgorithmTest {

    private StringMatchAlgorithm stringMatch = new StringMatchAlgorithm();

    public StringMatchAlgorithmTest() {
    }

    @Test
    public void indexOfByBM() {
        Assert.assertEquals(stringMatch.indexOfByBM("hello", "ll"), "hello".indexOf("ll"));
        Assert.assertEquals(stringMatch.indexOfByBM("mississippi", "issip"), "mississippi".indexOf("issip"));
    }

    @Test
    public void indexOfByKMP() {
        Assert.assertEquals(stringMatch.indexOfByKMP("hello", "ll"), "hello".indexOf("ll"));
        Assert.assertEquals(stringMatch.indexOfByKMP("mississippi", "issip"), "mississippi".indexOf("issip"));
    }

    @Test
    public void indexOfByBF() {
        Assert.assertEquals(stringMatch.indexOfByBF("hello", "ll"), "hello".indexOf("ll"));
        Assert.assertEquals(stringMatch.indexOfByBF("mississippi", "issip"), "mississippi".indexOf("issip"));
    }
}