package com.uniqueGames.repository;

import com.uniqueGames.model.Game;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GameMapper {
    @Select("SELECT COUNT(*) FROM TB_GAME")
    int totRowCount();

    @Select("SELECT COUNT(*) FROM TB_GAME WHERE NAME LIKE CONCAT('%', #{keyword}, '%')")
    int totRowCountSearch(String keyword);

    @Select("SELECT * FROM (SELECT ROW_NUMBER() OVER(ORDER BY ${order1} ${order2}) AS RNO, id, name FROM TB_GAME) AS TB1 WHERE RNO BETWEEN ${start} AND ${end}")
    List<Game> aGetGameList(@Param("order1") String order1, @Param("order2") String order2, @Param("start") int start, @Param("end") int end);


    @Select("SELECT * FROM TB_GAME WHERE ID=#{id}")
    Game getGameForIndex(int id);

    @Select("SELECT * FROM TB_GAME_IMAGE WHERE G_ID=#{gId}")
    List<Game> getGameImg(int gId);

    @Select("SELECT * FROM TB_GAME_IMAGE WHERE G_ID=#{gId} LIMIT 1")
    Game getOneFile(int gId);

    @Select("SELECT * FROM TB_GAME")
    List<Game> getGameAllList();

    @Insert("INSERT INTO TB_GAME (NAME, GAME_GENRE, DESCRIPTION) VALUES (#{name}, #{genre}, #{description})")
    int aRegisterGame(String name, String genre, String description);

    @Select("SELECT ID FROM TB_GAME WHERE NAME = #{name}")
    int aGetGid(String name);

    @Delete("DELETE FROM TB_GAME WHERE ID = #{gid}")
    int aDeleteGame(int gid);

    @Update("UPDATE TB_GAME SET NAME = #{name}, GAME_GENRE = #{genre}, DESCRIPTION = #{description} WHERE ID = #{gid}")
    int aUpdateGame(String name, String genre, String description, int gid);

    @Update("UPDATE TB_GAME_IMAGE SET UPLOAD_IMG = #{imagePath} LIMIT 1")
    int aUpdateGameImg(String imagePath);

    @Select("SELECT UPLOAD_IMG FROM TB_GAME_IMAGE WHERE G_ID = #{id} LIMIT 1")
    String aGetGameImg(int id);

    @Insert("INSERT INTO TB_GAME_IMAGE (G_ID, UPLOAD_IMG) VALUES (#{gid}, #{imagePath})")
    int aRegisterGameImg(int gid,String imagePath);
}
