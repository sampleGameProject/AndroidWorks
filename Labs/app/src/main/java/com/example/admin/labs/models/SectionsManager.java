package com.example.admin.labs.models;

import android.content.Context;
import android.content.res.Resources;

import com.example.admin.labs.R;
import com.example.admin.labs.entry_adapter.EntryItem;
import com.example.admin.labs.entry_adapter.Item;
import com.example.admin.labs.entry_adapter.SectionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 11.09.2014.
 */
public class SectionsManager {

    class Section{
        private String title;
        private String[] items;

        public Section(String title,String[] items){
            this.title = title;
            this.items = items;
        }

        public String getTitle(){
            return title;
        }

        public String[] getItems(){
            return items;
        }
    }

    private Section[] sections;
    private HashMap<Integer,String> itemTitlesMap;

    public SectionsManager(Context context){
        sections = new Section[3];

        Resources res = context.getResources();

        sections[0] = new Section(context.getString(R.string.title_section1),
                res.getStringArray(R.array.interface_array));

        sections[1] = new Section(context.getString(R.string.title_section2),
                res.getStringArray(R.array.data_work_array));

        sections[2] = new Section(context.getString(R.string.title_section3),
                res.getStringArray(R.array.sensors_work_array));

        itemTitlesMap = new HashMap<Integer, String>();

        int index = 1;

        for(Section s : sections){
            String[] titleItems = s.getItems();
            for (String itemTitle : titleItems){
                itemTitlesMap.put(index,itemTitle);
                index++;
            }
            index++;
        }
    }

    public ArrayList<Item> getAdapterList(){

        ArrayList<Item> items = new ArrayList<Item>();

        for(Section s : sections){
            items.add(new SectionItem(s.getTitle()));
            String[] titleItems = s.getItems();
            for (String itemTitle : titleItems){
                items.add(new EntryItem(itemTitle,""));
            }
        }
        return items;
    }

    public String getItemTitle(int drawerItemPosition){
        if (itemTitlesMap.containsKey(drawerItemPosition))
            return itemTitlesMap.get(drawerItemPosition);
        else
            return "Unknown key";
    }
}
