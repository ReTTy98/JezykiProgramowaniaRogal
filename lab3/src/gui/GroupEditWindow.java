/*
Program: Program z GUI pozwalajacy na zarzadzanie grupami obiektów typu Book.
Plik: GroupEditWindow.java

- Klasa odpowiedzialna za stworzenie i zarzadzaniem okienka pozwalajacego na edycje poszegolnych elementow w grupie 
- Użyto pluginu Window Builder dla Eclipse
- Brak metody action performed dla calego programu, zamiast tego wywolane sa anonimowe klasy Action Listener dla kazdego przycisku


Autor: Paul Paczyński
Data: 8.12.21
*/




package gui;

import java.awt.Dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import data.GroupType;
import data.BookException;
import data.GroupOfBooks;
import data.Book;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.awt.event.ActionEvent;


public class GroupEditWindow extends JDialog {
	private JTextField textGroupName;
	private JTextField textCollectionType;
	private JTable table;
	private GroupOfBooks currentGroup;

	/**
	 * Launch the application.
	 **/
	public static void main() {
		try {
			final GroupOfBooks currentGroup = new GroupOfBooks(GroupType.VECTOR, "Starting group");
			new GroupEditWindow(null, currentGroup);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public GroupEditWindow(final Window parent, final GroupOfBooks group) {
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		

		
		this.currentGroup = group;
		setTitle("Modify Group");
		setBounds(100, 100, 615, 364);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton buttonAdd = new JButton("Add new book");
				buttonAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final Book newBook = UserDialogWindow.createNewBook(GroupEditWindow.this);
						if (newBook != null){
							currentGroup.add(newBook);
						}
						refreshList(currentGroup);
					}
				});
				buttonPane.add(buttonAdd);
			}
			{
				JButton buttonEdit = new JButton("Edit book");
				buttonEdit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int rowIndex = getSelectedRow();
						if (rowIndex >=0){
							final Iterator<Book> i = currentGroup.iterator();
							while (rowIndex -->0){
								i.next();
							}
							UserDialogWindow.modifyBook(GroupEditWindow.this, i.next());
						}
						refreshList(currentGroup);

					}
				});
				buttonPane.add(buttonEdit);
			}
			{
				JButton buttonDelete = new JButton("Delete book");
				buttonDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int rowIndex = getSelectedRow();
						if (rowIndex >=0){
							final Iterator<Book> i = currentGroup.iterator();
							while (rowIndex-- >= 0){
								i.next();
							}
							i.remove();
						}
						refreshList(currentGroup);

					}
				});
				buttonPane.add(buttonDelete);
			}
			{
				JButton buttonLoadFromFile = new JButton("Load book from file");
				buttonLoadFromFile.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e){
						final JFileChooser reader = new JFileChooser(".");
						final int file = reader.showOpenDialog(GroupEditWindow.this);
						if (file ==0){
							Book book;
							try {
								book = Book.fromFile(reader.getSelectedFile().getName());
								System.out.println(book);
								currentGroup.add(book);
								refreshList(currentGroup);
							} catch (BookException e1) {
								JOptionPane.showMessageDialog(GroupEditWindow.this, e1.getMessage(),"Error",0);
								refreshList(currentGroup);
							}
							
						}
					}
				});
				buttonLoadFromFile.setActionCommand("OK");
				buttonPane.add(buttonLoadFromFile);
				getRootPane().setDefaultButton(buttonLoadFromFile);
			}
			{
				JButton buttonSaveToFile = new JButton("Save book to file");
				buttonSaveToFile.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int rowIndex = getSelectedRow();
						if (rowIndex >= 0){
							final Iterator<Book> i = currentGroup.iterator();
							while(rowIndex -->0){
								i.next();
							}
							final Book book = i.next();
							final JFileChooser reader2 = new JFileChooser(".");
							final int file2 = reader2.showSaveDialog(GroupEditWindow.this);
							if (file2 == 0 ){
								try {
									Book.toFile(reader2.getSelectedFile().getName(), book);
									refreshList(currentGroup);
								} catch (BookException e1) {
									JOptionPane.showMessageDialog(GroupEditWindow.this, e1.getMessage(),"Error",0);
									refreshList(currentGroup);
								}
							}
						}

					}
				});
				buttonSaveToFile.setActionCommand("Cancel");
				buttonPane.add(buttonSaveToFile);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			getContentPane().add(scrollPane, BorderLayout.CENTER);
			{
				table = new JTable();
				table.setModel(new DefaultTableModel(
					new Object[][] {
					},
					new String[] {
						"Title", "Author", "Date of publication", "Genre", "Pages"
					}
				) {
					boolean[] columnEditables = new boolean[] {
						false, false, false, false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});
				scrollPane.setViewportView(table);
			}
			refreshList(currentGroup);

		}
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.NORTH);
			{
				JLabel labelNameOfGroup = new JLabel("Group:");
				panel.add(labelNameOfGroup);
			}
			{
				textGroupName = new JTextField();
				textGroupName.setEditable(false);
				panel.add(textGroupName);
				textGroupName.setColumns(10);
				textGroupName.setText(currentGroup.getName());
			}
			{
				JLabel labelCollectionType = new JLabel("Collection type");
				panel.add(labelCollectionType);
			}
			{
				textCollectionType = new JTextField();
				textCollectionType.setEditable(false);
				panel.add(textCollectionType);
				textCollectionType.setColumns(20);
				textCollectionType.setText(currentGroup.getType().toString());
			}
		}
		{
			JMenuBar menuBar = new JMenuBar();
			setJMenuBar(menuBar);
			{
				JMenu mmSort = new JMenu("Sort");
				menuBar.add(mmSort);
				{
					JMenuItem mmiteSortByTitle = new JMenuItem("Sort by title");
					mmiteSortByTitle.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try {
								currentGroup.sortTitle();
								refreshList(currentGroup);
							} catch (BookException e1) {
								JOptionPane.showMessageDialog(GroupEditWindow.this, e1.getMessage(),"Error",0);
							}
						}
					});
					mmSort.add(mmiteSortByTitle);
				}
				{
					JMenuItem mmiteSortByAuthor = new JMenuItem("Sort by author");
					mmiteSortByAuthor.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try{	
								currentGroup.sortAuthor();
								refreshList(currentGroup);
							}
							catch (BookException e1){
								JOptionPane.showMessageDialog(GroupEditWindow.this, e1.getMessage(),"Error",0);
							}
						}
					});
					mmSort.add(mmiteSortByAuthor);
				}
				{
					JMenuItem mmiteSortByGenre = new JMenuItem("Sort by genre");
					mmiteSortByGenre.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try{	
								currentGroup.sortGenre();
								refreshList(currentGroup);
							}
							catch (BookException e1){
								JOptionPane.showMessageDialog(GroupEditWindow.this, e1.getMessage(),"Error",0);
							}
						}
					});
					mmSort.add(mmiteSortByGenre);
				}
				{
					JMenuItem mmiteSortByDate = new JMenuItem("Sort by date");
					mmiteSortByDate.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try{	
								currentGroup.sortDate();
								refreshList(currentGroup);
							}
							catch (BookException e1){
								JOptionPane.showMessageDialog(GroupEditWindow.this, e1.getMessage(),"Error",0);
							}
						}
					});
					mmSort.add(mmiteSortByDate);
				}
				{
					JMenuItem mmiteSortByPages = new JMenuItem("Sort by pages");
					mmiteSortByPages.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try{	
								currentGroup.sortPages();
								refreshList(currentGroup);
							}
							catch (BookException e1){
								JOptionPane.showMessageDialog(GroupEditWindow.this, e1.getMessage(),"Error",0);
							}
						}
					});
					mmSort.add(mmiteSortByPages);
				}
			}
			{
				JMenu mmGroupProperties = new JMenu("Properties");
				menuBar.add(mmGroupProperties);
				{
					JMenuItem mmiteChangeGroupsName = new JMenuItem("Change name of the group");
					mmiteChangeGroupsName.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try{
								final String name = enterGroupName(GroupEditWindow.this);
								if (name == null){
									return;
								}
								currentGroup.setName(name);
								textGroupName.setText(name);
								refreshList(currentGroup);
							}
							catch (BookException e1){
								JOptionPane.showMessageDialog(GroupEditWindow.this, e1.getMessage(),"Error",0);
							}

						}
					});
					mmGroupProperties.add(mmiteChangeGroupsName);
				}
				{
					JMenuItem mmiteChangeGroupType = new JMenuItem("Change collection type");
					mmiteChangeGroupType.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							try{
								final GroupType type = chooseGroupType(GroupEditWindow.this, currentGroup.getType());
								if (type == null){
									return;
								}
								currentGroup.setType(type);
								textCollectionType.setText(type.toString());
								refreshList(currentGroup);
							}
							catch (BookException e1){
								JOptionPane.showMessageDialog(GroupEditWindow.this, e1.getMessage(),"Error",0);
							}
						}
					});
					mmGroupProperties.add(mmiteChangeGroupType);
				}
			}
		}
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	void refreshList(GroupOfBooks group){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setRowCount(0);
		for (final Book book :group ){
			final String[] row = {book.getTitle(),book.getAuthor(),book.getDateOfPublication(),book.getGenre().toString(),new StringBuilder().append(book.getPages()).toString()};
			model.addRow(row);
		}
	}
	int getSelectedRow(){
		final int rowIndex = this.table.getSelectedRow();
		if (rowIndex < 0){
			JOptionPane.showMessageDialog(this, "You did not select a book","error",0);
		}
		return rowIndex;
	}

	public static String enterGroupName(final Window parent){
        return JOptionPane.showInputDialog(parent, "Enter the name for the group");
    }

    public static GroupType chooseGroupType(final Window parent, final GroupType current_type){
        final Object[] options = GroupType.values();
        final GroupType type = (GroupType)JOptionPane.showInputDialog(parent, "Choose collection type", "Collection", 3, null, options, current_type);
        return type;
    }

    public static GroupOfBooks createNewGroupOfBooks(Window parent){
        final String name = enterGroupName(parent);
        if (name == null || name.equals("")){
            return null;
        }
        final GroupType type = chooseGroupType(parent, null);
        if (type== null){
            return null;
        }
        GroupOfBooks new_group;
        try{
            new_group = new GroupOfBooks(type,name);

        }
        catch (BookException error){
            JOptionPane.showMessageDialog(parent, error.getMessage(), "Error", 0);
            return null;
        }
        final GroupEditWindow dialog = new GroupEditWindow(parent, new_group);
		return dialog.currentGroup;
        

    }

	

}
