package top.mnsx.my_spring.jdbc;

import top.mnsx.my_spring.parser.XmlSpringConfigParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.jdbc
 * @CreateTime: 2022/7/14
 * @Description:
 */
public class JdbcFactory {
    private static Connection connection;

    static {
        Map<String, String> dataSource = XmlSpringConfigParser.getDataSource("applicationContext.xml");
        dataSource.entrySet().forEach(System.out::println);
        try {
            Class.forName(dataSource.get("driver"));
            connection = DriverManager.getConnection(
                    dataSource.get("url"),
                    dataSource.get("name"),
                    dataSource.get("password"));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
