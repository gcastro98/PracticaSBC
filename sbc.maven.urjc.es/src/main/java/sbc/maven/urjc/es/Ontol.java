package sbc.maven.urjc.es;

import java.io.File;

import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;

public class Ontol {
	
	String ontoIRI;
	OWLOntologyManager manager;
	OWLDataFactory factory;
	File owlFile;
	DefaultPrefixManager prefixManager;
	OWLOntology ontology;
	
	public Ontol(String ontoPath, String ontoIRI) {
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		owlFile = new File(ontoPath);
		this.ontoIRI=ontoIRI;
		prefixManager = new DefaultPrefixManager();
	}
	
	public void createOntology() {
		try {
			ontology = manager.createOntology(IRI.create("http://www.movieontology.org/2010/01/movieontology.owl"));
		}catch (OWLOntologyCreationException e){
			System.out.println("Ontología no creada: "+e.getMessage());
		}
	}

	public void loadOntology() {
		try {

			ontology = manager.loadOntologyFromOntologyDocument(new File("MovieOntology.owl"));
		}catch (OWLOntologyCreationException e){
			System.out.println("Ontología no creada: "+e.getMessage());
		}
	}

	public void saveOntology() {
		try {
			manager.saveOntology(ontology,IRI.create(owlFile));
		}catch (Exception e){
			System.out.println("Ontología no guardada: "+e.getMessage());
		}
	}
	
	public void addAxioma(String name) {
		try {
			OWLClass clase = factory.getOWLClass(name, prefixManager);
			OWLAxiom axioma = factory.getOWLDeclarationAxiom(clase);
			manager.addAxiom(ontology, axioma);
		}catch (Exception e){
			System.out.println("Axioma no guardado: "+e.getMessage());
		}
	}
	
	public void addsubclass(String subClass, String superClass) {
		try {
			OWLClass subclase = factory.getOWLClass(subClass, prefixManager);
			OWLClass superclase = factory.getOWLClass(superClass, prefixManager);
			OWLSubClassOfAxiom axioma = factory.getOWLSubClassOfAxiom(subclase, superclase);
			manager.addAxiom(ontology, axioma);
		}catch (Exception e){
			System.out.println("Error al añadir subclase no guardada: "+e.getMessage());
		}
	}
	
	public void addProperty(String prop) {
		try {
			OWLObjectProperty property = factory.getOWLObjectProperty(prop,prefixManager);
			OWLAxiom axioma = factory.getOWLDeclarationAxiom(property);
			manager.addAxiom(ontology, axioma);

		}catch (Exception e){
			System.out.println("Error al crear la propiedad: "+e.getMessage());
		}
	}
	
	public void addProperty(String prop,String dominio, String rango) {
		try {
			OWLObjectProperty property = factory.getOWLObjectProperty(prop,prefixManager);
			OWLClass dom = factory.getOWLClass(dominio, prefixManager);
			OWLClass rang = factory.getOWLClass(rango, prefixManager);
			OWLObjectPropertyDomainAxiom domain = factory.getOWLObjectPropertyDomainAxiom(property, dom);
			OWLObjectPropertyRangeAxiom range = factory.getOWLObjectPropertyRangeAxiom(property, rang);
			OWLAxiom axioma = factory.getOWLDeclarationAxiom(property);
			manager.addAxiom(ontology, axioma);
			manager.addAxiom(ontology, domain);
			manager.addAxiom(ontology, range);


		}catch (Exception e){
			System.out.println("Error al crear la propiedad: "+e.getMessage());
		}
	}
	
	public void addDataProperty(String prop, String rango) {
		try {
			OWLDataProperty property = factory.getOWLDataProperty(prop,prefixManager);
			OWLDatatype rang = factory.getOWLDatatype(rango, prefixManager);
			OWLAxiom axioma = factory.getOWLDeclarationAxiom(property);
			OWLDataPropertyRangeAxiom range = factory.getOWLDataPropertyRangeAxiom(property, rang);
			manager.addAxiom(ontology, axioma);
			manager.addAxiom(ontology, range);


		}catch (Exception e){
			System.out.println("Error al crear el data property: "+e.getMessage());
		}
	}
	
	public void createInstancia(String nombre,String clase) {
		try {
			OWLClass clas = factory.getOWLClass(clase, prefixManager);
			OWLNamedIndividual instancia = factory.getOWLNamedIndividual(nombre, prefixManager);
			OWLClassAssertionAxiom axioma = factory.getOWLClassAssertionAxiom(clas, instancia);
			manager.addAxiom(ontology, axioma);

		}catch (Exception e){
			System.out.println("Error al crear la instancia: "+e.getMessage());
		}
	}
	
	public void expCarnivoro(String prop, String clase) {
		try {
			OWLObjectProperty property = factory.getOWLObjectProperty(prop,prefixManager);
			OWLClass clas = factory.getOWLClass(clase, prefixManager);
			OWLClass carnivoro_clas = factory.getOWLClass("Carnivoro", prefixManager);
			OWLObjectSomeValuesFrom from = factory.getOWLObjectSomeValuesFrom(property, clas);
			OWLEquivalentClassesAxiom axioma = factory.getOWLEquivalentClassesAxiom(from,carnivoro_clas);
			manager.addAxiom(ontology, axioma);

		}catch (Exception e){
			System.out.println("Error al crear la expresión de equivalencia.: "+e.getMessage());
		}
	}
	public void razonador() {
		try {
		OWLReasonerFactory a = new ElkReasonerFactory();
		OWLReasoner reasoner = a.createReasoner(ontology);
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		System.out.println(reasoner.isConsistent());
		if (reasoner.isConsistent()) {
			InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
			OWLOntology infOnt = manager.createOntology(IRI.create("http://sbc2019inf/onto"));
			iog.fillOntology(manager.getOWLDataFactory(), infOnt);
			manager.saveOntology(infOnt,IRI.create(new File("infOntology")));
		}
		reasoner.dispose();
		}catch(Exception e){
			System.out.println("Error al crear la expresión de equivalencia.: "+e.getMessage());
		}
	}
	
	
	
	/*public void expTigreComeVaca() {
		try {
			OWLObjectProperty property = factory.getOWLObjectProperty("Come",prefixManager);
			OWLClass clas = factory.getOWLClass("Tigre", prefixManager);
			OWLObjectSomeValuesFrom from = factory.getOWLObjectSomeValuesFrom(property, clas);
			OWLSubClassOfAxiom axioma = factory.getOWLSubClassOfAxiom(subClass, superClass);
			manager.addAxiom(ontology, axioma);

		}catch (Exception e){
			System.out.println("Error al crear la expresión de equivalencia.: "+e.getMessage());
		}
	}*/
	

}
