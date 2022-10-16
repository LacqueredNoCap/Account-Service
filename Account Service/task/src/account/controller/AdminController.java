package account.controller;

import account.dto.request.UserLockRequest;
import account.dto.response.StatusResponse;
import account.entity.User;
import account.dto.request.UserRoleChangeRequest;
import account.dto.response.UserDeletedResponse;
import account.dto.response.info.UserInfoResponse;
import account.service.UserRoleService;
import account.service.UserService;
import account.service.access.UserAccessService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

//    hasRole('ROLE_<...>')
//    hasAnyRole('ROLE_<...>', 'ROLE_<...>', ...)
//    hasAuthority('permission')
//    hasAnyAuthority('permission1', 'permission2', ...)

@RestController
@RequestMapping("/api/admin/user")
public class AdminController {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final UserAccessService userAccessService;

    public AdminController(
            UserService userService,
            UserRoleService userRoleService,
            UserAccessService userAccessService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.userAccessService = userAccessService;
    }

    @GetMapping
    public List<UserInfoResponse> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(UserInfoResponse::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{email}")
    public UserDeletedResponse deleteUser(
            @PathVariable(value = "email", required = false) String email) {
        userService.deleteUserByEmail(email);
        return new UserDeletedResponse(email, "Deleted successfully!");
    }

    @DeleteMapping
    public void deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUserByEmail(userDetails.getUsername());
    }

    @PutMapping("/role")
    public UserInfoResponse changeUserRoles(@RequestBody UserRoleChangeRequest request) {
        userRoleService.changeUserRole(request);

        User user = userService.findUserByEmail(request.getUser());
        return new UserInfoResponse(user);
    }

    @PutMapping("/access")
    public StatusResponse provideAccessToUser(@RequestBody UserLockRequest userLockRequest) {
        userAccessService.provideAccessToUser(userLockRequest.getUser(), userLockRequest.getRoleOperation());

        return new StatusResponse(
                String.format("User %s %sed!",
                userLockRequest.getUser(),
                userLockRequest.getRoleOperation().toLowerCase(Locale.ENGLISH))
        );
    }
}
