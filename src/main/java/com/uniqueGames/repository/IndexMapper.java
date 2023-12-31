package com.uniqueGames.repository;


import com.uniqueGames.config.Login;
import com.uniqueGames.model.Game;
import java.util.ArrayList;
import java.util.List;

import com.uniqueGames.model.Member;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@Mapper
public interface IndexMapper {

    @Select("SELECT\n" +
            "    TB_GAME.ID,\n" +
            "    TB_GAME.NAME,\n" +
            "    TB_GAME.GAME_GENRE,\n" +
            "    TB_GAME.DONATION_STATUS,\n" +
            "    TB_GAME.DESCRIPTION,\n" +
            "    COUNT(TB_LIKE.ID) AS LIKE_COUNT\n" +
            "FROM\n" +
            "    TB_GAME\n" +
            "        LEFT JOIN\n" +
            "    TB_LIKE ON TB_GAME.ID = TB_LIKE.G_ID\n" +
            "GROUP BY\n" +
            "    TB_GAME.ID,\n" +
            "    TB_GAME.NAME,\n" +
            "    TB_GAME.GAME_GENRE,\n" +
            "    TB_GAME.DONATION_STATUS,\n" +
            "    TB_GAME.DESCRIPTION;")
    List<Game> getGameList();

    @Select("SELECT * FROM TB_GAME WHERE ID=?#{id}")
    Game getGame(Game vo);

    @Select("SELECT * FROM TB_GAME WHERE DONATION_STATUS = 1")
    List<Game> getDonationList();

    @Select("SELECT COUNT(*) FROM TB_LIKE WHERE G_ID= #{gId}")
    int getGameLikeCount(@Param("gId") int gId);

    @Select("SELECT ID, NAME, GAME_GENRE, DONATION_STATUS, DESCRIPTION FROM TB_GAME")
    List<Game> getRankingList();

    @Select("SELECT COUNT(*) FROM TB_LIKE where G_ID = #{gId} and M_ID = #{memberId}")
    int hasLiked(@Param("memberId") String memberId, @Param("gId") int gId);

    @Update("INSERT INTO TB_LIKE (G_ID, M_ID) VALUE(#{gId},#{memberId})")
    void addLikeInfo(@Param("memberId") String memberId, @Param("gId") int gId);

    @Delete("DELETE FROM TB_LIKE WHERE M_ID = #{memberId} and G_ID = #{gId}")
    void removeLikeInfo(@Param("memberId") String memberId, @Param("gId") int gId);
}
