
package com.sp.shorturl.controller;

import com.sp.shorturl.entity.UrlRequest;
import com.sp.shorturl.entity.UrlResponse;
import com.sp.shorturl.service.UrlService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlShortnerController {

    private final UrlService urlService;

    public UrlShortnerController(UrlService urlService) {
        this.urlService = urlService;
    }

    // Generate short URL
    @PostMapping("/v1/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(
            @RequestBody UrlRequest request) {

        return urlService.generateShortUrl(
                request.getOriginalUrl()
        );
    }

    // Redirect short URL to original URL
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectUrl(
            @PathVariable String shortCode) {

        String originalUrl = urlService.getOriginalUrl(shortCode);

        if (originalUrl == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
