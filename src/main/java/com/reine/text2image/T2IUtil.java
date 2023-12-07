package com.reine.text2image;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * 文生图工具类
 *
 * @author reine
 */
@SuppressWarnings("MagicConstant")
public class T2IUtil {

    private final T2IConstant constant;

    private Font font;

    public T2IUtil(T2IConstant constant) {
        this.constant = constant;
    }

    /**
     * 判断平台原生是否安装了此字体
     * @param fontName 字体名称
     * @return 是否支持
     */
    public boolean isPlatformSupportFont(String fontName){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        List<String> list = Arrays.asList(fontNames);
        return list.stream().anyMatch(s -> s.contains(fontName));
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

    /**
     * 换行
     *
     * @param line 预处理字符串
     * @return 处理后的字符串
     */
    private String lineBreak(String line) {
        constant.setLineCharCountMax(0);
        StringBuilder result = new StringBuilder();
        int width = 0;
        for (char c : line.toCharArray()) {
            if (String.valueOf(c).getBytes().length == 3) {  // 中文字符
                if (width + 1 == constant.getLineCharCount()) {  // 剩余位置不够一个汉字
                    width = 2;
                    result.append('\n').append(c);
                } else {  // 中文宽度加2，注意换行边界
                    width += 2;
                    result.append(c);
                }
            } else {
                if (c == '\t') {
                    int spaceCount = constant.getTableWidth() - width % constant.getTableWidth();  // 已有长度对TABLE_WIDTH取余
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

            if (width >= constant.getLineCharCount()) {
                result.append('\n');
                width = 0;
                constant.setLineCharCountMax(constant.getLineCharCount());
            }
            if (width > constant.getLineCharCountMax()) {
                constant.setLineCharCountMax(width);
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
    public ByteArrayOutputStream drawImageToByteArrayOutputStream(String msg) {
        if (font == null) {
            font = new Font(
                    constant.getFontName(),
                    constant.getFontPlain(),
                    constant.getCharSize()
            );
        }
        constant.setLineCharCountMax(0);
        String outputStr = lineBreak(msg);
        int lines = outputStr.split("\n").length;

        int imageWidth = constant.getLineCharCountMax() * constant.getCharSize() / 2 + 84;
        int lineHeight = constant.getCharSize() + constant.getLineSpacing();
        int imageHeight = lineHeight * lines + 84;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(constant.getBackgroudColor());
        graphics2D.fillRect(0, 0, imageWidth, imageHeight);
        graphics2D.setColor(constant.getFontColor());
        graphics2D.setFont(font);

        String[] linesArray = outputStr.split("\n");
        // 文本距离顶部的高度
        int y = 40 + lineHeight;
        for (String line : linesArray) {
            graphics2D.drawString(line, 42, y);
            y += lineHeight;
        }
        graphics2D.setColor(constant.getRectColor());
        graphics2D.drawRect(10, 10, constant.getLineCharCountMax() * constant.getCharSize() / 2 + 62, lineHeight * lines + 62);
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
