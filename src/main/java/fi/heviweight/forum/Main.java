package fi.heviweight.forum;

import fi.heviweight.forum.db.Database;
import fi.heviweight.forum.pojo.*;
import java.util.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        String jdbcOsoite = "jdbc:sqlite:src/heviwait.db";
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        }
        Database db = new Database(jdbcOsoite);

        ForumDao fd = new ForumDao(db);
        TopicDao td = new TopicDao(db);
        PostDao pd = new PostDao(db);

        get("/forum", (req, res) -> {
            HashMap<String, List<Forum>> map = new HashMap<>();
            map.put("boards", fd.findAll());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/board", (req, res) -> {
            HashMap<String, Object> map = new HashMap<>();
            int i = Integer.parseInt(req.queryParams("boardId"));
            List<Topic> b = td.getTopics(i);
            map.put("topics", b);
            map.put("bName", b.get(0).getBoard());
            map.put("id", i);
            return new ModelAndView(map, "board");
        }, new ThymeleafTemplateEngine());

        post("/board", (req, res) -> {
            int i = 0;
            try {
                int bId = Integer.parseInt(req.queryParams("boardId"));
                String topic = req.queryParams("topicName");
                if (topic.length() > 25) {
                    topic = topic.substring(0, 25);
                }
                i = td.addTopic(topic, bId);
            } catch (Exception e) {
            }
            if (i == 0) {
                res.status(404);
            }

            res.redirect("/topic?topicId=" + i, 307);
            return "";
        });

        get("/topic", (req, res) -> {
            HashMap<String, Object> map = new HashMap<>();
            int i = Integer.parseInt(req.queryParams("topicId"));
            List<Post> p = pd.getPosts(i);
            map.put("posts", p);
            map.put("id", p.get(0).getTopicId());
            map.put("tName", p.get(0).getTopic());
            map.put("bName", p.get(0).getBoard());
            map.put("bId", p.get(0).getBoardId());
            return new ModelAndView(map, "topic");
        }, new ThymeleafTemplateEngine());

        post("/topic", (req, res) -> {
            boolean success = true;
            try {
                req.queryParams("nick").isEmpty();
                System.out.println("Post/topic nick.isEmpty()");
            } catch (Exception e) {
                success = false;
                System.out.println("Post/topic no nick.");
            }

            if (success && (!req.queryParams("nick").isEmpty() || !req.queryParams("message").isEmpty())) {
                try {
                    System.out.println("Post/topic tryPost");
                    String nick = req.queryParams("nick");
                    if (nick.length() > 15) {
                        nick = nick.substring(0, 15);
                    }
                    String message = req.queryParams("message");
                    if (message.length() > 2000) {
                        message = message.substring(0, 2000);
                    }
                    new PostDao(db).addPost(nick, message, Integer.parseInt(req.queryParams("topicId")));
                } catch (Exception e) {
                    System.out.println("Exception caught in Post/topic.");
                    throw e;
                }
            }
            HashMap<String, Object> map = new HashMap<>();
            if (!success) {
                List<Post> p = pd.getPosts(Integer.parseInt(req.queryParams("topicId")));
                map.put("posts", p);
                map.put("id", Integer.parseInt(req.queryParams("topicId")));
                map.put("tName", "Make an opening post");
//                map.put("bName", p.get(0).getBoard());
//                map.put("bId", p.get(0).getBoardId());
            } else {
                int i = Integer.parseInt(req.queryParams("topicId"));
                map.put("posts", pd.getPosts(i));
                List<Post> p = pd.getPosts(i);
                map.put("id", p.get(0).getTopicId());
                map.put("tName", p.get(0).getTopic());
                map.put("bName", p.get(0).getBoard());
                map.put("bId", p.get(0).getBoardId());
            }
            return new ModelAndView(map, "topic");
        }, new ThymeleafTemplateEngine());
    }
}
