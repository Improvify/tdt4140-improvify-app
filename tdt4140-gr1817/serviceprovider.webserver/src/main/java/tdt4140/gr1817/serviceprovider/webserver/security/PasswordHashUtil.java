package tdt4140.gr1817.serviceprovider.webserver.security;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class PasswordHashUtil {

    private final HashAlgorithmConfig config;


    /**
     * Constructs a new PasswordHashUtil to be used in hashing passwords safely.
     * Uses the default hash config, SHA_512.
     *
     * @see HashAlgorithmConfig
     */
    @Inject
    public PasswordHashUtil() {
        this(HashAlgorithmConfig.SHA_512_DEFAULT);
    }


    /**
     * Constructs a new PasswordHashUtil to be used in hashing passwords safely.
     *
     * @param config the configuration for the hash algorithm to be used.
     * @see HashAlgorithmConfig
     */
    public PasswordHashUtil(HashAlgorithmConfig config) {
        this.config = config;
    }


    /**
     * Hashes the password according to the {{@link #config}} with a randomly generated salt.
     *
     * @param password the password to be hashed.
     * @return the hashed password.
     */
    public String hashPassword(final String password) {
        byte[] salt = generateSalt();
        byte[] hash = hashPassword(password, salt);
        return stringifyHash(hash, salt);
    }

    /**
     * Hashes the password according to the {@link #config} with the given salt.
     *
     * @param password the password to be hashed.
     * @param salt     the salt to use in hashing.
     * @return the hashed password.
     */
    public byte[] hashPassword(final String password, final byte[] salt) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(config.getAlgorithm());
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt,
                    config.getIterations(), config.getKeyLength());
            SecretKey key = skf.generateSecret(spec);
            return key.getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates the password by hashing it and comparing it against the given entry in the database.
     *
     * @param password the password to be validated.
     * @param dbEntry  the hashed password, salt and algorithm info from the database.
     * @return if the password matched the entry in the database.
     */
    public boolean validatePassword(final String password, final String dbEntry) {
        try {
            String[] dbPasswordStrings = dbEntry.split("\\$");
            byte[] salt = dbPasswordStrings[3].getBytes();
            byte[] hash = dbPasswordStrings[4].getBytes();
            return validatePassword(password, hash, salt);

        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Validates the password by hashing it and comparing it against the given entry in the database.
     *
     * @param password the password to be validated.
     * @param hash     the hashed password from the database.
     * @param salt     the salt used to hash the password from the database.
     * @return if the password matched the entry in the database.
     */
    public boolean validatePassword(final String password, final byte[] hash, final byte[] salt) {
        try {
            final byte[] hashedPassword = hashPassword(password, salt);
            return Arrays.equals(hashedPassword, hash);

        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Encodes the hash, salt, iterations and hash algorithm identifier to create an unambiguous entry to be used in
     * saving to the database.
     *
     * @param hash the hash to be stringified.
     * @param salt the salt used for creating the hash.
     * @return the
     */
    private String stringifyHash(byte[] hash, byte[] salt) {
        final Base64.Encoder encoder = Base64.getEncoder();
        String hashString = encoder.encodeToString(hash);
        String saltString = encoder.encodeToString(salt);
        return String.format("$%s$%s$%s$%s", config.getModularCryptFormatIdentifier(), config.getIterations(),
                saltString, hashString);
    }

    /**
     * Generates a new random 32 byte salt using {@link SecureRandom}.
     *
     * @return the generated salt.
     * @see SecureRandom
     */
    private byte[] generateSalt() {
        final Random random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        return salt;
    }
}
