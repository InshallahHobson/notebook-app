package com.example.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Inshallah Hobson
 *
 * A note object that stores the note contents and is parcelled to transfer between activities
 */
public class Note implements Parcelable {

    private String title = "Untitled note";
    private String content;

    public Note() {
    }


    ;

    /**
     * Constructor to use when re-constructing object from a parcel
     *
     * @param in a parcel from which to read this object
     */
    public Note(Parcel in) {
        readFromParcel(in);
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        // Writing each field into the parcel
        dest.writeString(title);
        dest.writeString(content);
    }

    /**
     * Called from the constructor to create this object from a parcel.
     *
     * @param in parcel from which to re-create object
     */
    private void readFromParcel(Parcel in) {

        // Read back each field written to the parcel
        title = in.readString();
        content = in.readString();
    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     *
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     *
     * I just find it easier to use the constructor.
     * It makes sense for the way my brain thinks ;-)
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Note createFromParcel(Parcel in) {
                    return new Note(in);
                }

                public Note[] newArray(int size) {
                    return new Note[size];
                }
            };

}
