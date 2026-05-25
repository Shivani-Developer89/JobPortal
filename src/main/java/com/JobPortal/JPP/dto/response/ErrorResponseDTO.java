package com.JobPortal.JPP.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.message.Message;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    private LocalDateTime timestamp;

    private int status;

    private String message;

    private String details;
}


