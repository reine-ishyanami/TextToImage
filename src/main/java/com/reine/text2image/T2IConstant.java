package com.reine.text2image;

import java.awt.*;

/**
 * @author reine
 */
public class T2IConstant {
    /**
     * 每行字符数：30个中文字符(=60英文字符)
     */
    static final int LINE_CHAR_COUNT = 30 * 2;
    /**
     * 字符大小
     */
    static final int CHAR_SIZE = 32;
    /**
     * 字符高度
     */
    static final int CHAR_SIZE_H = 47;
    /**
     * tab占用的长度
     */
    static final int TABLE_WIDTH = 4;

    /**
     * 字体
     */
    static final String FONT_NAME = "JetBrains Mono";
    /**
     * 字体样式
     */
    static final int FONT_PLAIN = Font.PLAIN;
    /**
     * 每行最大字符数
     */
    static int LINE_CHAR_COUNT_MAX = 0;
    /**
     * 图片背景颜色
     */
    static Color BACKGROUD_COLOR = new Color(255, 252, 245);
    /**
     * 字体颜色
     */
    static Color FONT_COLOR = new Color(125, 101, 89);
    /**
     * 边框颜色
     */
    static Color RECT_COLOR = new Color(220, 211, 196);

}
