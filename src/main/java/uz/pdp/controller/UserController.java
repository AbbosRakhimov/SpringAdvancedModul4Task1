package uz.pdp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import uz.pdp.entity.Card;
import uz.pdp.entity.Income;
import uz.pdp.entity.Outcome;
import uz.pdp.entity.User;
import uz.pdp.repository.CardRepository;
import uz.pdp.repository.IncomeRepository;
import uz.pdp.repository.OutcomeRepository;
import uz.pdp.repository.UserRepository;
import uz.pdp.service.MyAuthService;
import uz.pdp.service.UserService;
import uz.pdp.template.CardDto;
import uz.pdp.template.IncomeDto;
import uz.pdp.template.OutcomeDto;
import uz.pdp.template.Response;
import uz.pdp.template.UserDto;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	IncomeRepository incomeRepository;
	
	@Autowired
	OutcomeRepository outcomeRepository;
	
	@Autowired
	CardRepository cardRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@PostMapping
	public HttpEntity<?> addUser(@Valid @RequestBody UserDto user) {
		Response response = userService.addUser(user);
		 return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
	}
	
	@PostMapping("/login")
	public HttpEntity<?> loginToSystem(@Valid @RequestBody UserDto user){
		 Response response =userService.loadUserToSystem(user);
		 return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
	}
	@PostMapping(value = "/card")
	public HttpEntity<?> addCard(@Valid @RequestBody CardDto cardDto){
		Response response = userService.addCard(cardDto);
		return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
	}
	@GetMapping(value = "/cardAll")
	public HttpEntity<?> getAllCards(){
		List<Card> cards = userService.getCards();
		return ResponseEntity.status(cards.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.OK).body(cards);
	}
	@PostMapping(value = "/transferoutcome")
	public HttpEntity<?> transferOutcome(@Valid @RequestBody OutcomeDto outcomeDto){
		Response response = userService.transferOutcome(outcomeDto);
		return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
	}
	@PostMapping(value = "/transferincome")
	public HttpEntity<?> transferOutcome(@Valid @RequestBody IncomeDto incomeDto){
		Response response = userService.transferIncomecome(incomeDto);
		return ResponseEntity.status(response.isSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(response);
	}
	@GetMapping(value = "/outcome")
	public HttpEntity<?> getAllOutcome(){
		List<Outcome> outcomes = userService.getAllOutcome();
		return ResponseEntity.status(outcomes.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.OK).body(outcomes);
	}
	@GetMapping(value = "/income")
	public HttpEntity<?> getAllIncome(){
		List<Income> incomes = userService.getAllIncome();
		return ResponseEntity.status(incomes.isEmpty()?HttpStatus.NOT_FOUND:HttpStatus.OK).body(incomes);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}
}
