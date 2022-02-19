package uz.pdp.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Outcome {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Card from_card_id;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Card to_card_id;
	
	@Column(nullable = false)
	private Double amount;
	
	@Column(nullable = false)
	private Date date;

	public Outcome(Card from_card_id, Card to_card_id, Double amount, Date date) {
		super();
		this.from_card_id = from_card_id;
		this.to_card_id = to_card_id;
		this.amount = amount;
		this.date = date;
	}
	
	
}
