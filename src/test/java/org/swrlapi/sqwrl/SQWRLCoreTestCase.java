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

public class SQWRLCoreTestCase extends SWRLAPITestBase
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
	public void TestClassAtomInAntecedentWithNamedIndividual() throws SWRLParseException, SQWRLException
	{
		declareOWLClass("Male");
		declareOWLNamedIndividual("p1");
		declareOWLClassAssertion("Male", "p1");

		SQWRLResult result = executeSQWRLQuery("q1", "Male(p1) -> sqwrl:select(p1)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLNamedResultValue p1 = row.get(0).asNamedResult();
			result.next();
		}
	}

	@Test
	public void TestClassAtomInAntecedentWithVariable() throws SWRLParseException, SQWRLException
	{
		declareOWLClass("Male");
		declareOWLNamedIndividual("p1");
		declareOWLClassAssertion("Male", "p1");

		SQWRLResult result = executeSQWRLQuery("q1", "Male(?m) -> sqwrl:select(?m)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLNamedResultValue m = row.get(0).asNamedResult();
			result.next();
		}
	}

	@Test
	public void TestSWRLBAddWithQualifiedLongLiterals() throws SWRLParseException, SQWRLException
	{
		String query = "swrlb:add(\"4\"^^\"xsd:long\", \"2\"^^\"xsd:long\", \"2\"^^\"xsd:long\") -> sqwrl:select(\"Yes!)\")";
		SQWRLResult result = executeSQWRLQuery("q1", query);

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSWRLBAddWithRawLongLiterals() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(4, 2, 2) -> sqwrl:select(\"Yes!\")");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSWRLBAddWithRawLongLiteralsAndVariableResult() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(?r, 2, 2) -> sqwrl:select(?r)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next(); // TODO Returns a float
		}
	}

	@Test
	public void TestSWRLBAddWithQualifiedDoubleLiterals() throws SWRLParseException, SQWRLException
	{
		String query = "swrlb:add(\"4.0\"^^\"xsd:double\", \"2.0\"^^\"xsd:double\", \"2.0\"^^\"xsd:double\") -> sqwrl:select(\"Yes!)\")";
		SQWRLResult result = executeSQWRLQuery("q1", query);

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSWRLBAddWithRawDoubleLiterals() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(4.0, 2.0, 2.0) -> sqwrl:select(\"Yes!\")");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSWRLBAddWithRawDoubleLiteralsAndVariableResult() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(?r, 2.0, 2.0) -> sqwrl:select(?r)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next(); // TODO Returns a float
		}
	}

	// TODO Move to Collections tests
	@Test
	public void TestSQWRLUnion() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT");

		SQWRLResult result = executeSQWRLQuery("q1", ". sqwrl:makeSet(?s1, DDI) ^ sqwrl:makeSet(?s2, AZT) "
				+ " . sqwrl:union(?u, ?s1, ?s2) ^ sqwrl:element(?e, ?u) -> sqwrl:select(?e) ^ sqwrl:orderBy(?e)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLNamedResultValue l = row.get(0).asNamedResult();
			result.next();
		}
	}

	// TODO Move to Collections tests
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

	// TODO Move to Collections tests
	@Test
	public void TestSQWRLCollectionSize() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT");

		SQWRLResult result = executeSQWRLQuery("q1",
				". sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT) . sqwrl:size(?size, ?s1) -> sqwrl:select(?size)");
	}

	// TODO Move to Collections tests
	@Test
	public void TestSQWRLFirst() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT");

		SQWRLResult result = executeSQWRLQuery("q1",
				". sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT) . sqwrl:first(?first, ?s1) -> sqwrl:select(?first)");
	}

	// TODO Move to Collections tests
	@Test
	public void TestSQWRLLast() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT");

		SQWRLResult result = executeSQWRLQuery("q1",
				". sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT) . sqwrl:last(?last, ?s1) -> sqwrl:select(?last)");

	}

	// TODO 2 is detected to be float; Move to Collections tests
	public void TestSQWRLNth() throws SWRLParseException, SQWRLException
	{
		declareOWLNamedIndividuals("DDI", "AZT", "BBT");

		SQWRLResult result = executeSQWRLQuery("q1",
				" . sqwrl:makeBag(?s1, DDI) ^ sqwrl:makeBag(?s1, AZT) ^ sqwrl:makeBag(?s1, BBT) "
						+ " . sqwrl:nth(?second, ?s1, 2) -> sqwrl:select(?second)");

	}

	// TODO  2 is detected to be float;  Move to Collections tests
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
