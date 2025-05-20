package com.bogdan.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Response",
        description = "Schema to hold successful response information")
public class ResponseDTO {

    @Schema(description = "Status code in the response.")
    private String statusCode;

    @Schema(description = "Status message in the response.")
    private String statusMsg;

    //Getter, Setter, ToString

    public ResponseDTO(String statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "statusCode='" + statusCode + '\'' +
                ", statusMsg='" + statusMsg + '\'' +
                '}';
    }
}
