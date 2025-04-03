package com.dataquadinc.controller;
import com.dataquadinc.dto.LogoutResponseDTO;
import com.dataquadinc.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = {
        "http://35.188.150.92",
        "http://192.168.0.140:3000",
        "http://192.168.0.139:3000",
        "https://mymulya.com",
        "http://localhost:3000",
        "http://192.168.0.135:8080", // Sixth IP
        "http://182.18.177.16:443", // Seventh IP
        "http://192.168.0.135:80", // Eighth IP
        "http://localhost/", // Ninth IP
        "http://mymulya.com:443" // Tenth IP
})

@RestController
@RequestMapping("/users")
public class  LogoutController {

    private final LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @PutMapping("/logout/{userId}")
    public LogoutResponseDTO logout(@PathVariable String userId) {

        return logoutService.logout(userId);
    }
}


