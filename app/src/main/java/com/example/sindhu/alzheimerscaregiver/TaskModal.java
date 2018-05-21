package com.example.sindhu.alzheimerscaregiver;

import android.os.Parcel;
import android.os.Parcelable;


public class TaskModal implements Parcelable {
    public static final Creator<TaskModal> CREATOR = new Creator<TaskModal>() {
        @Override
        public TaskModal createFromParcel(Parcel in) {
            return new TaskModal(in);
        }

        @Override
        public TaskModal[] newArray(int size) {
            return new TaskModal[size];
        }
    };
    // Task variables
    private long id;
    private String name;
    private long timeInMilliseconds;
    private long timestamp;

    // Constructors
    public TaskModal() {
    }

    public TaskModal(String name, long timeInMilliseconds, long timestamp) {
        this.name = name;
        this.timeInMilliseconds = timeInMilliseconds;
        this.timestamp = timestamp;
    }

    public TaskModal(long id, String name, long timeInMilliseconds, long timestamp) {
        this.id = id;
        this.name = name;
        this.timeInMilliseconds = timeInMilliseconds;
        this.timestamp = timestamp;
    }

    protected TaskModal(Parcel in) {
        id = in.readLong();
        name = in.readString();
        timeInMilliseconds = in.readLong();
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeLong(timeInMilliseconds);
        dest.writeLong(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters
    public long getId() {
        return id;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public void setTimeInMilliseconds(long timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
