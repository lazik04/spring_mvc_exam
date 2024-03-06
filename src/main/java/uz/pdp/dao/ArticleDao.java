package uz.pdp.dao;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.springframework.stereotype.Repository;
import uz.pdp.domain.Article;
import uz.pdp.domain.User;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class ArticleDao {
    private final SessionFactory session;

    public List<Article> getListOfArticles() {
        return session.getCurrentSession().createQuery(" from Article ", Article.class).list();
    }

    public void addArticle(Article article) {
        session.getCurrentSession().persist(article);
    }

    public Article getArticleById(Long id) {
        return session.getCurrentSession().get(Article.class, id);
    }
}
