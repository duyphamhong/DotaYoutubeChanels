package com.youtubeapp.doto.dotayoutubechanels.activities.utils;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by admin on 1/10/2016.
 */
public class StringUtils {
    public static String subStringPartern(String value){
        if(value.length() > 30)
        {
            int count = 30;
            for(int i = 30; i < value.length(); i++)
            {
                if(value.charAt(i) == '.' || value.charAt(i) == '!'
                        || value.charAt(i) == '?'){
                    count  = i;
                    break;
                }
            }
            return value.substring(0, count) + ".....";
        }
        else
            return value;
    }

    public static String replace(String str, int index, char replace){
        if(str==null){
            return str;
        }else if(index<0 || index>=str.length()){
            return str;
        }
        char[] chars = str.toCharArray();
        chars[index] = replace;
        return String.valueOf(chars);
    }

    public static String formatDecimal(BigInteger value) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(value.longValue());
    }
}
