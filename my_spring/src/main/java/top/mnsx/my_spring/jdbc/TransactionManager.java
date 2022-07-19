package top.mnsx.my_spring.jdbc;

import top.mnsx.my_spring.annotation.bean.Component;

import java.sql.Connection;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.jdbc
 * @CreateTime: 2022/7/18
 * @Description:
 */
public class TransactionManager {
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    static {
        Connection connection = getConnection();
        threadLocal.set(connection);
    }

    public static Connection getThreadConnection() {
        return threadLocal.get();
    }

    private static Connection getConnection() {
        return JdbcFactory.getConnection();
    }
}
