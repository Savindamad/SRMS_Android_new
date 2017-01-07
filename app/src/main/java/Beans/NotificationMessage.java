package Beans;

public class NotificationMessage {
    String messageId;
    String message;
    String sentUserId;
    String sentUserName;
    String receivedUserId;
    String receivedUserName;
    String tableNo;
    String status;
    String time;

    public NotificationMessage( String messageId, String message, String sentUserId, String sentUserName, String receivedUserId, String receivedUserName, String status, String tableNo) {
        this.status = status;
        this.receivedUserName = receivedUserName;
        this.receivedUserId = receivedUserId;
        this.sentUserName = sentUserName;
        this.sentUserId = sentUserId;
        this.messageId = messageId;
        this.message = message;
        this.tableNo = tableNo;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public String getSentUserId() {
        return sentUserId;
    }

    public String getSentUserName() {
        return sentUserName;
    }

    public String getReceivedUserId() {
        return receivedUserId;
    }

    public String getReceivedUserName() {
        return receivedUserName;
    }

    public String getTableNo() {
        return tableNo;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
