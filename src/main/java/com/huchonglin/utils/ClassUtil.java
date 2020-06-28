package com.huchonglin.utils;

import com.huchonglin.utils.constant.KeywordConfigConsts;
import com.huchonglin.utils.constant.MethodWayConfigConsts;
import com.huchonglin.utils.constant.SymbolConfigConsts;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author : hcl
 * @Date: 2020/6/27 18:42
 */
@Slf4j
public class ClassUtil {
    private static Map<String, String> temporaryMap;

    /**
     * 构建包名并返回
     * 
     * @param fileInfo
     *            StringBuilder，描述一个类的信息
     * @param packageName
     *            包名
     * @return
     */
    public static StringBuilder generatePackageName(StringBuilder fileInfo, String packageName) {
        return fileInfo.append(SymbolConfigConsts.PACKAGE).append(SymbolConfigConsts.SPACE).append(packageName)
            .append(SymbolConfigConsts.STOP).append(SymbolConfigConsts.LINE_BREAK);
    }

    /**
     * 导包
     * 
     * @return
     * @param fileInfo
     * @param methodWay
     */
    public static StringBuilder importPackage(StringBuilder fileInfo, int methodWay) {
        temporaryMap = DbUtil.getNecessaryImport();
        Set<Map.Entry<String, String>> columnTypeEntrySet = DbUtil.getColumnTypeMap().entrySet();
        for (Map.Entry<String, String> columnTypeEntry : columnTypeEntrySet) {
            String entryValue = temporaryMap.get(columnTypeEntry.getValue());
            if (entryValue != null) {
                fileInfo.append(entryValue).append(SymbolConfigConsts.LINE_BREAK);
            }

        }

        if (methodWay == MethodWayConfigConsts.LOMBOK) {
            String entryValue = temporaryMap.get(SymbolConfigConsts.LOMBOK_GETTER);
            fileInfo.append(entryValue).append(SymbolConfigConsts.LINE_BREAK);
            entryValue = temporaryMap.get(SymbolConfigConsts.LOMBOK_SETTER);
            fileInfo.append(entryValue).append(SymbolConfigConsts.LINE_BREAK);
        }

        log.info("导包成功");
        return fileInfo;
    }

    /**
     * 构建类名
     * 
     * @param className
     *            类名
     * @param fileInfo
     */
    public static StringBuilder generateClassName(String className, StringBuilder fileInfo) {
        fileInfo.append(KeywordConfigConsts.PUBLIC).append(SymbolConfigConsts.SPACE).append(KeywordConfigConsts.CLASS)
            .append(SymbolConfigConsts.SPACE).append(className).append(SymbolConfigConsts.LEFT_BRACKET)
            .append(SymbolConfigConsts.LINE_BREAK);
        log.info("类名构建完成：" + className);
        return fileInfo;
    }
}
