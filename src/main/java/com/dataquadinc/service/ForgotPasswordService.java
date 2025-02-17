package com.dataquadinc.service;

import com.dataquadinc.dto.ForgotResponseDto;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.repository.UserDao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> otpStorage = new HashMap<>();

    public ForgotResponseDto generateOtp(String email) {
        if (!isValidEmailFormat(email)) {
            return new ForgotResponseDto(false, "Invalid email format: " + email, "Email format is invalid.");
        }

        if (!hasValidMXRecord(email)) {
            return new ForgotResponseDto(false, "Invalid email domain: " + email, "Invalid email address.");
        }

        if (!isEmailValidOnServer(email)) {
            return new ForgotResponseDto(false, "The email address does not exist on the server: " + email, "Email not found.");
        }

        UserDetails user = userDao.findByEmail(email);
        if (user == null) {
            return new ForgotResponseDto(false, "User not found: " + email, "User does not exist.");
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        try {
            sendOtpToEmail(email, otp);
        } catch (MailSendException e) {
            return new ForgotResponseDto(false, "Failed to send OTP: " + email, "Email sending failed.");
        } catch (Exception e) {
            return new ForgotResponseDto(false, "Unexpected error while sending OTP: " + e.getMessage(), "Error sending email.");
        }

        return new ForgotResponseDto(true, "OTP sent successfully.", null);
    }

    private boolean isValidEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && Pattern.matches(emailRegex, email);
    }

    private boolean hasValidMXRecord(String email) {
        String domain = email.substring(email.indexOf("@") + 1);
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});
            return attrs.get("MX") != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmailValidOnServer(String email) {
        String domain = email.substring(email.indexOf("@") + 1);

        // Step 1: Try MX Record Lookup
        if (!hasValidMXRecord(domain)) {
            return false;
        }

        // Step 2: Try SMTP Validation
        boolean smtpValid = smtpEmailCheck(email, domain);
        if (smtpValid) {
            return true;
        }

        // Step 3: If SMTP fails, use ZeroBounce API
        return checkEmailWithZeroBounce(email);
    }
//
//    private boolean checkEmailWithZeroBounce(String email) {
//        String apiKey = "e7f17e34886d4d1a9078dec6ca241cb6";  // Replace with your actual API key
//        String url = "https://api.zerobounce.net/v2/validate?api_key=" + apiKey + "&email=" + email;
//
//        try {
//            URL obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            con.setRequestMethod("GET");
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuilder response = new StringBuilder();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//            JSONObject jsonResponse = new JSONObject(response.toString());
//            String status = jsonResponse.getString("status");
//
//            // ✅ Log for debugging
//            System.out.println("ZeroBounce Response: " + jsonResponse.toString());
//
//            // Check if email is valid
//            return "valid".equalsIgnoreCase(status);
//        } catch (Exception e) {
//            System.out.println("ZeroBounce API error: " + e.getMessage());
//            return false;
//        }
//    }

    // ✅ Method to check if the email server accepts the email using SMTP
    private boolean smtpEmailCheck(String email, String domain) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("smtp." + domain, 25), 5000); // Connect to SMTP server
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Read server greeting
            String response = reader.readLine();
            if (response == null || !response.startsWith("220")) {
                System.out.println("SMTP Connection failed: " + response);
                socket.close();
                return false;
            }

            // HELO Command
            writer.println("HELO mydomain.com");
            reader.readLine();

            // MAIL FROM Command
            writer.println("MAIL FROM:<test@mydomain.com>");
            reader.readLine();

            // RCPT TO Command (Check if email exists)
            writer.println("RCPT TO:<" + email + ">");
            response = reader.readLine();  // Read response

            // QUIT Command
            writer.println("QUIT");
            socket.close();

            // Log response for debugging
            System.out.println("SMTP Response for " + email + ": " + response);

            // Fix: Handle null response before calling startsWith()
            return response != null && response.startsWith("250");
        } catch (IOException e) {
            System.out.println("SMTP Error: " + e.getMessage());
            return false; // SMTP check failed
        }
    }


    // Function to check email via an external API
    private boolean checkEmailWithZeroBounce(String email) {
        String apiKey = "e7f17e34886d4d1a9078dec6ca241cb6";  // Replace with your actual API key
        String url = "https://api.zerobounce.net/v2/validate?api_key=" + apiKey + "&email=" + email;

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("ZeroBounce API Response Code: " + responseCode);

            if (responseCode != 200) {
                System.out.println("ZeroBounce API call failed. HTTP Code: " + responseCode);
                return false;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // ✅ Print full API response
            System.out.println("ZeroBounce Response: " + response.toString());

            // Parse JSON response safely
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.toString());

            // ✅ Check if "status" field exists
            JsonNode statusNode = jsonResponse.get("status");
            if (statusNode == null) {
                System.out.println("ZeroBounce API did not return a valid status field.");
                return false;
            }

            String status = statusNode.asText();
            return "valid".equalsIgnoreCase(status);

        } catch (Exception e) {
            System.out.println("ZeroBounce API error: " + e.getMessage());
            return false;
        }
    }


    private void sendOtpToEmail(String email, String otp) throws MailSendException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for password reset");
        message.setText("Your OTP for password reset is: " + otp);

        try {
            mailSender.send(message);
        } catch (MailSendException e) {
            throw new MailSendException("Failed to send OTP: " + email, e);
        }
    }

    public ForgotResponseDto verifyOtp(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        if (storedOtp == null) {
            return new ForgotResponseDto(false, "OTP expired or missing.", "OTP invalid.");
        }

        if (storedOtp.equals(otp)) {
            return new ForgotResponseDto(true, "OTP verified successfully.", null);
        } else {
            return new ForgotResponseDto(false, "Invalid OTP.", "OTP incorrect.");
        }
    }

    public ForgotResponseDto updatePassword(String email, String updatePassword) {
        UserDetails user = userDao.findByEmail(email);
        if (user == null) {
            return new ForgotResponseDto(false, "User not found: " + email, "User does not exist.");
        }

        if (BCrypt.checkpw(updatePassword, user.getPassword())) {
            return new ForgotResponseDto(false, "New password cannot be the same as the old one.", "Password reuse not allowed.");
        }

        String hashedPassword = BCrypt.hashpw(updatePassword, BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userDao.save(user);
        otpStorage.remove(email);
        sendPasswordUpdateConfirmationEmail(email);

        return new ForgotResponseDto(true, "Password updated successfully.", null);
    }

    private void sendPasswordUpdateConfirmationEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Updated Successfully");
        message.setText("Your password has been updated. If this wasn't you, contact support.");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error sending confirmation email: " + e.getMessage());
        }
    }
}
