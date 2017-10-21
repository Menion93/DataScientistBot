package main.java.session;

/**
 * Created by Andrea on 07/10/2017.
 */
public class MessageEntry {

    private String message;
    private boolean userMessage;
    private Long timestamp;

    public MessageEntry(String message, boolean userMessage){
        this.message = message;
        this.userMessage = userMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public MessageEntry(String message, boolean userMessage, long ts){
        this.message = message;
        this.userMessage = userMessage;
        this.timestamp = ts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isUserMessage() {
        return userMessage;
    }

    public void setUserMessage(boolean userMessage) {
        this.userMessage = userMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
