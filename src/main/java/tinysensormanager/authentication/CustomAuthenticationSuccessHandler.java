package tinysensormanager.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is responsible for handling successful authentication requests.
 * It sets the target URL to redirect the user after successful authentication.
 *
 * @author manokel01
 * @version 1.0
 */
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * The name of the session attribute that stores the URL to redirect the user to after successful authentication.
     */
    public static final String REDIRECT_URL_SESSION_ATTRIBUTE_NAME = "REDIRECT_URL";

    /**
     * Sets the target URL to redirect the user to after successful authentication.
     * If there is no stored URL, the default target URL is used.
     *
     * @param request        the HTTP request
     * @param response       the HTTP response
     * @param authentication the authentication object
     * @throws ServletException if a servlet exception occurs
     * @throws IOException      if an I/O exception occurs
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {

        // get the redirect URL from the session attribute
        Object redirectUrlObject = request.getSession().getAttribute(REDIRECT_URL_SESSION_ATTRIBUTE_NAME);

        if (redirectUrlObject != null) {
            // if there is a redirect URL, set it as the default target URL
            setDefaultTargetUrl(redirectUrlObject.toString());
        } else {
            // if there is no redirect URL, use the default target URL
            setDefaultTargetUrl("/api/users?lastname=");
        }

        // remove the redirect URL from the session
        request.getSession().removeAttribute(REDIRECT_URL_SESSION_ATTRIBUTE_NAME);

        // call the parent class method to handle the successful authentication
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
