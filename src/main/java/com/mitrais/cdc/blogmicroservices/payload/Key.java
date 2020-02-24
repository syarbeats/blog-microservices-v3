package com.mitrais.cdc.blogmicroservices.payload;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Key implements Serializable {

    private String key;

    public Key(String key) {
        this.key = key;
    }

    public Key() {
    }
}
