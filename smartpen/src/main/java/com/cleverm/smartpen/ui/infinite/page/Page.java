package com.cleverm.smartpen.ui.infinite.page;


import java.util.IllegalFormatCodePointException;

public class Page {
    public String data;
    public Object res;
    public OnPageClickListener onPageClickListener;
    public Object info;

    public Page(String data, Object res,Object info) {
        this.data = data;
        this.res = res;
        this.info=info;
    }

    public Page(String data, Object url, Object info ,OnPageClickListener listener) {
        this.data = data;
        this.res = url;
        this.info= info;
        this.onPageClickListener = listener;
    }

    @Override
    public String toString() {
        return "Page{" +
                "data='" + data + '\'' +
                ", res=" + res +
                ", onPageClickListener=" + onPageClickListener +
                ", info=" + info +
                '}';
    }
}
