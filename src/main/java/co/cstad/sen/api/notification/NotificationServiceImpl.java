package co.cstad.sen.api.notification;

import co.cstad.sen.api.notification.web.CreateNotificationDto;
import co.cstad.sen.api.notification.web.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class NotificationServiceImpl  implements  NotificationService{

    @Value("${onesignal.app-id}")
    public String appId;


    @Value("${onesignal.rest-api-key}")
    public String restApiKey;



    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        this.restTemplate= restTemplate;
    }
    @Override
    public boolean pushNotification(CreateNotificationDto notificationDto) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("accept", "application/json");
        httpHeaders.set("Authorization", "Basic " + restApiKey);
        httpHeaders.set("content-type", "application/json");


        NotificationDto body = NotificationDto.builder()
                .appId(appId)
                .includedSegments(notificationDto.includedSegments())
                .contents(notificationDto.contents())
                .build();


        HttpEntity<NotificationDto> requestBody = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<?> response = restTemplate.postForEntity(
                "https://onesignal.com/api/v1/notifications",
                requestBody,
                Map.class
        );
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        // Log OneSignal error messages, if available
        if (response.getBody() instanceof Map) {
            Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
            if (responseBody.containsKey("errors")) {
                System.out.println("OneSignal Errors: " + responseBody.get("errors"));
            }
        }

        return response.getStatusCode() == HttpStatus.OK;
    }
}
//@Service
//public class NotificationServiceImpl implements NotificationService {
//
//    private final String appId;
//    private final String restApiKey;
//    private final RestTemplate restTemplate;
//
//    public NotificationServiceImpl(@Value("${onesignal.app-id}") String appId,
//                                   @Value("${onesignal.rest-api-key}") String restApiKey,
//                                   RestTemplate restTemplate) {
//        this.appId = appId;
//        this.restApiKey = restApiKey;
//        this.restTemplate = restTemplate;
//    }
//
//    @Override
//    public boolean pushNotification(CreateNotificationDto notificationDto) {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("accept", "application/json");
//        httpHeaders.set("Authorization", "Basic " + restApiKey);
//        httpHeaders.set("content-type", "application/json");
//
//        ContentDto content = notificationDto.contents();
//
//        NotificationDto body = NotificationDto.builder()
//                .appId(appId)
//                .includedSegments(notificationDto.includedSegments())
//                .contents(content)
//                .build();
//
//
//        // Log the request payload
//        System.out.println("Request Payload: " + body);
//
//        HttpEntity<NotificationDto> requestBody = new HttpEntity<>(body, httpHeaders);
//
//        ResponseEntity<?> response = restTemplate.postForEntity(
//                "https://onesignal.com/api/v1/notifications",
//                requestBody,
//                Map.class
//        );
//
//        // Log the response status code
//        System.out.println("Response Status: " + response.getStatusCode());
//
//        // Log the response body
//        System.out.println("Response Body: " + response.getBody());
//
//        // Log OneSignal error messages, if available
//        if (response.getBody() instanceof Map) {
//            Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
//            if (responseBody.containsKey("errors")) {
//                System.out.println("OneSignal Errors: " + responseBody.get("errors"));
//            }
//        }
//
//        return response.getStatusCode() == HttpStatus.OK;
//    }
//}