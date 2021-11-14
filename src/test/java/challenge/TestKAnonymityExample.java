package challenge;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.criteria.KAnonymity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestKAnonymityExample {
	
	final String[][] expected = {
            { "age", "gender", "zipcode" },
            { "<50", "*", "816**" },
            { "<50", "*", "816**" },
            { ">=50", "*", "819**" },
            { ">=50", "*", "819**" },
            { "<50", "*", "819**" },
            { ">=50", "*", "819**" },
            { "<50", "*", "819**" } };
	final String[][] expectedWithBuilder = {
            { "age", "gender", "zipcode" },
            { "**", "male", "81***" },
            { "**", "female", "81***" },
            { "**", "male", "81***" },
            { "**", "female", "81***" },
            { "**", "female", "81***" },
            { "**", "male", "81***" },
            { "**", "male", "81***" } };
	final String dataDirectory = "data/";
	final String inputCSV = "test.csv";
	/**
     * Perform unit test based on the example
     */
    @Test
    public void testExample() {
        try {
        	KAnonymityExample.main(null);
        } catch (final Exception e) {
            e.printStackTrace();
            Assertions.fail(); 
        }
    }
	/**
     * Performs a test on the result
     *
     * @throws IOException
     */
    @Test
    public void testKAnonymization_manualData() throws IOException {
        // k == 2
    	DefaultData input = KAnonymityExample.getDataManual();
    	KAnonymityExample.generalizeDataManual(input);
        
        final ARXAnonymizer anonymizer = new ARXAnonymizer();
        final ARXConfiguration config = ARXConfiguration.create();
        config.addPrivacyModel(new KAnonymity(2));
        config.setSuppressionLimit(0d);
        final String[][] result = resultToArray(anonymizer.anonymize(input, config));
                                     
        Assertions.assertTrue(Arrays.deepEquals(result, expected));
    }
    
    /**
     * Performs a test on the result
     *
     * @throws IOException
     */
    @Test
    public void testKAnonymization_csvData() throws IOException {
    	 // k == 3
    	Data input = KAnonymityExample.getDataCSV(dataDirectory+inputCSV);
    	KAnonymityExample.generalizeDataBuilder(input);
        
        final ARXAnonymizer anonymizer = new ARXAnonymizer();
        final ARXConfiguration config = ARXConfiguration.create();
        config.addPrivacyModel(new KAnonymity(3));
        config.setSuppressionLimit(0d);
        final String[][] result = resultToArray(anonymizer.anonymize(input, config));
                                     
        Assertions.assertTrue(Arrays.deepEquals(result, expectedWithBuilder));
    }
    
    /**
     * Convert to array
     * @param result
     * @return
     */
    protected String[][] resultToArray(final ARXResult result) {
        final ArrayList<String[]> list = new ArrayList<String[]>();
        final Iterator<String[]> transformed = result.getOutput(false).iterator();
        while (transformed.hasNext()) {
            list.add(transformed.next());
        }
        return list.toArray(new String[list.size()][]);
    }
}
