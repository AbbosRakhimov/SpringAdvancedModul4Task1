package uz.pdp.template;




import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
public class CardDto {

	@NotNull(message = "Cardname is mandatory")
	private String cardName;
	
	@NotNull(message = "Balance is mandatory")
	private Double balance;
	
	@NotNull(message = "UserId is mandatory")
	private Integer userId;
}
