package kr.enterkey.commons;

import lombok.Data;

import java.util.List;

/**
 * Created by enterkey88 on 2016-10-11.
 */
@Data
public class ErrorResponse {
    private String message;

    private String code;

    private List<FieldError> errors;

    public static class FieldError {
        private String field;
        private String value;
        private String reason;
    }
}
