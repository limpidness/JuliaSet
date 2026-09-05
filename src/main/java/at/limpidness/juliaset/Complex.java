package at.limpidness.juliaset;

/**
 * A complex number with mandatory functionality
 *
 * @param real The real part of the number
 * @param imaginary The imaginary part of the number
 */
record Complex(float real, float imaginary) {
    /**
     * Computes the square of the magnitude
     *
     * @return Square of the magnitude
     */
    float magnitude2() {
        return real * real + imaginary * imaginary;
    }

    /**
     * Multiplies with another complex number
     *
     * @param other the number to multiply with
     * @return Product of the two numbers
     */
    Complex times(Complex other) {
        return new Complex(real * other.real - imaginary * other.imaginary,
                imaginary * other.real + real * other.imaginary);
    }

    /**
     * Adds another complex number
     *
     * @param other The number to add
     * @return Sum of the two numbers
     */
    Complex plus(Complex other) {
        return new Complex(real + other.real, imaginary + other.imaginary);
    }
}
