package com.example.gattest.utils;
public class StringUtil{
    /**
     * 将不定长的数字参数列表转换成String数组
     * 主要用于国际化
     * @param number
     * @return
     */
    public static String[] parseInt2StringArray(int ... number){
        String[] array = new String[number.length];
        for (int i=0;i<number.length;i++ ) {
            array[i]=number[i]+"";
        }
        return array;
    }

    /**
     * 判断字符串是否为空对象或空字符串
     * @param string
     * @return
     */
    public static boolean isNullOrEmpty(String string){
        return string==null || "".equals(string.trim())?true:false;
    }
}
