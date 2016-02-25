
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
            //System.out.println(map.get("boards").get(0).getBoardName());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/board", (req, res) -> {
            HashMap<String, List<Topic>> map = new HashMap<>();
            int i = Integer.parseInt(req.queryParams("boardID"));
            map.put("topics", td.getTopics(i));
            //System.out.println("board ID?" + i);
            return new ModelAndView(map, "board");
        }, new ThymeleafTemplateEngine());

        post("/board", (req, res) -> {
            int i = new TopicDao(db).addTopic(req.queryParams("heading"), Integer.parseInt(req.queryParams("boardID")));
            String r = "";
            for (Post p : new PostDao(db).getPosts(i)) {
                r = r + "<p>" + p.getPoster() + ": " + p.getContent() + "</p>\n";
            }
            return r;
        });

        get("/topic", (req, res) -> {
            HashMap<String, List<Post>> map = new HashMap<>();
            int i = Integer.parseInt(req.queryParams("topicID"));
            map.put("posts", pd.getPosts(i));
            return new ModelAndView(map, "topic");
        }, new ThymeleafTemplateEngine());

        post("/topic", (req, res) -> {
            String r = "";
            new PostDao(db).addPost(req.queryParams("nick"), req.queryParams("message"), Integer.parseInt(req.queryParams("topicId")));
            for (Post p : new PostDao(db).getPosts(Integer.parseInt(req.queryParams("topicID")))) {
                r = r + "<p>" + p.getPoster() + ": " + p.getContent() + "</p>\n";
            }
            return r;
        });
    }
}
