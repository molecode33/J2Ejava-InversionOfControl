package ext;

import dao.IDao;

public class DaoImplementV2 implements IDao {
    @Override
    public double getData() {
        System.out.println("Version web service");
        double t = 10;
        return t;
    }
}
