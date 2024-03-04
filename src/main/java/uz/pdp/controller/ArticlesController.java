package uz.pdp.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.dao.ArticleDao;
import uz.pdp.domain.Article;
import uz.pdp.domain.User;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Transactional
public class ArticlesController {
    private final ArticleDao articleDao;
    private final SessionFactory sessionFactory;
    @GetMapping("/articles")
    public ModelAndView articles(ModelAndView modelAndView){
        List<Article> listOfArticles = articleDao.getListOfArticles();
        modelAndView.addObject("articlesList",listOfArticles);
        modelAndView.setViewName("articles");
        return modelAndView;
    }

    @GetMapping("/add/article")
    public String addArticle(){
        return "add_article";
    }

    @PostMapping("/add/article")
    public String addArticle1(@ModelAttribute Article article, @RequestParam("name") String name){
        User user = sessionFactory.getCurrentSession().createQuery(
                "select u from User u where username=:name", User.class)
                .setParameter("name", name).uniqueResult();
        article.setLikes(0L);
        article.setAuthor(user);
        articleDao.addArticle(article);
        return "add_article";
    }

}
