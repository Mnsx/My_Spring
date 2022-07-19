package top.mnsx.my_spring.transaction;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.transaction.interceptor.TransactionalProxy;
import top.mnsx.my_spring.annotation.Transactional;
import top.mnsx.my_spring.jdbc.JdbcFactory;
import top.mnsx.my_spring.jdbc.TransactionManager;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.transaction
 * @CreateTime: 2022/7/18
 * @Description:
 */
public class TransactionProxy {
    private Class<?> targetClass;
    private List<String> transactionalMethods;

    public Object getProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Object result = null;
                if (transactionalMethods.contains(method.getName())) {
                    Connection connection = TransactionManager.getThreadConnection();
                    connection.setAutoCommit(false);
                    try {
                        result = methodProxy.invokeSuper(o, objects);
                        connection.commit();
                    } catch (Throwable e) {
                        connection.rollback();
                    } finally {
                        JdbcFactory.closeDataSource(connection);
                    }
                } else {
                    result = methodProxy.invokeSuper(o, objects);
                }
                return result;
            }
        });
        return enhancer.create();
    }

    public TransactionProxy(Class<?> targetClass, List<String> transactionalMethods) {
        this.targetClass = targetClass;
        this.transactionalMethods = transactionalMethods;
    }
}
