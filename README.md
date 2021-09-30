## Сrop-resize
### 🗓 May 2021

A simple utility for resizing images on the backend: changing advertising banners, previews for music albums and movie covers in low resolution, and others.

## Technology stack

- Java, Maven
    - [picocli.info](https://picocli.info/)
    - [thumbnailator](https://github.com/coobird/thumbnailator) 
    - [marvin project](https://github.com/gabrielarchanjo/marvin-framework)
    - [junit5](https://github.com/junit-team/junit5).

## Interface

```bash
Version: name version https://gitlab.com/link/
Available formats: jpeg png webp
Usage: convert input-file [options ...] output-file
Options Settings:
  --resize width height       resize the image
  --quality value             PEG/PNG compression level
  --crop width height x y     сut out one or more rectangular regions of the image
  --blur {radius}             reduce image noise and reduce detail levels 
  --format "outputFormat"     the image format type
```
