package com.paymentchain.TransactionService.common;

import io.swagger.annotations.ApiModelProperty;

public class StandarizedApiExceptionResponse {

    //Propiedad del API
    @ApiModelProperty(notes = "The unique uri identifier that categorizes the error",
            name = "type", required = true, example = "errors/authentication/not-authorized")
    private String type = "/errors/transaction";

    @ApiModelProperty(notes = "A brief, human-redeable message about the error", name = "tittle",
            required = true, example = "The User not have authorization")
    private String tittle;

    @ApiModelProperty(notes = "The unique code error", name = "code", required = true, example = "231")
    private String code;

    @ApiModelProperty(notes = "A human-redeable explanation of the error", name = "detail",
            required = true, example = "The user does not hava than propertly persmissions"
            + "to access the resource. Please contact with: ...")
    private String detail;

    @ApiModelProperty(notes = "A URI that identifies the specific ocurrence of the error",
            name = "instance", required = true,
            example = "/errors/authentication/not-authorized/01")
    private String instance = "/errors/transaction/blank";

    public StandarizedApiExceptionResponse(String tittle, String code, String detail) {
        this.tittle = tittle;
        this.code = code;
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

}
