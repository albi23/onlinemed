package com.onlinemed.servises.impl.login;

import com.onlinemed.model.Person;
import com.onlinemed.servises.api.PasetoTokenService;
import com.onlinemed.servises.api.PersonService;
import net.aholbrook.paseto.exception.PasetoParseException;
import net.aholbrook.paseto.exception.SignatureVerificationException;
import net.aholbrook.paseto.exception.claims.ExpiredTokenException;
import net.aholbrook.paseto.exception.claims.MissingClaimException;
import net.aholbrook.paseto.service.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.onlinemed.servises.impl.login.SecurityConstants.TOKEN_HEADER;

@Service
public class PasetoTokenServiceImpl implements PasetoTokenService {

    @Autowired
    PersonService personService;

    private static int expirationMinutes;
    private static int securityExpirationMinutes;
    private static String sharedKeyValue;
    private static String sharedSecurityKeyValue;

    @Value("${shared.key}")
    public void setSharedKey(String sharedKey) {
        PasetoTokenServiceImpl.sharedKeyValue = sharedKey;
    }

    @Value("${key.expiration.minutes}")
    public void setExpirationTime(int expirationTime) {
        PasetoTokenServiceImpl.expirationMinutes = expirationTime;
    }

    @Value("${security.shared.key}")
    public void setSharedSecurityKey(String sharedSecurityKey) {
        PasetoTokenServiceImpl.sharedSecurityKeyValue = sharedSecurityKey;
    }

    @Value("${security.key.expiration.minutes}")
    public void setSecurityExpirationTime(int expirationTime) {
        PasetoTokenServiceImpl.securityExpirationMinutes = expirationTime;
    }


    @Override
    public String generateRequestToken(String subject) {
        return PasetoUtil.generateToken(subject,
                PasetoTokenServiceImpl.sharedKeyValue,
                PasetoTokenServiceImpl.expirationMinutes);
    }

    @Override
    public Token parseRequestToken(String token) {
        return PasetoUtil.parseToken(token,
                PasetoTokenServiceImpl.sharedKeyValue,
                PasetoTokenServiceImpl.expirationMinutes);
    }

    @Override
    public String generateSecurityToken(String subject) {
        return PasetoUtil.generateToken(subject,
                PasetoTokenServiceImpl.sharedSecurityKeyValue,
                PasetoTokenServiceImpl.securityExpirationMinutes);
    }

    @Override
    public Token parseSecurityToken(String token) {
        return PasetoUtil.parseToken(token,
                PasetoTokenServiceImpl.sharedSecurityKeyValue,
                PasetoTokenServiceImpl.securityExpirationMinutes);
    }

    @Override
    public boolean securityTokensValid(Person person, String token) {
        final boolean correctToken = parseToken(token);
        return correctToken && person.getSecurity().getSecurityToken().equals(token);
    }

    @Override
    public boolean requestTokenValid(String token) {
       return this.parseToken(token);
    }

    private boolean parseToken(String token){
        try {
            parseRequestToken(token);
            return true;
        } catch (ExpiredTokenException | MissingClaimException | PasetoParseException | SignatureVerificationException e) {
            return false;
        }
    }


    @Override
    public Person getPersonFromRequestToken(HttpServletRequest request) {
        String token =  request.getHeader(TOKEN_HEADER);
        if (token != null) {
            String username = parseRequestToken(token).getSubject();
            return personService.findPersonByUsernameWithTouchRoles(username);
        }
        return null;
    }

}
