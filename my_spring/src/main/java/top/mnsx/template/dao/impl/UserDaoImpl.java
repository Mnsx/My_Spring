package top.mnsx.template.dao.impl;

import top.mnsx.template.dao.UserDao;
import top.mnsx.template.entity.User;
import top.mnsx.my_spring.annotation.Autowired;
import top.mnsx.my_spring.annotation.bean.Repository;
import top.mnsx.my_spring.jdbc.JdbcTemplate;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.template.dao.impl
 * @CreateTime: 2022/7/14
 * @Description:
 */
@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer updateBalanceById(int id, int money) {
        return jdbcTemplate.update("update test set balance = ? where id = ?", money, id);
    }

    @Override
    public Integer getMoneyById(int id) {
        return jdbcTemplate.queryForObject("select * from test where id = ?", User.class, id).getBalance();
    }
}
