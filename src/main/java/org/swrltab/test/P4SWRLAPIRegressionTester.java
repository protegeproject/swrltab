package org.swrltab.test;

public class P4SWRLAPIRegressionTester
{
	// private final SQWRLQueryEngine queryEngine;
	// private final String ruleEngineName;

	public static void main(String[] args)
	{
		@SuppressWarnings("unused")
		String ruleEngineName = "", owlFileName = "";

		if (args.length == 2) {
			ruleEngineName = args[0];
			owlFileName = args[1];
		} else
			Usage();

		// OWLOntologyManager manager = null;
		// OWLOntologyID ontologyID = null;

		// SWRLRuleEngineFactory.registerRuleEngine("Drools", new DroolsSWRLRuleEngineCreator());

		// SWRLAPIOWLOntology swrlapiOWLOntology = new DefaultSWRLAPIOWLOntology(manager, ontologyID);

	}

	private static void Usage()
	{
		System.err.println("Usage: " + P4SWRLAPIRegressionTester.class.getName() + " <ruleEngine> <owlFileName>");
		System.exit(1);
	}
}
