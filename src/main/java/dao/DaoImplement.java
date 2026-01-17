package dao;

public class DaoImplement implements IDao{

    @Override
    public double getData() {
        System.out.println("Version base de données");
        double temp = 32;
        return temp;
    }
}
