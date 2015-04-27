package org.swrltab.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.drools.core.DroolsFactory;
import org.swrlapi.exceptions.SWRLAPIException;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.ui.dialog.SWRLAPIDialogManager;
import org.swrlapi.ui.model.SQWRLQueryEngineModel;
import org.swrlapi.ui.view.SWRLAPIView;
import org.swrlapi.ui.view.queries.SWRLAPIQueriesView;

/**
 * Standalone SWRLAPI-based application that presents a SQWRL editor and query execution graphical interface.
 * <p>
 * The Drools rule engine is used for query execution.
 * <p>
 * To invoke from Maven put <code>org.swrltab.ui.SQWRLTab</code> in the <code>mainClass</code> element of the
 * <code>exec-maven-plugin</code> plugin configuration in the Maven project POM and run with the <code>exec:java</code>
 * goal.
 *
 * @see org.swrlapi.ui.view.queries.SWRLAPIQueriesView
 */
public class SQWRLTab extends JFrame implements SWRLAPIView
{
	private static final long serialVersionUID = 1L;

	private static final String APPLICATION_NAME = "SQWRLTab";
	private static final int APPLICATION_WINDOW_WIDTH = 1000;
	private static final int APPLICATION_WINDOW_HEIGHT = 580;

	private final SWRLAPIQueriesView queriesView;

	public static void main(String[] args)
	{
		String owlFileName = SQWRLTab.class.getClassLoader().getResource("projects/SWRLSimple.owl").getFile();
		File owlFile = new File(owlFileName);

		try {
			// Create a SWRLAPI OWL ontology from the OWL ontology in the supplied file
			SWRLAPIOWLOntology swrlapiOWLOntology = SWRLAPIFactory.createSWRLAPIOntology(owlFile);

			// Create a Drools-based query engine
			SQWRLQueryEngine queryEngine = swrlapiOWLOntology
					.createSQWRLQueryEngine(DroolsFactory.getSWRLRuleEngineCreator());

			// Create the query engine model, supplying it with the ontology and query engine
			SQWRLQueryEngineModel sqwrlQueryEngineModel = SWRLAPIFactory.createSQWRLQueryEngineModel(queryEngine);

			// Create the dialog manager
			SWRLAPIDialogManager dialogManager = SWRLAPIFactory
					.createSWRLAPIDialogManager(sqwrlQueryEngineModel);

			// Create the view
			SQWRLTab sqwrlTab = new SQWRLTab(sqwrlQueryEngineModel, dialogManager);

			// Make the view visible
			sqwrlTab.setVisible(true);

		} catch (RuntimeException e) {
			System.err.println("Error starting application: " + e.getMessage());
			System.exit(-1);
		}
	}

	public SQWRLTab(SQWRLQueryEngineModel sqwrlQueryEngineModel, SWRLAPIDialogManager applicationDialogManager)
			throws SWRLAPIException
	{
		super(APPLICATION_NAME);
		this.queriesView = createAndAddSWRLAPIQueriesView(sqwrlQueryEngineModel, applicationDialogManager);
	}

	@Override
	public void update()
	{
		this.queriesView.update();
	}

	private SWRLAPIQueriesView createAndAddSWRLAPIQueriesView(SQWRLQueryEngineModel sqwrlQueryEngineModel,
			SWRLAPIDialogManager applicationDialogManager) throws SWRLAPIException
	{
		Icon ruleEngineIcon = DroolsFactory.getSWRLRuleEngineIcon();
		SWRLAPIQueriesView queriesView = new SWRLAPIQueriesView(sqwrlQueryEngineModel, applicationDialogManager,
				ruleEngineIcon);
		Container contentPane = getContentPane();

		contentPane.setLayout(new BorderLayout());
		contentPane.add(queriesView);
		setSize(APPLICATION_WINDOW_WIDTH, APPLICATION_WINDOW_HEIGHT);

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

	@SuppressWarnings("unused")
	private static void Usage()
	{
		System.err.println("Usage: " + SQWRLTab.class.getName() + " <owlFileName>");
		System.exit(1);
	}
}
