package app.com.CATE.models;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mdmunirhossain on 12/18/17.
 */
public class CategoryModel{
    private String id ;
    private String name ;
    private String detail ;
    private String key ;

    public void setId(String id) {
        id = id ;
    }
    public void setName(String name) {
        name = name ;
    }
    public void setDetail(String detail) {
        detail = detail ;
    }
    public void setKey(String key){
        key = key;
    }

    public String getId() {
        return this.id ;
    }
    public String getName() {
        return this.name ;
    }
    public String getDetail() {
        return this.detail ;
    }
    public String getKey(){
        return this.key;
    }
    public CategoryModel(String id, String name, String detail, String key) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.key = key;
    }

}
//public class CategoryModel{
//    private Drawable iconDrawable ;
//    private String titleStr ;
//    private String descStr ;
//    private String channelStr ;
//
//    public void setIcon(Drawable icon) {
//        iconDrawable = icon ;
//    }
//    public void setTitle(String title) {
//        titleStr = title ;
//    }
//    public void setDesc(String desc) {
//        descStr = desc ;
//    }
//
//    public void setChannelId(String channel){
//        channelStr = channel;
//    }
//    public Drawable getIcon() {
//        return this.iconDrawable ;
//    }
//    public String getTitle() {
//        return this.titleStr ;
//    }
//    public String getDesc() {
//        return this.descStr ;
//    }
//    public String getChannelId(){
//        return this.channelStr;
//    }
//}