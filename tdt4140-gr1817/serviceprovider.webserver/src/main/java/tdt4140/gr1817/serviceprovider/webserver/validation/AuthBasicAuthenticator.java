package tdt4140.gr1817.serviceprovider.webserver.validation;

import tdt4140.gr1817.ecosystem.persistence.data.User;

import java.nio.charset.Charset;
import java.util.Base64;

public class AuthBasicAuthenticator {

    public boolean authenticate(String authString, User user) {
        if (authString != null) {
            // Split the username:password from Basic
            String[] authParts = authString.split("\\s+");
            String authInfo = authParts[1];

            // Decode the string from base64
            byte[] bytes = Base64.getDecoder().decode(authInfo);
            String decodedAuth = new String(bytes, Charset.forName("utf8"));

            String[] userNameAndPassword = decodedAuth.split(":");
            return userNameAndPassword[0].equals(user.getUsername())
                    && userNameAndPassword[1].equals(user.getPassword());
        }
        return false;
    }
}
