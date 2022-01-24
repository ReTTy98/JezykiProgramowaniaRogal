/*
Program: Program z GUI pozwalajacy na zarzadzanie grupami obiektów typu Book.
Plik: GroupOfBooks.java

-Operacje tworzenia grupy ksiązek
-metody pozwalajace na porownywanie i sortowanie kolekcji dzieki komparatorom

Autor: Paul Paczyński
Data: 8.12.21
*/


package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;


public class GroupOfBooks implements Iterable<Book>, Serializable{
	
	private String name;
	private GroupType type;
	private Collection<Book> collection;
	
	public GroupOfBooks(final String type_name, final String name) throws BookException{
		this.setName(name);
		GroupType type = GroupType.lookFor(type_name);
		if (type == null){
			throw new BookException("Wrong collection type");
		}
		this.type = type;
		collection = this.type.createCollection();

		
	}

	public GroupOfBooks(final GroupType type, final String name) throws BookException{
		this.setName(name);
		if (type == null){
			throw new BookException("Wrong collection type");
		}
		this.type = type;
		collection = this.type.createCollection();
	}
	
	
	
	public void setName(String name) throws BookException {
		if ((name == null) || name.equals("")){
			throw new BookException("Group name must not be empty");
		}
		this.name = name;
		

	}

	public void setType(GroupType type) throws BookException{
		if (type == null){
			throw new BookException("Collection type must be known");
		}
		if (this.type == type){
			return;
		}

		Collection<Book> previousCollection = collection;
		collection = type.createCollection();
		this.type = type;
		for (Book book : previousCollection){
			collection.add(book);
		}
	}

	public void setType(String type_name) throws BookException {
		for(GroupType type : GroupType.values()){
			if (type.toString().equals(type_name)) {
				setType(type);
				return;
			}
		}
		throw new BookException("This type does not exist");
	}

	public boolean add(final Book book){
		return this.collection.add(book);
	}

	public String getName(){
		return this.name;
	}
	public GroupType getType(){
		return this.type;
	}
	
	public int size(){
		return this.collection.size();
	}

	public static GroupOfBooks fromFile(final BufferedReader reader) throws BookException{
		try{
			final String group_name = reader.readLine();
			final String group_type = reader.readLine();
			final GroupOfBooks groupOfBooks = new GroupOfBooks(group_type, group_name);
			Book book;

			while ((book = Book.fromFile(reader)) != null){
				groupOfBooks.collection.add(book);
			}
			return groupOfBooks;

		}
		catch (IOException error){
			throw new BookException("Couldnt load from file, i dont know why :/");
		}
	}

	public static GroupOfBooks fromFile(final String file_name)throws BookException{
		try{
			BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)));
			return GroupOfBooks.fromFile(reader);
		}
		catch (FileNotFoundException error){
			throw new BookException("File not found");
		}
		catch (IOException error){
			throw new BookException("No idea what happened");
		}

		

	}

	public static void toFile(final PrintWriter writer, final GroupOfBooks group){
		writer.println(group.getName());
		writer.println(group.getType());
		for (final Book book : group.collection){
			Book.toFile(writer, book);
		}
	}

	public static void toFile(final String file_name, final GroupOfBooks group) throws BookException{
		System.out.println(file_name);
		try{
			final PrintWriter writer = new PrintWriter(file_name);
			toFile(writer, group);
			if(writer != null){

				writer.close();
			}
		}
		catch (NullPointerException error){
			throw new BookException("No data to save");
		}
		catch (FileNotFoundException error){
			throw new BookException("File not found");
		}
		

		
	}

	Comparator<Book> compareByTitle = new Comparator<Book>() {
		@Override
		public int compare(Book b1, Book b2){
			return b1.getTitle().compareTo(b2.getTitle());
		}
	};

	Comparator<Book> compareByAuthor = new Comparator<Book>() {
		@Override
		public int compare(Book b1, Book b2){
			return b1.getAuthor().compareTo(b2.getAuthor());
		}
	};

	Comparator<Book> compareByDate = new Comparator<Book>() {
		@Override
		public int compare(Book b1, Book b2){
			int date1 = Integer.parseInt(b1.getDateOfPublication());
			int date2 = Integer.parseInt(b2.getDateOfPublication());
			return Integer.compare(date1, date2);
		}
	};

	Comparator<Book> compareByPages = new Comparator<Book>() {
		@Override
		public int compare(Book b1, Book b2){
			return Integer.compare(b1.getPages(), b2.getPages());
		}
	};

	Comparator<Book> compareByGenre = new Comparator<Book>() {
		@Override
		public int compare(Book b1, Book b2){
			return b1.getGenre().compareTo(b2.getGenre());
		}
	};

	public void checkType(GroupType GroupType)  throws BookException{
		if (GroupType == data.GroupType.HASH_SET || GroupType == data.GroupType.TREE_SET){
			throw new BookException("Sets cant be sorted");
		}	
	}

	public void sortTitle() throws BookException{
		checkType(this.type);
		Collections.sort((List<Book>) this.collection, compareByTitle);
	}

	public void sortAuthor() throws BookException {
		checkType(this.type);
		Collections.sort((List<Book>) this.collection, compareByAuthor);
	}
	
	public void sortDate() throws BookException{
		checkType(this.type);
		Collections.sort((List<Book>) this.collection, compareByDate);
	}

	public void sortPages() throws BookException{
		checkType(this.type);
		Collections.sort((List<Book>) this.collection, compareByPages);
	}

	public void sortGenre() throws BookException{
		checkType(this.type);
		Collections.sort((List<Book>) this.collection, compareByGenre);
	}

	public void printGroup(){
		for (Book book : this.collection  ){
			System.out.println(book.getTitle());
		}
		
	}


	@Override
	public Iterator<Book> iterator() {
		return collection.iterator();
	}


	

}
