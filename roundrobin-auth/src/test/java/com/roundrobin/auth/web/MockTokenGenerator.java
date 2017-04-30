package com.roundrobin.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.Key;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

/**
 * Created by rengasu on 4/28/17.
 */
public class MockTokenGenerator {
  public static void main(String[] args) {
    List<String> scopes = new ArrayList<>();
    scopes.add("profile");
    scopes.add("location");
    scopes.add("vault");
    scopes.add("api");
    scopes.add("skill");

    List<String> resources= new ArrayList<>();
    resources.add("vault");
    resources.add("auth");
    resources.add("api");
    Map<String, Object> token = new HashMap<>();
    token.put("user_name", UUID.randomUUID().toString());
    token.put("scope", scopes);
    token.put("aud", resources);
    token.put("exp", (System.currentTimeMillis()/1000) + 1800);
    try {
      String a = new ObjectMapper().writeValueAsString(token);
      System.out.println("a:" + a);
    }catch(Exception e){

    }


    KeyStoreKeyFactory keyStoreKeyFactory =
            new KeyStoreKeyFactory(new ClassPathResource("roundrobin-auth.jks"), "fckgw-rhqq2-yxrkt-8tg6w-2b7q8".toCharArray());
    KeyPair pair = keyStoreKeyFactory.getKeyPair("roundrobin-auth");

    String compactJws = Jwts.builder()
            .setPayload("{\"aud\": [\"auth\",\"vault\"],\"user_name\": \"5902dcfe78f6150c85d9b873\",\"scope\": [\"profile\",\"location\",\"api\",\"vault\"],\"verified\": false,\"exp\": 1493403070,\"authorities\": [\"USER\"],\"jti\": \"e0b5b767-3a5e-48a2-8e68-0e9227e94177\",\"client_id\": \"web\"}")
            .signWith(SignatureAlgorithm.RS256, pair.getPrivate())
            .compact();
    System.out.println(compactJws);
  }
}
