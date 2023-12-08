package com.reine.text2image;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author reine
 */
@Slf4j
public class T2ITests {

    private T2IUtil t2IUtil;

    @SneakyThrows
    @Test
    void text_to_image_test() {
        String msg = """
                帮助
                1. echo
                2. ping
                """.indent(0);
        Path path = Path.of("input.jpg");
        File file;
        if (!Files.exists(path)) file = Files.createFile(path).toFile();
        else file = path.toFile();
        if (t2IUtil.storeImageAfterGenerateTextImage(file, msg)) {
            log.info("写入成功");
        } else log.error("写入失败");
    }

    @BeforeEach
    void set_font_file() {
        String path = getClass().getResource("/font/SourceHanSansCN-Medium.otf").getPath();
        path = path.substring(1);
        File file = Paths.get(path).toFile();
        // 设置图片参数
        T2IConstant constant = T2IConstant.builder().build();
        t2IUtil = new T2IUtil(constant);
        t2IUtil.useCustomFont(file);
    }
}
