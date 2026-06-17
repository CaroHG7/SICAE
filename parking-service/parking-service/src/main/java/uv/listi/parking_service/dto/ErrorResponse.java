package uv.listi.parking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String error;
    private boolean success;
    private String message;
    private String path;
    
    
}
