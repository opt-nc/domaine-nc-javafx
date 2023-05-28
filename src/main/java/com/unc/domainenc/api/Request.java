package com.unc.domainenc.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Request {

    private static final String apiURL = "https://domaine-nc.p.rapidapi.com/domaines";
    private static final String apiHOST = "domaine-nc.p.rapidapi.com";

    private static String apiKEY;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public Request() {
        restTemplate = new RestTemplate();
        mapper = new ObjectMapper();
    }

    public static void setApiKey(String apiKEY) {
        Request.apiKEY = apiKEY;
    }

    public List<DomaineEntity> getDomaine() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Host", apiHOST);
        headers.set("X-RapidAPI-Key", apiKEY);
        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            return mapper.readValue(response.getBody(), new TypeReference<List<DomaineEntity>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public DomaineInfoEntity getDomaineInfo(String domaineName) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Host", apiHOST);
        headers.set("X-RapidAPI-Key", apiKEY);
        ResponseEntity<String> response = restTemplate.exchange(String.format("%s/%s/NC", apiURL, domaineName), HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            return mapper.readValue(response.getBody(), DomaineInfoEntity.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

