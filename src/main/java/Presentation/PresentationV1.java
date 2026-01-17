package Presentation;

import metier.MetierImplement;
import ext.DaoImplementV2;

public class PresentationV1 {
    public static void main(String[] args ){
        /*
         Dans la couche presentation on va faire le calcul dont la méthode se trouve dans la couche Metier.
         */
         /*
        Pour que l'application puisse fonctionne on doit créer un objet DaoImplement
        qui permettra d'acceder à la mêthode setDao() permettant d'injecter ainsi les dépendances.
         */
        // Injection des dépendances par mêthode statique.
        DaoImplementV2 d = new DaoImplementV2();
        // Donc il faut creer un objet de la classe Metier (MetierImplement)
        MetierImplement metier = new MetierImplement(d);// Injection des dépendances via le constructeur.
       // metier.setDao(d); // Injection via le setter.
        System.out.println(metier.calcul());
        // Cannot invoke "DAO.IDao.getData()" because "this.dao" is null :
        // Cela veux dire que nous avons pas satisfait les dépendances


    }
}
