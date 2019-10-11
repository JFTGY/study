package com.study.algorithm;

import com.common.utils.ArrayUtils;
import lombok.Setter;

import java.util.Arrays;
import java.util.Random;

/**
 * 这是一个模拟退火算法类，主要是为了解决线性和非线性求解问题。
 * <p>
 * 线性问题主要是在指定区间求取一元线性函数最小值，而非线性问题是TSP问题。
 * <p>
 * 为了使求解结果更加精确，需要调整无效阈值、初始温度、冷却速率、迭代阈值、降温阈值以及交换次数这五个参数。
 * 如果参数选择不合理，线性以及非线性求解效率可能会有较大的差异。
 * <p>
 * 参考：Simulated Annealing for Traveling Salesman Problem -- David Bookstaber
 * Optimization by Simulated Annealing -- S. Kirkpatrick, C. D. Gelatt, Jr., M. P. Vecchi
 *
 * @author : JF
 * Date : 2019.9.6
 * Time : 09:10
 * Email   ：tangqibao_620@163.com
 */
@SuppressWarnings("unused")
@Setter
public class SimulatedAnnealingAlgorithm {

    /**
     * 无效阈值。该参数是为了避免模拟退火算法陷入死循环，当出现多次无效操作，则迭代器前进一步。
     */
    private int invalidThreshold = 100;  // 无效阈值
    /**
     * 初始温度。该参数用于控制采样概率，该值的大小和精度无线性关系，但是稍微取大点，可以提高前期模拟的精度。
     */
    private double initTemperature = 100;
    /**
     * 冷却速率。该参数和温度共同控制采样概率，无特殊情况，一般默认0.95。
     */
    private double coolingRate = 0.95;
    /**
     * 迭代阈值。该参数控制模拟次数，如果参数取得过大，则精度较高，但是运行速度较慢。取值视具体情况而定。
     */
    private int iterationThreshold = 4000;
    /**
     * 降温阈值。该参数控制降温的时机。
     */
    private int coolingThreshold = 10;                 // 降温阈值
    /**
     * 交换次数。主要用于非线性情况，前期随机交换次数较多，后期逐渐稳定后，交换次数会降低。
     */
    private int numberOfSwap = 3;                      // 交换次数

    private static final Random rand = new Random(); // 随机数生成器

    public static void main(String[] args) {
        SimulatedAnnealingAlgorithm sa = new SimulatedAnnealingAlgorithm();
        LinearCostFunction cf = (double x) -> x + 10 * Math.sin(5 * x) + 7 * Math.cos(4 * x);
        System.out.println(sa.simulatedAnnealing(cf, 0, 9));
        NonlinearCostFunction<double[]> ncf = (double[][] data, int[] sequences) -> {
            double distance = 0.0;
            int len = data.length;
            for (int i = len - 2; i >= 0; --i) {
                int x = sequences[i], y = sequences[i + 1];
                distance += Math.sqrt(Math.pow(data[x][0] - data[y][0], 2) +
                        Math.pow(data[x][1] - data[y][1], 2));
            }
            distance += Math.sqrt(Math.pow(data[sequences[len - 1]][0] - data[sequences[0]][0], 2)
                    + Math.pow(data[sequences[len - 1]][1] - data[sequences[0]][1], 2));
            return distance;
        };
        // 169.5784932576663
        double[][] data = {{116.46, 39.92}, {117.2, 39.13}, {121.48, 31.22}, {106.54, 29.59}, {91.11, 29.97}, {87.68, 43.77}, {106.27, 38.47}, {111.65, 40.82}, {108.33, 22.84}, {126.63, 45.75}, {125.35, 43.88}, {123.38, 41.8}, {114.48, 38.03}, {112.53, 37.87}, {101.74, 36.56}, {117, 36.65}, {113.6, 34.76}, {118.78, 32.04}, {117.27, 31.86}, {120.19, 30.26}, {119.3, 26.08}, {115.89, 28.68}, {113, 28.21}, {114.31, 30.52}, {113.23, 23.16}, {121.5, 25.05}, {110.35, 20.02}, {103.73, 36.03}, {108.95, 34.27}, {104.06, 30.67}, {106.71, 26.57}, {102.73, 25.04}, {114.1, 22.2}, {113.33, 22.13}};
        // 2.696102905900265
//        double[][] data = {{0.235263, 0.274722}, {0.759341, 0.433666}, {0.485559, 0.781962}, {0.178607, 0.493581}, {0.539375, 0.394668}, {0.343336, 0.517668}, {0.249070, 0.706378}, {0.658285, 0.766169}, {0.684994, 0.267492}, {0.496912, 0.223178}, {0.641858, 0.620075}, {0.806771, 0.580588}, {0.336323, 0.369070}, {0.522920, 0.526854}, {0.564054, 0.657255}, {0.625150, 0.489281}, {0.381730, 0.703551}};
        System.out.println(sa.simulatedAnnealing(ncf, data));
    }

    /**
     * 利用模拟退火算法解决TSP问题
     *
     * @param costFunction 非线性代价函数，该函数主要是计算多维数据在特定顺序下的值。
     * @param data         多维数组
     * @param <T>          数组类型
     * @return TSP问题的最优解
     */
    public <T> double simulatedAnnealing(NonlinearCostFunction<T> costFunction, T[] data) {
        int len = data.length;
        int[] curSequences = new int[len];
        for (int i = 0; i < len; ++i) {
            curSequences[i] = i;
        }
        int iterations = 1, coolingCount = 0, numberOfSwap = this.numberOfSwap, numberOfInvalid = 0;
        double temperature = initTemperature;
        double curDistance = costFunction.calculate(data, curSequences), minDistance = curDistance;
        while (iterations < iterationThreshold) {
            int[] newSequence = Arrays.copyOf(curSequences, len);
            for (int i = 0; i < numberOfSwap; ++i) {
                ArrayUtils.swap(newSequence, rand.nextInt(len), rand.nextInt(len));
            }
            double newDistance = costFunction.calculate(data, newSequence);
            double diff = newDistance - curDistance;
            if (newDistance < curDistance || rand.nextDouble() < Math.exp(-diff / temperature)) {
                curSequences = newSequence;
                if (++coolingCount >= coolingThreshold && diff < 0) {
                    temperature *= coolingRate;
                    coolingCount = 0;
                }
                numberOfSwap = (int) Math.round(numberOfSwap * Math.exp(-diff / (iterations * temperature)));
                if (numberOfSwap == 0) {
                    numberOfSwap = 1;
                }
                if ((curDistance = newDistance) < minDistance) {
                    minDistance = curDistance;
                }
                ++iterations;
                numberOfInvalid = 0;
            }
            // 避免陷入死循环
            if (++numberOfInvalid == invalidThreshold) {
                ++iterations;
            }
        }
        return minDistance;
    }

    /**
     * 利用模拟退火算法计算一元函数在指定区间的最小值。
     *
     * @param costFunction 线性代价函数，该函数主要是计算一元函数值。
     * @param lowerBound   下界
     * @param upperBound   上界
     * @return
     */
    public double simulatedAnnealing(LinearCostFunction costFunction, double lowerBound, double upperBound) {
        double temperature = initTemperature;
        double curX = lowerBound + rand.nextDouble() * (upperBound - lowerBound), minX = curX;
        double curY = costFunction.calculate(curX), minY = curY;
        int iterations = 1, coolingCount = 0, numberOfInvalid = 0;
        while (iterations < iterationThreshold) {
            double randomStep = 2 * rand.nextDouble() - 1;
            double newX = curX + randomStep;
            // 取值可能超出边界
            if (newX < lowerBound || newX > upperBound) {
                newX = curX - randomStep;
            }
            double newY = costFunction.calculate(newX);
            double diff = newY - curY;
            // 判断取舍
            if (diff < 0.0 || Math.random() < Math.exp(-diff / temperature)) {
                curX = newX;
                if (++coolingCount >= coolingThreshold && diff < 0) {
                    temperature *= coolingRate;
                    coolingCount = 0;
                }
                if ((curY = newY) < minY) {
                    minY = curY;
                }
                ++iterations;
                numberOfInvalid = 0;
            }
            // 避免死循环
            if (++numberOfInvalid == invalidThreshold) {
                ++iterations;
            }
        }
        return minY;
    }

    /**
     * 线性代价函数接口，主要用于一元问题的求解。
     */
    interface LinearCostFunction {
        double calculate(double x);
    }

    /**
     * 非线性代价函数接口，主要用于TSP问题的求解。
     *
     * @param <T>
     */
    interface NonlinearCostFunction<T> {
        double calculate(T[] data, int[] sequences);
    }
}




