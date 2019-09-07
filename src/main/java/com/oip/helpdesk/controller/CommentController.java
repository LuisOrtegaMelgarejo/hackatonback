package com.oip.helpdesk.controller;

import java.io.File;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import com.oip.helpdesk.domain.entities.*;
import com.oip.helpdesk.repository.*;
import com.oip.helpdesk.service.UserService;
import com.oip.helpdesk.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;

import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CommentController {

	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserService userService;
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	AlertRepository alertRepository;
	@Autowired
	FilesRepository filesRepository;
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

	@Autowired
	UserRepository userRepository;

	private final StorageService storageService;

	@Autowired
	public CommentController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/comments")
	public List<Comment> getAllComment() {	
		return this.commentRepository.findAll();
		
	}

	@PostMapping("/comment/ticket/{ticketId}")
	public ResponseEntity createComment(@RequestHeader("Authorization") String header,
										@RequestParam("text") String text,
										@PathVariable(value = "ticketId") Long ticketId,
										@RequestParam(name = "files",required = false) MultipartFile[] files) {
		User user = userService.findByToken(header);
		try {
			Comment comentario = new Comment();
			comentario.setText(text);
			comentario.setUser(user);
			Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket","ticketId", ticketId));

			comentario.setTicket_id(ticketId);
			commentRepository.save(comentario);

			if (!(ticket.getUser().getId().equals(user.getId()))){
				Alert alert = new Alert();
				alert.setMessage("El usuario "+user.getName()+" "+user.getLast_name()+" ha comentado el ticket "+String.format("%05d", ticket.getId())+" - "+ticket.getTitle());
				alert.setUserId(ticket.getUser().getId());
				alertRepository.save(alert);
				sendSimpleMessage(ticket,alert);
			}

			if (!(ticket.getAsignado().getId().equals(user.getId()))){
				Alert alert = new Alert();
				alert.setMessage("El usuario "+user.getName()+" "+user.getLast_name()+" ha comentado el ticket "+String.format("%05d", ticket.getId())+" - "+ticket.getTitle());
				alert.setUserId(ticket.getAsignado().getId());
				alertRepository.save(alert);
				sendSimpleMessage(ticket,alert);
			}

			if (files != null ){
				for (int i=0; i<files.length;i++){
					Files f  = new Files();
					f.setComment_id(comentario.getId());
					f.setOriginalname(files[i].getOriginalFilename());
					String random = UUID.randomUUID().toString();
					f.setRandonname(random);
					storageService.store(files[i],random);
					filesRepository.save(f);
				}
			}

		}catch (Exception e){
			ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok().build();
	}
	
	
	@GetMapping("/comment/{commentId}")
	public Comment getCommentById(@PathVariable(value = "commentId") Long commentId){		
		 return commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","comment_id", commentId));
		
	}
	
	@PutMapping("/comment/{commentId}/update")
	public Comment updateComment(@PathVariable("commentId") Long commentId, @Valid @RequestBody Comment commentData) {
	
		Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","comment_id", commentId));
		comment.setText(commentData.getText());		
					
		Comment updateComment = commentRepository.save(comment);
		
		return updateComment;		
	}
	
	
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) {
		if(!commentRepository.existsById(commentId)) {
            throw new ResourceNotFoundException("Channel","channel_id", commentId);
        }

        return commentRepository.findById(commentId).map(comment -> {
        	commentRepository.delete(comment);
             return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Comment","comment_id", commentId));
			
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Files f = filesRepository.findByRandonname(filename);
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + f.getOriginalname() + "\"").body(file);
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
