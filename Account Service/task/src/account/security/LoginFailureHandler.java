package account.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import account.entity.User;
import account.service.access.UserAccessService;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserAccessService userAccessService;

    @Value("${security.maxFailedAttempts}")
    private int maxFailedAttempts;

    public LoginFailureHandler(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String authorization = request.getHeader("Authorization");
        validateAuthorizationHeader(authorization);
        String[] principal = decodePrincipal(authorization);

        User user = userAccessService.findUserByEmail(principal[0]);

        if (user.isNotLocked()) {
            userAccessService.increaseFailedAttempts(user);
            if (user.getFailedAttempts() == maxFailedAttempts) {
                userAccessService.lock(user);
                exception = new LockedException(String.format(
                        "Your account has been locked due to %d consecutive failed attempts.",
                        maxFailedAttempts));
            }
        } else {
            exception = new LockedException("Your account is locked.");
        }

        super.onAuthenticationFailure(request, response, exception);
    }

    private void validateAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null ||
                !authorizationHeader.toLowerCase().startsWith("basic")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "No basic auth found"
            );
        }
    }

    private String[] decodePrincipal(String authorizationHeader) {
        // Authorization: Basic encodedInBase64(username:password)
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        byte[] decodedCredentials = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedCredentials, StandardCharsets.UTF_8);
        // credentials = username:password
        return credentials.split(":", 2);
    }

}
