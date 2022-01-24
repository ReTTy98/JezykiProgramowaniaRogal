/*

Program: Prosty edytor kolorowego grafu
Plik : Edge.java

Klasa Edge reprezentuje krawedzie grafu. Krawedz nie moze istniec
bez podanych wezlow jako poczatek i koniec. 

autor: Paul Paczyński
data: grudzień 2021

*/





package data;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;

public class Edge implements Serializable{
	
	protected Node nodeFrom;
	protected Node nodeTo;
	private Color color = Color.blue;

	
	public Edge(Node nodeFrom,Node nodeTo) {
		this.nodeFrom = nodeFrom;
		this.nodeTo = nodeTo;
	}
	
	public void setNodeFrom(Node nodeFrom) {
		this.nodeFrom = nodeFrom;
	}
	
	public void setNodeTo(Node nodeTo) {
		this.nodeTo = nodeTo;
	}
	
	public Node getNodeFrom() {
		return this.nodeFrom;
	}
	public Node getNodeTo() {
		return this.nodeTo;
	}

	public void setColor(Color color){
		this.color = color;
	}
	public Color getColor(){
		return this.color;
	}
	
	public boolean edgeHovered(int mouseX, int mouseY){
		double distance = Line2D.ptSegDist(this.nodeFrom.getX(), this.nodeFrom.getY(), this.nodeTo.getX(), this.nodeTo.getY(),mouseX, mouseY);

		if (distance < 10) {
			return true;
		}
		else{	
			return false;
		}
}

	

	protected double angleBeetwen(Node from, Node to){
		double x = from.getX();
		double y = from.getY();

		double deltaX = to.getX() - x;
		double deltaY = to.getY() - y;
		double rotation = -Math.atan2(deltaX, deltaY);
		rotation = Math.toRadians(Math.toDegrees(rotation) + 180);
		return rotation;
	}

	protected Point2D getPointOnCircle(Node node, double radians){
		double centerX = node.getX();
		double centerY = node.getY(); 

		radians = radians - Math.toRadians(90.0);

		double xPosy = Math.round((float) (centerX + Math.cos(radians) * node.getR()));
		double yPosy = Math.round((float) (centerY + Math.sin(radians) * node.getR()));

		return new Point2D.Double(xPosy,yPosy);

	}


	public void draw(Graphics graph){
		if(this.nodeFrom != null && this.nodeTo != null){
			graph.setColor(color);
			((Graphics2D)graph).setStroke(new BasicStroke(3.0F));

			double from = angleBeetwen(nodeFrom, nodeTo);
			double to = angleBeetwen(nodeTo, nodeFrom);

			Point2D pointFrom = getPointOnCircle(nodeFrom, from);
			Point2D pointTo = getPointOnCircle(nodeTo, to);

			graph.drawLine((int)pointFrom.getX(), (int)pointFrom.getY(), (int)pointTo.getX(), (int)pointTo.getY());
			graph.setColor(Color.black);

		}
	}
}

