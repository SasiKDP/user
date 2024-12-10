package com.dataquadinc.service;

import com.dataquadinc.model.UserDetails;
import com.dataquadinc.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ForgotPasswordService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> otpStorage = new HashMap<>();

    public String generateOtp(String email){
        UserDetails user = userDao.findByEmail(email);
        if(user == null){
            throw new RuntimeException("User not found with email: " + email);
        }

        String otp = String.format("%06d", new Random().nextInt(999999));


        otpStorage.put(email,otp);

        sendOtpToEmail(email, otp);

        System.out.println("OTP sent to " + email + ": " + otp);
        return otp;
    }

    private void sendOtpToEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for password reset");
        message.setText("Your OTP for password reset is: " + otp);
        // message.setFrom(email); // Change this to your email address

        try {
            mailSender.send(message);
            System.out.println("OTP sent to " + email);
        } catch (Exception e) {
            System.out.println("Error sending to " + email);
            throw new RuntimeException("Error sending OTP email: " + e.getMessage());
        }
    }


    public boolean verifyOtp(String email, String otp) {
        // Retrieve OTP from memory storage
        String storedOtp = otpStorage.get(email);
        if (storedOtp == null) {
            throw new RuntimeException("OTP has expired or does not exist.");
        }

        // Compare the OTPs
        return storedOtp.equals(otp);
    }

    public void updatePassword(String email, String updatePassword) {
        // Fetch user details from the database by email
        UserDetails user = userDao.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        // Check if the new password is the same as the current one
        if (BCrypt.checkpw(updatePassword, user.getPassword())) {
            throw new RuntimeException("The new password cannot be the same as the previous password.");
        }

        // Hash the new password
        String hashedPassword = BCrypt.hashpw(updatePassword, BCrypt.gensalt());

        // Update the user's password in the database
        user.setPassword(hashedPassword);
        userDao.save(user);

        System.out.println("Password updated successfully for " + email);

    }



}
