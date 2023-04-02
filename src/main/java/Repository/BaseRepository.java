/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author GSS
 */
public class BaseRepository {

    public BaseRepository() {
        this.connectSQL();
    }

    Connection connect;
    String url = "jdbc:mysql://localhost:3306/mvc";
    String user = "root";
    String passw = "";
    String instructionSQL;

    private void connectSQL() {
        try {
            connect = DriverManager.getConnection(url, user, passw);
            if (connect != null) {
                System.out.println("Conexión exitosa a la base de datos");
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }
    }

    public void store(String name, String idNumber, String position) {
        try {
            // Verificar si el registro ya existe
            String query = "SELECT COUNT(*) FROM users WHERE idNumber=?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, idNumber);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Actualizar el registro
                query = "UPDATE users SET name=?, position=? WHERE idNumber=?";
                ps = connect.prepareStatement(query);
                ps.setString(1, name);
                ps.setString(2, position);
                ps.setString(3, idNumber);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Registro actualizado exitosamente");
            } else {
                // Insertar un nuevo registro
                query = "INSERT INTO users (name, idNumber, position) VALUES (?, ?, ?)";
                ps = connect.prepareStatement(query);
                ps.setString(1, name);
                ps.setString(2, idNumber);
                ps.setString(3, position);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Dato registrado exitosamente");
            }
        } catch (Exception err) {
            System.out.println(err.getMessage());
            JOptionPane.showMessageDialog(null, err);
        }
    }

    public void delete() {

    }

    public ResultSet index() {

        instructionSQL = "SELECT * FROM users";
        ArrayList<String> datos = new ArrayList<String>();
        try {
            ResultSet results = connect.prepareStatement(instructionSQL).executeQuery();
            return results;
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }
        return null;

    }

    public ResultSet get(String idNumber) {
        instructionSQL = "SELECT * FROM users where idNumber = ? ";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(instructionSQL);
            preparedStatement.setString(1, idNumber);
            ResultSet results = preparedStatement.executeQuery();
            return results;
            
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }
        return null;
    }
}
