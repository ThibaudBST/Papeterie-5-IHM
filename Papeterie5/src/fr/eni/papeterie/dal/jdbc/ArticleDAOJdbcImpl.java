package fr.eni.papeterie.dal.jdbc;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Stylo;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.dal.ArticleDAO;
import fr.eni.papeterie.dal.DALException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class ArticleDAOJdbcImpl implements ArticleDAO {
    public Stylo stylo;
    public Ramette ramette;


    Connection con = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;

    // Insertion d'un article dans la BDD
    public void insert(Article article) throws SQLException {
        // Connexion à la BDD
        con = JdbcTools.getConnection();

        int i = 0;
        System.out.println("Insertion...");

        if (article instanceof Ramette) {
            String sql = "INSERT INTO Articles (reference, marque, designation, prixUnitaire, qteStock, grammage, type)" + "VALUES ('" + article.getReference() + "', '" +
                    article.getMarque() + "', '" +
                    article.getDesignation() + "', '" +
                    article.getPrixUnitaire() + "', '" +
                    article.getQteStock() + "', '" +
                    ((Ramette) article).getGrammage() + "', '" +
                    "ramette');";
            System.out.println(sql);
            pstmt = con.prepareStatement(sql, RETURN_GENERATED_KEYS);
            i = pstmt.executeUpdate();
            if (i == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    article.setIdArticle(rs.getInt(1));
                }
            }
            System.out.println(article.getIdArticle());

        } else if (article instanceof Stylo) {
            String sql = "INSERT INTO Articles (reference, marque, designation, prixUnitaire, qteStock, couleur, type) " + "VALUES ('" + article.getReference() + "', '" +
                    article.getMarque() + "', '" +
                    article.getDesignation() + "', '" +
                    article.getPrixUnitaire() + "', '" +
                    article.getQteStock() + "', '" +
                    ((Stylo) article).getCouleur() + "', '" +
                    "stylo');";
            System.out.println(sql);
            pstmt = con.prepareStatement(sql, RETURN_GENERATED_KEYS);
            i = pstmt.executeUpdate();
            if (i == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    article.setIdArticle(rs.getInt(1));
                }
            }
        }

        System.out.println("Données insérés dans la table...");

        // Fermeture de la connexion
        con.close();
    }

    // Selection d'un article en fonction de son ID
    public Article selectById(int idArticle) throws SQLException, DALException {
        // Connexion à la BDD
        con = JdbcTools.getConnection();
        stmt = con.createStatement();


        Article article = null;

        ResultSet rs = stmt.executeQuery("SELECT * FROM Articles WHERE idArticle =" + idArticle + ";");
        rs.next();

        if (rs.getString("couleur") != null) {
            article = new Stylo(rs.getInt("idArticle"), rs.getString("marque"), rs.getString("reference"), rs.getString("designation"),
                    rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), rs.getString("couleur"));
        } else {
            article = new Ramette(rs.getInt("idArticle"), rs.getString("marque"), rs.getString("reference"), rs.getString("designation"),
                    rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), rs.getInt("grammage"));
        }

        // Fermeture de la connexion
        con.close();
        return article;
    }

    // Selection de la liste complète des articles de la BDD
    public List<Article> selectAll() throws SQLException, DALException {
        // Connexion à la BDD
        con = JdbcTools.getConnection();
        stmt = con.createStatement();

        List<Article> articleList = new ArrayList<>();
        Article article = null;

        ResultSet rs = stmt.executeQuery("SELECT * FROM Articles;");
        while (rs.next()) {
            if (rs.getString("couleur") != null) {
                article = new Stylo(rs.getInt("idArticle"), rs.getString("marque"), rs.getString("reference"), rs.getString("designation"),
                        rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), rs.getString("couleur"));
            } else {
                article = new Ramette(rs.getInt("idArticle"), rs.getString("marque"), rs.getString("reference"), rs.getString("designation"),
                        rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), rs.getInt("grammage"));
            }
            articleList.add(article);
        }

        // Fermeture de la connexion
        con.close();
        return articleList;
    }

    // Selection d'un article par la marque
    public List<Article> selectByMarque(String marque) throws SQLException, DALException {
        // Connexion à la BDD
        con = JdbcTools.getConnection();
        stmt = con.createStatement();

        List<Article> articleList = new ArrayList<>();
        Article article = null;

        ResultSet rs = stmt.executeQuery("SELECT * FROM Articles WHERE marque = '" + marque + "';");
        while (rs.next()) {
            if (rs.getString("couleur") != null) {
                article = new Stylo(rs.getInt("idArticle"), rs.getString("marque"), rs.getString("reference"), rs.getString("designation"),
                        rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), rs.getString("couleur"));
            } else {
                article = new Ramette(rs.getInt("idArticle"), rs.getString("marque"), rs.getString("reference"), rs.getString("designation"),
                        rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), rs.getInt("grammage"));

            }
            articleList.add(article);
        }

        // Fermeture de la connexion
        con.close();
        return articleList;
    }

    // Selection d'un article par un mot-clé
    public List<Article> selectByMotCle(String motcle) throws SQLException, DALException {
        // Connexion à la BDD
        con = JdbcTools.getConnection();
        stmt = con.createStatement();

        List<Article> articleList = new ArrayList<>();
        Article article = null;

        ResultSet rs = stmt.executeQuery("SELECT * FROM Articles WHERE reference ='" + motcle + "' OR marque = '" + motcle + "' OR designation = '" + motcle + "';");
        while (rs.next()) {
            if (rs.getString("couleur") != null) {
                article = new Stylo(rs.getInt("idArticle"), rs.getString("marque"), rs.getString("reference"), rs.getString("designation"),
                        rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), rs.getString("couleur"));
            } else {
                article = new Ramette(rs.getInt("idArticle"), rs.getString("marque"), rs.getString("reference"), rs.getString("designation"),
                        rs.getFloat("prixUnitaire"), rs.getInt("qteStock"), rs.getInt("grammage"));

            }
            articleList.add(article);
        }

        // Fermeture de la connexion
        con.close();
        return articleList;
    }

    // Modification d'un article
    public void update(Article article) throws SQLException, DALException {
        // Connexion à la BDD
        con = JdbcTools.getConnection();
        stmt = con.createStatement();

        if (article instanceof Ramette) {
            String sql = "UPDATE Articles SET reference = '" + article.getReference() + "', marque = '" + article.getMarque() + "', designation = '" + article.getDesignation() + "', prixUnitaire = '" + article.getPrixUnitaire() + "', qteStock = '" + article.getQteStock() + "', grammage = '" + ((Ramette) article).getGrammage() + "' WHERE idArticle = " + article.getIdArticle() + ";";
            System.out.println(sql);
            stmt.executeUpdate(sql);
        } else if (article instanceof Stylo) {
            String sql = "UPDATE Articles SET reference = '" + article.getReference() + "', marque = '" + article.getMarque() + "', designation = '" + article.getDesignation() + "', prixUnitaire = '" + article.getPrixUnitaire() + "', qteStock = '" + article.getQteStock() + "', couleur = '" + ((Stylo) article).getCouleur() + "' WHERE idArticle = " + article.getIdArticle() + ";";
            System.out.println(sql);
            stmt.executeUpdate(sql);
        }

        // Fermeture de la connexion
        con.close();
    }

    // Suppresion d'un article par son ID
    public void delete(int idArticle) throws SQLException, DALException {
        // Connexion à la BDD
        con = JdbcTools.getConnection();
        stmt = con.createStatement();

        String sql = "DELETE FROM Articles WHERE idArticle = " + idArticle + ";";
        System.out.println(sql);
        stmt.executeUpdate(sql);

        // Fermeture de la connexion
        con.close();
    }
}
