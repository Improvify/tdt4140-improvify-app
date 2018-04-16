package tdt4140.gr1817.serviceprovider.webserver.security;

/**
 * Enum of all hash configurations the web server supports.
 */
public enum HashAlgorithmConfig {

    SHA_512_DEFAULT("PBKDF2WithHmacSHA512", "pbkdf2-sha512", 2048, 10000);

    private final String algorithm;
    private final String modularCryptFormatIdentifier;
    private final int keyLength;
    private final int iterations;

    /**
     * Create a new hash algorithm config.
     *
     * @param algorithm                    name of the hash algorithm that will be used. Must correspond to a algorithm
     *                                     in {@link javax.crypto.SecretKeyFactory}.
     * @param modularCryptFormatIdentifier the identifier for the given hash algorithm.
     * @param keyLength                    the length of the outputted key specified in BITS. Often a power of 2.
     * @param iterations                   the amount of iterations the hash will run.
     * @see javax.crypto.SecretKeyFactory
     * @see <a href="https://passlib.readthedocs.io/en/stable/modular_crypt_format.html#modular-crypt-format">
     * Modular Crypt Format</a>
     */
    HashAlgorithmConfig(String algorithm, String modularCryptFormatIdentifier, int keyLength, int iterations) {
        this.algorithm = algorithm;
        this.modularCryptFormatIdentifier = modularCryptFormatIdentifier;
        this.keyLength = keyLength;
        this.iterations = iterations;
    }

    public int getIterations() {
        return iterations;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getModularCryptFormatIdentifier() {
        return modularCryptFormatIdentifier;
    }
}
