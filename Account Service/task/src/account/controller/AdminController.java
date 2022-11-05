package account.controller;

import account.dto.request.UserAccessRequest;
import account.dto.response.StatusResponse;
import account.entity.User;
import account.dto.request.UserRoleChangeRequest;
import account.dto.response.UserDeletedResponse;
import account.dto.response.info.UserInfoResponse;
import account.service.UserRoleService;
import account.service.UserService;
import account.service.access.UserAccessService;
import account.service.event.EventEnum;
import account.service.event.EventService;
import account.service.role.RoleOperation;
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
    private final EventService eventService;

    public AdminController(
            UserService userService,
            UserRoleService userRoleService,
            UserAccessService userAccessService,
            EventService eventService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.userAccessService = userAccessService;
        this.eventService = eventService;
    }

    @GetMapping
    public List<UserInfoResponse> getAllUsers() {
        return userService.findAllUsers().stream()
                .map(UserInfoResponse::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{email}")
    public UserDeletedResponse deleteUser(
            @PathVariable(value = "email", required = false) String email,
            @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUserByEmail(email);

        eventService.makeEvent(
                EventEnum.DELETE_USER,
                userDetails.getUsername(),
                email.toLowerCase(Locale.ENGLISH),
                "/api/admin/user/role"
        );

        return new UserDeletedResponse(email, "Deleted successfully!");
    }

    @DeleteMapping
    public void deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUserByEmail(userDetails.getUsername());
    }

    @PutMapping("/role")
    public UserInfoResponse changeUserRoles(
            @RequestBody UserRoleChangeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        userRoleService.changeUserRole(request);

        eventService.makeEvent(
                EventEnum.valueOf(request.getOperation() + "_ROLE"),
                userDetails.getUsername(),
                String.format(
                        "%s role %s %s %s",
                        request.getOperation().charAt(0) +
                                request.getOperation()
                                        .substring(1)
                                        .toLowerCase(Locale.ENGLISH),
                        request.getRole(),
                        RoleOperation.valueOf(request.getOperation()).equals(RoleOperation.GRANT) ?
                                "to" : "from",
                        request.getUser().toLowerCase(Locale.ENGLISH)
                ),
                "/api/admin/user/role"
        );

        User user = userService.findUserByEmail(request.getUser());
        return new UserInfoResponse(user);
    }

    @PutMapping("/access")
    public StatusResponse provideAccessToUser(
            @RequestBody UserAccessRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        userAccessService.provideAccessToUser(request.getUser(), request.getOperation());

        eventService.makeEvent(
                EventEnum.valueOf(request.getOperation() + "_USER"),
                userDetails.getUsername(),
                String.format(
                        "%s user %s",
                        request.getOperation().charAt(0) + request.getOperation().substring(1).toLowerCase(Locale.ENGLISH),
                        request.getUser().toLowerCase(Locale.ENGLISH)
                ),
                "/api/admin/user/access"
        );

        return new StatusResponse(
                String.format("User %s %sed!",
                request.getUser(),
                request.getOperation().toLowerCase(Locale.ENGLISH))
        );
    }
}
