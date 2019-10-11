package com.study.algorithm;

import org.junit.Assert;
import org.junit.Test;
import org.nfunk.jep.JEP;

public class MathFormulaParserAlgorithmTest {
    @Test
    public void test() {
        String formula = "- 1.e1 * ( - 1 + - (-1. * (-3 * 4)) * (1 + 2)) / (3 - .1 * (-(-(3 + 1))))";
        JEP jep = new JEP();
        jep.parseExpression(formula);
        Assert.assertEquals(new MathFormulaParserAlgorithm().execute(formula), jep.getValue(), 0.000001);
    }
}