package com.onlinemed.servises.impl.login;

import com.onlinemed.model.Person;
import com.onlinemed.servises.api.PasetoTokenService;
import com.onlinemed.servises.api.PersonService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class PasetoTokenServiceImpl implements PasetoTokenService {

    @Autowired
    PersonService personService;
    private int expirationMinutes;
    private SecretKey sharedKeyValue;
    private final ZoneId defaultZoneId = ZoneId.systemDefault();

    @Value("${shared.key}")
    public void setSharedKey(String sharedKey) {
        sharedKeyValue = new SecretKeySpec(sharedKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
    }

    @Value("${key.expiration.minutes}")
    public void setExpirationTime(int expirationTime) {
        expirationMinutes = expirationTime;
    }

    @Override
    public String generateRequestToken(String subject) {
        var localDateTime = LocalDateTime.now().plusMinutes(expirationMinutes);
        var expiration = Date.from(localDateTime.atZone(defaultZoneId).toInstant());

        return Jwts.builder()
                .subject(subject)
                .signWith(sharedKeyValue)
                .expiration(expiration)
                .compact();
    }

    @Override
    public Optional<String> parseRequestToken(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(sharedKeyValue)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject());

        } catch (Exception ex) {
            return Optional.empty();
        }

    }


    @Override
    public boolean requestTokenValid(String token) {
        return parseRequestToken(token).isPresent();
    }


    @Override
    public Optional<Person> getPersonFromRequestToken(String token) {
        return Optional.ofNullable(token)
                .map(optToken -> parseRequestToken(token))
                .filter(Optional::isPresent)
                .flatMap(username -> personService.findPersonByUsernameWithTouchRoles(username.get()));

    }

}
