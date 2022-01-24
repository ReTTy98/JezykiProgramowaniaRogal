/*
Program: Program z GUI pozwalajacy na zarzadzanie grupami obiektów typu Book.
Plik: MainFrame.java

- Głowne okienko programu odpowiedzialne za zarządzanie grupami ksiązek.
- Stworzone za pomocą pluginu do Eclipse "Window Builder"

Autor: Paul Paczyński
Data: 8.12.21
*/




package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import data.BookException;
import data.GroupOfBooks;

import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.JMenu;

public class MainFrame extends JFrame implements ActionListener {

	public static final String ABOUT_MESSAGE = "Program: Edytor grup ksiazek\nAutor: Paul Paczyński\nData:Grudzien 2021\n" ;

	private List<GroupOfBooks> currentList;

	private JPanel contentPane;
	private JTable table;
	JButton buttonCreate = new JButton("Create");
	JMenuBar menuBar = new JMenuBar();
	JButton buttonEdit = new JButton("Edit");
	JButton buttonDelete = new JButton("Delete");
	JButton buttonOpenFile = new JButton("Open");
	JButton buttonSaveFile = new JButton("Save");
	private final JMenu mmAbout = new JMenu("About");
	private final JMenuItem mmiteAbout = new JMenuItem("About program");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
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

		this.currentList = new ArrayList<GroupOfBooks>();

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 556, 437);
		menuBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		setJMenuBar(menuBar);
		
		menuBar.add(mmAbout);
		
		mmAbout.add(mmiteAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBounds(0, 305, 540, 65);
		contentPane.add(panel);

		buttonCreate.setMaximumSize(new Dimension(100, 35));
		buttonCreate.addActionListener(this);
		buttonEdit.setMaximumSize(new Dimension(100, 35));
		buttonEdit.addActionListener(this);
		buttonDelete.setMaximumSize(new Dimension(100, 35));
		buttonDelete.addActionListener(this);
		buttonOpenFile.setMaximumSize(new Dimension(100, 35));
		buttonOpenFile.addActionListener(this);
		buttonSaveFile.setMaximumSize(new Dimension(100, 35));
		buttonSaveFile.addActionListener(this);
		mmiteAbout.addActionListener(this);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(buttonCreate);

		panel.add(buttonEdit);

		panel.add(buttonDelete);

		panel.add(buttonOpenFile);

		panel.add(buttonSaveFile);




		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 540, 305);
		contentPane.add(scrollPane);
		

		table = new JTable();
		table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Group Name", "Collection Type", "Number of Books"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, Integer.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(1).setResizable(false);
		this.refreshList();
		
		scrollPane.setViewportView(table);
	
		
		
	}

	void refreshList(){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setRowCount(0);
		for (final GroupOfBooks group :this.currentList ){
			final String[] row = {group.getName(),group.getType().toString(), new StringBuilder().append(group.size()).toString()};
			model.addRow(row);
		}
	}

	int getSelectedRow(){
		final int rowIndex = this.table.getSelectedRow();
		if (rowIndex < 0){
			JOptionPane.showMessageDialog(this, "You did not select a group","error",0);
		}
		return rowIndex;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object eventSource = event.getSource();

		if (eventSource == buttonCreate){
			final GroupOfBooks group = GroupEditWindow.createNewGroupOfBooks(MainFrame.this);
			if (group != null){
				this.currentList.add(group);
			}
			
		}
	
		if (eventSource == buttonEdit){
			int rowIndex = getSelectedRow();
			if (rowIndex >= 0 ){
				final Iterator<GroupOfBooks> i = this.currentList.iterator();
				while (rowIndex --> 0) {
					i.next();
				}
				new GroupEditWindow(this, i.next());
			}
			
		}
		if (eventSource == buttonDelete){
			int rowIndex = getSelectedRow();
			if (rowIndex >= 0 ){
				final Iterator<GroupOfBooks> i = this.currentList.iterator();
				while (rowIndex -->= 0) {
					i.next();
				}
				i.remove();
			
			}
		}
		if (eventSource == buttonOpenFile){
			final JFileChooser reader = new JFileChooser(".");
			final int file = reader.showOpenDialog(MainFrame.this);
			if (file == 0){
				try {
					final GroupOfBooks loadedGroup = GroupOfBooks.fromFile(reader.getSelectedFile().getName());
					currentList.add(loadedGroup);
				} catch (BookException e) {
					
					JOptionPane.showMessageDialog(this, e.getMessage(),"Error",0);
					
				} 
			}
		}
		if (eventSource == buttonSaveFile){
			int rowIndex = getSelectedRow();
			if (rowIndex >=0){
				final Iterator<GroupOfBooks> i = currentList.iterator();
				while(rowIndex -->0){
					i.next();
				}
				try {
					final GroupOfBooks group2 = i.next();
					final JFileChooser chooser2 = new JFileChooser(".");
					final int file2 = chooser2.showSaveDialog(MainFrame.this);
					if (file2 == 0){
						GroupOfBooks.toFile(chooser2.getSelectedFile().getName(), group2);
					}

				} catch (BookException e) {
					
					JOptionPane.showMessageDialog(this, e.getMessage(),"Error",0);
				}
			}

		}
		if (eventSource == mmiteAbout){
			JOptionPane.showMessageDialog(MainFrame.this, ABOUT_MESSAGE,"About Author",1);
		}
	
		this.refreshList();	
	}
}
