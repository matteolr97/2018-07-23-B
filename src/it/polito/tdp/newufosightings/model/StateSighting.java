package it.polito.tdp.newufosightings.model;

public class StateSighting {

	private State s1;
	private State s2;
	private int peso;
	public StateSighting(State s1, State s2, int peso) {
		super();
		this.s1 = s1;
		this.s2 = s2;
		this.peso = peso;
	}
	public State getS1() {
		return s1;
	}
	public void setS1(State s1) {
		this.s1 = s1;
	}
	public State getS2() {
		return s2;
	}
	public void setS2(State s2) {
		this.s2 = s2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	
}
