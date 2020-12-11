package id.java.personal.project.dto.response.error;

import id.java.personal.project.constant.StatusConstant;
import org.springframework.http.HttpStatus;

public class StatusResponse extends BaseResponse {
    private String message;
    private Object data;

    public StatusResponse() {}

    public StatusResponse(HttpStatus response, String status, String message, Object data) {
        super(response, status);
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public StatusResponse statusInternalServerError(String message, Object data){
        StatusResponse statusResponse = new StatusResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                StatusConstant.INTERNAL_SERVER_ERROR.getMessage(),
                message,
                data
        );

        return statusResponse;
    }

    public StatusResponse statusNotFound(String message, Object data){
        StatusResponse statusResponse = new StatusResponse(
                HttpStatus.NOT_FOUND,
                StatusConstant.NOT_FOUND.getMessage(),
                message,
                data
        );
        return statusResponse;
    }

    public StatusResponse statusOk(Object data){
        StatusResponse statusResponse = new StatusResponse(
                HttpStatus.OK,
                StatusConstant.OK.getMessage(),
                "Ok",
                data
        );
        return statusResponse;
    }

    public StatusResponse statusCreated(String message, Object data){
        StatusResponse statusResponse = new StatusResponse(
                HttpStatus.CREATED,
                StatusConstant.CREATED.getMessage(),
                message,
                data
        );
        return statusResponse;
    }

    public StatusResponse statusUnauthorized(String message, Object data){
        StatusResponse statusResponse = new StatusResponse(
          HttpStatus.UNAUTHORIZED,
          StatusConstant.UNAUTHORIZED.getMessage(),
          message,
          data
        );
        return statusResponse;
    }
}
