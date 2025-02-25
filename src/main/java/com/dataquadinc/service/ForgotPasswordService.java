
package com.dataquadinc.service;

import com.dataquadinc.dto.ForgotResponseDto;
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

    // OTP Generation and Storage
    public ForgotResponseDto generateOtp(String email) {
        UserDetails user = userDao.findByEmail(email);
        if (user == null) {
            return new ForgotResponseDto(false, "User not found with email: " + email, "User does not exist.");
        }

        // Generate a 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Save OTP in memory storage (Map)
        otpStorage.put(email, otp);

        // Send OTP to user's email
        sendOtpToEmail(email, otp);

        return new ForgotResponseDto(true, "OTP sent successfully. Please check your email.", null);
    }

    private void sendOtpToEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for password reset");
        message.setText("Your OTP for password reset is: " + otp);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error sending OTP email: " + e.getMessage());
        }
    }

    public ForgotResponseDto verifyOtp(String email, String otp) {
        // Retrieve OTP from memory storage
        String storedOtp = otpStorage.get(email);
        if (storedOtp == null) {
            return new ForgotResponseDto(false, "OTP has expired or does not exist.", "OTP is expired or missing.");
        }

        // Compare the OTPs
        if (storedOtp.equals(otp)) {
            return new ForgotResponseDto(true, "OTP verified successfully.", null);
        } else {
            return new ForgotResponseDto(false, "Invalid OTP. Please try again.", "Invalid OTP.");
        }
    }

    public ForgotResponseDto updatePassword(String email, String updatePassword) {
        // Fetch user details from the database by email
        UserDetails user = userDao.findByEmail(email);
        if (user == null) {
            return new ForgotResponseDto(false, "User not found with email: " + email, "User does not exist.");
        }

        // Check if the new password is the same as the current one
        if (BCrypt.checkpw(updatePassword, user.getPassword())) {
            return new ForgotResponseDto(false, "The new password cannot be the same as the previous password.", "New password matches the old one.");
        }

        // Hash the new password
        String hashedPassword = BCrypt.hashpw(updatePassword, BCrypt.gensalt());

        // Update the user's password in the database
        user.setPassword(hashedPassword);
        userDao.save(user);

        // Clear the OTP after successful password change
        otpStorage.remove(email);

        // Send confirmation email
        sendPasswordUpdateConfirmationEmail(email);

        return new ForgotResponseDto(true, "Password updated successfully.", null);
    }

    private void sendPasswordUpdateConfirmationEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Updated Successfully");
        message.setText("Your password has been updated successfully. You can now log in using your new credentials.\n\n"
                + "If you did not request this change, please contact our support team immediately.");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error sending confirmation email: " + e.getMessage());
        }
    }
}
