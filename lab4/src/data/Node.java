/*

Program: Prosty edytor kolorowego grafu
Plik : Node.java

Klasa Node reprezentuje węzły na plaszczyznie 

autor: Paul Paczyński
data: grudzień 2021

*/




package data;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.Font;

public class Node implements Serializable {
	
	protected int x;
	protected int y;
	protected int r;
	private Color color;
	private String text;
	
	private static final Font font = new Font("BOLD",1,14 );

	public Node(int x, int y){
		this.x = x;
		this.y = y;
		this.r = 30;
		this.color = Color.white;
		
		
	}
	
	public void setX(int nx) {
		this.x = nx;
	}
	public void setY(int ny) {
		this.y = ny;
	}
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public int getR(){
		return this.r;
	}

	public void setColor(Color color){
		this.color = color;
	}
	public Color getColor(){
		return this.color;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return this.text;
	}
	
	public boolean nodeHovered(int mouseX, int mouseY) {
		return (x-mouseX) * (x - mouseX) + (y - mouseY) * (y -mouseY) <= r*r;
	}
	
	void draw(Graphics graph) {
		graph.setColor(color);
		graph.fillOval(x-r,y-r,r*2,r*2);
		graph.setColor(Color.black);
		graph.drawOval(x-r,y-r,r*2,r*2);
		if(this.text != null){
			graph.setFont(font);
			FontRenderContext frc = ((Graphics2D)graph).getFontRenderContext();
			Rectangle2D bounds = font.getStringBounds(this.text, frc);
			graph.drawString(this.text, this.x - (int)bounds.getWidth() /2, this.y + (int)bounds.getHeight() /4);
		}
		
	}

}
