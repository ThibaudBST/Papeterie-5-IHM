package fr.eni.papeterie.dal;

import fr.eni.papeterie.bo.Article;

import java.sql.SQLException;
import java.util.List;

public interface ArticleDAO {

    Article selectById(int id) throws SQLException, DALException;

    List<Article> selectAll() throws SQLException, DALException;

    List<Article> selectByMarque(String marque) throws SQLException, DALException;

    List<Article> selectByMotCle(String motcle) throws SQLException, DALException;

    void update(Article article) throws SQLException, DALException;

    void delete(int id) throws SQLException, DALException;

    void insert(Article article) throws SQLException, DALException;


}
