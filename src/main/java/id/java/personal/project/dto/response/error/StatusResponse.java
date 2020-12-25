package id.java.personal.project.dto.response.error;

import id.java.personal.project.constant.StatusEnum;
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
        return new StatusResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                StatusEnum.INTERNAL_SERVER_ERROR.getMessage(),
                message,
                data
        );
    }

    public StatusResponse statusNotFound(String message, Object data){
        return new StatusResponse(
                HttpStatus.NOT_FOUND,
                StatusEnum.NOT_FOUND.getMessage(),
                message,
                data
        );
    }

    public StatusResponse statusOk(Object data){
        return new StatusResponse(
                HttpStatus.OK,
                StatusEnum.OK.getMessage(),
                "Ok",
                data
        );
    }

    public StatusResponse statusCreated(String message, Object data){
        return new StatusResponse(
                HttpStatus.CREATED,
                StatusEnum.CREATED.getMessage(),
                message,
                data
        );
    }

    public StatusResponse statusUnauthorized(String message, Object data){
        return new StatusResponse(
          HttpStatus.UNAUTHORIZED,
          StatusEnum.UNAUTHORIZED.getMessage(),
          message,
          data
        );
    }

    public StatusResponse statusBadRequest(String message, Object data){
        return new StatusResponse(
                HttpStatus.BAD_REQUEST,
                StatusEnum.BAD_REQUEST.getMessage(),
                message,
                null
        );
    }

    public StatusResponse statusConflict(String message, Object data){
        return new StatusResponse(
                HttpStatus.CONFLICT,
                StatusEnum.CONFLICT.getMessage(),
                message,
                data
        );
    }

    public StatusResponse statusNotModified(String message, Object data){
        return new StatusResponse(
                HttpStatus.NOT_MODIFIED,
                StatusEnum.NOT_MODIFIED.getMessage(),
                message,
                data
        );
    }
}
