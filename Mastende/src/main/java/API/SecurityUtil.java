package API;

import org.mindrot.jbcrypt.BCrypt;
import com.verifalia.api.VerifaliaRestClient;

public class SecurityUtil {
    public static String hashPassword(String plainTextPassword) {
        // Generate a salt and hash the password. The cost factor (e.g., 12)
        // determines the computational difficulty and should be adjusted based on hardware.
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        // Verify a plain-text password against a stored hashed password.
        // BCrypt extracts the salt from the hashed password for verification.
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    public String verifaliaApi( String username, String password){
        VerifaliaRestClient verifalia = new VerifaliaRestClient(username, password);

      return  null;
    };

    public static void main(String[] args) {
        String password = "mySecretPassword123";
        String hashedPassword = hashPassword(password);
        System.out.println("Hashed password: " + hashedPassword);

        boolean isMatch = checkPassword(password, hashedPassword);
        System.out.println("Password matches: " + isMatch);

        boolean isWrongPasswordMatch = checkPassword("mySecretPassword123", hashedPassword);
        System.out.println("Wrong password matches: " + isWrongPasswordMatch);
    }

}
