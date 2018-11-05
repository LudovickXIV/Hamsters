
package test.ludovick.com.hamsters.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hamster implements Parcelable{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;

    public Hamster(){

    }

    protected Hamster(Parcel in) {
        title = in.readString();
        description = in.readString();
        image = in.readString();
    }

    public static final Creator<Hamster> CREATOR = new Creator<Hamster>() {
        @Override
        public Hamster createFromParcel(Parcel in) {
            return new Hamster(in);
        }

        @Override
        public Hamster[] newArray(int size) {
            return new Hamster[size];
        }
    };

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(image);
    }
}
