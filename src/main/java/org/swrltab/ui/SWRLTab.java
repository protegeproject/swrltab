package org.swrltab.ui;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.exceptions.SWRLAPIException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.ui.dialog.SWRLRuleEngineDialogManager;
import org.swrlapi.ui.menu.SWRLRuleEngineMenuManager;
import org.swrlapi.ui.model.FileBackedSWRLRuleEngineModel;
import org.swrlapi.ui.view.SWRLAPIView;
import org.swrlapi.ui.view.rules.SWRLRulesView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Optional;

/**
 * Standalone SWRLAPI-based application that presents a SWRL editor and rule execution graphical interface.
 * <p/>
 * The Drools rule engine is used for rule execution.
 * <p/>
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
  @NonNull private final FileBackedSWRLRuleEngineModel ruleEngineModel;
  @NonNull private final SWRLRuleEngineDialogManager dialogManager;

  public static void main(@NonNull String[] args)
  {
    if (args.length > 1)
      Usage();

    Optional<@NonNull String> owlFilename = args.length == 0 ? Optional.<@NonNull String>empty() : Optional.of(args[0]);
    Optional<@NonNull File> owlFile = (owlFilename != null && owlFilename.isPresent()) ?
      Optional.of(new File(owlFilename.get())) :
      Optional.<@NonNull File>empty();

    try {
      // Create an OWL ontology using the OWLAPI
      OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
      OWLOntology ontology = owlFile.isPresent() ?
          ontologyManager.loadOntologyFromOntologyDocument(owlFile.get()) :
          ontologyManager.createOntology();
      DefaultPrefixManager prefixManager = new DefaultPrefixManager();
      OWLDocumentFormat format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);

      if (format.isPrefixOWLOntologyFormat())
        prefixManager.copyPrefixesFrom(format.asPrefixOWLOntologyFormat().getPrefixName2PrefixMap());

      // Create a rule engine
      SWRLRuleEngine ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology, prefixManager);

      // Create a rule engine model. This is the core application model.
      FileBackedSWRLRuleEngineModel ruleEngineModel = SWRLAPIFactory
          .createFileBackedSWRLRuleEngineModel(ruleEngine, owlFile);

      // Create a rule editor dialog manager
      SWRLRuleEngineDialogManager dialogManager = SWRLAPIFactory.createSWRLRuleEngineDialogManager(ruleEngineModel);

      // Create the view
      SWRLTab swrlTab = new SWRLTab(ruleEngineModel, dialogManager);

      // Initialize it
      swrlTab.initialize();

      // Make the view visible
      swrlTab.setVisible(true);

    } catch (OWLOntologyCreationException e) {
      if (owlFile.isPresent())
        System.err.println("Error creating OWL ontology from file " + owlFile.get().getAbsolutePath() + ": " + (
            e.getMessage() != null ? e.getMessage() : ""));
      else
        System.err.println("Error creating OWL ontology: " + (e.getMessage() != null ? e.getMessage() : ""));
      System.exit(-1);
    } catch (RuntimeException e) {
      System.err.println("Error starting application: " + (e.getMessage() != null ? e.getMessage() : ""));
      System.exit(-1);
    }
  }

  public SWRLTab(@NonNull FileBackedSWRLRuleEngineModel ruleEngineModel,
      @NonNull SWRLRuleEngineDialogManager dialogManager) throws SWRLAPIException
  {
    super(APPLICATION_NAME);

    this.ruleEngineModel = ruleEngineModel;
    this.dialogManager = dialogManager;
    this.rulesView = new SWRLRulesView(ruleEngineModel, dialogManager);
  }

  @Override public void initialize()
  {
    this.rulesView.initialize();

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(rulesView);

    setSize(APPLICATION_WINDOW_WIDTH, APPLICATION_WINDOW_HEIGHT);

    SWRLRuleEngineMenuManager.createSWRLRuleEngineMenus(this, ruleEngineModel, dialogManager);
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

  private static void Usage()
  {
    System.err.println("Usage: " + SWRLTab.class.getName() + " [ <owlFileName> ]");
    System.exit(1);
  }
}
