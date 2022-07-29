package account.controller;

import account.payload.request.NewPasswordRequest;
import account.payload.response.PasswordResetResponse;
import account.dto.User;
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
    public User signup(@RequestBody @Valid User newUser) {
        userService.register(newUser);
        return newUser;
    }

    @PostMapping("/changepass")
    public ResponseEntity<?> changePassword(@RequestBody NewPasswordRequest newPassword,
                                         @AuthenticationPrincipal UserDetails details) {
        userService.changePassword(newPassword.getNewPassword(), details.getUsername());

        return ResponseEntity.ok().body(new PasswordResetResponse(
                details.getUsername(),
                "The password has been updated successfully"));
    }
    
}
