package com.example.brandon.trace;

import android.util.Base64;

/**
 * Message class used to send & receive information via a ControlConnection.
 */
public class Message {
    public static final String NEW = "new";
    public static final String LIST = "list";
    public static final String PART = "part";
    public static final String DONE = "done";

    public String Type;
    public String File;
    public int Length;
    public String Body;

    public Message(String Type, String File, int Length, String Body) {
        this.Type = Type;
        this.File = File;
        this.Length = Length;
        this.Body = Body;
    }

    // Retrieve and decode the message body
    public byte[] extractBody() {
        String body = Body.substring(0, Length);
        return Base64.decode(body, Base64.DEFAULT);
    }
}
