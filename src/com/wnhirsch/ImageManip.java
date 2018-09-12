package com.wnhirsch;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ImageManip {

    private final int SHADE_LIMIT  = 0xFF;

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
        for(int x = 0; x < img.getWidth(); x++)
            for(int y = 0; y < img.getHeight(); y++){
                // le uma cor da imagem
                Color c = new Color(img.getRGB(x, y));
                // calcula o valor da luminancia
                int lm = (int) (RED_LUM*c.getRed() + GREEN_LUM*c.getGreen() + BLUE_LUM*c.getBlue());
                lm = validateRGB(lm);
                // atribui essa cor para a nova imagem
                int newColor = lm + (lm << 8) + (lm << 16);
                newImage.setRGB(x, y, newColor);
            }

        return newImage;
    }

    public BufferedImage quantizeImage(BufferedImage img, int shades) {
        double range = ((double) SHADE_LIMIT+1) / shades;
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for(int x = 0; x < img.getWidth(); x++)
            for(int y = 0; y < img.getHeight(); y++){
                int color = (int) ((img.getRGB(x, y) >> 16) / range);
                newImage.setRGB(x, y, color + (color << 8) + (color << 16));
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
}
