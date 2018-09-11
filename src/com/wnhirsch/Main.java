package com.wnhirsch;

import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args){

        AppWindow window = new AppWindow();

        ImageManip functions = new ImageManip();
        String dir = System.getProperty("user.dir") + "/test_images/";
        BufferedImage img = functions.readImage(dir + "Space_187k.jpg");
        functions.writeImage(img, dir + "teste.jpg");

    }

}
