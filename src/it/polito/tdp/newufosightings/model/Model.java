package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	Graph<State, DefaultWeightedEdge> grafo;
	NewUfoSightingsDAO dao;
	Map<String, State> idMap ;
	List<State> vicini;
	String result;
	
	
	public Model() {
		dao = new NewUfoSightingsDAO();
		idMap = new HashMap<>();
		vicini = new LinkedList<>();
	}
/*	public String creaGrafo(int anno, int xG) {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

		dao.loadAllStates(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		for(State s: this.grafo.vertexSet())
		{
			vicini = dao.getVicini(idMap, s);
			for(State stemp:vicini) {
				int peso = dao.getPeso(s,stemp, anno, xG);
				DefaultWeightedEdge d = grafo.getEdge(s, stemp);
				if(d == null )
					Graphs.addEdge(grafo, s, stemp, peso);
			}
				result+=s.getName()+"\n";		}
	
	System.out.println("Vertici= "+grafo.vertexSet().size());
	System.out.println("Archi = "+grafo.edgeSet().size());
			return result;
	}*/
	
	public void createGraph(int anno, int giorni) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.loadAllStates(idMap));
		for(StateSighting s : dao.stateWithSighting(idMap, anno, giorni)) {
			Graphs.addEdgeWithVertices(grafo, s.getS1(), s.getS2(), s.getPeso());
		}
	}
	
	public int getNumeroVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNumeroArchi() {
		return grafo.edgeSet().size();
	}
	
	public String getSumWeightNeighbours() {
		String stringa = "";
		for(State s : grafo.vertexSet()) {
			int somma = 0;
			for(State n : Graphs.neighborListOf(grafo, s)) {
				DefaultWeightedEdge e = grafo.getEdge(s, n);
				somma += grafo.getEdgeWeight(e);
			}
			stringa += s.getName()+" "+somma+"\n";
		}
		return stringa;
	}
	

}
