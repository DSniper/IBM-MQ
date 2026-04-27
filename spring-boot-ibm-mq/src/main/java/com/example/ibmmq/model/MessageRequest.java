package com.example.ibmmq.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Request DTO for message publishing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("priority")
    @Builder.Default
    private int priority = 4;

    @JsonProperty("retry_count")
    @Builder.Default
    private int retryCount = 0;
}
