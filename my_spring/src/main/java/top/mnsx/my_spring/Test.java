package top.mnsx.my_spring;

import org.springframework.jdbc.core.JdbcTemplate;
import top.mnsx.my_spring.container.ClassPathXmlApplicationContext;
import top.mnsx.my_spring.jdbc.JdbcFactory;
import top.mnsx.my_spring.parser.XmlSpringConfigParser;
import top.mnsx.my_spring.template.dao.UserDao;
import top.mnsx.my_spring.template.dao.impl.UserDaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring
 * @CreateTime: 2022/7/10
 * @Description: 启动类
 */
public class Test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserDao userDao = (UserDao) context.getBean(UserDaoImpl.class);
        userDao.updateBalanceById();
    }
}
