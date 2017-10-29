package in.arpaul.inshorts.dataobjects;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aritrapal on 12/09/17.
 */

public class NewsDO extends BaseDO implements Parcelable {
    @SerializedName("ID")
    @Expose
    public String id;
    @SerializedName("TITLE")
    @Expose
    public String title;
    @SerializedName("URL")
    @Expose
    public String url;
    @SerializedName("PUBLISHER")
    @Expose
    public String publisher;
    @SerializedName("CATEGORY")
    @Expose
    public String category;
    @SerializedName("HOSTNAME")
    @Expose
    public String hostname;
    @SerializedName("TIMESTAMP")
    @Expose
    public long timestamp;

    public NewsDO() {
    }

    public NewsDO(String id, String title, String url, String publisher, String category, String hostname, long timestamp) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.publisher = publisher;
        this.category = category;
        this.hostname = hostname;
        this.timestamp = timestamp;
    }

    protected NewsDO(Parcel in) {
        id = in.readString();
        title = in.readString();
        url = in.readString();
        publisher = in.readString();
        category = in.readString();
        hostname = in.readString();
        timestamp = in.readLong();
    }

    public static final Creator<NewsDO> CREATOR = new Creator<NewsDO>() {
        @Override
        public NewsDO createFromParcel(Parcel in) {
            return new NewsDO(in);
        }

        @Override
        public NewsDO[] newArray(int size) {
            return new NewsDO[size];
        }
    };

    @Override
    public String toString() {
        return "NewsDO{" +
                "id=" + id +
                ", title=" + title +
                ", url=" + url +
                ", publisher=" + publisher +
                ", category=" + category +
                ", hostname=" + hostname +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(publisher);
        dest.writeString(category);
        dest.writeString(hostname);
        dest.writeLong(timestamp);
    }
}
