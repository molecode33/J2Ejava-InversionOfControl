package metier;

import dao.IDao;

public class MetierImplement implements IMetier{

    // Couplage Faible << Classe ----> Interface

    private IDao dao ;
    /*
    Remarque Importante : l'objet Dao est null donc lorsque nus allons appeler
    la méthode getData() défini dans l'interface IDao nous aurons une Exception "Null Pointor Exception".
     */

    /*
    Comme notre Objet Dao n'a pas de valeur alors
    il faut trouver un moyen de l'affecter une valeur avant d'appeler la méthode getData()
     */

    public MetierImplement() {
    }

    public MetierImplement(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        double t = dao.getData();
        double result = t * 32;
        return result;
    }
/*
Il existe différent méthode d'affecter un valeur à notre objet dao :
1.  Seters : La méthode setDao permet d'ingecter dans la variable dao
un objet d'une Classe qui implémente l'interface IDao.
On parle de L'Injection des Dépendances
2. Constructeur :
 */
    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
