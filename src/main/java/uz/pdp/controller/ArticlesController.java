package uz.pdp.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;
import uz.pdp.dao.ArticleDao;
import uz.pdp.dao.CommentDao;
import uz.pdp.domain.Article;
import uz.pdp.domain.Comment;
import uz.pdp.domain.User;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Transactional
public class ArticlesController {

    private final ArticleDao articleDao;
    private final SessionFactory sessionFactory;
    private final CommentDao commentDao;

    @GetMapping("/articles")
    public ModelAndView articles(ModelAndView modelAndView) {
        List<Article> listOfArticles = articleDao.getListOfArticles();
        modelAndView.addObject("articlesList", listOfArticles);
        modelAndView.setViewName("articles");
        return modelAndView;
    }

    @GetMapping("/add/article")
    public String addArticle() {
        return "add_article";
    }

    @PostMapping("/add/article")
    public String addArticle1(@ModelAttribute Article article, @RequestParam("name") String name) {
        User user = sessionFactory.getCurrentSession().createQuery(
                        "select u from User u where username=:name", User.class)
                .setParameter("name", name).uniqueResult();
        article.setLikes(0L);
        article.setAuthor(user);
        articleDao.addArticle(article);
        return "add_article";
    }

    @GetMapping("articles/read/{id}")
    public ModelAndView addDeveloper(ModelAndView modelAndView, @PathVariable("id") Long id) {
        Article article = articleDao.getArticleById(id);
        List<Comment> listOfComments = commentDao.getListOfComments();
        modelAndView.addObject("comments",listOfComments);
        modelAndView.addObject("article", article);
        modelAndView.setViewName("read");
        return modelAndView;
    }

    @PostMapping("/articles/read/{id}")
    public ModelAndView likeComment(ModelAndView modelAndView,
                                    @PathVariable Long id,
//                                    @RequestParam("articleId") Long articleId,
                                    @RequestParam("username") String username,
                                    @RequestParam("comment") String comment
    ) {
//        articleDao.setLikesById(id);
        User user = sessionFactory.getCurrentSession().createQuery(
                        "select u from User u where username=:name", User.class)
                .setParameter("name", username).uniqueResult();
        Comment.builder().content(comment)
                        .commentBy(user);
        modelAndView.setViewName("read");
        return modelAndView;
    }
}

