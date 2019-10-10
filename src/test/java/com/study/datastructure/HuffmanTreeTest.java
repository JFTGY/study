package com.study.datastructure;

import org.junit.Test;

/**
 * @author : JF
 * Date    : 2019.10.10
 * Time    : 19:59
 * Email   ：tangqibao_620@163.com
 */
public class HuffmanTreeTest {
    @Test
    public void test() {
        String srcFilePath = "Huffman.txt";
        String encodeFilePath = "encode.hf";
        String decodeFilePath = "decode.txt";
        HuffmanTree tree = new HuffmanTree();
        System.out.println(tree.huffmanEncodeFile(srcFilePath, encodeFilePath));
        System.out.println(tree.huffmanDecodeFile(encodeFilePath, decodeFilePath));
    }
}