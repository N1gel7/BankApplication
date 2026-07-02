package test.bankapplication.service;

import io.github.bucket4j.Bandwidth;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import io.github.bucket4j.Bucket;

@Service
public class RateLimitingService {

    public RateLimitingService() {
    }

    private Map<String,Bucket> loginIpBuckets = new ConcurrentHashMap<>();
    private Map<String,Bucket> loginEmailBuckets = new ConcurrentHashMap<>();
    private Map<String,Bucket> transferBuckets = new ConcurrentHashMap<>();

    public Bucket resolveLoginIpBucket(String ipAddress) {
        return loginIpBuckets
                .computeIfAbsent(ipAddress,key-> Bucket.builder()
                        .addLimit(Bandwidth.builder()
                                .capacity(5)
                                .refillIntervally(5,Duration.ofMinutes(15))
                                .build())
                        .build());

    }


    public Bucket resolveLoginEmailBucket(String email){
        return loginEmailBuckets.computeIfAbsent(email,key-> Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(5)
                        .refillIntervally(5,Duration.ofMinutes(15))
                        .build())
                .build());
    }

    public Bucket resolveTransferBucket(String email){
        return transferBuckets.computeIfAbsent(email,key-> Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(1)
                        .refillIntervally(1,Duration.ofSeconds(5))
                        .build())
                .build());

    }



}
