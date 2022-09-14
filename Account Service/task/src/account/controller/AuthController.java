package account.controller;

import account.dto.User;
import account.payload.request.NewPasswordRequest;
import account.payload.request.dto.UserSingUpRequest;
import account.payload.response.PasswordResetResponse;
import account.payload.response.dto.UserInfoResponse;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserInfoResponse signup(@RequestBody @Valid UserSingUpRequest newUser) {
        userService.signup(newUser);
        User user = userService.findUserByEmail(newUser.getEmail());
        return new UserInfoResponse(user);
    }

    @PostMapping("/changepass")
    public ResponseEntity<?> changePassword(@RequestBody NewPasswordRequest request,
                                         @AuthenticationPrincipal UserDetails details) {
        userService.changeUserPassword(details.getUsername(), request.getNewPassword());

        return ResponseEntity.ok().body(new PasswordResetResponse(
                details.getUsername(),
                "The password has been updated successfully"));
    }
    
}
