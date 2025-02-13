package com.dataquadinc.service;

import com.dataquadinc.dto.UserVerifyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class UserVerifyService {

    private static final Logger logger = LoggerFactory.getLogger(UserVerifyService.class);
    private final JavaMailSender javaMailSender;
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, Long> otpTimestamps = new ConcurrentHashMap<>();
    private final Random random = new Random();

    private static final long OTP_EXPIRY_TIME_MS = 5 *60*1000; // 5 minutes
    private static final long OTP_COOLDOWN_MS = 60 * 1000; // 1 minute

    public UserVerifyService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        startOtpCleanupTask();
    }

    public String sendOtp(String email) {
        if (email == null || email.isEmpty()) {
            return "Invalid email address.";
        }

        // Trim the email to remove any whitespace
        email = email.trim();

        // Check cooldown period
        long currentTime = System.currentTimeMillis();
        if (otpTimestamps.containsKey(email) && (currentTime - otpTimestamps.get(email)) < OTP_COOLDOWN_MS) {
            return "Please wait before requesting a new OTP.";
        }

        // Generate a 6-digit OTP
        String otp = String.format("%06d", random.nextInt(999999));

        // Store OTP with timestamp
        otpStorage.put(email, otp.trim()); // Ensure OTP is trimmed
        otpTimestamps.put(email, currentTime);
        logger.info("Stored OTP for {}: {}", email, otp);

        // Send OTP via email
        sendOtpEmail(email, otp);

        return "OTP sent successfully to " + email;
    }

    public String verifyOtp(UserVerifyDto userDTO) {
        if (userDTO == null || userDTO.getEmail() == null || userDTO.getOtp() == null) {
            return "Email and OTP are required.";
        }

        // Trim inputs
        String email = userDTO.getEmail().trim();
        String enteredOtp = userDTO.getOtp().trim();

        // Retrieve stored OTP
        String storedOtp = otpStorage.get(email);
        logger.info("Verifying OTP for {}. Entered: {}, Stored: {}", email, enteredOtp, storedOtp);
        logger.info("Current OTP storage before verification: {}", otpStorage);

        if (storedOtp == null) {
            logger.warn("No OTP found for email: {}", email);
            return "Invalid or expired OTP.";
        }

        if (storedOtp.equals(enteredOtp)) {
            otpStorage.remove(email); // Remove OTP after successful verification
            otpTimestamps.remove(email); // Remove timestamp to allow new requests
            logger.info("OTP verification successful for {}", email);
            return "Email verified successfully!";
        }

        logger.warn("OTP verification failed for {}. Entered: {}, Expected: {}", email, enteredOtp, storedOtp);
        return "Invalid or expired OTP.";
    }

    private void sendOtpEmail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP is: " + otp + ". It is valid for 5 minutes.");
            javaMailSender.send(message);
            logger.info("OTP email sent successfully to {}", email);
        } catch (Exception e) {
            logger.error("Failed to send OTP email to {}: {}", email, e.getMessage());
        }
    }

    private void startOtpCleanupTask() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            logger.info("Cleaning expired OTPs at time: {}", currentTime);

            otpStorage.entrySet().removeIf(entry ->
                    (currentTime - otpTimestamps.getOrDefault(entry.getKey(), 0L)) > OTP_EXPIRY_TIME_MS);
            otpTimestamps.entrySet().removeIf(entry ->
                    (currentTime - entry.getValue()) > OTP_EXPIRY_TIME_MS);

            logger.info("Current OTP storage after cleanup: {}", otpStorage);
        }, 1, 1, TimeUnit.MINUTES);
    }
}