package dpp.aplication.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dpp.aplication.exseptions.NonUniqueResultException;
import dpp.aplication.model.Employees;
import dpp.aplication.model.Projects;
import dpp.aplication.model.WorkDone;

public class WorkDoneDAO implements DAO<WorkDone> {

	@Override
	public boolean addOneRow(WorkDone workDone) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "INSERT INTO WorkDone " 
				+ "(`employeeId`, `projectId`, `date`, `hoursWorked`, `remarks`, `id`) "
				+ "VALUES(?, ?, STR_TO_DATE(?, '%d-%m-%Y'), ?,?,?);";

		PreparedStatement statement = conn.prepareStatement(sql);
		return fillInfoSQL(statement, workDone);
	}
	
	public boolean editRowById(WorkDone workDone) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "UPDATE WorkDone "
				+ "SET `employeeId`=?, `projectId`=?, `date`=STR_TO_DATE(?, '%d-%m-%Y'), `hoursWorked`=?, `remarks`=? "
				+ "WHERE `id`=?;";

		PreparedStatement statement = conn.prepareStatement(sql);
		return fillInfoSQL(statement, workDone);
	}

	private boolean fillInfoSQL(PreparedStatement statement, WorkDone workDone) throws SQLException {
		statement.setInt(1, workDone.getEmployeeId());
		statement.setInt(2, workDone.getProjectId());
		
		String strDate = null;
		if (workDone.getDate() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			strDate = sdf.format(workDone.getDate());
		}
		statement.setString(3, strDate);
		
		statement.setDouble(4, workDone.getHoursWorked());
		statement.setString(5, workDone.getRemarks());
		statement.setInt(6, workDone.getId());
		return statement.executeUpdate() > 0;
	}

	public Optional<WorkDone> getElementById(int id) throws NonUniqueResultException, SQLException {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM `WorkDone` WHERE Id = ?");
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        List<WorkDone> workDoneList = parseWorkDone(rs);
        
        if (workDoneList.size() == 0) return Optional.empty();
        if (workDoneList.size() == 1) return Optional.of(workDoneList.get(0));
        throw new NonUniqueResultException("Found multiple results for id: " + id);
	}

	private List<WorkDone> parseWorkDone(ResultSet rs) throws SQLException {
		List<WorkDone> resultList = new ArrayList<>();
		while (rs.next()) {
			WorkDone workDone = new WorkDone();
			workDone.setId(rs.getInt("id"));
			workDone.setEmployeeId(rs.getInt("employeeId"));
			workDone.setProjectId(rs.getInt("projectId"));
			workDone.setDate(rs.getDate("date"));
			workDone.setHoursWorked(rs.getDouble("hoursWorked"));
			workDone.setRemarks(rs.getString("remarks"));
			resultList.add(workDone);
		}

		return resultList;
	}

	public boolean deleteElementById(int id) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "DELETE FROM `WorkDone` WHERE `id`=?;";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setInt(1, id);
		return statement.executeUpdate() > 0;
	}

}
