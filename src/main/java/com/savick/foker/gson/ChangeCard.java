package com.savick.foker.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by skaveesh on 2017-06-24.
 */
public class ChangeCard {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("isAllowed")
    @Expose
    private boolean isAllowed;

    public ChangeCard(String message, boolean isAllowed) {
        this.message = message;
        this.isAllowed = isAllowed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }
}
