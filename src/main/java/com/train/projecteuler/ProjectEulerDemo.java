package com.train.projecteuler;

import com.common.utils.ArrayUtils;
import com.common.utils.MathUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("unused")
public class ProjectEulerDemo {
    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        lessSpiralPrimes();
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
        System.out.println(test());
    }

    // 59、XOR decryption
    public static int test() {
        File file = new File("src/p059_cipher.txt");
        List<Integer> list = new ArrayList<Integer>();
        int index = 0;
        StringBuilder sb = new StringBuilder();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            int c = 0;
            while ((c = bis.read()) != -1) {
                if (c >= '0' && c <= '9') {
                    if (index >= sb.length()) {
                        sb.append((char) c);
                    } else {
                        sb.setCharAt(index, (char) c);
                    }
                    ++index;
                } else {
                    list.add(Integer.parseInt(sb.substring(0, index)));
                    index = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[] data = list.stream().mapToInt(Integer::valueOf).toArray();
        int len = data.length;
        list = null;
        int maxENum = 0;
        int sum = 0;
        int first = 0, second = 0, third = 0;
        for (int a = 97; a <= 122; ++a) {
            for (int b = 97; b <= 122; ++b) {
                for (int c = 97; c <= 122; ++c) {
                    int eNum = 0;
                    for (int i = 0; i + 3 < len; ++i) {
                        if ((a ^ data[i]) == 116 &&
                                (b ^ data[i + 1]) == 104 &&
                                (c ^ data[i + 2]) == 101) {
                            ++eNum;
                        }
                    }
                    if (eNum >= maxENum) {
                        maxENum = eNum;
                        first = a;
                        second = b;
                        third = c;
                    }
                }
            }
        }
        for (int i = 0; i < len; ++i) {
            switch (i % 3) {
                case 0:
                    sum += first ^ data[i];
                    break;
                case 1:
                    sum += second ^ data[i];
                    break;
                case 2:
                    sum += third ^ data[i];
                    break;
            }
        }
        return sum;
    }

    // 58、Spiral primes
    public static int lessSpiralPrimes() {
        double total = 1.0d;
        int primeCount = 0;
        for (int n = 3; ; n += 2) {
            total += 4;
            int n1 = n - 1;
            int leftUp = n1 * n1 + 1;
            if (MathUtils.isPrime(leftUp)) {
                ++primeCount;
            }
            int rightUp = leftUp - n1;
            if (MathUtils.isPrime(rightUp)) {
                ++primeCount;
            }
            int leftDown = n * n - n1;
            if (MathUtils.isPrime(leftDown)) {
                ++primeCount;
            }
            if (primeCount / total < 0.1d) {
                return n;
            }
        }
    }

    // 57、Square root convergents
    public static int countSquareRootConvergents() {
        BigInteger numerator = new BigInteger("1");
        BigInteger denominator = new BigInteger("1");
        int count = 0;
        for (int i = 1; i < 1001; ++i) {
            String a = denominator.toString();
            denominator = denominator.add(numerator);
            numerator = new BigInteger(a).multiply(new BigInteger("2")).add(numerator);
            if (numerator.toString().length() > denominator.toString().length()) {
                ++count;
            }
        }
        return count;
    }

    // 56、Powerful digit sum
    public static int sumPowerfulDigit() {
        int maxSum = 0;
        for (int base = 1; base < 100; ++base) {
            for (int exp = 1; exp < 100; ++exp) {
                char[] digits = new BigInteger(Integer.toString(base)).pow(exp).toString().toCharArray();
                int sum = 0;
                for (char c : digits) {
                    sum += c;
                }
                sum -= digits.length * 48;
                if (sum > maxSum) {
                    maxSum = sum;
                }
            }
        }
        return maxSum;
    }

    // 55、Lychrel numbers
    public static int countLychrelNumber() {
        int lychrelCount = 0;
        for (int i = 1; i < 10000; ++i) {
            BigInteger bi = BigInteger.valueOf(i);
            int iterCount = 0;
            while (true) {
                String value = bi.toString();
                bi = bi.add(new BigInteger(ArrayUtils.reverse(value)));
                if (!isPalindrome(bi.toString())) {
                    if (++iterCount > 50) {
                        ++lychrelCount;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return lychrelCount;
    }

    // 53、Combinatoric selections
    public static int countCombinatoricSelections() {
        int n = 101;
        int cacheLen = 101;
        int[] befores = new int[cacheLen];
        befores[0] = 1;
        int[] afters = new int[cacheLen];
        afters[0] = 1;
        int count = 0;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= i; j++) {
                int value = befores[j - 1] + befores[j];
                if ((befores[j - 1] | befores[j]) == -1 || value > 1000000) {
                    afters[j] = -1;
                    ++count;
                } else {
                    afters[j] = value;
                }
            }
            int[] temp = befores;
            befores = afters;
            afters = temp;
        }
        return count;
    }

    // 52、Permuted multiples
    public static int permutedMultiples() {
        // 走马灯数142857
        outer:
        for (int i = 100; ; ++i) {
            char[] digits = Integer.toString(i).toCharArray();
            if (digits[0] != '1' || digits[1] > '6') {
                continue;
            }
            Arrays.sort(digits);
            for (int multiply = 2; multiply < 6; ++multiply) {
                char[] ds = Integer.toString(i * multiply).toCharArray();
                Arrays.sort(ds);
                if (!Arrays.equals(digits, ds)) {
                    continue outer;
                }
            }
            return i;
        }
    }

    // 51、Prime digit replacements
    public static int minPrimeDigitReplacements() {
        int[] primes = MathUtils.primes(1000000);
        boolean[] isPrime = new boolean[1000000];
        for (int prime : primes) {
            isPrime[prime] = true;
        }
        HashMap<Integer, List<Integer>> digitMap = new HashMap<>();
        for (int prime : primes) {
            int stringSize = MathUtils.stringSize(prime);
            char[] digits = Integer.toString(prime).toCharArray();
            for (int i = 0, limit = digits.length; i < limit; ++i) {
                int digit = digits[i] - 48;
                if (digit > 2) {
                    continue;
                }
                List<Integer> list = digitMap.get(digit);
                if (list == null) {
                    list = new ArrayList<Integer>();
                    list.add(i);
                    digitMap.put(digit, list);
                } else {
                    list.add(i);
                }
            }
            for (Entry<Integer, List<Integer>> entry : digitMap.entrySet()) {
                int digit = entry.getKey();
                List<Integer> bitList = entry.getValue();
                int size = bitList.size();
                int limit = 2 - digit;
                int[] digitPos = bitList.stream().mapToInt(Integer::valueOf).toArray();
                for (int i = 1; i <= size; ++i) {
                    for (int[] ps : MathUtils.nchoosek(digitPos, i)) {
                        int bitValue = 0;
                        for (int pos : ps) {
                            bitValue += Math.pow(10, stringSize - 1 - pos);
                        }
                        int valueNonBit = prime - digit * bitValue;
                        int bitCount = 0;
                        for (int j = digit + 1; j < 10; ++j) {
                            int value = j * bitValue + valueNonBit;
                            if (!isPrime[value] && ++bitCount > limit) {
                                break;
                            }
                        }
                        if (bitCount <= limit) {
                            return prime;
                        }
                    }
                }
            }
            digitMap.clear();
        }
        return -1;
    }

    // 50、Consecutive prime sum
    public static int sumConsecutivePrime() {
        int[] primes = MathUtils.primes(1000000);
        int len = primes.length;
        boolean[] isPrime = new boolean[1000000];
        for (int prime : primes) {
            isPrime[prime] = true;
        }
        int maxLen = 0;
        int value = 0;
        for (int i = 0; i + maxLen < len; ++i) {
            int primeLen = 0;
            int sum = 0;
            for (int j = i; (sum += primes[j]) < 1000000; ++j) {
                primeLen = j - i + 1;
                if (primeLen > maxLen && isPrime[sum]) {
                    maxLen = primeLen;
                    value = sum;
                }
            }
        }
        return value;
    }

    // 49、Prime permutations
    public static String primePermutations() {
        int[] primes = MathUtils.primes(10000);
        HashSet<Integer> primeSet = new HashSet<Integer>();
        for (int prime : primes) {
            if (prime > 1000) {
                primeSet.add(prime);
            }
        }
        primes = null;
        int[] cache = new int[12];
        for (int prime : primeSet) {
            char[] digits = Integer.toString(prime).toCharArray();
            int count = 0;
            for (char[] cs : perms(digits)) {
                int value = Integer.parseInt(new String(cs));
                if (primeSet.contains(value)) {
                    cache[count++] = value;
                }
            }
            if (count >= 3) {
                primes = Arrays.copyOf(cache, count);
                for (int[] ps : MathUtils.nchoosek(primes, 3)) {
                    Arrays.sort(ps);
                    if (ps[1] * 2 == ps[0] + ps[2]) {
                        if (ps[0] != 1487) {
                            return String.valueOf(ps[0]) + String.valueOf(ps[1]) + String.valueOf(ps[2]);
                        }
                    }
                }
            }
        }
        return null;
    }

    public static List<char[]> perms(char[] array) {
        int len = array.length;
        if (len == 0) {
            return new ArrayList<char[]>(0);
        }
        List<char[]> permList = new ArrayList<char[]>(len);
        Arrays.sort(array);
        int remaining = len - 1;
        while (true) {
            permList.add(Arrays.copyOf(array, len));
            int replace = remaining;
            while (replace >= 1 && array[replace] <= array[--replace]) ;
            if (array[replace] >= array[replace + 1]) {
                break;
            }
            int replaced = replace;
            while (++replaced < len && array[replaced] > array[replace]) ;
            ArrayUtils.swap(array, replace++, --replaced);
            ArrayUtils.reverse(array, replace, remaining);
        }
        return permList;
    }

    // 48、Self Powers
    public static String selfPowers() { // 9110846700
        BigInteger bi = new BigInteger("0");
        for (int i = 1; i <= 1000; i++) {
            String str = Integer.toString(i);
            BigInteger calValue = new BigInteger(str);
            for (int j = 2; j <= i; j++) {
                calValue = calValue.multiply(new BigInteger(str));
                String value = calValue.toString();
                int len = value.length();
                if (value.length() > 10) {
                    value = value.substring(len - 10);
                    calValue = new BigInteger(value);
                }
            }
            bi = bi.add(calValue);
        }
        String value = bi.toString();
        return value.substring(value.length() - 10);
    }

    // 47、Distinct primes factors
    public static int firstFourDistinctPrimesFactors() { // 134043
        int[] factors = null;
        outer:
        for (int i = 1000; i < 200000; ++i) {
            for (int iLimit = i + 4; i < iLimit; ++i) {
                factors = MathUtils.factor(i);
                int len = 1;
                for (int j = factors.length - 1; j >= 1; ) {
                    if (factors[j--] != factors[j]) {
                        ++len;
                    }
                }
                if (len != 4) {
                    continue outer;
                }
            }
            return i - 4;
        }
        return 0;
    }

    // 46、Goldbach's other conjecture
    public static int minOddNumber() {
        int[] primes = MathUtils.primes(10000);
        outer:
        for (int i = 11; i < 10000; i += 2) {
            if (MathUtils.isPrime(i)) {
                continue;
            }
            int prime = 0;
            for (int j = 1; (prime = primes[j]) < i; ++j) {
                if (MathUtils.isSquare((i - prime) >> 1)) {
                    continue outer;
                }
            }
            return i;
        }
        return 1;
    }

    // 45、Triangular, pentagonal, and hexagonal
    public static int nextTriangularAndPentagonalAndHexagonal() {
        // T(n) = H((m + 1) / 2) 当 n为奇数时
        // T(n) = P(m),则整数delta = sqrt(1 + 12 * n *(n + 1))，整数m = (1 + delta) / 6
        int next = 0;
        for (long i = 287; i < 100000; i += 2) {
            long p = i * (i + 1);
            long delta = 1 + 12 * p;
            if (MathUtils.isSquare(delta) && ((int) Math.sqrt(delta) + 1) % 6 == 0) {
                next = (int) (p >> 1);
            }
        }
        return next;
    }

    // 44、Pentagon numbers
    public static int pentagonNumber() {
        int size = 3000;
        int[] pentagons = new int[size + 1];
        HashSet<Integer> pentagonSet = new HashSet<Integer>((int) (size / 0.75f));
        for (int i = 1; i <= size; ++i) {
            pentagonSet.add(pentagons[i] = (i * (3 * i - 1)) >> 1);
        }
        int minDiff = Integer.MAX_VALUE;
        int diff = 0;
        for (int i = 1; i < size; ++i) {
            for (int j = i + 1; j < size; ++j) {
                if (pentagonSet.contains(pentagons[j] + pentagons[i]) &&
                        pentagonSet.contains(diff = (pentagons[j] - pentagons[i]))) {
                    if (diff < minDiff) {
                        minDiff = diff;
                    }
                }
            }
        }
        return minDiff;
    }

    // 43、Sub-string divisibility
    public static long sumSubStringDivisibility() {
        long sum = 0L;
        for (int[] array : MathUtils.perms(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})) {
            // d2d3d4能被2整除
            if ((array[3] & 1) == 1) {
                continue;
            }
            // d3d4d5能被3整除
            if ((array[2] + array[3] + array[4]) % 3 != 0) {
                continue;
            }
            // d4d5d6能被5整除
            if (array[5] != 0 && array[5] != 5) {
                continue;
            }
            // d5d6d7能被7整除
            if ((array[4] * 100 + array[5] * 10 + array[6]) % 7 != 0) {
                continue;
            }
            // d6d7d8能被11整除
            if (array[6] != array[5] + array[7] && array[6] + 11 != array[5] + array[7]) {
                continue;
            }
            // d7d8d9能被13整除
            if ((array[6] * 100 + array[7] * 10 + array[8]) % 13 != 0) {
                continue;
            }
            // d8d9d10能被17整除
            if ((array[7] * 100 + array[8] * 10 + array[9]) % 17 != 0) {
                continue;
            }
            long value = array[0];
            for (int i = 1; i <= 9; ++i) {
                value = value * 10 + array[i];
            }
            sum += value;
        }
        return sum;
    }

    // 42、Coded triangle numbers
    public static int maxTriangleNumbers() {
        File file = new File("src/p042_words.txt");
        HashSet<Integer> sumSet = new HashSet<>((int) (100 / 0.75f), 0.75f);
        for (int i = 1; i < 100; ) {
            sumSet.add((i * ++i) / 2);
        }
        int count = 0;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            int sum = 0;
            int c = 0;
            while ((c = bis.read()) != -1) {
                if (c >= 'A' && c <= 'Z') {
                    sum += c - 64;
                } else {
                    if (sum > 0) {
                        if (sumSet.contains(sum)) {
                            ++count;
                        }
                        sum = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // 41、Pandigital prime
    public static int maxPandigitalPrime() {
        int maxPrime = 0;
        final int[] digits = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int size = 2; size < 9; ++size) {
            int[] pow = new int[size];
            int len = size - 1;
            for (int i = 0; i < size; ++i) {
                pow[i] = (int) Math.pow(10, len - i);
            }
            for (int[] array : MathUtils.perms(Arrays.copyOf(digits, size))) {
                if (array[len] % 2 != 0 && array[len] != 5) {
                    int value = 0;
                    for (int i = 0; i <= len; ++i) {
                        value += array[i] * pow[i];
                    }
                    if (value > maxPrime && MathUtils.isPrime(value)) {
                        if (value > maxPrime) {
                            maxPrime = value;
                        }
                    }
                }
            }
        }
        return maxPrime;
    }

    // 40、Champernowne’s constant
    public static int champernowneConstant() {
        // d1 × d10 × d100 × d1000 × d10000 × d100000 × d1000000
        int prob = 1;
        for (int i = 1; i <= 1000000; i *= 10) {
            prob *= Character.digit(digit(i), 10);
        }
        return prob;
    }

    // an = 9 * n * 10 ^ (n - 1)  --->  Sn = n * 10 ^ n - (10 ^ n - 1) / 9
    private static char digit(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("The value of n must be greater than zero.");
        }
        int stringSize = 1;
        int pow = 1;
        while (true) {
            int Sn = stringSize * (pow *= 10) - (pow - 1) / 9;
            if (n > Sn) {
                ++stringSize;
            } else {
                break;
            }
        }
        pow /= 10;
        int remaining = n - (stringSize - 1) * pow + (pow - 1) / 9;
        int valuePos = (remaining - 1) / stringSize;
        int digitPos = remaining - valuePos * stringSize;
        return Integer.toString(pow + valuePos).charAt(digitPos - 1);
    }

    // 39、Integer right triangles
    public static int maxLengthOfIntegerTriangles() {
        /**
         * a < b < c
         * a + b + c = m ---> a + b > m / 2 ---> ( b > m / 4 , a > m / 2 - b)
         * a ^ 2 + b ^ 2 = c ^ 2 ---> a + b = m / 2 + (a * b) / m ---> (m % 2 == 0 && (a * b) % m == 0)
         */
        int limit = 1000;
        int maxValue = 0;
        int maxLen = 0;
        for (int m = 12; m <= limit; m += 2) {
            int half = m / 2;
            int len = 0;
            for (int b = (half >> 1) + 1; b < half; ++b) {
                for (int a = half - b + 1; a < b; ++a) {
                    if ((a * b) % m == 0 && (a + b) == (half + (a * b) / m)) {
                        ++len;
                    }
                }
            }
            if (len > maxLen) {
                maxLen = len;
                maxValue = m;
            }
        }
        return maxValue;
    }

    // 38、Pandigital multiples
    public static int largestPandigital() {
        char[] digits = new char[9];
        int maxValue = 0;
        outer:
        for (int i = 1; i < 10000; ++i) {
            int len = 0, prod = 0, index = 0, stringSize = 0;
            for (int j = 1; j < 10; ++j) {
                prod = i * j;
                stringSize = MathUtils.stringSize(prod);
                len += stringSize;
                if (len > 9) {
                    continue outer;
                }
                System.arraycopy(String.valueOf(prod).toCharArray(), 0, digits, index, stringSize);
                index += stringSize;
                if (len == 9) {
                    boolean[] verify = new boolean[10];
                    for (char c : digits) {
                        int digit = c - 48;
                        if (verify[digit] || digit == 0) {
                            continue outer;
                        }
                        verify[digit] = true;
                    }
                    int value = Integer.valueOf(new String(digits));
                    if (value > maxValue) {
                        maxValue = value;
                    }
                    break;
                }
            }
        }
        return maxValue;
    }

    // 37、Truncatable primes
    public static int sumTruncatablePrimes() {
        int sum = 0;
        int count = 0;
        outer:
        for (int i = 11; ; ++i) {
            if (MathUtils.isPrime(i)) {
                char[] digits = Integer.toString(i).toCharArray();
                int limit = digits.length - 1;
                for (int j = 0; j < limit; ++j) { // right
                    int value = 0;
                    for (int k = j; k >= 0; --k) {
                        value += (int) Math.pow(10, j - k) * (digits[k] - 48);
                    }
                    if (!MathUtils.isPrime(value)) {
                        continue outer;
                    }
                }
                for (int j = limit; j >= 1; --j) { // left
                    int value = 0;
                    for (int k = limit; k >= j; --k) {
                        value += (int) Math.pow(10, limit - k) * (digits[k] - 48);
                    }
                    if (!MathUtils.isPrime(value)) {
                        continue outer;
                    }
                }
                sum += i;
                if (++count == 11) {
                    break;
                }
            }
        }
        return sum;
    }

    // 36、Double-base palindromes
    public static long sumPalindromic() {
        int limit = 1000000;
        long sum = 0L;
        for (int i = 1; i < limit; ++i) {
            if (isPalindrome(i) && isPalindrome(Integer.toBinaryString(i))) {
                sum += i;
            }
        }
        return sum;
    }

    // 35、Circular primes
    public static int numberOfCirclePrimes() {
        int limit = 1000000;
        int[] primes = MathUtils.primes(limit);
        int count = 0;
        int len = primes.length;
        HashSet<Integer> primeSet = new HashSet<Integer>((int) (len / 0.75f), 0.75f);
        for (int prime : primes) {
            primeSet.add(prime);
        }
        primes = null;
        outer:
        for (int prime : primeSet) {
            int primeSize = MathUtils.stringSize(prime);
            int pow = (int) Math.pow(10, primeSize - 1);
            int value = prime;
            while (true) {
                value = (value % 10) * pow + value / 10;
                if (!primeSet.contains(value)) {
                    continue outer;
                }
                if (value == prime) {
                    break;
                }
            }
            ++count;
        }
        return count;
    }

    // 34、Digit factorials
    public static long sumDigitFactorials() {
        final int[] factorialCache = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880};
        long sum = 0L;
        for (int i = 3; i < 9999999; ++i) {
            int fac = 0;
            int value = i;
            while (true) {
                if (value < 10) {
                    fac += factorialCache[value];
                    break;
                }
                fac += factorialCache[value % 10];
                value /= 10;
            }
            if (fac == i) {
                System.out.print(i + " ");
                sum += i;
            }
        }
        return sum;
    }

    // 33、Digit cancelling fractions
    public static int prodDigitCancellingFractions() {
        int numerator = 1, denominator = 1;
        for (int i = 10; i < 100; i++) {
            for (int j = i + 1; j < 100; j++) {
                if ((i % 10) == (j / 10)) {
                    int numer = i / 10;
                    int denomin = j % 10;
                    if (i * denomin == j * numer) {
                        numerator *= numer;
                        denominator *= denomin;
                    }
                }
            }
        }
        HashMap<Integer, Integer> factorMap = new HashMap<Integer, Integer>();
        for (int factor : MathUtils.factor(denominator)) {
            Integer count = factorMap.get(factor);
            factorMap.put(factor, count == null ? 1 : count + 1);
        }
        for (int factor : MathUtils.factor(numerator)) {
            Integer count = factorMap.get(factor);
            if (count != null) {
                if (count == 1) {
                    factorMap.remove(factor);
                } else {
                    factorMap.put(factor, count - 1);
                }
            }
        }
        int prod = 1;
        for (Entry<Integer, Integer> entry : factorMap.entrySet()) {
            prod *= (int) Math.pow(entry.getKey(), entry.getValue());
        }
        return prod;
    }

    // 32、Pandigital products
    public static long sumPandigitaProducts() {
        List<int[]> perms = MathUtils.perms(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        TreeSet<Integer> set = new TreeSet<Integer>();
        for (int[] array : perms) {
            // 1、4、4
            if ((array[0] * array[4]) % 10 == array[8]) {
                int a = array[0];
                int b = 1000 * array[1] + 100 * array[2] + 10 * array[3] + array[4];
                int c = 1000 * array[5] + 100 * array[6] + 10 * array[7] + array[8];
                if (c == a * b) {
                    set.add(c);
                }
            }
            // 2、3、4
            if ((array[1] * array[4]) % 10 == array[8]) {
                int a = 10 * array[0] + array[1];
                int b = 100 * array[2] + 10 * array[3] + array[4];
                int c = 1000 * array[5] + 100 * array[6] + 10 * array[7] + array[8];
                if (c == a * b) {
                    set.add(c);
                }
            }
        }
        long sum = 0L;
        for (Integer value : set) {
            sum += value;
        }
        return sum;
    }

    // 31、Coin sums
    public static int coinSums(int sum) {
        if (sum <= 0) {
            throw new IllegalArgumentException("Sum must be a positive number.");
        }
        return partitionInteger(sum, 0, 0, 0, new int[]{1, 2, 5, 10, 20, 50, 100, 200});
    }

    private static int partitionInteger(int sum, int index, int cumulativeValue, int count, int[] values) {
        for (int i = index; i < 8; ++i) {
            cumulativeValue += values[i];
            if (cumulativeValue < sum) {
                count = partitionInteger(sum, i, cumulativeValue, count, values);
            } else if (cumulativeValue == sum) {
                ++count;
            } else {
                break;
            }
            cumulativeValue -= values[i];
        }
        return count;
    }

    // 30、Digit fifth powers
    public static int digitFifthPowers() {
        int[] cache = new int[10];
        int exp = 5;
        for (int i = 0; i < 10; ++i) {
            cache[i] = (int) (Math.pow(i, exp));
        }
        int sum = 0;
        for (int i = 2; i < 1000000; ++i) {
            int value = i;
            int result = 0;
            while (true) {
                if (value < 10) {
                    result += cache[value];
                    break;
                }
                result += cache[value % 10];
                value /= 10;
            }
            if (result == i) {
                sum += i;
            }
        }
        return sum;
    }

    // 29、Distinct powers
    public static int numberOfDistinctPowers(int limit) {
        TreeSet<Integer> set = new TreeSet<Integer>();
        int count = 0;
        for (int base = 2; base <= limit; ++base) {
            if (set.contains(base)) {
                continue;
            }
            int maxExp = 0;
            for (int b = base; b <= limit; b *= base) {
                ++maxExp;
                set.add(b);
            }
            boolean[] array = new boolean[maxExp * limit + 1];
            for (int exp = 1; exp <= maxExp; ++exp) {
                for (int multiple = 2; multiple <= limit; ++multiple) {
                    array[exp * multiple] = true;
                }
            }
            for (boolean b : array) {
                if (b) {
                    ++count;
                }
            }
        }
        return count;
    }

    // 28、Number spiral diagonals
    // 左上：n ^ 2 - n + 1
    // 右上：n ^ 2
    // 左下：n ^ 2 - 2 * n + 2
    // 右下：n ^ 2 - 3 * n + 3
    // 综合：4 * n * n - 6 * n + 6  (n > 1)
    public static long numberOfSpiralDiagonals() {
        int limit = 1001;
        long sum = 1L;
        for (int n = 3; n <= limit; n += 2) {
            sum += 4 * n * n - 6 * n + 6;
        }
        return sum;
    }

    // 27、Quadratic primes
    public static int quadraticPrimes() {
        int a = 0;
        int b = 0;
        int maxLen = 1;
        int[] primes = MathUtils.primes(1000);
        for (int prime : primes) {
            int limit = 2 * ((int) Math.sqrt(prime));
            for (int value = -limit; value <= limit; ++value) { // a ^ 2 < 4 * b
                int len = 1;
                for (int i = 1; ; ++i) {
                    int result = i * i + value * i + prime;
                    if (MathUtils.isPrime(result)) {
                        ++len;
                    } else {
                        break;
                    }
                }
                if (len > maxLen) {
                    maxLen = len;
                    a = value;
                    b = prime;
                }
            }
        }
        return a * b;
    }

    // 26、Reciprocal cycles
    public static int maxReciprocalCyclesLen(int limit) {
        int maxLen = 0;
        int value = 1;
        for (int i = 1; i < limit; ++i) {
            int len = reciprocalCyclesLen(i);
            if (len > maxLen) {
                maxLen = len;
                value = i;
            }
        }
        return value;
    }

    private static int reciprocalCyclesLen(int denominator) {
        int numerator = 10;
        int index = 0;
        HashMap<Integer, Integer> remainderMap = new HashMap<>();
        Integer result = null;
        while (true) {
            while (numerator < denominator) {
                ++index;
                numerator *= 10;
            }
            ++index;
            numerator = numerator % denominator;
            if (numerator == 0) {
                return index;
            }
            result = remainderMap.get(numerator);
            if (result != null) {
                break;
            }
            remainderMap.put(numerator, index);
            numerator *= 10;
        }
        return index - result;
    }

    // 25、1000-digit Fibonacci number
    public static int getIndexFibNumber(int limit) {
        BigInteger first = new BigInteger("1");
        BigInteger next = new BigInteger("1");
        int index = 2;
        while (true) {
            ++index;
            next = first.add(first = next);
            if (next.toString().length() == 1000) {
                break;
            }
        }
        return index;
    }

    // 24、Lexicographic permutations
    public static int[] lexPerms() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int len = array.length;
        int remaining = len - 1;
        int index = 0;
        while (true) {
            if (++index == 1000000) {
                break;
            }
            int replace = remaining;
            while (replace >= 1 && array[replace] <= array[--replace]) ;
            if (array[replace] >= array[replace + 1]) {
                break;
            }
            int replaced = replace;
            while (++replaced < len && array[replaced] > array[replace]) ;
            ArrayUtils.swap(array, replace++, --replaced);
            ArrayUtils.reverse(array, replace, remaining);
        }
        return array;
    }

    // 23、Non-abundant sums
    public static int sumNonAbundant() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 28123; i++) {
            if (i < sumProperDivisors(i)) {
                list.add(i);
            }
        }
        int[] abundants = list.stream().mapToInt(Integer::valueOf).toArray();
        int len = abundants.length;
        boolean[] isAbundant = new boolean[28124];
        for (int i = 0, limit = 28124 >>> 1; i < len; ++i) {
            for (int j = i + 1; j < len; ++j) {
                int value = abundants[i] + abundants[j];
                if (value > 28123) {
                    break;
                } else {
                    isAbundant[value] = true;
                }
            }
            if (abundants[i] > limit) {
                break;
            }
        }
        int sum = 0;
        for (int i = 1; i < 28124; ++i) {
            if (!isAbundant[i]) {
                sum += i;
            }
        }
        return sum;
    }

    // 22、Names scores
    public static long nameScores() {
        File file = new File("src/p022_names.txt");
        TreeMap<String, Integer> scoreMap = new TreeMap<>();
        StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        int index = 0;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            int score = 0;
            int c = 0;
            while ((c = bis.read()) != -1) {
                if (c >= 'A' && c <= 'Z') {
                    if (index >= sb.length()) {
                        sb.append((char) c);
                    } else {
                        sb.setCharAt(index, (char) c);
                    }
                    ++index;
                    score += c - 64;
                } else {
                    if (score > 0) {
                        scoreMap.put(sb.substring(0, index), score);
                        score = 0;
                        index = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long sum = 0;
        index = 0;
        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            sum += ++index * entry.getValue().intValue();
        }
        return sum;
    }

    // 21、Amicable numbers
    public static long sumAllAmicableNumbers(int limit) {
        long sum = 0;
        HashMap<Integer, Object> amicableNumberMap = new HashMap<Integer, Object>();
        for (int i = 1; i < 10000; i++) {
            if (amicableNumberMap.containsKey(i)) {
                continue;
            }
            int next = sumProperDivisors(i);
            if (i != next && i == sumProperDivisors(next)) {
                sum += i + next;
                amicableNumberMap.put(next, null);
            }
        }
        return sum;
    }

    private static int sumProperDivisors(int value) {
        int[] factors = MathUtils.factor(value);
        int other = 1;
        for (int i = 1; i < factors.length; i++) {
            for (int[] ns : MathUtils.nchoosek(factors, i)) {
                int count = 1;
                for (int n : ns) {
                    count *= n;
                }
                other += count;
            }
        }
        return other;
    }

    // 20、Factorial digit sum
    public static int facDigitSum() {
        BigInteger bi = new BigInteger("1");
        for (int i = 2; i <= 100; i++) {
            bi = bi.multiply(new BigInteger(String.valueOf(i)));
        }
        String str = bi.toString();
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += str.charAt(i) - 48;
        }
        return sum;
    }

    // 19、Counting Sundays
    public static int countSundays() {
        LocalDate begin = LocalDate.of(1901, 1, 1);
        long cutoff = LocalDate.of(2000, 12, 31).toEpochDay();
        int count = 0;
        while (begin.toEpochDay() <= cutoff) {
            if (begin.getDayOfWeek().ordinal() == 6) {
                ++count;
                System.out.println(begin);
            }
            begin = begin.plusMonths(1);
        }
        System.out.println(begin);
        return count;
    }

    // 18、 Maximum path sum I
    public static int maxPath() {
        int[][] triangle = {{75}, {95, 64}, {17, 47, 82}, {18, 35, 87, 10}, {20, 04, 82, 47, 65}, {19, 01, 23, 75, 03, 34}, {88, 02, 77, 73, 07, 63, 67}, {99, 65, 04, 28, 06, 16, 70, 92}, {41, 41, 26, 56, 83, 40, 80, 70, 33}, {41, 48, 72, 33, 47, 32, 37, 16, 94, 29}, {53, 71, 44, 65, 25, 43, 91, 52, 97, 51, 14}, {70, 11, 33, 28, 77, 73, 17, 78, 39, 68, 17, 57}, {91, 71, 52, 38, 17, 14, 91, 43, 58, 50, 27, 29, 48}, {63, 66, 04, 68, 89, 53, 67, 30, 73, 16, 69, 87, 40, 31}, {04, 62, 98, 27, 23, 9, 70, 98, 73, 93, 38, 53, 60, 04, 23}};
        return path(triangle, triangle.length, 0, 0, 0);
    }

    private static int path(int[][] triangle, int size, int row, int column, int value) {
        if (size == row) {
            return value;
        }
        int columnLen = triangle[row].length;
        if (column == columnLen) {
            return value;
        }
        if (row == size - 1) {
            return value + triangle[row][column];
        }
        return triangle[row][column] + Math.max(path(triangle, size, row + 1, column, value), path(triangle, size, row + 1, column + 1, value));
    }

    // 17、Number letter counts
    public static int len() {
        int len = 0;
        for (int i = 1; i < 1000; i++) {
            len += numberToWords(i).length();
        }
        return len + 11;
    }

    private static String numberToWords(int value) {
        final String[] to20 = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
        final String[] tees = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
        StringBuilder sb = new StringBuilder();
        if (value > 99) {
            sb.append(to20[value / 100] + "Hundred");
            value = value % 100;
            if (value > 0) {
                sb.append("and");
            }
        }
        if (value > 19) {
            sb.append(tees[value / 10]);
            value = value % 10;
        }
        if (value > 0) {
            sb.append(to20[value]);
        }
        return sb.toString();
    }

    // 16、Power digit sum
    public static int powerDigitSum(int exp) {
        BigInteger bi = new BigInteger("2");
        bi = bi.pow(1000);
        String str = bi.toString();
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += str.charAt(i) - 48;
        }
        return sum;
    }

    // 15、Lattice paths
    public static long pathNum(int gridSize) {
        if (gridSize < 0) {
            throw new IllegalArgumentException();
        }
        return MathUtils.nchoosek(gridSize * 2, gridSize);
    }

    // 14、	Longest Collatz sequence
    // n → n/2 (n is even)
    // n → 3n + 1 (n is odd)
    public static int largestCollatzSequenceNum(int limit) {
        int maxLen = 1;
        int largestCollatz = 1;
        int[] counts = new int[limit + 1];
        for (int i = 2; i <= limit; ++i) {
            int len = 1;
            int value = i;
            while (value != 1) {
                if (value < i && value > 0) {
                    len += counts[value];
                    break;
                }
                if ((value & 1) == 1) {
                    value = value + ((value + 1) >>> 1);
                    len += 2;
                } else {
                    int tailNum = Integer.numberOfTrailingZeros(value);
                    value >>>= tailNum;
                    len += tailNum;
                }
            }
            counts[i] = len;
            if (len > maxLen) {
                maxLen = len;
                largestCollatz = i;
            }
        }
        return largestCollatz;
    }

    // 13、Large sum
    public static String largeNum() {
        String value = "37107287533902102798797998220837590246510135740250|46376937677490009712648124896970078050417018260538|74324986199524741059474233309513058123726617309629|91942213363574161572522430563301811072406154908250|23067588207539346171171980310421047513778063246676|89261670696623633820136378418383684178734361726757|28112879812849979408065481931592621691275889832738|44274228917432520321923589422876796487670272189318|47451445736001306439091167216856844588711603153276|70386486105843025439939619828917593665686757934951|62176457141856560629502157223196586755079324193331|64906352462741904929101432445813822663347944758178|92575867718337217661963751590579239728245598838407|58203565325359399008402633568948830189458628227828|80181199384826282014278194139940567587151170094390|35398664372827112653829987240784473053190104293586|86515506006295864861532075273371959191420517255829|71693888707715466499115593487603532921714970056938|54370070576826684624621495650076471787294438377604|53282654108756828443191190634694037855217779295145|36123272525000296071075082563815656710885258350721|45876576172410976447339110607218265236877223636045|17423706905851860660448207621209813287860733969412|81142660418086830619328460811191061556940512689692|51934325451728388641918047049293215058642563049483|62467221648435076201727918039944693004732956340691|15732444386908125794514089057706229429197107928209|55037687525678773091862540744969844508330393682126|18336384825330154686196124348767681297534375946515|80386287592878490201521685554828717201219257766954|78182833757993103614740356856449095527097864797581|16726320100436897842553539920931837441497806860984|48403098129077791799088218795327364475675590848030|87086987551392711854517078544161852424320693150332|59959406895756536782107074926966537676326235447210|69793950679652694742597709739166693763042633987085|41052684708299085211399427365734116182760315001271|65378607361501080857009149939512557028198746004375|35829035317434717326932123578154982629742552737307|94953759765105305946966067683156574377167401875275|88902802571733229619176668713819931811048770190271|25267680276078003013678680992525463401061632866526|36270218540497705585629946580636237993140746255962|24074486908231174977792365466257246923322810917141|91430288197103288597806669760892938638285025333403|34413065578016127815921815005561868836468420090470|23053081172816430487623791969842487255036638784583|11487696932154902810424020138335124462181441773470|63783299490636259666498587618221225225512486764533|67720186971698544312419572409913959008952310058822|95548255300263520781532296796249481641953868218774|76085327132285723110424803456124867697064507995236|37774242535411291684276865538926205024910326572967|23701913275725675285653248258265463092207058596522|29798860272258331913126375147341994889534765745501|18495701454879288984856827726077713721403798879715|38298203783031473527721580348144513491373226651381|34829543829199918180278916522431027392251122869539|40957953066405232632538044100059654939159879593635|29746152185502371307642255121183693803580388584903|41698116222072977186158236678424689157993532961922|62467957194401269043877107275048102390895523597457|23189706772547915061505504953922979530901129967519|86188088225875314529584099251203829009407770775672|11306739708304724483816533873502340845647058077308|82959174767140363198008187129011875491310547126581|97623331044818386269515456334926366572897563400500|42846280183517070527831839425882145521227251250327|55121603546981200581762165212827652751691296897789|32238195734329339946437501907836945765883352399886|75506164965184775180738168837861091527357929701337|62177842752192623401942399639168044983993173312731|32924185707147349566916674687634660915035914677504|99518671430235219628894890102423325116913619626622|73267460800591547471830798392868535206946944540724|76841822524674417161514036427982273348055556214818|97142617910342598647204516893989422179826088076852|87783646182799346313767754307809363333018982642090|10848802521674670883215120185883543223812876952786|71329612474782464538636993009049310363619763878039|62184073572399794223406235393808339651327408011116|66627891981488087797941876876144230030984490851411|60661826293682836764744779239180335110989069790714|85786944089552990653640447425576083659976645795096|66024396409905389607120198219976047599490197230297|64913982680032973156037120041377903785566085089252|16730939319872750275468906903707539413042652315011|94809377245048795150954100921645863754710598436791|78639167021187492431995700641917969777599028300699|15368713711936614952811305876380278410754449733078|40789923115535562561142322423255033685442488917353|44889911501440648020369068063960672322193204149535|41503128880339536053299340368006977710650566631954|81234880673210146739058568557934581403627822703280|82616570773948327592232845941706525094512325230608|22918802058777319719839450180888072429661980811197|77158542502016545090413245809786882778948721859617|72107838435069186155435662884062257473692284509516|20849603980134001723930671666823555245252804609722|53503534226472524250874054075591789781264330331690";
        String[] nums = value.split("\\|");
        BigInteger bi = new BigInteger("0");
        for (String s : nums) {
            System.out.println(s);
            bi = bi.add(new BigInteger(s));
        }
        return bi.toString().substring(0, 10);
    }

    // 12、Highly divisible triangular number
    public static int triangularNumberOverNum(int num) {
        if (num < 1) {
            throw new IllegalArgumentException();
        }
        if (num == 1) {
            return 3;
        }
        // n * (n + 1) / 2
        int n = 1;
        while (true) {
            int triangularNum = (n * (++n)) / 2;
            int factorNum = 1;
            int[] factors = MathUtils.factor(triangularNum);
            for (int i = factors.length; i > 0; --i) {
                factorNum += MathUtils.nchoosek(factors, i).size();
            }
            if (factorNum > num) {
                return triangularNum;
            }
        }
    }

    // 11、Largest product in a grid
    public static int largestProductInGrid() {
        String value = "08 02 22 97 38 15 00 40 00 75 04 05 07 78 52 12 50 77 91 08 "
                + "49 49 99 40 17 81 18 57 60 87 17 40 98 43 69 48 04 56 62 00 "
                + "81 49 31 73 55 79 14 29 93 71 40 67 53 88 30 03 49 13 36 65 "
                + "52 70 95 23 04 60 11 42 69 24 68 56 01 32 56 71 37 02 36 91 "
                + "22 31 16 71 51 67 63 89 41 92 36 54 22 40 40 28 66 33 13 80 "
                + "24 47 32 60 99 03 45 02 44 75 33 53 78 36 84 20 35 17 12 50 "
                + "32 98 81 28 64 23 67 10 26 38 40 67 59 54 70 66 18 38 64 70 "
                + "67 26 20 68 02 62 12 20 95 63 94 39 63 08 40 91 66 49 94 21 "
                + "24 55 58 05 66 73 99 26 97 17 78 78 96 83 14 88 34 89 63 72 "
                + "21 36 23 09 75 00 76 44 20 45 35 14 00 61 33 97 34 31 33 95 "
                + "78 17 53 28 22 75 31 67 15 94 03 80 04 62 16 14 09 53 56 92 "
                + "16 39 05 42 96 35 31 47 55 58 88 24 00 17 54 24 36 29 85 57 "
                + "86 56 00 48 35 71 89 07 05 44 44 37 44 60 21 58 51 54 17 58 "
                + "19 80 81 68 05 94 47 69 28 73 92 13 86 52 17 77 04 89 55 40 "
                + "04 52 08 83 97 35 99 16 07 97 57 32 16 26 26 79 33 27 98 66 "
                + "88 36 68 87 57 62 20 72 03 46 33 67 46 55 12 32 63 93 53 69 "
                + "04 42 16 73 38 25 39 11 24 94 72 18 08 46 29 32 40 62 76 36 "
                + "20 69 36 41 72 30 23 88 34 62 99 69 82 67 59 85 74 04 36 16 "
                + "20 73 35 29 78 31 90 01 74 31 49 71 48 86 81 16 23 57 05 54 "
                + "01 70 54 71 83 51 54 69 16 92 33 48 61 43 52 01 89 19 67 48 ";
        String[] values = value.split(" ");
        int[][] digits = new int[20][20];
        int row = 0;
        int column = 0;
        for (String v : values) {
            int digit = (v.charAt(0) - 48) * 10 + (v.charAt(1) - 48);
            if (column == 20) {
                digits[++row][0] = digit;
                column = 1;
            } else {
                digits[row][column++] = digit;
            }
        }
        int maxProd = 0, prod = 0;
        for (row = 0; row < 20; ++row) {
            for (column = 0; column < 20; ++column) {
                // 右边情况
                if (column < 17) {
                    prod = digits[row][column] * digits[row][column + 1]
                            * digits[row][column + 2] * digits[row][column + 3];
                    if (prod > maxProd) {
                        maxProd = prod;
                    }
                }
                // 下边情况
                if (row < 17) {
                    prod = digits[row][column] * digits[row + 1][column]
                            * digits[row + 2][column] * digits[row + 3][column];
                    if (prod > maxProd) {
                        maxProd = prod;
                    }
                    // 左下的情况
                    if (column > 2) {
                        prod = digits[row][column] * digits[row + 1][column - 1]
                                * digits[row + 2][column - 2] * digits[row + 3][column - 3];
                        if (prod > maxProd) {
                            maxProd = prod;
                        }
                    }
                    // 右下情况
                    if (column < 17) {
                        prod = digits[row][column] * digits[row + 1][column + 1]
                                * digits[row + 2][column + 2] * digits[row + 3][column + 3];
                        if (prod > maxProd) {
                            maxProd = prod;
                        }
                    }
                }
            }
        }
        return maxProd;
    }

    // 10、Summation of primes
    public static long sumPrimes(int limit) {
        if (limit < 0) {
            return -1L;
        }
        long sum = 0L;
        int[] primes = MathUtils.primes(2000000);
        for (int prime : primes) {
            sum += prime;
        }
        return sum;
    }

    // 9、Special Pythagorean triplet
    public static int specialPythagoreanTriplet(int sum) {
        int limit = (int) ((sum - 3) / 3);
        for (int i = 1; i <= limit; i++) {
            if ((i * i) % (sum - i) == 0) {
                int v1 = sum - 3 * i;
                int v2 = (i * i) / (sum - i);
                int n = (v1 + v2) / 2;
                int m = (v1 - v2) / 2;
                if (m > 0 && n > m) {
                    return i * (i + m) * (i + n);
                }
            }
        }
        return -1;
    }

    // 8、Largest product in a series

    /**
     * String value = "73167176531330624919225119674426574742355349194934"
     * +"96983520312774506326239578318016984801869478851843"
     * +"85861560789112949495459501737958331952853208805511"
     * +"12540698747158523863050715693290963295227443043557"
     * +"66896648950445244523161731856403098711121722383113"
     * +"62229893423380308135336276614282806444486645238749"
     * +"30358907296290491560440772390713810515859307960866"
     * +"70172427121883998797908792274921901699720888093776"
     * +"65727333001053367881220235421809751254540594752243"
     * +"52584907711670556013604839586446706324415722155397"
     * +"53697817977846174064955149290862569321978468622482"
     * +"83972241375657056057490261407972968652414535100474"
     * +"82166370484403199890008895243450658541227588666881"
     * +"16427171479924442928230863465674813919123162824586"
     * +"17866458359124566529476545682848912883142607690042"
     * +"24219022671055626321111109370544217506941658960408"
     * +"07198403850962455444362981230987879927244284909188"
     * +"84580156166097919133875499200524063689912560717606"
     * +"05886116467109405077541002256983155200055935729725"
     * +"71636269561882670428252483600823257530420752963450";
     */
    public static long largestProductInASeries(String value, int num) {
        char[] digits = value.toCharArray();
        int len = value.length();
        long maxProd = 0L, prod = 0L;
        int head = 0, tail;
        int limit = len - num;
        int digit = 0;
        outer:
        while (head <= limit) {
            prod = 1L;
            tail = head;
            for (int cutoff = head + num; tail < cutoff; ++tail) {
                digit = digits[tail] - 48;
                if (digit == 0) {
                    head = tail + 1;
                    continue outer;
                }
                prod *= digit;
            }
            if (prod > maxProd) {
                maxProd = prod;
            }
            while (tail < len) {
                digit = digits[tail++] - 48;
                if (digit == 0) {
                    head = tail;
                    break;
                }
                prod = prod / (digits[head++] - 48) * digit;
                if (prod > maxProd) {
                    maxProd = prod;
                }
            }
        }
        return maxProd;
    }

    // 7、10001st prime
    public static int primeIn10001st() {
        int count = 1;
        for (int i = 3; ; i += 2) {
            if (MathUtils.isPrime(i)) {
                if (++count == 10001) {
                    return i;
                }
            }
        }
    }

    // 6、Sum square difference
    public static int sumSquareDifference(int value) {
        // 前n方和：n * (n + 1) * (2 * n + 1) / 6;
        int v1 = value * (value + 1) * (2 * value + 1) / 6;
        int v2 = value * (value + 1) / 2;
        return v2 * v2 - v1;
    }

    // 5、Smallest multiple
    public static int smallestMultiple(int value) {
        int[] primes = MathUtils.primes(value);
        int expLimit = (int) (Math.log(value) / Math.log(2));
        int i = 1;
        int product = 1;
        for (int prime : primes) {
            product *= prime;
        }
        while (++i <= expLimit) {
            int pow = (int) Math.pow(value, 1.0d / i);
            for (int j = 0; ; ++j) {
                if (primes[j] <= pow) {
                    product *= primes[j];
                } else {
                    break;
                }
            }
        }
        return product;
    }

    // 4、Largest palindrome product
    public static int largestPalindrome3() {
        // 1000000 ~ 998001
        int limit = 99;
        int largest = 0;
        for (int i = 999; i >= limit; --i) {
            for (int j = 999; j >= limit; --j) {
                int product = i * j;
                if (isPalindrome(product)) {
                    if (product > largest) {
                        largest = product;
                    }
                    int nextLimit = Math.min(i, j);
                    if (limit < nextLimit) {
                        limit = nextLimit;
                    }
                }
            }
        }
        return largest;
    }

    public static boolean isPalindrome(int value) {
        if (value < 0) {
            return false;
        }
        return isPalindrome(Integer.toString(value));
    }

    public static boolean isPalindrome(String value) {
        int len = 0;
        if (value == null || (len = value.length()) < 2) {
            return true;
        }
        char[] digits = value.toCharArray();
        int left = 0;
        int right = len - 1;
        while (true) {
            if (digits[left] != digits[right]) {
                return false;
            }
            if (++left >= --right) {
                return true;
            }
        }
    }

    // 3、Largest prime factor
    public static int largestPrimeFactor(long value) {
        if (value <= 1) {
            throw new IllegalArgumentException("The value of limit must be greater than one.");
        }
        int largestFactor = 2;
        while ((value & 1) == 0) {
            value >>= 1;
        }
        int limit = (int) Math.sqrt(value);
        for (int i = 3; i <= limit; i += 2) {
            if (value % i == 0) {
                if (i > largestFactor) {
                    largestFactor = i;
                }
                value /= i;
                if (value == 1) {
                    break;
                }
                i = 1;
            }
        }
        return largestFactor;
    }

    // 2、Even Fibonacci numbers
    public static int evenFibSum(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("The value of limit must be greater and equal to zero.");
        }
        if (limit < 2) {
            return 0;
        }
        int first = 1;
        int second = 1;
        int sum = 0, fib = 0;
        while (fib < limit) {
            fib = first + second;
            if ((fib & 1) == 0) {
                sum += fib;
            }
            first = second;
            second = fib;
        }
        return sum;
    }

    // 	1、Multiples of 3 and 5
    public static int sum(int limit) {
        int threeIndex = 1, fiveIndex = 1;
        int sum = 0, value = 0;
        int three = 3, five = 5;
        while (value < limit) {
            sum += value;
            int tempThree = threeIndex * three;
            int tempFive = fiveIndex * five;
            if (tempThree < tempFive) {
                value = tempThree;
                ++threeIndex;
            } else if (tempThree > tempFive) {
                value = tempFive;
                ++fiveIndex;
            } else {
                value = tempFive;
                ++threeIndex;
                ++fiveIndex;
            }
        }
        return sum;
    }
}
