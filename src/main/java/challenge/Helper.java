/*
 * This example This example provides a brief overview of running k-anonymity as the selected privacy model via ARX API. 
 * Furthermore, this example including data is implemented based on and inspired by materials on the ARX Github repository (https://github.com/arx-deidentifier/arx).
 */
package challenge;

import java.util.Arrays;
import java.util.Iterator;

import org.deidentifier.arx.DataHandle;

public class Helper {
	/**
     * Prints a given data handle.
     *
     * @param handle
     */
    protected static void print(DataHandle handle) {
        final Iterator<String[]> itHandle = handle.iterator();
        print(itHandle);
    }

    /**
     * Prints a given iterator.
     *
     * @param iterator
     */
    protected static void print(Iterator<String[]> iterator) {
        while (iterator.hasNext()) {
            System.out.print("   ");
            System.out.println(Arrays.toString(iterator.next()));
        }
    }

}
