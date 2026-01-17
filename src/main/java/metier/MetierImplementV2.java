package metier;

import dao.IDao;

import java.lang.reflect.Constructor;

public class MetierImplementV2 implements IMetier{

    private IDao dao ;

    public MetierImplementV2() {
    }

    public MetierImplementV2(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        double t = dao.getData();
        double result = t + 100;
        return result;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
