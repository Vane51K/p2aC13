package edu.umg.dto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistroUsuario {

    // Método para obtener la conexión a la base de datos
    public static Connection obtenerConexion() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost/tu_basededatos?useSSL=false&serverTimezone=UTC";
        String usuario = "tu_usuario";
        String contraseña = "tu_contraseña";
        return DriverManager.getConnection(jdbcUrl, usuario, contraseña);
    }

    public static String encriptarContraseña(String contraseña) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytesContraseña = contraseña.getBytes(StandardCharsets.UTF_8);
            byte[] hash = md.digest(bytesContraseña);

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void registrarUsuario(String username, String contraseña) {
        String contraseñaEncriptada = encriptarContraseña(contraseña);

        String sql = "INSERT INTO tu_tabla (username, password) VALUES (?, ?)";

        try (Connection conn = obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, contraseñaEncriptada);
            pstmt.executeUpdate();
            System.out.println("Usuario registrado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String nuevoUsuario = "nuevoUsuario";
        String nuevaContraseña = "contraseñaEnClaro"; // Reemplaza por la contraseña real
        registrarUsuario(nuevoUsuario, nuevaContraseña);
    }
}
