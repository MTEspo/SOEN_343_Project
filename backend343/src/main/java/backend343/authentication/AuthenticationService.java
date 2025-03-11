package backend343.authentication;
import backend343.dto.LoginUserDto;
import backend343.factory.*;
import backend343.repository.UserRepository;
import backend343.dto.RegisterDto;
import backend343.dto.VerifyUserDto;
import backend343.service.EmailService;
import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.model.PromotionCode;
import com.stripe.param.CouponCreateParams;
import com.stripe.param.PromotionCodeCreateParams;
import jakarta.mail.MessagingException;
import backend343.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserFactoryProvider userFactoryProvider;


    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 EmailService emailService,
                                 UserFactoryProvider userFactoryProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.userFactoryProvider = userFactoryProvider;
    }

    public User signup(RegisterDto input) {
        User user = userFactoryProvider.getFactory(input.getRole()).createUser(input, passwordEncoder);

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
        user.setEnabled(false);
        sendVerificationEmail(user);

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input){
        User user = userRepository.findByEmail(input.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        if(!user.isEnabled()){
            throw new RuntimeException("User is not verified");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        return user;
    }

    public void verifyUser(VerifyUserDto input) throws StripeException, MessagingException {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Verification code expired");
            }
            if(user.getVerificationCode().equals(input.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
                sendWelcomeEmailWithCoupon(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else{
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.isEnabled()){
                throw new RuntimeException("User is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void sendWelcomeEmailWithCoupon(User user) throws StripeException, MessagingException {
        String couponCode = createCoupon(20);
        String subject = "Welcome to our website!";
        String htmlMessage = "<h1>Welcome to our website!</h1><p>Thank you for verifying your account. As a token of appreciation, we'd like to offer you a 20% discount on your next purchase. Use the code <strong>" + couponCode + "</strong> at checkout.</p>";
        emailService.sendEmail(user.getEmail(), subject, htmlMessage);
    }

    public String createCoupon(int percentOff) throws StripeException {
        String couponCode = generateCouponCode();

        CouponCreateParams params = CouponCreateParams.builder()
                .setId(couponCode)
                .setPercentOff(BigDecimal.valueOf(percentOff))
                .setMaxRedemptions(1L)
                .build();
        Coupon coupon = Coupon.create(params);

        PromotionCodeCreateParams promotionCodeParams = PromotionCodeCreateParams.builder()
                .setCoupon(coupon.getId())
                .setCode(couponCode)
                .build();
        PromotionCode.create(promotionCodeParams);

        return couponCode;
    }

    private String generateCouponCode() {
        Random random = new Random();
        int code = random.nextInt(999999)+100000;
        return String.valueOf(code);
    }

    private void sendVerificationEmail(User user){
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<h1>Verify your account</h1><p>Use the following code to verify your account: <strong>" + verificationCode + "</strong></p>";
        try{
            emailService.sendVerificationEmail(user.getEmail(),subject,htmlMessage);
        } catch(MessagingException e){
            e.printStackTrace();
        }
    }

    public static String generateVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(999999)+100000;
        return String.valueOf(code);
    }
}
