package com.example.brandon.trace;

import android.util.Base64;

/**
 * Created by Brandon on 9/3/2016.
 */
public class Message {
    public static final String TYPE_PART = "part";
    public static final String TYPE_DONE = "done";

    public String Type;
    public String File;
    public int Length;
    public String Body;

    public Message() {
    }

    // Retrieve and decode the message body
    public byte[] extractBody() {
        String body = Body.substring(0, Length);
        return Base64.decode(body, Base64.DEFAULT);
    }
}
