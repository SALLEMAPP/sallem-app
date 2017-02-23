package com.seniorproject.sallemapp.entities;

import java.lang.ref.PhantomReference;

/**
 * Created by Centeral on 2/21/2017.
 */

public class AbuseType {
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public Type getType() {
        return _type;
    }

    public void setType(Type type) {
        _type = type;
    }

    public enum Type{
        RACISM,
        HATERED,
        VIOLENCE,
        POLITICAL,
        HARRASSMENT,
        SEXUAL
    }

    private int _id;
    private Type _type;
}
