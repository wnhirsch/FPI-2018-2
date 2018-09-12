package com.wnhirsch;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

class AppWindow {

    private JFrame menuWindow;
    private JFrame imageInWindow;
    private JFrame imageOutWindow;
    private JPanel menuPanel;
    private JPanel imageInPanel;
    private JPanel imageOutPanel;
    private JLabel imageInLabel;
    private JLabel imageOutLabel;
    private JSlider shadeNumberLabel;

    private BufferedImage imageIn;
    private BufferedImage imageOut;
    private ImageManip imageFunctions;

    public AppWindow() {
        imageFunctions = new ImageManip();

        menuWindow = new JFrame("FPI 2018/2");
        imageInWindow = new JFrame("Input Image");
        imageOutWindow = new JFrame("Output Image");

        menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuPanel = new JPanel();
        imageInPanel = new JPanel();
        imageOutPanel = new JPanel();

        config_menuPanel();
        config_imageInPanel();
        config_imageOutPanel();

        menuWindow.add(menuPanel);
        imageInWindow.add(imageInPanel);
        imageOutWindow.add(imageOutPanel);

        menuWindow.pack();
        menuWindow.setSize(300, 300);
        menuWindow.setLocationRelativeTo(null);
        imageInWindow.pack();
        imageInWindow.setSize(500, 500);
        imageInWindow.setLocationRelativeTo(null);
        imageOutWindow.pack();
        imageOutWindow.setSize(500, 500);
        imageOutWindow.setLocationRelativeTo(null);

        menuWindow.setVisible(true);
    }

    private void config_imageInPanel() {
        imageInLabel = new JLabel();
        imageInLabel.setVisible(true);
        imageInPanel.add(imageInLabel);
    }

    private void config_imageOutPanel() {
        imageOutLabel = new JLabel();
        imageOutLabel.setVisible(true);
        imageOutPanel.add(imageOutLabel);
    }

    private void config_menuPanel() {
//        String myInfo = "Trabalho realizado pelo aluno:<br>Wellington Nascente Hirsch<br>00287715<br>Turma A";
//        JLabel label_info = new JLabel("<html>" + myInfo + "</p></html>");
//        label_info.setLayout(new BoxLayout(label_info, BoxLayout.PAGE_AXIS));
//        menuPanel.add(label_info);

        JButton btn_readImage = new JButton("Open Image");
        btn_readImage.addActionListener(click_readImage);
        menuPanel.add(btn_readImage);

        JButton btn_saveImage = new JButton("Save Image");
        btn_saveImage.addActionListener(click_saveImage);
        menuPanel.add(btn_saveImage);

        JButton btn_grayScale = new JButton("Apply Grayscale");
        btn_grayScale.addActionListener(click_grayScale);
        menuPanel.add(btn_grayScale);

        JButton btn_vertMirror = new JButton("Apply Vertical Mirroring");
        btn_vertMirror.addActionListener(click_vertMirror);
        menuPanel.add(btn_vertMirror);

        JButton btn_horizMirror = new JButton("Apply Horizontal Mirroring");
        btn_horizMirror.addActionListener(click_horizMirror);
        menuPanel.add(btn_horizMirror);

        shadeNumberLabel = new JSlider(JSlider.HORIZONTAL, 1, 256, 1);
        shadeNumberLabel.setPaintLabels(true);
        shadeNumberLabel.setPaintTicks(true);
        shadeNumberLabel.setMinorTickSpacing(25);
        shadeNumberLabel.setMajorTickSpacing(50);
        menuPanel.add(shadeNumberLabel);

        JButton btn_quantImage = new JButton("Apply Quantize");
        btn_quantImage.addActionListener(click_quantImage);
        menuPanel.add(btn_quantImage);
    }

    private ActionListener click_readImage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileManager = new JFileChooser(System.getProperty("user.dir"));
            fileManager.setDialogTitle("Select an image");
            fileManager.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("JPEG images", "jpg");
            fileManager.addChoosableFileFilter(fileFilter);

            int choose = fileManager.showOpenDialog(null);

            if (choose == JFileChooser.APPROVE_OPTION) {
                File imageFile = fileManager.getSelectedFile();
                imageIn = imageFunctions.readImage(imageFile.getPath());
                imageOut = imageIn;
                imageInLabel.setIcon(new ImageIcon(imageIn));
                imageOutLabel.setIcon(new ImageIcon(imageOut));
                imageInWindow.setVisible(true);
                imageOutWindow.setVisible(true);
            }
        }
    };
    private ActionListener click_saveImage;

    {
        click_saveImage = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageOutWindow.setVisible(true);
                if (imageOut != null) {
                    JFileChooser fileManager = new JFileChooser(System.getProperty("user.dir"));
                    fileManager.setDialogTitle("Select a directory");
                    fileManager.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    int choose = fileManager.showOpenDialog(null);

                    if (choose == JFileChooser.APPROVE_OPTION) {
                        if (fileManager.getSelectedFile().isDirectory()) {
                            String filename = JOptionPane.showInputDialog("Type the image filename (.jpg):");
                            if (filename != null) {
                                if (filename.lastIndexOf(".jpg") != filename.length() - 4 && filename.lastIndexOf(".JPG") != filename.length() - 4) {
                                    filename += ".jpg";
                                }
                                File saveDirectory = fileManager.getSelectedFile();
                                imageFunctions.writeImage(imageOut, saveDirectory.getPath() + "\\" + filename);
                            }
                        }
                    }
                }
            }
        };
    }

    private ActionListener click_grayScale = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(imageOut != null){
                imageOut = imageFunctions.grayScale(imageOut);
                imageOutLabel.setIcon(new ImageIcon(imageOut));
                imageOutWindow.setVisible(true);
            }
        }
    };
    private ActionListener click_vertMirror = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(imageOut != null){
                imageOut = imageFunctions.vertMirror(imageOut);
                imageOutLabel.setIcon(new ImageIcon(imageOut));
                imageOutWindow.setVisible(true);
            }
        }
    };
    private ActionListener click_horizMirror = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(imageOut != null){
                imageOut = imageFunctions.horizMirror(imageOut);
                imageOutLabel.setIcon(new ImageIcon(imageOut));
                imageOutWindow.setVisible(true);
            }
        }
    };
    private ActionListener click_quantImage = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(imageOut != null){
                if(imageOut.getType() == BufferedImage.TYPE_BYTE_GRAY){
                    imageOut = imageFunctions.quantizeImage(imageOut, shadeNumberLabel.getValue());
                    imageOutLabel.setIcon(new ImageIcon(imageOut));
                    imageOutWindow.setVisible(true);
                }
            }
        }
    };

}
