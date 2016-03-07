package fi.heviweight.forum.pojo;

public class Forum {

    private final int boardId;
    private final String boardName;
    private final String description;
    private final String timestamp;
    private final int messageCount;

    public Forum(int boardId, String boardName, String description, String timestamp, int messageCount) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.description = description;
        this.timestamp = timestamp;
        this.messageCount = messageCount;
    }

    public int getId() {
        return boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getBoardId() {
        return boardId;
    }

    public int getMessageCount() {
        return messageCount;
    }
}
