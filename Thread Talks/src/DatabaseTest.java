import dao.DatabaseConfig;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        
        if (DatabaseConfig.testConnection()) {
            System.out.println("✓ Database connection successful!");
        } else {
            System.out.println("✗ Database connection failed!");
            System.out.println("Please check:");
            System.out.println("1. Is MySQL running?");
            System.out.println("2. Is the threadtalk database created?");
            System.out.println("3. Are username and password correct?");
        }
    }
} 