package org.swrltab.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.drools.core.DroolsFactory;
import org.swrlapi.exceptions.SWRLAPIException;
import org.swrlapi.ui.dialog.SWRLAPIApplicationDialogManager;
import org.swrlapi.ui.model.SWRLAPIApplicationModel;
import org.swrlapi.ui.view.SWRLAPIApplicationView;
import org.swrlapi.ui.view.rules.SWRLAPIRulesView;

/**
 * Standalone SWRLAPI-based application that presents a SWRL editor and rule execution graphical interface.
 * <p/>
 * The Drools rule engine is used for rule execution.
 * <p/>
 * To invoke from Maven put <code>org.swrltab.ui.SWRLTab</code> in the <code>mainClass</code> element of the
 * <code>exec-maven-plugin</code> plugin configuration in the Maven project POM and run with the <code>exec:java</code>
 * goal.
 *
 * @see org.swrlapi.ui.view.rules.SWRLAPIRulesView
 */
public class SWRLTab extends JFrame implements SWRLAPIApplicationView
{
	private static final long serialVersionUID = 1L;

	private static final String APPLICATION_NAME = "SWRLTabRules";
	private static final int APPLICATION_WINDOW_WIDTH = 1000;
	private static final int APPLICATION_WINDOW_HEIGHT = 580;

	private final SWRLAPIRulesView rulesView;

	public static void main(String[] args)
	{
		if (args.length != 1)
			Usage();

		String owlFileName = args[0];
		File owlFile = new File(owlFileName);

		try {
			// Create a SWRLAPI OWL ontology from the OWL ontology in the supplied file
			SWRLAPIOWLOntology swrlapiOWLOntology = SWRLAPIFactory.createSWRLAPIOntology(owlFile);

			// Create a Drools-based rule engine
			SWRLRuleEngine ruleEngine = swrlapiOWLOntology.createSWRLRuleEngine(DroolsFactory.getSWRLRuleEngineCreator());

			// Create the application model, supplying it with the ontology and rule engine
			SWRLAPIApplicationModel applicationModel = SWRLAPIFactory.createSWRLAPIApplicationModel(swrlapiOWLOntology,
					ruleEngine);

			// Create the application controller
			SWRLAPIApplicationDialogManager dialogManager = SWRLAPIFactory
					.createSWRLAPIApplicationDialogManager(applicationModel);

			// Create the application view
			SWRLTab applicationView = new SWRLTab(applicationModel, dialogManager);

			// Make the view visible
			applicationView.setVisible(true);
		} catch (RuntimeException e) {
			System.err.println("Error starting application: " + e.getMessage());
			System.exit(-1);
		}
	}

	public SWRLTab(SWRLAPIApplicationModel applicationModel, SWRLAPIApplicationDialogManager applicationDialogManager)
			throws SWRLAPIException
	{
		super(APPLICATION_NAME);

		this.rulesView = createAndAddSWRLAPIRulesView(applicationModel, applicationDialogManager);
	}

	@Override
	public void update()
	{
		this.rulesView.update();
	}

	@Override
	public String getApplicationName()
	{
		return APPLICATION_NAME;
	}

	private SWRLAPIRulesView createAndAddSWRLAPIRulesView(SWRLAPIApplicationModel applicationModel,
			SWRLAPIApplicationDialogManager applicationDialogManager) throws SWRLAPIException
	{
		Icon ruleEngineIcon = DroolsFactory.getSWRLRuleEngineIcon();
		SWRLAPIRulesView rulesView = new SWRLAPIRulesView(applicationModel, applicationDialogManager, ruleEngineIcon);
		Container contentPane = getContentPane();

		contentPane.setLayout(new BorderLayout());
		contentPane.add(rulesView);
		setSize(APPLICATION_WINDOW_WIDTH, APPLICATION_WINDOW_HEIGHT);

		return rulesView;
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
		System.err.println("Usage: " + SWRLTab.class.getName() + " <owlFileName>");
		System.exit(1);
	}
}
