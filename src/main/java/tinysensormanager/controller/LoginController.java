package tinysensormanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * The LoginController class handles requests related to user authentication and login.
 *
 * @author manokel01
 * @version 1.0.0
 */
@Controller
public class LoginController {

    /**
     * This method handles GET requests for the /login endpoint.
     *
     * @param model     the Model object to be populated with attributes
     * @param principal the Principal object representing the authenticated user
     * @param request   the HttpServletRequest object representing the user request
     * @return the name of the view to be rendered
     * @throws Exception if an exception occurs during processing
     */
    @GetMapping("/login")
    String login(Model model, Principal principal, HttpServletRequest request) throws Exception {
        String referer = request.getHeader("Referer");
        request.getSession().setAttribute("CustomAuthenticationSuccessHandler.REDIRECT_URL_SESSION_ATTRIBUTE_NAME", referer);
        return principal == null ? "login" : "redirect:/api/users?lastname=";
    }

    /**
     * This method handles GET requests for the / endpoint.
     *
     * @param model     the Model object to be populated with attributes
     * @param principal the Principal object representing the authenticated user
     * @param request   the HttpServletRequest object representing the user request
     * @return the name of the view to be rendered
     * @throws Exception if an exception occurs during processing
     */
    @GetMapping("/")
    String root(Model model, Principal principal, HttpServletRequest request) throws Exception {
        return principal == null ? "login" : "redirect:/api/users?lastname=";
    }
}

