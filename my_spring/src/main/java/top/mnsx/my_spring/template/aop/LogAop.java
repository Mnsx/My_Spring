package top.mnsx.my_spring.template.aop;

import top.mnsx.my_spring.annotation.Around;
import top.mnsx.my_spring.annotation.Aspect;
import top.mnsx.my_spring.aop.ProceedingJoinPoint;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.template.aop
 * @CreateTime: 2022/7/13
 * @Description:
 */
@Aspect
public class LogAop {

    @Around(execution = "top.mnsx.my_spring.template.service.impl.OrderServiceImpl.findOrders")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            System.out.println("前置通知");
            result = joinPoint.proceed();
            System.out.println("后置通知");
        } catch (Throwable throwable) {
            System.out.println("异常通知" + throwable.getMessage());
        } finally {
            System.out.println("最终通知");
        }
        return result;
    }
}
