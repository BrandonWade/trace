package com.example.brandon.trace;

/**
 * Message class used to send & receive information via a ControlConnection.
 */
public class Message {
    public static final String NEW = "new";
    public static final String LIST = "list";
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
}
