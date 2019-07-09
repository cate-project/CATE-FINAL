package app.com.CATE.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mdmunirhossain on 12/18/17.
 */

public class YoutubeDataModel implements Parcelable {
    private String title = "";
    private String description = "";
    private String publishedAt = "";
    private String thumbnail = "";
    private String video_id = "";
    private String video_kind = "";
    private int video_index;

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo_kind() {
        return video_kind;
    }

    public void setVideo_kind(String video_kind) {
        this.video_kind = video_kind;
    }

    public int getVideo_index() { return video_index;}

    public void setVideo_index(int video_index) {this.video_index = video_index;}

    public YoutubeDataModel(String title, String description, String publishedAt, String thumbnail,String video_id, String video_kind, int video_index) {
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.thumbnail = thumbnail;
        this.video_id = video_id;
        this.video_kind = video_kind;
        this.video_index = video_index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        //dest.writeString(description);
        //dest.writeString(publishedAt);
        dest.writeString(thumbnail);
        dest.writeString(video_id);
        dest.writeString(video_kind);
        dest.writeInt(video_index);
    }

    public YoutubeDataModel() {
        super();
    }


    protected YoutubeDataModel(Parcel in) {
        this();
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.title = in.readString();
        //this.description = in.readString();
        //this.publishedAt = in.readString();
        this.thumbnail = in.readString();
        this.video_id = in.readString();
        this.video_kind = in.readString();
        this.video_index = in.readInt();
    }

    public static final Creator<YoutubeDataModel> CREATOR = new Creator<YoutubeDataModel>() {
        @Override
        public YoutubeDataModel createFromParcel(Parcel in) {
            return new YoutubeDataModel(in);
        }

        @Override
        public YoutubeDataModel[] newArray(int size) {
            return new YoutubeDataModel[size];
        }
    };
}
