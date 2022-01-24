/*

Program: Prosty edytor kolorowego grafu
Plik : Graph.java

Klasa Graph reprezentuje graf na plaszczyznie.

autor: Paul Paczyński
data: grudzień 2021

*/



package data;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;


public class Graph implements Serializable {
	
	private Set<Node> nodes;
	private Set<Edge> edges;
	
	public Graph() {
		this.nodes = new HashSet<Node>();
		this.edges = new HashSet<Edge>();
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	public void removeNode(Node node) {
		nodes.remove(node);
	}
	
	public Node[] getNodes() {
		Node[] array = new Node[0];
		return nodes.toArray(array);
	}
	
	public void addEdge(Edge edge) {
		if(duplicatCheck(edge)){
			edges.add(edge);
		}
	}
	
	public Edge[] getEdges() {
		Edge[] array = new Edge[0];
		return edges.toArray(array);
	}
	
	public void removeEdge(Edge edge) {
		edges.remove(edge);
	}
	
	public void removeEdge(Node node) {
		List<Edge> toRemove = new ArrayList<Edge>();
		
		for(Edge edge: edges) {
			if(node == edge.nodeFrom || node == edge.nodeTo) {
				toRemove.add(edge);
			}
		}
		for(Edge edge:toRemove) {
			removeEdge(edge);
		}
	}
	
	public boolean duplicatCheck(Edge edge){
		Iterator<Edge> i = edges.iterator();
		while(i.hasNext()){
			Edge iedge = i.next();
			int ifromX = iedge.nodeFrom.getX();
			int ifromY = iedge.nodeFrom.getY();
			int itoX = iedge.nodeTo.getX();
			int itoY = iedge.nodeTo.getY();

			int fromX = edge.getNodeFrom().getX();
			int fromY = edge.getNodeFrom().getY();
			int toX = edge.getNodeTo().getX();
			int toY = edge.getNodeTo().getY();

			if(ifromX == fromX && ifromY == fromY && itoX == toX && itoY == toY){
				return false;
			}
			if(ifromX ==toX && ifromY == toY && itoX == fromX && itoY==fromY){
				return false;
			}
			if(fromX == toX && fromY==toY){
				return false;
			}
			
		}
		
		return true;

	}
	
	public void draw(Graphics graph) {
		for(Node node: nodes) {
			node.draw(graph);
		}
		for(Edge edge: edges) {
			edge.draw(graph);
		}
	}

	public void saveToFile(String file_name) {
		try{
			ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(file_name+".bin"));
			o.writeObject(this);
			o.close();
		}
		catch(FileNotFoundException e){
			System.out.println("ERROR 1:  " + e.getMessage());
		}
		catch(IOException e){
			System.out.println("ERROR 2  " + e.getMessage());
		}
	}

	public Graph loadFromFile(String file_name){
		try{
			ObjectInputStream o = new ObjectInputStream(new FileInputStream(file_name));
			Graph graph = (Graph) o.readObject();
			o.close();
			return graph;
		}
		catch(FileNotFoundException e){
			System.out.println("ERROR 1  " + e.getMessage());
		}
		catch(IOException e){
			System.out.println("ERROR 2  " + e.getMessage());
		}
		catch(ClassNotFoundException e){
			System.out.println("ERROR 3  " + e.getMessage());
		}
		return null;
		
	}
	

}
