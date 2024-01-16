package co.cstad.sen.api.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudServiceImpl implements CloudService {

    @Value("${cloud.base.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

    public CloudServiceImpl() {

        restTemplate = new RestTemplate();

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    }

    @Override
    public boolean createUser(String username, String email, String password) {

        String apiUrl = baseUrl + "/api/v1/owncloud/users";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        try {

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    httpEntity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                updateUserEmailByUsername(username, email);

                return true;

            } else {

                // Send mail to Admin to handle it later.
                return false;

            }

        } catch (HttpStatusCodeException e) {

            // Send mail to Admin to handle it later.
            return false;

        }

    }

    @Override
    public boolean updateUserEmailByUsername(String username, String email) {

        String url = baseUrl + "/api/v1/owncloud/users/" + username + "/email";

        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("email", email)
                .build()
                .toUri();

        try {

            ResponseEntity<String> response = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    null,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            } else {
                // Send mail to Admin to handle it later.
                return false;
            }

        } catch (HttpStatusCodeException e) {

            // Send mail to Admin to handle it later.
            return false;

        }

    }

}
