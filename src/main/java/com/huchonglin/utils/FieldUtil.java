package com.huchonglin.utils;

import com.huchonglin.utils.constant.KeywordConfigConsts;
import com.huchonglin.utils.constant.SymbolConfigConsts;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Auther: hcl
 * @Date: 2020/6/27 18:43
 */
@Slf4j
public class FieldUtil {
    private static StringBuilder fieldName;

    public static StringBuilder generateField(StringBuilder fileInfo) {

        for (Map.Entry<String, String> entry : DbUtil.getColumnTypeMap().entrySet()) {
            fieldName = CommonUtil.lowerFirstCase(entry.getKey());
            fileInfo.append(SymbolConfigConsts.TAB).append(KeywordConfigConsts.PRIVATE).append(SymbolConfigConsts.SPACE)
                .append(DbUtil.getDbType2javaType().get(entry.getValue())).append(SymbolConfigConsts.SPACE)
                .append(fieldName).append(SymbolConfigConsts.STOP).append(SymbolConfigConsts.LINE_BREAK);
            log.info("字段名构建完成：" + fieldName);
        }
        return fileInfo;
    }
}
