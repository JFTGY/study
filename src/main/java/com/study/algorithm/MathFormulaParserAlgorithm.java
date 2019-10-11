package com.study.algorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 这是一个数学公式解析Demo类，主要是为了解析纯数字的数学公式，并且计算出相应结果。
 * <p>
 * 数学公式中的数字，只能是正负整数、小数以及科学记数，运算符暂时只支持"+、-、*、/"，并且完全满足Java语法。
 * 针对于数学公式中除零问题，算法并不对其做任何处理。
 * <p>
 * 指令集解析采用的逆波兰法处理
 *
 * @author : JF
 * Date    : 2019.9.22
 * Time    : 13:10
 */
public class MathFormulaParserAlgorithm {
    private static final HashMap<String, Integer> priorityMap = new HashMap<>(8, 0.75F);

    static {
        priorityMap.put("(", Integer.MAX_VALUE);
        priorityMap.put(")", 0);
        priorityMap.put("+", 1);
        priorityMap.put("-", 1);
        priorityMap.put("*", 2);
        priorityMap.put("/", 2);
    }

    public double execute(String formula) {
        MathFormulaParserAlgorithm mfp = new MathFormulaParserAlgorithm();
        List<String> instructions = mfp.formulaToInstructions(formula);
        instructions = mfp.rpn(instructions);
        return mfp.executeTokens(instructions);
    }

    /**
     * 将原始指令集转换为逆波兰形式
     * <p>
     * 该函数采用的是双端队列ArrayDeque来实现逆波兰算法。
     * 一定要注意数字和操作符插入的方向，数组插入双端队列的后方，操作符插入双端队列的前方。
     * 最后使用ArrayList来转换ArrayDeque，其逆波兰指令集的顺序才是正确的。
     *
     * @param instructions 原始指令集
     * @return 逆波兰指令集
     */
    private List<String> rpn(List<String> instructions) {
        ArrayDeque<String> rpnDeque = new ArrayDeque<>();
        int opNumber = 0;
        for (String instruction : instructions) {
            // 数字指令直接添加。
            if (isNumber(instruction)) {
                rpnDeque.addLast(instruction);
                continue;
            }
            char op = instruction.charAt(0);
            // 左括号指令直接添加
            if (op == '(') {
                rpnDeque.addFirst("(");
                ++opNumber;
            } else if (op == ')') {
                // 当指令是右括号时，需要将左括号之前的运算符全部添加。
                while (opNumber > 0 && rpnDeque.peekFirst().charAt(0) != '(') {
                    rpnDeque.addLast(rpnDeque.removeFirst());
                    --opNumber;
                }
                // 弹出左括号指令
                rpnDeque.removeFirst();
                --opNumber;
            } else {
                // 当指令时运算符时，如果前一个指令是左括号指令，则直接添加。
                // 需要迭代比较前一个运算符指令与当前运算符指令的优先级。
                // 当前优先级高直接添加到指令栈中，否则添加到逆波兰栈中。
                if (opNumber > 0) {
                    String preOp;
                    while (opNumber > 0 && (preOp = rpnDeque.peekFirst()).charAt(0) != '('
                            && priorityMap.get(String.valueOf(op)) <= priorityMap.get(preOp)) {
                        rpnDeque.addLast(rpnDeque.removeFirst());
                        --opNumber;
                    }
                }
                rpnDeque.addFirst(String.valueOf(op));
                ++opNumber;
            }
        }
        // 将指令栈中的剩余指令添加到逆波兰栈中。
        while (opNumber-- > 0) {
            rpnDeque.addLast(rpnDeque.removeFirst());
        }
        return new ArrayList<>(rpnDeque);
    }

    /**
     * 执行逆波兰指令，计算出指令结果。
     *
     * @param instructions 逆波兰指令列表
     * @return 指令结果
     */
    private double executeTokens(List<String> instructions) {
        ArrayDeque<Double> numStack = new ArrayDeque<>();
        for (String instruction : instructions) {
            char first = instruction.charAt(0);
            if (instruction.length() > 1 || first >= '0') {
                numStack.push(Double.parseDouble(instruction));
                continue;
            }
            double b = numStack.pop(), a = numStack.pop();
            switch (first) {
                case '+':
                    numStack.push(a + b);
                    break;
                case '-':
                    numStack.push(a - b);
                    break;
                case '*':
                    numStack.push(a * b);
                    break;
                case '/':
                    numStack.push(a / b);
                    break;
                default:
                    throw new Error(instruction);
            }
        }
        return numStack.pop();
    }

    /**
     * 利用逆波兰算法将中序指令转换为后序指令。
     *
     * @param instructions 中序指令列表
     * @return 后序指令列表
     */
    private List<String> rpn0(List<String> instructions) {
        ArrayDeque<String> rpnDeque = new ArrayDeque<>();
        ArrayDeque<Character> opDeque = new ArrayDeque<>();
        for (String instruction : instructions) {
            // 数字指令直接添加。
            if (isNumber(instruction)) {
                rpnDeque.addLast(instruction);
                continue;
            }
            char op = instruction.charAt(0);
            // 左括号指令直接添加
            if (op == '(') {
                opDeque.push('(');
            } else if (op == ')') {
                // 当指令是右括号时，需要将左括号之前的运算符全部添加。
                while (!opDeque.isEmpty() && opDeque.peek() != '(') {
                    rpnDeque.addLast(String.valueOf(opDeque.pop()));
                }
                // 弹出左括号指令
                opDeque.pop();
            } else {
                // 当指令时运算符时，如果前一个指令是左括号指令，则直接添加。
                // 需要迭代比较前一个运算符指令与当前运算符指令的优先级。
                // 当前优先级高直接添加到指令栈中，否则添加到逆波兰栈中。
                if (!opDeque.isEmpty()) {
                    char preOp;
                    while (!opDeque.isEmpty() && (preOp = opDeque.peek()) != '('
                            && priorityMap.get(String.valueOf(op)) <= priorityMap.get(String.valueOf(preOp))) {
                        rpnDeque.addLast(String.valueOf(opDeque.pop()));
                    }
                }
                opDeque.push(op);
            }
        }
        // 将指令栈中的剩余指令添加到逆波兰栈中。
        while (!opDeque.isEmpty()) {
            rpnDeque.addLast(String.valueOf(opDeque.pop()));
        }
        return new ArrayList<>(rpnDeque);
    }

    /**
     * 根据解析规则将公式字符串解析为指令列表。
     *
     * @param formula 公式字符串
     * @return 指令列表
     */
    private List<String> formulaToInstructions(String formula) {
        int len;
        if (formula == null || (len = (formula = formula.trim()).length()) == 0) {
            throw new MathFormulaParserException("公式为空！");
        }
        int[] cutOffIndex = new int[1]; // 为了记录解析数字的截止索引值
        int leftParenthesisCount = 0, rightParenthesisCount = 0; // 验证小括号的左右括号是否对称
        boolean isNumberAhead = false;   // 记录前面一个instruction是否为数字，为了解决"-(2)"这样的格式，默认初始值为true。
        List<String> instructions = new ArrayList<>();
        for (int i = 0; i < len; ++i) {
            char ch = formula.charAt(i);
            switch (ch) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.': // 这几个都可能是数字的首位，注意两个数字不能直接相连。
                    if (isNumberAhead) {
                        throw new MathFormulaParserException("格式解析错误：数字之间无运算符。");
                    }
                    instructions.add(parserNumber(formula, i, cutOffIndex)); // 要么抛异常，要么一定能解析出一个数字。
                    i = cutOffIndex[0];
                    isNumberAhead = true;
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                    if (isOpChar(ch)) {
                        // "+"和"-"号都可能是数字的首字符。
                        // 如果解析为数字，只要满足格式，直接保存即可。
                        // 如果解析为非数字，则此时该字符将作为运算符处理。
                        String number = parserNumber(formula, i, cutOffIndex);
                        if (number != null) {
                            if (!isNumberAhead) {
                                instructions.add(number);
                                i = cutOffIndex[0];
                                isNumberAhead = true;
                                break;
                            }
                        }
                    }
                    // 作为运算符，必须保证前面必须有数字
                    if (!isNumberAhead) {
                        // 为了解决"(-3)"这样的格式，逆波兰可能会解析出错，默认转换为"(0-3)"的形式。
                        if (isOpChar(ch)) {
                            instructions.add("0");
                        } else {
                            throw new MathFormulaParserException("运算符解析错误：" + ch + "号前面没有数字。");
                        }
                    }
                    instructions.add(String.valueOf(ch));
                    isNumberAhead = false;
                    break;
                case '(':
                    // 左括号之前要么什么都没有，要么连接运算符。
                    if (isNumberAhead) {
                        throw new MathFormulaParserException("运算符解析错误：左括号和前面数字无运算符连接。");
                    }
                    ++leftParenthesisCount;
                    instructions.add(String.valueOf(ch));
                    isNumberAhead = false;
                    break;
                case ')':
                    // 右括号之前一定要有数字.
                    if (!isNumberAhead) {
                        throw new MathFormulaParserException("运算符解析错误：右括号前面无数字。");
                    }
                    ++rightParenthesisCount;
                    isNumberAhead = true;
                    instructions.add(String.valueOf(ch));
                    break;
                case ' ': // 空格全剔除
                    break;
                default:
                    throw new MathFormulaParserException("字符解析错误：" + ch + "是无效字符。");
            }
        }
        // 验证小括号的对称性。
        if (leftParenthesisCount != rightParenthesisCount) {
            throw new MathFormulaParserException("格式解析错误：小括号不对称，多了一个" + (leftParenthesisCount > rightParenthesisCount ? "左括号。" : "右括号。"));
        }
        return instructions;
    }

    /**
     * 从指定位置(0-9、.、+、-)开始，按照数字格式解析数字，并返回该数字字符串，同时记录数字在字符串的截止索引。
     * <p>
     * 解析思路可以参考: {@link MathFormulaParserAlgorithm#isNumber(String)}
     *
     * @param formula     被解析的字符串
     * @param begin       开始解析位置
     * @param cutOffIndex 保存解析截止索引
     * @return 解析后得到的数字
     */
    private String parserNumber(String formula, int begin, int[] cutOffIndex) {
        int len;
        if (begin < 0 || begin > (len = formula.length())) {
            throw new IllegalArgumentException("初始值错误：begin = " + begin);
        }
        // 必须保证数字解析时，首字符为[0-9.+-]。
        char first = formula.charAt(begin);
        if (!(first == '.' || isOpChar(first) || Character.isDigit(first))) {
            throw new IllegalArgumentException("初始字符错误：" + first);
        }
        boolean decimalPointSeen = false;   // 小数点是否已出现
        boolean eSeen = false;       // 指数e是否出现
        boolean digitsSeen = false;  // 数字是否出现
        boolean digitsAfterE = true; // 指数e后面的数字是否出现
        StringBuilder number = new StringBuilder();
        int index = begin;
        for (; index < len; ++index) {
            char ch = formula.charAt(index);
            if (Character.isDigit(ch)) { // 当字符为数字时，直接将其保存。
                digitsSeen = true;
                digitsAfterE = true;
                number.append(ch);
            } else if (ch == '.') { // 当字符为.时，必须保证小数点和指数e都没出现。
                number.append(ch);
                if (decimalPointSeen || eSeen) {
                    throw new MathFormulaParserException("数值类型错误：" + number.toString());
                }
                decimalPointSeen = true;
            } else if (isExpChar(ch)) {  // 当字符串为指数e时，必须保证e没有出现，并且e前面出现过数字。
                if (eSeen || !digitsSeen) {
                    throw new MathFormulaParserException("数值类型错误：" + number.toString());
                }
                number.append(ch);
                eSeen = true;
                digitsAfterE = false;
            } else if (ch == '+' || ch == '-') { // 正负号只能出现在数字之前和指数e之后。
                if (index != begin && !isExpChar(formula.charAt(index - 1))) {
                    break;
                }
                number.append(ch);
            } else if (Character.isWhitespace(ch)) { // 空格只能出现在正负号和数字之间，即数字和小数点之前。
                if (decimalPointSeen && formula.charAt(index - 1) == '.') {
                    break;
                }
                // 如果指数E出现，且E后面有整数，则将该空格视为分割空格，直接退出即可。
                // 否则E后面没直连的整数，此时直接抛出异常。
                if (eSeen) {
                    if (digitsAfterE) {
                        break;
                    }
                    throw new MathFormulaParserException("数值类型错误：" + number.append(ch).toString());
                }
                // 如何前面情况都不存在，则空格要么在符号和数字之间，或者在数字后面。
                // 如果在数字后面，就将该空格视为分割空格，直接退出即可。
                // 如果空格在符号和数字之间，则忽略，继续解析。
                if (digitsSeen) {
                    break;
                }
            } else {
                // 出现无效字符，则直接退出，并且索引必须退一步，以免"污染"后面的字符。
                --index;
                break;
            }
        }
        // 解析完成后，可能出现两种情况。
        // 一种是遇到错误，直接break，这一种number保存的都是合法数字，无需处理。
        // 另一种是字符串完全解析完了，number保存的可能是合法数字，也可能无效数字，需处理。
        // 因为Java中 "1 + - 2"这样的式子是合法的，当我们从+号开始解析，可能只保存了+就退出了。
        // 并且它也不是合法数字，对于这样的情况，我们应该将其作为运算符处理，返回null即可。
        if (!number.toString().matches("[+-]?((\\d+(\\.)?\\d*)|((\\.)?\\d+))((e-?)[+-]?\\d+)?")) { // 无效数字
            if (number.length() == 1 && isOpChar(number.charAt(0))) {
                return null;
            }
            throw new MathFormulaParserException("格式类型错误：" + number.toString());
        }
        cutOffIndex[0] = index;
        return number.toString();
    }

    /**
     * 判断一个字符串是否为数字。
     * <p>
     * 数字判断的范围十分宽泛，只要满足编译器要求的格式都视为正确。
     * 诸如：-   1、 -.1、+0.e2、.1e-2等
     * 假设我们将数字分为四部分：符号、左边数、指数e和指数e右边的数，完整四部分类似于数字 -12.34e56。
     * 1、如果存在"+"或者"-"号，我们允许符号与后面的数字有空格。
     * 2、对于左边的小数格式，我们允许小数的整数部分省略或者小数部分省略，比如 12.或者.12，它们默认分别为12.0和0.12。
     * 3、当存在指数字符e时，其左、右两边一定要有数值，且右边一定为整数。
     * 4、除了符号与数字之间可以存在空格，其余地方均不能出现空格。
     * <p>
     * 正则表达式：[+-]?\\s*((\\d+(\\.)?\\d*)|((\\.)?\\d+))((e-?)[+-]?\\d+)?
     *
     * @param number 数字字符串
     * @return 符合数字格式，返回true，否则返回false
     */
    private boolean isNumber(String number) {
        int len;
        if (number == null || (len = (number = number.trim()).length()) == 0) {
            return false;
        }
        boolean decimalPointSeen = false;   // 小数点是否已出现
        boolean eSeen = false;       // 指数e是否出现
        boolean digitsSeen = false;  // 数字是否出现
        boolean digitsAfterE = true; // 指数e后面的数字是否出现
        for (int i = 0; i < len; i++) {
            char ch = number.charAt(i);
            if (Character.isDigit(ch)) { // 当字符为数字时，将e前和e后的数字同时设为出现。
                digitsSeen = true;
                digitsAfterE = true;
            } else if (ch == '.') {  // 当字符为.时，必须保证小数点和指数e都没出现。
                if (decimalPointSeen || eSeen) {
                    return false;
                }
                decimalPointSeen = true;
            } else if (isExpChar(ch)) { // 当字符串为指数e时，必须保证e没有出现，并且e前面出现过数字。
                if (eSeen || !digitsSeen) {
                    return false;
                }
                eSeen = true;
                digitsAfterE = false;
            } else if (ch == '+' || ch == '-') { // 正负号只能出现在数字之前和指数e之后。
                if (i != 0 && !isExpChar(number.charAt(i - 1))) {
                    return false;
                }
            } else if (Character.isWhitespace(ch)) { // 空格只能出现在正负号和数字之间，即数字和小数点之前。
                if (digitsSeen || decimalPointSeen) {
                    return false;
                }
            } else { // 非法字符
                return false;
            }
        }
        return digitsSeen && digitsAfterE;
    }

    /**
     * 判断字符是否是指数字符
     *
     * @param c 待判字符
     * @return 如果是指数字符，返回true，否则返回false。
     */
    private boolean isExpChar(char c) {
        return c == 'e' || c == 'E';
    }

    /**
     * 判断字符是否是正负号
     *
     * @param c 待判字符
     * @return 如果是正负号，返回true，否则返回false。
     */
    private boolean isOpChar(char c) {
        return c == '+' || c == '-';
    }

    class MathFormulaParserException extends RuntimeException {
        MathFormulaParserException(String s) {
            super(s);
        }
    }
}
