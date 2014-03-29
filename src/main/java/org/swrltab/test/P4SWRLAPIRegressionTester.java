package org.swrltab.test;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLRuleEngineFactory;
import org.swrlapi.drools.DroolsSWRLRuleEngineCreator;
import org.swrlapi.exceptions.SWRLRuleEngineException;
import org.swrlapi.ext.SWRLAPIOWLOntology;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.test.SWRLAPIRegressionTester;

/**
 * Example invocation: <code><pre>
 * java -jar target/swrltab-1.0-jar-with-dependencies.jar ~/workspace/swrlapi/swrltab/src/main/resources/projects/SWRLCoreTests.owl 
 * </pre></code>
 * 
 * @author martin
 */
public class P4SWRLAPIRegressionTester
{
	public static void main(String[] args)
	{
		String owlFileName = "";

		if (args.length == 1) {
			owlFileName = args[0];
		} else
			Usage();

		try {
			OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
			File file = new File(owlFileName);
			OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(file);
			SWRLAPIOWLOntology swrlapiOWLOntology = SWRLAPIFactory.createSWRLAPIOWLOntology(ontologyManager, ontology);

			SWRLRuleEngineFactory swrlRuleEngineFactory = SWRLAPIFactory.createSWRLRuleEngineFactory();
			swrlRuleEngineFactory.registerRuleEngine(new DroolsSWRLRuleEngineCreator());

			SQWRLQueryEngine queryEngine = swrlRuleEngineFactory.createSQWRLQueryEngine(ontologyManager, swrlapiOWLOntology);

			SWRLAPIRegressionTester swrlapiRegressionTester = new SWRLAPIRegressionTester(swrlapiOWLOntology, queryEngine);

			swrlapiRegressionTester.run();
		} catch (SWRLRuleEngineException e) {
			e.printStackTrace();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private static void Usage()
	{
		System.err.println("Usage: " + P4SWRLAPIRegressionTester.class.getName() + " <owlFileName>");
		System.exit(1);
	}
}
