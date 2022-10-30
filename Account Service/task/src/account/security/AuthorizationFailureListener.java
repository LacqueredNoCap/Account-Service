package account.security;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import account.service.event.EventService;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthorizationFailureListener implements
        ApplicationListener<AuthorizationFailureEvent> {

    private final EventService eventService;
    private final HttpServletRequest request;

    public AuthorizationFailureListener(EventService eventService, HttpServletRequest request) {
        this.eventService = eventService;
        this.request = request;
    }

    @Override
    public void onApplicationEvent(AuthorizationFailureEvent event) {
//        String authorization = request.getHeader("Authorization");
//        validateAuthorizationHeader(authorization);
//        String username = decodePrincipal(authorization);
//
//        eventService.makeEvent(
//                EventEnum.ACCESS_DENIED,
//                username.toLowerCase(Locale.ENGLISH),
//                request.getRequestURI(),
//                request.getRequestURI()
//        );
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
