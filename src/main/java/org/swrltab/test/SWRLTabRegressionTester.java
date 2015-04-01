package org.swrltab.test;

import java.io.File;

import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.drools.core.DroolsSWRLRuleEngineCreator;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.test.SWRLAPIRegressionTester;

/**
 * Uses a {@link org.swrlapi.test.SWRLAPIRegressionTester} to individually execute all SQWRL queries in an ontology and
 * compare the generated result with the expected result stored in the <code>rdfs:comment</code> annotation associated
 * with each query.
 * 
 * @see org.swrlapi.test.SWRLAPIRegressionTester
 */
public class SWRLTabRegressionTester
{
	public static void main(String[] args)
	{
		if (args.length != 1)
			Usage();

		String owlFileName = args[0];
		File owlFile = new File(owlFileName);

		try {
			SWRLAPIOWLOntology swrlapiOWLOntology = SWRLAPIFactory.createSWRLAPIOntology(owlFile);
			SQWRLQueryEngine sqwrlQueryEngine = swrlapiOWLOntology.createSQWRLQueryEngine(new DroolsSWRLRuleEngineCreator());
			SWRLAPIRegressionTester swrlapiRegressionTester = new SWRLAPIRegressionTester(swrlapiOWLOntology,
					sqwrlQueryEngine);

			swrlapiRegressionTester.run();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private static void Usage()
	{
		System.err.println("Usage: " + SWRLTabRegressionTester.class.getName() + " <owlFileName>");
		System.exit(1);
	}
}

// SWRLRuleRenderer renderer = swrlapiOWLOntology.createSWRLRuleRenderer();
// for (SWRLAPIRule rule : swrlapiOWLOntology.getSWRLAPIRules())
// System.out.println(renderer.renderSWRLRule(rule));

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
