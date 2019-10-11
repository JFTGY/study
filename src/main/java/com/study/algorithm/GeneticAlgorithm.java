package com.study.algorithm;

import com.common.utils.ArrayUtils;
import lombok.Setter;

import java.util.Arrays;
import java.util.Random;

@Setter
public class GeneticAlgorithm {

    private int numberOfGene = 100;       // 基因种类数量
    private int lengthOfGene;             // 每条基因长度
    private int thresholdOfGenetic = 200; // 遗传迭代次数
    private double crossRate = 0.7;       // 交叉遗传率
    private double mutateRate = 0.02;     // 变异遗传率
    private static final Random rand = new Random();

    class NonlinearGeneticAlgorithm {

        private double minValue = Integer.MAX_VALUE;

        public <T> double generic(NonlinearFitnessFunction<T> fitnessFunction, T[] data) {
            lengthOfGene = data.length;
            int[] sequences = new int[lengthOfGene];
            for (int i = 0; i < lengthOfGene; ++i) {
                sequences[i] = i;
            }
            int[][] genes = new int[numberOfGene][lengthOfGene];
            for (int i = 0; i < numberOfGene; i++) {
                ArrayUtils.shuffle(sequences);
                genes[i] = Arrays.copyOf(sequences, lengthOfGene);
            }
            for (int i = 0; i < thresholdOfGenetic; i++) {
                genes = selection(getFitnessArray(fitnessFunction, data, genes), genes);
                cross(genes);
                mutate(genes);
            }
            for (int i = 0; i < lengthOfGene; i++) {
                double value = fitnessFunction.calculate(data, genes[i]);
                if (value < minValue) {
                    minValue = value;
                }
            }
            return minValue;
        }

        private void mutate(int[][] genes) {
            for (int i = 0; i < lengthOfGene; ++i) {
                if (rand.nextDouble() < mutateRate) {
                    ArrayUtils.shuffle(genes[i]);
                }
            }
        }

        private void cross(int[][] genes) {
            for (int i = 0, limit = numberOfGene - 1; i < limit; i += 2) {
                if (rand.nextDouble() > crossRate) {
                    continue;
                }
                int crossBegin = rand.nextInt(lengthOfGene);
                int crossEnd = rand.nextInt(lengthOfGene);
                if (crossBegin > crossEnd) {
                    int temp = crossBegin;
                    crossBegin = crossEnd;
                    crossEnd = temp;
                }
                int[] segment = Arrays.copyOfRange(genes[i], crossBegin, crossEnd + 1);
                for (int j = crossBegin; j <= crossEnd; ++j) {
                    int value1 = genes[i + 1][j], value2 = segment[j - crossBegin];
                    for (int k = 0; k < lengthOfGene; ++k) {
                        if (genes[i][k] == value1) {
                            genes[i][k] = genes[i][j];
                            genes[i][j] = value1;
                            break;
                        }
                        if (genes[i + 1][k] == value2) {
                            genes[i + 1][k] = genes[i + 1][j];
                            genes[i + 1][j] = value2;
                            break;
                        }
                    }
                }
            }
        }

        private <T> double[] getFitnessArray(NonlinearFitnessFunction fitnessFunction, T[] data, int[][] genes) {
            double[] sumArrayOfFitness = new double[numberOfGene];
            sumArrayOfFitness[0] = fitnessFunction.calculate(data, genes[0]);
            if (sumArrayOfFitness[0] < minValue) {
                minValue = sumArrayOfFitness[0];
            }
            sumArrayOfFitness[0] = 1 / sumArrayOfFitness[0];
            for (int i = 1; i < numberOfGene; ++i) {
                double value = fitnessFunction.calculate(data, genes[i]);
                if (value < minValue) {
                    minValue = value;
                }
                sumArrayOfFitness[i] = sumArrayOfFitness[i - 1] + 1 / value;
            }
            return sumArrayOfFitness;
        }

        private int[][] selection(double[] sumArrayOfFitness, int[][] genes) {
            int[][] newGenes = new int[numberOfGene][lengthOfGene];
            double fitness = sumArrayOfFitness[numberOfGene - 1];
            for (int i = 0; i < numberOfGene; ++i) {
                int pos = ArrayUtils.lowerBound(sumArrayOfFitness, rand.nextDouble() * fitness);
                System.arraycopy(genes[pos], 0, newGenes[i], 0, lengthOfGene);
            }
            return newGenes;
        }

    }

    class LinearGeneticAlgorithm {

        private double minValue = Integer.MAX_VALUE;

        public double genetic(LinearFitnessFunction linearFitnessFunction, double lowerBound, double upperBound, double accuracy) {
            // 计算二进制编码基因的长度
            geneLength(lowerBound, upperBound, accuracy);
            // 初始化种群
            int[] genes = new int[numberOfGene];
            int maxGeneValue = 1 << lengthOfGene;
            for (int i = 0; i < numberOfGene; ++i) {
                genes[i] = rand.nextInt(maxGeneValue);
            }
            for (int i = 0; i < thresholdOfGenetic; ++i) {
                // 选择
                genes = selection(sumFitnessArray(linearFitnessFunction, lowerBound, upperBound, genes), genes);
                // 交叉
                cross(genes);
                // 变异
                mutate(genes);
            }
            for (int i = 0; i < numberOfGene; i++) {
                double value = linearFitnessFunction.calculate(decode(lowerBound, upperBound, genes[i]));
                if (value < minValue) {
                    minValue = value;
                }
            }
            return minValue;
        }

        /**
         * 基因间发生突变
         */
        private void mutate(int[] genes) {
            for (int i = 0; i < numberOfGene; ++i) {
                if (rand.nextDouble() < mutateRate) {
                    for (int j = 0; j < lengthOfGene; ++j) {
                        if (rand.nextDouble() > 0.5) {
                            genes[i] ^= 1 << j;
                        }
                    }
                }
            }
        }

        /**
         * 交换基因片段
         */
        private void cross(int[] genes) {
            for (int i = 0, limit = numberOfGene - 1; i < limit; i += 2) {
                if (rand.nextDouble() < crossRate) {
                    int crossBegin = rand.nextInt(lengthOfGene);
                    int crossEnd = rand.nextInt(lengthOfGene);
                    if (crossBegin > crossEnd) {
                        int temp = crossBegin;
                        crossBegin = crossEnd;
                        crossEnd = temp;
                    }
                    int gene1 = genes[i], gene2 = genes[i + 1];
                    int crossLen = crossEnd - crossBegin + 1;
                    int segment1 = (gene1 >>> crossBegin) & ((1 << crossLen) - 1);
                    int segment2 = (gene2 >>> crossBegin) & ((1 << crossLen) - 1);
                    genes[i] = ((gene1 >>> (crossEnd + 1)) << (crossEnd + 1)) +
                            (segment2 << crossBegin) + (gene1 & ((1 << crossBegin) - 1));
                    genes[i + 1] = ((gene2 >>> (crossEnd + 1)) << (crossEnd + 1)) +
                            (segment1 << crossBegin) + (gene2 & ((1 << crossBegin) - 1));
                }
            }
        }

        /**
         * 计算各基因的适应度，累加到适应度数组，累加的意义：适应度概率与二分法随机搜索累加适应度的索引概率一致。
         */
        private double[] sumFitnessArray(LinearFitnessFunction linearFitnessFunction, double lowerBound, double upperBound, int[] genes) {
            double sumOfFitness = 0.0;
            double[] sumArrayOfFitness = new double[numberOfGene];
            double minFitness = Integer.MAX_VALUE;
            for (int i = 0; i < numberOfGene; ++i) {
                double value = linearFitnessFunction.calculate(decode(lowerBound, upperBound, genes[i]));
                if (value < minValue) {
                    minValue = value;
                }
                if (value < minFitness) {
                    minFitness = value;
                }
                sumArrayOfFitness[i] = value;
            }
            sumOfFitness = 0;
            minFitness -= rand.nextDouble() * rand.nextDouble(); // 减去一个较小量，避免Infinity出现
            for (int i = 0; i < numberOfGene; i++) {
                sumArrayOfFitness[i] = sumOfFitness += 1 / (sumArrayOfFitness[i] - minFitness);
            }
            return sumArrayOfFitness;
        }

        /**
         * 选择适应度更大的基因
         */
        private int[] selection(double[] sumArrayOfFitness, int[] genes) {
            int[] newGenes = new int[numberOfGene];
            double firstFitness = sumArrayOfFitness[0], diff = sumArrayOfFitness[numberOfGene - 1] - firstFitness;
            for (int i = 0; i < numberOfGene; ++i) {
                int pos = ArrayUtils.lowerBound(sumArrayOfFitness, firstFitness + rand.nextDouble() * diff);
                if (pos == numberOfGene) {
                    pos = numberOfGene - 1;
                }
                newGenes[i] = genes[pos];
            }
            return newGenes;
        }

        /**
         * 将基因解码为函数的自变量值
         */
        private double decode(double lowerBound, double upperBound, int gene) {
            return lowerBound + gene * (upperBound - lowerBound) / (1 << lengthOfGene);
        }
    }

    private void geneLength(double lowerBound, double upperBound, double accuracy) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("The lower bound is " + lowerBound
                    + "and the upper bound is " + upperBound + ".");
        }
        long range = Math.round((upperBound - lowerBound) / accuracy);
        if (range < 0 || range > 2147483647) {
            throw new IllegalArgumentException("The accuracy is too small.");
        }
        lengthOfGene = Integer.bitCount((Integer.highestOneBit((int) range) << 1) - 1);
    }

    interface LinearFitnessFunction {
        double calculate(double x);
    }

    interface NonlinearFitnessFunction<T> {
        double calculate(T[] data, int[] sequences);
    }

    public GeneticAlgorithm setGeneNumber(int numberOfGene) {
        this.numberOfGene = numberOfGene;
        return this;
    }

    public GeneticAlgorithm setGenericThreshold(int thresholdOfGenetic) {
        this.thresholdOfGenetic = thresholdOfGenetic;
        return this;
    }

    public GeneticAlgorithm setCrossRate(double crossRate) {
        this.crossRate = crossRate;
        return this;
    }

    public GeneticAlgorithm setMutateRate(double mutateRate) {
        this.mutateRate = mutateRate;
        return this;
    }
}

class GeneticAlgorithmTwoDimensional {
    private static final int CHROMOSOME_NUMBER = 100;
    private final int CHROMOSOME_LENGTH;
    private static final int GENETIC_THRESHOLD = 200;
    private static final double CROSS_RATE = 0.6;
    private static final double MUTATE_RATE = 0.01;

    private int[][] chromosomes = new int[CHROMOSOME_NUMBER][];

    private double shortestDistance = Double.MAX_VALUE;

    private Random rand = new Random();

    private int[][] cities;

    public GeneticAlgorithmTwoDimensional(int[][] cities) {
        this.CHROMOSOME_LENGTH = cities.length;
        this.cities = cities;
    }

    private void shuffle(int[] array) {
        int size = rand.nextInt(CHROMOSOME_LENGTH);
        for (int i = size; i > 1; --i) {
            ArrayUtils.swap(array, i - 1, rand.nextInt(i));
        }
    }

    public int genetic() {
        int[] positions = new int[CHROMOSOME_LENGTH];
        for (int i = 0; i < CHROMOSOME_LENGTH; ++i) {
            positions[i] = i;
        }
        for (int i = 0; i < CHROMOSOME_NUMBER; ++i) {
            chromosomes[i] = Arrays.copyOf(positions, CHROMOSOME_LENGTH);
            shuffle(chromosomes[i]);

        }
        for (int i = 0; i < GENETIC_THRESHOLD; ++i) {
            // 选择
            selection(getSumFitnessArray());
            // 交叉
            cross();
            // 变异
            mutate();
        }
        return (int) shortestDistance;
    }

    private double[] getSumFitnessArray() {
        double[] fitnessArray = new double[CHROMOSOME_NUMBER];
        double sumFitness = 0.0;
        for (int i = 0; i < CHROMOSOME_NUMBER; ++i) {
            double fitness = distance(chromosomes[i]);
            if (fitness < shortestDistance) {
                shortestDistance = fitness;
            }
            sumFitness += fitnessArray[i] = fitness;
        }
        double totalSumFitness = 0.0;
        double[] sumFitnessArray = fitnessArray;
        for (int i = 0; i < CHROMOSOME_NUMBER; ++i) {
            sumFitnessArray[i] = totalSumFitness += sumFitness - fitnessArray[i];
        }
        return sumFitnessArray;
    }

    private void selection(double[] sumFitnessArray) {
        int[][] newChromosomes = new int[CHROMOSOME_NUMBER][];
        for (int i = 0; i < CHROMOSOME_NUMBER; ++i) {
            double target = sumFitnessArray[0] + rand.nextDouble() * (sumFitnessArray[CHROMOSOME_NUMBER - 1] - sumFitnessArray[0]);
            int index = ArrayUtils.lowerBound(sumFitnessArray, target);
            if (index == CHROMOSOME_NUMBER) {
                index = CHROMOSOME_NUMBER - 1;
            }
            newChromosomes[i] = Arrays.copyOf(chromosomes[index], CHROMOSOME_LENGTH);
        }
        chromosomes = newChromosomes;
    }

    private void cross() {
        for (int i = CHROMOSOME_NUMBER - 1; i > 0; i -= 2) {
            if (rand.nextDouble() > CROSS_RATE) {
                continue;
            }
            int begin = rand.nextInt(CHROMOSOME_LENGTH);
            int end = rand.nextInt(CHROMOSOME_LENGTH);
            if (begin > end) {
                int temp = begin;
                begin = end;
                end = temp;
            }
            int[] tempArray = Arrays.copyOfRange(chromosomes[i], begin, end + 1);
            for (int j = begin; j <= end; ++j) {
                int value = chromosomes[i - 1][j];
                for (int k = 0; k < CHROMOSOME_LENGTH; ++k) {
                    if (chromosomes[i][k] == value) {
                        chromosomes[i][k] = chromosomes[i][j];
                        chromosomes[i][j] = value;
                        break;
                    }
                }
            }
            for (int j = begin; j <= end; ++j) {
                int value = tempArray[j - begin];
                for (int k = 0; k < CHROMOSOME_LENGTH; ++k) {
                    if (chromosomes[i - 1][k] == value) {
                        chromosomes[i - 1][k] = chromosomes[i - 1][j];
                        chromosomes[i - 1][j] = value;
                        break;
                    }
                }
            }
        }
    }

    private void mutate() {
        for (int i = 0; i < CHROMOSOME_NUMBER; ++i) {
            if (rand.nextDouble() < MUTATE_RATE) {
                shuffle(chromosomes[i]);
            }
        }
    }

    private double distance(int[] positions) {
        int[][] cities = this.cities;
        double distance = 0.0;
        for (int i = CHROMOSOME_LENGTH - 2; i >= 0; --i) {
            distance += Math.sqrt(Math.pow(cities[i][0] - cities[i + 1][0], 2) + Math.pow(cities[i][1] - cities[i + 1][1], 2));
        }
        distance += Math.sqrt(Math.pow(cities[CHROMOSOME_LENGTH - 1][0] - cities[0][0], 2)
                + Math.pow(cities[CHROMOSOME_LENGTH - 1][1] - cities[0][1], 2));
        return distance;
    }
}

class GeneticAlgorithmOneDimensional {

    private int binarySearch(double[] array, double target) {
        int left = 0, right = array.length;
        while (left < right) {
            int mid = (left + right) >>> 1;
            if (target > array[mid]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    private static final int CHROMOSOME_NUMBER = 100;
    private static final int CHROMOSOME_LENGTH = 17;
    private static final int GENETIC_THRESHOLD = 200;
    private static final double CROSS_RATE = 0.6;
    private static final double MUTATE_RATE = 0.01;
    private static final int LOWER_BOUND = 0;
    private static final int UPPER_BOUND = 9;
    private static final int CHROMOSOME_THRESHOLD = 1 << 17;
    private static final int MAX_CHROMOSOME = CHROMOSOME_THRESHOLD - 1;

    double maxValue = Integer.MIN_VALUE;
    Random rand = new Random();

    private int[] chromosomes = new int[CHROMOSOME_NUMBER];

    public void genetic() {
        // 初始化种群
        for (int i = 0; i < CHROMOSOME_NUMBER; ++i) {
            chromosomes[i] = rand.nextInt(CHROMOSOME_THRESHOLD);
        }
        for (int index = 0; index < GENETIC_THRESHOLD; ++index) {
            // 选择
            selection(getSumFitnessArray());
            // 交叉
            cross();
            // 变异
            mutate();
        }
    }

    /**
     * 基因间发生突变
     */
    private void mutate() {
        int midChromosomeLength = CHROMOSOME_LENGTH / 2;
        for (int i = 0, randPos; i < CHROMOSOME_NUMBER; ++i) {
            if (rand.nextDouble() < MUTATE_RATE) {
                for (int j = 0; j < CHROMOSOME_LENGTH; ++j) {
                    randPos = rand.nextInt(CHROMOSOME_LENGTH);
                    if (randPos > midChromosomeLength) {
                        chromosomes[i] ^= 1 << randPos;
                    }
                }
            }
        }
    }

    /**
     * 交换基因之间的片段
     */
    private void cross() {
        for (int i = 0, limit = CHROMOSOME_NUMBER - 1; i < limit; i += 2) {
            if (rand.nextDouble() < CROSS_RATE) {
                int crossBegin = rand.nextInt(CHROMOSOME_LENGTH);
                int crossEnd = rand.nextInt(CHROMOSOME_LENGTH);
                if (crossBegin > crossEnd) {
                    int temp = crossBegin;
                    crossBegin = crossEnd;
                    crossEnd = temp;
                }
                int crossLen = crossEnd - crossBegin + 1;
                int segment1 = (chromosomes[i] >>> crossBegin) & ((1 << crossLen) - 1);
                int segment2 = (chromosomes[i + 1] >>> crossBegin) & ((1 << crossLen) - 1);
                chromosomes[i] = ((chromosomes[i] >>> (crossEnd + 1)) << (crossEnd + 1)) +
                        (segment2 << crossBegin) + chromosomes[i] & (1 << crossBegin);
                chromosomes[i + 1] = ((chromosomes[i + 1] >>> (crossEnd + 1)) << (crossEnd + 1)) +
                        (segment1 << crossBegin) + chromosomes[i + 1] & (1 << crossBegin);
            }
        }
    }

    /**
     * 计算各基因的适应度，累加到适应度数组，累加的意义：适应度概率与二分法随机搜索累加适应度的索引概率一致。
     */
    private double[] getSumFitnessArray() {
        double sumFitness = 0.0;
        double[] sumFitnessArray = new double[CHROMOSOME_NUMBER];
        for (int i = 0; i < CHROMOSOME_NUMBER; ++i) {
            double value = calFunValue(decode(chromosomes[i]));
            if (value > maxValue) {
                maxValue = value;
            }
            sumFitnessArray[i] = sumFitness += value;
        }
        return sumFitnessArray;
    }

    /**
     * 选择适应度更大的基因
     */
    private void selection(double[] sumFitnessArray) {
        int[] tempChromosomes = new int[CHROMOSOME_NUMBER];
        for (int i = 0; i < CHROMOSOME_NUMBER; ++i) {
            double randTarget = sumFitnessArray[0] + rand.nextDouble() * (sumFitnessArray[CHROMOSOME_NUMBER - 1] - sumFitnessArray[0]);
            tempChromosomes[i] = chromosomes[binarySearch(sumFitnessArray, randTarget)];
        }
        chromosomes = tempChromosomes;
    }

    /**
     * 将基因解码为函数的自变量值
     */
    private double decode(double chromosome) {
        return LOWER_BOUND + chromosome * (UPPER_BOUND - LOWER_BOUND) / CHROMOSOME_THRESHOLD;
    }

    /**
     * 根据自变量值计算函数的值
     */
    private double calFunValue(double x) {
        return x + 10 * Math.sin(5 * x) + 7 * Math.cos(4 * x);
    }

}

