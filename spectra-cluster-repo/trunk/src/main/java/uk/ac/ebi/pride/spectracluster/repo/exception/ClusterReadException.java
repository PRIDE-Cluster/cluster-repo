package uk.ac.ebi.pride.spectracluster.repo.exception;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterReadException extends RuntimeException {

    public ClusterReadException() {
    }

    public ClusterReadException(String message) {
        super(message);
    }

    public ClusterReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClusterReadException(Throwable cause) {
        super(cause);
    }

    public ClusterReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
