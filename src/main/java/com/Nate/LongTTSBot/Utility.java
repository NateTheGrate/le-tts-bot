package com.Nate.LongTTSBot;

import com.sun.media.jfxmedia.logging.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
     * writes a value to a variable tied to a user ID
     * @param file
     * @param userID
     * @param variableName
     * @param value
     */
    public static void writeNewUserVariableToFile(File file,String userID, String variableName, String value){
        try {
            if(!file.exists()){
                file.createNewFile();
                file.setReadable(true);
                file.setWritable(true);
            }

            FileWriter fw = new FileWriter(file, true); //appends
            BufferedWriter bw = new BufferedWriter(fw);

            bw.append(userID + ":" + variableName + ":" + value);
            bw.newLine();

            bw.close();
            fw.close();

        }catch(IOException e){
            System.out.println("Problem with writing to file in " + file.getAbsolutePath() );
        }
    }

    public static void overwriteUserVariable( File file, String userID, String variableName, String value, boolean add){
        try{

            boolean variableExists = false;
            List<String>lines = getLinesInTextFile(file);

            FileWriter fw = new FileWriter(file, false); //does not append
            BufferedWriter bw = new BufferedWriter(fw);

            int index = 0;
            for( String line : lines){
                if(line.startsWith(userID) ){
                    if(line.substring(line.indexOf(":") + 1, line.lastIndexOf(":") ).equals(variableName)){
                        if(add){
                            lines.set(index, userID + ":" + variableName + ":" + (Integer.parseInt(line.substring(line.lastIndexOf(":") + 1) ) + 1) );
                        }else {
                            lines.set(index, userID + ":" + variableName + ":" + value);
                        }
                        variableExists = true;
                        break;
                    }
                }
                index++;
            }



            for( String line : lines){
                if(variableExists) {
                    bw.write(line);
                }
            }

            bw.close();
            fw.close();

            if (!variableExists) {
                writeNewUserVariableToFile(file, userID, variableName, value);
            }


        }catch(IOException e){
            e.printStackTrace();
        }
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
       // str.replace("\n", " ");
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

    /**
     * splits string into chuncks for every instance of a given phrase
     * @param str
     * @return split strings
     */
    public static ArrayList<String> split(String str, String phrase){
        // str.replace("\n", " ");
        ArrayList<String> result = new ArrayList<String>();

        String temp = "";
        for(int i = 0; i < str.length(); i++){
            temp += str.substring(i, i+1);
            //split string into chunks of at least 140 characters
            if(temp.indexOf(phrase) >= 0 ) {
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
    /**
     * combines two images
     * @param result
     * @param Background
     * @param Foreground
     * @param startX
     * @param startY
     * @return combined image
     * @throws IOException
     */
    public static File combineImages(File result,File Background, File Foreground, int startX, int startY) throws IOException {

        if(!result.exists()){
            Logger.logMsg(Logger.WARNING, "combined image file @" + result.getAbsolutePath() + " does not exist, making a new one at that location");
            result.createNewFile();
        }

        //load images
        BufferedImage image = ImageIO.read(Background);
        BufferedImage overlay = ImageIO.read(Foreground);

        //create new image
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage combinedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);


        //draw images
        Graphics g = combinedImage.getGraphics();
        int xx = 103-(overlay.getWidth()/2);
        int yy = 101-(overlay.getHeight()/2);
        g.drawImage( image, 0, 0, null);
        g.drawImage( overlay, xx, yy, null);

        //save as new image
        ImageIO.write( combinedImage, "JPG", result );

        //return resulting image
        return result;

    }

}
