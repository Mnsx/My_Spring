package top.mnsx.my_spring.proxy;

import top.mnsx.my_spring.aop.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.proxy
 * @CreateTime: 2022/7/13
 * @Description:
 */
public class JdkProxy {
    private Class<?> targetClass;
    private Class<?> aopClass;
    private Object targetObject;
    private String methodName;
    private Method aopMethod;

    public Object getProxyInstance() {
        return Proxy.newProxyInstance(targetClass.getClassLoader(),
                targetClass.getInterfaces(),
                    (proxy, method, args) -> {
                        if (method.getName().equals(methodName)) {
                            ProceedingJoinPoint proceedingJoinPoint = new ProceedingJoinPoint(method, targetClass.newInstance(), args);
                            return aopMethod.invoke(aopClass.newInstance(), proceedingJoinPoint);
                        } else {
                            return method.invoke(targetObject, args);
                        }
        });
    }

    public JdkProxy(Class<?> targetClass, Class<?> aopClass, Object targetObject, String methodName, Method aopMethod) {
        this.targetClass = targetClass;
        this.aopClass = aopClass;
        this.targetObject = targetObject;
        this.methodName = methodName;
        this.aopMethod = aopMethod;
    }
}
