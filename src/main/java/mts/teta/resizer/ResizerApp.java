package mts.teta.resizer;

import mts.teta.resizer.imageprocessor.ImageProcessor;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "resizer",
        version = "resizer 0.0.1",
        headerHeading = "Version: resizer 0.0.1%n",
        header = "Available formats: jpeg png",
        optionListHeading = "Options Settings:%n",
        sortOptions = false,
        separator = " ",
        customSynopsis = "convert input-file [options ...] output-file")

public class ResizerApp extends ConsoleAttributes implements Callable<Integer> {
    public static void main(String... args) {
        int exitCode = runConsole(args);
        System.exit(exitCode);
    }

    protected static int runConsole(String[] args) {

        CommandLine.Help.ColorScheme colorScheme = new CommandLine.Help.ColorScheme.Builder()
                .ansi        (CommandLine.Help.Ansi.ON)
                .errors      (CommandLine.Help.Ansi.Style.fg_red, CommandLine.Help.Ansi.Style.bold)
                .stackTraces (CommandLine.Help.Ansi.Style.italic)
                .build();

        return new CommandLine(new ResizerApp())
                .setColorScheme(colorScheme)
                .setUsageHelpLongOptionsMaxWidth(40)
                .execute(args);
    }

    @Override
    public Integer call() throws Exception {
        ImageProcessor imageProcessor = new ImageProcessor();
        imageProcessor.processImage(ImageIO.read(inputFile), this);
        return 0;
    }
}
