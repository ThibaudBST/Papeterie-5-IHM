/*
 * Created by JFormDesigner on Tue May 17 14:41:30 CEST 2022
 */

package fr.eni.papeterie.ihm;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import com.intellij.uiDesigner.core.*;
import fr.eni.papeterie.bll.BLLException;
import fr.eni.papeterie.bll.CatalogueManager;
import fr.eni.papeterie.bo.Article;
import fr.eni.papeterie.bo.Ramette;
import fr.eni.papeterie.bo.Stylo;


/**
 * @author unknown
 */
public class Ecran extends JFrame {
    public Ecran() throws SQLException {
        initComponents();
    }

    private Article article;
    List<Article> articleList = new ArrayList<Article>();
    CatalogueManager mger;
    int i = 0;
    public void listerArticles() {
        try{
        mger = CatalogueManager.getInstance();
        articleList = mger.getCatalogue();
    } catch (Exception e){
            e.printStackTrace();
        }
    }
    public Article getCatalogueArticle(List<Article> articleList, int index){
        article = articleList.get(index);
        return article;
    }
        private void rBtnRametteItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED){
            cbCouleur.setEnabled(false);
        } else if (e.getStateChange() == ItemEvent.DESELECTED){
            cbCouleur.setEnabled(true);
        }
    }

    private void rBtnStyloItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            check80grammes.setEnabled(false);
            check100grammes.setEnabled(false);
        } else if (e.getStateChange() == ItemEvent.DESELECTED){
            check80grammes.setEnabled(true);
            check100grammes.setEnabled(true);
        }
    }
    private void btnSave(ActionEvent e) {
        if (article.getIdArticle() == null) {
            try {
                mger.addArticle(saveNewArticle(article));
                articleList.add(saveNewArticle(article));
            } catch (BLLException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            try {
                mger.updateArticle(saveNewArticle(article));
            } catch (BLLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void btnDelete(ActionEvent e) {
        int index = article.getIdArticle();
        try {
            mger.removeArticle(index);
            articleList.remove(getCatalogueArticle(articleList, i).getIdArticle());
        } catch (BLLException ex) {
            throw new RuntimeException(ex);
        }
        i--;
        InitializeArticles();
    }

    private void btnForward(ActionEvent e) throws BLLException {
        if (i < articleList.size() - 1) {
            i++;
            balayageListe();
        } else  if (articleList.size() < getSourceList().size() + 1) { {newArticle();
            balayageListe();
            }
        }
    }

    private void btnBack(ActionEvent e) {
        if(i > 0){
            i--;
            balayageListe();
        }

    }
    private void btnNew(ActionEvent e) throws BLLException {
        if (articleList.size() < getSourceList().size() + 1) {
            newArticle();
            i = articleList.size() - 1;
            balayageListe();
            System.out.println(articleList);
        } else {
            i = articleList.size() -1;
            balayageListe();
        }
    }

    public List<Article> getSourceList() throws BLLException {
        List<Article> sourceList;
        try {
            sourceList = mger.getCatalogue();
        } catch (BLLException ex) {
            throw new RuntimeException(ex);
        }
        return sourceList;
    }
    public void balayageListe(){
        article = getCatalogueArticle(articleList, i);
        String newRef = (article.getReference());
        txtReference.setText(newRef);
        String newDesignation = (article.getDesignation());
        txtDesignation.setText(newDesignation);
        String newMarque = (article.getMarque());
        txtMarque.setText(newMarque);
        String newStock = (String.valueOf(article.getQteStock()));
        txtStock.setText(newStock);
        String newPrix = (String.valueOf(article.getPrixUnitaire()));
        txtPrix.setText(newPrix);
        if (article instanceof Ramette){
            rBtnRamette.setSelected(true);
            cbCouleur.setEnabled(false);
            cbCouleur.setSelectedIndex(-1);
            if(((Ramette) article).getGrammage() == 80){
                check80grammes.setSelected(true);
            } else if (((Ramette) article).getGrammage() == 100){
                check100grammes.setSelected(true);
            }
        } else if (article instanceof Stylo){
            rBtnStylo.setSelected(true);
            check80grammes.setSelected(false);
            check100grammes.setSelected(false);
            check80grammes.setEnabled(false);
            check100grammes.setEnabled(false);
            for(int i = 1; i < cbCouleur.getItemCount(); i++){
                if((Arrays.equals(((Stylo) article).getCouleur().toUpperCase().toCharArray(), cbCouleur.getItemAt(i).toUpperCase().toCharArray()))){
                    cbCouleur.setSelectedIndex(i);
                }
            }
        }
    }

    private void newArticle(){
        Article newArticle = new Ramette();
        txtReference.setText(null);
        txtMarque.setText(null);
        txtDesignation.setText(null);
        txtStock.setText(null);
        txtPrix.setText(null);
        rBtnRamette.setSelected(true);
        rBtnStylo.setSelected(false);
        check80grammes.setSelected(true);
        check100grammes.setSelected(false);
        cbCouleur.setSelectedIndex(0);
        articleList.add(newArticle);
    }
    private void InitializeArticles(){
        listerArticles();
        balayageListe();
    }

    public Article saveNewArticle(Article article){
        if(rBtnRamette.isSelected()){
            int grammage;
            if(check80grammes.isSelected()) {
                grammage = 80;
                article = new Ramette(article.getIdArticle(),txtMarque.getText(), txtReference.getText(), txtDesignation.getText(), Float.parseFloat(txtPrix.getText()), Integer.parseInt(txtStock.getText()), grammage);
            } else if(check100grammes.isSelected()){
                grammage = 100;
                article = new Ramette(article.getIdArticle(),txtMarque.getText(), txtReference.getText(), txtDesignation.getText(), Float.parseFloat(txtPrix.getText()), Integer.parseInt(txtStock.getText()), grammage);
            }
        } else if (rBtnStylo.isSelected()){
            int j;
            j = cbCouleur.getSelectedIndex();
            String couleur;
            couleur = cbCouleur.getItemAt(j);
            article = new Stylo(article.getIdArticle(), txtMarque.getText(), txtReference.getText(), txtDesignation.getText(), Float.parseFloat(txtPrix.getText()), Integer.parseInt(txtStock.getText()), couleur);
        }
        return article;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        lblReference = new JLabel();
        txtReference = new JTextField();
        lblDesignation = new JLabel();
        txtDesignation = new JTextField();
        lblMarque = new JLabel();
        txtMarque = new JTextField();
        lblStock = new JLabel();
        txtStock = new JTextField();
        lblPrix = new JLabel();
        txtPrix = new JTextField();
        lblType = new JLabel();
        rbtnPanel = new JPanel();
        rBtnRamette = new JRadioButton();
        rBtnStylo = new JRadioButton();
        lblGrammage = new JLabel();
        grammagePanel = new JPanel();
        check80grammes = new JCheckBox();
        check100grammes = new JCheckBox();
        lblCouleur = new JLabel();
        cbCouleur = new JComboBox<>();
        btnPanel = new JPanel();
        btnBack = new JButton();
        btnNew = new JButton();
        btnSave = new JButton();
        btnDelete = new JButton();
        btnForward = new JButton();

        InitializeArticles();

        //======== this ========
        setTitle("Catalogue");
        var contentPane = getContentPane();
        contentPane.setLayout(new GridLayoutManager(12, 2, new Insets(0, 0, 0, 0), 0, -1));

        //---- lblReference ----
        lblReference.setText("R\u00e9f\u00e9rence");
        contentPane.add(lblReference, new GridConstraints(0, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));
        contentPane.add(txtReference, new GridConstraints(0, 1, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //---- lblDesignation ----
        lblDesignation.setText("D\u00e9signation");
        contentPane.add(lblDesignation, new GridConstraints(1, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));
        contentPane.add(txtDesignation, new GridConstraints(1, 1, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //---- lblMarque ----
        lblMarque.setText("Marque");
        contentPane.add(lblMarque, new GridConstraints(2, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));
        contentPane.add(txtMarque, new GridConstraints(2, 1, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //---- lblStock ----
        lblStock.setText("Stock");
        contentPane.add(lblStock, new GridConstraints(3, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));
        contentPane.add(txtStock, new GridConstraints(3, 1, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //---- lblPrix ----
        lblPrix.setText("Prix");
        contentPane.add(lblPrix, new GridConstraints(4, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));
        contentPane.add(txtPrix, new GridConstraints(4, 1, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //---- lblType ----
        lblType.setText("Type");
        contentPane.add(lblType, new GridConstraints(5, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //======== rbtnPanel ========
        {
            rbtnPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));

            //---- rBtnRamette ----
            rBtnRamette.setText("Ramette");
            rBtnRamette.addItemListener(e -> rBtnRametteItemStateChanged(e));
            rbtnPanel.add(rBtnRamette, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- rBtnStylo ----
            rBtnStylo.setText("Stylo");
            rBtnStylo.addItemListener(e -> rBtnStyloItemStateChanged(e));
            rbtnPanel.add(rBtnStylo, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
        }
        contentPane.add(rbtnPanel, new GridConstraints(5, 1, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //---- lblGrammage ----
        lblGrammage.setText("Grammage");
        contentPane.add(lblGrammage, new GridConstraints(6, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //======== grammagePanel ========
        {
            grammagePanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));

            //---- check80grammes ----
            check80grammes.setText("80 Grammes");
            grammagePanel.add(check80grammes, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- check100grammes ----
            check100grammes.setText("100 Grammes");
            grammagePanel.add(check100grammes, new GridConstraints(1, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
        }
        contentPane.add(grammagePanel, new GridConstraints(6, 1, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //---- lblCouleur ----
        lblCouleur.setText("Couleur");
        contentPane.add(lblCouleur, new GridConstraints(7, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //---- cbCouleur ----
        cbCouleur.setModel(new DefaultComboBoxModel<>(new String[] {
            " ",
            "Noir",
            "Blanc",
            "Rouge",
            "Bleu",
            "Vert",
            "Orange",
            "Rose",
            "Marron",
                "Jaune"
        }));
        contentPane.add(cbCouleur, new GridConstraints(7, 1, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));

        //======== btnPanel ========
        {
            btnPanel.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), 8, -1));

            //---- btnBack ----
            btnBack.setIcon(new ImageIcon(getClass().getResource("/fr/eni/papeterie/ihm/img/Back24.gif")));
            btnBack.addActionListener(e -> btnBack(e));
            btnPanel.add(btnBack, new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- btnNew ----
            btnNew.setIcon(new ImageIcon(getClass().getResource("/fr/eni/papeterie/ihm/img/New24.gif")));
            btnNew.addActionListener(e -> {
                try {
                    btnNew(e);
                } catch (BLLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            btnPanel.add(btnNew, new GridConstraints(0, 1, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- btnSave ----
            btnSave.setIcon(new ImageIcon(getClass().getResource("/fr/eni/papeterie/ihm/img/Save24.gif")));
            btnSave.addActionListener(e -> btnSave(e));
            btnPanel.add(btnSave, new GridConstraints(0, 2, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- btnDelete ----
            btnDelete.setIcon(new ImageIcon(getClass().getResource("/fr/eni/papeterie/ihm/img/Delete24.gif")));
            btnDelete.addActionListener(e -> btnDelete(e));
            btnPanel.add(btnDelete, new GridConstraints(0, 3, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));

            //---- btnForward ----
            btnForward.setIcon(new ImageIcon(getClass().getResource("/fr/eni/papeterie/ihm/img/Forward24.gif")));
            btnForward.addActionListener(e -> {
                try {
                    btnForward(e);
                } catch (BLLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            btnPanel.add(btnForward, new GridConstraints(0, 4, 1, 1,
                GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                null, null, null));
        }
        contentPane.add(btnPanel, new GridConstraints(8, 0, 3, 2,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));
        pack();
        setLocationRelativeTo(getOwner());

        //---- typeButtonGroup ----
        var typeButtonGroup = new ButtonGroup();
        typeButtonGroup.add(rBtnRamette);
        typeButtonGroup.add(rBtnStylo);

        //---- grammageButtonGroup ----
        var grammageButtonGroup = new ButtonGroup();
        grammageButtonGroup.add(check80grammes);
        grammageButtonGroup.add(check100grammes);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel lblReference;
    private JTextField txtReference;
    private JLabel lblDesignation;
    private JTextField txtDesignation;
    private JLabel lblMarque;

    public JTextField getTxtReference() {
        return txtReference;
    }

    public void setTxtReference(JTextField txtReference) {
        this.txtReference = txtReference;
    }

    public JTextField getTxtDesignation() {
        return txtDesignation;
    }

    public void setTxtDesignation(JTextField txtDesignation) {
        this.txtDesignation = txtDesignation;
    }

    public JTextField getTxtMarque() {
        return txtMarque;
    }

    public void setTxtMarque(JTextField txtMarque) {
        this.txtMarque = txtMarque;
    }

    public JTextField getTxtStock() {
        return txtStock;
    }

    public void setTxtStock(JTextField txtStock) {
        this.txtStock = txtStock;
    }

    public JTextField getTxtPrix() {
        return txtPrix;
    }

    public void setTxtPrix(JTextField txtPrix) {
        this.txtPrix = txtPrix;
    }

    private JTextField txtMarque;
    private JLabel lblStock;
    private JTextField txtStock;
    private JLabel lblPrix;
    private JTextField txtPrix;
    private JLabel lblType;
    private JPanel rbtnPanel;
    private JRadioButton rBtnRamette;
    private JRadioButton rBtnStylo;
    private JLabel lblGrammage;
    private JPanel grammagePanel;
    private JCheckBox check80grammes;
    private JCheckBox check100grammes;
    private JLabel lblCouleur;
    private JComboBox<String> cbCouleur;
    private JPanel btnPanel;
    private JButton btnBack;
    private JButton btnNew;
    private JButton btnSave;
    private JButton btnDelete;
    private JButton btnForward;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
