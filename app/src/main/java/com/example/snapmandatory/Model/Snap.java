package com.example.snapmandatory.Model;

import android.graphics.Bitmap;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Snap {

    private String id;
    private String user;
    private String image_id;
    private Bitmap image;
    private LocalDateTime localDateTime;

    public Snap(String id, String user, String image_id, Bitmap image, LocalDateTime localDateTime) {
        this.id = id;
        this.user = user;
        this.image_id = image_id;
        this.image = image;
        this.localDateTime = localDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public String getImage_id() {
        return image_id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public long compareDates(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime limit_date = localDateTime.plusHours(24);

        long hours = ChronoUnit.HOURS.between(now, limit_date);
        return hours;
    }

    @Override
    public String toString() {
        return "Snap{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", image_id='" + image_id + '\'' +
                ", image=" + image +
                ", localDateTime=" + localDateTime.toString() +
                '}';
    }
}
