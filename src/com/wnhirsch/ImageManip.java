package com.wnhirsch;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ImageManip {

    private final int SHADE_LIMIT  = 0xFF;

    private final double RED_LUM   = 0.299;
    private final double GREEN_LUM = 0.587;
    private final double BLUE_LUM  = 0.114;
    
    // Convolution Kernels
    public final double[][] gaussiano = new double[][] {{0.0625, 0.125, 0.0625},
                                                        {0.125,  0.25,  0.125 },
                                                        {0.0625, 0.125, 0.0625}};
    
    public final double[][] laplaciano = new double[][] {{0, -1,  0},
                                                        {-1, +4, -1},
                                                        { 0, -1,  0}};
    
    public final double[][] genPH = new double[][]      {{-1, -1, -1},
                                                        { -1, +8, -1},
                                                        { -1, -1, -1}};
    
    public final double[][] preHX = new double[][]      {{-1, 0, 1},
                                                        { -1, 0, 1},
                                                        { -1, 0, 1}};
    
    public final double[][] preHYHX = new double[][]    {{-1, -1, -1},
                                                        {  0,  0,  0},
                                                        {  1,  1,  1}};
    
    public final double[][] sobelHX = new double[][]    {{-1, 0, 1},
                                                        { -2, 0, 2},
                                                        { -1, 0, 1}};
    
    public final double[][] sobelHY = new double[][]    {{-1, -2, -1},
                                                        {  0,  0,  0},
                                                        {  1,  2,  1}};



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
                Pixel p = new Pixel(img.getRGB(x, y), true);
                // calcula o valor da luminancia
                int lm = (int) (RED_LUM*p.red + GREEN_LUM*p.green + BLUE_LUM*p.blue);
                p = new Pixel(lm, false);
                p.validateRGB();
                // atribui essa cor para a nova imagem
                newImage.setRGB(x, y, p.getRGB());
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
    
    public BufferedImage brightnessInc(BufferedImage img, int intensity) {
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        // Para cada pixel da imagem
        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                // Le uma cor e soma a intensidade do brilho
                Pixel p = new Pixel(img.getRGB(x, y), true);
                p.red += intensity;
                p.blue += intensity;
                p.green += intensity;
                // Valida o valor final e salva na imagem
                p.validateRGB();
                newImage.setRGB(x, y, p.getRGB());
            }
        }
        return newImage;
    }

    public BufferedImage contrastInc(BufferedImage img, double intensity) {
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        // Para cada pixel da imagem
        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                // Le uma cor e multiplica a intensidade do contraste
                Pixel p = new Pixel(img.getRGB(x, y), true);
                p.red *= intensity;
                p.blue *= intensity;
                p.green *= intensity;
                // Valida o valor final e salva na imagem
                p.validateRGB();
                newImage.setRGB(x, y, p.getRGB());
            }
        }
        return newImage;
    }
        
    public BufferedImage negativeImg(BufferedImage img) {
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        // Para cada pixel da imagem
        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                // Le uma cor e inverte seu valor
                Pixel p = new Pixel(img.getRGB(x, y), true);
                p.red = 255 - p.red;
                p.blue = 255 - p.blue;
                p.green = 255 - p.green;
                // Salva na imagem
                newImage.setRGB(x, y, p.getRGB());
            }
        }
        return newImage;
    }
    
    public BufferedImage reduceImg(BufferedImage img, int sx, int sy) {
        // Calcula o novo tamanho da imagem reduzida
        int newWidth = Math.floorDiv(img.getWidth(), sx);
        int newHeight = Math.floorDiv(img.getHeight(), sy);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, img.getType());
        
        // Para cada pixel na nova imagem
        for(int x = 0; x < newWidth; x++){
            for(int y = 0; y < newHeight; y++){
                Pixel newColor = new Pixel(0, true);
                // Calcula a média de cores de um retangulo sx por sy dentro da imagem antiga
                for(int xold = x*sx; xold < (x+1)*sx && xold < img.getWidth(); xold++){
                    for(int yold = y*sy; yold < (y+1)*sy && yold < img.getHeight(); yold++){
                        newColor.sumColors(img.getRGB(xold, yold));
                    }
                }
                // Armazena na nova imagem
                newColor.divideColor(sx*sy);
                newImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return newImage;
    }
    
    public BufferedImage enlargeImg(BufferedImage img) {
        int sx = 2, sy = 2; // pré-definido
        
        // Calcula o novo tamanho da imagem ampliada
        int newWidth = img.getWidth() * sx;
        int newHeight = img.getHeight() * sy;
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, img.getType());
        
        // Percorre as linhas pares que estão parcialmente completas
        for(int y = 0; y < newHeight; y+=2){
            // E para cada pixel
            for(int x = 0; x < newWidth; x++){
                // Se for par simplesmente insere
                if(x % 2 == 0) {
                    newImage.setRGB(x, y, img.getRGB(x/2, y/2));
                }
                // Se for impar realiza a média dos dois pixels vizinhos
                else if((x+1)/2 < img.getWidth()) {
                    Pixel prevColor = new Pixel(img.getRGB((x-1)/2, y/2), true);
                    prevColor.sumColors(img.getRGB((x+1)/2, y/2));
                    prevColor.divideColor(2);
                    newImage.setRGB(x, y, prevColor.getRGB());
                }
                else {
                    newImage.setRGB(x, y, img.getRGB((x-1)/2, y/2));
                }
            }
        }
            
        // Com isso a imagem tera as linhas pares preenchidas e as impares não
        // Agora para cada coluna de pixels
        for(int x = 0; x < newWidth; x++){
            // de cada linha impar
            for(int y = 1; y < newHeight; y+=2){
                // Calcular a média das linhas pares vizinhas
                if((y+1)/2 < img.getHeight()) {
                    Pixel prevColor = new Pixel(img.getRGB(x/2, (y-1)/2), true);
                    prevColor.sumColors(img.getRGB(x/2, (y+1)/2));
                    prevColor.divideColor(2);
                    newImage.setRGB(x, y, prevColor.getRGB());
                }
                else {
                    newImage.setRGB(x, y, img.getRGB(x/2, (y-1)/2));
                }
            }
        }
        
        return newImage;
    }
    
    public BufferedImage clockwiseImg(BufferedImage img) {
        BufferedImage newImage = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());
        // Para cada pixel da imagem
        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                // Salva ele na posição 90 graus no sentido horário da original
                newImage.setRGB(newImage.getWidth()-y-1, x, img.getRGB(x,y));
            }
        }
        return newImage;
    }
    
    public BufferedImage anticlockwiseImg(BufferedImage img) {
        BufferedImage newImage = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());
        // Para cada pixel da imagem
        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                // Salva ele na posição 90 graus no sentido anti-horário da original
                newImage.setRGB(y, newImage.getHeight()-x-1, img.getRGB(x,y));
            }
        }
        return newImage;
    }
    
    public BufferedImage convolutionImg(BufferedImage img, double[][] kernel, boolean shouldSum) {
        // Rotaciona a matriz do kernel em 180 graus
        double[][] kernelRotated = new double[3][3];
        for(int xk = 0; xk < 3; xk++){
            for(int yk = 0; yk < 3; yk++){
                kernelRotated[xk][yk] = kernel[3-xk-1][3-yk-1];
            }
        }
            
        BufferedImage newImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        // Para cada pixdel da imagem (ignorando as bordas)
        for(int x = 1; x < img.getWidth()-1; x++){
            for(int y = 1; y < img.getHeight()-1; y++){
                int newColor = 0;
                // Multiplica o kernel pelo pixel atual e seus vizinhos
                for(int xk = -1; xk <= 1; xk++){
                    for(int yk = -1; yk <= 1; yk++){
                        newColor += kernelRotated[xk+1][yk+1] * (img.getRGB(x+xk, y+yk) & 0xFF);
                    }
                }
                // Ajusta a soma
                Pixel p = new Pixel(newColor, false);
                if(shouldSum){
                    p.red += 127;
                    p.green += 127;
                    p.blue += 127;
                }
                p.validateRGB();
                // Armazena na imagem
                newImage.setRGB(x, y, p.getRGB());
            }
        }
        
        return newImage;
    }
}
