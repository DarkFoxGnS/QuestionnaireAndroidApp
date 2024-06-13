package com.tiborszabo.quizapp.objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * A subclass of the ArrayList, used to provide further functionality.
 * @author Tibor Peter Szabo.
 */
public class SettingsList extends ArrayList<SettingData> {

    /**
     * Gets the SettingData object by the name of the object.
     * @param name String, the name of the object.
     * @return SettingData, the requested object by name.
     */
    public SettingData getObjectByName(String name){
        for (SettingData data: this) {
            if (data.getName().equals(name)){
                return data;
            }
        }
        return null;
    }

    /**
     * Gets the value of the SettingData object based on it's name.
     * @param name String, the name of the object.
     * @return String, the value of the object.
     */
    public String getValueByName(String name){
        //Find the data by name and return the value.
        for (SettingData settingData: this) {
            if (settingData.getName().equals(name)){
                return settingData.getData();
            }
        }
        return null;
    }

    /**
     * Sets the value of the SettingData by name.
     * @param name String, the name of the object.
     * @param data String, the data of the object.
     */
    public void setValueByName(String name,String data){
        //Find the data by name and set the value of it.
        for (SettingData settingData: this) {
            if (settingData.getName().equals(name)){
                settingData.setData(data);
            }
        }
    }

    /**
     *Converts the content of this list to writable data.
     * @return List of Strings containing formatted data.
     */
    public List<String> toStringList() {
        //convert the data to writeable string.
        List<String> list = new ArrayList<>();

        for (SettingData data : this) {
            list.add(data.toString());
        }

        return list;
    }
}
