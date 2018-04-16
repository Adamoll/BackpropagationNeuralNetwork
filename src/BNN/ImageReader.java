package BNN;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {
    private static final int white = Color.white.getRGB();
    private static final int black = Color.black.getRGB();

    private String folderName;
    private File folder;
    private File[] listOfFiles;

    private int actualImageIndex;
    private int imageNumber;

    public ImageReader(String folderName) {
        this.folderName = folderName;
        this.folder = new File(folderName);
        this.listOfFiles = folder.listFiles();
        this.imageNumber = listOfFiles.length;
        NetworkUtils.shuffleArray(this.listOfFiles);
        actualImageIndex = 0;
    }

    private int[] getImageArray(String fileName) {
        int[] result = null;
        try {
            BufferedImage image = ImageIO.read(new File(fileName));
            result = new int[NetworkUtils.inputLayerSize];
            for(int i = 0; i < image.getHeight(); i++) {
                for(int j = 0; j < image.getWidth(); j++) {
                    int color = image.getRGB(j, i);
                    if(color == white) {
                        result[i * 7 + j] = 0;
                    } else if (color == black) {
                        result[i * 7 + j] = 1;
                    } else {
                        result[i * 7 + j] = 0;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public LearningPattern getNextPattern() {
        actualImageIndex ++;
        if(actualImageIndex < listOfFiles.length) {
            return new LearningPattern(getImageArray(listOfFiles[actualImageIndex - 1].getAbsolutePath()),
                    Integer.parseInt(listOfFiles[actualImageIndex - 1].getName().substring(0, 1)), listOfFiles[actualImageIndex - 1].getName());
        }
        else
            return null;
    }

    public void resetAndShuffleImages(){
        actualImageIndex = 0;
        NetworkUtils.shuffleArray(listOfFiles);
    }


    public int getImageNumber() {
        return imageNumber;
    }
}
