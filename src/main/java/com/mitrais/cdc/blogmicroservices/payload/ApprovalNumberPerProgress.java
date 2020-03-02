package com.mitrais.cdc.blogmicroservices.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalNumberPerProgress {

    private long y;
    private String label;

    public ApprovalNumberPerProgress(long y, String label) {
        this.y = y;
        this.label = label;
    }

    public ApprovalNumberPerProgress() {

    }
}
