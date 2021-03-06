package dpp.aplication.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dpp.aplication.exseptions.NonUniqueResultException;
import dpp.aplication.model.Projects;
import dpp.aplication.model.WorkDone;

public class ProjectsDAO implements DAO<Projects>{

	@Override
	public boolean addOneRow(Projects projects) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "INSERT INTO Projects " 
				+ "(`dateOfStart`, `description`, `price`, `endDate`, `id`) "
				+ "VALUES(STR_TO_DATE(?, '%d-%m-%Y'), ?,?,STR_TO_DATE(?, '%d-%m-%Y'),?);";

		PreparedStatement statement = conn.prepareStatement(sql);
		return fillInfoSQL(statement, projects);
	}

	private boolean fillInfoSQL(PreparedStatement statement, Projects projects) throws SQLException {
		String strDate = null;
		if (projects.getDateOfStart() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			strDate = sdf.format(projects.getDateOfStart());
		}
		statement.setString(1, strDate);
		statement.setString(2, projects.getDescription());
		statement.setInt(3, projects.getPrice());
		
		if (projects.getEndDate() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			strDate = sdf.format(projects.getEndDate());
		}
		statement.setString(4, strDate);
		statement.setInt(5, projects.getId());

		return statement.executeUpdate() > 0;
	}


	public Optional<Projects> getElementById(int id) throws SQLException, NonUniqueResultException {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM `Projects` WHERE Id = ?");
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        List<Projects> projects = parseProjects(rs);
        
        if (projects.size() == 0) return Optional.empty();
        if (projects.size() == 1) {
        	if(WorkDoneDAO.IsElementByProjectId(projects.get(0).getId())) projects.get(0).setIsprojectIdInWorkDone(true);
        	else projects.get(0).setIsprojectIdInWorkDone(false);
        	return Optional.of(projects.get(0));
        }
        throw new NonUniqueResultException("Found multiple results for id: " + id);
	}

	private List<Projects> parseProjects(ResultSet rs) throws SQLException {
		List<Projects> resultList = new ArrayList<>();
		while (rs.next()) {
			Projects projects = new Projects();
			projects.setId(rs.getInt("id"));
			projects.setDateOfStart(rs.getDate("dateOfStart"));
			projects.setDescription(rs.getString("description"));
			projects.setPrice(rs.getInt("price"));
			projects.setEndDate(rs.getDate("endDate"));
			resultList.add(projects);
		}

		return resultList;
	}

	public boolean deleteElementById(Projects OpProj) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql1 = "DELETE FROM `Projects` WHERE `id`="+OpProj.getId()+";";
		Statement statement1=conn.createStatement();
		
		// if the project is associated with an employee then we delete the association in table WorkDone
		if(OpProj.isIsprojectIdInWorkDone()) {
			String sql2 = "DELETE FROM `WorkDone` WHERE `projectId`="+OpProj.getId()+";";
			Statement statement2=conn.createStatement();
			conn.setAutoCommit(false);
			statement2.execute(sql2);
		}
		
		// we do both sql Query together
		statement1.execute(sql1);
		
		try {
			conn.commit();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public List<Projects> getLinesWithDate(int action) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql="";
		
		// action==4 --- Find all current projects. 
		if(action==4) sql="SELECT * FROM `Projects` WHERE DATE(`dateOfStart`) < DATE(NOW()) AND DATE(`endDate`) > DATE(NOW());";
		
		// action==5 --- Find all projects starting today.
		if(action==5) sql="SELECT * FROM `Projects` WHERE DATE(`dateOfStart`) = DATE(NOW());";
		PreparedStatement statement=conn.prepareStatement(sql);
		ResultSet rs=statement.executeQuery();
		return parseProjects(rs);
	}




}
