package com.onlinemed.servises.impl.login;

import net.aholbrook.paseto.meta.PasetoBuilders;
import net.aholbrook.paseto.service.Token;
import net.aholbrook.paseto.service.TokenService;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PasetoUtil {


    public static Token parseToken(String token, String sharedKey, int expirationMinutes) {
        byte[] key = Hex.decode(sharedKey);
        TokenService<Token> tokenService = PasetoBuilders.V1.localService(() -> key, Token.class)
                .withDefaultValidityPeriod(Duration.ofMinutes(
                        expirationMinutes)
                        .getSeconds())
                .build();
        return tokenService.decode(token);
    }

    public static String generateToken(String subject, String sharedKey, int expirationMinutes) {
        byte[] key = Hex.decode(sharedKey);
        TokenService<Token> tokenService = PasetoBuilders.V1.localService(() -> key, Token.class)
                .withDefaultValidityPeriod(Duration.ofMinutes(
                        expirationMinutes)
                        .getSeconds())
                .build();
        return tokenService.encode(new Token().setSubject(subject));
    }
}
