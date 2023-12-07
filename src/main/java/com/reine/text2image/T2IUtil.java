package com.reine.text2image;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import static com.reine.text2image.T2IConstant.*;

/**
 * 文生图工具类
 *
 * @author reine
 */
public class T2IUtil {


    private static Font font = new Font(FONT_NAME, FONT_PLAIN, CHAR_SIZE);

    /**
     * 使用第三方字体
     * @param fontFile 字体文件
     */
    @SneakyThrows
    public static void useCustomFont(File fontFile) {
        // 创建字体对象
        Font jetbrainsMonoFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        // 设置字体大小和样式
        int fontStyle = Font.PLAIN;
        int fontSize = 12;
        font = jetbrainsMonoFont.deriveFont(fontStyle, fontSize);
    }

    /**
     * 换行
     *
     * @param line 预处理字符串
     * @return 处理后的字符串
     */
    private static String lineBreak(String line) {
        LINE_CHAR_COUNT_MAX = 0;
        StringBuilder result = new StringBuilder();
        int width = 0;
        for (char c : line.toCharArray()) {
            if (String.valueOf(c).getBytes().length == 3) {  // 中文字符
                if (width + 1 == LINE_CHAR_COUNT) {  // 剩余位置不够一个汉字
                    width = 2;
                    result.append('\n').append(c);
                } else {  // 中文宽度加2，注意换行边界
                    width += 2;
                    result.append(c);
                }
            } else {
                if (c == '\t') {
                    int spaceCount = TABLE_WIDTH - width % TABLE_WIDTH;  // 已有长度对TABLE_WIDTH取余
                    result.append(" ".repeat(spaceCount));
                    width += spaceCount;
                } else if (c == '\n') {
                    width = 0;
                    result.append(c);
                } else {
                    width++;
                    result.append(c);
                }
            }

            if (width >= LINE_CHAR_COUNT) {
                result.append('\n');
                width = 0;
                LINE_CHAR_COUNT_MAX = LINE_CHAR_COUNT;
            }
            if (width > LINE_CHAR_COUNT_MAX) {
                LINE_CHAR_COUNT_MAX = width;
            }
        }

        if (result.toString().endsWith("\n")) {
            return result.toString();
        }
        return result + "\n";
    }


    /**
     * 根据文本绘制图片
     *
     * @param msg 文本
     * @return 字节数组输出流
     */
    @SneakyThrows(IOException.class)
    public static ByteArrayOutputStream drawImageToByteArrayOutputStream(String msg) {
        LINE_CHAR_COUNT_MAX = 0;
        String outputStr = lineBreak(msg);
        int lines = outputStr.split("\n").length;

        int imageWidth = LINE_CHAR_COUNT_MAX * CHAR_SIZE / 2 + 84;
        int imageHeight = CHAR_SIZE_H * lines + 84;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(BACKGROUD_COLOR);
        graphics2D.fillRect(0, 0, imageWidth, imageHeight);
        graphics2D.setColor(FONT_COLOR);
        graphics2D.setFont(font);

        String[] linesArray = outputStr.split("\n");
        // 文本距离顶部的高度
        int y = 40 + CHAR_SIZE_H;
        for (String line : linesArray) {
            graphics2D.drawString(line, 42, y);
            y += CHAR_SIZE_H;
        }
        graphics2D.setColor(RECT_COLOR);
        graphics2D.drawRect(10, 10, LINE_CHAR_COUNT_MAX * CHAR_SIZE / 2 + 62, CHAR_SIZE_H * lines + 62);
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
    public static String drawImageToBase64(String msg) {
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
    public static boolean storeImageAfterGenerateTextImage(File file, String msg) {
        ByteArrayOutputStream outputStream = drawImageToByteArrayOutputStream(msg);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            outputStream.writeTo(fileOutputStream);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
