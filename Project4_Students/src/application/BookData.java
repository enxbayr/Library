package application;

import business.Book;

final public class BookData {
	private String title;
	private String authors;
	private String isbn;
	private String number;
	private String maxDay;
	private String isAvailable;
	private String copyNum;
	public BookData(Book book) {
		this.title = book.getTitle();
		this.authors = book.getAuthors().toString();
		this.isbn = book.getIsbn();
		this.number = Integer.toString(book.getNumCopies());
		this.maxDay = Integer.toString(book.getMaxCheckoutLength());
	}
	public BookData()
	{}

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
	
	public String getMaxDay() {
		return maxDay;
	}
	
	@Override
	public String toString() {
		return title + authors + isbn + number;
	}

	public String getIsAvailable() {
		return isAvailable;
	}

	public String getCopyNum() {
		return copyNum;
	}

	public void setIsAvailable(String isAvailable) {
		this.isAvailable = isAvailable;
	}

	public void setCopyNum(String copyNum) {
		this.copyNum = copyNum;
	}
	
}
