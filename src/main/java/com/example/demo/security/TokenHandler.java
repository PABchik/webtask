package com.example.demo.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

@Component
public class TokenHandler {

    private final Mac mac;

    private final String HMAC_SHA256 = "HmacSHA256";
    private final String SEPARATOR = ".";
    private final String SEPARATOR_SPLITTER = "\\.";

    public TokenHandler(@Value("${com.example.demo.security.secret}") String secret) {
        try {
            this.mac = Mac.getInstance(HMAC_SHA256);
            this.mac.init(new SecretKeySpec(secret.getBytes(), HMAC_SHA256));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public UserDetails parseUserFromToken(String token) {
        String[] parts = token.split(SEPARATOR_SPLITTER);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
            byte[] userBytes = fromBase64(parts[0]);
            byte[] hashBytes = fromBase64(parts[1]);
            if (Arrays.equals(createHmac(userBytes), hashBytes)) {
                UserDetails user = fromJson(userBytes);
                if (user != null && user.getExpires() > new Date().getTime()) return user;
            }
        }
        return null;
    }

    public String createTokenForUser(UserDetails user) {
        byte[] userBytes = toJson(user);
        byte[] hash = createHmac(userBytes);
        //noinspection StringBufferReplaceableByString
        return new StringBuffer(toBase64(userBytes)).append(SEPARATOR).append(toBase64(hash)).toString();
    }

    private byte[] fromBase64(String content) {
        return DatatypeConverter.parseBase64Binary(content);
    }

    private String toBase64(byte[] content) {
        return DatatypeConverter.printBase64Binary(content);
    }

    private synchronized byte[] createHmac(byte[] content) {
        return mac.doFinal(content);
    }

    private UserDetails fromJson(byte[] content) {
        try {
            return new ObjectMapper().readValue(new ByteArrayInputStream(content), UserDetails.class);
        } catch (IOException e) {
            return null;
        }
    }

    private byte[] toJson(UserDetails user) {
        try {
            return new ObjectMapper().writeValueAsBytes(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
