package io.github.winsontse.hearteyes.util;

/**
 * Created by winson on 16/5/28.
 */
public class HeartEyesException extends Exception {
    /**
     * Constructs a new {@code Error} with the current stack trace and the
     * specified detail message.
     *
     * @param detailMessage the detail message for this error.
     */
    public HeartEyesException(String detailMessage) {
        super(detailMessage);
    }
}
