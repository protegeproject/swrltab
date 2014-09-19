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

// TODO Need to wire up the result tests

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
		declareOWLClassAssertion("Male", "p1");

		SQWRLResult result = executeSQWRLQuery("q1", "Male(?m) -> sqwrl:select(?m)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLNamedResultValue m = row.get(0).asNamedResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLCount() throws SWRLParseException, SQWRLException
	{
		declareOWLClassAssertion("Male", "p1");
		declareOWLClassAssertion("Male", "p2");

		SQWRLResult result = executeSQWRLQuery("q1", "Male(?m) -> sqwrl:count(?m)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue m = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLAvg() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasAge", "10", "xsd:int");
		declareOWLDataPropertyAssertion("p2", "hasAge", "20", "xsd:int");
		declareOWLDataPropertyAssertion("p3", "hasAge", "30", "xsd:int");

		SQWRLResult result = executeSQWRLQuery("q1", "hasAge(?p, ?age)-> sqwrl:avg(?age)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue m = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLMin() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasAge", "10", "xsd:int");
		declareOWLDataPropertyAssertion("p2", "hasAge", "20", "xsd:int");
		declareOWLDataPropertyAssertion("p3", "hasAge", "30", "xsd:int");

		SQWRLResult result = executeSQWRLQuery("q1", "hasAge(?p, ?age)-> sqwrl:min(?age)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue m = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLMax() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasAge", "10", "xsd:int");
		declareOWLDataPropertyAssertion("p2", "hasAge", "20", "xsd:int");
		declareOWLDataPropertyAssertion("p3", "hasAge", "30", "xsd:int");

		SQWRLResult result = executeSQWRLQuery("q1", "hasAge(?p, ?age)-> sqwrl:max(?age)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue m = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLOrderByString() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasName", "Fred", "xsd:string");
		declareOWLDataPropertyAssertion("p2", "hasName", "Bob", "xsd:string");
		declareOWLDataPropertyAssertion("p3", "hasName", "Ann", "xsd:string");

		SQWRLResult result = executeSQWRLQuery("q1", "hasName(?p, ?name)-> sqwrl:select(?name) ^ sqwrl:orderBy(?name)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue m = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLOrderByInt() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasAge", "10", "xsd:int");
		declareOWLDataPropertyAssertion("p2", "hasAge", "20", "xsd:int");
		declareOWLDataPropertyAssertion("p3", "hasAge", "30", "xsd:int");

		SQWRLResult result = executeSQWRLQuery("q1", "hasAge(?p, ?age)-> sqwrl:select(?age) ^ sqwrl:orderBy(?age)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue m = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLOrderByDescendingInt() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasAge", "10", "xsd:int");
		declareOWLDataPropertyAssertion("p2", "hasAge", "20", "xsd:int");
		declareOWLDataPropertyAssertion("p3", "hasAge", "30", "xsd:int");

		SQWRLResult result = executeSQWRLQuery("q1",
				"hasAge(?p, ?age)-> sqwrl:select(?age) ^ sqwrl:orderByDescending(?age)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue m = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSQWRLOrderByDescendingString() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasName", "Fred", "xsd:string");
		declareOWLDataPropertyAssertion("p2", "hasName", "Bob", "xsd:string");
		declareOWLDataPropertyAssertion("p3", "hasName", "Ann", "xsd:string");

		SQWRLResult result = executeSQWRLQuery("q1",
				"hasName(?p, ?name)-> sqwrl:select(?name) ^ sqwrl:orderByDescending(?name)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue m = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithQualifiedLongLiterals() throws SWRLParseException, SQWRLException
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
	public void TestSWRLCoreAddBuiltInWithRawLongLiterals() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(4, 2, 2) -> sqwrl:select(\"Yes!\")");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithRawLongLiteralsAndVariableResult() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(?r, 2, 2) -> sqwrl:select(?r)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithQualifiedDoubleLiterals() throws SWRLParseException, SQWRLException
	{
		String query = "swrlb:add(\"4.0\"^^\"xsd:double\", \"2.0\"^^\"xsd:double\", \"2.0\"^^\"xsd:double\")"
				+ " -> sqwrl:select(\"Yes!)\")";
		SQWRLResult result = executeSQWRLQuery("q1", query);

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithRawDoubleLiterals() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(4.0, 2.0, 2.0) -> sqwrl:select(\"Yes!\")");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithRawDoubleLiteralsAndVariableResult() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(?r, 2.0, 2.0) -> sqwrl:select(?r)");

		while (result.hasNext()) {
			List<SQWRLResultValue> row = result.getRow();
			SQWRLLiteralResultValue l = row.get(0).asLiteralResult();
			result.next();
		}
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
