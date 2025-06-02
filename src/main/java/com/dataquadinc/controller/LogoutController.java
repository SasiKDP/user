package com.dataquadinc.controller;
import com.dataquadinc.dto.LogoutResponseDTO;
import com.dataquadinc.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class LogoutController {

    private final LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @PutMapping("/logout/{userId}")
    public ResponseEntity<LogoutResponseDTO> logout(@PathVariable String userId,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        LogoutResponseDTO logoutResponse = logoutService.logout(userId);

        if (logoutResponse.isSuccess()) {
            System.out.println("=== CONTROLLER: User logged out successfully. Clearing cookie... ===");

            // Clear the authToken cookie using ResponseCookie
            ResponseCookie clearCookie = ResponseCookie.from("authToken", "")
                    .httpOnly(true)
                    .secure(false)  // Set to true if you're using HTTPS in production
                    .path("/")
                    .maxAge(0)     // This instructs browser to delete the cookie
                    .sameSite("Lax")  // Keep sameSite as it was when setting the cookie
                    .build();

            // Add Set-Cookie header to response to clear cookie
            response.setHeader("Set-Cookie", clearCookie.toString());

            return ResponseEntity.ok(logoutResponse);
        } else {
            System.out.println("=== CONTROLLER: Logout failed. Returning 400 ===");
            return ResponseEntity.badRequest().body(logoutResponse);
        }
    }
}
