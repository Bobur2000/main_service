package com.example.code_generator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CodeServiceClient {
    @Value("${code.generator.service.url}")
    private String codeGeneratorServiceUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String codeServiceUrl = "http://localhost:8081/codes";

    public String generateCode(String username) {
        try {
            return restTemplate.postForObject(codeGeneratorServiceUrl + "/generate?username=" + username, null, String.class);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        }

    public boolean validateCode(String username, String code) {
        try {

            return restTemplate.getForObject(codeGeneratorServiceUrl + "/validate?username=" + username + "&code=" + code, Boolean.class);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
