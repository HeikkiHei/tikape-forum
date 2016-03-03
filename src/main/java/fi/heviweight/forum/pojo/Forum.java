package fi.heviweight.forum.pojo;

public class Forum {

    private final int boardId;
    private final int topicId;
    private final String boardName;
    private final String description;
    private final String lastThread;
    private final String timestamp;
    private final int messageCount;

    public Forum(int boardId, int topicId, String boardName, String description, String lastThread, String timestamp, int messageCount) {
        this.boardId = boardId;
        this.topicId = topicId;
        this.boardName = boardName;
        this.description = description;
        this.lastThread = lastThread;
        this.timestamp = timestamp.split("\\.")[0];
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

    public String getLastThread() {
        return lastThread;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getBoardId() {
        return boardId;
    }

    public int getMessageCount() {
        return messageCount;
    }
}
