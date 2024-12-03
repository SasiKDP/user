package com.dataquadinc.service;
import com.dataquadinc.dto.LogoutResponse;
import com.dataquadinc.exceptions.InvaildUserException;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.repository.UserDao;
import com.dataquadinc.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class LogoutService {
    @Autowired
    private UserDao userDao;


    public LogoutResponse logoutUser(String userId) {

        UserDetails user = userDao.findByUserId(userId);

        // Check if the user is null, if so, throw an exception
        if (user == null) {
            throw new InvaildUserException("User Not Found With Id: " + userId);
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        user.setLastLoginTime(localDateTime);
        userDao.save(user);
        LogoutResponse response = new LogoutResponse();
        response.setUserId(userId);
        response.setLogoutTime(localDateTime);
        return response;

    }
}







