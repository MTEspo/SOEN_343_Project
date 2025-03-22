package backend343.controller;

import backend343.models.User;
import backend343.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/update-username/{userId}")
    public User updateUsername(@PathVariable Long userId, @RequestParam String newUsername) {
        return userDetailsService.updateUsername(userId, newUsername);
    }
}
