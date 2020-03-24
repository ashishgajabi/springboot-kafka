package au.com.easynebula.clienttransactionmanagement.domain;

import au.com.easynebula.clienttransactionmanagement.utils.FixedWidthColumn;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class FutureTransaction {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@FixedWidthColumn(exclude = true)
	private String id;

	@FixedWidthColumn(startAt = 3, width = 4)
	@Column(length = 4)
	private String clientType;

	@FixedWidthColumn(startAt = 7, width = 4)
	@Column(length = 4)
	private String clientNumber;

	@FixedWidthColumn(startAt = 11, width = 4)
	@Column(length = 4)
	private String accountNumber;

	@FixedWidthColumn(startAt = 15, width = 4)
	@Column(length = 4)
	private String subAccountNumber;

	@FixedWidthColumn(startAt = 25, width = 2)
	@Column(length = 2)
	private String productGroupCode;

	@FixedWidthColumn(startAt = 27, width = 4)
	@Column(length = 4)
	private String exchangeCode;

	@FixedWidthColumn(startAt = 31, width = 6)
	@Column(length = 6)
	private String symbol;

	@FixedWidthColumn(startAt = 37, width = 8, isDate = true)
	@Basic
	private LocalDate expirationDate;

	@FixedWidthColumn(startAt = 52, width = 10)
	@Column(length = 10)
	private String quantityLong;

	@FixedWidthColumn(startAt = 63, width = 10)
	@Column(length = 10)
	private String quantityShort;

	@FixedWidthColumn(startAt = 121, width = 8, isDate = true)
	@Column(length = 10)
	private LocalDate transactionDate;

	public String getId() {
		return id;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(final String clientType) {
		this.clientType = clientType;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(final String clientNumber) {
		this.clientNumber = clientNumber;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(final String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getSubAccountNumber() {
		return subAccountNumber;
	}

	public void setSubAccountNumber(final String subAccountNumber) {
		this.subAccountNumber = subAccountNumber;
	}

	public String getProductGroupCode() {
		return productGroupCode;
	}

	public void setProductGroupCode(final String productGroupCode) {
		this.productGroupCode = productGroupCode;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(final String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(final String symbol) {
		this.symbol = symbol;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(final LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getQuantityLong() {
		return quantityLong;
	}

	public void setQuantityLong(final String quantityLong) {
		this.quantityLong = quantityLong;
	}

	public String getQuantityShort() {
		return quantityShort;
	}

	public void setQuantityShort(final String quantityShort) {
		this.quantityShort = quantityShort;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(final LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public String toString() {
		return "FutureTransaction{" +
				"id='" + id + '\'' +
				", clientType='" + clientType + '\'' +
				", clientNumber='" + clientNumber + '\'' +
				", accountNumber='" + accountNumber + '\'' +
				", subAccountNumber='" + subAccountNumber + '\'' +
				", productGroupCode='" + productGroupCode + '\'' +
				", exchangeCode='" + exchangeCode + '\'' +
				", symbol='" + symbol + '\'' +
				", expirationDate=" + expirationDate +
				", quantityLong='" + quantityLong + '\'' +
				", quantityShort='" + quantityShort + '\'' +
				", transactionDate=" + transactionDate +
				'}';
	}
}
