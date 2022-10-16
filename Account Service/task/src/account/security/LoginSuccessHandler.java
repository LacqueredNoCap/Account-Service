package account.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import account.entity.User;
import account.service.access.UserAccessService;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserAccessService userAccessService;

    public LoginSuccessHandler(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userAccessService.findUserByEmail(userDetails.getUsername());
        if (user.getFailedAttempts() > 0) {
            userAccessService.resetFailedAttempts(user);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
