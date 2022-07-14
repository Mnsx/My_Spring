package top.mnsx.my_spring.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.jdbc
 * @CreateTime: 2022/7/14
 * @Description: jdbc操作类
 */
public class JdbcTemplate {
    private final Connection connection;

    public static JdbcTemplate getInstance() {
        return new JdbcTemplate();
    }

    private JdbcTemplate() {
        this.connection = JdbcFactory.getConnection();
    }

    public int update(String sql, Object... args) {
        int result = 0;
        try {
            for (Object arg : args) {
                sql = sql.replaceFirst("\\?", "'" + arg + "'");
            }
            System.out.println(sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public<T> T queryForObject(String sql, Class<?> T) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public<T> List<T> query(String sql, Class<?> T) {
        return null;
    }
}
