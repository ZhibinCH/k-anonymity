package challenge;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.ARXPopulationModel.Region;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased.Order;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelAttributes;
import org.deidentifier.arx.risk.RiskModelHistogram;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness;
import org.deidentifier.arx.risk.RiskModelSampleRisks;
import org.deidentifier.arx.risk.RiskModelSampleUniqueness;
import org.deidentifier.arx.risk.RiskModelAttributes.QuasiIdentifierRisk;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness.PopulationUniquenessModel;



public class KAnonymityExample {
	
	public static String dataDirectory = "data/";
    public static String inputCSV = "test.csv";
    public static String outputCSV ="test_anonymized.csv";
    public static String inputHierarchyAge = "test_hierarchy_age.csv";
    public static String inputHierarchyGender = "test_hierarchy_gender.csv";
    public static String inputHierarchyZipcode = "test_hierarchy_zipcode.csv";
    
	public static void main(String[] args) throws Exception {
// 1. Load data
//		DefaultData input = getDataManual(); // alternative 1.1
		Data input = getDataCSV(); 			// alternative 1.2
// 2. Generalization
//	    generalizeDataManual(input); 		// alternative 2.1
//		generalizeDataCSV(input); 			// alternative 2.2
		generalizeDataBuilder(input); 		// alternative 2.3
// 3. Define privacy models and transformation rules
// 4. Execute the anonymization algorithm
		ARXResult result = runAnonymizer(input); // step 3 and 4
// 5. Access and compare data
		compareData(input,result); // step 5
// 6. Analyze re-identification risks
// 7. Store data	
		storeResult(result);
	}
	private static DefaultData getDataManual() {
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
	
	private static Data getDataCSV() throws IOException {
		Data data = Data.create(dataDirectory+inputCSV, StandardCharsets.UTF_8, ';');
        return data;
	}
	
	private static void generalizeDataManual(Data data) {
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
	
	private static void generalizeDataCSV(Data data) throws IOException {
        data.getDefinition().setAttributeType("age", Hierarchy.create(dataDirectory+inputHierarchyAge, StandardCharsets.UTF_8, ';'));
        data.getDefinition().setAttributeType("gender", Hierarchy.create(dataDirectory+inputHierarchyGender, StandardCharsets.UTF_8, ';'));
        data.getDefinition().setAttributeType("zipcode", Hierarchy.create(dataDirectory+inputHierarchyZipcode, StandardCharsets.UTF_8, ';'));
	}
	
	private static void generalizeDataBuilder(Data data) {
		// Define hierarchies
        HierarchyBuilderRedactionBased<?> builder1 = HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT,
                                                                                           Order.RIGHT_TO_LEFT,
                                                                                           ' ',
                                                                                           '*');
        HierarchyBuilderRedactionBased<?> builder2 = HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT,
                                                                                           Order.RIGHT_TO_LEFT,
                                                                                           ' ',
                                                                                           '*');

        data.getDefinition().setAttributeType("age", builder1);
        data.getDefinition().setAttributeType("gender", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
        data.getDefinition().setAttributeType("zipcode", builder2);
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
	
	private static void compareData (Data data, ARXResult result) {
		 System.out.println("\n - Input data");
	     Helper.print(data.getHandle());
	     System.out.println("\n - Quasi-identifiers sorted by risk:");
	     analyzeAttributes(data.getHandle());
	     System.out.println("\n - Output data");
	     Helper.print(result.getOutput());
	     analyzeResult(result.getOutput());
	     
	}
	
	/**
     * Perform risk analysis for input
     * @param handle
     */
    private static void analyzeAttributes(DataHandle handle) {
        ARXPopulationModel populationmodel = ARXPopulationModel.create(Region.USA);
        RiskEstimateBuilder builder = handle.getRiskEstimator(populationmodel);
        RiskModelAttributes riskmodel = builder.getAttributeRisks();
        for (QuasiIdentifierRisk risk : riskmodel.getAttributeRisks()) {
            System.out.println("   * Distinction: " + risk.getDistinction() + ", Separation: " + risk.getSeparation() + ", Identifier: " + risk.getIdentifier());
        }
    }
    
    /**
     * Perform risk estimate for result
     * @param handle
     */
    private static void analyzeResult(DataHandle handle) {
        
        ARXPopulationModel populationmodel = ARXPopulationModel.create(Region.USA);
        RiskEstimateBuilder builder = handle.getRiskEstimator(populationmodel);
        RiskModelHistogram classes = builder.getEquivalenceClassModel();
        RiskModelSampleRisks sampleReidentifiationRisk = builder.getSampleBasedReidentificationRisk();
        RiskModelSampleUniqueness sampleUniqueness = builder.getSampleBasedUniquenessRisk();
        RiskModelPopulationUniqueness populationUniqueness = builder.getPopulationBasedUniquenessRisk();
        
        System.out.println("\n - Risk estimates:");
        System.out.println("   * Sample-based measures");
        System.out.println("     + Average risk     : " + sampleReidentifiationRisk.getAverageRisk());
        System.out.println("     + Lowest risk      : " + sampleReidentifiationRisk.getLowestRisk());
        System.out.println("     + Tuples affected  : " + sampleReidentifiationRisk.getFractionOfRecordsAffectedByLowestRisk());
        System.out.println("     + Highest risk     : " + sampleReidentifiationRisk.getHighestRisk());
        System.out.println("     + Tuples affected  : " + sampleReidentifiationRisk.getFractionOfRecordsAffectedByHighestRisk());
        System.out.println("     + Sample uniqueness: " + sampleUniqueness.getFractionOfUniqueRecords());
        System.out.println("   * Population-based measures");
        System.out.println("     + Population unqiueness (Zayatz): " + populationUniqueness.getFractionOfUniqueTuples(PopulationUniquenessModel.ZAYATZ));
    }
	
	private static void storeResult(ARXResult result) throws IOException {
		System.out.print("\n - Writing data...");
        result.getOutput(false).save(dataDirectory+outputCSV, ';');
        System.out.println("Done!");
        System.out.println("Result is saved: "+System.getProperty("user.dir")+dataDirectory+outputCSV);
	}
}
