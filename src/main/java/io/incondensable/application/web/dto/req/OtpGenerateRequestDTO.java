package io.incondensable.application.web.dto.req;

/**
 * @author abbas
 */
public class OtpGenerateRequestDTO {
    private String userId;
    private String emailAddress;

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
}
