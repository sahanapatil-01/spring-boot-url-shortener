# URL Shortener using Spring Boot, MySQL and Redis

## Overview
A REST API developed using Java 25 and Spring Boot 4.1.1 that converts long URLs into short links and redirects users to their original websites.

MySQL stores the URL mappings permanently, while Redis caches frequently accessed URLs for faster redirection.

## Features
- Generate short URLs using Base62 encoding
- Redirect short URLs to original websites
- Store URLs in MySQL
- Cache URLs using Redis
- Configure cache expiration time
- Test API endpoints using Postman

## Tech Stack
- Java 25
- Spring Boot 4.1.1
- Spring Data JPA
- MySQL
- Redis
- Maven
- Postman
- IntelliJ IDEA
- Ubuntu / WSL

## API Endpoints

### Shorten URL

**POST** `http://localhost:8080/v1/shorten`

Request:

```json
{
  "originalUrl": "https://www.google.com"
}
```

Example response:

```json
{
  "shortUrl": "http://localhost:8080/7"
}
```

### Redirect URL

**GET** `http://localhost:8080/7`

Redirects to `https://www.google.com` if that mapping exists in the database.

## How to Run

1. Install Java 25, MySQL and Redis.
2. Create a MySQL database named `url_shortener_db`.
3. Configure `application.properties` and set the `DB_PASSWORD` environment variable.
4. Start Redis using `sudo service redis-server start` in Ubuntu.
5. Run `ShorturlApplication.java` in IntelliJ IDEA.
6. Test the endpoints using Postman or Chrome.

## Redis Cache Verification

```bash
redis-cli ping
redis-cli
KEYS *
GET http://localhost:8080/7
TTL http://localhost:8080/7
```

## Future Enhancements
- Custom short URLs
- URL expiration
- Click analytics
- User authentication
- Frontend interface

## Conclusion
This project demonstrates REST API development, MySQL integration, Base62 encoding and Redis caching using Spring Boot.