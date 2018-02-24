package com.example.customschedule.DIYSetting;

import org.litepal.crud.DataSupport;

/**
 * Created by hyt on 2018/2/16.
 */

public class DIYDaySchedule extends DataSupport {
    String clsName;
    String clsSite;
    int clsStartNumber;
    int clsCountNumber;
    int iID;
    int day;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public int getClsCountNumber() {
        return clsCountNumber;
    }

    public void setClsCountNumber(int clsCountNumber) {
        this.clsCountNumber = clsCountNumber;
    }

    public String getClsSite() {
        return clsSite;
    }

    public void setClsSite(String clsSite) {
        this.clsSite = clsSite;
    }

    public int getClsStartNumber() {
        return clsStartNumber;
    }

    public void setClsStartNumber(int clsStartNumber) {
        this.clsStartNumber = clsStartNumber;
    }

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }
}
