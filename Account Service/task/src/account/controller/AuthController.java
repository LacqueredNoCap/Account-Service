package account.controller;

import account.entity.User;
import account.dto.request.NewPasswordRequest;
import account.dto.request.info.UserSingUpRequest;
import account.dto.response.PasswordResetResponse;
import account.dto.response.info.UserInfoResponse;
import account.service.UserService;
import account.service.event.EventEnum;
import account.service.event.EventService;
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
    private final EventService eventService;

    @Autowired
    public AuthController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @PostMapping("/signup")
    public UserInfoResponse signup(@RequestBody @Valid UserSingUpRequest newUser) {
        userService.signup(newUser);
        User user = userService.findUserByEmail(newUser.getEmail());

        eventService.makeEvent(
                EventEnum.CREATE_USER,
                "Anonymous",
                user.getEmail(),
                "/api/auth/signup"
                );

        return new UserInfoResponse(user);
    }

    @PostMapping("/changepass")
    public ResponseEntity<?> changePassword(
            @RequestBody NewPasswordRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.changeUserPassword(userDetails.getUsername(), request.getNewPassword());

        eventService.makeEvent(
                EventEnum.CHANGE_PASSWORD,
                userDetails.getUsername(),
                userDetails.getUsername(),
                "/api/auth/changepass"
        );

        return ResponseEntity.ok().body(new PasswordResetResponse(
                userDetails.getUsername(),
                "The password has been updated successfully")
        );
    }
    
}
