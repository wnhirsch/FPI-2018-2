package com.wnhirsch;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AppWindow {

    private JFrame window;
    private JPanel principalForm;
    private JPanel imgInForm;
    private JPanel imgOutForm;
    private JPanel menuForm;

    public AppWindow() {
        window = new JFrame("T1_FPI");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        principalForm = new JPanel();

        imgInForm = new JPanel();
        imgOutForm = new JPanel();
        menuForm = new JPanel();

        config_imgInForm();
        config_imgOutForm();
        config_menuForm();

        window.add(principalForm);

        principalForm.add(imgInForm);
        principalForm.add(imgOutForm);
        principalForm.add(menuForm);

        window.pack();
        window.setSize(1000, 500);
        window.setVisible(true);
    }

    private void config_imgInForm() {

    }

    private void config_imgOutForm() {

    }

    private void config_menuForm() {
        JButton readImage = new JButton("Abrir Imagem");
        readImage.addActionListener(click_readImage);
        imgInForm.add(readImage);

        JButton saveImage = new JButton("Salvar Imagem");
        saveImage.addActionListener(click_saveImage);
        imgInForm.add(saveImage);
    }

    private ActionListener click_readImage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileManager = new JFileChooser();
            int choose = fileManager.showOpenDialog(null);


            if (choose == JFileChooser.APPROVE_OPTION) {
                File reader = fileManager.getSelectedFile();
            }
        }
    };
    private ActionListener click_saveImage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("APERzzzzzzzz");
        }
    };


}
