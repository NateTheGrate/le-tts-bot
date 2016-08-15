package com.Nate.LongTTSBot;

/**
 * Created by iWasHere on 8/15/2016.
 */
public class StringHelper {
    public static String replace(String string, String toReplace, String replacement){
        String finder = string.toLowerCase(); //made lowercase as to find what we're replacing

        StringBuilder builder = new StringBuilder(string);

        while (finder.contains(toReplace)){
            int index = finder.indexOf(toReplace); //find where we are replacing in the StringBuilder
            finder = finder.replaceFirst(toReplace,replacement); //remove the instance of toReplace
            builder = builder.replace(index,index+toReplace.length(),replacement); //affect the final string (not all lowercase)
        }

        return builder.toString();
    }
}
