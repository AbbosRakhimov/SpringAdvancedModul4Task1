package uz.pdp.template;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class IncomeDto {

	@NotNull(message =" Username is mandatory")
	private String fullUserName;
	
	@NotNull(message =" From_card_id is mandatory")
	private  Integer from_card_id;
	
	@NotNull(message =" To_card_id is mandatory")
	private Integer to_card_id;
	
	@NotNull(message =" Balance is mandatory")
	private Double balance;
}
