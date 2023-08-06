package io.incondensable.application.web.dto;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author abbas
 */
public class OtpGeneratedPayload {

//    @Serial
//    private final static long serialVersionUID = 806953693294748654L;

    private String userId;
    private String emailAddress;
    private int otpCode;

    public OtpGeneratedPayload() {
    }

    public OtpGeneratedPayload(String userId, String emailAddress, int otpCode) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.otpCode = otpCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(int otpCode) {
        this.otpCode = otpCode;
    }
}
