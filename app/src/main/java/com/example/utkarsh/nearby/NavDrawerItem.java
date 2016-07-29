package com.example.utkarsh.nearby;

/**
 * Created by utkarsh on 23/7/16.
 */


public class NavDrawerItem {

    private String title;
    private int icon;

    // boolean to set visiblity of the counter
    private boolean ischeckboxselected = false;

    public NavDrawerItem(){}

    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public NavDrawerItem(String title, int icon, boolean ischeckboxselected){
        this.title = title;
        this.icon = icon;
        this.ischeckboxselected = ischeckboxselected;

    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }


    public boolean getifcheckboxselected(){
        return this.ischeckboxselected;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }


    public void setcheckboxselectivity(boolean ischeckboxselected){
        this.ischeckboxselected = ischeckboxselected;
    }
}