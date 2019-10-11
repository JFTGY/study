package com.study.algorithm;

import org.junit.Test;

/**
 * @author : JF
 * Date    : 2019.10.10
 * Time    : 20:30
 * Email   ：tangqibao_620@163.com
 */
public class SimulatedAnnealingAlgorithmTest {

    private SimulatedAnnealingAlgorithm sa = new SimulatedAnnealingAlgorithm();

    @Test
    public void linearTest() {
        SimulatedAnnealingAlgorithm.LinearCostFunction cf = (double x) -> x + 10 * Math.sin(5 * x) + 7 * Math.cos(4 * x);
        System.out.println(sa.simulatedAnnealing(cf, 0, 9));
    }

    @Test
    public void nonlinearTest(){
        SimulatedAnnealingAlgorithm.NonlinearCostFunction<double[]> ncf = (double[][] data, int[] sequences) -> {
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
        // double[][] data = {{0.235263, 0.274722}, {0.759341, 0.433666}, {0.485559, 0.781962}, {0.178607, 0.493581}, {0.539375, 0.394668}, {0.343336, 0.517668}, {0.249070, 0.706378}, {0.658285, 0.766169}, {0.684994, 0.267492}, {0.496912, 0.223178}, {0.641858, 0.620075}, {0.806771, 0.580588}, {0.336323, 0.369070}, {0.522920, 0.526854}, {0.564054, 0.657255}, {0.625150, 0.489281}, {0.381730, 0.703551}};
        System.out.println(sa.simulatedAnnealing(ncf, data));
    }
}