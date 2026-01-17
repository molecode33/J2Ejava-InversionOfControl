package Presentation;

import dao.IDao;
import metier.IMetier;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class PresentationV2 {
    public static void main(String[] args){

        try {
            // Injection dynamique qui permet à l'application d'etre completement fermé à la modification et ouvert à l'extension
            Scanner sc = new Scanner(new File("config.txt"));

            String daoClassName = sc.nextLine();
            //     DaoImplementV2 d = new DaoImplementV2();
            Class cDao = Class.forName(daoClassName);
            IDao dao = (IDao) cDao.getConstructor().newInstance();
            System.out.println(dao.getData());

            String metierClassName = sc.nextLine();
            //MetierImplement metier = new MetierImplement(d);
            Class cMetier = Class.forName(metierClassName);
            //IMetier metier = (IMetier) cMetier.getConstructor(IDao.class).newInstance(dao);
           // System.out.println("Resultat = "+metier.calcul());

            // metier.setDao(d);
            IMetier metier = (IMetier) cMetier.getConstructor().newInstance();
            Method setDao = cMetier.getDeclaredMethod("setDao", IDao.class);
            setDao.invoke(metier, dao);
            System.out.println("Result = "+metier.calcul());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
