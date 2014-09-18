package org.swrlapi.sqwrl;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLRuleEngineFactory;
import org.swrlapi.drools.core.DroolsSWRLRuleEngineCreator;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.exceptions.SQWRLException;
import org.swrlapi.sqwrl.values.SQWRLLiteralResultValue;
import org.swrlapi.sqwrl.values.SQWRLNamedResultValue;
import org.swrlapi.sqwrl.values.SQWRLResultValue;
import org.swrlapi.test.SWRLAPITestBase;

import java.util.List;

public class SQWRLCollectionsTestCase extends SWRLAPITestBase
{
	String Namespace = "http://swrlapi.org/ontologies/SQWRLCoreTests.owl#";

	SQWRLQueryEngine sqwrlQueryEngine;

	@Before
	public void setUp() throws OWLOntologyCreationException
	{
		SWRLAPIOWLOntology swrlapiOWLOntology = createEmptyOntology(Namespace);

		SWRLRuleEngineFactory swrlRuleEngineFactory = SWRLAPIFactory.createSWRLRuleEngineFactory();
		swrlRuleEngineFactory.registerRuleEngine(new DroolsSWRLRuleEngineCreator());

		sqwrlQueryEngine = swrlRuleEngineFactory.createSQWRLQueryEngine(swrlapiOWLOntology);
	}

	@Test
	public void TestSQWRLCollectionsEqual() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT");

		SQWRLResult result = executeSQWRLQuery("q1", ". sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT)"
				+ " ^ sqwrl:makeBag(?s2, AZT) ^ sqwrl:makeBag(?s2, AZT) . sqwrl:equal(?s1, ?s2) -> sqwrl:select(\"Yes\")");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLCollectionSize() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT");

		SQWRLResult result = executeSQWRLQuery("q1",
				". sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT) . sqwrl:size(?size, ?s1) -> sqwrl:select(?size)");
		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLCollectionSizeEqual() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT");

		SQWRLResult result = executeSQWRLQuery("q1",
				". sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT) . sqwrl:size(?size, ?s1) "
						+ " ^ swrlb:equal(?size, 2) -> sqwrl:select(\"Yes!\")");
		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLFirst() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT");

		SQWRLResult result = executeSQWRLQuery("q1",
				". sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT) . sqwrl:first(?first, ?s1) -> sqwrl:select(?first)");
	}

	@Test
	public void TestSQWRLLast() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT");

		SQWRLResult result = executeSQWRLQuery("q1",
				". sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT) . sqwrl:last(?last, ?s1) -> sqwrl:select(?last)");

	}

	public void TestSQWRLNth() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT", "BBT");

		SQWRLResult result = executeSQWRLQuery("q1",
				" . sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT) ^ sqwrl:makeBag(?s1, BBT) "
						+ " . sqwrl:nth(?second, ?s1, 2) -> sqwrl:select(?second)");

	}

	public void TestSQWRLNthLast() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT", "BBT");

		SQWRLResult result = executeSQWRLQuery("q1",
				" . sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT) ^ sqwrl:makeBag(?s1, BBT) "
						+ " . sqwrl:nthLast(?secondLast, ?s1, 2) -> sqwrl:select(?secondLast)");

	}

	private SQWRLResult executeSQWRLQuery(String queryName) throws SQWRLException
	{
		return sqwrlQueryEngine.runSQWRLQuery(queryName);
	}

	protected SQWRLResult executeSQWRLQuery(String queryName, String query) throws SQWRLException, SWRLParseException
	{
		createSQWRLQuery(queryName, query);

		return executeSQWRLQuery(queryName);
	}
}
