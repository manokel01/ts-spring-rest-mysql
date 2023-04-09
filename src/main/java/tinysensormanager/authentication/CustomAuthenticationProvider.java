package tinysensormanager.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import tinysensormanager.repo.DbUserRepo;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Locale;

/**
 *
 * This class implements the {@link AuthenticationProvider} interface to provide custom authentication
 * functionality for Spring Security. It authenticates users against a database and throws a {@link BadCredentialsException}
 * if the authentication fails.
 *
 * To use this class, simply autowire it into your project and call the authenticate() method passing in a
 * UsernamePasswordAuthenticationToken object containing the user's credentials.
 *
 * @author manokel01
 * @version 1.0
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private DbUserRepo dbUserRepo;

    /**
     * Message source used to retrieve localized error messages.
     */
    private final MessageSource messageSource;

    /**
     * Constructor to inject the {@link DbUserRepo} instance via Spring's dependency injection.
     * @param dbUserRepo The repository for interacting with User entities in the database.
     * @param messageSource The message source used to retrieve localized error messages.
     */
    @Autowired
    public CustomAuthenticationProvider(DbUserRepo dbUserRepo, MessageSource messageSource) {
        this.dbUserRepo = dbUserRepo;
        this.messageSource = messageSource;
    }

    /**
     * Message source accessor used to retrieve localized error messages.
     */
    private MessageSourceAccessor accessor;

    /**
     * Initializes the message source accessor.
     */
    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    /**
     * Authenticates the user using the specified authentication object.
     *
     * @param authentication the {@link Authentication} object containing the user's credentials.
     * @return a {@link UsernamePasswordAuthenticationToken} object representing the authenticated user.
     * @throws AuthenticationException if the user could not be authenticated.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (!dbUserRepo.isUserValid(username, password)) {
            throw new BadCredentialsException(accessor.getMessage("Bad Credentials"));
        }

        return new UsernamePasswordAuthenticationToken(username, password, Collections.<GrantedAuthority>emptyList());
    }

    /**
     * Determines whether the specified authentication object is supported by this authentication provider.
     *
     * @param authentication the authentication object to check.
     * @return {@code true} if the authentication object is supported, {@code false} otherwise.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

