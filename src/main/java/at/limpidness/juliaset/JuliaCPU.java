package at.limpidness.juliaset;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Computes the Julia set by iterating the
 * quadratic map f(z) = z² + c, with c being the
 * given parameter to the <i>run</i> method
 */
public class JuliaCPU implements Julia {

    private static final byte alpha = (byte) 0xff;
    private static final byte black = (byte) 0x0;
    private final byte[] array = new byte[DIM * DIM * 4];
    private byte red, green, blue;

    /**
     * Computes a single column of the Julia set.
     * The column is given in the constructor.
     */
    private class JuliaRunnable implements Runnable {
        private final static float scale = 1.5f;

        private final Complex c;
        private final int y;

        /**
         * Constructs a Runnable for the Julia set computation
         *
         * @param y Index of column to compute
         * @param complex Constant c
         */
        JuliaRunnable(int y, Complex complex) {
            this.c = complex;
            this.y = y;
        }

        @Override
        public void run() {
            // Iteration of rows
            loop: for (int x = 0; x < DIM; x++) {
                int index = (y + x * DIM) * 4;
                float jx = scale * (DIM/2.0f - y)/(DIM/2.0f);
                float jy = scale * (DIM/2.0f - x)/(DIM/2.0f);

                Complex a = new Complex(jx, jy);

                // Iteration function f(z) = z² + c
                for (int i = 0; i < 200; i++) {
                    a = a.times(a).plus(c);
                    if (a.magnitude2() > 1000) {
                        array[index] = black;
                        array[index + 1] = black;
                        array[index + 2] = black;
                        array[index + 3] = alpha;
                        continue loop; // Next pixel
                    }
                }

                // Value is in the julia set, colorize pixel
                array[index] = blue;
                array[index + 1] = green;
                array[index + 2] = red;
                array[index + 3] = alpha;
            }
        }
    }

    @Override
    public ByteBuffer run(float real, float imaginary, byte red, byte green, byte blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;

        try(ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            List<Future<?>> futures = new ArrayList<>(DIM * DIM);
            // Tasks for each column
            for (int i = 0; i < DIM; i++) {
                JuliaRunnable juliaRunnable = new JuliaRunnable(i, new Complex(real, imaginary));
                futures.add(executor.submit(juliaRunnable));
            }

            // Wait for all tasks
            for (Future<?> future : futures)
                future.get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return ByteBuffer.wrap(array);
    }
}
