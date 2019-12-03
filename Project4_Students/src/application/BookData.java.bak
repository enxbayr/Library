package application;

import business.Book;

final public class BookData {
	private final String title;
	private final String authors;
	private final String isbn;
	private final String number;
	
	public BookData(Book book) {
		this.title = book.getTitle();
		this.authors = book.getAuthors().toString();
		this.isbn = book.getIsbn();
		this.number = Integer.toString(book.getNumCopies());
	}

	public String getTitle() {
		return title;
	}

	public String getAuthors() {
		return authors;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getNumber() {
		return number;
	}
	
	
	
}
