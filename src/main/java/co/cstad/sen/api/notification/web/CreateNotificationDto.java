package co.cstad.sen.api.notification.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;


public record CreateNotificationDto(@JsonProperty("included_segments")
                                    String[] includedSegments,
                                    ContentDto contents,
                                    @JsonProperty("app_id")
                                    @Value("${onesignal.app-id}")
                                    String appId
) {
}