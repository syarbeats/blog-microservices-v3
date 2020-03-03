package com.mitrais.cdc.blogmicroservices.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalResultStatistic {
    private long y;
    private String label;

    public ApprovalResultStatistic(long y, String label) {
        this.y = y;
        this.label = label;
    }

    public ApprovalResultStatistic() {
    }
}
