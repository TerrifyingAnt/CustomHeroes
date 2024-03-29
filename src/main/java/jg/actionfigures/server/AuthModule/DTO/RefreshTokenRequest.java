package jg.actionfigures.server.AuthModule.DTO;

// * Класс передачи информации о токене обновления и логине пользователя
public class RefreshTokenRequest {
    private String refreshToken;
    private String username;

    public RefreshTokenRequest() {}

    public RefreshTokenRequest(String refreshToken, String username) {
        this.refreshToken = refreshToken;
        this.username = username;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
