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

    // OTP Generation and Storage
    public String generateOtp(String email) {
        UserDetails user = userDao.findByEmail(email);
        if(user == null){
            throw new RuntimeException("User not found with email: " + email);
        }

        // Generate a 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Save OTP in memory storage (Map)
        otpStorage.put(email, otp);

        // Send OTP to user's email
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

//    public void updatePassword(String email, String updatePassword) {
//        // Fetch user details from the database by email
//        UserDetails user = userDao.findByEmail(email);
//        if (user == null) {
//            throw new RuntimeException("User not found with email: " + email);
//        }
//
//        // Check if the new password is the same as the current one
//        if (BCrypt.checkpw(updatePassword, user.getPassword())) {
//            throw new RuntimeException("The new password cannot be the same as the previous password.");
//        }
//
//        // Hash the new password
//        String hashedPassword = BCrypt.hashpw(updatePassword, BCrypt.gensalt());
//
//        // Update the user's password in the database
//        user.setPassword(hashedPassword);
//        userDao.save(user);
//
//        System.out.println("Password updated successfully for " + email);
//
//    }

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

        // Step 6: Clear the OTP after successful password change
        otpStorage.remove(email);

        // Send confirmation email
        sendPasswordUpdateConfirmationEmail(email);
    }

    private void sendPasswordUpdateConfirmationEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Updated Successfully");
        message.setText("Your password has been updated successfully. You can now log in using your new credentials.\n\n"
                + "If you did not request this change, please contact our support team immediately.");

        try {
            mailSender.send(message);
            System.out.println("Password update confirmation email sent to " + email);
        } catch (Exception e) {
            System.out.println("Error sending confirmation email to " + email);
            throw new RuntimeException("Error sending confirmation email: " + e.getMessage());
        }
    }





}
