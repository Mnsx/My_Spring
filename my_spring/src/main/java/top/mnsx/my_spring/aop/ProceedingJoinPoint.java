package top.mnsx.my_spring.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.annotation.aop
 * @CreateTime: 2022/7/13
 * @Description:
 */
public class ProceedingJoinPoint {

    //目标方法
    private Method method;

    //目标对象
    private Object target;

    //目标方法参数
    private Object[] args;

    public ProceedingJoinPoint(Method method, Object target, Object[] args) {
        this.args = args;
        this.method = method;
        this.target = target;
    }

    public Object proceed() {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
