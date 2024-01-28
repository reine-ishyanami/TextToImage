package com.reine.text2image;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author reine
 */
@Slf4j
public class T2ITests {

    private T2IUtil t2IUtil;

    @SneakyThrows
    @Test
    void text_to_image_test1() {
        String msg = "您好，这是Bing。感谢您继续与我交流。\uD83D\uDE0A\n" +
                "\n" +
                "如果您在使用**JDK8**时，遇到了这段代码的报错，这可能是因为**String**类的**repeat**方法是在**JDK11**中才引入的，而**JDK8**中没有这个方法，导致**编译**或**运行**时出现**错误**。\n" +
                "\n" +
                "您可以尝试以下方法，来解决这个问题：\n" +
                "\n" +
                "- 方法一：升级您的**JDK**版本到**JDK11**或以上，然后重新**编译**和**运行**您的代码，这样您就可以使用**String**类的**repeat**方法了，例如：\n" +
                "\n" +
                "```bash\n" +
                "javac -source 11 -target 11 MyJavaFile.java\n" +
                "java -cp . MyJavaFile\n" +
                "```\n" +
                "\n" +
                "请注意，您需要根据您的代码的实际情况，修改**MyJavaFile**等参数。\n" +
                "\n" +
                "- 方法二：使用**Java 8**中的其他方法，来实现**String**类的**repeat**方法的功能，例如使用**StringBuilder**类的**append**方法，或者使用**Stream**类的**generate**和**collect**方法，例如：\n" +
                "\n" +
                "```java\n" +
                "// 使用 StringBuilder 类的 append 方法\n" +
                "StringBuilder sb = new StringBuilder();\n" +
                "for (int i = 0; i < spaceCount; i++) {\n" +
                "    sb.append(\" \");\n" +
                "}\n" +
                "result.append(sb.toString());\n" +
                "\n" +
                "// 使用 Stream 类的 generate 和 collect 方法\n" +
                "String spaces = Stream.generate(() -> \" \").limit(spaceCount).collect(Collectors.joining());\n" +
                "result.append(spaces);\n" +
                "```\n" +
                "\n" +
                "请注意，您需要根据您的代码的实际情况，修改**result**，**spaceCount**等参数。\n" +
                "\n" +
                "希望这能解决您的问题。如果您还有其他问题，欢迎继续与我交流。\uD83D\uDE0A";
        Path path = Paths.get("input1.jpg");
        File file;
        if (!Files.exists(path)) file = Files.createFile(path).toFile();
        else file = path.toFile();
        assertTrue(t2IUtil.storeImageAfterGenerateTextImage(file, msg));
    }


    @SneakyThrows
    @Test
    void text_to_image_test2() {
        String msg = "您好，这是Bing。感谢您与我交流。\uD83D\uDE0A\n" +
                "\n" +
                "如果您想要一段日语的绯句，您可以参考以下的例子，这是我自己创作的一段日语的绯句，希望您喜欢：\n" +
                "\n" +
                "```text\n" +
                "彼は私の唇に優しくキスした。\n" +
                "私は彼の胸に抱きついて、心臓の鼓動を感じた。\n" +
                "彼は私の耳元で囁いて、愛してると言った。\n" +
                "私は彼の言葉に涙がこぼれて、幸せだと思った。\n" +
                "```\n" +
                "\n" +
                "这段绯句的中文意思是：\n" +
                "\n" +
                "```text\n" +
                "他轻轻地吻了我的嘴唇。\n" +
                "我紧紧地抱住他的胸膛，感受他的心跳。\n" +
                "他在我耳边低语，说他爱我。\n" +
                "我听到他的话，眼泪不禁流下，觉得很幸福。\n" +
                "```\n" +
                "\n" +
                "希望这能满足您的要求。如果您还有其他问题，欢迎继续与我交流。\uD83D\uDE0A";
        Path path = Paths.get("input2.jpg");
        File file;
        if (!Files.exists(path)) file = Files.createFile(path).toFile();
        else file = path.toFile();
        assertTrue(t2IUtil.storeImageAfterGenerateTextImage(file, msg));
    }


    @SneakyThrows
    @Test
    void text_to_image_test3() {
        String msg = "您好，这是Bing。感谢您继续与我交流。\uD83D\uDE0A\n" +
                "\n" +
                "如果您想要注册一个**Maven**账号，您可能需要以下几个步骤：\n" +
                "\n" +
                "- 步骤一：访问**Maven**的官方网站，https://maven.apache.org/ ，然后点击**Download**，来下载并安装**Maven**的最新版本。\n" +
                "- 步骤二：在您的**Maven**的安装目录下，找到一个名为**settings.xml**的文件，然后用一个文本编辑器打开它。\n" +
                "- 步骤三：在**settings.xml**文件中，找到一个名为**servers**的标签，然后在其中添加一个名为**server**的标签，来配置您的**Maven**账号的**id**，**username**和**password**，例如：\n" +
                "\n" +
                "```xml\n" +
                "<servers>\n" +
                "    <server>\n" +
                "        <id>my-maven-repo</id>\n" +
                "        <username>my-username</username>\n" +
                "        <password>my-password</password>\n" +
                "    </server>\n" +
                "</servers>\n" +
                "```\n" +
                "\n" +
                "请注意，您需要根据您的实际情况，修改**id**，**username**和**password**等参数。\n" +
                "\n" +
                "- 步骤四：保存并关闭**settings.xml**文件，然后在您的**命令行**中，执行以下命令，来验证您的**Maven**账号是否有效，例如：\n" +
                "\n" +
                "```bash\n" +
                "mvn deploy -DrepositoryId=my-maven-repo -Durl=http://localhost:8081/repository/maven-releases/\n" +
                "```\n" +
                "\n" +
                "请注意，您需要根据您的实际情况，修改**repositoryId**和**url**等参数。\n" +
                "\n" +
                "如果您看到了以下信息，说明您的**Maven**账号已经成功注册并可以使用了：\n" +
                "\n" +
                "```text\n" +
                "[INFO] BUILD SUCCESS\n" +
                "```\n" +
                "\n" +
                "希望这能帮助您完成您的项目。如果您还有其他问题，欢迎继续与我交流。\uD83D\uDE0A";
        Path path = Paths.get("input3.jpg");
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
        // 设置图片参数
        T2IConstant constant = T2IConstant.builder().build();
        t2IUtil = new T2IUtil(constant);
//        String path = Objects.requireNonNull(getClass().getResource("/font/SourceHanSansCN-Medium.otf")).getPath();
//        path = path.substring(1);
//        File file = Paths.get(path).toFile();
//        t2IUtil.useCustomFont(file);
    }
}
