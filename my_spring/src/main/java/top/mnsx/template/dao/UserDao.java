package top.mnsx.template.dao;


public interface UserDao {
    Integer updateBalanceById(int id, int money);

    Integer getMoneyById(int id);
}
