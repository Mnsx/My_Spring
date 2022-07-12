package top.mnsx.my_spring.exception;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.exception
 * @CreateTime: 2022/7/12
 * @Description:
 */
public class NotFoundBeanMatchConditionException extends RuntimeException {
    public NotFoundBeanMatchConditionException() {
        super();
    }

    public NotFoundBeanMatchConditionException(String message) {
        super(message);
    }

    public NotFoundBeanMatchConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundBeanMatchConditionException(Throwable cause) {
        super(cause);
    }

    protected NotFoundBeanMatchConditionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
