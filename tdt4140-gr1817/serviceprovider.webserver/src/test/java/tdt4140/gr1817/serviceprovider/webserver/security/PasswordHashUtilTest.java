package tdt4140.gr1817.serviceprovider.webserver.security;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PasswordHashUtilTest {
    private PasswordHashUtil passwordHashUtil;

    @Before
    public void setUp() throws Exception {
        passwordHashUtil = new PasswordHashUtil();
    }

    @Test
    public void shouldHashPassword() {
        // Given
        String password = "123";

        // When
        String hash = passwordHashUtil.hashPassword(password);

        // Then
        assertThat(hash.contains(HashAlgorithmConfig.SHA_512_DEFAULT.getModularCryptFormatIdentifier()),
                is(true));
    }

    @Test
    public void shouldBeValidPassword() {
        // Given
        String password = "123";
        String hashedPassword = "$pbkdf2-sha512$10000$Ezgf0AjofuOCkUyxBCx+SFJz9O2lxnu52nG6v2fjoVA=$cYKAa0njRBQpkVazJp" +
                "nB2PcIaKQ+scvrL0uSlpHPwfZkITWX4WFG2I6Cn4Bkhe2yehtnVeWPatx4d1NDI4cgsksxmgaGd8+NitlnRY+MJ8crdNl4xI9OHf" +
                "NXvZgTSfHVR/yqON614tLRDoZJMIplMM93COOLMDsPs3CCbUkMQ+SW26akhpGXz0oZBHUTUrf4crppo6nit3M4BhoQOY8xHBISX0" +
                "w5xo8QyqLd98/M/NUEm9N8ijSHF5mc+IsQy2FUSlkC51R9t43e1Xe/9Viljgpp/pZ+09OkwbdMlw1fwurzu96I7Z97Ijf7g6FRdB" +
                "aWUUGuD1yyvTg3H//rtNC/pw==";

        // When
        boolean outcome = passwordHashUtil.validatePassword(password, hashedPassword);

        // Then
        assertThat(outcome, is(true));
    }

    @Test
    public void shouldNotBeValidPassword() {
        // Given
        String password = "567";


        // When
        boolean outcome = passwordHashUtil.validatePassword(password, null, null);

        // Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldNotBeValidDbEntry() {
        // Given
        String password = "567";
        String hashedPassword = "567";

        // When
        boolean outcome = passwordHashUtil.validatePassword(password, hashedPassword);

        // Then
        assertThat(outcome, is(false));
    }

    @Test
    public void shouldNotBeValidSaltAndHash() {
        // Given
        String password = "567";

        // When
        boolean outcome = passwordHashUtil.validatePassword(password, null, null);

        // Then
        assertThat(outcome, is(false));
    }
}