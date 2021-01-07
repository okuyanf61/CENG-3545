package com.mehmetfatih.roomdemo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity
public class Department implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

}
