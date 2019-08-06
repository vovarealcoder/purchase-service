package com.vova.purchaseservice.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class LoginAttemptService {

    private final Logger logger = Logger.getLogger("attempt-service");
    private static final int MAX_ATTEMPT = 10;
    private LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }

    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    void loginFailed(String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        logger.log(Level.FINE, "Failed login " + key + ", attempts " + attempts);
        attemptsCache.put(key, attempts);
    }

    boolean isBlocked(String key) {
        try {
            boolean locked = attemptsCache.get(key) >= MAX_ATTEMPT;
            logger.log(Level.FINE, "key is blocked check returned " + locked);
            return locked;
        } catch (ExecutionException e) {
            return false;
        }
    }
}