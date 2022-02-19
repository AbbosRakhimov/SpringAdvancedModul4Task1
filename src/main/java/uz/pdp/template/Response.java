package uz.pdp.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response {

	private String message;
	
	private boolean success;

	public Response(boolean success) {
		super();
		this.success = success;
	}
	
	
}
