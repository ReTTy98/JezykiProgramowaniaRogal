/*

Program: Prosty edytor kolorowego grafu
Plik : MainFrame.java

Klasa główna mojego programu, odpowiedzialna za stworzenie okna glowenego
dla edytora kolorowych grafow

autor: Paul Paczyński
data: grudzień 2021

*/

package gui;

import data.ColorUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import data.Edge;
import data.Graph;
import data.Node;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private GraphPanel panel = new GraphPanel();
	private ColorUtils colorCheck = new ColorUtils();
	private final String ABOUT = "Program: Prosty edytor grafów kolorowych\nAutor:Paul Paczyński\nData:Grudzień 2021";
	private final String MANUAL = "WEZLY:\n"+
	"Nacisnij 2 razy LPM na puste pole, aby dodac wezel.\n"+
	"Przenies wezel trzymajac LPM.\n"+
	"Nacisnij PPM celujac w wezel, aby wyswietlic jego menu\n"+

	"KRAWEDZIE:\n"+
	"Nacisnij PPM celujac w krawedz, aby wyswietlic jej menu\n"+


	"Przytrzymaj LPM na pustym polu, aby przeniesc wszystkie obiekty\n"+
	"STRZAŁKI\n"+
	"Strzalki przenosza caly graf, przytzymanie SHIFT zwieksza predkosc\n"+
	"SKROTY KLAWISZOWE\n"+
	"C - najedz myszka nad krawedz/wezel i nacisnij C aby zmienic kolor\n"+
	"N - najedz na puste pole i nacisjin N aby utworzyc wezel\n"+
	"T - najedz na wezel i nacisnij T, aby zmienic jego nazwe\n"+
	"DEL - najedz na wezel/krawedz aby go usunac\n";
	

	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 639, 526);
		MainFrame.this.setTitle("Prosty edytor kolorowych grafow");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mmFile = new JMenu("File");
		menuBar.add(mmFile);
		
		JMenuItem mmFileNew = new JMenuItem("New");
		mmFileNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				MainFrame.this.panel.setGraph(new Graph());
				repaint();
			}
		});
		mmFile.add(mmFileNew);
		
		JMenuItem mmFileOpen = new JMenuItem("Open from");
		mmFileOpen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				final JFileChooser reader = new JFileChooser(".");
				final int file = reader.showOpenDialog(MainFrame.this);
				if(file ==0){
					Graph graph = MainFrame.this.panel.getGraph().loadFromFile(reader.getSelectedFile().getAbsolutePath());
					MainFrame.this.panel.setGraph(graph);
					repaint();

				}
				
			}
		});
		mmFile.add(mmFileOpen);
		
		JMenuItem mmFileSave = new JMenuItem("Save to");
		mmFileSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				final JFileChooser chooser = new JFileChooser(".bin");
				final int file = chooser.showSaveDialog(MainFrame.this);
				if (file == 0){
					MainFrame.this.panel.getGraph().saveToFile(chooser.getSelectedFile().getAbsolutePath());
				}
				
			}
		});
		mmFile.add(mmFileSave);
		
		JMenuItem mmFileTest = new JMenuItem("Testing example");
		mmFileTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				test();
				repaint();
			}
		});
		mmFile.add(mmFileTest);
		
		JMenu mmGraph = new JMenu("Graph");
		menuBar.add(mmGraph);
		
		JMenuItem mmGraphNodes = new JMenuItem("Show list of nodes");
		mmGraphNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showListOfNodes(panel.getGraph());
			}
		});
		mmGraph.add(mmGraphNodes);
		
		JMenuItem mmGraphEdges = new JMenuItem("Show list of edges");
		mmGraphEdges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showListOfEdges(panel.getGraph());
			}
		});
		mmGraph.add(mmGraphEdges);
		
		JMenu mmHelp = new JMenu("Help");
		menuBar.add(mmHelp);
		
		JMenuItem mmHelpManual = new JMenuItem("Manual");
		mmHelpManual.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(MainFrame.this, MANUAL, "Instrukcja obslugi", 1);
			}
		});
		mmHelp.add(mmHelpManual);
		
		JMenuItem mmHelpAuthor = new JMenuItem("Author");
		mmHelpAuthor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(MainFrame.this, ABOUT,"O Programie",1);
			}
		});
		mmHelp.add(mmHelpAuthor);
		
		
		
		contentPane = new JPanel(); 
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		

		contentPane.add(panel, BorderLayout.CENTER);
		Graph graph = new Graph();
		panel.setGraph(graph);
	}
	
	private void test() {
		Graph graph = new Graph();
		
		Node n1 = new Node(100,100);
		Node n2 = new Node(200,200);
		Node n3 = new Node(300,300);

		Edge e1 = new Edge(n1,n2);
		Edge e2 = new Edge(n2,n3);
		Edge e3 = new Edge(n3,n1);


		graph.addNode(n3);
		graph.addNode(n2);
		graph.addNode(n1);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);

		n1.setColor(Color.blue);
		n2.setColor(Color.red);
		n3.setColor(Color.yellow);

		n1.setText("Pierwszy");
		n2.setText("Drugi");
		n3.setText("Trzeci");

		e1.setColor(Color.pink);
		e2.setColor(Color.green);
		e3.setColor(Color.orange);

		

		panel.setGraph(graph);
	}
	
	private void showListOfNodes(Graph graph){
		Node[] array = graph.getNodes();
		StringBuilder message = new StringBuilder ("How many? : " + array.length + '\n');
		for (int i = 0 ; i <array.length;i++) {
			Node node = array[i];
			String cords = ("[" + node.getX()+","+node.getY() + "]   Color: "); 
			String color = colorCheck.getColorNameFromRgb(node.getColor().getRed(),node.getColor().getGreen(),node.getColor().getBlue())+"   Text: ";
			String text = node.getText()+"\n";
			message.append(cords + color + text );
		}
		JOptionPane.showMessageDialog(this,message,"List of Nodes",-1);
	}
	
	private void showListOfEdges(Graph graph) {
		Edge[] array = graph.getEdges();
		StringBuilder message = new StringBuilder ("How many? : " + array.length + "\n");
		for (int i = 0; i < array.length;i++) {
			Edge edge = array[i];
			String nodeFromCords = ("[ " + edge.getNodeFrom().getX() +"  "+ edge.getNodeFrom().getY() + "]    --->  ");
			String nodeToCords = ("   [" +edge.getNodeTo().getX() + "  " +edge.getNodeTo().getY() + "]   Color: " );
			String color = colorCheck.getColorNameFromRgb(edge.getColor().getRed(),edge.getColor().getGreen(),edge.getColor().getBlue())+"\n";
			message.append(nodeFromCords + nodeToCords+ color);
		}
		JOptionPane.showMessageDialog(this, message,"List of edges",-1);
	}
	

}
