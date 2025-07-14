import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePasswords {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("admin/password: " + encoder.encode("password"));
        System.out.println("user/user123: " + encoder.encode("user123"));
        System.out.println("others/password123: " + encoder.encode("password123"));
    }
}