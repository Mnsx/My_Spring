package top.mnsx.my_spring.exception;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.exception
 * @CreateTime: 2022/7/12
 * @Description:
 */
public class TooMuchBeanException extends RuntimeException {
    public TooMuchBeanException() {
        super();
    }

    public TooMuchBeanException(String message) {
        super(message);
    }

    public TooMuchBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooMuchBeanException(Throwable cause) {
        super(cause);
    }

    protected TooMuchBeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
