package com.basecare.ding192.data.entity;

/**
 * Created by dingrui 2019/11/6
 */

public class UploadAttachmentBean {

    /**
     * mimetype : image/jpeg
     * id : 483
     * filename : 8-1.jpg
     */

    private String mimetype;
    private int id;
    private String filename;
    private String description;

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UploadAttachmentBean{" +
                "mimetype='" + mimetype + '\'' +
                ", id=" + id +
                ", filename='" + filename + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
