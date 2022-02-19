package uz.pdp.service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uz.pdp.entity.Card;
import uz.pdp.entity.Income;
import uz.pdp.entity.Outcome;
import uz.pdp.entity.User;
import uz.pdp.repository.CardRepository;
import uz.pdp.repository.IncomeRepository;
import uz.pdp.repository.OutcomeRepository;
import uz.pdp.repository.UserRepository;
import uz.pdp.securityconfig.JwtProvider;
import uz.pdp.template.CardDto;
import uz.pdp.template.IncomeDto;
import uz.pdp.template.OutcomeDto;
import uz.pdp.template.Response;
import uz.pdp.template.UserDto;

@Service
public class UserService {

	@Autowired
	MyAuthService myAuthService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	CardRepository cardRepository;
	
	@Autowired
	OutcomeRepository outcomeRepository;
	
	@Autowired
	IncomeRepository incomeRepository;

	public Response loadUserToSystem(UserDto user) {
		UserDetails userDetails = myAuthService.loadUserByUsername(user.getUserFullName());
		if (userDetails == null)
			return new Response("User not found", false);
		if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
			String token = jwtProvider.generateToken(user.getUserFullName());
			return new Response(token, true);
		}
		return new Response("password wrong", false);
	}

	
	public Response addUser(UserDto user) {
		if(userRepository.existsByUserFullName(user.getUserFullName()))
			return new Response("User exist", false);
		if(user.getPassword().length()<8)
			return new Response("Password cannot be less than 8 characters", false);
		String encode = passwordEncoder.encode(user.getPassword());
		User user2 = new User(user.getUserFullName(), encode);
		userRepository.save(user2);
		return new Response("User successfully added", true);
	}

	public Response addCard(CardDto cardDto) {
		Optional<User> uOptional = userRepository.findById(cardDto.getUserId());
		if (!uOptional.isPresent())
			return new Response("User not found", false);
		UUID myuuid = UUID.randomUUID();
		Integer number = (int) myuuid.getMostSignificantBits();
		if (number < 0) {
			number = number * (-1);
		}
		if (cardRepository.existsByUserNameOrNumber(uOptional.get().getUserFullName(), number))
			return new Response("Card exist", false);

		if (cardDto.getBalance() < 0) {
			return new Response("Balance cannot be less than 0", false);
		}
		Calendar cal = Calendar.getInstance();
		Date expired_date = new Date();
		cal.setTime(expired_date);
		cal.add(Calendar.YEAR, 3);
		expired_date = cal.getTime();

		Card card = new Card(cardDto.getCardName(), uOptional.get().getUserFullName(), number, cardDto.getBalance(),
				expired_date, true, uOptional.get());
		cardRepository.save(card);

		return new Response("Card added", true);
	}
	
	public Response transferOutcome(OutcomeDto outcomeDto) {
		if(outcomeDto.getFrom_card_id()==outcomeDto.getTo_card_id())
			return new Response("It is the same Card", false);
		Optional<Card> fromCaOptional = cardRepository.findById(outcomeDto.getFrom_card_id());
		if(!fromCaOptional.isPresent())
			return new Response("Your Card not exist", false);
		Optional<Card> toCaOptional = cardRepository.findById(outcomeDto.getTo_card_id());
		if(!toCaOptional.isPresent())
			return new Response("Card, which should receive the money, does not exist", false);
		if(!fromCaOptional.get().getUserName().equals(outcomeDto.getFullUserName()))
			return new Response("This Card is not yours", false);
		if(fromCaOptional.get().getBalance()<outcomeDto.getBalance())
			return new Response("Your credit is insufficient.", false);
		Double currentBalanceForFromCard = fromCaOptional.get().getBalance()-outcomeDto.getBalance();
		Card card = fromCaOptional.get();
		card.setBalance(currentBalanceForFromCard);
		cardRepository.save(card);
		Double currentBalanceForToCard=toCaOptional.get().getBalance()+outcomeDto.getBalance();
		Card card2 = toCaOptional.get();
		card2.setBalance(currentBalanceForToCard);
		cardRepository.save(card2);
		
		Outcome outcome = new Outcome(fromCaOptional.get(), toCaOptional.get(),(outcomeDto.getBalance()*(-1)),new Date());
		outcomeRepository.save(outcome);
		
		return new Response("Outcome successfuly added", true); 
	}
	 public List<Outcome> getAllOutcome() {
		 return outcomeRepository.findAll();
	 }
	public List<Card> getCards(){
		return cardRepository.findAll();
	}
	
	public Response transferIncomecome(IncomeDto incomeDto) {
		if(incomeDto.getFrom_card_id()==incomeDto.getTo_card_id())
			return new Response("It is the same Card", false);
		Optional<Card> fromCaOptional = cardRepository.findById(incomeDto.getFrom_card_id());
		if(!fromCaOptional.isPresent())
			return new Response("Your Card not exist", false);
		Optional<Card> toCaOptional = cardRepository.findById(incomeDto.getTo_card_id());
		if(!toCaOptional.isPresent())
			return new Response("Card, which should receive the money, does not exist", false);
		if(!toCaOptional.get().getUserName().equals(incomeDto.getFullUserName()))
			return new Response("This Card is not yours", false);
		if(toCaOptional.get().getBalance()<incomeDto.getBalance())
			return new Response("Your credit is insufficient.", false);
		Double currentBalanceForFromCard = fromCaOptional.get().getBalance()-incomeDto.getBalance();
		Card card = toCaOptional.get();
		card.setBalance(currentBalanceForFromCard);
		cardRepository.save(card);
		Double currentBalanceForToCard=toCaOptional.get().getBalance()+incomeDto.getBalance();
		Card card2 = toCaOptional.get();
		card2.setBalance(currentBalanceForToCard);
		cardRepository.save(card2);
		
		Income income = new Income(fromCaOptional.get(), toCaOptional.get(),(incomeDto.getBalance()),new Date());
		incomeRepository.save(income);
		
		return new Response("Income successfuly added", true); 
	}
	
	 public List<Income> getAllIncome() {
		 return incomeRepository.findAll();
	 }
	
//	authenticationManager
//	.authenticate(new UsernamePasswordAuthenticationToken(user.getUserFullName(), user.getPassword()));
}
