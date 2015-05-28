package org.swrltab.ui;

import checkers.nullness.quals.NonNull;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.SWRLAPIException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.ui.action.CloseAction;
import org.swrlapi.ui.action.OpenAction;
import org.swrlapi.ui.action.QuitAction;
import org.swrlapi.ui.action.SaveAction;
import org.swrlapi.ui.action.SaveAsAction;
import org.swrlapi.ui.dialog.SWRLAPIDialogManager;
import org.swrlapi.ui.model.FileBackedOWLOntologyModel;
import org.swrlapi.ui.model.SWRLRuleEngineModel;
import org.swrlapi.ui.view.SWRLAPIView;
import org.swrlapi.ui.view.rules.SWRLRulesView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Standalone SWRLAPI-based application that presents a SWRL editor and rule execution graphical interface.
 * <p>
 * The Drools rule engine is used for rule execution.
 * <p>
 * To invoke from Maven put <code>org.swrltab.ui.SWRLTab</code> in the <code>mainClass</code> element of the
 * <code>exec-maven-plugin</code> plugin configuration in the Maven project POM and run with the <code>exec:java</code>
 * goal.
 *
 * @see org.swrlapi.ui.view.rules.SWRLRulesView
 */
public class SWRLTab extends JFrame implements SWRLAPIView
{
  private static final long serialVersionUID = 1L;

  private static final String APPLICATION_NAME = "SWRLTab";
  private static final int APPLICATION_WINDOW_WIDTH = 1000;
  private static final int APPLICATION_WINDOW_HEIGHT = 580;

  @NonNull private final SWRLRulesView rulesView;

  public static void main(@NonNull String[] args)
  {
    if (args.length != 1)
      Usage();

    String owlFileName = args[0];
    File owlFile = new File(owlFileName);

    try {
      // Create an OWL ontology using the OWLAPI
      OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
      OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(owlFile);

      // Create a rule engine and get its icon
      SWRLRuleEngine ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology);
      Icon ruleEngineIcon = ruleEngine.getRuleEngineIcon();

      // Create the rule engine model, supplying it with the rule engine
      SWRLRuleEngineModel swrlRuleEngineModel = SWRLAPIFactory.createSWRLRuleEngineModel(ruleEngine);

      // Create the application dialog manager
      SWRLAPIDialogManager dialogManager = SWRLAPIFactory.createDialogManager(swrlRuleEngineModel);

      FileBackedOWLOntologyModel ontologyModel = SWRLAPIFactory
        .createFileBackedOWLOntologyModel(ontology, swrlRuleEngineModel, owlFile);

      // Create the view
      SWRLTab swrlTab = new SWRLTab(ontologyModel, dialogManager, ruleEngineIcon);

      // Make the view visible
      swrlTab.setVisible(true);

    } catch (OWLOntologyCreationException e) {
      System.err.println("Error creating OWL ontology from file " + owlFile.getAbsolutePath() + ": " + e.getMessage());
      System.exit(-1);
    } catch (RuntimeException e) {
      System.err.println("Error starting application: " + e.getMessage());
      System.exit(-1);
    }
  }

  public SWRLTab(@NonNull FileBackedOWLOntologyModel ontologyModel, @NonNull SWRLAPIDialogManager dialogManager,
    @NonNull Icon ruleEngineIcon) throws SWRLAPIException
  {
    super(APPLICATION_NAME);

    this.rulesView = new SWRLRulesView(ontologyModel.getSWRLRuleEngineModel(), dialogManager, ruleEngineIcon);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(rulesView);

    setSize(APPLICATION_WINDOW_WIDTH, APPLICATION_WINDOW_HEIGHT);

    createMenus(this, ontologyModel, dialogManager);
  }

  @Override public void update()
  {
    this.rulesView.update();
  }

  @Override protected void processWindowEvent(@NonNull WindowEvent e)
  {
    super.processWindowEvent(e);

    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      this.setVisible(false);
      System.exit(0);
    }
  }

  private void createMenus(@NonNull JFrame parent, @NonNull FileBackedOWLOntologyModel ontologyModel,
    @NonNull SWRLAPIDialogManager dialogManager)
  {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("File");
    JMenuItem openItem = new JMenuItem(OpenAction.TITLE);
    JMenuItem saveItem = new JMenuItem(SaveAction.TITLE);
    JMenuItem saveAsItem = new JMenuItem(SaveAsAction.TITLE);
    JMenuItem closeItem = new JMenuItem(CloseAction.TITLE);
    JMenuItem quitItem = new JMenuItem(QuitAction.TITLE);

    openItem.addActionListener(new OpenAction(parent, ontologyModel, dialogManager));
    saveItem.addActionListener(new SaveAction(parent, ontologyModel, dialogManager));
    saveAsItem.addActionListener(new SaveAsAction(parent, ontologyModel, dialogManager));
    closeItem.addActionListener(new CloseAction(parent, dialogManager));
    quitItem.addActionListener(new QuitAction(parent, dialogManager));

    menu.add(openItem);
    menu.add(saveItem);
    menu.add(saveAsItem);
    menu.add(closeItem);
    menu.add(quitItem);

    menuBar.add(menu);
    parent.setJMenuBar(menuBar);
  }

  private static void Usage()
  {
    System.err.println("Usage: " + SWRLTab.class.getName() + " <owlFileName>");
    System.exit(1);
  }
}
