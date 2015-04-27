package org.swrltab.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.swrlapi.core.SWRLAPIFactory;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.drools.core.DroolsFactory;
import org.swrlapi.exceptions.SWRLAPIException;
import org.swrlapi.ui.action.OpenAction;
import org.swrlapi.ui.dialog.SWRLRuleEngineDialogManager;
import org.swrlapi.ui.model.SWRLRuleEngineModel;
import org.swrlapi.ui.view.SWRLAPIView;
import org.swrlapi.ui.view.rules.SWRLAPIRulesView;

/**
 * Standalone SWRLAPI-based application that presents a SWRL editor and rule execution graphical interface.
 * <p>
 * The Drools rule engine is used for rule execution.
 * <p>
 * To invoke from Maven put <code>org.swrltab.ui.SWRLTab</code> in the <code>mainClass</code> element of the
 * <code>exec-maven-plugin</code> plugin configuration in the Maven project POM and run with the <code>exec:java</code>
 * goal.
 *
 * @see org.swrlapi.ui.view.rules.SWRLAPIRulesView
 */
public class SWRLTab extends JFrame implements SWRLAPIView
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

			// Create the rule engine model, supplying it with the ontology and rule engine
			SWRLRuleEngineModel swrlRuleEngineModel = SWRLAPIFactory.createSWRLRuleEngineModel(ruleEngine);

			// Create the rule engine controller
			SWRLRuleEngineDialogManager dialogManager = SWRLAPIFactory.createSWRLRuleEngineDialogManager(swrlRuleEngineModel);

			// Create the view
			SWRLTab swrlTab = new SWRLTab(swrlRuleEngineModel, dialogManager);

			// Make the view visible
			swrlTab.setVisible(true);
		} catch (RuntimeException e) {
			System.err.println("Error starting application: " + e.getMessage());
			System.exit(-1);
		}
	}

	public SWRLTab(SWRLRuleEngineModel swrlRuleEngineModel, SWRLRuleEngineDialogManager applicationDialogManager)
			throws SWRLAPIException
	{
		super(APPLICATION_NAME);

		this.rulesView = createAndAddSWRLAPIRulesView(swrlRuleEngineModel, applicationDialogManager);

		createMenus();
	}

	@Override
	public void update()
	{
		this.rulesView.update();
	}

	private SWRLAPIRulesView createAndAddSWRLAPIRulesView(SWRLRuleEngineModel applicationModel,
			SWRLRuleEngineDialogManager applicationDialogManager) throws SWRLAPIException
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

	private void createMenus()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new OpenAction());
		menu.add(openItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}

	private static void Usage()
	{
		System.err.println("Usage: " + SWRLTab.class.getName() + " <owlFileName>");
		System.exit(1);
	}
}
