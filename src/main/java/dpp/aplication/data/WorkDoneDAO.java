package dpp.aplication.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import dpp.aplication.exseptions.NonUniqueResultException;
import dpp.aplication.model.Employees;
import dpp.aplication.model.InCome;
import dpp.aplication.model.Projects;
import dpp.aplication.model.WorkDone;

public class WorkDoneDAO implements DAO<WorkDone> {

	@Override
	public boolean addOneRow(WorkDone workDone) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "INSERT INTO WorkDone " + "(`employeeId`, `projectId`, `date`, `hoursWorked`, `remarks`, `id`) "
				+ "VALUES(?, ?, STR_TO_DATE(?, '%d-%m-%Y'), ?,?,?);";

		PreparedStatement statement = conn.prepareStatement(sql);
		return fillInfoSQL(statement, workDone);
	}

	public List<WorkDone> getAllLines() throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		Statement statement = conn.createStatement();
		
		String sql = "SELECT `WorkDone`.`id`, `Employees`.`name` name, `Employees`.`surname` surname,  `employeeId`, `projectId`," 
				+ "	`Projects`.`dateOfStart` dateOfStart, `Projects`.`endDate` endDate, `Projects`.`price`," 
				+ "	date, hoursWorked, remarks "
				+ " FROM `Employees` "
				+ "	INNER JOIN `WorkDone` ON `WorkDone`.`employeeId` = `Employees`.`id`"
				+ "	INNER JOIN `Projects` ON `Projects`.`id` = `WorkDone`.`projectId`" 
				+ "	ORDER BY `Employees`.id;";
		
		

		ResultSet rs = statement.executeQuery(sql);

		return parseWorkDoneEmployeesProjects(rs);
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

	@Override
	public Optional<WorkDone> getElementById(int id) throws NonUniqueResultException, SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "SELECT `WorkDone`.`id`, `Employees`.`name` name, `Employees`.`surname` surname,  `employeeId`, `projectId`," 
				+ "	`Projects`.`dateOfStart` dateOfStart, `Projects`.`endDate` endDate, `Projects`.`price`," 
				+ "	date, hoursWorked, remarks "
				+ " FROM `Employees` "
				+ "	INNER JOIN `WorkDone` ON `WorkDone`.`employeeId` = `Employees`.`id`"
				+ "	INNER JOIN `Projects` ON `Projects`.`id` = `WorkDone`.`projectId`" 
				+ " WHERE `WorkDone`.`id` = ?"
				+ "	ORDER BY `Employees`.id;";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setInt(1, id);
		ResultSet rs = statement.executeQuery();
		List<WorkDone> workDoneList = parseWorkDoneEmployeesProjects(rs);

		if (workDoneList.size() == 0)
			return Optional.empty();
		if (workDoneList.size() == 1)
			return Optional.of(workDoneList.get(0));
		throw new NonUniqueResultException("Found multiple results for id: " + id);
	}

	private List<WorkDone> parseWorkDoneEmployeesProjects(ResultSet rs) throws SQLException {
		List<WorkDone> resultList = new ArrayList<>();
		while (rs.next()) {
			WorkDone workDone = new WorkDone();
			workDone.setId(rs.getInt("id"));
			workDone.setEmployeeId(rs.getInt("employeeId"));
			workDone.setProjectId(rs.getInt("projectId"));
			workDone.setDate(rs.getDate("date"));
			workDone.setHoursWorked(rs.getDouble("hoursWorked"));
			workDone.setRemarks(rs.getString("remarks"));
			workDone.getEmployees().setName(rs.getString("name"));
			workDone.getEmployees().setSurname(rs.getString("surname"));
			workDone.getProjects().setDateOfStart(rs.getDate("dateOfStart"));
			workDone.getProjects().setEndDate(rs.getDate("endDate"));
			workDone.getProjects().setPrice(rs.getInt("price"));
			resultList.add(workDone);
		}
		
		return resultList;
	}

	@Override
	public boolean deleteElementById(int id) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "DELETE FROM `WorkDone` WHERE `id`=?;";
		PreparedStatement statement = conn.prepareStatement(sql);
		statement.setInt(1, id);
		return statement.executeUpdate() > 0;
	}

	public List<InCome> getRecentProjects(Date fromDate) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql="SELECT `projectId`, price-SUM((hoursWorked*salary/(22*8))) incomePerProject " + 
				"FROM `Employees` " + 
				"INNER JOIN `WorkDone` ON `WorkDone`.`employeeId` = `Employees`.`id` " + 
				"INNER JOIN `Projects` ON `WorkDone`.`projectId` = `Projects`.`id` " + 
				"WHERE endDate BETWEEN STR_TO_DATE(?, '%d-%m-%Y') AND DATE(NOW()) " + 
				"GROUP BY projectId " + 
				"ORDER BY `projectId`;";
		PreparedStatement statement=conn.prepareStatement(sql);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = sdf.format(fromDate);
		statement.setString(1, strDate);
		
		ResultSet rs=statement.executeQuery();
		
		return parseIncome(rs);
	}
	
	private List<InCome> parseIncome(ResultSet rs) throws SQLException {
		List<InCome> resultList = new ArrayList<>();
		while (rs.next()) {
			InCome inCome = new InCome();
			inCome.setProjectId(rs.getInt("projectId"));
			inCome.setIncomePerProject(rs.getDouble("incomePerProject"));
			
			resultList.add(inCome);
		}
		
		return resultList;
	}

	public List<WorkDone> getTopThriElementByProjectId(int id) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		
		String sql = "SELECT `WorkDone`.`id`, `Employees`.`name` name, `Employees`.`surname` surname,  `employeeId`, `projectId`," 
				+ "	`Projects`.`dateOfStart` dateOfStart, `Projects`.`endDate` endDate, `Projects`.`price`," 
				+ "	date, hoursWorked, remarks "
				+ " FROM `Employees` "
				+ "	INNER JOIN `WorkDone` ON `WorkDone`.`employeeId` = `Employees`.`id`"
				+ "	INNER JOIN `Projects` ON `Projects`.`id` = `WorkDone`.`projectId`"
				+ " WHERE projectId = ?"
				+ "	ORDER BY hoursWorked DESC"
				+ " LIMIT 3;";
		
		PreparedStatement statement=conn.prepareStatement(sql);
		
		statement.setInt(1, id);
		ResultSet rs=statement.executeQuery();

		return parseWorkDoneEmployeesProjects(rs);
	}
	
	
	public static boolean IsElementByProjectId(int id) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		
		String sql = "SELECT projectId FROM `WorkDone` "
				+ " WHERE projectId = ?"
				+ " LIMIT 1;";
		
		PreparedStatement statement=conn.prepareStatement(sql);
		
		statement.setInt(1, id);
		ResultSet rs=statement.executeQuery();

		WorkDone workDone = new WorkDone();
		while (rs.next()) {
			workDone.setProjectId(rs.getInt("projectId"));
		}
		
		if(workDone.getProjectId()!=0) return true;
		return false;
	}
	
	

}
