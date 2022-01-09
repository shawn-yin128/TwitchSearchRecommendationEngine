package com.twitch.twitchproject.dao;

import com.twitch.twitchproject.entity.db.Item;
import com.twitch.twitchproject.entity.db.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class FavoriteDao {
    private SessionFactory sessionFactory;

    @Autowired
    public FavoriteDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // insert a favorite item into db
    public void setFavoriteItem(String userId, Item item) {
        Session session = null;

        try {
            session = sessionFactory.openSession(); // access db
            User user = session.get(User.class, userId); // find the user based on userId
            user.getItemSet().add(item);
            session.beginTransaction(); // make sure atomicity
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback(); // if something wrong, rollback
        } finally {
            if (session != null) {
                session.close(); // close connection
            }
        }
    }

    // remove a favorite item into db
    public void unsetFavoriteItem(String userId, String itemId) {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            User user = session.get(User.class, userId);
            Item item = session.get(Item.class, itemId); // find the corresponding item
            user.getItemSet().remove(item);
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // get favorite items
    public Set<Item> getFavoriteItem(String userId) {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            return session.get(User.class, userId).getItemSet();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return new HashSet<>();
    }
}
