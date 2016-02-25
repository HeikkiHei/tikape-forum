/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.heviweight.forum.pojo;

public class Post {
    private final int boardId;
    private final int topicId;
    private final String poster;
    private final String content;
    private final String topic;
    private final String board;
    private final String timestamp;

    public Post(int topicId, int boardId, String poster, String content, String topic, String board, String timestamp) {
        this.boardId = boardId;
        this.topicId = topicId;
        this.poster = poster;
        this.content = content;
        this.topic = topic;
        this.board = board;
        this.timestamp = timestamp;
    }

    public int getBoardId() {
        return boardId;
    }

    public int getTopicId() {
        return topicId;
    }
    
    public String getPoster() {
        return poster;
    }

    public String getContent() {
        return content;
    }

    public String getTopic() {
        return topic;
    }

    public String getBoard() {
        return board;
    }

    public String getTimestamp() {
        return timestamp;
    }
    
}
