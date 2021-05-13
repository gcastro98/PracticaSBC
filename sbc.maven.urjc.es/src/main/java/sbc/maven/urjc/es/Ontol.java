package sbc.maven.urjc.es;

import java.io.File;
import java.util.List;

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
			ontology = manager.createOntology(IRI.create(ontoIRI));
		}catch (OWLOntologyCreationException e){
			System.out.println("Ontolog�a no creada: "+e.getMessage());
		}
	}

	public void loadOntology() {
		try {

			ontology = manager.loadOntologyFromOntologyDocument(new File("MovieOntology.owl"));
		}catch (OWLOntologyCreationException e){
			System.out.println("Ontolog�a no creada: "+e.getMessage());
		}
	}

	public void saveOntology() {
		try {
			manager.saveOntology(ontology,IRI.create(new File ("ontologyCreada.owl")));
		}catch (Exception e){
			System.out.println("Ontolog�a no guardada: "+e.getMessage());
		}
	}
	
	public void addClass(String name) {
		try {
			OWLClass clase = factory.getOWLClass(name, prefixManager);
			OWLAxiom axioma = factory.getOWLDeclarationAxiom(clase);
			manager.addAxiom(ontology, axioma);
		}catch (Exception e){
			System.out.println("Axioma no guardado: "+e.getMessage());
		}
	}
	
	public void addSubClass(String subClass, String superClass) {
		try {
			OWLClass subclase = factory.getOWLClass(subClass, prefixManager);
			OWLClass superclase = factory.getOWLClass(superClass, prefixManager);

			OWLAxiom axioma_subclase = factory.getOWLDeclarationAxiom(subclase);
			OWLAxiom axioma_superclase = factory.getOWLDeclarationAxiom(superclase);
			OWLSubClassOfAxiom axioma_derivado = factory.getOWLSubClassOfAxiom(subclase, superclase);

			manager.addAxiom(ontology, axioma_superclase);
			manager.addAxiom(ontology, axioma_subclase);
			manager.addAxiom(ontology, axioma_derivado);
		}catch (Exception e){
			System.out.println("Error al a�adir subclase no guardada: "+e.getMessage());
		}
	}
	
	public void addObjectProperty(String prop) {
		try {
			OWLObjectProperty property = factory.getOWLObjectProperty(prop,prefixManager);
			OWLAxiom axioma = factory.getOWLDeclarationAxiom(property);
			manager.addAxiom(ontology, axioma);

		}catch (Exception e){
			System.out.println("Error al crear la propiedad: "+e.getMessage());
		}
	}
	
	public void addObjectProperty(String prop, String dominio, String rango) { //AddObjectProperty
		try {
			OWLObjectProperty property = factory.getOWLObjectProperty(IRI.create(prop));
			OWLClass dom = factory.getOWLClass(IRI.create(dominio));
			OWLClass rang = factory.getOWLClass(IRI.create(rango));
			System.out.println(dom.toString());
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

	
	public void addDataProperty(String prop, String dominio) {
		try {
			OWLDataProperty property = factory.getOWLDataProperty(IRI.create(prop));
			OWLDatatype dom = factory.getOWLDatatype(IRI.create(dominio));
			OWLAxiom axioma = factory.getOWLDeclarationAxiom(property);
			OWLDataPropertyRangeAxiom range = factory.getOWLDataPropertyRangeAxiom(property, dom);
			manager.addAxiom(ontology, axioma);
			manager.addAxiom(ontology, range);
		}catch (Exception e){
			System.out.println("Error al crear el data property: "+e.getMessage());
		}
	}

	
	public void createInstancia(String nombre,String clase) {
		try {
			OWLClass clas = factory.getOWLClass(IRI.create(clase));
			OWLNamedIndividual instancia = factory.getOWLNamedIndividual(nombre,prefixManager);
			OWLClassAssertionAxiom axioma = factory.getOWLClassAssertionAxiom(clas, instancia);
			manager.addAxiom(ontology, axioma);

		}catch (Exception e){
			System.out.println("Error al crear la instancia: "+e.getMessage());
		}
	}

	public void createInstanciaWithDataProperty(String prop,String nombre_instancia,String nombre_clase, double valor) {
		try {
			//Sustraigo individuos
			OWLClass clase = factory.getOWLClass(IRI.create(nombre_clase));
			OWLNamedIndividual instancia = factory.getOWLNamedIndividual(nombre_instancia, prefixManager);

			//Sustraigo la propiedad
			OWLDataProperty property = factory.getOWLDataProperty(IRI.create(prop));


			//creo instancias
			createInstancia(nombre_instancia,nombre_clase);
			addDataProperty(prop,nombre_clase);

			//creo la instancias con su propiedad
			OWLDatatype dom = factory.getOWLDatatype(IRI.create(nombre_clase));
			OWLAxiom axioma = factory.getOWLDeclarationAxiom(property);
			OWLDataPropertyAssertionAxiom axioma_final = factory.getOWLDataPropertyAssertionAxiom(property,instancia,valor);
			manager.addAxiom(ontology, axioma);
			manager.addAxiom(ontology, axioma_final);

		}catch (Exception e){
			System.out.println("Error al crear la instancia: "+e.getMessage());
		}
	}

	public void createInstanciaWithObjetivoProperty(String nombre_propiedad, String nombre_individuo1, String clase_individuo1, String nombre_individuo2, String clase_individuo2) {
		// individuo_1 ---propiedad---> individuo_2
		try {
			//Sustraigo individuos
			OWLClass clase_1 = factory.getOWLClass(IRI.create(clase_individuo1));
			OWLClass clase_2 = factory.getOWLClass(IRI.create(clase_individuo1));
			OWLNamedIndividual instancia_1 = factory.getOWLNamedIndividual(nombre_individuo1, prefixManager);
			OWLNamedIndividual instancia_2 = factory.getOWLNamedIndividual(nombre_individuo2, prefixManager);
			//Sustraigo la propiedad
			OWLObjectProperty property = factory.getOWLObjectProperty(IRI.create(nombre_propiedad));
			//creo las instancias y la propiedad
			createInstancia(nombre_individuo1,clase_individuo1);
			createInstancia(nombre_individuo2,clase_individuo2);
			addObjectProperty(nombre_propiedad,clase_individuo1,clase_individuo2);

			//creo la instancias con su propiedad
			OWLAxiom axioma_total = factory.getOWLObjectPropertyAssertionAxiom(property,instancia_1,instancia_2);
			manager.addAxiom(ontology, axioma_total);


		}catch (Exception e){
			System.out.println("Error al crear la instancia: "+e.getMessage());
		}
	}

	public void addActores(List<Actor> actores){
		for (Actor actor: actores) {
			System.out.println(actor.getName().trim().replace(" ", "_"));
			createInstancia(actor.getName(),"Actor");
			}
	}

	public void addPeliculas(List<Pelicula> peliculas){
		for (Pelicula pelicula: peliculas) {
			//a�ado peliculas y actores.
			for (Actor actor: pelicula.getReparto()){
				createInstanciaWithObjetivoProperty("http://www.movieontology.org/2009/10/01/movieontology.owl#isActorIn",actor.getName(),"http://dbpedia.org/ontology/Actor",pelicula.getTitulo(),"http://www.movieontology.org/2009/11/09/Movie");
			}
			createInstanciaWithDataProperty("http://www.movieontology.org/2009/10/01/movieontology.owl#imdbrating",pelicula.getTitulo(),"http://www.movieontology.org/2009/11/09/Movie",pelicula.getCalificacion());
			createInstanciaWithObjetivoProperty("http://www.movieontology.org/2009/10/01/movieontology.owl#belongToGenre",pelicula.getTitulo(),"http://www.movieontology.org/2009/11/09/Movie",pelicula.getGenero(),"http://www.movieontology.org/2009/10/01/movieontology.owl#Genre");
			createInstanciaWithObjetivoProperty("http://www.movieontology.org/2009/10/01/movieontology.owl#hasReleasingCountry",pelicula.getTitulo(),"http://www.movieontology.org/2009/11/09/Movie",pelicula.getPais(),"http://www.movieontology.org/2009/10/01/Country");
			createInstanciaWithObjetivoProperty("http://www.movieontology.org/2009/10/01/movieontology.owl#isProducedBy",pelicula.getTitulo(),"http://www.movieontology.org/2009/11/09/Movie",pelicula.getProductora(),"http://www.movieontology.org/2009/10/01/movieontology.owl#Production_Company");
		}
	}
	// Si come animal --> es carnivoro
	// Prop = que  ; Clasefrom = el qu
	// e ; Clase_eq = quien
	public void addExpresion(String nombre_propiedad, String nombre_rango, String nombre_resultado) {
		// rango cumple propiedad --equivalente--> resultado
		try {
			OWLObjectProperty propiedad = factory.getOWLObjectProperty(nombre_propiedad,prefixManager); 	//Recogemos la propiedad (objeto) de la ont. a partir de su nombre.
			OWLClass rango_class = factory.getOWLClass(nombre_rango, prefixManager);						//Recogemos la clase del rango
			OWLClass resultado_class = factory.getOWLClass(nombre_resultado, prefixManager);				//Recogemos la clase resultante
			OWLObjectSomeValuesFrom cualidad = factory.getOWLObjectSomeValuesFrom(propiedad, rango_class);	//Definimos la cualidad en funcion de la propiedad y el rango
			OWLAxiom axioma = factory.getOWLEquivalentClassesAxiom(resultado_class, cualidad);						//Definimos el axioma en funcion de la clase resultante y la cualidad que se le aplica
			manager.addAxiom(ontology, axioma);

		}catch (Exception e){
			System.out.println("Error al crear la expresi�n de equivalencia.: "+e.getMessage());
		}
	}
	public void razonador() {
		try {
		OWLReasonerFactory a = new ElkReasonerFactory();
		OWLReasoner reasoner = a.createReasoner(ontology);
		reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
		if (reasoner.isConsistent()) {
			InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
			OWLOntology infOnt = manager.createOntology(IRI.create("http://sbc2019inf/onto"));
			iog.fillOntology(manager.getOWLDataFactory(), infOnt);
			manager.saveOntology(infOnt,IRI.create(new File("infOntology.owl")));
		}
		reasoner.dispose();
		}catch(Exception e){
			System.out.println("Error al crear la expresi�n de equivalencia.: "+e.getMessage());
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
			System.out.println("Error al crear la expresi�n de equivalencia.: "+e.getMessage());
		}
	}*/
	

}
