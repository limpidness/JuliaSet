package at.limpidness.juliaset;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static at.limpidness.juliaset.Julia.DIM;

/**
 * Controller class of the Julia set visualization
 */
public class JuliaSetController {

    private final Julia julia = new JuliaCPU();

    @FXML
    private  String borderRed;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField inputReal;

    @FXML
    private TextField inputImaginary;

    @FXML
    private Label labelError;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button saveButton;

    /**
     * Compute the Julia set when corresponding button is hit.
     */
    @FXML
    private void runJuliaAction(ActionEvent ignore) {
        // Read input from text fields. Use default values if empty.
        String realString = inputReal.getText().replaceAll(",", ".");
        if (realString.isEmpty())
            realString = inputReal.getPromptText();
        String imaginaryString = inputImaginary.getText().replaceAll(",", ".");
        if (imaginaryString.isEmpty())
            imaginaryString = inputImaginary.getPromptText();


        float real = 0.0f, imaginary = 0.0f;
        boolean error = false;

        // Parse input to float. Show error message if not possible
        try {
            real = Float.parseFloat(realString);
            labelError.setText(null);
            inputReal.setStyle(null);
        } catch (NumberFormatException ignoreException) {
            inputReal.setStyle(borderRed);
            labelError.setText("Invalid Input");
            error = true;
        }
        try {
            imaginary = Float.parseFloat(imaginaryString);
            labelError.setText(null);
            inputImaginary.setStyle(null);
        } catch (NumberFormatException ignoreException) {
            inputImaginary.setStyle(borderRed);
            labelError.setText("Invalid Input");
            error = true;
        }

        // Only continue if both values are valid.
        if (error) return;

        // Compute Julia set.
        Color color = colorPicker.getValue();
        byte red = (byte) (color.getRed() * 255);
        byte green = (byte) (color.getGreen() * 255);
        byte blue = (byte) (color.getBlue() * 255);
        ByteBuffer buffer = julia.run(real, imaginary,
                red, green, blue);

        // Update visualization
        PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();

        PixelBuffer<ByteBuffer> pixelBuffer = new PixelBuffer<>(DIM, DIM, buffer, pixelFormat);
        WritableImage image = new WritableImage(pixelBuffer);

        imageView.setImage(image);
    }

    /**
     * Saves the actual visualization of the Julia set as PNG image.
     */
    @FXML
    private void saveAction(ActionEvent ignore) {

        // Image of the Julia set
        Image image = imageView.getImage();
        if (image == null) return;

        // Choose file to save
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.png"));
        File file = fileChooser.showSaveDialog(null);

        // If aborted
        if (file == null) return;

        // Save image
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            saveButton.setStyle(null);
        } catch (IOException ignore1) {
            saveButton.setStyle(borderRed);
        }
    }
}
