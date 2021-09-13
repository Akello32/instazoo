package by.matmux.instazoo.web;

import by.matmux.instazoo.payload.request.LoginRequest;
import by.matmux.instazoo.payload.request.SignupRequest;
import by.matmux.instazoo.payload.response.JWTTokenSuccessResponse;
import by.matmux.instazoo.payload.response.MessageResponse;
import by.matmux.instazoo.security.JWTokenProvider;
import by.matmux.instazoo.security.SecurityConstants;
import by.matmux.instazoo.service.UserService;
import by.matmux.instazoo.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    @Autowired
    private JWTokenProvider jwTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ResponseErrorValidation errorValidation;
    @Autowired
    private UserService service;

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = errorValidation.mapValidationService(bindingResult);

        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult result) {
        ResponseEntity<Object> errors = errorValidation.mapValidationService(result);

        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        service.createUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
}
