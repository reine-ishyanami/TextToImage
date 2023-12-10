package com.reine.text2image;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

/**
 * @author reine
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class T2IConstant {

    /**
     * 每行最大字符数：30个中文字符(=60英文字符)
     */
    @Builder.Default
    private int lineCharCount = 30 * 2;

    /**
     * 字符大小
     */
    @Builder.Default
    private int charSize = 32;

    /**
     * 行间距
     */
    @Builder.Default
    private int lineSpacing = 5;

    /**
     * 制表符宽度
     */
    @Builder.Default
    private int tableWidth = 4;

    /**
     * 字体
     */
    @Builder.Default
    private String fontName = "宋体";

    /**
     * 字体样式
     */
    @Builder.Default
    private int fontPlain = Font.PLAIN;

    /**
     * 是否生成边框
     */
    @Builder.Default
    private boolean border = true;

    /**
     * 边框宽度
     */
    @Builder.Default
    private int borderWidth = 1;

    /**
     * 图片背景颜色
     */
    @Builder.Default
    private Color backgroundColor = new Color(255, 252, 245);

    /**
     * 字体颜色
     */
    @Builder.Default
    private Color fontColor = new Color(125, 101, 89);

    /**
     * 边框颜色
     */
    @Builder.Default
    private Color rectColor = new Color(220, 211, 196);

}
