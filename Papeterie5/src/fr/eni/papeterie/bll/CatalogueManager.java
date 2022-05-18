package fr.eni.papeterie.bll;

import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;
import fr.eni.papeterie.dal.ArticleDAO;
import fr.eni.papeterie.dal.DALException;
import fr.eni.papeterie.dal.DAOFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class CatalogueManager {

    private static ArticleDAO daoArticles;
    private static CatalogueManager instance;

    public CatalogueManager(){
        daoArticles = DAOFactory.getArticleDAO();
    }

    public static CatalogueManager getInstance() throws BLLException {
        if (instance == null) {
            instance = new CatalogueManager();
        }
        return instance;
    }

    public void addArticle(Article article) throws BLLException {
        try {
            validerArticle(article);
            daoArticles.insert(article);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BLLException("Impossible d'entrer une donnée.");
        }
    }


    public List<Article> getCatalogue() throws BLLException {
        List<Article> listeArticles;
        try{
            listeArticles = daoArticles.selectAll();
        }catch (Exception e) {
            e.printStackTrace();
            throw new BLLException("Impossible d'afficher le catalogue.");
        }
        return listeArticles;
    }

    public void updateArticle(Article article) throws BLLException {
        try{
            validerArticle(article);
            daoArticles.update(article);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BLLException("Impossible de modifier l'article.");
        }
    }
    public void validerArticle (Article article) throws BLLException {
        if (article.getDesignation() == null || Objects.equals(article.getDesignation(), "")) {
            throw new BLLException("Veuillez indiquer une designation.");
        }
        if (article.getReference() == null || Objects.equals(article.getReference(), "")) {
            throw new BLLException("Veuillez indiquer une référence.");
        }
        if (article.getMarque() == null || Objects.equals(article.getMarque(), "")) {
            throw new BLLException("Veuillez indiquer une marque.");
        }
        if (article.getQteStock() < 0) {
            throw new BLLException("Veuillez indiquer une quantité de stock.");
        }
        if (article.getPrixUnitaire() < 0) {
            throw new BLLException("Veuillez indiquer un prix unitaire.");
        }
        if (article instanceof Stylo && ((Stylo) article).getCouleur() == null) {
            throw new BLLException("Veuillez noter la couleur du stylo.");
        }
        if (article instanceof Ramette && ((Ramette) article).getGrammage() < 0) {
            throw new BLLException("Veuillez indiquer un grammage.");
        }
    }

    public void removeArticle(int index) throws BLLException {
        try{
            daoArticles.delete(index);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BLLException("Impossible de modifier l'article.");
        }
    }

    public Article getArticle(int index) throws DALException, SQLException {
        return daoArticles.selectById(index);
    }
}
