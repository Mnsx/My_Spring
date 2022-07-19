package top.mnsx.my_spring.jdbc;

import top.mnsx.my_spring.annotation.Autowired;
import top.mnsx.my_spring.annotation.bean.Component;
import top.mnsx.my_spring.annotation.bean.Service;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.jdbc
 * @CreateTime: 2022/7/14
 * @Description: jdbc操作类
 */
@Component
public class JdbcTemplate {
    public JdbcTemplate() {
    }

    public int update(String sql, Object... args) {
        int result = 0;
        Connection connection  = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = TransactionManager.getThreadConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            result = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(preparedStatement);
            JdbcFactory.closeDataSource(connection);
        }
        return result;
    }

    public<T> T queryForObject(String sql, Class<T> aClass, Object... args) {
        Connection connection  = null;
        PreparedStatement preparedStatement = null;
        T result = null;
        try {
            connection = TransactionManager.getThreadConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int length = metaData.getColumnCount();
            if (resultSet.next()) {
                result = aClass.newInstance();
                for (int i = 0; i < length; ++i) {
                    Object columnValue = resultSet.getObject(i + 1);
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Field field = aClass.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(result, columnValue);
                }
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(preparedStatement);
            JdbcFactory.closeDataSource(connection);
        }
        return result;
    }

    public<T> List<T> query(String sql, Class<T> aClass, Object... args) {
        Connection connection  = null;
        PreparedStatement preparedStatement = null;
        List<T> results = new ArrayList<>();
        try {
            connection = TransactionManager.getThreadConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; ++i) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int length = metaData.getColumnCount();
            while (resultSet.next()) {
                T result = aClass.newInstance();
                for (int i = 0; i < length; ++i) {
                    Object columnValue = resultSet.getObject(i + 1);
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Field field = aClass.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(result, columnValue);
                }
                results.add(result);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            closePreparedStatement(preparedStatement);
            JdbcFactory.closeDataSource(connection);
        }
        return results;
    }

    private void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
