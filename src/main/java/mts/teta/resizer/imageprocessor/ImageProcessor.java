package mts.teta.resizer.imageprocessor;

import marvin.image.MarvinImage;
import mts.teta.resizer.ResizerApp;
import net.coobird.thumbnailator.Thumbnails;
import org.marvinproject.image.blur.gaussianBlur.GaussianBlur;
import org.marvinproject.image.segmentation.crop.Crop;

import java.awt.image.BufferedImage;
import java.io.File;

public class ImageProcessor {

    public void processImage(BufferedImage inputImage, ResizerApp resizerApp) throws Exception {

        BufferedImage bufferedImg = null;
        MarvinImage tmpMarvinImgIn;
        MarvinImage tmpMarvinImgOut;
        GaussianBlur gaussianBlur;
        Crop crop;
        int width;
        int height;
        String format;

        format = checkFormat(resizerApp);
        checkBlur(resizerApp.getBlurRadius());
        checkCropParam(inputImage, resizerApp);

        if (resizerApp.getBlurRadius() != null || resizerApp.cropParams != null) {
            tmpMarvinImgIn = new MarvinImage(inputImage);
            if (resizerApp.cropParams != null) {
                tmpMarvinImgOut = new MarvinImage(resizerApp.getCropWidth(), resizerApp.getCropHeight());
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
            bufferedImg = inputImage;

        if (resizerApp.resizeParams == null) {
            width = bufferedImg.getWidth();
            height = bufferedImg.getHeight();
        }
        else {
            width = resizerApp.getResizeWidth();
            height = resizerApp.getResizeHeight();
        }

        if (resizerApp.getQuality() == null) {
            Thumbnails.of(bufferedImg)
                    .forceSize(width, height)
                    .outputFormat(format)
                    .toFile(resizerApp.getOutputFile());
        }
        else {
            Thumbnails.of(bufferedImg)
                    .outputQuality(convertQuality(resizerApp.getQuality()))
                    .forceSize(width, height)
                    .outputFormat(format)
                    .toFile(resizerApp.getOutputFile());
        }
    }

    private static float convertQuality(Integer quality) throws BadAttributesException {
        if (quality != null && (quality < 1 || quality > 100)) {
            throw new BadAttributesException("Please check params!");
        }
        return (float)quality / 100f;
    }

    private static void checkBlur(Integer blur) throws BadAttributesException {
        if (blur != null && blur < 1) {
            throw new BadAttributesException("Blur radius can't be less than 1");
        }
    }

    private static void checkCropParam(BufferedImage inputImage, ResizerApp resizerApp)
            throws BadAttributesException {
        if (resizerApp.cropParams == null)
            return;
        if ((resizerApp.getCropX() < 0 || resizerApp.getCropY() < 0
                || (inputImage.getWidth() - resizerApp.getCropWidth() - resizerApp.getCropX()) < 0)
                || (inputImage.getHeight() - resizerApp.getCropHeight() - resizerApp.getCropY() < 0)) {
            throw new BadAttributesException("Crop params is out of input image size");
        }
    }

    private static String checkFormat(ResizerApp resizerApp) throws BadAttributesException {

        String format;

        format = resizerApp.getOutputFormat();
        if (format != null && !format.equalsIgnoreCase("jpeg")
                && !format.equalsIgnoreCase("jpg")
                && !format.equalsIgnoreCase("png")) {
            throw new BadAttributesException("Available formats: jpeg png");
        }
        if (format == null) {
            format = getFormatFromFileName(resizerApp.getOutputFile());
        }
        if (format == null || (!format.equalsIgnoreCase("jpeg")
                && !format.equalsIgnoreCase("jpg")
                && !format.equalsIgnoreCase("png"))) {
            format = getFormatFromFileName(resizerApp.getInputFile());
        }
        if (format == null || (!format.equalsIgnoreCase("jpeg")
                && !format.equalsIgnoreCase("jpg")
                && !format.equalsIgnoreCase("png"))) {
            throw new BadAttributesException("Available formats: jpeg png");
        }
        return format.toLowerCase();
    }

    private static String getFormatFromFileName(File file) {

        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        return null;
    }
}
