package top.mnsx.my_spring.exception;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.exception
 * @CreateTime: 2022/7/12
 * @Description:
 */
public class ToMuchBeanException extends RuntimeException {
    public ToMuchBeanException() {
        super();
    }

    public ToMuchBeanException(String message) {
        super(message);
    }

    public ToMuchBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToMuchBeanException(Throwable cause) {
        super(cause);
    }

    protected ToMuchBeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
