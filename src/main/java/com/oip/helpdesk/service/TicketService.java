package com.oip.helpdesk.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.oip.helpdesk.domain.entities.DataReporte;
import com.oip.helpdesk.domain.entities.Status;
import com.oip.helpdesk.domain.entities.User;
import com.oip.helpdesk.domain.exceptions.ResourceNotFoundException;
import com.oip.helpdesk.repository.DataReporteRepository;
import com.oip.helpdesk.repository.TicketStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oip.helpdesk.domain.entities.Ticket;
import com.oip.helpdesk.repository.TicketRepository;


@Service
public class TicketService {
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	TicketStatusRepository ticketStatusRepository;
	@Autowired
	DataReporteRepository dataReporteRepository;

	public List<Ticket> getAll(){
		return ticketRepository.findAll();
	}

	public Optional<Ticket> findById(long id) {
		return ticketRepository.findById(id);
		
	}

	public List<Status> getStatusAvailable (Ticket ticket, User user){
		List<Status> status = new ArrayList<>();
		Status state;
		status.add(ticket.getStatus());
		if (ticket.getStatus().getName().equals("CREADO")){
			if (user.getRoles().iterator().next().getName().equals("user") &&
				user.getId()==ticket.getUser().getId()){
				state = ticketStatusRepository.findById(new Long(5)).orElseThrow(() -> new ResourceNotFoundException("Status","status_id",5));
				status.add(state);
			}
		}else if (ticket.getStatus().getName().equals("PENDIENTE")){
			if ((user.getRoles().iterator().next().getName().equals("user") &&
					user.getId()==ticket.getUser().getId())){
				state = ticketStatusRepository.findById(new Long(5)).orElseThrow(() -> new ResourceNotFoundException("Status","status_id",5));
				status.add(state);
			}else if((!user.getRoles().iterator().next().getName().equals("user") &&
					user.getId().equals(ticket.getAsignado().getId()))){
				state = ticketStatusRepository.findById(new Long(3)).orElseThrow(() -> new ResourceNotFoundException("Status","status_id",3));
				status.add(state);
				state = ticketStatusRepository.findById(new Long(5)).orElseThrow(() -> new ResourceNotFoundException("Status","status_id",5));
				status.add(state);
			}
		}else if (ticket.getStatus().getName().equals("CERRADO")) {
			if ((user.getRoles().iterator().next().getName().equals("user") &&
					user.getId()==ticket.getUser().getId())){
				state = ticketStatusRepository.findById(new Long(4)).orElseThrow(() -> new ResourceNotFoundException("Status","status_id",4));
				status.add(state);
				state = ticketStatusRepository.findById(new Long(2)).orElseThrow(() -> new ResourceNotFoundException("Status","status_id",2));
				status.add(state);
				state = ticketStatusRepository.findById(new Long(5)).orElseThrow(() -> new ResourceNotFoundException("Status","status_id",5));
				status.add(state);
			}
		}
		return  status;
	}

	public String getDifferences(Ticket or,Ticket up,User user){
		if (or.getStatus().getName().equals(up.getStatus().getName())
			&& or.getTitle().equals(up.getTitle())
			&& or.getDescription().equals(up.getDescription())){
			return "";
		}
		String alert = "El usuario "+user.getName()+" ha modificado el ticket "+String.format("%05d", or.getId())+" - "+or.getTitle();
		if (!or.getStatus().getName().equals(up.getStatus().getName())){
			alert += " de estado "+or.getStatus().getName().toUpperCase()+ " a "+up.getStatus().getName().toUpperCase();
		}
		if (!or.getTitle().equals(up.getTitle())){
			alert += " en su titulo";
		}
		if (!or.getDescription().equals(up.getDescription())){
			alert += " en su descripcion";
		}
		return alert;
	}

	public ArrayList<HashMap<String,Object>> groupTicketsBy(User user,String parameter,String fecha1,String fecha2){
		List<DataReporte> data = null;
		if (parameter.equals("AREA")){
			data = dataReporteRepository.groupByAreaAndStatus(fecha1,fecha2);
		}else if(parameter.equals("USUARIO")){
			data = dataReporteRepository.groupByUsuarioAndStatus(fecha1,fecha2);
		}else if(parameter.equals("PRIORIDAD")){
			data = dataReporteRepository.groupByPriortyAndStatus(fecha1,fecha2);
		}else if(parameter.equals("ALL")){
			if(user.getRoles().iterator().next().getName().equals("superadmin")){
				data = dataReporteRepository.groupByStatus();
			}else if(user.getRoles().iterator().next().getName().equals("admin")){
				data = dataReporteRepository.groupByStatusByAssigned(user.getId());
			}else if(user.getRoles().iterator().next().getName().equals("user")){
				data = dataReporteRepository.groupByStatusByCreated(user.getId());
			}
		}

		ArrayList<HashMap<String,Object>> grouped = new ArrayList<>();

		for (int i= 0;i<data.size();i++){

			boolean exist = false;
			int j;

			for (j = 0; j<grouped.size(); j++){
				if (grouped.get(j).get("name").toString().equals(data.get(i).getParameter())){
					exist = true;
					break;
				}
			}

			if (!exist){
				HashMap<String,Object> predefined = new HashMap<>();
				predefined.put("name",data.get(i).getParameter());
				predefined.put("detalle",new HashMap<String,Integer>());
				grouped.add(predefined);
			}

			HashMap<String,Object> detalle = (HashMap<String,Object>)grouped.get(j).get("detalle");
			detalle.put(data.get(i).getStatus(),data.get(i).getCantidad());

		}

		return grouped;
	}

	public List<DataReporte> dataTicketsBy(String parameter,String fecha1,String fecha2){
		List<DataReporte> data = null;
		if (parameter.equals("AREA")){
			data = dataReporteRepository.groupByAreaAndStatus(fecha1,fecha2);
		}else if(parameter.equals("USUARIO")){
			data = dataReporteRepository.groupByUsuarioAndStatus(fecha1,fecha2);
		}else if(parameter.equals("USUARIO")){
			data = dataReporteRepository.groupByPriortyAndStatus(fecha1,fecha2);
		}
		return  data;
	}
}
