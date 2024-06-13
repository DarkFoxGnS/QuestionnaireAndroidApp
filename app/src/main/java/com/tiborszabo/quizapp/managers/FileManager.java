package com.tiborszabo.quizapp.managers;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the reads and writes on the local directory of the application.
 * @author Tibor Peter Szabo.
 */
public class FileManager {

    /**
     * Writes the data to the filename.
     * @param context, fulfilled by getApplicationContext from an Activity.
     * @param filename, name of the file to write.
     * @param data, the data to write to the file.
     */
    public static void dryWrite(Context context,String filename,String data){
        //Writes data to the local directory.
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(context.getFilesDir(),filename)));
            bw.write(data);
            bw.close();
        }catch (Exception e){
            Log.e("FileManager.getData",e.getMessage());
        }
    }

    /**
     * Writes list to file named filename.
     * @param context, fulfilled by getApplicationContext from an Activity.
     * @param filename, name of the file to write.
     * @param list, the list of data that will be written to the file.
     */
    public static void setData(Context context,String filename, List<String> list){
        //Writes the List of strings to the local directory.
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(context.getFilesDir(),filename)));

            for (String line: list) {
                bw.write(line+"\r\n");
            }
            bw.close();
        }catch (Exception e){
            Log.e("FileManager.getData",e.getMessage());
        }
    }

    /**
     * Receives data from the local storage.
     * @param context, fulfilled by getApplicationContext from an Activity.
     * @param fileName, name of the file to read from.
     * @return List of strings containing the data.
     */
    public static List<String> getData(Context context, String fileName) {
        //Receives data from the local directory.
        List<String> data = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            String line = "";
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
            br.close();
        } catch (Exception e) {
            Log.e("FileManager.getData ERROR", e.getMessage());
        }

        return data;
    }
}
