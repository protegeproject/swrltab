package org.swrltab.test;

import java.io.File;

import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLAPIRenderer;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleEngineFactory;
import org.swrlapi.drools.core.DroolsSWRLRuleEngineCreator;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.test.SWRLAPIRegressionTester;

/**
 * To invoke from Maven put <code>org.swrltab.test.SWRLTabRegressionTester</code> in the <code>mainClass</code> element
 * of the <code>exec-maven-plugin</code> plugin configuration in the Maven project POM and run with the
 * <code>exec:java</code> goal.
 */
public class SWRLTabRegressionTester
{
	public static void main(String[] args)
	{
		// TODO Hard code temporarily for testing. SWRLCoreTests, SQWRLCollectionsTests, SQWRLCoreTests, SWRLInferenceTests
		String owlFileName = SWRLTabRegressionTester.class.getClassLoader().getResource("projects/SQWRLCoreTests.owl")
				.getFile();
		File owlFile = new File(owlFileName);

		try {
			SWRLAPIOWLOntology swrlapiOWLOntology = SWRLAPIFactory.createOntology(owlFile);
			SWRLRuleEngineFactory swrlRuleEngineFactory = SWRLAPIFactory.createSWRLRuleEngineFactory();
			swrlRuleEngineFactory.registerRuleEngine(new DroolsSWRLRuleEngineCreator());

			SQWRLQueryEngine sqwrlQueryEngine = swrlRuleEngineFactory.createSQWRLQueryEngine(swrlapiOWLOntology);
			SWRLAPIRegressionTester swrlapiRegressionTester = new SWRLAPIRegressionTester(swrlapiOWLOntology,
					sqwrlQueryEngine);

			//			SWRLAPIRenderer renderer = SWRLAPIFactory.createSWRLAPIRenderer(swrlapiOWLOntology);
			//			for (SWRLAPIRule rule : swrlapiOWLOntology.getSWRLAPIRules())
			//				System.out.println(renderer.renderSWRLRule(rule));

			swrlapiRegressionTester.run();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private static void Usage()
	{
		System.err.println("Usage: " + SWRLTabRegressionTester.class.getName() + " <owlFileName>");
		System.exit(1);
	}
}

// SWRLParser parser = new SWRLParser(swrlapiOWLOntology);
// Scanner scanner = new Scanner(System.in);
// while (true) {
// String line = scanner.nextLine();
// try {
// System.out.println("Rule: " + line);
// parser.parseSWRLRule(line, true);
// System.out.println("Foine");
// } catch (SWRLIncompleteRuleException e) {
// System.err.println("Incomplete " + e.getMessage());
// } catch (SWRLParseException e) {
// System.err.println("Error " + e.getMessage());
// }
// }
