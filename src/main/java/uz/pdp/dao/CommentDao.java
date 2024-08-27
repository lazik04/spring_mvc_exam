package uz.pdp.dao;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import uz.pdp.domain.Article;
import uz.pdp.domain.Comment;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class CommentDao {
    private final SessionFactory sessionFactory;

    public void addComment(Comment comment){
        sessionFactory.getCurrentSession().persist(comment);
    }

    public List<Comment> getListOfComments() {
        return sessionFactory.getCurrentSession().createQuery(" from Comment ", Comment.class).list();
    }
}
