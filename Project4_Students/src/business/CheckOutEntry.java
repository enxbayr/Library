package business;

import java.io.Serializable;
import java.time.LocalDate;

public class CheckOutEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int FINE_PER_DAY = 2;
	private Book book;
	private BookCopy copy;
	private LibraryMember member;
	private LocalDate dueDate;
	private boolean hasReturned;
	private LocalDate outDate;
	private LocalDate returnedDate;
	private double fineAmount;

	public CheckOutEntry(Book b, BookCopy bc, LibraryMember member, LocalDate outDate, LocalDate returnedDate, String fineAmount) {
		this.book = b;
		this.copy = bc;
		this.member = member;
		this.outDate = outDate;
		this.dueDate = LocalDate.now().plusDays(b.getMaxCheckoutLength());
		if (returnedDate == null)
			this.hasReturned = false;
		else {
			this.hasReturned = true;
			this.returnedDate = returnedDate;
			//int days = Period.between(this.dueDate, returnedDate).getDays();
			this.fineAmount = Double.valueOf(fineAmount);
		}
	}

	public double getFineAmount()
	{
		return fineAmount;
	}
	public Book getBook() {
		return book;
	}

	public BookCopy getCopy() {
		return copy;
	}

	public LibraryMember getMember() {
		return member;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setHasReturned(boolean val) {
		this.hasReturned = val;
	}
	public void setFineAmount(double amount)
	{
		this.fineAmount = amount;
	}
	public void setReturnedDate(LocalDate date)
	{
		this.returnedDate = date;
	}

	public boolean getHasReturned() {
		return this.hasReturned;
	}

	public LocalDate getOutDate() {
		return this.outDate;
	}

	public LocalDate getReturnedDate() {
		return returnedDate;
	}
}
