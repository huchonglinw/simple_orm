package com.huchonglin.utils;

import com.huchonglin.utils.constant.JdbcConfigConsts;
import com.huchonglin.utils.constant.KeywordConfigConsts;
import com.huchonglin.utils.constant.PackageConfigConsts;
import com.huchonglin.utils.constant.SymbolConfigConsts;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: hcl
 * @Date: 2020/6/27 18:43
 */
@Slf4j
public class DbUtil {
    private final static String DB_DRIVER;
    private final static String DB_URL;
    private final static String DB_NAME;
    private final static String DB_USER;
    private final static String DB_PASSWORD;
    private final static String METHOD_WAY;

    private final static String BASE_PACKAGE;

    private final static String FileSeparator = File.separator;

    /* Map <数据库原生类型, Java类型> */
    protected final static Map<String, String> DB_TYPE2JAVA_TYPE = new HashMap<>();
    /* Map <数据库原生类型, 相关包的字符串> */
    protected final static Map<String, String> NECESSARY_IMPORT = new HashMap<>();
    /* Map <数据库列名, 列的类型> */
    protected final static Map<String, String> COLUMN_TYPE_MAP = new HashMap<>();

    protected static Map<String, String> getDbType2javaType() {
        return DB_TYPE2JAVA_TYPE;
    }

    protected static Map<String, String> getNecessaryImport() {
        return NECESSARY_IMPORT;
    }

    protected static Map<String, String> getColumnTypeMap() {
        return COLUMN_TYPE_MAP;
    }

    public static String getMethodWay() {
        return METHOD_WAY;
    }

    static {
        Properties properties = PropertiesUtil.loadProperties();

        BASE_PACKAGE = (String)properties.get(JdbcConfigConsts.BASE_PACKAGE);
        DB_DRIVER = (String)properties.get(JdbcConfigConsts.DB_DRIVER);
        DB_URL = (String)properties.get(JdbcConfigConsts.DB_URL);
        DB_NAME = (String)properties.get(JdbcConfigConsts.DB_NAME);
        DB_USER = (String)properties.get(JdbcConfigConsts.DB_USER);
        DB_PASSWORD = (String)properties.get(JdbcConfigConsts.DB_PASSWORD);
        METHOD_WAY = (String)properties.get(JdbcConfigConsts.METHOD_WAY);

        DB_TYPE2JAVA_TYPE.put("INT UNSIGNED", "Integer");
        DB_TYPE2JAVA_TYPE.put("VARCHAR", "String");
        DB_TYPE2JAVA_TYPE.put("TIMESTAMP", "Date");
        DB_TYPE2JAVA_TYPE.put("INT", "Integer");
        DB_TYPE2JAVA_TYPE.put("TINYINT", "Byte");
        DB_TYPE2JAVA_TYPE.put("DATETIME", "Date");
        DB_TYPE2JAVA_TYPE.put("CHAR", "String");

        NECESSARY_IMPORT.put(SymbolConfigConsts.TIMESTAMP, PackageConfigConsts.TIMESTAMP);
        NECESSARY_IMPORT.put(SymbolConfigConsts.DATETIME, PackageConfigConsts.DATETIME);
        NECESSARY_IMPORT.put(SymbolConfigConsts.LOMBOK_GETTER, PackageConfigConsts.LOMBOK_GETTER);
        NECESSARY_IMPORT.put(SymbolConfigConsts.LOMBOK_SETTER, PackageConfigConsts.LOMBOK_SETTER);
    }

    /**
     * 根据DB的表生成相应的Class
     */
    public static void generateClassFromDb() {
        String fileCanonicalPath;
        StringBuilder fileInfo;

        try {
            // 加载驱动
            Class.forName(DB_DRIVER);

            // 获取连接
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            DatabaseMetaData metaData = conn.getMetaData();

            fileCanonicalPath = DbUtil.createPackage(BASE_PACKAGE);
            ResultSet resultSet = metaData.getTables(DB_NAME, null, null, null);

            int methodWay = MethodUtil.decideMethodWay(METHOD_WAY);

            while (resultSet.next()) {
                fileInfo = new StringBuilder();

                String tableName = resultSet.getString(3);
                log.info("tableName : " + tableName);

                String className = CommonUtil.upperFirstCase(tableName).toString();

                ResultSet columns = metaData.getColumns(DB_NAME, DB_NAME, tableName, null);
                while (columns.next()) {
                    String column_name = columns.getString("COLUMN_NAME");
                    String type_name = columns.getString("TYPE_NAME");
                    COLUMN_TYPE_MAP.put(column_name, type_name);
                }

                fileInfo = ClassUtil.generatePackageName(fileInfo, BASE_PACKAGE);

                // 判断是否需要导包
                fileInfo = ClassUtil.importPackage(fileInfo, methodWay);

                fileInfo = ClassUtil.generateClassName(className, fileInfo);

                fileInfo = FieldUtil.generateField(fileInfo);

                fileInfo = MethodUtil.generateMethod(fileInfo, methodWay);

                fileInfo = CommonUtil.finishGenerate(fileInfo);

                StringBuilder targetLocation = getTargetLocation(fileCanonicalPath, className);

                writeFileInfo(targetLocation.toString(), fileInfo);
            }
        } catch (ClassNotFoundException e) {
            log.error("加载jdbc驱动失败");
        } catch (SQLException e) {
            log.error("获取数据库连接失败");
        }

    }

    private static StringBuilder getTargetLocation(String fileCanonicalPath, String className) {
        StringBuilder targetLocation = new StringBuilder();
        targetLocation.append(fileCanonicalPath).append(FileSeparator).append(className)
            .append(SymbolConfigConsts.JAVA_SUFFIX);

        log.info("类信息构建完成，输出位置：" + targetLocation);
        return targetLocation;
    }

    /**
     * 根据basePackage，在项目下创建相应的包
     *
     * @param basePackage
     *            包路径
     * @return basePackage之后的路径
     * @throws IOException
     */
    private static String createPackage(String basePackage) {
        StringBuilder root = new StringBuilder();
        String canonicalPath = null;

        try {
            canonicalPath = new File("").getCanonicalPath();
        } catch (IOException e) {
            log.error("Invalid file path");
            throw new RuntimeException("Invalid file path");
        }

        root.append(canonicalPath).append(FileSeparator).append(SymbolConfigConsts.SRC);

        File file = new File(root.toString());
        String[] fileNames = file.list();
        for (String fileName : fileNames) {
            if (fileName.equals(KeywordConfigConsts.MAIN)) {
                root.append(FileSeparator).append(KeywordConfigConsts.MAIN).append(FileSeparator)
                    .append(KeywordConfigConsts.JAVA);
            }
        }
        String[] splits = basePackage.split("\\.");
        for (String split : splits) {
            root.append(FileSeparator).append(split);
        }
        String rootPath = root.toString();
        file = new File(rootPath);
        boolean mkdir = file.mkdirs();
        String fileCanonicalPath = null;

        try {
            fileCanonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            log.error("Invalid file path");
            throw new RuntimeException("Invalid file path");
        }
        if (mkdir) {
            log.info("包创建成功，路径为" + fileCanonicalPath);
        } else {
            log.error("包创建失败");
            throw new RuntimeException("包创建失败");
        }
        return fileCanonicalPath;
    }

    private static void writeFileInfo(String outPutPath, StringBuilder fileInfo) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outPutPath);
            fos.write(fileInfo.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            log.error("找不到指定路径");
        } catch (IOException e) {
            log.error("输出class文件失败");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("关闭输出流失败");
                }
            }
            log.info("类构建完成：" + outPutPath);
        }
    }
}
