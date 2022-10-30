package account.security;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import account.service.access.UserAccessService;


@Component
public class AuthenticationSuccessEventListener implements
        ApplicationListener<AuthenticationSuccessEvent> {

    private final UserAccessService userAccessService;
    private final HttpServletRequest request;

    public AuthenticationSuccessEventListener(
            UserAccessService userAccessService,
            HttpServletRequest request) {
        this.userAccessService = userAccessService;
        this.request = request;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        System.out.println("SUCCESS AUTHENTICATION");

        String authorization = request.getHeader("Authorization");
        validateAuthorizationHeader(authorization);
        String username = decodePrincipal(authorization);

        userAccessService.onSuccessLoginAttempt(username);
    }

    private void validateAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null ||
                !authorizationHeader.toLowerCase().startsWith("basic")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "No basic auth found"
            );
        }
    }

    private String decodePrincipal(String authorizationHeader) {
        // Authorization: Basic encodedInBase64(username:password)
        String base64Credentials = authorizationHeader.substring("Basic ".length());
        byte[] decodedCredentials = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decodedCredentials, StandardCharsets.UTF_8);
        // credentials = username:password
        return credentials.split(":", 2)[0];
    }
}
