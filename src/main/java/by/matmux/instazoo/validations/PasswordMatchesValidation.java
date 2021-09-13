package by.matmux.instazoo.validations;

import by.matmux.instazoo.annotations.PasswordMatches;
import by.matmux.instazoo.payload.request.SignupRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidation implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        SignupRequest signupRequest = (SignupRequest) obj;
        boolean res = signupRequest.getPassword().equals(signupRequest.getConfirmPassword());
        return res;
    }
}
