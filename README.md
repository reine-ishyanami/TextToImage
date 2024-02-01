# 文本转图片

## 使用方法

1. 引入依赖（将jar包下载后放在项目目录下的`lib`文件夹下）

   ```xml
   <!-- 引入jitpack仓库  -->
   <repositories>
       <repository>
           <id>jitpack.io</id>
           <url>https://jitpack.io</url>
       </repository>
   </repositories>
   <dependencies>
       <dependency>
           <groupId>com.github.reine-ishyanami</groupId>
           <artifactId>TextToImage</artifactId>
           <version>0.0.4</version>
       </dependency>
   </dependencies>
   ```
   
   ```groovy
   repositories {
      mavenCentral()
      maven { url "https://jitpack.io" }
   }
   dependencies {
      implementation 'com.github.reine-ishyanami:TextToImage:0.0.4'
   }
   ```

2. 编写代码

    ```java
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
    ```

## 可设置的图片参数

| 参数名             | 类型      | 默认值                      | 参数含义    |
|:----------------|:--------|:-------------------------|:--------|
| lineCharCount   | int     | 70                       | 每行最大字符数 |
| charSize        | int     | 32                       | 字符大小    |
| lineSpacing     | int     | 5                        | 行间距     |
| tableWidth      | int     | 4                        | 制表符宽度   |
| borderWidth     | int     | 1                        | 边框宽度    |
| fontPlain       | int     | Font.PLAIN               | 字体样式    |
| border          | boolean | true                     | 是否生成边框  |
| fontName        | String  | 宋体                       | 字体      |
| backgroundColor | Color   | new Color(255, 252, 245) | 图片背景颜色  |
| fontColor       | Color   | new Color(125, 101, 89)  | 字体颜色    |
| rectColor       | Color   | new Color(220, 211, 196) | 边框颜色    |

## 传递颜色参数时也可以使用其他方式

1. 传入16进制颜色编码
   
   ```java
   Color color = new Color(0xFFFCF5FF);
   Color color = new Color(0xFFFCF5);  // 去掉最后面的透明度编码，效果与上面一行效果一样   
   ```
   
2. 分别传入rgb的8位无符号数值

   ```java
   Color color = new Color(255, 252, 245); // 三位分别为r,g,b
   ```
   
## 按照默认方式生成的图片示例

![example](./img/example.jpg)