package org.swrltab.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;
import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.ext.SWRLAPIOWLOntology;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.parser.SWRLParser;

/**
 * Example invocation: <code><pre>
 * java -jar ~/workspace/swrlapi/swrltab/target/swrltab-1.0-jar-with-dependencies.jar ~/workspace/swrlapi/swrltab/src/main/resources/projects/SWRLCoreTests.owl 
 * </pre></code>
 */
public class P5SWRLAPIRegressionTester
{
	public static void main(String[] args)
	{
		String testBase = "/Users/martin/workspace/swrlapi/swrltab/src/main/resources/projects/";
		// String owlFileName = testBase + "SWRLSimple.owl";
		// String owlFileName = testBase + "SQWRLCollectionsTests.owl";
		// String owlFileName = testBase + "SQWRLCoreTests.owl";
		// String owlFileName = testBase + "SWRLInferenceTests.owl";
		String owlFileName = testBase + "SWRLCoreTests.owl";
		File file = new File(owlFileName);

		try {
			OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = getOntology(ontologyManager, file);
			DefaultPrefixManager prefixManager = getPrefixManager(ontologyManager, ontology);
			SWRLAPIOWLOntology swrlapiOWLOntology = SWRLAPIFactory.createSWRLAPIOWLOntology(ontologyManager, ontology,
					prefixManager);

			SWRLParser parser = new SWRLParser(swrlapiOWLOntology, prefixManager);

			try {
				parser.parseSWRLRule(
						":hasName(?x, \"dd\", false, true, \"dsdsd\", 43, 545.34) ^ differentFrom(?y, ?f) -> sameAs(?x, ?x", true);
			} catch (SWRLParseException e) {
				System.err.println("e" + e.getMessage());
			}

			// SWRLRuleEngineFactory swrlRuleEngineFactory = SWRLAPIFactory.createSWRLRuleEngineFactory();
			// swrlRuleEngineFactory.registerRuleEngine(new DroolsSWRLRuleEngineCreator());
			//
			// SQWRLQueryEngine sqwrlQueryEngine = swrlRuleEngineFactory.createSQWRLQueryEngine(swrlapiOWLOntology);
			// SWRLAPIRegressionTester swrlapiRegressionTester = new SWRLAPIRegressionTester(sqwrlQueryEngine);
			//
			// swrlapiRegressionTester.run();
			// } catch (SWRLRuleEngineException e) {
			// e.printStackTrace();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private static OWLOntology getOntology(OWLOntologyManager ontologyManager, File file)
			throws OWLOntologyCreationException
	{
		Map<String, String> map = new HashMap<String, String>();

		map.put("http://swrl.stanford.edu/ontologies/3.3/swrla.owl", "file:///tmp/swrla.owl");
		map.put("http://swrl.stanford.edu/ontologies/built-ins/3.4/swrlm.owl", "file:///tmp/swrlm.owl");
		map.put("http://swrl.stanford.edu/ontologies/built-ins/3.4/swrlm.owl", "file:///tmp/swrlm.owl");
		map.put("http://swrl.stanford.edu/ontologies/built-ins/3.3/swrlx.owl", "file:///tmp/swrlx.owl");
		map.put("http://swrl.stanford.edu/ontologies/built-ins/3.4/swrlxml.owl", "file:///tmp/swrlxml.owl");
		map.put("http://swrl.stanford.edu/ontologies/built-ins/3.3/temporal.owl", "file:///tmp/temporal.owl");
		map.put("http://sqwrl.stanford.edu/ontologies/built-ins/3.4/sqwrl.owl", "file:///tmp/sqwrl.owl");

		for (String key : map.keySet())
			ontologyManager.addIRIMapper(new SimpleIRIMapper(IRI.create(key), IRI.create(map.get(key))));

		return ontologyManager.loadOntologyFromOntologyDocument(file);
	}

	private static DefaultPrefixManager getPrefixManager(OWLOntologyManager ontologyManager, OWLOntology ontology)
	{
		DefaultPrefixManager prefixManager = new DefaultPrefixManager();
		OWLOntologyFormat ontologyFormat = ontologyManager.getOntologyFormat(ontology);

		if (ontologyFormat.isPrefixOWLOntologyFormat()) {
			PrefixOWLOntologyFormat prefixOntologyFormat = ontologyFormat.asPrefixOWLOntologyFormat();
			String defaultPrefix = prefixOntologyFormat.getDefaultPrefix();
			Map<String, String> map = prefixOntologyFormat.getPrefixName2PrefixMap();
			for (String prefix : map.keySet())
				prefixManager.setPrefix(prefix, map.get(prefix));
			prefixManager.setDefaultPrefix(defaultPrefix);
		}
		return prefixManager;
	}

	@SuppressWarnings("unused")
	private static void Usage()
	{
		System.err.println("Usage: " + P5SWRLAPIRegressionTester.class.getName() + " <owlFileName>");
		System.exit(1);
	}
}
