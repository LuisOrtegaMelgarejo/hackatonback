package com.oip.helpdesk.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.oip.helpdesk.domain.entities.*;
import com.oip.helpdesk.repository.*;
import com.oip.helpdesk.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.service.UserService;

import org.springframework.security.core.Authentication;

import static com.oip.helpdesk.security.Constants.SECRET;
import static com.oip.helpdesk.security.Constants.TOKEN_PREFIX;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class TicketController {

	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AreaRepository areaRepository;
	@Autowired
	UserService userService;
	@Autowired
	ChannelRepository channelRepository;
	@Autowired
	PriorityRepository priorityRepository;
	@Autowired
	ServiceRepository serviceRepository;
	@Autowired
	TicketStatusRepository ticketStatusRepository;
	@Autowired
	TicketTypeRepository ticketTypeRepository;
	@Autowired
	AlertRepository alertRepository;
	@Autowired
	TicketService ticketService;
	@Autowired
	ParameterRepository parameterRepository;

	@Autowired
	public JavaMailSender emailSender;

	@PostConstruct
	public void init(){
		JavaMailSenderImpl ms = (JavaMailSenderImpl) emailSender;
		ms.setHost(parameterRepository.findByCode("host").getValue());
		ms.setPort(Integer.parseInt(parameterRepository.findByCode("port").getValue()));
		ms.setUsername(parameterRepository.findByCode("username").getValue());
		ms.setPassword(parameterRepository.findByCode("password").getValue());
	}

	private final static Logger LOGGER = Logger.getLogger("com.oip.helpdesk.controller.ticketController");

	@GetMapping("/tickets/{ticketId}")
	public  Ticket getTicketById(@RequestHeader("Authorization") String header, @PathVariable (value = "ticketId") Long ticketId ){
		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket","ticketId", ticketId));
		return  ticket;
	}
	//*************Get tickets  by user*******************
	@PostMapping("/tickets")
	public HashMap<String,List<Ticket>> getAllTicketsByUserId(@RequestHeader("Authorization") String header , @RequestBody (required=false) HashMap<String,String> filters) {

		User user = userService.findByToken(header);
		LOGGER.log(Level.INFO, "Rol de Usuario " + user.getRoles().iterator().next().getName());
		HashMap<String,List<Ticket>> data = new HashMap<>();

		if(user.getRoles().iterator().next().getName().equals("superadmin")){

			List<Ticket> tickets_to_me  = ticketRepository.findAssignedTickets(user.getId());
			List<Ticket> tickets_null  = ticketRepository.findNullTickets();
			List<Ticket> tickets_all = ticketRepository.findAll();

			if(filters!=null){
				if(filters.get("priority_id")!=null){
					tickets_to_me = tickets_to_me.stream().filter(p -> filters.get("priority_id").equals(p.getPriority().getId().toString())).collect(Collectors.toList());
					tickets_null = tickets_null.stream().filter(p -> filters.get("priority_id").equals(p.getPriority().getId().toString())).collect(Collectors.toList());
					tickets_all = tickets_all.stream().filter(p -> filters.get("priority_id").equals(p.getPriority().getId().toString())).collect(Collectors.toList());
				}if(filters.get("status_id")!=null){
					tickets_to_me = tickets_to_me.stream().filter(p -> filters.get("status_id").equals(p.getStatus().getId().toString())).collect(Collectors.toList());
					tickets_null = tickets_null.stream().filter(p -> filters.get("status_id").equals(p.getStatus().getId().toString())).collect(Collectors.toList());
					tickets_all = tickets_all.stream().filter(p -> filters.get("status_id").equals(p.getStatus().getId().toString())).collect(Collectors.toList());
				}if(filters.get("title")!=null){
					tickets_to_me = tickets_to_me.stream().filter(p -> p.getTitle().contains(filters.get("title"))).collect(Collectors.toList());
					tickets_null = tickets_null.stream().filter(p -> p.getTitle().contains(filters.get("title"))).collect(Collectors.toList());
					tickets_all = tickets_all.stream().filter(p -> p.getTitle().contains(filters.get("title"))).collect(Collectors.toList());
				}
			}

			data.put("me",tickets_to_me);
			data.put("null",tickets_null);
			data.put("all",tickets_all);

		}else if(user.getRoles().iterator().next().getName().equals("admin")){

			List<Ticket> tickets_to_me  = ticketRepository.findAssignedTickets(user.getId());

			if(filters!=null){
				if(filters.get("priority_id")!=null){
					tickets_to_me = tickets_to_me.stream().filter(p -> filters.get("priority_id").equals(p.getPriority().getId().toString())).collect(Collectors.toList());
				}if(filters.get("status_id")!=null){
					tickets_to_me = tickets_to_me.stream().filter(p -> filters.get("status_id").equals(p.getStatus().getId().toString())).collect(Collectors.toList());
				}if(filters.get("title")!=null){
					tickets_to_me = tickets_to_me.stream().filter(p -> p.getTitle().contains(filters.get("title"))).collect(Collectors.toList());
				}
			}

			data.put("me",tickets_to_me);

		}else{

			List<Ticket> tickets_created_me  = ticketRepository.findByUserId(user.getId());
			if(filters!=null){
				if(filters.get("priority_id")!=null){
					tickets_created_me = tickets_created_me.stream().filter(p -> filters.get("priority_id").equals(p.getPriority().getId().toString())).collect(Collectors.toList());
				}if(filters.get("status_id")!=null){
					tickets_created_me = tickets_created_me.stream().filter(p -> filters.get("status_id").equals(p.getStatus().getId().toString())).collect(Collectors.toList());
				}if(filters.get("title")!=null){
					tickets_created_me = tickets_created_me.stream().filter(p -> p.getTitle().contains(filters.get("title"))).collect(Collectors.toList());
				}
			}

			data.put("me",tickets_created_me);

		}
		return data;

	}

	@PostMapping("/tickets/save")
	public ResponseEntity<?> createTicket (@RequestHeader("Authorization") String header,@RequestBody HashMap<String,String> body) {

		try{

			User user = userService.findByToken(header);
			Ticket ticket = new Ticket();

			Channel channel = channelRepository.findById(Long.parseLong(body.get("channel_id"))).orElseThrow(() -> new ResourceNotFoundException("Channel","channel_id", body.get("channel_id")));
			ticket.setChannel(channel);
			Priority priority = priorityRepository.findById(Long.parseLong(body.get("priority_id"))).orElseThrow(() -> new ResourceNotFoundException("Priority","priority_id", body.get("priority_id")));
			ticket.setPriority(priority);
			Service service = serviceRepository.findById(Long.parseLong(body.get("service_id"))).orElseThrow(() -> new ResourceNotFoundException("Priority","priority_id", body.get("priority_id")));
			ticket.setService(service);
			Status status = ticketStatusRepository.findById(new Long(1)).orElseThrow(() -> new ResourceNotFoundException("Status","status_id",1));
			ticket.setStatus(status);
			TicketType ticketType = ticketTypeRepository.findById(Long.parseLong(body.get("type_id"))).orElseThrow(() -> new ResourceNotFoundException("Type","type_id", body.get("type_id")));
			ticket.setType(ticketType);
			ticket.setTitle(body.get("title"));
			ticket.setSla(Boolean.parseBoolean(body.get("sla")));
			ticket.setDescription(body.get("description"));

			Alert alert = new Alert();

			if(user.getRoles().iterator().next().getName().equals("user")){
				ticket.setUser(user);
				ticket.setArea(user.getArea());
				alert.setUserId(user.getId());
			}else{
				
				User solicitado =  userService.findById(Long.parseLong(body.get("user_id"))).orElseThrow(() -> new ResourceNotFoundException("User","user_id", body.get("user_id")));

				if(solicitado.getRoles().iterator().next().getName().equals("user")){
					ticket.setUser(solicitado);
					ticket.setArea(solicitado.getArea());
				}else {
					return ResponseEntity.badRequest().build();
				}
				alert.setUserId(solicitado.getId());

			}

			ticketRepository.save(ticket);

			alert.setMessage("Su ticket "+String.format("%05d", ticket.getId())+" - "+ticket.getTitle()+" ha sido creado exitosamente");
			alertRepository.save(alert);
			sendSimpleMessage(ticket,alert);


			List<User> admins = userRepository.findAdmins();
			for(User adm: admins){
				Alert alert2 = new Alert();
				alert2.setMessage("Se ha creado el ticket "+String.format("%05d", ticket.getId())+" - "+ticket.getTitle()+", debe ser asignado para su atención");
				alert2.setUserId(adm.getId());
				alertRepository.save(alert2);

			}

			return ResponseEntity.ok().build();
		}catch (Exception e){
			return ResponseEntity.badRequest().build();
		}

	}

	@PatchMapping("/tickets/assign/{ticketId}")
	public ResponseEntity<?> assignTicket(@RequestHeader("Authorization") String header,
										  @PathVariable (value = "ticketId") Long ticketId,
										  @RequestBody HashMap<String,String> body) {

		User user = userService.findByToken(header);
		Ticket ticket = ticketRepository.findBySpecificId(ticketId);

		if(ticket.getAsignado()!=null && !user.getRoles().iterator().next().getName().equals("superadmin")){
			return ResponseEntity.badRequest().build();
		}

		User asignado =  userService.findById(Long.parseLong(body.get("assigned_to"))).orElseThrow(() -> new ResourceNotFoundException("User","assigned_to", body.get("assigned_to")));

		if(asignado.getRoles().iterator().next().getName().equals("admin") || asignado.getRoles().iterator().next().getName().equals("superadmin")){
			ticket.setAsignado(asignado);
		}else {
			return ResponseEntity.badRequest().build();
		}

		Status status = ticketStatusRepository.findById(new Long(2)).orElseThrow(() -> new ResourceNotFoundException("Status","status_id",1));
		ticket.setStatus(status);
		ticketRepository.save(ticket);

		Alert alert = new Alert();
		alert.setMessage("Has sido asignado al ticket "+String.format("%05d", ticket.getId())+" - "+ticket.getTitle());
		alert.setUserId(asignado.getId());
		alertRepository.save(alert);

		Alert alert2 = new Alert();
		alert2.setMessage("Su ticket "+String.format("%05d", ticket.getId())+" - "+ticket.getTitle()+ " ha sido asignado y pasara a estado PENDIENTE");
		alert2.setUserId(ticket.getUser().getId());
		alertRepository.save(alert2);

		sendSimpleMessage(ticket,alert);
		sendSimpleMessage(ticket,alert2);

		return ResponseEntity.ok().build();
	}

	@Transactional
	@PutMapping("/tickets/{ticketId}")
	public ResponseEntity<?> updateticket (@RequestHeader("Authorization") String header,@PathVariable (value = "ticketId") Long ticketId,@RequestBody HashMap<String,String> body) {
		User user = userService.findByToken(header);

		Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket","ticketId", ticketId));
		Ticket ticketOriginal = new Ticket(ticket);
		try {
			if (body.containsKey("description")){
				ticket.setDescription(body.get("description"));
			}
			if (body.containsKey("title")){
				ticket.setTitle(body.get("title"));
			}
			if (body.containsKey("status_id")){
				Status status = ticketStatusRepository.findById(Long.parseLong(body.get("status_id"))).orElseThrow(() -> new ResourceNotFoundException("Status","status_id",1));
				List<Status> statusAvailable = ticketService.getStatusAvailable(ticket,user);

				if (statusAvailable.contains(status)){
					ticket.setStatus(status);
				}else {
					return ResponseEntity.badRequest().build();
				}
			}
			if (body.containsKey("service_id")){
				Service service = serviceRepository.findById(Long.parseLong(body.get("service_id"))).orElseThrow(() -> new ResourceNotFoundException("Service","service_id",1));
				ticket.setService(service);
			}
			if (body.containsKey("type_id")){
				TicketType type = ticketTypeRepository.findById(Long.parseLong(body.get("type_id"))).orElseThrow(() -> new ResourceNotFoundException("Type","type_id",1));
				ticket.setType(type);
			}

			ticketRepository.save(ticket);

			if (!ticketService.getDifferences(ticketOriginal,ticket,user).isEmpty()){
				Alert alert = new Alert();
				alert.setMessage(ticketService.getDifferences(ticketOriginal,ticket,user));
				if (user.getRoles().iterator().next().getName().equals("user")){
					if (ticketOriginal.getAsignado()!=null){
						alert.setUserId(ticketOriginal.getAsignado().getId());
					}else {
						return ResponseEntity.ok().build();
					}
				}else{
					alert.setUserId(ticketOriginal.getUser().getId());
				}
				alertRepository.save(alert);
				sendSimpleMessage(ticket,alert);
			}

		}catch (Exception e){
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok().build();

	}
	 
	 
  	@DeleteMapping("/users/{userId}/tickets/{ticketId}")
	public ResponseEntity<?> deleteTicket(@PathVariable (value = "userId") Long userId,@PathVariable (value = "ticketId") Long ticketId) {
		if(!userRepository.existsById(userId)) {
			throw new ResourceNotFoundException("User","user_id", userId);
		}

		return ticketRepository.findById(ticketId).map(ticket -> {
			 ticketRepository.delete(ticket);
			 return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("Ticket","ticketId", ticketId));
	}

	@GetMapping("/tickets/statusAvailable/{ticketId}")
	public List<Status> getStatisAvailable(@RequestHeader("Authorization") String header,@PathVariable (value = "ticketId") Long ticketId){
		User user = userService.findByToken(header);
		Ticket ticket = ticketRepository.findBySpecificId(ticketId);
		List<Status> status = ticketService.getStatusAvailable(ticket,user);
		return status;
	}

	@PostMapping("/tickets/reporting")
	public ResponseEntity<ArrayList<HashMap<String,Object>>> dataReportingRest(@RequestHeader("Authorization") String header,@RequestBody HashMap<String,String> body){
		User user = userService.findByToken(header);
		ArrayList<HashMap<String,Object>> data;
		if(user.getRoles().iterator().next().getName().equals("superadmin")){
			data = ticketService.groupTicketsBy(user,body.get("parameter"),body.get("start"),body.get("end"));
		}else {
			return ResponseEntity.status(403).build();
		}
		return ResponseEntity.ok(data);
	}


    @PostMapping("/tickets/dashboards")
    public ResponseEntity<ArrayList<HashMap<String,Object>>> dataReportingRest(@RequestHeader("Authorization") String header){
        User user = userService.findByToken(header);
        ArrayList<HashMap<String,Object>> data = ticketService.groupTicketsBy(user,"ALL","1900-01-01","2999-01-01");
        return ResponseEntity.ok(data);
    }

	public void sendSimpleMessage(Ticket ticket,Alert alert) {
		System.out.println("se trata de enviar el correo");
		SimpleMailMessage message = new SimpleMailMessage();
		User user = userRepository.findById(alert.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User","userId", alert.getUserId()));
		message.setTo(user.getEmail());
		message.setFrom(parameterRepository.findByCode("from").getValue());
		message.setSubject("Ticket "+String.format("%05d", ticket.getId()));
		message.setText(alert.getMessage());
		try{
			emailSender.send(message);
			System.out.println("se envio");
		}catch (Exception e){
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace().toString());
		}
	}
}
