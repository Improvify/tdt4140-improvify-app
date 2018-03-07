package tdt4140.gr1817.serviceprovider.webserver.validation;

/**
 * A general validation interface, specifying the validate method.
 * <p>
 * Implement this interface for each data resource.
 */
public interface Validator {

    /**
     * Validate if a JSON-string is correctly formated.
     *
     * @param json The string to be validated.
     * @return If the string was correctly formated.
     */
    boolean validate(String json);
}
