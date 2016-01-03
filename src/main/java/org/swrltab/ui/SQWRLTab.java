package org.swrltab.ui;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.swrlapi.exceptions.SWRLAPIException;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.ui.dialog.SWRLRuleEngineDialogManager;
import org.swrlapi.ui.menu.SWRLRuleEngineMenuManager;
import org.swrlapi.ui.model.FileBackedSQWRLQueryEngineModel;
import org.swrlapi.ui.view.SWRLAPIView;
import org.swrlapi.ui.view.queries.SQWRLQueriesView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Optional;

/**
 * Standalone SWRLAPI-based application that presents a SQWRL editor and query execution graphical interface.
 * <p/>
 * The Drools rule engine is used for query execution.
 * <p/>
 * To invoke from Maven put <code>org.swrltab.ui.SQWRLTab</code> in the <code>mainClass</code> element of the
 * <code>exec-maven-plugin</code> plugin configuration in the Maven project POM and run with the <code>exec:java</code>
 * goal.
 *
 * @see SQWRLQueriesView
 */
public class SQWRLTab extends JFrame implements SWRLAPIView
{
  private static final long serialVersionUID = 1L;

  private static final String APPLICATION_NAME = "SQWRLTab";
  private static final int APPLICATION_WINDOW_WIDTH = 1000;
  private static final int APPLICATION_WINDOW_HEIGHT = 580;

  private final @NonNull SQWRLQueriesView queriesView;
  private final @NonNull FileBackedSQWRLQueryEngineModel queryEngineModel;
  private final @NonNull SWRLRuleEngineDialogManager dialogManager;

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

      // Create a SQWRL query engine
      SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontology);

      // Create a query engine model. This is the core application model.
      FileBackedSQWRLQueryEngineModel queryEngineModel = SWRLAPIFactory
          .createFileBackedSQWRLQueryEngineModel(queryEngine, owlFile);

      // Create the dialog manager
      SWRLRuleEngineDialogManager dialogManager = SWRLAPIFactory.createSWRLRuleEngineDialogManager(queryEngineModel);

      // Create the view
      SQWRLTab sqwrlTab = new SQWRLTab(queryEngineModel, dialogManager);

      // Initialize it
      sqwrlTab.initialize();

      // Make the view visible
      sqwrlTab.setVisible(true);

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

  public SQWRLTab(@NonNull FileBackedSQWRLQueryEngineModel queryEngineModel,
      @NonNull SWRLRuleEngineDialogManager dialogManager) throws SWRLAPIException
  {
    super(APPLICATION_NAME);

    this.queriesView = new SQWRLQueriesView(queryEngineModel, dialogManager);
    this.queryEngineModel = queryEngineModel;
    this.dialogManager = dialogManager;
  }

  @Override public void initialize()
  {
    this.queriesView.initialize();

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(queriesView);

    setSize(APPLICATION_WINDOW_WIDTH, APPLICATION_WINDOW_HEIGHT);

    SWRLRuleEngineMenuManager.createSWRLRuleEngineMenus(this, this.queryEngineModel, this.dialogManager);
  }

  @Override public void update()
  {
    this.queriesView.update();
  }

  @Override protected void processWindowEvent(@NonNull WindowEvent e)
  {
    super.processWindowEvent(e);

    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      this.setVisible(false);
      System.exit(0);
    }
  }

  @SuppressWarnings("unused") private static void Usage()
  {
    System.err.println("Usage: " + SQWRLTab.class.getName() + " <owlFileName>");
    System.exit(1);
  }
}
