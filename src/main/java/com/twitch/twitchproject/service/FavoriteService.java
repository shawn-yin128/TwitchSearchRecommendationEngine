package com.twitch.twitchproject.service;

import com.twitch.twitchproject.dao.FavoriteDao;
import com.twitch.twitchproject.entity.db.Item;
import com.twitch.twitchproject.entity.db.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FavoriteService {
    private FavoriteDao favoriteDao;

    @Autowired
    public FavoriteService(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    public void setFavoriteItem(String userId, Item item) {
        favoriteDao.setFavoriteItem(userId, item);
    }

    public void unsetFavoriteItem(String userId, String itemId) {
        favoriteDao.unsetFavoriteItem(userId, itemId);
    }

    public Map<String, List<Item>> getFavoriteItem(String userId) {
        Map<String, List<Item>> itemMap = new HashMap<>();
        for (ItemType itemType : ItemType.values()) {
            itemMap.put(itemType.toString(), new ArrayList<>());
        }

        Set<Item> favorites = favoriteDao.getFavoriteItem(userId);
        for(Item item : favorites) {
            itemMap.get(item.getType().toString()).add(item);
        }
        return itemMap;
    }
}
