package org.hope6537.note.design.interpreter.math;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * 模拟算数运算——解析器模式
 */
public class Client {

    public static void main(String[] args) throws IOException {
        String expr = getExpStr();
        HashMap<String, Integer> value = getValue(expr);

        Calculator calculator = new Calculator(expr);
        System.out.println("运算结果为" + expr + "=" + calculator.run(value));

    }

    /**
     * 获得表达式
     */
    public static String getExpStr() throws IOException {
        System.out.println("请输入表达式:");
        return (new BufferedReader(new InputStreamReader(System.in))).readLine();
    }

    /**
     * 获得值映射
     */
    public static HashMap<String, Integer> getValue(String expr) throws IOException {
        HashMap<String, Integer> map = new HashMap<>();
        for (char ch : expr.toCharArray()) {
            if (ch != '+' && ch != '-') {
                //解决重复参数问题,参数相同值都相同
                if (!map.containsKey(String.valueOf(ch))) {
                    System.out.printf("请输入%s的值: ", ch);
                    String in = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    map.put(String.valueOf(ch), Integer.parseInt(in));
                }
            }
        }
        return map;
    }

}
