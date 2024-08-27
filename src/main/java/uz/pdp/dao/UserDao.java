package uz.pdp.dao;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.domain.User;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserDao {

    private final SessionFactory session;

    public User getUserByUsername(final String username) {
        return session.getCurrentSession()
                .createNativeQuery("select * from users where username = ?1", User.class)
                .setParameter(1, username)
                .uniqueResult();
    }

    public void save(User user) {
        String s = session.getCurrentSession().createQuery(
                        "select username from User  where username=:username", String.class)
                .setParameter("username", user.getUsername()).uniqueResult();
        if(s==null){
        session.getCurrentSession()
                .persist(user);
        }
        throw new RuntimeException("Email already exist");
    }

    public User update(User user){
        return session.getCurrentSession()
                .merge(user);
    }


    public User getById(long id){
        return session.getCurrentSession()
                .get(User.class, id);
    }

    public void delete(long id){
        session.getCurrentSession()
                .remove(getById(id));
    }

    public List<User> getAll(){
        return session.getCurrentSession()
                .createQuery("from User", User.class)
                .list();
    }
}
