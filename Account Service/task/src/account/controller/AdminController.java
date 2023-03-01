package account.controller;

import account.dto.User;
import account.payload.request.UserRoleChangeRequest;
import account.payload.response.UserDeletedResponse;
import account.payload.response.dto.UserInfoResponse;
import account.service.UserRoleService;
import account.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//    hasRole('ROLE_<...>')
//    hasAnyRole('ROLE_<...>', 'ROLE_<...>', ...)
//    hasAuthority('permission')
//    hasAnyAuthority('permission1', 'permission2', ...)

@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    private final UserService userService;
    private final UserRoleService userRoleService;

    public AdminController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @GetMapping
    public List<UserInfoResponse> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(UserInfoResponse::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{email}")
    public UserDeletedResponse deleteUser(@PathVariable(value = "email", required = false) String email) {
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
}
