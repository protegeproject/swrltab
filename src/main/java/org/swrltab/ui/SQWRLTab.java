package org.swrltab.ui;

import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.drools.DroolsFactory;
import org.swrlapi.drools.DroolsSWRLRuleEngineCreator;
import org.swrlapi.exceptions.SWRLAPIException;
import org.swrlapi.ui.controller.SWRLAPIApplicationController;
import org.swrlapi.ui.model.SWRLAPIApplicationModel;
import org.swrlapi.ui.view.SWRLAPIApplicationView;
import org.swrlapi.ui.view.queries.SWRLAPIQueriesView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Standalone SWRLAPI-based application that presents a SQWRL editor and query execution graphical interface.
 * <p/>
 * The Drools rule engine is used for query execution.
 * <p/>
 * To invoke from Maven put <code>org.swrltab.ui.SQWRLTab</code> in the <code>mainClass</code> element of
 * the <code>exec-maven-plugin</code> plugin configuration in the Maven project POM and run with
 * the <code>exec:java</code> goal.
 *
 * @see SWRLTab, SWRLAPIQueriesView
 */
public class SQWRLTab extends JFrame implements SWRLAPIApplicationView
{
	private static final long serialVersionUID = 1L;

	private static final String APPLICATION_NAME = "SQWRLTab";
	private static final int APPLICATION_WIDTH = 1000;
	private static final int APPLICATION_HEIGHT = 580;

	private final SWRLAPIQueriesView queriesView;

	public static void main(String[] args)
	{
		// TODO Hard code temporarily for testing. SWRLCoreTests, SQWRLCollectionsTests, SQWRLCoreTests, SWRLInferenceTests
		String owlFileName = SWRLTab.class.getClassLoader().getResource("projects/SWRLInferenceTests.owl").getFile();
		File owlFile = new File(owlFileName);

		try {
			// Create a SWRLAPI OWL ontology from the OWL ontology in the supplied file
			SWRLAPIOWLOntology swrlapiOWLOntology = SWRLAPIFactory.createOntology(owlFile);

			// Create a Drools-based query engine
			SWRLRuleEngine queryEngine = SWRLAPIFactory.createQueryEngine(swrlapiOWLOntology,
					new DroolsSWRLRuleEngineCreator());

			// Create the application model, supplying it with the ontology and query engine
			SWRLAPIApplicationModel applicationModel = SWRLAPIFactory.createApplicationModel(swrlapiOWLOntology, queryEngine);

			// Create the application controller
			SWRLAPIApplicationController applicationController = SWRLAPIFactory.createApplicationController(applicationModel);

			// Create the application view
			SQWRLTab applicationView = new SQWRLTab(applicationController);

			// Make the view visible
			applicationView.setVisible(true);

		} catch (SWRLAPIException e) {
			System.err.println("Error starting application: " + e.getMessage());
			System.exit(-1);
		}
	}

	public SQWRLTab(SWRLAPIApplicationController applicationController) throws SWRLAPIException
	{
		super(APPLICATION_NAME);
		this.queriesView = createAndAddSWRLAPIQueriesView(applicationController);
	}

	@Override
	public String getApplicationName()
	{
		return APPLICATION_NAME;
	}

	@Override
	public void update()
	{
		this.queriesView.update();
	}

	private SWRLAPIQueriesView createAndAddSWRLAPIQueriesView(SWRLAPIApplicationController applicationController)
			throws SWRLAPIException
	{
		Icon ruleEngineIcon = DroolsFactory.getSWRLRuleEngineIcon();
		SWRLAPIQueriesView queriesView = new SWRLAPIQueriesView(applicationController, ruleEngineIcon);
		Container contentPane = getContentPane();

		contentPane.setLayout(new BorderLayout());
		contentPane.add(queriesView);
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);

		return queriesView;
	}

	@Override
	protected void processWindowEvent(WindowEvent e)
	{
		super.processWindowEvent(e);

		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.setVisible(false);
			System.exit(0);
		}
	}

	private static void Usage()
	{
		System.err.println("Usage: " + SQWRLTab.class.getName() + " <owlFileName>");
		System.exit(1);
	}
}
