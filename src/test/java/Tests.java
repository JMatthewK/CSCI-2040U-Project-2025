import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

public class Tests {

    private CatalogViewer loginSystem;

    @BeforeEach
    public void setUp() {
        try{
            loginSystem = new CatalogViewer();
        } catch(IOException ex){
        System.out.println (ex.toString());
        System.out.println("Could not find file ");
        }
        
        List<Account> testAccounts = List.of(
            new Account("user1", "password123", false),
            new Account("admin1", "adminpass", true)
        );
        loginSystem.setAccounts(testAccounts);
    }

    @Test
    public void testAuthenticate_ValidUser() {
        boolean result = loginSystem.authenticate("user1", "password123");
        assertTrue(result);
        assertEquals("user1", loginSystem.getCurrentUser());
        assertEquals(1, loginSystem.getUserStatus());
    }

    @Test
    public void testAuthenticate_ValidAdmin() {
        boolean result = loginSystem.authenticate("admin1", "adminpass");
        assertTrue(result);
        assertEquals("admin1", loginSystem.getCurrentUser());
        assertEquals(2, loginSystem.getUserStatus());
    }

    @Test
    public void testAuthenticate_InvalidCredentials() {
        boolean result = loginSystem.authenticate("user1", "wrongpass");
        assertFalse(result);
        assertFalse(loginSystem.isLoggedIn());
    }
}