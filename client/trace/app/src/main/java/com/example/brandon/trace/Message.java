package com.example.brandon.trace;

/**
 * Message class used to send & receive information via a ControlConnection.
 */
public class Message {
    public static final String NEW = "new";
    public static final String DONE = "done";

    public String Type;
    public String File;
    public String Body;

    public Message(String Type, String File, String Body) {
        this.Type = Type;
        this.File = File;
        this.Body = Body;
    }
}
