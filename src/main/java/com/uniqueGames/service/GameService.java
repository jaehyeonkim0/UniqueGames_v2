package com.uniqueGames.service;

import com.uniqueGames.model.Game;
import com.uniqueGames.model.Member;
import com.uniqueGames.model.Order;
import com.uniqueGames.repository.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Repository
@Service
public class GameService {
    @Autowired
    GameMapper gameMapper;

    public int totRowCount() {
        return gameMapper.totRowCount();
    }

    public int totRowCountSearch(String keyword) {
        return gameMapper.totRowCountSearch(keyword);
    }

    public ArrayList<Game> aGetGameList(String order1, String order2, int start, int end) {
        ArrayList<Game> gList = new ArrayList<>();
        for (Game game : gameMapper.aGetGameList(order1, order2, start, end)) {
            gList.add(game);
        }
        return gList;
    }

    public Game aGetGame(int id) {
        return gameMapper.aGetGame(id);
    }

    public ArrayList<Order> addGameInfo(ArrayList<Order> cartList) {
        for (int i = 0; i < cartList.size(); i++) {
            int gid = cartList.get(i).getGId();
            Game game = gameMapper.aGetGame(gid);

            cartList.get(i).setGameImg(game.getImagePath());
            cartList.get(i).setGametitle(game.getName());
        }

        return cartList;
    }

    public int aRegisterGame(String name, String genre, String imagePath, String description) {
        return gameMapper.aRegisterGame(name, genre, imagePath, description);
    }

    public int aGetGid(String name) {
        return gameMapper.aGetGid(name);
    }

    public int aDeleteGame(int gid) {
        return gameMapper.aDeleteGame(gid);
    }

    public int aUpdateGame(String name, String genre, String imagePath, String description, int gid) {
        return gameMapper.aUpdateGame(name, genre, imagePath, description, gid);
    }
}
