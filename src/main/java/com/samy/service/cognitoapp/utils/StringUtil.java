package com.samy.service.cognitoapp.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    public static String splitCamelCase(String s) {
        return StringUtils.capitalize(StringUtils.join(
                StringUtils.splitByCharacterTypeCamelCase(s.toLowerCase()), StringUtils.SPACE));
    }
}
