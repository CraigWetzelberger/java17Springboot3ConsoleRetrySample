package co.hbu.java17springbootconsole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FailingService {
    public class FailingServiceException extends RuntimeException {
        private final int code;
        public FailingServiceException(String message, int code) {
            super(message);
            this.code = code;
        }

        @Override
        public String toString() {
            return "FailingServiceException{" +
                    "code=" + code +
                    '}';
        }
    }

    private int currentAttempts = 0;
    private int maxAttempts = 2;
    private static Logger LOG = LoggerFactory.getLogger(FailingService.class);

    public FailingService() {
        maxAttempts = new Random().nextInt(2)+1;
    }

    @Override
    public String toString() {
        return "FailingService{" +
                "currentAttempts=" + currentAttempts +
                ", maxAttempts=" + maxAttempts +
                '}';
    }

    @Retryable(maxAttempts = 2, retryFor = {RuntimeException.class, FailingService.FailingServiceException.class})
    public void myFailingMethod() {
        System.out.println("Trying myFailingMethod with retry. " + this.toString() );

        if (currentAttempts++ < maxAttempts) {
            System.out.println("It failed!");
            int rand = new Random().nextInt(2);

            if(rand == 0)
                throw new RuntimeException("myFailingMethod FailingServiceException failed at " + java.time.LocalTime.now());
            else
                throw new FailingServiceException("myFailingMethod FailingServiceException failed at " + java.time.LocalTime.now(), 2);

        } else {
            System.out.println("It worked!");
        }
    }

    @Recover
    public void recoverMethod(RuntimeException e) {
        LOG.error("CAUGHT Runtime FAILURE: " + e.toString());
    }

    @Recover
    public void recoverMethod(FailingService.FailingServiceException e) {
        LOG.error("CAUGHT ServiceException FAILURE: " + e.toString());
    }
}
