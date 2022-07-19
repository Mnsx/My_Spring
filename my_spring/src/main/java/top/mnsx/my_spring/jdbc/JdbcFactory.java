package top.mnsx.my_spring.jdbc;

import top.mnsx.my_spring.annotation.bean.Component;
import top.mnsx.my_spring.parser.XmlSpringConfigParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.jdbc
 * @CreateTime: 2022/7/14
 * @Description:
 */
public class JdbcFactory {
    private static Map<String, String> dataSource = XmlSpringConfigParser.getDataSource("applicationContext.xml");
    private static LinkedList<Connection> pool = new LinkedList<>();
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    static {
        try {
            Class.forName(dataSource.get("driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Integer initConnections = 10;
        String init_count = dataSource.get("init_count");
        if (init_count != null) {
            initConnections = Integer.parseInt(init_count);
        }
        for (int i = 0; i < initConnections; ++i) {
            try {
                Connection connection = DriverManager.getConnection(
                        dataSource.get("url"),
                        dataSource.get("name"),
                        dataSource.get("password"));
                pool.add(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public JdbcFactory() {

    }

    public static Connection getConnection() {
        Connection connection = null;
        lock.lock();
        try {
            while (pool.size() < 0) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!pool.isEmpty()) {
                connection = pool.removeFirst();
            }
            return connection;
        } finally {
            lock.unlock();
        }
    }

    public static void closeDataSource(Connection connection) {
        if (connection != null) {
            lock.lock();
            try {
                pool.addLast(connection);
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
