package com.wnhirsch;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageManip {

    private final int SHADE_LIMIT  = 255;
//    private final int RGB_LIMIT    = 0xFFFFFF;

    private final double RED_LUM   = 0.299;
    private final double GREEN_LUM = 0.587;
    private final double BLUE_LUM  = 0.114;


    public BufferedImage readImage(String filePath){
        BufferedImage img = null;

        // Lendo a imagem
        try{
            img = ImageIO.read(new File(filePath));
        }
        catch(IOException e){
            System.out.println("READING IMAGE ERROR: " + e.toString());
            return null;
        }

        // Sucesso na leitura
        System.out.println("Image has been readed!");
        return img;
    }

    public boolean writeImage(BufferedImage img, String filePath){
        // Salvando a Imagem
        try{
            File outFile = new File(filePath);
            ImageIO.write(img, "jpg", outFile);
        }
        catch(IOException e){
            System.out.println("SAVING IMAGE ERROR: " + e.toString());
            return false;
        }

        // Success na escrita
        System.out.println("Image has been saved!");
        return true;
    }

    public BufferedImage vertMirror(BufferedImage img) {
        // Imagem de saída
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        // Criamos um vetor para salvar os pixels de uma linha
        int[] auxLine = new int[img.getWidth()];
        // para cada linha da imagem...
        for(int y = 0; y < img.getHeight(); y++){
            // lemos a linha atual
            auxLine = img.getRGB(0,y,img.getWidth(),1,auxLine,0,1);
            // e armazenamos no lado oposto da nova imagem
            newImage.setRGB(0,img.getHeight()-y-1,img.getWidth(),1,auxLine,0,1);
        }

        return newImage;
    }

    public BufferedImage horizMirror(BufferedImage img) {
        // Imagem de saída
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        // Criamos um vetor para salvar os pixels de uma coluna
        int[] auxLine = new int[img.getHeight()];
        // para cada coluna da imagem...
        for(int x = 0; x < img.getWidth(); x++){
            // lemos a coluna atual
            auxLine = img.getRGB(x,0,1,img.getHeight(),auxLine,0,1);
            // e armazenamos no lado oposto da nova imagem
            newImage.setRGB(img.getWidth()-x-1,0,1,img.getHeight(),auxLine,0,1);
        }

        return newImage;
    }

    public BufferedImage grayScale(BufferedImage img){
        // Imagem de saída
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        // para cada pixel da imagem...
        for(int i = 0; i < img.getHeight(); i++)
            for(int j = 0; j < img.getWidth(); j++){
                // le uma cor da imagem
                Color c = new Color(img.getRGB(j, i));
                // calcula o valor da luminancia
                int lm = (int) (RED_LUM*c.getRed() + GREEN_LUM*c.getGreen() + BLUE_LUM*c.getBlue());
                lm = validateRGB(lm);
                // atribui essa cor para a nova imagem
                int newColor = lm + Integer.rotateLeft(lm,Byte.SIZE) + Integer.rotateLeft(lm,Byte.SIZE*2);
                newImage.setRGB(j, i, newColor);
            }

        return newImage;
    }

    private int validateRGB(int cor){
        // Caso quebre o limite (0 <= cor <= 255) atribui o mais próximo
        if(cor > SHADE_LIMIT){
            return SHADE_LIMIT;
        }
        else if(cor < 0){
            return 0;
        }
        else{
            return cor;
        }
    }






//    public BufferedImage negative(BufferedImage img){
//        BufferedImage aux = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
//        for(int i = 0; i < img.getHeight(); i++)
//            for(int j = 0; j < img.getWidth(); j++){
//                aux.setRGB(j, i, RGB_LIMIT - img.getRGB(j, i));
//            }
//        return aux;
//    }
//
//    public BufferedImage histogramEqualization(BufferedImage img) {
//        Histogram hist = new Histogram(img);
//        Histogram histCum = new Histogram();
//        float alpha = ((float) SHADE_LIMIT) / (img.getWidth() * img.getHeight());
//
//        histCum.setFreq(0,(int) alpha*hist.getFreq(0));
//        for(int i = 1; i <= SHADE_LIMIT; i++){
//            histCum.setFreq(i, (int) ( histCum.getFreq(i-1) + alpha*hist.getFreq(i)) );
//        }
//
//        BufferedImage aux = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
//        for(int i = 0; i < img.getHeight(); i++)
//            for(int j = 0; j < img.getWidth(); j++){
//                int lm = histCum.getFreq(img.getRGB(j,i) & SHADE_LIMIT);
//                aux.setRGB(j, i,lm + Integer.rotateLeft(lm,Byte.SIZE) + Integer.rotateLeft(lm,Byte.SIZE*2));
//            }
//        return aux;
//    }


}
