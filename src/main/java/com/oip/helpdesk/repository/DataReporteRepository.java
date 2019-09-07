package com.oip.helpdesk.repository;


import com.oip.helpdesk.domain.entities.DataReporte;
import com.oip.helpdesk.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataReporteRepository extends JpaRepository<DataReporte, Long>{
	@Query(
			value = "SELECT ROW_NUMBER() OVER (ORDER BY area.name,status.id ) as id,\n" +
					"area.name as parameter_name,\n" +
					"status.name as status_name,\n" +
					"(SELECT \n" +
					"\tcount(id)\n" +
					" FROM ticket\n" +
					" WHERE area_id = area.id\n" +
					" AND status_id = status.id\n" +
					" AND created_at >= ?1 " +
					" AND created_at <= ?2 " +
					") as cantidad\n" +
					"FROM area,status\n",
			nativeQuery = true)
	List<DataReporte> groupByAreaAndStatus(String fecha1,String fecha2);

	@Query(
			value = "SELECT ROW_NUMBER() OVER (ORDER BY admin.name,status.id ) as id,\n" +
					"CONCAT(admin.name,' ',admin.last_name) as parameter_name,\n" +
					"status.name as status_name,\n" +
					"(SELECT \n" +
					"\tcount(id)\n" +
					" FROM ticket\n" +
					" WHERE assigned_to = admin.id\n" +
					" AND status_id = status.id\n" +
					" AND created_at >= ?1 " +
					" AND created_at <= ?2 " +
					") as cantidad\n" +
					"FROM (SELECT \n" +
					"\t\t\t\tusers.*\n" +
					"\t\t\tFROM users \n" +
					"\t\t\tJOIN user_role ON user_role.user_id = users.id\n" +
					"\t\t\tWHERE user_role.role_id=1 \n" +
					"\t\t\tOR user_role.role_id=3\n" +
					") admin , status",
			nativeQuery = true)
	List<DataReporte> groupByUsuarioAndStatus(String fecha1,String fecha2);

	@Query(
			value = "SELECT ROW_NUMBER() OVER (ORDER BY priority.name,status.id ) as id,\n" +
					"priority.name as parameter_name,\n" +
					"status.name as status_name,\n" +
					"(SELECT \n" +
					"\tcount(id)\n" +
					" FROM ticket\n" +
					" WHERE priority_id = priority.id\n" +
					" AND status_id = status.id\n" +
					" AND created_at >= ?1 " +
					" AND created_at <= ?2 " +
					") as cantidad\n" +
					"FROM priority, status",
			nativeQuery = true)
	List<DataReporte> groupByPriortyAndStatus(String fecha1,String fecha2);

	@Query(
			value = "SELECT ROW_NUMBER() OVER (ORDER BY status.id ) as id,\n" +
					"\t'data' as parameter_name,\n" +
					"\tstatus.name as status_name,\n" +
					"\t(SELECT \n" +
					"\t  count(id)\n" +
					"\t FROM ticket\n" +
					"\t WHERE status_id = status.id\n" +
					"\t) as cantidad\n" +
					"\tFROM  status",
			nativeQuery = true)
	List<DataReporte> groupByStatus();

	@Query(
			value = "SELECT ROW_NUMBER() OVER (ORDER BY status.id ) as id,\n" +
					"\t'data' as parameter_name,\n" +
					"\tstatus.name as status_name,\n" +
					"\t(SELECT \n" +
					"\t  count(id)\n" +
					"\t FROM ticket\n" +
					"\t WHERE status_id = status.id\n" +
					"\t	AND assigned_to = ?1 \n" +
					"\t) as cantidad\n" +
					"\tFROM  status",
			nativeQuery = true)
	List<DataReporte> groupByStatusByAssigned(Long id);

	@Query(
			value = "SELECT ROW_NUMBER() OVER (ORDER BY status.id ) as id,\n" +
					"\t'data' as parameter_name,\n" +
					"\tstatus.name as status_name,\n" +
					"\t(SELECT \n" +
					"\t  count(id)\n" +
					"\t FROM ticket\n" +
					"\t WHERE status_id = status.id\n" +
					"\t	AND user_id = ?1 \n" +
					"\t) as cantidad\n" +
					"\tFROM  status",
			nativeQuery = true)
	List<DataReporte> groupByStatusByCreated(Long id);


}

