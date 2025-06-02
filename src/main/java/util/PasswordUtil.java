package util;

import org.mindrot.jbcrypt.BCrypt;



public class PasswordUtil {
    public static String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
    
    public static boolean checkPassword(String plainPassword, String hashedPassword){
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    
    public static void main(String[] args) {
        String clave1 = "1234";
        String clave2 = "1234";
        String claveHash = PasswordUtil.hashPassword("1234");
        
        String resultado1 = PasswordUtil.hashPassword(clave1);
        String resultado2 = PasswordUtil.hashPassword(clave2);
        System.out.println(resultado1);
        System.out.println(resultado2);
        
        System.out.println(claveHash);
        
        boolean b = checkPassword("1234", resultado2);
        System.out.println(b);
    }
}
