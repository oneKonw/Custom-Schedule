package com.example.customschedule.DIYSetting;

import org.litepal.crud.DataSupport;

/**
 * Created by hyt on 2018/2/10.
 */

public class DIYCourses extends DataSupport {
    private int iId;
    private String txtClsName;//名称
    private String txtClsSite;//地点
    private int txtday;//星期几
    private int txtNumber;//节数
    private int txtCountNumber = 0;//总共几节

    public String getTxtClsName() {
        return txtClsName;
    }

    public void setTxtClsName(String txtClsName) {
        this.txtClsName = txtClsName;
    }

    public String getTxtClsSite() {
        return txtClsSite;
    }

    public void setTxtClsSite(String txtClsSite) {
        this.txtClsSite = txtClsSite;
    }

    public int getTxtCountNumber() {
        return txtCountNumber;
    }

    public void setTxtCountNumber(int txtCountNumber) {
        this.txtCountNumber = txtCountNumber;
    }

    public int getTxtday() {
        return txtday;
    }

    public void setTxtday(int txtday) {
        this.txtday = txtday;
    }

    public int getTxtNumber() {
        return txtNumber;
    }

    public void setTxtNumber(int txtNumber) {
        this.txtNumber = txtNumber;
    }

    public int getiId() {
        return iId;
    }

    public void setiId(int iId) {
        this.iId = iId;
    }
}
