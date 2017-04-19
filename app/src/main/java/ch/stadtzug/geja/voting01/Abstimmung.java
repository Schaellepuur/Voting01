package ch.stadtzug.geja.voting01;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by gerlach jan on 11.04.2017.
 */

class Abstimmung implements Parcelable {
    private String id;
    private String name;
    private String abstimmungstext;
    private String option1, option2, option3;

    public Abstimmung(String id, String name, String abstimmungstext) {
        this.id = id;
        this.name = name;
        this.abstimmungstext = abstimmungstext;
    }

    protected Abstimmung(Parcel in) {
        id = in.readString();
        name = in.readString();
        abstimmungstext = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
    }

    public static final Creator<Abstimmung> CREATOR = new Creator<Abstimmung>() {
        @Override
        public Abstimmung createFromParcel(Parcel in) {
            return new Abstimmung(in);
        }

        @Override
        public Abstimmung[] newArray(int size) {
            return new Abstimmung[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbstimmungstext() {
        return abstimmungstext;
    }

    public void setAbstimmungstext(String abstimmungstext) {
        this.abstimmungstext = abstimmungstext;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(abstimmungstext);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
    }
}
