
package com.sp.shorturl.service;

import com.sp.shorturl.entity.UrlEntity;
import com.sp.shorturl.entity.UrlResponse;
import com.sp.shorturl.repository.UrlRepository;
import com.sp.shorturl.utility.Base62Encoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final StringRedisTemplate redisTemplate;

    @Value("${cache.expiration.seconds}")
    private long redisExpirationTime;

    public UrlService(UrlRepository urlRepository,
                      StringRedisTemplate redisTemplate) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
    }

    public ResponseEntity<UrlResponse> generateShortUrl(String originalUrl) {

        if (originalUrl != null && !originalUrl.isEmpty()) {

            UrlEntity urlEntity = new UrlEntity();
            urlEntity.setOriginalUrl(originalUrl);

            UrlEntity urlEntityDB = urlRepository.save(urlEntity);

            String shortUrl = Base62Encoder.encode(urlEntityDB.getId());
            shortUrl = "http://localhost:8080/" + shortUrl;

            urlEntityDB.setShortUrl(shortUrl);
            urlRepository.save(urlEntityDB);

            UrlResponse response = new UrlResponse(shortUrl);

            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.badRequest()
                    .body(new UrlResponse("Invalid URL"));
        }
    }

    public String getOriginalUrl(String id) {

        String shortUrl = "http://localhost:8080/" + id;

        // Check Redis cache
        String cachedValue = redisTemplate.opsForValue().get(shortUrl);

        if (cachedValue != null) {
            return cachedValue;
        }

        // Fetch from MySQL
        return urlRepository.findByShortUrl(shortUrl)
                .map(urlEntity -> {

                    redisTemplate.opsForValue().set(
                            shortUrl,
                            urlEntity.getOriginalUrl(),
                            Duration.ofSeconds(redisExpirationTime)
                    );

                    return urlEntity.getOriginalUrl();

                })
                .orElse(null);
    }
}
