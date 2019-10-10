package com.study.datastructure;

import java.io.*;
import java.util.*;

/**
 * 霍夫曼树类
 *
 * @author 江峰
 * @create 2017-11-21
 */
public class HuffmanTree {
    private static final String FILE_MARK = "HF";
    private static final HuffmanEncode HUFFMAN_ENCODE = new HuffmanEncode();
    private static final HuffmanDecode HUFFMAN_DECODE = new HuffmanDecode();

    /**
     * 将文件转换为霍夫曼编码文件
     *
     * @param srcFile      需要编码的文件对象
     * @param destFilePath 编码文件路径
     * @return false 编码失败
     */
    public boolean huffmanEncodeFile(File srcFile, String destFilePath) {
        checkFile(srcFile);
        return HUFFMAN_ENCODE.buffmanEncodeTextFile(srcFile, destFilePath);
    }

    /**
     * 将文件转换为霍夫曼编码文件
     *
     * @param srcFilePath  需要编码的文件路径
     * @param destFilePath 编码文件路径
     * @return false 编码失败
     */
    public boolean huffmanEncodeFile(String srcFilePath, String destFilePath) {
        File srcFile = new File(srcFilePath);
        checkFile(srcFile);
        return huffmanEncodeFile(srcFile, destFilePath);
    }

    /**
     * 解码huffman编码文件
     *
     * @param srcFilePath  编码文件路径
     * @param destFilePath 解码文件路径
     * @return false 解码失败
     */
    public boolean huffmanDecodeFile(String srcFilePath, String destFilePath) {
        return huffmanDecodeFile(new File(srcFilePath), destFilePath);
    }

    /**
     * 解码huffman编码文件
     *
     * @param srcFile      编码文件对象
     * @param destFilePath 解码文件路径
     * @return false 解码失败
     */
    public boolean huffmanDecodeFile(File srcFile, String destFilePath) {
        checkFile(srcFile);
        return HUFFMAN_DECODE.decodeHuffmanFile(srcFile, destFilePath);
    }

    /**
     * 霍夫曼编码文件私有内部类
     *
     * @author 江峰
     * @create 2017-11-20
     */
    private static class HuffmanEncode {
        public HashMap<String, String> encodeMap;
        public List<TreeNode> nodes;
        private TreeNode huffmanTreeRootNode;

        /**
         * huffman编码文本文件
         *
         * @param srcFile      需要编码的文件对象
         * @param destFilePath 编码文件路径
         * @return false 编码失败
         */
        public boolean buffmanEncodeTextFile(File srcFile, String destFilePath) {
            String text = extractTextFileString(srcFile);
            HashMap<String, Integer> weightMap = getTreeNodeWeight(text);
            createHuffmanTree(weightMap);
            breadthFirstSearch();
            createHuffmanEncode();
            writeToHuffmanFile(text, destFilePath);
            return false;
        }

        /**
         * 将文本内容转码写入Huffman文件中
         *
         * @param text         需要写入的文本内容
         * @param destFilePath 写入的文件路径
         * @return false 写入失败
         */
        public boolean writeToHuffmanFile(String text, String destFilePath) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(destFilePath);
                List<byte[]> huffmanDecodeBytes = new ArrayList<byte[]>();
                // 首先写入文件标识
                byte[] bytes = FILE_MARK.getBytes("utf-8");
                huffmanDecodeBytes.add(bytes);
                // 再写入字符种类数量
                bytes = new String(new int[]{encodeMap.size()}, 0, 1).getBytes("utf-8");
                huffmanDecodeBytes.add(bytes);
                // 然后再写入霍夫树解码
                String key = null;
                String value = null;
                for (Map.Entry<String, String> entry : encodeMap.entrySet()) {
                    value = entry.getValue();
                    huffmanDecodeBytes.add(value.getBytes("utf-8"));
                    key = entry.getKey();
                    if (key.equals("0")) {
                        huffmanDecodeBytes.add(new byte[]{0});
                    } else if (key.equals("1")) {
                        huffmanDecodeBytes.add(new byte[]{1});
                    } else {
                        huffmanDecodeBytes.add(key.getBytes("utf-8"));
                    }
                }
                // 最后写入文本信息
                List<Byte> byteList = stringToHuffmanCode(text, encodeMap);
                huffmanDecodeBytes.add(byteListToByteArray(byteList));
                // 开始写入数据
                int size = huffmanDecodeBytes.size();
                for (int i = 0; i < size; i++) {
                    fos.write(huffmanDecodeBytes.get(i));
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }

        /**
         * 字符串转换为Huffman编码
         *
         * @param text      字符串对象
         * @param encodeMap Huffman编码map集合
         * @return 字节列表对象
         */
        public List<Byte> stringToHuffmanCode(String text, HashMap<String, String> encodeMap) {
            List<Byte> bytes = new ArrayList<Byte>();
            int len = text.length();
            String binary = null;
            String str = "";
            byte value = 0;
            for (int i = 0; i < len; i++) {
                int codePoint = text.codePointAt(i);
                binary = new String(new int[]{codePoint}, 0, 1);
                str += encodeMap.get(binary);
                if (str.length() >= 8) {
                    value = (byte) (int) Integer.valueOf(str.substring(0, 8), 2);
                    bytes.add(value);
                    str = str.substring(8);
                }
                if (!Character.isBmpCodePoint(codePoint)) {
                    i++;
                }
            }
            if (str.length() > 0) {
                value = (byte) (int) Integer.valueOf(format(str, true), 2);
                bytes.add(value);
            }
            return bytes;
        }

        /**
         * 将二进制字符串转换为八位二进制字符串
         *
         * @param binaryString 二进制字符串
         * @param direction    false向前添0，true向后添0
         * @return 八位的二进制字符串
         */
        public static String format(String binaryString, boolean direction) {
            // true为向后加，false为向前加
            StringBuilder sb = new StringBuilder(binaryString);
            int len = binaryString.length();
            if (len == 8) {
                return binaryString;
            }
            if (direction) {
                while (len++ < 8) {
                    sb.insert(len - 1, "0");
                }
            } else {
                while (len++ < 8) {
                    sb.insert(0, "0");
                }
            }
            return sb.toString();
        }

        /**
         * 将字节列表转换为字节数组
         *
         * @param byteList 字节列表对象
         * @return 字节数组对象
         */
        public byte[] byteListToByteArray(List<Byte> byteList) {
            int size = byteList.size();
            byte[] bytes = new byte[size];
            for (int i = 0; i < size; i++) {
                bytes[i] = byteList.get(i);
            }
            return bytes;
        }

        /**
         * 创建Huffman编码
         */
        public void createHuffmanEncode() {
            int size = nodes.size();
            encodeMap = new HashMap<String, String>(nodes.size());
            String value = null;
            TreeNode node = null;
            for (int i = 0; i < size; i++) {
                node = nodes.get(i);
                value = node.data.toString();
                encodeMap.put(value, node.code);
            }
        }

        /**
         * 广度优先搜索
         */
        public void breadthFirstSearch() {
            nodes = new ArrayList<TreeNode>();
            Queue<TreeNode> queue = new ArrayDeque<TreeNode>();
            if (huffmanTreeRootNode != null) {
                queue.offer(huffmanTreeRootNode);
            }
            while (!queue.isEmpty()) {
                if (queue.peek().data != null) {
                    nodes.add(queue.peek());
                }
                TreeNode p = queue.poll();
                if (p.leftNode != null) {
                    queue.offer(p.leftNode);
                    p.leftNode.code = p.code + "0";
                }
                if (p.rightNode != null) {
                    queue.offer(p.rightNode);
                    p.rightNode.code = p.code + "1";
                }
            }
        }

        /**
         * 根据权重Map创建霍夫曼树
         *
         * @param weightMap 权重map集合对象
         * @return 霍夫曼树的根节点
         */
        public void createHuffmanTree(HashMap<String, Integer> weightMap) {
            List<TreeNode> treeNodes = new ArrayList<TreeNode>(weightMap.size());
            TreeNode treeNode = null;
            for (Map.Entry<String, Integer> entry : weightMap.entrySet()) {
                treeNode = new TreeNode(entry.getValue(), entry.getKey(), null, null);
                treeNodes.add(treeNode);
            }
            TreeNode leftNode = null;
            TreeNode rightNode = null;
            int leftIndex = 0;
            int rightIndex = 0;
            while (treeNodes.size() > 1) {
                descSort(treeNodes);
                huffmanTreeRootNode = new TreeNode(0, null, null, null);
                leftIndex = treeNodes.size() - 1;
                rightIndex = treeNodes.size() - 2;
                leftNode = treeNodes.get(leftIndex);
                rightNode = treeNodes.get(rightIndex);
                huffmanTreeRootNode.key = leftNode.key + rightNode.key;
                huffmanTreeRootNode.leftNode = leftNode;
                huffmanTreeRootNode.rightNode = rightNode;
                treeNodes.remove(leftIndex);
                treeNodes.remove(rightIndex);
                treeNodes.add(huffmanTreeRootNode);
            }
        }

        /**
         * 节点逆序排序
         *
         * @param treeNodes 树节点列表对象
         */
        private void descSort(List<TreeNode> treeNodes) {
            Collections.sort(treeNodes, new Comparator<TreeNode>() {
                public int compare(TreeNode o1, TreeNode o2) {
                    return o2.key - o1.key;
                }
            });
        }

        /**
         * 获得字符权值
         *
         * @param text 文本字符串
         * @return 文本字符权值map集合
         */
        public HashMap<String, Integer> getTreeNodeWeight(String text) {
            HashMap<String, Integer> weightMap = new HashMap<String, Integer>();
            int count = 0;
            int len = text.length();
            Integer value = 0;
            for (int i = 0; i < len; i++) {
                int codePoint = text.codePointAt(i);
                String key = new String(new int[]{codePoint}, 0, 1);
                value = weightMap.get(value);
                count = (value == null) ? 1 : value + 1;
                weightMap.put(key, count);
                if (!Character.isBmpCodePoint(codePoint)) {
                    i++;
                }
            }
            return weightMap;
        }

        /**
         * 提取文本内容
         *
         * @param textFile
         * @return
         */
        public String extractTextFileString(File textFile) {
            // 首先判断文件是否为UTF-8编码
            if (!isUtf8Encode(textFile)) {
                throw new FileTypeException("该文件不为UTF-8编码！");
            }
            StringBuilder sb = new StringBuilder();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(textFile));
                String lineString = "";
                while ((lineString = br.readLine()) != null) {
                    sb.append(lineString + "\r\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }

        /**
         * 判断文件是否为UTF-8编码
         *
         * @param file 文件对象
         * @return true 是UTF-8编码
         * @throws FileTypeException 如果文本全为ASCII字符，无法判断其编码类型。
         */
        public boolean isUtf8Encode(File file) {
            if (!file.exists() || !file.isFile()) {
                throw new FileTypeException(file.getAbsolutePath());
            }
            int asciiCount = 0;
            long len = file.length();
            BufferedInputStream bis = null;
            try {
                bis = new BufferedInputStream(new FileInputStream(file));
                int index = 0;
                // 为了兼容微软文本编辑器的BOM(EF BB BF)
                if (bis.markSupported()) {
                    bis.mark(index);
                    if (bis.read() == 0xEF && bis.read() == 0xBB && bis.read() == 0xBF) {
                        return true;
                    } else {
                        bis.reset();
                    }
                }
                while (index < len) {
                    int value = bis.read();
                    if (value >= 192 && value < 224) {
                        if (index + 1 >= len || judge(bis.read())) {
                            return false;
                        } else {
                            index += 2;
                            continue;
                        }
                    } else if (value >= 224 && value < 240) {
                        if (index + 2 >= len || (judge(bis.read()) && judge(bis.read()))) {
                            return false;
                        } else {
                            index += 3;
                            continue;
                        }
                    } else if (value >= 240) {
                        if (index + 3 >= len || (judge(bis.read()) && judge(bis.read()) && judge(bis.read()))) {
                            return false;
                        } else {
                            index += 4;
                            continue;
                        }
                    } else {
                        index++;
                        asciiCount++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (asciiCount == len) {
                throw new FileTypeException("文件字符全是ASCII类型，无法判断文件是否是UTF-8编码！");
            }
            return true;
        }

        /**
         * 判断UTF-8的辅助函数
         *
         * @param value
         * @return
         */
        public boolean judge(int value) {
            if (value < 128 && value >= 192) {
                return false;
            }
            return true;
        }
    }

    /**
     * 解码霍夫曼文件私有内部类
     *
     * @author 江峰
     * @create 2017-11-20
     */
    private static class HuffmanDecode {
        private static final int[] UTF_8_TABLE = {0, 192, 224, 240};
        private static int index = 0;

        public boolean decodeHuffmanFile(File srcFile, String destFilePath) {
            byte[] fileBytes = getFileBytes(srcFile);
            if (!getFileMark(fileBytes).equals(FILE_MARK)) {
                throw new FileTypeException("该文件不是Huffman文件！");
            }
            int size = getHuffmanSize(fileBytes);
            HashMap<String, String> decodeMap = getHuffmanDecodeMap(fileBytes, size);
            List<String> decodeDatas = decodeHuffmanData(extractHuffmanData(fileBytes), decodeMap);
            return writeToFile(decodeDatas, destFilePath);
        }

        /**
         * 将解码结果写入文件
         *
         * @param decodeDatas 解码字符串列表
         * @param file        解码文件
         * @return
         */
        public boolean writeToFile(List<String> decodeDatas, String destFilePath) {
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(destFilePath));
                for (String data : decodeDatas) {
                    bw.write(data);
                }
                bw.flush();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        }

        /**
         * 提取Huffman编码文件数据
         *
         * @param fileBytes 文件字节数组
         * @return 编码字符路径二进制字符串列表
         */
        public List<String> extractHuffmanData(byte[] fileBytes) {
            int len = fileBytes.length;
            List<String> data = new ArrayList<String>(len - index);
            index--;
            while (++index < len) {
                int b = fileBytes[index] & 0xFF;
                data.add(format(Integer.toBinaryString(b), false));
            }
            return data;
        }

        /**
         * 解码字符路径二进制字符串
         *
         * @param data      二进制字符串列表
         * @param decodeMap
         * @return
         */
        public static List<String> decodeHuffmanData(List<String> data, HashMap<String, String> decodeMap) {
            int size = data.size();
            int index = 0;
            String content = "";
            List<String> decodeDataStrings = new ArrayList<String>();
            int beginIndex = 0;
            int endIndex = 1;
            String temp = null;
            int len = 0;
            while (index < size) {
                content += data.get(index);
                len = content.length();
                while (endIndex <= len) {
                    temp = content.substring(beginIndex, endIndex);
                    if (decodeMap.containsKey(temp)) {
                        decodeDataStrings.add(decodeMap.get(temp));
                        if (endIndex == len) {
                            beginIndex = 0;
                            endIndex = 1;
                            content = "";
                            break;
                        } else {
                            beginIndex = endIndex;
                            endIndex++;
                        }
                    } else {
                        if (endIndex == len) {
                            content = temp;
                            endIndex = len - 1 - beginIndex;
                            beginIndex = 0;
                            break;
                        } else {
                            endIndex++;
                        }
                    }
                }
                index++;
            }
            return decodeDataStrings;
        }

        /**
         * 将二进制字符串转换为八位二进制字符串
         *
         * @param binaryString 二进制字符串
         * @param direction    false向前添0，true向后添0
         * @return 八位的二进制字符串
         */
        public static String format(String binaryString, boolean direction) {
            // true为向后加，false为向前加
            StringBuilder sb = new StringBuilder(binaryString);
            int len = binaryString.length();
            if (len == 8) {
                return binaryString;
            }
            if (direction) {
                while (len++ < 8) {
                    sb.insert(len - 1, "0");
                }
            } else {
                while (len++ < 8) {
                    sb.insert(0, "0");
                }
            }
            return sb.toString();
        }

        /**
         * 获得霍夫曼解码map对象
         *
         * @param fileBytes 文件字节数组
         * @param size      字符编码数量
         * @return 霍夫曼树解码map对象
         */
        public HashMap<String, String> getHuffmanDecodeMap(byte[] fileBytes, int size) {
            HashMap<String, String> map = new HashMap<String, String>(size);
            int value = 0;
            while (size-- > 0) {
                String key = getUtf8String(fileBytes);
                value = getUtf8Value(fileBytes);
                if (value == 0) {
                    map.put(key, "0");
                } else if (value == 1) {
                    map.put(key, "1");
                } else {
                    map.put(key, new String(new int[]{value}, 0, 1));
                }
            }
            return map;
        }

        public String getUtf8String(byte[] fileBytes) {
            StringBuilder sb = new StringBuilder();
            int value = 0;
            int initialIndex = index;
            while (true) {
                value = getUtf8Value(fileBytes);
                if (value == 48) {
                    sb.append("0");
                } else if (value == 49) {
                    sb.append("1");
                } else {
                    break;
                }
                initialIndex++;
            }
            index = initialIndex;
            return sb.toString();
        }

        /**
         * 获得Huffman编码字符数
         *
         * @param fileBytes 文件字节数组对象
         * @return 编码字符的数量
         */
        public int getHuffmanSize(byte[] fileBytes) {
            return getUtf8Value(fileBytes);
        }

        /**
         * 获得utf-8字节数值
         *
         * @param fileBytes 文件字节数组对象
         * @return 文件字节数组索引值处的数值
         */
        public int getUtf8Value(byte[] fileBytes) {
            int value = fileBytes[index] & 0xFF;
            int size = -1;
            while (++size < 4 && (value >= UTF_8_TABLE[size])) ;
            size--;
            String firstBinary = null;
            String secondBinary = null;
            String thirdBinary = null;
            String fourthBinary = null;
            switch (size) {
                case 0:
                    break;
                case 1:
                    firstBinary = Integer.toBinaryString(((fileBytes[index] & 0xFF) & 31));
                    secondBinary = format(Integer.toBinaryString(((fileBytes[index + 1] & 0xFF) & 63)));
                    value = Integer.valueOf(firstBinary + secondBinary, 2);
                    break;
                case 2:
                    firstBinary = Integer.toBinaryString(((fileBytes[index] & 0xFF) & 15));
                    secondBinary = format(Integer.toBinaryString(((fileBytes[index + 1] & 0xFF) & 63)));
                    thirdBinary = format(Integer.toBinaryString(((fileBytes[index + 2] & 0xFF) & 63)));
                    value = Integer.valueOf(firstBinary + secondBinary + thirdBinary, 2);
                    break;
                case 3:
                    firstBinary = Integer.toBinaryString(((fileBytes[index] & 0xFF) & 7));
                    secondBinary = format(Integer.toBinaryString(((fileBytes[index + 1] & 0xFF) & 63)));
                    thirdBinary = format(Integer.toBinaryString(((fileBytes[index + 2] & 0xFF) & 63)));
                    fourthBinary = format(Integer.toBinaryString(((fileBytes[index + 3] & 0xFF) & 63)));
                    value = Integer.valueOf(firstBinary + secondBinary + thirdBinary + fourthBinary, 2);
                    break;
            }
            index = index + size + 1;
            return value;
        }

        /**
         * 将二进制字符串转换为6位二进制字符串
         *
         * @param binaryString 二进制字符串对象
         * @return 6位二进制字符串
         */
        public String format(String binaryString) {
            StringBuilder sb = new StringBuilder(binaryString);
            int len = binaryString.length();
            if (len == 6) {
                return binaryString;
            }
            while (len++ < 6) {
                sb.insert(0, "0");
            }
            return sb.toString();
        }

        /**
         * 获得文件的文件标识
         *
         * @param fileBytes 编码文件对象
         * @return 文件标识字符串
         */
        public String getFileMark(byte[] fileBytes) {
            return new String(new byte[]{fileBytes[index++], fileBytes[index++]});
        }

        /**
         * 获得文件字节数组
         *
         * @param file 文件对象
         * @return 文件字节数组对象
         */
        public byte[] getFileBytes(File file) {
            FileInputStream fis = null;
            byte[] fileBytes = null;
            try {
                fis = new FileInputStream(file);
                long fileLength = file.length();
                fileBytes = new byte[(int) fileLength];
                fis.read(fileBytes);
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return fileBytes;
        }
    }

    /**
     * 检查需要编码的文件是否存在和文件类型是否正确
     *
     * @param file 需要检验的文件对象
     */
    private void checkFile(File file) {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new FileTypeException("文件类型错误！");
        }
    }

}

/**
 * 霍夫曼树节点类
 *
 * @author 江峰
 * @create 2017-11-21
 */
class TreeNode {
    public TreeNode leftNode;
    public TreeNode rightNode;
    public int key;
    public Object data;
    public String code = "";

    public TreeNode(int key, Object data, TreeNode leftNode, TreeNode rightNode) {
        this.key = key;
        this.data = data;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }
}

/**
 * 文件类型异常类
 *
 * @author 江峰
 * @create 2017-11-21
 */
class FileTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileTypeException(String s) {
        super(s);
    }
}

/**
 * 没找到文件异常类
 *
 * @author 江峰
 * @create 2017-11-21
 */
class FileNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileNotFoundException(String s) {
        super(s);
    }
}

/**
 * 霍夫曼编码解码演示类
 *
 * @author 江峰
 * @create 2017-11-21
 */
class HuffmanTreeDemo {
    public static void main(String[] args) throws Exception {
        String srcFilePath = "C:\\Users\\江峰\\Desktop\\Huffman.txt";
        String encodeFilePath = "C:\\Users\\江峰\\Desktop\\encode.hf";
        String decodeFilePath = "C:\\Users\\江峰\\Desktop\\decode.txt";
        HuffmanTree tree = new HuffmanTree();
        System.out.println(tree.huffmanEncodeFile(srcFilePath, encodeFilePath));
        System.out.println(tree.huffmanDecodeFile(encodeFilePath, decodeFilePath));
        System.exit(0);
    }
}


