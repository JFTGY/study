package com.study.algorithm;

import org.junit.Test;

/**
 * @author : JF
 * Date    : 2019.10.10
 * Time    : 20:34
 * Email   ：tangqibao_620@163.com
 */
public class GeneticAlgorithmTest {

    @Test
    public void linearTest(){
        GeneticAlgorithm.LinearGeneticAlgorithm ga = new GeneticAlgorithm().new LinearGeneticAlgorithm();
        double sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += ga.genetic((double x) -> x + 10 * Math.sin(5 * x) + 7 * Math.cos(4 * x), 0, 9, 1e-6);
        }
        System.out.println(sum / 10);
    }

    @Test
    public void nonlinearTest() {
        GeneticAlgorithm.NonlinearFitnessFunction<double[]> ncf = (double[][] data, int[] sequences) -> {
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
        GeneticAlgorithm.NonlinearGeneticAlgorithm nga = new GeneticAlgorithm().new NonlinearGeneticAlgorithm();
        double[][] data = {{116.46, 39.92}, {117.2, 39.13}, {121.48, 31.22}, {106.54, 29.59}, {91.11, 29.97}, {87.68, 43.77}, {106.27, 38.47}, {111.65, 40.82}, {108.33, 22.84}, {126.63, 45.75}, {125.35, 43.88}, {123.38, 41.8}, {114.48, 38.03}, {112.53, 37.87}, {101.74, 36.56}, {117, 36.65}, {113.6, 34.76}, {118.78, 32.04}, {117.27, 31.86}, {120.19, 30.26}, {119.3, 26.08}, {115.89, 28.68}, {113, 28.21}, {114.31, 30.52}, {113.23, 23.16}, {121.5, 25.05}, {110.35, 20.02}, {103.73, 36.03}, {108.95, 34.27}, {104.06, 30.67}, {106.71, 26.57}, {102.73, 25.04}, {114.1, 22.2}, {113.33, 22.13}};
        System.out.println(nga.generic(ncf, data));
        int[] routes = {11, 10, 9, 0, 1, 15, 12, 7, 13, 16, 28, 6, 27, 14, 5, 4, 31, 29, 3, 30, 8, 26, 33, 24, 32, 25, 20, 21, 22, 23, 18, 17, 19, 2, 11};
        System.out.println(ncf.calculate(data, routes));
    }
}