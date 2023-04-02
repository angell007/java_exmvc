package Controller;

import Model.Employee;
import View.Index;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author GSS
 */
public class Controller {

    private Index obj_view;
    private Employee obj_model;

    public Controller() {
        this.obj_view = new Index();
        this.obj_model = new Employee();

        this.start_view();
        this.obj_view.btnSave.addActionListener(operate);
        this.obj_view.table.addMouseListener(edit);
    }

    private void start_view() {
        this.obj_view.setVisible(true);
        this.obj_view.setLocationRelativeTo(null);
        this.index();
    }

    private void index() {

        DefaultTableModel model = (DefaultTableModel) obj_view.table.getModel();
        model.setRowCount(0);
        ResultSet results = this.obj_model.index();

        try {
            while (results.next()) {
                String name = results.getString("name");
                String idNumber = results.getString("idNumber");
                String position = results.getString("position");
                model.addRow(new Object[]{name, idNumber, position});
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }

    }

    MouseAdapter edit = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            int row = obj_view.table.getSelectedRow();
            int col = obj_view.table.getSelectedColumn();
            if (row >= 0 && col >= 0) {
                Object value = obj_view.table.getValueAt(row, col);

                if (col == 1) {
                    System.out.println(value.toString());
                    obj_model.setIdNumber(value.toString());
                    obj_view.idNumber.enable(false);

                    ResultSet results = obj_model.get(obj_model.getIdNumber());

                    try {
                        while (results.next()) {
                            obj_view.name.setText(results.getString("name"));
                            obj_view.idNumber.setText(results.getString("idNumber"));
                            obj_view.position.setText(results.getString("position"));
                        }
                    } catch (Exception err) {
                        JOptionPane.showMessageDialog(null, err);
                    }

                }
            }
        }
    };

    ActionListener operate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            obj_view.loading.setText("Loading...");

            obj_model.setName(obj_view.name.getText());
            obj_model.setIdNumber(obj_view.idNumber.getText());
            obj_model.setPosition(obj_view.position.getText());

            try {
                obj_model.store(obj_model.getName(), obj_model.getIdNumber(), obj_model.getPosition());
                obj_view.loading.setText("");
                obj_view.idNumber.enable(true);
                obj_view.idNumber.setText("");
                obj_view.name.setText("");
                obj_view.position.setText("");
                index();
            } catch (Exception ex) {
                System.err.println(ex);
            }

        }
    };
}
