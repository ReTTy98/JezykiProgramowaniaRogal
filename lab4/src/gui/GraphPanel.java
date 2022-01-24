/*

Program: Prosty edytor kolorowego grafu
Plik : GraphPanel.java

Klasa GraphPanel implementuje wszystkie funkcjonalnosci mojego programu 
Klasa umozliwia:
-rysowanie grafu w oknie
-obsluge zdarzen generowanych przez myszke
-obsluge zdarzen generowanych przez klawiature
-wywolywanie menu kontekstowych dla wezlow i krawedzi w celu ich edycji
-edycje wezlow , krawedzi oraz wyswietlanie ich atrybutow


autor: Paul Paczyński
data: grudzień 2021

*/




package gui;

import data.Edge;
import data.Graph;
import data.Node;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	
	protected Graph graph;
	
	private int mouseX;
	private int mouseY;
	
	private boolean mouseLeft = false;
	private Node nodeHover = null;
	private Edge edgeHover = null;
	
	private boolean drawingEdge = false;
	
	private Edge currentEdge = new Edge(null,null);
	
	protected int mouseCursor = Cursor.DEFAULT_CURSOR;

	/**
	 * Create the panel.
	 */
	public GraphPanel() {
		this.addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		requestFocus();

	}
	
	public void setGraph(Graph graph) {
		this.graph = graph;
		
	}
	public Graph getGraph() {
		return graph;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		repaint();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int distance;
		if(e.isShiftDown()){
			distance = 15;
		}
		else{
			distance = 2;
		}
		switch (e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				moveGraph(-distance, 0);
				break;
			case KeyEvent.VK_RIGHT:
				moveGraph(distance, 0);
				break;
			case KeyEvent.VK_UP:
				moveGraph(0, -distance);
				break;
			case KeyEvent.VK_DOWN:
				moveGraph(0, distance);
				break;
			case KeyEvent.VK_DELETE:
				if(nodeHover!=null){
					graph.removeNode(nodeHover);
					nodeHover=null;
				
				}
				else if(edgeHover!=null){
					graph.removeEdge(edgeHover);
					edgeHover=null;
				}
				break;
			case KeyEvent.VK_N:
				if(nodeHover==null){
					graph.addNode(new Node(mouseX,mouseY));
				}
				break;
			case KeyEvent.VK_C:
				if(nodeHover!=null){
					Color newColor = JColorChooser.showDialog(GraphPanel.this, "Change node's color", nodeHover.getColor());
					if (newColor!=null){
						nodeHover.setColor(newColor);
					}

				}
				else if(edgeHover!=null){
					Color newColor = JColorChooser.showDialog(GraphPanel.this, "Change edge's color", edgeHover.getColor());
					if (newColor!=null){
						edgeHover.setColor(newColor);
					}

				}
				break;
			case KeyEvent.VK_T:
				if(nodeHover!=null){
					String text = JOptionPane.showInputDialog(GraphPanel.this, "Write new text");
					if(text!=null){
						nodeHover.setText(text);
				}
				break;
			}
		}
		repaint();

	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (mouseLeft) {
			if(nodeHover != null) {
				moveNode(nodeHover,e.getX() - mouseX, e.getY() - mouseY );
			}
			else if(edgeHover !=null){
				moveNode(edgeHover.getNodeFrom(), e.getX()-mouseX, e.getY()-mouseY);
				moveNode(edgeHover.getNodeTo(), e.getX()-mouseX, e.getY()-mouseY);
			}
			else {
				moveGraph(e.getX() - mouseX, e.getY() - mouseY);
			}
			
		}
		mouseX = e.getX();
		mouseY = e.getY();
		repaint();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setMouse(e);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && e.getButton() == 1 && nodeHover == null){
			graph.addNode(new Node(e.getX(),e.getY()));
				repaint();

		}
		if(e.getClickCount() == 2 && e.getButton()==1 && nodeHover!=null){
			drawingEdge = true;
			currentEdge.setNodeFrom(nodeHover);
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() ==1) {
			mouseLeft = true;
			if(drawingEdge) {
				Node currentNode;
				currentNode  = findNode(e);
				if(currentNode!=null){
					currentEdge.setNodeTo(currentNode);
					graph.addEdge(new Edge(currentEdge.getNodeFrom(),currentEdge.getNodeTo()));
					drawingEdge = false;
					repaint();
				}
				else{
					JOptionPane.showMessageDialog(this, "Couldnt find node");
					drawingEdge = false;
				}
				
			}
		}
		
		if (e.getButton() == 3) {
			if (nodeHover != null) {
				createPopupMenu(e,nodeHover);
			}
			else if(edgeHover !=null){
				createPopupMenu(e,edgeHover);
			}
			else {
				createPopupMenu(e);
			}
		}
		setMouse(e);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() ==1 ) {
			mouseLeft = false;
		}
		if (e.getButton() == 3) {
		}
		setMouse(e);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}
	
	protected void setMouse(MouseEvent e) {
		nodeHover = findNode(e);
		edgeHover = findEdge(e);
		if (nodeHover != null || edgeHover !=null) {
			mouseCursor = Cursor.HAND_CURSOR;
			
		}
		else if (mouseLeft){
			mouseCursor = Cursor.MOVE_CURSOR;
		}
		else if(drawingEdge){
			mouseCursor = Cursor.TEXT_CURSOR;
		}
		else {
			mouseCursor = Cursor.DEFAULT_CURSOR;
		}
		setCursor(Cursor.getPredefinedCursor(mouseCursor));
		mouseX = e.getX();
		mouseY = e.getY();
		
		
	}
	
	private Node findNode(int mouseX, int mouseY) {
		for (Node node: graph.getNodes()) {
			if (node.nodeHovered(mouseX,mouseY)) {
				return node;
			}
		}
		return null;
	}

	private Node findNode(MouseEvent e) {
		return findNode(e.getX(),e.getY());
	}

	private Edge findEdge(int mouseX, int mouseY){
		for (Edge edge: graph.getEdges()){
			if (edge.edgeHovered(mouseX, mouseY)){
				return edge;
			}
		}
		return null;
	}


	private Edge findEdge(MouseEvent e){
		return findEdge(e.getX(),e.getY());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (graph == null) {
			return;
		}
		graph.draw(g);
		
	}
	
	private void moveNode(Node node, int nx, int ny) {
		node.setX(node.getX() + nx);
		node.setY(node.getY() + ny);
	}
	

	
	private void moveGraph(int nx, int ny) {
		for (Node node : graph.getNodes()) {
			moveNode(node,nx,ny);
		}
	}
	
	protected void createPopupMenu(MouseEvent event) {
		JMenuItem popItem;
		JPopupMenu popup = new JPopupMenu();
		popItem = new JMenuItem("Create new Node");
		
		popItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ 
				graph.addNode(new Node(event.getX(),event.getY()));
				repaint();
			}
		});
		popup.add(popItem);
		popup.show(event.getComponent(), event.getX(), event.getY());
		
		
	}

	protected void createPopupMenu(MouseEvent event, Edge edge){
		JPopupMenu popup = new JPopupMenu();
		JMenuItem popItemRemove;
		JMenuItem popItemChangeColor;

		popItemRemove = new JMenuItem("Remove this edge");
		popItemRemove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				graph.removeEdge(edge);
				repaint();
			}
		});

		popItemChangeColor = new JMenuItem("Change color");
		popItemChangeColor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Color newColor = JColorChooser.showDialog(GraphPanel.this, "Change edge's color", edge.getColor());
				if (newColor!=null){
					edge.setColor(newColor);
				}
				repaint();
			}
		});
		popup.add(popItemRemove);
		popup.add(popItemChangeColor);
		popup.show(event.getComponent(), event.getX(), event.getY());

	}
	
	protected void createPopupMenu(MouseEvent event, Node node) {
		JMenuItem popItemRemove;
		JMenuItem popItemAddEdge;
		JMenuItem popItemChangeColor;
		JMenuItem popItemChangeText;
		JPopupMenu popup = new JPopupMenu();
		popItemRemove = new JMenuItem("Remove this node");
		
		popItemRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graph.removeNode(node);
				graph.removeEdge(node);
				repaint();
			}	
		});
		
		popItemAddEdge = new JMenuItem("Add edge");
		popItemAddEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawingEdge = true;
				currentEdge.setNodeFrom(node);
			}
		});

		popItemChangeColor = new JMenuItem("Change color");
		popItemChangeColor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Color newColor = JColorChooser.showDialog(GraphPanel.this, "Change node's color", node.getColor());
				if (newColor!=null){
					node.setColor(newColor);
				}
				repaint();
			}
		});

		popItemChangeText = new JMenuItem("Change text");
		popItemChangeText.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String text = JOptionPane.showInputDialog(GraphPanel.this, "Write new text");
				if(text!=null){
					node.setText(text);
				}
				repaint();
			}
		});
		popup.add(popItemAddEdge);
		popup.add(popItemChangeColor);
		popup.add(popItemChangeText);
		popup.add(popItemRemove);
		
		popup.show(event.getComponent(), event.getX(), event.getY());
		
	}

}
