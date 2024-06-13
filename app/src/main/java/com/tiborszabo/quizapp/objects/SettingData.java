package com.tiborszabo.quizapp.objects;

import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.annotation.Annotation;

/**
 * This is a data object containing a pair of Strings.
 * @author Tibor Peter Szabo.
 */
public class SettingData {

    private String name;
    private String data;


    /**
     * Initializes the SettingData object with parameters.
     * @param name, the name of the setting.
     * @param data, the value of the setting.
     */
    public SettingData(String name,String data){
        this.name = name;
        this.data = data;
    }

    /**
     * Parses the data into a writeable format.
     * @return the formated string representation of the setting.
     */
    @NonNull
    @Override
    public String toString() {
        return this.getName().replace(";","%59")+";"+this.getData().replace(";","%59");
    }

    /**
     * Sets the name of the setting.
     * @param name, the new name of the setting.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value of the setting.
     * @param data, the new value of the setting.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Gets the name of the setting.
     * @return the name of the setting.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the value of the setting.
     * @return the value of the setting.
     */
    public String getData() {
        return data;
    }
}
