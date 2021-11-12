package challenge;

import java.io.IOException;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.criteria.KAnonymity;

public class KAnonymityExample {
	public static void main(String[] args) throws Exception {
	// 1. Load data
		DefaultData data = getData();
	// 2. Generalization
		generalizeData(data);
	// 3. Define privacy models and transformation rules
	// 4. Execute the anonymization algorithm
		ARXResult result = runAnonymizer(data);
	// 5. Access and compare data
	// 6. Analyze re-identification risks
	// 7. Store data	
		storeResult(result);
	}
	private static DefaultData getData() {
		DefaultData data = Data.create();
        data.add("age", "gender", "zipcode");
        data.add("34", "male", "81667");
        data.add("45", "female", "81675");
        data.add("66", "male", "81925");
        data.add("70", "female", "81931");
        data.add("34", "female", "81931");
        data.add("70", "male", "81931");
        data.add("45", "male", "81931");
        return data;
	}
	private static void generalizeData(Data data) {
		// Define hierarchies
        DefaultHierarchy age = Hierarchy.create();
        age.add("34", "<50", "*");
        age.add("45", "<50", "*");
        age.add("66", ">=50", "*");
        age.add("70", ">=50", "*");

        DefaultHierarchy gender = Hierarchy.create();
        gender.add("male", "*");
        gender.add("female", "*");

        // Only excerpts for readability
        DefaultHierarchy zipcode = Hierarchy.create();
        zipcode.add("81667", "8166*", "816**", "81***", "8****", "*****");
        zipcode.add("81675", "8167*", "816**", "81***", "8****", "*****");
        zipcode.add("81925", "8192*", "819**", "81***", "8****", "*****");
        zipcode.add("81931", "8193*", "819**", "81***", "8****", "*****");

        data.getDefinition().setAttributeType("age", age);
        data.getDefinition().setAttributeType("gender", gender);
        data.getDefinition().setAttributeType("zipcode", zipcode);	
	}
	
	private static ARXResult runAnonymizer (Data data) throws IOException {
		// Create an instance of the anonymizer
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        config.addPrivacyModel(new KAnonymity(3));
        config.setSuppressionLimit(0d);
        ARXResult result = anonymizer.anonymize(data, config);
        return result;
	}
	
	private static void storeResult(ARXResult result) throws IOException {
		System.out.print(" - Writing data...");
        result.getOutput(false).save("data/test_anonymized.csv", ';');
        System.out.println("Done! Result is saved: "+System.getProperty("user.dir")+"/data/test_anonymized.csv");
	}
}
