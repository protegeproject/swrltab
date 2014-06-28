package org.swrltab.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.drools.DroolsFactory;
import org.swrlapi.drools.DroolsSWRLRuleEngineCreator;
import org.swrlapi.ui.controller.SWRLAPIApplicationController;
import org.swrlapi.ui.model.SWRLAPIApplicationModel;
import org.swrlapi.ui.view.SWRLAPIApplicationView;
import org.swrlapi.ui.view.queries.SWRLAPIQueriesView;

/**
 * Standalone SWRLAPI-based application that presents a SQWRL editor and query execution graphical interface.
 * <p>
 * The Drools rule engine is used for query execution.
 * 
 * @see SWRLTabRulesApplicationView, SWRLAPIQueriesView
 */
public class SWRLTabQueriesApplicationView extends JFrame implements SWRLAPIApplicationView
{
	private static final long serialVersionUID = 1L;

	private static final String APPLICATION_NAME = "SWRLTabQueries";
	private static final int APPLICATION_WIDTH = 1000;
	private static final int APPLICATION_HEIGHT = 580;

	private final SWRLAPIQueriesView queriesView;

	public static void main(String[] args)
	{
		String owlFileName = "";

		if (args.length == 1) {
			owlFileName = args[0];
		} else
			Usage();

		try {
			// Create a SWRLAPI OWL ontology from the OWL ontology in the supplied file
			SWRLAPIOWLOntology swrlapiOWLOntology = SWRLAPIFactory.createSWRLAPIOWLOntology(owlFileName);

			// Create a Drools-based query engine
			SWRLRuleEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(swrlapiOWLOntology,
					new DroolsSWRLRuleEngineCreator());

			// Create the application model, supplying it with the ontology and query engine
			SWRLAPIApplicationModel applicationModel = SWRLAPIFactory.createSWRLAPIApplicationModel(swrlapiOWLOntology,
					queryEngine);

			// Create the application controller
			SWRLAPIApplicationController applicationController = SWRLAPIFactory
					.createSWRLAPIApplicationController(applicationModel);

			// Create the application view
			SWRLTabQueriesApplicationView applicationView = new SWRLTabQueriesApplicationView(applicationController);

			// Make the view visible
			applicationView.setVisible(true);

		} catch (RuntimeException e) {
			System.err.println("Error starting application: " + e.getMessage());
			System.exit(-1);
		}
	}

	public SWRLTabQueriesApplicationView(SWRLAPIApplicationController applicationController)
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
		System.err.println("Usage: " + SWRLTabQueriesApplicationView.class.getName() + " <owlFileName>");
		System.exit(1);
	}
}
