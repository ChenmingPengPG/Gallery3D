package com.pcm.framework.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyData {
    private MyData(){}
    private static volatile MyData myData;
    public static MyData getMyData(){
        if(myData == null){
            synchronized (MyData.class){
                if(myData == null){
                    myData = new MyData();
                }
            }
        }
        return myData;
    }
    public Map<String, String> datamap = new HashMap<String, String>(){{
        put("img_15_02_13", "颜值巅峰");
        put("img_18_09_05", "苏州践行");
        put("img_19_01_20", "上海中心");
        put("img_19_01_24", "南京夫子庙");
        put("img_19_01_25", "雨花台/南京大学");
        put("img_19_02_13", "24岁啦");
        put("img_19_05_28", "乌镇游");
        put("img_19_07_15", "青山ktv");
        put("img_19_10_31", "外滩");
        put("img_19_11_15", "海底捞");
        put("img_20_02_13", "25岁啦");
    }};

    private List<String> picsName = new ArrayList<>();
    private List<String> photoUris = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private List<String> describe = new ArrayList<>();
    private List<String> pathes = new ArrayList<>();

    public List<String> getPathes() {
        return pathes;
    }

    public void setPathes(List<String> pathes) {
        this.pathes = pathes;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getDescribe() {
        return describe;
    }

    public void setDescribe(List<String> describe) {
        this.describe = describe;
    }

    public List<String> getPhotoUris() {
        return photoUris;
    }

    public void setPhotoUris(List<String> photoUris) {
        this.photoUris = photoUris;
    }



    public List<String> getPicsName() {
        return picsName;
    }

    public void setPicsName(List<String> picsName) {
        this.picsName = picsName;
    }



    public String getCurrenIndex(String time) {
        int index = 1;
        for(String name : picsName){
            if(name.contains(time)){
                String[] re = name.split("_");
                name = re[re.length-1];
                if(index <= Integer.parseInt(name)){
                    index += 1;
                }
            }
        }
        return String.valueOf(index);
    }


    public void addData(String uriString, String path, String picsName, String title, String describe){
        getPhotoUris().add(uriString);
        getPicsName().add(picsName);
        getTitle().add(title);
        getDescribe().add(describe);
        getPathes().add(path);
    }
    public void deleteData(int position){
        getPhotoUris().remove(position);
        getPicsName().remove(position);
        getTitle().remove(position);
        getDescribe().remove(position);
        getPathes().remove(position);
    }
}
