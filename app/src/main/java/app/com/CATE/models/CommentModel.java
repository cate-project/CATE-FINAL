package app.com.CATE.models;

public class CommentModel {

    private String author;
    private String desc;

    public CommentModel(){
    }

    public CommentModel(String author, String desc){
        this.author = author;
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public  void setAuthor(String author){
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public  void setDesc(String desc){
        this.desc = desc;
    }
}
