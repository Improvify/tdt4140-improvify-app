package tdt4140.gr1817.serviceprovider.webserver.security;

import tdt4140.gr1817.ecosystem.persistence.data.User;

import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.Base64;

public class AuthBasicAuthenticator {

    private final PasswordHashUtil passwordHashUtil;

    @Inject
    public AuthBasicAuthenticator(PasswordHashUtil passwordHashUtil) {
        this.passwordHashUtil = passwordHashUtil;
    }

    public boolean authenticate(String authString, User user) {
        if (authString != null) {
            // Split the username:password from Basic
            String[] authParts = authString.split("\\s+");
            String authInfo = authParts[1];

            // Decode the string from base64
            byte[] bytes = Base64.getDecoder().decode(authInfo);
            String decodedAuth = new String(bytes, Charset.forName("utf8"));

            String[] userNameAndPassword = decodedAuth.split(":");

            if (userNameAndPassword[0].equals(user.getUsername())) {
                return passwordHashUtil.validatePassword(userNameAndPassword[1], user.getPassword());
            }
        }
        return false;
    }
}
