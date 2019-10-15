package com.train.leetcode;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author : JF
 * Date    : 2019.10.15
 * Time    : 19:44
 * Email   ：tangqibao_620@163.com
 */
public class Solution6Test {

    private static final String[] TEST_CASE = {"PAYPALISHIRING", "PAYPALISHIRING", "PYAIHRNAPLSIIG", "PAHNAPLSIIGYIR", "PINALSIGYAHRPI", "PHASIYIRPLIGAN", "PRAIIYHNPSGAIL", "PNAIGYRPIAHLSI", "PAGYNPIARLIIHS", "PAYPGANLIIRSIH", "PAYPALGINSIHRI", "PAYPALISGHNIIR", "PAYPALISHIGRNI", "PAYPALISHIRIGN", "PAYPALISHIRING"};

    private Solution6 solution;

    public Solution6Test() {
        solution = new Solution6();
    }

    @Test
    public void convert() {
        String s = "PAYPALISHIRING";
        for(int i = s.length(); i >= 0; --i){
            Assert.assertEquals(solution.convert(s, i), TEST_CASE[i]);
        }
    }
}