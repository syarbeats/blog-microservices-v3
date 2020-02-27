package com.mitrais.cdc.blogmicroservices.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RowNum {

    private long rownum;

    public RowNum(long rownum) {
        this.rownum = rownum;
    }

    public RowNum() {
    }
}
