package com.roaster.demo;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@SuppressWarnings("unused")
@RestController
public class RoastController {

private final RestClient client = RestClient.create();


    private final String clientId = "CLIENT_ID";

    private final String clientSecret = "CLIENT_SECRET";


    private final String redirectUri =
        "http://127.0.0.1:8080/auth/callback";



    private String accessToken;


@GetMapping("/auth/login")
public ResponseEntity<Void> login() {

    String scopes =
        "user-top-read user-read-recently-played";

    String spotifyUrl =
        "https://accounts.spotify.com/authorize" +
        "?response_type=code" +
        "&client_id=" + clientId +
        "&redirect_uri=" +
        URLEncoder.encode(
            redirectUri,
            StandardCharsets.UTF_8
        ) +
        "&scope=" +
        URLEncoder.encode(
            scopes,
            StandardCharsets.UTF_8
        );

    return ResponseEntity
        .status(302)
        .location(URI.create(spotifyUrl))
        .build();
}



    @GetMapping("/auth/callback")
    public void callback(@RequestParam String code) {

        String spotifyData = getSpotifyData(exchangeCodeForToken(code));
    }




    private String exchangeCodeForToken(String code) {

        String spotifyAuth = clientId + ":" + clientSecret;
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = spotifyAuth.getBytes(StandardCharsets.UTF_8);
        String Base = encoder.encodeToString(bytes);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);
        String response = client.post()
        .uri("https://accounts.spotify.com/api/token")
        .header("Authorization", "Basic " + Base)
        .header("Content-Type", "application/x-www-form-urlencoded")
        .body(body)
        .retrieve()
        .body(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);
        JsonNode tokenJson = json.get("access_token");
        String token = tokenJson.asString();
        
        return token;
    }



    private String getSpotifyData(String accessToken) {

        return "";
    }



    private String getGeminiRoast(String spotifyData) {

        return "";
    }
}