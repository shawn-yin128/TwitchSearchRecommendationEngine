package com.twitch.twitchbackend.dao;

import com.twitch.twitchbackend.entity.db.Item;
import com.twitch.twitchbackend.entity.db.ItemType;
import com.twitch.twitchbackend.entity.db.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

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

    // get favorite item id
    public Set<String> getFavoriteItemIds(String userId) {
        Set<String> itemIds = new HashSet<>();
        Session session = null;

        try {
            session = sessionFactory.openSession();
            Set<Item> items = session.get(User.class, userId).getItemSet();
            for(Item item : items) {
                itemIds.add(item.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return itemIds;
    }

    // get favorite games according to different types, for one type, there may be duplicate game since there are different items and some may be same game
    public Map<String, List<String>> getFavoriteGameIds(Set<String> favoriteItemIds) {
        Map<String, List<String>> itemMap = new HashMap<>();

        for (ItemType type : ItemType.values()) {
            itemMap.put(type.toString(), new ArrayList<>());
        }

        Session session = null;

        try {
            session = sessionFactory.openSession();
            for(String itemId : favoriteItemIds) {
                Item item = session.get(Item.class, itemId);
                itemMap.get(item.getType().toString()).add(item.getGameId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return itemMap;
    }
}
