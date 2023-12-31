package com.reine.text2image;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author reine
 */
@Slf4j
public class T2ITests {

    private T2IUtil t2IUtil;

    @SneakyThrows
    @Test
    void text_to_image_test() {
        String msg = "アドバイス\n" +
                "Advice\n" +
                "建议";
        Path path = Paths.get("input.jpg");
        File file;
        if (!Files.exists(path)) file = Files.createFile(path).toFile();
        else file = path.toFile();
        assertTrue(t2IUtil.storeImageAfterGenerateTextImage(file, msg));
    }

    @SneakyThrows
    @Test
    void text_to_image_convert_to_base64_test() {
        String msg = "アドバイス\n" +
                "Advice\n" +
                "建议";
        String result = t2IUtil.drawImageToBase64(msg);
        assertTrue(result.startsWith("base64://"));
    }

    /**
     * 设置第三方字体
     */
    @BeforeEach
    void set_font_file() {
        String path = Objects.requireNonNull(getClass().getResource("/font/SourceHanSansCN-Medium.otf")).getPath();
        path = path.substring(1);
        File file = Paths.get(path).toFile();
        // 设置图片参数
        T2IConstant constant = T2IConstant.builder().build();
        t2IUtil = new T2IUtil(constant);
        t2IUtil.useCustomFont(file);
    }
}
