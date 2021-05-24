package mts.teta.resizer;

import picocli.CommandLine;

import java.io.File;
import java.util.Stack;

public class ConsoleAttributes {

    @CommandLine.Option(names = {"-h", "--help"},
            usageHelp = true,
            hidden = true)
    private boolean help;

    @CommandLine.Option(names = {"-V", "-v", "--version"},
            versionHelp = true,
            hidden = true)
    private boolean versionRequested;

    @CommandLine.Parameters(index = "0",
            hidden = true)
    File inputFile;

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getInputFile() {
        return inputFile;
    }

    @CommandLine.Parameters(index = "1",
            hidden = true)
    private File outputFile;

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    @CommandLine.Option(names = "--resize",
            description = "resize the image",
            paramLabel = "width height",
            parameterConsumer = ConvertResizeParams.class)
    public ResizeParams resizeParams;

    static class ConvertResizeParams implements CommandLine.IParameterConsumer {
        public void consumeParameters(Stack<String> args,
                                      CommandLine.Model.ArgSpec argSpec,
                                      CommandLine.Model.CommandSpec commandSpec) {
            if (args.size() < 2) {
                throw new CommandLine.ParameterException(commandSpec.commandLine(),
                        "--resize flag must be accompanied by two int parameters: width and height");
            }
            Integer width = Integer.parseInt(args.pop());
            Integer height = Integer.parseInt(args.pop());
            argSpec.setValue(new ResizeParams(width, height));
        }
    }

    static class ResizeParams {
        Integer width;
        Integer height;

        public ResizeParams(Integer width, Integer height) {
            this.width = width;
            this.height = height;
        }
        public void setResizeParams(Integer width, Integer height) {
            if (width != null)
                this.width = width;
            if (height != null)
                this.height = height;
        }
    }

    public void setResizeWidth(Integer resizeWidth) {
        if (resizeParams == null)
            resizeParams = new ResizeParams(resizeWidth, null);
        else
            resizeParams.setResizeParams(resizeWidth, null);
    }

    public void setResizeHeight(Integer resizeHeight) {
        if (resizeParams == null)
            resizeParams = new ResizeParams(null, resizeHeight);
        else
            resizeParams.setResizeParams(null, resizeHeight);
    }

    public int getResizeWidth() {
        return resizeParams.width;
    }

    public int getResizeHeight() {
        return resizeParams.height;
    }

    @CommandLine.Option(names = "--quality",
            description = "JPEG/PNG compression level",
            paramLabel = "value")
    private Integer quality;

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Integer getQuality() {
        return quality;
    }

    @CommandLine.Option(names = "--crop",
            description = "@|fg(red) cut|@ out one rectangular area of the image",
            paramLabel = "width height x y",
            parameterConsumer = ConvertCropParams.class)
    public CropParams cropParams;

    static class ConvertCropParams implements CommandLine.IParameterConsumer {
        public void consumeParameters(Stack<String> args,
                                      CommandLine.Model.ArgSpec argSpec,
                                      CommandLine.Model.CommandSpec commandSpec) {
            if (args.size() < 4) {
                throw new CommandLine.ParameterException(commandSpec.commandLine(),
                        "--crop flag must be accompanied by four int parameters: width, height, x and y");
            }
            Integer width = Integer.parseInt(args.pop());
            Integer height = Integer.parseInt(args.pop());
            Integer x = Integer.parseInt(args.pop());
            Integer y = Integer.parseInt(args.pop());
            argSpec.setValue(new CropParams(width, height, x, y));
        }
    }

    static class CropParams {
        Integer width;
        Integer height;
        Integer x;
        Integer y;

        public CropParams(Integer width, Integer height, Integer x, Integer y) {
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
        }
    }

    public void setCropParams(Integer width, Integer height, Integer x, Integer y) {
        if (cropParams == null)
            cropParams = new CropParams(width, height, x, y);
        else {
            cropParams.width = width;
            cropParams.height = height;
            cropParams.x = x;
            cropParams.y = y;
        }
    }

    public Integer getCropWidth() { return cropParams.width; }
    public Integer getCropHeight() { return cropParams.height; }
    public Integer getCropX() { return cropParams.x; }
    public Integer getCropY() { return cropParams.y; }

    @CommandLine.Option(names = "--blur",
            description = "reduce image noise detail levels",
            paramLabel = "{radius}")
    private Integer blurRadius;

    public void setBlurRadius(Integer blurRadius) {
        this.blurRadius = blurRadius;
    }

    public Integer getBlurRadius() {
        return blurRadius;
    }

    @CommandLine.Option(names = "--format",
            description = "the image @|fg(red) format type|@",
            paramLabel = "\"outputFormat\"")
    private String outputFormat;

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getOutputFormat() {
        return outputFormat;
    }
}
