package com.huchonglin.utils;

import com.huchonglin.utils.constant.SymbolConfigConsts;

/**
 * @author: hcl
 * @date: 2020/6/27 21:52
 */
public class CommonUtil {

    /**
     * eg: users -> Users ; users_top -> UsersTop 将str根据"_"分割，首字母大大写
     *
     * @param str
     *            传进来的字符串
     * @return 首字母大写的字符串
     */
    public static StringBuilder upperFirstCase(String str) {
        StringBuilder className = new StringBuilder();
        String[] splits = str.split("_");

        for (String split : splits) {
            int right = split.length();
            className.append(split.substring(0, 1).toUpperCase()).append(split.substring(1, right));
        }
        return className;
    }

    /**
     * eg: user_name -> userName user_Name -> userName
     *
     * @param str
     * @return
     */
    public static StringBuilder lowerFirstCase(String str) {
        StringBuilder fieldName = new StringBuilder();
        String leftSubString;
        String rightSubString;
        String[] splits = str.split("_");

        for (int i = 0; i < splits.length; i++) {
            int right = splits[i].length();
            leftSubString = splits[i].substring(0, 1);
            rightSubString = splits[i].substring(1, right);
            if (i == 0) {
                fieldName.append(leftSubString.toLowerCase()).append(rightSubString);
            } else {
                fieldName.append(leftSubString.toUpperCase()).append(rightSubString);
            }
        }
        return fieldName;
    }

    public static StringBuilder finishGenerate(StringBuilder fileInfo) {
        return fileInfo.append(SymbolConfigConsts.RIGHT_BRACKET);
    }
}
