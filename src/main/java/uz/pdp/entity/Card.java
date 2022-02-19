package uz.pdp.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username", "number"}))
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String cardName;
	
	@Column(nullable = false)
	private String userName;
	
	@Column(nullable = false)
	private Integer number;
	
	private Double balance;
	
	@Column(nullable = false)
	private Date expired_date;
	
	@Column(nullable = false)
	private Boolean active; 
	
	@OneToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	public Card(String cardName, String userName, Integer number, Double balance, Date expired_date, Boolean active,
			User user) {
		super();
		this.cardName = cardName;
		this.userName = userName;
		this.number = number;
		this.balance = balance;
		this.expired_date = expired_date;
		this.active = active;
		this.user = user;
	}
	
	
}
