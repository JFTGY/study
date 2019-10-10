package com.common.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数学工具类
 * @author 江峰
 * @create 2018-11-24
 */
//@SuppressWarnings("unused")
public class MathUtils {
    private MathUtils(){
        throw new IllegalAccessError();
    }

    public static void main(String[] args) throws Exception {
        for(int[] value : nchoosek(new int[]{2,2,7,7,7}, 5)){
            System.out.println(Arrays.toString(value));
        }
    }

    final static int [] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE };
    public static int stringSize(int x) {
        for (int i = 0; ; i++){
            if (x <= sizeTable[i]){
                return i+1;
            }
        }
    }

    public static boolean isSquare(long n){
        // F(x) = 0,1,4,7,9;
        if(n < 0){
            throw new IllegalArgumentException("n = " + n);
        }
//		for(int i = 1; n > 0; i += 2){ // 奇数前n项和
//			n -= i;
//		}
//		return 0 == n;
        long sqrt = (long)Math.sqrt(n);
        return sqrt * sqrt == n;
    }

    static final int[] log2Tabel = {0,0,1,1,2,2,2,2,3,3,3,3,3,3,3,3};
    /**
     * 快速计算log2(n)的整数部分
     * @param n 待计算数值
     * @return log2(n)的整数值
     */
    public static int log2(int n){
        if(n <= 0){
            throw new IllegalArgumentException("N must be an positive integer.");
        }
        int y = 0;
        if(n >= 0x10000){y += 16;n >>>= 16;}
        if(n >= 0x00100){y += 8 ;n >>>= 8 ;}
        if(n >= 0x00010){y += 4 ;n >>>= 4 ;}
        return y + log2Tabel[n];
    }
    /**
     * 一个简单的快速求幂方法，只是作为学习实用，一般还是使用{@code Math.pow(double, double)}
     * @param base 底数值
     * @param exp 指数值
     * @return 幂值
     */
    public static int quickPower(int base,int exp) {
        if(exp < 0){
            throw new IllegalArgumentException("Exp must be a position integer.");
        }
        int result = 1;
        while(exp != 0) {
            if((exp & 1) == 1){
                result *= base;
            }
            base *= base;
            exp >>= 1;
        }
        return result;
    }
    /**
     * 计算begin:1:end的累积值。
     * @param begin 累积的开始值
     * @param end 累积的结束值
     * @return begin到end的累积值，类型为BigInteger。
     * @throws IllegalArgumentException 当begin小于零或者begin大于end时抛出。
     */
    public static BigInteger prod(int begin, int end){
        if(begin < 0 || begin > end){
            throw new IllegalArgumentException("begin = " + begin + ", end = " + end + ".");
        }
        if(begin == 0){
            return new BigInteger("0");
        }
        int dataLen = end - begin + 1;
        int mid = 0;
        BigInteger prodVal = new BigInteger("1");
        int accumNums = 0;
        if((dataLen & 1) == 1){
            mid = (dataLen >> 1) + begin;
            prodVal = prodVal.multiply(new BigInteger(Integer.toString(mid)));
            accumNums = end - mid;
        }else{
            mid = (dataLen >> 1) + begin - 1;
            prodVal = prodVal.multiply(new BigInteger(Long.toString((long)mid * end)));
            accumNums = end - mid - 1;
        }
        long midSquare = (long)mid * mid;
        for(int i = 1; --accumNums >= 0; i += 2){
            prodVal = prodVal.multiply(new BigInteger(Long.toString(midSquare -= i)));
        }
        return prodVal;
    }
    /**
     * 阶乘函数
     *
     * 阶乘系数n如果小于零，则会抛出IllegalArgumentException.
     * n的值如果大于20将会发生溢出，会得到错误的结果，故程序也将抛出IllegalArgumentException.
     *
     * @param n 阶乘系数，其值必须大于等于0，否则抛出IllegalArgumentException。
     * @return n!的值
     */
    public static long factorial(int n){
        if(n < 0 || n > 20){
            throw new IllegalArgumentException("N must be an integer between 0 and 20.");
        }
        if(n <= 1){
            return 1L;
        }
        int mid = (n & 1) == 1 ? (n >> 1) + 1 : (n >> 1);
        int midSquare = mid * mid;
        long fac = (n & 1) == 1 ? mid : midSquare << 1;
        for(int i = 1, limit = 2 * mid - 1; midSquare > limit; i += 2){
            fac *= midSquare -= i;
        }
        return fac;
    }
    /**
     * 判断数值是否是素数，具体参见{@code Prime.isPrime(int)}方法。
     * @param n 待检测是否是素数的整数值
     * @return 如果该数值为素数，返回true，否则返回false。
     * @throws IllegalArgumentException n值小于0
     */
    public static boolean isPrime(int n){
        return Prime.isPrime(n);
    }
    /**
     * 分解质因数，具体参见{@code Prime.factor(int)}方法。
     *
     * @param n 待分解值
     * @return 质因数数组
     * @throws IllegalArgumentException 如果n值大于零
     */
    public static int[] factor(int n){
        return Prime.factor(n);
    }
    /**
     * 获取指定范围的素数。具体参见{@code Prime.primes(int)}方法。
     *
     * @param n 指定素数范围的最大值
     * @return 指定范围内素数数组
     * @throws IllegalArgumentException n值小于0
     */
    public static int[] primes(int n){
        return Prime.primes(n);
    }
    /**
     * 获取正整数n的所有划分数，具体细节参见{@code PartitionInteger.getPartitionNumber(int)}方法
     * @param n 待划分的正整数值
     * @return 返回n整数划分数量
     * @throws IllegalArgumentException n值小于等于0
     */
    public static int getPartitionNumber(int n){
        return PartitionInteger.getPartitionNumber(n);
    }
    /**
     * 获取划分长度为m的n的划分数，具体细节参见{@code PartitionInteger.getPartitionNumber(int, int)}方法
     * @param n 待划分的正整数值
     * @param m 划分的整数数量
     * @return 返回n整数划分数量
     * @throws IllegalArgumentException m小于零或者m大于n。
     */
    public static int getPartitionNum(int n, int m){
        return PartitionInteger.getPartitionNumber(n, m);
    }
    /**
     * 获取n的所有划分排列
     * @param n 待划分的正整数
     * @return n整数划分的所有排列结果
     * @throws IllegalArgumentException n小于等于0
     */
    public static List<int[]> partitionInteger(int n){
        return PartitionInteger.partitionInteger(n);
    }
    /**
     * 获取n划分长度为m的排列
     * @param n 待划分的正整数值
     * @param m 被划分的整数数量
     * @return n被划分为m个整数的排列结果
     * @throws IllegalArgumentException m小于零或者m大于n。
     */
    public static List<int[]> partitionInteger(int n, int m){
        return PartitionInteger.partitionInteger(n, m);
    }
    /**
     * 从n个不同元素中取出m个元素的组合数
     * @param n 元素的种类
     * @param k 需要取出元素的数量
     * @return 组合数C(n, k)的结果
     * @throws IllegalArgumentException k小于零，k大于n，n和k超出规定范围。
     */
    public static long nchoosek(int n, int k){
        return Nchoosek.nchoosek(n, k);
    }
    /**
     * 从元素数组中取出m个元素的组合排列(含重复元素)
     * @param array 元素所处数组
     * @param k 需要取出元素的数量
     * @return 数组中取出m个元素的组合排列结果
     * @throws IllegalArgumentException k小于零，k大于数组长度，数组长度和k超出规定范围。
     */
    public static List<int[]> nchoosek(int[] array, int k){
        return Nchoosek.nchoosek(array, k);
    }
    /**
     * 利用字典法获取指定数组的全排列序列
     * @param array 全排列序列元素数组
     * @return 全排列结果
     * @throws NullPointerException 当元素数组为null时
     */
    public static List<int[]> perms(int[] array){
        return Perm.perms1(array);
    }
    /**
     * 打印指定数值的二进制字符串
     * @param n 待转换为二进制字符串的数值
     * @param gap 需要被间隔的长度
     * @param gapLen 空格的间隔数
     * @return 数值的二进制字符串
     */
    public static String toBinaryString(int n, int gap, int gapLen){
        if((gap | gapLen) < 0){
            throw new IllegalArgumentException("gap = " + gap + ", num = " + gapLen);
        }
        int gapNum = gap == 0 ? 0 : (32 % gap) == 0 ? 32 / gap - 1 : 32 / gap;
        int newLen = gapNum * gapLen + 32;
        char[] newBinary = new char[newLen];
        int binaryLen = 32 - Integer.numberOfLeadingZeros(n);
        char[] binary32 = new char[32];
        Arrays.fill(binary32, 0, 32 - binaryLen, '0');
        int index = 0;
        while(index < binaryLen){
            binary32[31 - index++] = (n & 1) == 1 ? '1' : '0';
            n >>>= 1;
        }
        int originalLen = binary32.length;
        Arrays.fill(binary32, 0, 32 - originalLen, '0');
        System.arraycopy(binary32, 0, binary32, 32 - originalLen, originalLen);
        index = newLen;
        for(int i = 0; i < 32; i++){
            if(gap > 0 && i % gap == 0 && i > 0){
                int temp = gapLen;
                while(--temp >= 0){
                    newBinary[--index] = ' ';
                }
            }
            newBinary[--index] = binary32[31 - i];
        }
        return String.valueOf(newBinary);
    }
    /**
     * 获取两个数据的最大公约数（辗转相除法）
     * @param a 一个参数
     * @param b 另一个参数
     * @return 最大公约数值
     * @throws IllegalArgumentException a或b为非正数.
     */
    public static int greatestCommonDivisor(int a, int b){
        return GreatestCommonDivisor.greatestCommonDivisor(a, b);
    }
    /**
     * 获取两数据的最小公倍数(数据之积等于最大公约数和最小公倍数之积)
     * @param a 一个参数
     * @param b 另一个参数
     * @return ab两数据的最小公倍数.
     * @throws IllegalArgumentException a或者b为非正数.
     */
    public static int leastCommonMultiple(int a, int b){
        return LeastCommonMultiple.leastCommonMultiple(a, b);
    }
    /**
     * 将字符串进行MD5加密
     * @param encode 需要被加密的字符串
     * @return MD5字符串
     * @throws NullPointerException 如果被加密字符串为空。
     */
    public static String toMD5String(String encode){
        if(encode == null){
            throw new NullPointerException("encode");
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(encode.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] digest = md5.digest();
        int len = digest.length;
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < len; ++i){
            int value = digest[i] & 0xFF;
            if(value < 16){
                result.append('0');
            }
            result.append(Integer.toHexString(value).toUpperCase());
        }
        return result.toString();
    }

}

/**
 * 素数工具类
 * @author 江峰
 * @create 2018-11-24
 */
@SuppressWarnings("unused")
final class Prime{
    private Prime(){throw new IllegalAccessError();}
    /**
     * 获取指定范围的素数，该方法效率较低，仅供学习使用。
     *
     * 该方法通过遍历2到n，若数值为素数则保存在素数数组中。
     * 因为素数数量无法确定，因此采用最为保守的方式，即数值的一半作为该素数数组长度。
     *
     * @param n 指定素数范围的最大值
     * @return 指定范围内素数数组
     * @throws IllegalArgumentException 如果n值小于0
     */
    private static int[] primes0(int n){
        if(n < 0){
            throw new IllegalArgumentException("N must be a non negative integer.");
        }
        if(n <= 1){
            return new int[0];
        }
        int primeNums = (n & 1) == 1 ? (n >>> 1) + 1 : n >>> 1;
        int[] primes = new int[primeNums];
        int primeCount = 0;
        outer:
        for(int i = 2; i <= n; i++){
            for(int j = 2, limit = (int) Math.sqrt(i); j <= limit; j++){
                if(i % j == 0){
                    continue outer;
                }
            }
            primes[primeCount++] = i;
        }
        return Arrays.copyOf(primes , primeCount );
    }
    /**
     * 获取指定范围的素数。
     *
     * 该方法使用筛选法获取指定范围素数。
     * 首先初始化n+1(为了让下标和数值对应)长度、boolean类型的prime数组，用于存储是否为素数的信息。
     * 然后将奇数全部设置为true，即认定奇数为伪素数。
     * 接着在3:2:sqrt(n)范围内，选定素数，并将所有素数的倍数置false，即认定其为非素数。
     * 最后遍历prime数组，将数值为true的下标值认定为素数，并将偶数2也添加入素数数组。
     *
     * n等于0或者1时，返回长度为零的数组。
     *
     * @param n 指定素数范围的最大值
     * @return 指定范围内素数数组
     * @throws IllegalArgumentException n值小于0
     * @throws OutOfMemoryError 当n值过大时，开辟的数组长度可能超出虚拟机限制。
     */
    private static int[] primes1(int n){
        if(n < 0){
            throw new IllegalArgumentException("N must be a non negative integer.");
        }
        if(n <= 1){
            return new int[0];
        }
        boolean[] primes = new boolean[n + 1]; // 加一实现下标与真实数值对应，boolean默认为false
        /* 将下标为奇数的置为true，下标为偶数的默认为false。*/
        for(int i = 1; i <= n; i++){
            if((i & 1) == 1){
                primes[i] = true;
            }
        }
        for(int k = 3, limit = (int) Math.sqrt(n); k <= limit; k += 2){
            /*将素数倍数下标处的值全部置false*/
            if(isPrime(k)){
                for(int i = k * k; i <= n; i += k){
                    primes[i] = false;
                }
            }
        }
        int primeNums = 0;
        /*获取精确的素数数量，以免开辟过大的数组造成空间不足的情况。*/
        for(boolean isPrime : primes){
            if(isPrime){
                primeNums++;
            }
        }
        int[] primeArray = new int[primeNums];
        primeArray[0] = 2;
        int count = 1;
        for(int i = 3; i <= n; i++){
            if(primes[i]){
                primeArray[count++] = i;
            }
        }
        return primeArray;
    }
    /**
     * 获取指定范围的素数。
     *
     * 该方法使用筛选法获取指定范围素数。
     * 首先初始化n / 2长度、boolean类型的prime数组，将其下标认定为奇数，数组用于存储是否为素数的信息。
     * 然后在3:2:sqrt(n)范围内，判断真实下标值是否已经被筛选，若没有，则将其奇数倍数置true，即认定其为非素数。
     * 最后遍历prime数组，将数值为false的下标值认定为素数，并通过2i - 1进行转换，并将偶数2也添加入素数数组。
     *
     * 当n=0或1时，返回长度为零的数组。
     *
     * @param n 指定素数范围的最大值
     * @return 指定范围内素数数组
     * @throws IllegalArgumentException n值小于0
     */
    public static int[] primes(int n){
        if(n < 0){
            throw new IllegalArgumentException("N must be a non negative integer.");
        }
        if(n <= 1){
            return new int[0];
        }
        int len = ((n & 1) == 1) ? (n >> 1) + 1 : n >> 1;
        boolean[] p = new boolean[len + 1];
        for(int k = 3, limit = (int)Math.sqrt(n); k <= limit; k += 2){
            if(!p[(k + 1) >> 1]){
                for(int j = (k * k + 1) >> 1 ; j <= len; j += k){
                    p[j] = true;
                }
            }
        }
        int primeNums = 0;
        /*获取精确的素数数量，以免开辟过大的数组造成空间不足的情况。*/
        for(int i = 1; i <= len; i++){
            if(!p[i]){
                primeNums++;
            }
        }
        int[] primeArray = new int[primeNums];
        primeArray[0] = 2;
        int count = 1;
        for(int i = 2; i <= len; i++){
            if(!p[i]){
                primeArray[count++] = (i << 1) - 1;
            }
        }
        return primeArray;
    }
    /**
     * 判断数值是否是素数。该算法主要用于学习，实用算法参见{@link #isPrime(int)}
     *
     * 搜索范围为2到sqrt(n)，步长为1。
     *
     * 输入的数值必须为非负整数，否则将抛出IllegalArgumentException。
     *
     * @param n 待检测是否是素数的整数值
     * @return 如果该数值为素数，返回true，否则返回false。
     * @throws IllegalArgumentException n值小于0
     */
    private static boolean isPrime0(int n){
        if(n < 0){
            throw new IllegalArgumentException("N must be a non negative integer.");
        }
        if(n <= 1){
            return false;
        }
        for(int i = 2, limit = (int)Math.sqrt(n); i <= limit; i++){
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }
    /**
     * 判断数值是否是素数
     *
     * 提取小于等于sqrt(n)的所有素数，然后逐个判断n是否能整除这些素数，能则为非素数，全部不能，则为素数。
     *
     * 参考与Matlab2014a版本的isprime函数。
     *
     * @param n 待检测是否是素数的整数值
     * @return 如果该数值为素数，返回true，否则返回false。
     * @throws IllegalArgumentException n值小于0
     */
    private static boolean isPrime1(int n){
        if(n < 0){
            throw new IllegalArgumentException("N must be a non negative integer.");
        }
        if(n <= 1){
            return false;
        }
        for(int prime : primes((int) Math.sqrt(n))){
            if(n % prime == 0){
                return false;
            }
        }
        return true;
    }
    /**
     * 判断数值是否是素数。
     *
     * 采用孪生素数知识，抛开2和3，素数只能为6x - 1 和 6x + 1。
     * 搜索范围为5到sqrt(n)，步长从原先的1，变成6，效率得到极大提高。
     *
     * 输入的数值必须为非负整数，否则将抛出IllegalArgumentException。
     *
     * @param n 待检测是否是素数的整数值
     * @return 如果该数值为素数，返回true，否则返回false。
     * @throws IllegalArgumentException n值小于0
     */
    public static boolean isPrime(int n){
        if(n < 0){
            throw new IllegalArgumentException("N must be a non negative integer.");
        }
        if(n <= 1){
            return false;
        }
        if(n == 2 || n ==3){
            return true;
        }
        int remainder = n % 6;
        /*不在6的倍数两侧的一定不是质数*/
        if(remainder != 1 && remainder != 5){
            return false;
        }
        for(int i = 5, limit = (int) Math.sqrt(n); i <= limit; i += 6){
            if(n % i == 0 || n % (i + 2) == 0){  /*在6的倍数两侧的也可能不是质数*/
                return false;
            }
        }
        return true;
    }
    /**
     * 分解质因数
     *
     * @param n 待分解值
     * @return 质因数数组
     * @throws IllegalArgumentException 如果n值大于零
     */
    public static int[] factor(int n){
        if(n < 0){
            throw new IllegalArgumentException("N must be a non negative integer.");
        }
        if(n < 4){
            return new int[]{n};
        }
        int factorNums = (int)(Math.log(Integer.highestOneBit(n)) / Math.log(2));
        int[] factors = new int[factorNums];
        int factorCount = 0;
        for(int i = 2; i <= (int) Math.sqrt(n); i++){
            if(n % i == 0){
                factors[factorCount++] = i;
                n /= i;
                i = 1;
            }
        }
        factors[factorCount++] = n;
        return Arrays.copyOf(factors, factorCount);
    }
    /**
     * 分解质因数
     *
     * 详情参照Matlab2014a版本的factor函数
     *
     * @param n 待分解值
     * @return 质因数数组
     * @throws IllegalArgumentException 如果n值大于零
     */
    private static int[] factor0(int n){
        if(n < 0){
            throw new IllegalArgumentException("N must be a non negative integer.");
        }
        if(n < 4){
            return new int[]{n};
        }
        int[] factors = new int[(int)(Math.log(Integer.highestOneBit(n)) / Math.log(2))];
        int factorCount = 0;
        int[] primes = primes((int) Math.sqrt(n));
        while(true){
            int primeProd = 1;
            for(int prime : primes){
                if(n % prime == 0){
                    factors[factorCount++] = prime;
                    primeProd *= prime;
                }
            }
            if(primeProd == 1){ // Now, n is a prime number.
                factors[factorCount++] = n;
                break;
            }
            if((n = n / primeProd) <= 1){
                break;
            }
        }
        factors = Arrays.copyOf(factors, factorCount);
        Arrays.sort(factors); // These prime numbers in array are in disorder.
        return factors;
    }
}

/**
 * 全组合工具类
 * @author 江峰
 * @create 2018-11-24
 */
@SuppressWarnings("unused")
final class Nchoosek{
    private Nchoosek(){throw new IllegalAccessError();}
    // 逐个相除，错误版本，让自己不要忽略浮点型转换为整型时存在的精度问题
    private static int nchoosek0(int n, int k){
        checknk(n, k);
        if(k <= 1){  // C(n, 0) = 1,  C(n, 1) = n
            return k == 0 ? 1 : n;
        }
        k = (k > (n - k)) ? n - k : k;  // C(n, k) = C(n, n - k)
        int divisor = n - k + 1;
        int dividend = 1;
        double cnk = 1.0;
        for(int i = 0; i < k; i++){ // double c(13, 6)， float c(25, 9)
            cnk *= (double) (divisor + i) / (dividend + i);
        }
        return (int) cnk;
    }
    // 逐个整除，除不尽的分解成质因数再整除
    private static long nchoosek1(int n, int k){
        if(n > 70 || (n == 70 && k > 25 && k < 45)){
            throw new IllegalArgumentException("N(" + n + ") and k(" + k + ") don't meet the requirements.");
        }
        checknk(n, k);
        k = k > (n - k) ? n - k : k;
        if(k <= 1){  // C(n, 0) = 1,  C(n, 1) = n
            return k == 0 ? 1 : n;
        }
        int[] divisors = new int[k]; // n - k + 1 : n
        int firstDivisor = n - k + 1;
        for(int i = 0; i < k; i++){
            divisors[i] = firstDivisor + i;
        }
        outer:
        for(int dividend = 2; dividend <= k; dividend++){
            for(int i = k - 1; i >= 0; i--){
                int divisor = divisors[i];
                if(divisor % dividend == 0){
                    divisors[i] = divisor / dividend;
                    continue outer;
                }
            }
            int[] perms = Prime.factor(dividend);
            for(int perm : perms){
                for(int j = 0; j < k; j++){
                    int divisor = divisors[j];
                    if(divisor % perm == 0){
                        divisors[j] = divisor / perm;
                        break;
                    }
                }
            }
        }
        long cnk = 1L;
        for(int i = 0; i < k; i++){
            cnk *= divisors[i];
        }
        return cnk;
    }
    // 将A(n,k)和A(k, k)转换为素数幂乘积形式，两者相除就是幂数相减，最后计算素数幂乘积即可。
    private static long nchoosek2(int n, int k){
        if(n > 70 || (n == 70 && k > 25 && k < 45)){
            throw new IllegalArgumentException("N(" + n + ") and k(" + k + ") don't meet the requirements.");
        }
        checknk(n, k);
        k = k > (n - k) ? n - k : k;
        if(k <= 1){  // C(n, 0) = 1,  C(n, 1) = n
            return k == 0 ? 1 : n;
        }
        HashMap<Integer, Integer> primeMap = new HashMap<>();
        for(int i = n - k + 1; i <= n; i++){
            for(int prime : Prime.factor(i)){
                Integer primeCount = primeMap.get(prime);
                primeMap.put(prime, primeCount == null ? 1 : primeCount + 1);
            }
        }
        for(int i = 2; i <= k; i++){
            for(int prime : Prime.factor(i)){
                primeMap.put(prime, primeMap.get(prime) - 1);
            }
        }
        long cnk = 1L;
        for(Map.Entry<Integer, Integer> entry : primeMap.entrySet()){
            int coef = entry.getKey();
            int exp = entry.getValue();
            if(exp > 0){
                cnk *= (long) Math.pow(coef, exp);
            }
        }
        return cnk;
    }
    // 直接按照公式，先计算A(n, k)和A(k, k)，再两者相除。
    private static int nchoosek3(int n, int k){
        if(n > 16 || (n == 16 && k == 8)){
            throw new IllegalArgumentException("N(" + n + ") and k(" + k + ") don't meet the requirements.");
        }
        checknk(n, k);
        k = (k > (n - k)) ? n - k : k;  // C(n, k) = C(n, n - k)
        if(k <= 1){  // C(n, 0) = 1,  C(n, 1) = n
            return k == 0 ? 1 : n;
        }
        int divisor = 1;
        for(int i = n - k + 1; i <= n; i++){
            divisor *= i;
        }
        int dividend = 1;
        for(int i = 2; i <= k; i++){
            dividend *= i;
        }
        return (int) ((double) divisor / dividend);
    }
    // 优化A(n, k)和A(k, k)的计算
    private static long nchoosek4(int n, int k){
        if(n > 30 || (n == 30 && k > 13 && k < 17)){
            throw new IllegalArgumentException("N(" + n + ") and k(" + k + ") don't meet the requirements.");
        }
        checknk(n, k);
        k = (k > (n - k)) ? n - k : k;  // C(n, k) = C(n, n - k)
        if(k <= 1){  // C(n, 0) = 1,  C(n, 1) = n
            return k == 0 ? 1 : n;
        }
        return MathUtils.prod(n - k + 1, n).longValue() / MathUtils.prod(2, k).longValue();
    }
    // 简单按照C(n, k) = C(n - 1, k - 1) + C(n - 1, k)进行递归操作。
    private static int nchoosek5(int n, int k){
        if(n > 34 || (n == 34 && k >= 16 && k <= 18)){
            throw new IllegalArgumentException("N(" + n + ") and k(" + k + ") don't meet the requirements.");
        }
        checknk(n, k);
        if(k <= 1){  // C(n, 0) = 1,  C(n, 1) = n
            return k == 0 ? 1 : n;
        }
        if(k > (n >>> 1)){
            return nchoosek5(n, n - k);
        }
        return nchoosek5(n - 1, k - 1) + nchoosek5(n - 1, k);
    }
    // 将C(n, k) = C(n - 1, k - 1) + C(n - 1, k)转换为杨辉三角缓存操作
    private static int nchoosek6(int n, int k){
        if(n > 34 || (n == 34 && k >= 16 && k <= 18)){
            throw new IllegalArgumentException("N(" + n + ") and k(" + k + ") don't meet the requirements.");
        }
        checknk(n, k);
        k = (k > (n - k)) ? n - k : k;
        if(k <= 1){  // C(n, 0) = 1,  C(n, 1) = n
            return k == 0 ? 1 : n;
        }
        int[][] yhTriangle = new int[n + 1][n + 1];
        for(int i = 0; i <= n; i++){
            yhTriangle[i][0] = 1;
            yhTriangle[i][i] = 1;
        }
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= i; j++){
                yhTriangle[i][j] = yhTriangle[i - 1][j - 1] + yhTriangle[i - 1][j];
            }
        }
        return yhTriangle[n][k];
    }
    // 利用双数组缓存优化杨辉三角
    public static long nchoosek(int n, int k){
        if(n > 70 || (n == 70 && k > 25 && k < 45)){
            throw new IllegalArgumentException("N(" + n + ") and k(" + k + ") don't meet the requirements.");
        }
        checknk(n, k);
        k = (k > (n - k)) ? n - k : k;
        if(k <= 1){  // C(n, 0) = 1,  C(n, 1) = n
            return k == 0 ? 1 : n;
        }
        int cacheLen = k + 1;
        long[] befores = new long[cacheLen];
        befores[0] = 1;
        long[] afters = new long[cacheLen];
        afters[0] = 1;
        for(int i = 1; i <= n; i++){
            for(int j = 1; j <= k; j++){
                afters[j] = befores[j - 1] + befores[j];
            }
            long[] temp = befores;
            befores = afters;
            afters = temp;
        }
        return befores[k];
    }
    // 利用二进制性质求C(n, k)
    private static int nchoosek7(int n, int k){
        if(n > 31){
            throw new IllegalArgumentException("N must be less than or equal to 31");
        }
        checknk(n, k);
        k = (k > (n - k)) ? n - k : k;
        if(k <= 1){  // C(n, 0) = 1,  C(n, 1) = n
            return k == 0 ? 1 : n;
        }
        int limit = Integer.MAX_VALUE >> (31 - n);
        int cnk = 0;
        for(int i = 3; i < limit; i++){
            if(Integer.bitCount(i) == k){
                cnk++;
            }
        }
        return cnk;
    }
    // 利用二进制性质求数组的组合排列
    private static List<int[]> nchoosek0(int[] array, int k){
        int n = array.length;
        if(n > 31){
            throw new IllegalArgumentException("N must be less than or equal to 31");
        }
        checknk(n, k);
        if(k == 0){
            return new ArrayList<>();
        }
        if(n == k){
            List<int[]> combList = new ArrayList<>(1);
            combList.add(java.util.Arrays.copyOf(array, k));
            return combList;
        }
        int combNum = (int) nchoosek(n, (k > (n - k)) ? n - k : k);
        int bits = Integer.MAX_VALUE >> (31 - n);
        List<int[]> combList = new ArrayList<>(combNum);
        for(int i = 0; i < bits; i++){
            if(Integer.bitCount(i) != k){
                continue;
            }
            int[] comb = new int[k];
            int index = 0;
            int iTemp = i;
            while(iTemp != 0){
                int lowest = iTemp & -iTemp;
                comb[index++] = array[(int)(Math.log(lowest) / Math.log(2))];
                iTemp &= iTemp - 1;
            }
            /**
             * for(int j = 0; j < n; j++){
             if((i & (1 << j)) >= 1){
             comb[index++] = array[j];
             }
             }
             for(int j = 0; j < n; j++){
             if(i << (31 - j)) >>> 31 == 1){
             comb[index++] = array[j];
             }
             }

             */
            combList.add(comb);
        }
        return combList;
    }
    // 简单递归法(无重复)
    private static List<int[]> nchoosek1(int[] array, int k){
        int n = array.length;
        checknk(n, k);
        List<int[]> combList = new ArrayList<>((int)nchoosek(n, k > (n - k) ? n - k : k));
        nchoosek1(array, n, new int[k], k, 0, 0, combList);
        return combList;
    }
    private static void nchoosek1(int[] array, int n, int[] comb, int k,
                                  int aIndex, int cIndex, List<int[]> combList){
        if(cIndex == k){
            combList.add(Arrays.copyOf(comb, k));
            return;
        }
        for(int i = aIndex, limit = n - (k - cIndex); i <= limit; i++){
            comb[cIndex++] = array[i];
            nchoosek1(array, n, comb, k, i + 1, cIndex, combList);
            cIndex--;
        }
    }
    // 简单递归法(含重复)
    public static List<int[]> nchoosek(int[] array, int k){
        int n = array.length;
        int size = 0;
        if(n > 70){
            size = nchoosek0(n, k);
        }else {
            size = (int)nchoosek(n, k);
        }
        List<int[]> combList = new ArrayList<>(size);
        nchoosek11(array, n, new int[k], k, 0, 0, combList);
        return combList;
    }
    private static void nchoosek11(int[] array, int n, int[] comb, int k,
                                   int aIndex, int cIndex, List<int[]> combList){
        if(cIndex == k){
            combList.add(Arrays.copyOf(comb, k));
            return;
        }
        for(int i = aIndex, limit = n - (k - cIndex); i <= limit; i++){
            comb[cIndex++] = array[i];
            nchoosek11(array, n, comb, k, i + 1, cIndex, combList);
            cIndex--;
            int value = array[i];
            int index = i;
            while(++index <= limit && array[index] == value);
            i = index - 1;
        }
    }
    // 反向递归（Matlab）
    private static int[][] nchoosek2(int[] array, int k){
        int n = array.length;
        checknk(n, k);
        if(k == 0){
            return new int[1][0];
        }
        return nchoosek2(array, n, k);
    }
    private static int[][] nchoosek2(int[] array, int n, int k){
        int[][] comb = null;
        if(n == k){
            comb = new int[1][n];
            for(int i = 0; i < n; i++){
                comb[0][i] = array[i];
            }
            return comb;
        }
        if(k == 1){
            comb = new int[n][1];
            for(int i = 0; i < n; i++){
                comb[i][0] = array[i];
            }
            return comb;
        }
        for(int i = 0, limit = n - k + 1; i < limit; i++){
            int[][] next = nchoosek2(Arrays.copyOfRange(array, i + 1, n), n - i - 1, k - 1); // Get all possible values for the next one
            int combRowLen = comb == null ? 0 : comb.length;
            int totalRowLen = next.length + combRowLen;
            int totalColLen = next[0].length + 1;
            int[][] tempComb = new int[totalRowLen][totalColLen];
            if(comb != null){ // TempComb capacity expansion comb
                for(int j = 0; j < combRowLen; j++){
                    tempComb[j] = Arrays.copyOf(comb[j], totalColLen);
                }
            }
            int value = array[i];
            for(int row = combRowLen; row < totalRowLen; row++){
                tempComb[row][0] = value; // The value completes the current one
                for(int col = 1; col < totalColLen; col++){ // Copy the next one.
                    tempComb[row][col] = next[row - combRowLen][col - 1];
                }
            }
            comb = tempComb;
        }
        return comb;
    }
    // 利用缓存优化反向递归法(Matlab优化版)
    private static int[][] nchoosek3(int[] array, int k){
        int n = array.length;
        checknk(n, k);
        if(k == 0){
            return new int[1][0];
        }
        int combNum = (int) nchoosek(n, k > (n - k) ? n - k : k);
        int[][] comb = new int[combNum][k];
        int rowEndIndex = n - k + 1;
        for(int i = 0, k1 = k - 1; i < rowEndIndex; i++){ // Fill the right-most side.
            comb[i][k1] = array[k1 + i];
        }
        for(int begin = k - 2; begin >= 0; begin--){
            int rowLen = rowEndIndex;
            int previousRowEndIndex = rowEndIndex;
            for(int i = 0; i < rowEndIndex; i++){
                comb[i][begin] = array[begin];
            }
            for(int next = begin + 1, limit = begin + n - k; next <= limit; next++){
                int selectionNum = n - k + 1 + begin - next;
                int allPossibleNum = n - next;
                rowLen = rowLen * selectionNum / allPossibleNum;
                int rowBeginIndex = rowEndIndex;
                rowEndIndex = rowBeginIndex + rowLen;
                int nextVal = array[next];
                for(int i = rowBeginIndex; i < rowEndIndex; i++){
                    comb[i][begin] = nextVal;
                    for(int j = begin + 1; j < k; j++){
                        comb[i][j] = comb[previousRowEndIndex - rowLen + i - rowBeginIndex][j];
                    }
                }
            }
        }
        return comb;
    }
    // n、k参数检测
    private static void checknk(int n, int k){
        if(k < 0 || k > n){
            throw new IllegalArgumentException("K must be an integer between 0 and N.");
        }
    }
    // 组合数计算的测试代码
    private static void testNchoosekNum(){
        outer:
        for(int n = 1; n <= 30; n++){
            for(int k = 1; k <= n; k++){
                long test = nchoosek2(n, k);
                long verify = nchoosek(n, k);
                if(test != verify){
                    System.out.println("n = " + n + ", k = " + k + ", test = " + test + ", verify = " + verify);
                    break outer;
                }
            }
        }
    }
    // 数组组合排列测试代码
    private static void testNckooseSequence(){
        int n = 7;
        int k = 7;
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = i + 1;
        }
        int[][] combs = Nchoosek.nchoosek3(a, k);
        for(int[] array : combs){
            System.out.println(Arrays.toString(array));
        }
        System.out.println("组合数量：" + combs.length);
        System.out.println("结果是否正确：" + (combs.length == Nchoosek.nchoosek(n, k)));
    }
}

/**
 * 全排列工具类
 * @author 江峰
 * @create 2018-11-24
 */
final class Perm{
    /**
     * 使用迭代法实现数组的全排列，该方法是主函数。
     * @param array 待全排列的数组对象
     * @param isRepeat 判断是否剔除重复的全排列数组
     * @return 全排列结果的列表对象
     */
    public static List<int[]> perms0(int[] array, boolean isRepeat){
        int len = 0;
        if(array == null || (len = array.length) == 0){
            return null;
        }
        List<int[]> permList = new ArrayList<>(len);
        permsr(array, 0, len - 1, permList, isRepeat);
        return permList;
    }
    /**
     * 使用迭代法实现数组的全排列，该方法是迭代函数。
     * @param array 待全排列的数组对象
     * @param begin 全排列开始的索引值
     * @param end 全排列结束的索引值
     * @param permList 存储全排列结果的列表对象
     * @param repeat 判断是否剔除重复的全排列数组
     */
    private static void permsr(int[] array, int begin, int end, List<int[]> permList, boolean repeat){
        if(begin == end){
            int[] perms = Arrays.copyOf(array, array.length);
            permList.add(perms);
            return;
        }
        outer:
        for(int i = begin; i <= end; i++){
            if(repeat){
                int target = array[i];
                for(int j = begin; j < i; j++){
                    if(array[j] == target){
                        continue outer;
                    }
                }
            }
            ArrayUtils.swap(array, begin, i);
            permsr(array, begin + 1, end, permList, repeat);
            ArrayUtils.swap(array, begin, i);
        }
    }
    /**
     * 利用字典排序法实现全排列
     *
     * 使用字典排序实现，无需迭代。
     * 存在相同元素的情况，也不会出现重复结果。
     *
     * @param array 待排数组
     * @return 所有全排列结果所组成的数组列队
     */
    public static List<int[]> perms1(int[] array){
        int len = array.length;
        if(len == 0){
            return new ArrayList<int[]>(0);
        }
        List<int[]> permList = new ArrayList<int[]>(len);
        Arrays.sort(array);
        int remaining = len - 1;
        while(true){
            permList.add(Arrays.copyOf(array, len));
            int replace = remaining;
            while(replace >= 1 && array[replace] <= array[--replace]);
            if(array[replace] >= array[replace + 1]){
                break;
            }
            int replaced = replace;
            while(++replaced < len && array[replaced] > array[replace]);
            ArrayUtils.swap(array, replace++, --replaced);
            ArrayUtils.reverse(array, replace, remaining);
        }
        return permList;
    }
    /**
     * 利用矩阵缓存来实现无重复的全排列，具体参见Matlab的v_perms2的无递归版本。
     * @param array 全排列元素数组
     * @return 所有全排列组合序列
     */
    public static int[][] perms2(int[] array){
        int n = array.length; // Name the length of array as n. the algorithm is more readable this way.
        int[][] perms = {{array[0]}};
        for(int i = 0, limit = n - 1; i < limit; i++){
            int next = i + 1;
            int nextVal = array[next];
            int[][] previous = perms;
            int preRows = previous.length;
            int preCols = previous[0].length;
            int permsCols = next + 1;
            perms = new int[permsCols * preRows][permsCols]; // p = zeros(next * m, next);
            for(int row = 0; row < preRows; row++){ // p(1 : m, :) = [(k + 1) * ones(m, 1) q];
                perms[row][0] = nextVal;
                System.arraycopy(previous[row], 0, perms[row], 1, preCols);
            }
            for(int j = i; j >= 0; j--){
                int currentVal = array[j];
                int[][] temp = new int[preRows][preCols];
                for(int row= 0; row < preRows; row++){ // t = q;
                    System.arraycopy(previous[row], 0, temp[row], 0, preCols);
                }
                for(int row = 0; row < preRows; row++){ // t(t == j) = nextVal;
                    for(int col = 0; col < preCols; col++){
                        if(temp[row][col] == currentVal){
                            temp[row][col] = nextVal;
                            break;
                        }
                    }
                }
                /* p((i + 1 - j) * m + 1 : (next - j + 1) * m, :) = [j * ones(m, 1) t] */
                for(int startRow = (next - j) * preRows, row = startRow; row < (next - j + 1) * preRows; row++){
                    perms[row][0] = currentVal;
                    System.arraycopy(temp[row - startRow], 0, perms[row], 1, preCols);
                }
            }
        }
        return perms;
    }
    /**
     * 利用矩阵缓存来实现无重复全排列，它初始化了排列空间。具体参见Matlab的v_perms1的无递归版本。
     * @param array 全排列元素数组
     * @return 所有全排列组合序列
     */
    public static int[][] perms3(int[] array){
        int n = array.length; // Name the length of array as n. the algorithm is more readable this way.
        int[][] perms = new int[(int) MathUtils.factorial(n)][n]; // p = zeros(n!, n);
        perms[0][n - 1] = array[0];
        int rowEndIndex = 1;
        for(int i = 0, limit = n - 1; i < limit; i++){
            int next = i + 1;
            int nextVal = array[next];
            int[][] previous = new int[rowEndIndex][next];
            for(int row = 0; row < rowEndIndex; row++){ // q = p(1 : end, n - i + 1 : n);
                System.arraycopy(perms[row], n - i - 1, previous[row], 0, next);
            }
            int preRows = previous.length;
            int preCols = previous[0].length;
            int colBeginIndex = n - i - 2;
            for(int row = 0; row < preRows; row++){ // p(1 : m, n - i : n) = [(k + 1) * ones(m, 1) q];
                perms[row][colBeginIndex] = nextVal;
                System.arraycopy(previous[row], 0, perms[row], colBeginIndex + 1, preCols);
            }
            for(int j = i; j >= 0; j--){
                int currentVal = array[j];
                int[][] temp = new int[preRows][preCols];
                for(int row= 0; row < preRows; row++){ // t = q;
                    System.arraycopy(previous[row], 0, temp[row], 0, preCols);
                }
                for(int row = 0; row < preRows; row++){ // t(t == j) = nextVal;
                    for(int col = 0; col < preCols; col++){
                        if(temp[row][col] == currentVal){
                            temp[row][col] = nextVal;
                            break;
                        }
                    }
                }
                /* p((i + 1 - j) * m + 1 : (next - j + 1) * m, n - k : n) = [j * ones(m, 1) t] */
                for(int startRow = (next - j) * preRows, row = startRow; row < (next - j + 1) * preRows; row++){
                    perms[row][colBeginIndex] = currentVal;
                    System.arraycopy(temp[row - startRow], 0, perms[row], colBeginIndex + 1, preCols);
                }
            }
            rowEndIndex = (next + 1) * preRows;
        }
        return perms;
    }
    /**
     * 利用矩阵缓存来实现无重复全排列，从前向后操作，具体参见Matlab的v_perms0的无递归版本。
     * @param array 全排列元素数组
     * @return 所有全排列组合序列
     */
    public static int[][] perms4(int[] array){
        int n = array.length; // Name the length of array as n. the algorithm is more readable this way.
        int[][] perms = {{0}};
        int permRowEndIndex = 1;
        for(int i = 1; i < n; i++){
            int next = i + 1;
            int preRows = next * permRowEndIndex;
            int preCols = next;
            int[][] previous = new int[preRows][preCols]; // q = zeros(next * rowEnd, next);
            int[] replace = new int[next];
            for(int index = 0; index < next; index++){ // r = 1 : i + 1;
                replace[index] = index + 1;
            }
            int begin = 0;
            int end = permRowEndIndex;
            for(int j = 0; j <= i; j++){
                for(int row = begin; row < end; row++){ // q(begin:end, 1:i) = [j * ones(rowEnd,1), r(p)];
                    previous[row][0] = j;
                    for(int col = 1; col <= i; col++){
                        previous[row][col] = replace[perms[row - begin][col - 1]];
                    }
                }
                replace[j] = j; // r(j) = j;
                begin += permRowEndIndex; // begin = begin + rowEnd;
                end += permRowEndIndex;   // end = end + rowEnd;
            }
            permRowEndIndex *= next; // rowEnd = rowEnd * next;
            perms = previous; // p = q;
        }
        for(int row = 0; row < permRowEndIndex; row++){ // p = array(p);
            for(int col = 0; col < n; col++){
                perms[row][col] = array[perms[row][col]];
            }
        }
        return perms;
    }
}

/**
 * 整数划分工具类
 * @author 江峰
 * @create 2018-11-24
 */
final class PartitionInteger{
    /**
     *             1                          (m = 1 或  n = 1)
     * f(n, m) =   f(n, n)                    (m > n)
     *             f(n, n - 1) + 1            (m = n)
     *             f(n, m - 1) + f(n - m, m)  (n > m)
     */
    private PartitionInteger(){throw new IllegalAccessError();}
    // 递推公式获取n的所有划分数
    public static int getPartitionNumber0(int n){
        if(n <= 0){
            throw new IllegalArgumentException("N must be a positive number.");
        }
        return getPartitionNumber_0(n, n);
    }
    // 递推公式获取划分长度为m的n的所有划分数
    public static int getPartitionNumber0(int n, int m){
        chechmn(m, n);
        if(m == 0){
            return 0;
        }
        if(m == 1){
            return getPartitionNumber_0(n, m);
        }
        return getPartitionNumber_0(n, m) - getPartitionNumber_0(n, m - 1);
    }
    private static int getPartitionNumber_0(int n, int m){
        if(m == 1 || n == 1){
            return 1;
        }
        if(m > n){
            return getPartitionNumber_0(n, n);
        }
        if(m == n){
            return 1 + getPartitionNumber_0(n, n - 1);
        }
        return getPartitionNumber_0(n, m - 1) + getPartitionNumber_0(n - m, m);
    }
    // 缓存递推信息获取n的所有划分数
    public static int getPartitionNumber1(int n){
        if(n <= 0){
            throw new IllegalArgumentException("N must be a positive number.");
        }
        return getPartitionCache_1(n)[n - 1][n - 1];
    }
    // 缓存递推信息获取划分长度为m的n的所有划分数
    public static int getPartitionNumber1(int n, int m){
        chechmn(m, n);
        if(m == 0){
            return 0;
        }
        int[][] cache = getPartitionCache_1(n);
        if(m == 1){
            return cache[n - 1][0];
        }
        return cache[n - 1][m - 1] - cache[n - 1][m - 2];
    }
    public static int[][] getPartitionCache_1(int n){
        int[][] cache = new int[n][n];
        for(int i = 0; i < n; i++){
            cache[i][0] = 1;
        }
        for(int i = 1; i < n; i++){
            for(int j = 1; j <= i; j++){
                if(i == j){
                    cache[i][j] = 1 + cache[i][j - 1];
                }else if(i - j - 1 < j){
                    cache[i][j] = cache[i - j - 1][i - j - 1] + cache[i][j - 1];
                }else {
                    cache[i][j] = cache[i][j - 1] + cache[i - j - 1][j];
                }
            }
        }
        return cache;
    }
    // 稀疏矩阵获取n的所有划分数
    public static int getPartitionNumber2(int n){
        if(n <= 0){
            throw new IllegalArgumentException("N must be a positive number.");
        }
        return getSparseMatrix(n).get(n, n);
    }
    // 缓存递推信息获取划分长度为m的n的所有划分数
    public static int getPartitionNumber2(int n, int m){
        chechmn(m, n);
        if(m == 0){
            return 0;
        }
        if(m == 1){
            return getSparseMatrix(n).get(n, 1);
        }
        return getSparseMatrix(n).get(n, m) - getSparseMatrix(n).get(n, m - 1);
    }
    private static SparseMatrix getSparseMatrix(int n){
        SparseMatrix sm = new SparseMatrix();
        for(int i = 1; i <= n; i++){
            sm.add(i, 1, 1);
        }
        for(int i = 2; i <= n; i++){
            for(int j = 2; j <= i; j++){
                if(i == j){
                    sm.add(i, j, 1 + sm.get(i, j - 1));
                }else if(i - j < j){
                    sm.add(i, j, sm.get(i - j, i - j) + sm.get(i, j - 1));
                }else {
                    sm.add(i, j, sm.get(i, j - 1) + sm.get(i - j, j));
                }
            }
        }
        return sm;
    }
    /**
     * 稀疏矩阵类
     * @author 江峰
     * @create 2018-11-24
     */
    private static class SparseMatrix{
        HashMap<Position, Integer> map;
        public SparseMatrix(){
            map = new HashMap<PartitionInteger.SparseMatrix.Position, Integer>();
        }
        public void add(int x, int y, int value){
            map.put(new Position(x, y), value);
        }
        @SuppressWarnings("null")
        public int get(int x, int y){
            return map.get(new Position(x, y));
        }
        class Position{
            int x;
            int y;
            public Position(int x, int y){
                this.x = x;
                this.y = y;
            }
            @Override
            public int hashCode() {
                return x * 31 + y;
            }
            @Override
            public boolean equals(Object obj) {
                if(this == obj){
                    return true;
                }
                if(obj instanceof Position){
                    Position position = (Position) obj;
                    return position.x == this.x && position.y == this.y;
                }
                return false;
            }
        }
    }
    // f(n, m) = f(n, m - 1) + f(n - m, m)的缓存叠加，获取n的所有划分数
    public static int getPartitionNumber(int n){
        if(n <= 0){
            throw new IllegalArgumentException("N must be a positive number.");
        }
        int[] cache = new int[n + 1];
        cache[0] = 1;
        for(int i = 1; i <= n; i++){
            for(int j = i; j <= n; j++){
                cache[j] += cache[j - i];
            }
        }
        return cache[n];
    }
    // f(n, m) = f(n, m - 1) + f(n - m, m)的缓存叠加，获取划分长度为m的n的所有划分数
    public static int getPartitionNumber(int n, int m){
        chechmn(m, n);
        int[] cache = new int[n + 1];
        cache[0] = 1;
        int last = 0;
        for(int i = 1; i <= m; i++){
            last = cache[n];
            for(int j = i; j <= n; j++){
                cache[j] += cache[j - i];
            }
        }
        return cache[n] - last;
    }
    // 动态规划获取n的所有划分排列
    public static List<int[]> partitionInteger(int n){
        if(n <= 0){
            throw new IllegalArgumentException("N must be a positive number.");
        }
        List<int[]> intsList = new ArrayList<>(PartitionInteger.getPartitionNumber(n));
        partitionInteger(n, 1, 0, intsList, new int[n], 0);
        return intsList;
    }
    private static void partitionInteger(int n, int begin, int cumulativeValue, List<int[]> intsList,
                                         int[] intArray, int index){
        if(cumulativeValue == n){
            intsList.add(Arrays.copyOf(intArray, index));
            return;
        }
        if(cumulativeValue > n){
            return;
        }
        for(int i = begin; i <= n; i++){
            cumulativeValue += i;
            intArray[index++] = i;
            partitionInteger(n, i, cumulativeValue,intsList, intArray, index);
            cumulativeValue -= i;
            index--;
        }
    }
    // 动态规划获取n的所有划分长度为m的排列
    public static List<int[]> partitionInteger(int n, int m){
        chechmn(m, n);
        if(m == 0){
            return new ArrayList<>(0);
        }
        if(m == 1){
            return new ArrayList<>(Arrays.asList(new int[]{n}));
        }
        List<int[]> intsList = new ArrayList<>(PartitionInteger.getPartitionNumber2(n, m));
        partitionInteger(n, 1, 0, intsList, new int[n], 0, m);
        return intsList;
    }
    private static void partitionInteger(int n, int begin, int cumulativeValue, List<int[]> intsList,
                                         int[] intArray, int index, int limit){
        if(cumulativeValue > n){
            return;
        }
        for(int i = begin; i <= n; i++){
            cumulativeValue += i;
            index = index + 1;
            if(index < limit && cumulativeValue >= n){
                return;
            }
            if(index <= limit){
                if(index == limit && n - cumulativeValue + i >= intArray[index - 2]){
                    intArray[index - 1] = n - cumulativeValue + i;
                    intsList.add(Arrays.copyOf(intArray, index));
                    return;
                }
                intArray[index - 1] = i;
                partitionInteger(n, i, cumulativeValue,intsList, intArray, index, limit);
            }
            cumulativeValue -= i;
            index--;
        }
    }
    // 检验参数m和n
    private static void chechmn(int m, int n){
        if(m < 0 || m > n){
            throw new IllegalArgumentException("m = " + m + ", n = " + n);
        }
    }
}

final class BitCount{
    private BitCount(){throw new IllegalAccessError();}
    // 逐位计算
    public static int bitCount0(int n){
        int count = 0;
        while(n != 0){
            count += n & 1;
            n >>>= 1;
        }
        return count;
    }
    // 利用n & (n - 1)
    public static int bitCount1(int n){
        int count = 0;
        while(n != 0){
            n = n & (n - 1);
            count++;
        }
        return count;
    }
    // 利用分治法
    public static int bitCount2(int n){
        n = (n & 0x55555555) + ((n >>> 1) & 0x55555555);
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);
        n = (n & 0x0F0F0F0F) + ((n >>> 4) & 0x0F0F0F0F);
        n = (n & 0x00FF00FF) + ((n >>> 8) & 0x00FF00FF);
        n = (n & 0x0000FFFF) + ((n >>> 16) & 0x0000FFFF);
        return n;
    }
    // 分治法优化
    public static int bitCount3(int n) {
        n = n - ((n >>> 1) & 0x55555555); // (2 * b + a) - a = a + b
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333); // 保持不变
        n = (n + (n >>> 4)) & 0x0f0f0f0f; // 有无效组，需要使用掩码去除
        n = n + (n >>> 8);  // 没有无效组，可以直接加
        n = n + (n >>> 16); // 没有无效组，可以直接加
        return n & 0x3f;
    }
    // 分治法的求余优化
    public static int bitCount4(int n){
        n = n - ((n >>> 1) & 0x55555555);
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);
        n = (n + (n >>> 4)) & 0x0f0f0f0f;
        n = (n + (n >>> 8)) & 0x00FF00FF;
        return n % 255;
    }
    // 分治法的乘法优化
    public static int bitCount5(int n){
        n = n - ((n >>> 1) & 0x55555555);
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);
        n = (n + (n >>> 4)) & 0x0F0F0F0F;
        return (n * 0x01010101) >>> 24;
    }
    // 分治法的求余优化
    public static int bitCount6(int n){
        n = n - (((n >>> 1) & 0xdb6db6db) + ((n >>> 2) & 0x49249249));
        n = (n + (n >>> 3)) & 0xc71c71c7;
        return n < 0 ? ((n >>> 30) + ((n << 2) >>> 2) % 63) : n % 63;
    }
    private static final int[] oneNumberTable ={
            0,1,1,2,1,2,2,3,1,2,2,3,2,3,3,4,1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,
            1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
            1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
            2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
            1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
            2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
            2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
            3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,4,5,5,6,5,6,6,7,5,6,6,7,6,7,7,8
    };
    // 打表法
    public static int bitCount7(int n){
        int count = 0;
        for (int i = 0; i < 32; i += 8){
            count += oneNumberTable[(n >> i) & 0xFF];
        }
        return count;
    }
}

final class BinaryOperation{
    private BinaryOperation(){throw new IllegalAccessError();}
    // 加法操作
    public static int add(int a, int b){
//		if(b == 0){
//			return a;
//		}
//		return add(a ^ b, (a & b) << 1); // a ^ b累加值，(a & b) << 1进位。
        int sum = 0;
        do {
            sum = a ^ b;
            b = (a & b) << 1;
            a = sum;
        } while (b != 0);
        return sum;
    }
    // 减法操作，利用 a - b = a + (~b + 1)
    public static int subtract(int a, int b){
        return add(add(a, 1), ~b);
    }
    // 乘法操作
    public static int multiply(int a, int b){
        int mul = 0;
        for(int i = 0; b != 0; i++){
            if((b & 1) == 1){
                mul += (a & 1) << i;
            }
            b >>>= 1;
        }
        return mul;
    }
    // 除法操作，利用二次幂乘积的性质。
    public static int divide(int dividend, int divisor){
        if(divisor == 0){
            throw new ArithmeticException(" / by zero");
        }
        if(dividend == 0 || divisor == -divisor){
            return 0;
        }
        int signum = dividend ^ divisor; // 符号不相同则为负，值相同为0，符号相同值不相同为正
        int absDividend = Math.abs(dividend);
        int absDivisor = Math.abs(divisor);
        if(divisor == 1){
            return signum >= 0 ? -absDividend : absDividend;
        }
        int quotient = 0;
        while (absDividend - absDivisor >= 0) {
            int twoPowMulDivisor = absDivisor, multiple = 1;
            while (absDividend - (twoPowMulDivisor << 1) >= 0) {
                twoPowMulDivisor <<= 1;
                multiple <<= 1;
            }
            absDividend -= twoPowMulDivisor;
            quotient += multiple;
        }
        return signum >= 0 ? quotient : -quotient;
    }
}
@SuppressWarnings("unused")
final class GreatestCommonDivisor{
    private GreatestCommonDivisor(){throw new IllegalAccessError();}
    // 分解质因数法
    private static int greatestCommonDivisor2(int a, int b){
        if((a | b) < 0){
            throw new IllegalArgumentException("a and b must be a positive integer.");
        }
        HashMap<Integer, Integer> factorMap = new HashMap<>();
        for(int factor : MathUtils.factor(a)){
            Integer count = factorMap.get(factor);
            factorMap.put(factor, count == null ? 1 : count + 1);
        }
        int gcDivisor = 1;
        for(int factor : MathUtils.factor(b)){
            Integer count = factorMap.get(factor);
            if(count != null && count > 0){
                gcDivisor *= factor;
                factorMap.put(factor, count - 1);
            }
        }
        return gcDivisor;
    }
    // 更相减损法递归版
    private static int greatestCommonDivisor0(int a, int b){
        if((a | b) < 0){
            throw new IllegalArgumentException("a and b must be a positive integer.");
        }
        return greatestCommonDivisor00(a, b);
    }
    private static int greatestCommonDivisor00(int a, int b){
        if(a == b){
            return a;
        }else if(a > b){
            return greatestCommonDivisor00(a - b, b);
        }else {
            return greatestCommonDivisor00(a, b - a);
        }
    }
    // 更相减损法
    private static int greatestCommonDivisor1(int a, int b){
        if((a | b) < 0){
            throw new IllegalArgumentException("a and b must be a positive integer.");
        }
        while(a != b){
            if(a > b){
                a -= b;
            }else{
                b -= a;
            }
        }
        return a;
    }
    // 辗转相除法
    public static int greatestCommonDivisor(int a, int b){
        if((a | b) < 0){
            throw new IllegalArgumentException("a and b must be a positive integer.");
        }
        int gcDivisor = a;
        while(a != b){
            if(a > b){
                gcDivisor = b;
                a %= b;
            }else{
                gcDivisor = a;
                b %= a;
            }
            if(a == 0 || b == 0){
                break;
            }
        }
        return gcDivisor;
    }
    // 多数值求最大公约数
    public static int greatestCommonDivisor(int...nums){
        int len = nums.length;
        if(len == 0){
            throw new IllegalArgumentException("The array of nums is empty.");
        }
        if(len == 1){
            return nums[0];
        }
        int gcDivisor = nums[0];
        for(int i = 1; i < len; ++i){
            int num = nums[i];
            if(num <= 0){
                throw new IllegalArgumentException("The array of nums contains a non-positive integer.");
            }
            gcDivisor = greatestCommonDivisor(gcDivisor, num);
        }
        return gcDivisor;
    }
}
@SuppressWarnings("unused")
final class LeastCommonMultiple{
    private LeastCommonMultiple(){throw new IllegalAccessError();}
    // 分解质因数法
    private static int leastCommonMultiple0(int a, int b){
        if((a | b) < 0){
            throw new IllegalArgumentException("a and b must be a positive integer.");
        }
        int[] aFactors = MathUtils.factor(a);
        int[] bFactors = MathUtils.factor(b);
        HashMap<Integer, Integer> aFactorMap = new HashMap<>();
        for(int factor : aFactors){
            Integer count = aFactorMap.get(factor);
            aFactorMap.put(factor, count == null ? 1 : count + 1);
        }
        HashMap<Integer, Integer> bFactorMap = new HashMap<>();
        for(int factor : bFactors){
            Integer count = bFactorMap.get(factor);
            bFactorMap.put(factor, count == null ? 1 : count + 1);
        }
        HashMap<Integer, Integer> abFactorMap = new HashMap<>();
        for(Map.Entry<Integer, Integer> entry : aFactorMap.entrySet()){
            Integer aFactor = entry.getKey();
            Integer bCount = bFactorMap.get(aFactor);
            abFactorMap.put(aFactor, Math.max(entry.getValue(), bCount == null ? 0 : bCount));
        }
        for(Map.Entry<Integer, Integer> entry : bFactorMap.entrySet()){
            Integer bFactor = entry.getKey();
            Integer abCount = abFactorMap.get(bFactor);
            if(abCount == null){
                abFactorMap.put(bFactor, entry.getValue());
            }
        }
        int lcMultiple = 1;
        for(Map.Entry<Integer, Integer> entry : abFactorMap.entrySet()){
            lcMultiple *= (int)Math.pow(entry.getKey(), entry.getValue());
        }
        return lcMultiple;
    }
    // 公式法：a * b = g(a, b) * l(a, b)
    public static int leastCommonMultiple(int a, int b){
        if((a | b) < 0){
            throw new IllegalArgumentException("a and b must be a positive integer.");
        }
        int gcDivisor = GreatestCommonDivisor.greatestCommonDivisor(a, b);
        // a * b可能发生精度溢出
        BigInteger lcMultiple = new BigInteger(String.valueOf(a)).multiply(new BigInteger(String.valueOf(b)));
        return lcMultiple.divide(new BigInteger(String.valueOf(gcDivisor))).intValue();
    }
}