package com.cloudwalk.livenesscameraproject.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity
public class PersonFeatureModel {

    @Id
    private Long id;

    @Property
    private String name;

    @Property
    private String path;

    @Property
    private byte[] feature_data;

    @Generated(hash = 503085656)
    public PersonFeatureModel(Long id, String name, String path,
            byte[] feature_data) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.feature_data = feature_data;
    }

    @Generated(hash = 346097427)
    public PersonFeatureModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFeature_data() {
        return feature_data;
    }

    public void setFeature_data(byte[] feature_data) {
        this.feature_data = feature_data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
