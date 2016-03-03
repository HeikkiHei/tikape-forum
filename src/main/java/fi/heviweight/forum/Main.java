package fi.heviweight.forum;

import fi.heviweight.forum.db.Database;
import fi.heviweight.forum.pojo.*;
import java.util.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws Exception {
        Database db = new Database("jdbc:sqlite:src/heviwait.db");
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
                i = td.addTopic(req.queryParams("topicName"), bId);
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
            } catch (Exception e) {
                success = false;
            }

            if (success && (!req.queryParams("nick").isEmpty() || !req.queryParams("message").isEmpty())) {
                try {
                    new PostDao(db).addPost(req.queryParams("nick"), req.queryParams("message"), Integer.parseInt(req.queryParams("topicId")));
                } catch (Exception e) {
                }
            }
            HashMap<String, Object> map = new HashMap<>();
            if (!success) {
                List<Post> p = pd.getPosts(Integer.parseInt(req.queryParams("topicId")));
                map.put("posts", p);
                map.put("id", Integer.parseInt(req.queryParams("topicId")));
                map.put("tName", "Make an opening post");
                map.put("bName", p.get(0).getBoard());
                map.put("bId", p.get(0).getBoardId());
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
