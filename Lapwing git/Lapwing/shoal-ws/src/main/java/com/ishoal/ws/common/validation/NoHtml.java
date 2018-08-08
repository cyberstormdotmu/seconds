package com.ishoal.ws.common.validation;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.util.HtmlUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URLDecoder;
import java.text.Normalizer;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoHtml.Validator.class)
@Documented
public @interface NoHtml {
    String message() default "The field is not allowed to contain HTML content";

    Class<?>[] groups() default {

    };

    Class<? extends Payload>[] payload() default {

    };

    public static class Validator implements ConstraintValidator<NoHtml, String> {

        @Override
        public void initialize(NoHtml noHtml) {
            // No-op
        }

        /**
         * Protection against HTML/XSS injection - run input string through transformations
         * to normalize, URL decode, remove HTML escapes and then finally to clean the string
         * of all HTML content. If the resulting string differs from the input string then
         * it's a sign that the input string contained a (possibly obfuscated) attempt to
         * insert an XSS exploit.
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if(StringUtils.isNotBlank(value)) {
                try {
                    String normalized = Normalizer.normalize(value, Normalizer.Form.NFKC);
                    String urldecoded = URLDecoder.decode(normalized, "UTF-8");
                    String unescaped = HtmlUtils.htmlUnescape(urldecoded);
                    String sanitized = Jsoup.clean(unescaped, Whitelist.none());

                    return unescaped.equals(sanitized);
                } catch (UnsupportedEncodingException e) {
                    throw new ValidationException(e);
                }
            }
            return true;
        }
    }
}