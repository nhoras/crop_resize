package mts.teta.resizer.imageprocessor;

import marvin.image.MarvinImage;
import mts.teta.resizer.ResizerApp;
import net.coobird.thumbnailator.Thumbnails;
import org.marvinproject.image.blur.gaussianBlur.GaussianBlur;
import org.marvinproject.image.segmentation.crop.Crop;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageProcessor {
    
    public void processImage(BufferedImage InputImage, ResizerApp resizerApp) throws Exception {

        BufferedImage bufferedImg = null;
        MarvinImage tmpMarvinImgIn;
        MarvinImage tmpMarvinImgOut;
        GaussianBlur gaussianBlur;
        Crop crop;
        float quality;
        int width;
        int height;
        String format;
        
        if (resizerApp.getBlurRadius() != null || resizerApp.cropParams != null) {
            tmpMarvinImgIn = new MarvinImage(InputImage);
            if (resizerApp.cropParams != null) {
                tmpMarvinImgOut = new MarvinImage(tmpMarvinImgIn.getWidth(), tmpMarvinImgIn.getHeight());
                crop = new Crop();
                crop.load();
                crop.setAttributes("width", resizerApp.getCropWidth(), "height", resizerApp.getCropHeight(),
                                   "x", resizerApp.getCropX(), "y", resizerApp.getCropY());
                crop.process(tmpMarvinImgIn, tmpMarvinImgOut);
                tmpMarvinImgIn = tmpMarvinImgOut;
            }
            if (resizerApp.getBlurRadius() != null) {
                tmpMarvinImgOut = new MarvinImage(tmpMarvinImgIn.getWidth(), tmpMarvinImgIn.getHeight());
                gaussianBlur = new GaussianBlur();
                gaussianBlur.load();
                gaussianBlur.setAttribute("radius", resizerApp.getBlurRadius());
                gaussianBlur.process(tmpMarvinImgIn, tmpMarvinImgOut);
                tmpMarvinImgIn = tmpMarvinImgOut;
            }
            bufferedImg = tmpMarvinImgIn.getBufferedImageNoAlpha();
        }

        if (bufferedImg == null)
            bufferedImg = InputImage;

        quality = convertQuality(resizerApp.getQuality());

        if (resizerApp.resizeParams == null) {
            width = InputImage.getWidth();
            height = InputImage.getHeight();
        }
        else {
            width = resizerApp.getResizeWidth();
            height = resizerApp.getResizeHeight();
        }

        format = checkFormat(resizerApp);

        Thumbnails.of(bufferedImg)
                .outputQuality(quality)
                .size(width, height)
//                .outputFormat(format)
                .toFile(resizerApp.getOutputFile());
    }

    private static float convertQuality(Integer quality) {
        if (quality == null)
            return 1f;
        return (float)quality / 100f;
    }

    private static String checkFormat(ResizerApp resizerApp) {

        String format;

        format = resizerApp.getOutputFormat();
        if (format == null) {
            format = getFormatFromFileName(resizerApp.getOutputFile());
        }
        if (format == null) {
            format = getFormatFromFileName(resizerApp.getInputFile());
        }

        return format;
    }

    private static String getFormatFromFileName(File file) {

        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
        return null;
    }
}
