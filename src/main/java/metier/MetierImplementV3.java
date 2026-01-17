package metier;

import dao.IDao;

public class MetierImplementV3 implements IMetier{

    private IDao dao;

    public MetierImplementV3() {
    }

    public MetierImplementV3(IDao dao) {
        this.dao = dao;
    }


    @Override
    public double calcul() {

        double t = dao.getData();
        double solution = t - 10;
        return solution;
    }
}
