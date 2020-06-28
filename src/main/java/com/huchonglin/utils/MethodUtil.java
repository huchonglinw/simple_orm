package com.huchonglin.utils;

import com.huchonglin.utils.constant.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

/**
 * @author: hcl
 * @date: 2020/6/28 09:38
 */
@Slf4j
public class MethodUtil {
    public static StringBuilder generateMethod(StringBuilder fileInfo, int methodWay) {
        if (methodWay == MethodWayConfigConsts.NORMAL) {
            return generateMethodNormal(fileInfo);
        } else if (methodWay == MethodWayConfigConsts.LOMBOK) {
            return generateMethodLombok(fileInfo);
        }
        log.error("写入方法错误");
        return fileInfo;
    }

    private static StringBuilder generateMethodLombok(StringBuilder fileInfo) {
        int publicIndex = fileInfo.indexOf(KeywordConfigConsts.PUBLIC);

        final StringBuilder LOMBOK = new StringBuilder();
        LOMBOK.append(AnnoConfigConsts.LOMBOK_GETTER).append(SymbolConfigConsts.LINE_BREAK)
                .append(AnnoConfigConsts.LOMBOK_SETTER).append(SymbolConfigConsts.LINE_BREAK);

        fileInfo.insert(publicIndex, LOMBOK);
        return fileInfo;
    }

    private static StringBuilder generateMethodNormal(StringBuilder fileInfo) {
        Map<String, String> columnTypeMap = DbUtil.getColumnTypeMap();
        Map<String, String> dbType2javaTypeMap = DbUtil.getDbType2javaType();

        Set<Map.Entry<String, String>> columnTypeEntrySet = columnTypeMap.entrySet();
        for (Map.Entry<String, String> entry : columnTypeEntrySet) {
            String javaType = dbType2javaTypeMap.get(entry.getValue());
            if(javaType != null) {
                //getter
                String columnName = entry.getKey();
                //首字母大写
                StringBuilder columnNameUpperCase = CommonUtil.upperFirstCase(columnName);
                StringBuilder columnNameLowerCase = CommonUtil.lowerFirstCase(columnName);

                fileInfo.append(SymbolConfigConsts.LINE_BREAK).append(SymbolConfigConsts.TAB).append(KeywordConfigConsts.PUBLIC).append(SymbolConfigConsts.SPACE)
                        .append(javaType).append(SymbolConfigConsts.SPACE)
                        .append(KeywordConfigConsts.GET).append(columnNameUpperCase).append(SymbolConfigConsts.LEFT_PARENTHESIS).append(SymbolConfigConsts.RIGHT_parenthesis)
                        .append(SymbolConfigConsts.SPACE).append(SymbolConfigConsts.LEFT_BRACKET).append(SymbolConfigConsts.LINE_BREAK).append(SymbolConfigConsts.TAB).append(SymbolConfigConsts.TAB).append(KeywordConfigConsts.RETURN)
                        .append(SymbolConfigConsts.SPACE).append(columnNameLowerCase).append(SymbolConfigConsts.STOP).append(SymbolConfigConsts.LINE_BREAK)
                        .append(SymbolConfigConsts.TAB).append(SymbolConfigConsts.RIGHT_BRACKET).append(SymbolConfigConsts.LINE_BREAK);
                //setter
                fileInfo.append(SymbolConfigConsts.LINE_BREAK).append(SymbolConfigConsts.TAB).append(KeywordConfigConsts.PUBLIC).append(SymbolConfigConsts.SPACE)
                        .append(KeywordConfigConsts.VOID).append(SymbolConfigConsts.SPACE)
                        .append(KeywordConfigConsts.SET).append(columnNameUpperCase).append(SymbolConfigConsts.LEFT_PARENTHESIS)
                        .append(javaType).append(SymbolConfigConsts.SPACE).append(columnNameLowerCase)
                        .append(SymbolConfigConsts.RIGHT_parenthesis).append(SymbolConfigConsts.SPACE).append(SymbolConfigConsts.LEFT_BRACKET).append(SymbolConfigConsts.LINE_BREAK)
                        .append(SymbolConfigConsts.TAB).append(SymbolConfigConsts.TAB).append(KeywordConfigConsts.THIS).append(SymbolConfigConsts.DOT).append(columnNameLowerCase).append(SymbolConfigConsts.SPACE)
                        .append(SymbolConfigConsts.EQUAL_TO).append(SymbolConfigConsts.SPACE).append(columnNameLowerCase).append(SymbolConfigConsts.STOP).append(SymbolConfigConsts.LINE_BREAK)
                        .append(SymbolConfigConsts.TAB).append(SymbolConfigConsts.RIGHT_BRACKET).append(SymbolConfigConsts.LINE_BREAK);
            }
        }
        return fileInfo;
    }

//    private static StringBuilder generateGetterMethodNormal(StringBuilder fileInfo, StringBuilder columnNameUpperCase){
//
//    }

    /**
     * 选择生成Method的方式
     * 
     * @param methodWay
     *            生成Method的方式
     * @return
     */
    public static int decideMethodWay(String methodWay) {
        if (methodWay == null || methodWay.equals("normal")) {
            // 走正常的
            return MethodWayConfigConsts.NORMAL;
        } else if (methodWay.equals("lombok")) {
            // 走lombok
            return MethodWayConfigConsts.LOMBOK;
        }
        throw new RuntimeException("method.way import error");

    }
}
