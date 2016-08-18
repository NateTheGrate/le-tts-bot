package com.Nate.LongTTSBot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Nathanael on 8/17/2016.
 */
public class Utility {

    /**
     * lists all files in a folder
     * @author
     * @param folder
     * @return files in folder
     */
    public static File[] getFilesInFolder(File folder) {

        System.out.println("Finding files in "+folder.getAbsolutePath());
        File[] files = folder.listFiles();

        return files;
    }

    /**
     *  returns all lines in a files as a list of strings
     * @param file
     * @return the lines in a text file
     * @throws IOException
     */
    public static List<String> getLinesInTextFile(File file) throws IOException {
        List<String> strings = new ArrayList<>();
        try (Scanner scanner = new Scanner(file,"UTF-8")){
            while (scanner.hasNextLine()){
                String cur = scanner.nextLine();
                strings.add(cur);
            }
            scanner.close();
        }

        return strings;
    }

    /**
     * replaces given string and part of the string to replace, with a replacement string
     * @param string
     * @param toReplace
     * @param replacement
     * @return built string
     */
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

    /**
     * splits string into chunks of a given length
     * @param str
     * @return split strings
     */
    public static ArrayList<String> split(String str, int length){
        ArrayList<String> result = new ArrayList<String>();

        String temp = "";
        for(int i = 0; i < str.length(); i++){
            temp += str.substring(i, i+1);
            //split string into chunks of at least 140 characters
            if(temp.length() >= length &&(temp.endsWith(" ")) ) {
                result.add(temp);
                temp = "";
            }
        }
        //add anything left over
        if(temp.length() > 0){
            result.add(temp);
        }
        return result;
    }




}
