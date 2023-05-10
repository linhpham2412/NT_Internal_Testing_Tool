package nt.testingtool.istqb.Utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Optional;

import static nt.testingtool.istqb.Utils.ProjectConfiguration.getCurrentPath;

public class ImageCaptureHandler {
    public static void convertPNGImageToJPG(String imageName) {
        try {
            File imageToBeConvert = new File(getCurrentPath() + File.separator + imageName + ".png");
            BufferedImage input = ImageIO.read(imageToBeConvert);
            int width = input.getWidth();
            int height = input.getHeight();
            BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            int px[] = new int[width * height];
            input.getRGB(0, 0, width, height, px, 0, width);
            output.setRGB(0, 0, width, height, px, 0, width);
            ImageIO.write(output, "jpg", new File(getCurrentPath() + File.separator + imageName + ".jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void captureAndSaveImageInContainer(VBox certificateVbox, String imageName) {
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        WritableImage writableImage = new WritableImage((int) certificateVbox.getWidth(), (int) certificateVbox.getHeight());
        writableImage = certificateVbox.snapshot(snapshotParameters, writableImage);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null),
                    "png", new File(getCurrentPath() + File.separator + imageName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readTitleInMetadataOfJpgImage(final File jpegImageFile) throws IOException, ImageReadException {
        // note that metadata might be null if no metadata is found.
        final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
        final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        List<TiffField> imageTiffFields = jpegMetadata.getExif().getAllFields();
        Optional<TiffField> imageTitle = imageTiffFields.stream()
                .filter(tiffField -> tiffField.getTagName().matches("XPTitle"))
                .findFirst();
        return imageTitle.get().getValue().toString();
    }

    public static boolean updateImageJPGTitleMetadataFields(final File jpegImageFile, final File dst, String titleValue)
            throws IOException, ImageReadException, ImageWriteException {

        try (FileOutputStream fos = new FileOutputStream(dst);
             OutputStream os = new BufferedOutputStream(fos)) {
            TiffOutputSet outputSet = null;
            // note that metadata might be null if no metadata is found.
            final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
            final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                // note that exif might be null if no Exif metadata is found.
                final TiffImageMetadata exif = jpegMetadata.getExif();
                if (null != exif) {
                    outputSet = exif.getOutputSet();
                }
            }
            // if file does not contain any exif metadata, we create an empty
            // set of exif metadata. Otherwise, we keep all of the other
            // existing tags.
            if (null == outputSet) {
                outputSet = new TiffOutputSet();
            }

            final TiffOutputDirectory rootDir = outputSet.getOrCreateRootDirectory();
            rootDir.removeField(MicrosoftTagConstants.EXIF_TAG_XPTITLE);
            rootDir.add(MicrosoftTagConstants.EXIF_TAG_XPTITLE, titleValue);

            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os,
                    outputSet);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void addImageIntoColorRoundBorder(ImageView imageViewToWrap, Image imageToWrap, Color color) {
        // set a clip to apply rounded border to the original image.
        Rectangle clip = new Rectangle(
                imageViewToWrap.getFitWidth(), imageViewToWrap.getFitHeight()
        );
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageViewToWrap.setClip(clip);

        // snapshot the rounded image.
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageViewToWrap.snapshot(parameters, null);

        // remove the rounding clip so that our effect can show through.
        imageViewToWrap.setClip(null);

        // apply a shadow effect.
        imageViewToWrap.setEffect(new DropShadow(20, color));

        // store the rounded image in the imageView.
        imageViewToWrap.setImage(imageToWrap);
    }
}
