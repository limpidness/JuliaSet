package at.limpidness.juliaset;

import java.nio.ByteBuffer;

/**
 * Interface for computing the Julia set.<br>
 * Implementing classes have to override the method <i>run</i>
 */
public interface Julia {

    /** Resolution of the Julia set visualization **/
    int DIM = 1000;

    /**
     * Computes the Julia set with the given complex number <i>c = real + imaginary * i</i>
     * and returns a ByteBuffer with the given color values
     *
     * @param real Real part of <i>c</i>
     * @param imaginary Imaginary part of <i>c</i>
     * @return ByteBuffer with color values
     */
    ByteBuffer run(float real, float imaginary, byte red, byte green, byte blue);

}
