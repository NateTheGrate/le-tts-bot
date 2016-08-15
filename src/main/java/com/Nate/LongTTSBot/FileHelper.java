package com.Nate.LongTTSBot;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by iWasHere on 8/15/2016.
 */
public class FileHelper {

    public static File[] getFilesInFolder(File folder) {

        System.out.println("Finding files in "+folder.getAbsolutePath());
        File[] files = folder.listFiles();

        return files;
    }

    public static List<String> getLinesInTextFile(File file) throws IOException{
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
}
