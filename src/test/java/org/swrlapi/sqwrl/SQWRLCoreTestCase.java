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
import org.swrlapi.test.SWRLAPITestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SQWRLCoreTestCase extends SWRLAPITestBase
{
	String Namespace = "http://swrlapi.org/ontologies/SQWRLCoreTests.owl#";
	double DELTA = 1e-6;

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

		assertTrue(result.next());
		assertEquals(result.getIndividual(0).getShortName(), "p1");
	}

	@Test
	public void TestClassAtomInAntecedentWithVariable() throws SWRLParseException, SQWRLException
	{
		declareOWLClassAssertion("Male", "p1");

		SQWRLResult result = executeSQWRLQuery("q1", "Male(?m) -> sqwrl:select(?m)");

		assertTrue(result.next());

		assertEquals(result.getIndividual("m").getShortName(), "p1");
	}

	@Test
	public void TestSQWRLCount() throws SWRLParseException, SQWRLException
	{
		declareOWLClassAssertion("Male", "p1");
		declareOWLClassAssertion("Male", "p2");

		SQWRLResult result = executeSQWRLQuery("q1", "Male(?m) -> sqwrl:count(?m)");

		assertTrue(result.next());
		SQWRLLiteralResultValue literal = result.getLiteral(0);
		assertTrue(literal.isInt());
		assertEquals(literal.getInt(), 2);
	}

	@Test
	public void TestSQWRLAvg() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasAge", "10", "xsd:int");
		declareOWLDataPropertyAssertion("p2", "hasAge", "20", "xsd:int");
		declareOWLDataPropertyAssertion("p3", "hasAge", "30", "xsd:int");

		SQWRLResult result = executeSQWRLQuery("q1", "hasAge(?p, ?age)-> sqwrl:avg(?age)");

		assertTrue(result.next());
		SQWRLLiteralResultValue literal = result.getLiteral(0);
		assertTrue(literal.isDouble());
		assertEquals(literal.getDouble(), 20.0, DELTA);
	}

	@Test
	public void TestSQWRLMin() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasAge", "10", "xsd:int");
		declareOWLDataPropertyAssertion("p2", "hasAge", "20", "xsd:int");
		declareOWLDataPropertyAssertion("p3", "hasAge", "30", "xsd:int");

		SQWRLResult result = executeSQWRLQuery("q1", "hasAge(?p, ?age)-> sqwrl:min(?age)");

		assertTrue(result.next());
		SQWRLLiteralResultValue literal = result.getLiteral(0);
		assertTrue(literal.isInt());
		assertEquals(literal.getInt(), 10);
	}

	@Test
	public void TestSQWRLMax() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasAge", "10", "xsd:int");
		declareOWLDataPropertyAssertion("p2", "hasAge", "20", "xsd:int");
		declareOWLDataPropertyAssertion("p3", "hasAge", "30", "xsd:int");

		SQWRLResult result = executeSQWRLQuery("q1", "hasAge(?p, ?age)-> sqwrl:max(?age)");

		assertTrue(result.next());
		SQWRLLiteralResultValue literal = result.getLiteral(0);
		assertTrue(literal.isInt());
		assertEquals(literal.getInt(), 30);
	}

	@Test
	public void TestSQWRLOrderByInt() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasAge", "20", "xsd:int");
		declareOWLDataPropertyAssertion("p2", "hasAge", "10", "xsd:int");
		declareOWLDataPropertyAssertion("p3", "hasAge", "30", "xsd:int");

		SQWRLResult result = executeSQWRLQuery("q1", "hasAge(?p, ?age)-> sqwrl:select(?p, ?age) ^ sqwrl:orderBy(?age)");

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p2");
		assertTrue(result.getLiteral("age").isInt());
		assertEquals(result.getLiteral("age").getInt(), 10);

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p1");
		assertTrue(result.getLiteral("age").isInt());
		assertEquals(result.getLiteral("age").getInt(), 20);

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p3");
		assertTrue(result.getLiteral("age").isInt());
		assertEquals(result.getLiteral("age").getInt(), 30);
	}

	@Test
	public void TestSQWRLOrderByDescendingInt() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasAge", "20", "xsd:int");
		declareOWLDataPropertyAssertion("p2", "hasAge", "10", "xsd:int");
		declareOWLDataPropertyAssertion("p3", "hasAge", "30", "xsd:int");

		SQWRLResult result = executeSQWRLQuery("q1",
				"hasAge(?p, ?age)-> sqwrl:select(?p, ?age) ^ sqwrl:orderByDescending(?age)");

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p3");
		assertTrue(result.getLiteral("age").isInt());
		assertEquals(result.getLiteral("age").getInt(), 30);

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p1");
		assertTrue(result.getLiteral("age").isInt());
		assertEquals(result.getLiteral("age").getInt(), 20);

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p2");
		assertTrue(result.getLiteral("age").isInt());
		assertEquals(result.getLiteral("age").getInt(), 10);
	}

	@Test
	public void TestSQWRLOrderByString() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasName", "Fred", "xsd:string");
		declareOWLDataPropertyAssertion("p2", "hasName", "Bob", "xsd:string");
		declareOWLDataPropertyAssertion("p3", "hasName", "Ann", "xsd:string");

		SQWRLResult result = executeSQWRLQuery("q1", "hasName(?p, ?name)-> sqwrl:select(?p, ?name) ^ sqwrl:orderBy(?name)");

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p3");
		assertTrue(result.getLiteral("name").isString());
		assertEquals(result.getLiteral("name").getString(), "Ann");

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p2");
		assertTrue(result.getLiteral("name").isString());
		assertEquals(result.getLiteral("name").getString(), "Bob");

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p1");
		assertTrue(result.getLiteral("name").isString());
		assertEquals(result.getLiteral("name").getString(), "Fred");
	}

	@Test
	public void TestSQWRLOrderByDescendingString() throws SWRLParseException, SQWRLException
	{
		declareOWLDataPropertyAssertion("p1", "hasName", "Fred", "xsd:string");
		declareOWLDataPropertyAssertion("p2", "hasName", "Bob", "xsd:string");
		declareOWLDataPropertyAssertion("p3", "hasName", "Ann", "xsd:string");

		SQWRLResult result = executeSQWRLQuery("q1",
				"hasName(?p, ?name)-> sqwrl:select(?p, ?name) ^ sqwrl:orderByDescending(?name)");

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p1");
		assertTrue(result.getLiteral("name").isString());
		assertEquals(result.getLiteral("name").getString(), "Fred");

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p2");
		assertTrue(result.getLiteral("name").isString());
		assertEquals(result.getLiteral("name").getString(), "Bob");

		assertTrue(result.next());
		assertTrue(result.getIndividual("p").isIndividual());
		assertEquals(result.getIndividual("p").getShortName(), "p3");
		assertTrue(result.getLiteral("name").isString());
		assertEquals(result.getLiteral("name").getString(), "Ann");
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithQualifiedLongLiterals() throws SWRLParseException, SQWRLException
	{
		String query = "swrlb:add(\"4\"^^\"xsd:long\", \"2\"^^\"xsd:long\", \"2\"^^\"xsd:long\") -> sqwrl:select(\"Yes\")";
		SQWRLResult result = executeSQWRLQuery("q1", query);

		assertTrue(result.next());
		SQWRLLiteralResultValue literal = result.getLiteral(0);
		assertTrue(literal.isString());
		assertEquals(literal.getString(), "Yes");
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithRawIntLiterals() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(4, 2, 2) -> sqwrl:select(\"Yes\")");

		assertTrue(result.next());
		SQWRLLiteralResultValue literal = result.getLiteral(0);
		assertTrue(literal.isString());
		assertEquals(literal.getString(), "Yes");
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithRawIntLiteralsAndVariableResult() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(?r, 2, 2) -> sqwrl:select(?r)");

		assertTrue(result.next());
		SQWRLLiteralResultValue literal = result.getLiteral("r");
		assertTrue(literal.isInt());
		assertEquals(literal.getInt(), 4);
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithQualifiedDoubleLiterals() throws SWRLParseException, SQWRLException
	{
		String query = "swrlb:add(\"4.0\"^^\"xsd:double\", \"2.0\"^^\"xsd:double\", \"2.0\"^^\"xsd:double\")"
				+ " -> sqwrl:select(\"Yes\")";
		SQWRLResult result = executeSQWRLQuery("q1", query);

		assertTrue(result.next());
		SQWRLLiteralResultValue literal = result.getLiteral(0);
		assertTrue(literal.isString());
		assertEquals(literal.getString(), "Yes");
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithRawDoubleLiterals() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(4.1, 2.1, 2.0) -> sqwrl:select(\"Yes\")");

		assertTrue(result.next());
		SQWRLLiteralResultValue literal = result.getLiteral(0);
		assertTrue(literal.isString());
		assertEquals(literal.getString(), "Yes");
	}

	@Test
	public void TestSWRLCoreAddBuiltInWithRawFloatLiteralsAndVariableResult() throws SWRLParseException, SQWRLException
	{
		SQWRLResult result = executeSQWRLQuery("q1", "swrlb:add(?r, 2.1, 2.0) -> sqwrl:select(?r)");

		assertTrue(result.next());
		SQWRLLiteralResultValue literal = result.getLiteral("r");
		assertTrue(literal.isFloat());
		assertEquals(literal.getFloat(), 4.1, DELTA);
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
