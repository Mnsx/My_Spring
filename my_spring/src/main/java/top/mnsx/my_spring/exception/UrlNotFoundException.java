package top.mnsx.my_spring.exception;

/**
 * @BelongsProject: my_sprint
 * @BelongsPackage: top.mnsx.my_spring.exception
 * @CreateTime: 2022/7/10
 * @Description: URL找不到的异常
 */
public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException() {
        super();
    }

    public UrlNotFoundException(String message) {
        super(message);
    }

    public UrlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrlNotFoundException(Throwable cause) {
        super(cause);
    }

    protected UrlNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
