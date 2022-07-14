package top.mnsx.my_spring.template.dao.impl;

import jdk.nashorn.internal.scripts.JD;
import top.mnsx.my_spring.annotation.bean.Component;
import top.mnsx.my_spring.annotation.bean.Repository;
import top.mnsx.my_spring.annotation.bean.Service;
import top.mnsx.my_spring.jdbc.JdbcFactory;
import top.mnsx.my_spring.jdbc.JdbcTemplate;
import top.mnsx.my_spring.template.dao.UserDao;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.template.dao.impl
 * @CreateTime: 2022/7/14
 * @Description:
 */
@Service
public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate;

    @Override
    public void updateBalanceById() {
        jdbcTemplate = JdbcTemplate.getInstance();
        int result = jdbcTemplate.update("delete from test where id = ?",3);
    }
}
