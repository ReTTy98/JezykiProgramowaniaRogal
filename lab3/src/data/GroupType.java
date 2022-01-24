/*
Program: Program z GUI pozwalajacy na zarzadzanie grupami obiektów typu Book.
Plik: GroupType.java

-Stworzenie typu wyliczeniowego dla typu kolekcji 

Autor: Paul Paczyński
Data: 8.12.21
*/



package data;

import java.util.TreeSet;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Collection;



public enum GroupType {
	VECTOR("VECTOR",0,"List (Vector class)"),
	ARRAY_LIST("ARRAY_LIST",1,"List (Array list class"),
	LINKED_LIST("LINKED_LIST",2,"List (Linked list class)"),
	HASH_SET("HASH_SET",3,"Set (Hash set class"),
	TREE_SET("TREE_SET",4,"Set (Tree set class");
	
	String typeName;
	
	private GroupType(final String name, final int ordinal,final String type_name ) {
		this.typeName = type_name;
		
	}
	
	@Override
	public String toString() {
		return this.typeName;
	}

	public static GroupType lookFor(String type_name){
		for(GroupType type : values()){
			if(type.typeName.equals(type_name)){
				return type;
			}
		}
		return null;
	}
	
	public Collection<Book> createCollection() throws BookException {
		switch(this) {
		case VECTOR:return new Vector<Book>();
		case ARRAY_LIST:return new ArrayList<Book>();
		case LINKED_LIST:return new LinkedList<Book>();
		case HASH_SET:return new HashSet<Book>();
		case TREE_SET:return new TreeSet<Book>();
		default: throw new BookException("Type does not exist");
		
		}
	}
	

}
