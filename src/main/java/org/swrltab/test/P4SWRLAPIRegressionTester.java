package org.swrltab.test;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLRuleEngineFactory;
import org.swrlapi.drools.DroolsSWRLRuleEngineCreator;
import org.swrlapi.exceptions.SWRLRuleEngineException;
import org.swrlapi.ext.SWRLAPIOWLOntology;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.test.SWRLAPIRegressionTester;

/**
 * Example invocation: <code><pre>
 * java -jar ~/workspace/swrlapi/swrltab/target/swrltab-1.0-jar-with-dependencies.jar ~/workspace/swrlapi/swrltab/src/main/resources/projects/SWRLCoreTests.owl 
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

			DefaultPrefixManager prefixManager = new DefaultPrefixManager(
			// "http://swrl.stanford.edu/ontologies/tests/3.5.1/SQWRLCollectionsTests.owl#");
					// "http://swrl.stanford.edu/ontologies/tests/3.5.1/SQWRLCoreTests.owl#");
					// "http://swrl.stanford.edu/ontologies/tests/3.5.1/SWRLInferenceTests.owl#");
					"http://swrl.stanford.edu/ontologies/tests/3.5.1/SWRLCoreTests.owl#");
			prefixManager.setPrefix("swrl:", "http://www.w3.org/2003/11/swrl#");
			prefixManager.setPrefix("swrlb:", "http://www.w3.org/2003/11/swrlb#");
			prefixManager.setPrefix("sqwrl:", "http://sqwrl.stanford.edu/ontologies/built-ins/3.4/sqwrl.owl#");
			prefixManager.setPrefix("swrlm:", "http://swrl.stanford.edu/ontologies/built-ins/3.4/swrlm.owl#");
			prefixManager.setPrefix("temporal:", "http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl#");
			prefixManager.setPrefix("tbox:", "http://swrl.stanford.edu/ontologies/built-ins/3.3/tbox.owl#");
			prefixManager.setPrefix("swrlx:", "http://swrl.stanford.edu/ontologies/built-ins/3.3/swrlx.owl#");
			prefixManager.setPrefix("swrlxml:", "http://swrl.stanford.edu/ontologies/built-ins/3.4/swrlxml.owl#");
			prefixManager.setPrefix("swrla:", "http://swrl.stanford.edu/ontologies/3.3/swrla.owl#");

			SWRLAPIOWLOntology swrlapiOWLOntology = SWRLAPIFactory.createSWRLAPIOWLOntology(ontologyManager, ontology,
					prefixManager);

			SWRLRuleEngineFactory swrlRuleEngineFactory = SWRLAPIFactory.createSWRLRuleEngineFactory();
			swrlRuleEngineFactory.registerRuleEngine(new DroolsSWRLRuleEngineCreator());

			SQWRLQueryEngine sqwrlQueryEngine = swrlRuleEngineFactory.createSQWRLQueryEngine(swrlapiOWLOntology);
			SWRLAPIRegressionTester swrlapiRegressionTester = new SWRLAPIRegressionTester(sqwrlQueryEngine);

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
