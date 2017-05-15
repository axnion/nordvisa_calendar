package calendar.user;

import calendar.user.dto.PasswordRecoveryRequestDTO;
import calendar.user.dto.RecaptchaResponseDTO;
import calendar.user.dto.RecoverPasswordDTO;
import calendar.user.dto.RegistrationDTO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class UserController
 *
 * This class maps to /api/visitor, and contains all available routes for calling the API without
 * having to authenticate.
 *
 * With it you can do the following:
 * Register new account         -   POST    -   /api/visitor/registration
 * Request password recovery    -   POST    -   /api/visitor/request_password_recovery
 * Recover password             -   POST    -   /api/visitor/recover_password
 * Verify email address         -   GET     -   /api/visitor/verify_email
 *
 * @author Axel Nilsson (axnion)
 */
@RestController
@RequestMapping("/api/visitor")
public class VisitorController {
    private UserDAO dao;
    private Email email;
    private UserInformationValidator informationValidator;

    @Autowired
    public VisitorController(UserDAOMongo dao, Email email, UserInformationValidator validator) {
        this.dao = dao;
        this.email = email;
        this.informationValidator = validator;
    }

    /**
     * Runs on POST call to /api/visitor/registration. It registers a user account to the database.
     * If there is no other registered user the first person to register becomes a super
     * administrator.
     *
     * @param dto           Object containing data sent to server from client
     * @throws Exception    Invalid registration data, or database errors
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public RecaptchaResponseDTO registration(@ModelAttribute RegistrationDTO dto) throws Exception {
        informationValidator.validate(dto);

        RecaptchaResponseDTO response = informationValidator.validateRecaptcha(dto.getRecaptcha());

        ArrayList<User> existingUsers = dao.getAllUsers();
        User user = new User(dto);

        if(existingUsers.size() == 0) {
            user.setRole("SUPER_ADMIN");
        }

        user.setValidateEmailLink(new AuthenticationLink(generateRandomString(),
                DateTime.now().getMillis()));

        dao.add(user);

        email.sendVerificationEmail(user.getValidateEmailLink().getUrl());

        return response;
    }

    /**
     * Runs on POST call to /api/visitor/request_password_recovery. Takes an email address though
     * the PasswordRecoveryRequestDTO and tells dao to update the database with a password recovery
     * link, and then calls sendPasswordResetEmail on Email object.
     *
     * @param dto           Object containing data sent to server from client
     * @throws Exception    Invalid data, database errors
     */
    @RequestMapping(value = "/request_password_recovery", method = RequestMethod.POST)
    public void requestPasswordRecovery(@ModelAttribute PasswordRecoveryRequestDTO dto)
            throws Exception {
        User user = dao.getUserByEmail(dto.getEmail());
        user.setResetPasswordLink(new AuthenticationLink(generateRandomString(),
                DateTime.now().getMillis()));
        dao.update(user);

        email.sendPasswordResetEmail(user.getResetPasswordLink().getUrl());
    }

    /**
     * Runs on POST call to /api/visitor/request_password_recovery. Takes new password with
     * confirmation, and also the urlId to find the correct user to update. Calls recoverPassword on
     * DAO.
     *
     * @param dto           Object containing data sent to server from client
     * @throws Exception    Invalid data, database errors
     */
    @RequestMapping(value = "/recover_password", method = RequestMethod.POST)
    public void requestPasswordRecovery(@ModelAttribute RecoverPasswordDTO dto) throws Exception {
        informationValidator.validate(dto);

        User user = dao.getUserByPasswordRecoveryLink(dto.getUrlId());

        if (user == null) {
            throw new Exception("This link is invalid");
        }

        if(user.getResetPasswordLink().hasExpired()) {
            throw new Exception("This link has expired");
        }

        user.setResetPasswordLink(new AuthenticationLink("", 0));
        user.setPassword(dto.getPassword());

        dao.update(user);
    }

    /**
     * Runs on GET call to /api/visitor/verify_email.
     *
     * @param response      The object used to send the response
     * @param urlId         The query string param "id"
     * @return              A String, if something goes wrong the message is sent to the client.
     * @throws Exception    IOExceptions from HTTPServletResponse.
     */
    @RequestMapping(value = "/verify_email", method = RequestMethod.GET)
    public String verifyEmailAddress(HttpServletResponse response, @RequestParam("id") String urlId)
            throws Exception {
        try {
            User user = dao.getUserByEmailVerificationLink(urlId);

            if (user == null) {
                throw new Exception("This link is invalid");
            }

            if(user.getValidateEmailLink().hasExpired()) {
                throw new Exception("This link has expired");
            }

            user.setValidateEmailLink(new AuthenticationLink("", 0));
            dao.update(user);
        }
        catch (Exception expt) {
            return expt.getMessage();
        }
        response.sendRedirect("/");
        return "";
    }

    private String generateRandomString() {
        String characters = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVXYZ1234567890";
        int length = 20;
        Random rnd = new Random();

        char[] text = new char[length];
        for(int i = 0; i < length; i++) {
            text[i] = characters.charAt(rnd.nextInt(characters.length()));
        }

        return new String(text);
    }
}
