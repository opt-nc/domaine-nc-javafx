package com.unc.domainenc.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Request {

    private static final String apiURL = "https://domaine-nc.p.rapidapi.com/domaines";
    private static final String ApiHOST = "domaine-nc.p.rapidapi.com";

    private final String apiKEY;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public Request() {
        Dotenv dotenv = Dotenv.load();
        apiKEY = dotenv.get("X-RAPIDAPI-KEY");
        restTemplate = new RestTemplate();
        mapper = new ObjectMapper();
    }

    public static void main(String[] args) {
        Request request = new Request();
        List<DomaineEntity> list = request.getDomaine();
        for (DomaineEntity domaine : list) {
            System.out.println(domaine.getName() + domaine.getExtension());
        }
    }

    public List<DomaineEntity> getDomaine() {
        List<DomaineEntity> domaineList;
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RapidAPI-Host", ApiHOST);
        headers.set("X-RapidAPI-Key", apiKEY);
        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            domaineList = mapper.readValue(response.getBody(), new TypeReference<List<DomaineEntity>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return domaineList;
    }
}

