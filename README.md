# 文本转图片

## 使用方法

1. 引入依赖

2. 编写代码

    ```java
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
            // 由文本生成图片
            if (t2IUtil.storeImageAfterGenerateTextImage(file, msg)) {
                log.info("写入成功");
            } else log.error("写入失败");
        }
    
        /**
         * 初始化工具类
         */
        @BeforeEach
        void set_font_file() {
            String path = Objects.requireNonNull(getClass().getResource("/font/SourceHanSansCN-Medium.otf")).getPath();
            path = path.substring(1);
            File file = Paths.get(path).toFile();
            // 设置图片参数，初始化工具类
            T2IConstant constant = T2IConstant.builder().build();
            t2IUtil = new T2IUtil(constant);
            // 设置第三方字体
            t2IUtil.useCustomFont(file);
        }
    }
    ```

## 可设置的图片参数

| 参数名             | 类型      | 默认值                      | 参数含义    |
|:----------------|:--------|:-------------------------|:--------|
| lineCharCount   | int     | 60                       | 每行最大字符数 |
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

