package br.com.victorpfranca.mybudget.view.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import br.com.victorpfranca.mybudget.view.Messages;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = createPasswordValidator();
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        for (String string : messages) {
            context.buildConstraintViolationWithTemplate(string).addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        messages.forEach(messageTemplate -> context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation());
        //context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation().disableDefaultConstraintViolation();
        //String messageTemplate = messages.stream().collect(Collectors.joining(","));
        return false;
    }

    private static PasswordValidator createPasswordValidator() {
        MessageResolver messageResolver = new PropertiesMessageResolver() {
            @Override
            protected String getMessage(String key) {
                return Messages.msg(key);
            }
        };
        PasswordValidator validator = new PasswordValidator(messageResolver,
                Arrays.asList(new LengthRule(6, 30), new WhitespaceRule()));
        return validator;
    }

    public static void validate(String senha) {
        Class<?>[] groups = { Default.class };
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

        Set<ConstraintViolation<InternalPasswordValidator>> constraintViolations = validatorFactory.getValidator()
                .validate(new InternalPasswordValidator(senha), groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    private static class InternalPasswordValidator {
        public InternalPasswordValidator(String password) {
            this.password = password;
        }

        @ValidPassword
        private final String password;
    }
}