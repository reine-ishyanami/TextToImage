package com.reine.text2image;

import lombok.Data;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 文生图工具类
 *
 * @author reine
 */
@SuppressWarnings("MagicConstant")
public class T2IUtil {

    private final T2IConstant constant;

    private Font font;

    /**
     * 行最大宽度，ASCII码算1，非ASCII码算2，每行总和取最大
     */
    private int lineCharCountMax = 0;

    public T2IUtil(T2IConstant constant) {
        this.constant = constant;
    }

    /**
     * 判断平台原生是否安装了此字体
     *
     * @param fontName 字体名称
     * @return 如果支持此字体，则返回该字体名称，否则返回""
     */
    public String isPlatformSupportFont(String fontName) {
        if (font == null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fontArray = ge.getAvailableFontFamilyNames();
            for (String font : fontArray)
                if (font.contains(fontName))
                    return font;
            return "";
        } else
            return font.getFontName().contains(fontName) ? font.getFontName() : "";
    }


    /**
     * 使用第三方字体
     *
     * @param fontFile 字体文件
     */
    @SneakyThrows
    public void useCustomFont(File fontFile) {
        // 创建字体对象
        Font jetbrainsMonoFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        // 设置字体大小和样式
        font = jetbrainsMonoFont.deriveFont(constant.getFontPlain(), constant.getCharSize());
    }

    @Data
    private static class LineBreakResult {
        String resultLine;
        String maxLine;
    }

    /**
     * 换行
     *
     * @param line 预处理字符串
     * @return 处理后的字符串
     */
    private LineBreakResult lineBreak(String line) {
        LineBreakResult lineBreakResult = new LineBreakResult();
        lineCharCountMax = 0;
        StringBuilder result = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();
        int width = 0;
        for (char c : line.toCharArray()) {
            // ascii 字符处理
            if (String.valueOf(c).getBytes().length == 1) {
                if (c == '\t') {
                    int spaceCount = constant.getTableWidth() - width % constant.getTableWidth();  // 已有长度对TABLE_WIDTH取余
                    for (int i = 0; i < spaceCount; i++) result.append(" ");  // 填充指定数量的空格
                    width += spaceCount;
                    currentLine.append(c);
                } else if (c == '\n') {
                    width = 0;
                    result.append(c);
                    currentLine = new StringBuilder();
                } else {
                    width++;
                    result.append(c);
                    currentLine.append(c);
                }
            } else {  // 非ascii字符处理
                if (width + 1 == constant.getLineCharCount()) {  // 剩余位置不够一个汉字
                    width = 2;
                    result.append('\n').append(c);  // 换行
                    currentLine = new StringBuilder(String.valueOf(c));
                } else {  // 中文宽度加2，注意换行边界
                    width += 2;
                    result.append(c);
                    currentLine.append(c);
                }
            }

            // 判断是否需要插入换行符
            if (width == constant.getLineCharCount()) {
                result.append('\n');
                width = 0;
                // 如果需要插入换行符，则表示此时行宽以到达最大宽度
                lineCharCountMax = constant.getLineCharCount();
                lineBreakResult.setMaxLine(currentLine.toString());
            }
            if (width > lineCharCountMax) {
                lineCharCountMax = width;
                lineBreakResult.setMaxLine(currentLine.toString());
            }
        }

        // 保证字符串最后是换行符
        if (!result.toString().endsWith("\n")) {
            result.append("\n");
        }
        lineBreakResult.setResultLine(result.toString());
        return lineBreakResult;
    }


    /**
     * 根据文本绘制图片
     *
     * @param msg 文本
     * @return 字节数组输出流
     */
    @SneakyThrows(IOException.class)
    public ByteArrayOutputStream drawImageToByteArrayOutputStream(String msg) {
        // 如果没有自定义字体文件，则根据参数创建字体
        if (font == null) {
            font = new Font(
                    constant.getFontName(),
                    constant.getFontPlain(),
                    constant.getCharSize()
            );
        }
        // 行最大字符数，ASCII字符占一格，非ASCII字符占两格
        lineCharCountMax = 0;
        LineBreakResult outputStr = lineBreak(msg);
        int lines = outputStr.getResultLine().split("\n").length;

        // 计算最长行的宽度，然后重建画布
        int maxLineWidth;
        {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = image.createGraphics();
            graphics2D.setFont(font);
            maxLineWidth = graphics2D.getFontMetrics().stringWidth(outputStr.getMaxLine());
        }

        int imageWidth = maxLineWidth + 50;
        int lineHeight = constant.getCharSize() + constant.getLineSpacing();
        int imageHeight = lineHeight * lines + 50;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        // 抗锯齿
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(constant.getBackgroundColor());
        // 背景颜色
        graphics2D.fillRect(0, 0, imageWidth, imageHeight);
        graphics2D.setColor(constant.getFontColor());
        graphics2D.setFont(font);

        String[] linesArray = outputStr.getResultLine().split("\n");
        // 文本距离顶部的高度
        int y = 25 + lineHeight;
        // 文本
        for (String line : linesArray) {
            graphics2D.drawString(line, 25, y);
            y += lineHeight;
        }
        // 边框
        if (constant.isBorder()) {
            graphics2D.setStroke(new BasicStroke(constant.getBorderWidth()));
            graphics2D.setColor(constant.getRectColor());
            graphics2D.drawRect(10, 10, maxLineWidth + 30, lineHeight * lines + 30);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", outputStream);
        return outputStream;
    }

    /**
     * 根据文本绘制图片
     *
     * @param msg 文本
     * @return 图片base64编码字符串，格式为base64://xxx
     */
    public String drawImageToBase64(String msg) {
        ByteArrayOutputStream outputStream = drawImageToByteArrayOutputStream(msg);
        byte[] byteArray = outputStream.toByteArray();
        return "base64://" + Base64.getEncoder().encodeToString(byteArray);
    }


    /**
     * 将生成好的文字图片流保存到本地文件
     *
     * @param file 本地文件
     * @param msg  要进行文转图的文本
     * @return 是否成功
     */
    public boolean storeImageAfterGenerateTextImage(File file, String msg) {
        ByteArrayOutputStream outputStream = drawImageToByteArrayOutputStream(msg);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            outputStream.writeTo(fileOutputStream);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
