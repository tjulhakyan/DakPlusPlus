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

import dpp.aplication.data.ConnectionFactory;
import dpp.aplication.exseptions.NonUniqueResultException;
import dpp.aplication.model.Employees;
import dpp.aplication.model.Projects;;

public class EmployeesDAO implements DAO<Employees> {

	public List<Employees> getAllLines() throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Employees;");
		return parseEmployees(rs);
	}
	
	public List<Employees> getLinesByLetters(String letters) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		
		String stringForSql="%"+letters+"%";
		
		String sql="SELECT * FROM Employees WHERE `name` LIKE ? OR `surname` LIKE ?;";
		PreparedStatement statement=conn.prepareStatement(sql);
		statement.setString(1, stringForSql);
		statement.setString(2, stringForSql);
		ResultSet rs=statement.executeQuery();
		
		return parseEmployees(rs);
	}
	
	
	public List<Employees> getLinesWithTodaysBirthdays(int action) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql="";
		
		// action==7 --- if we need to show Employees whose birthdays is today 
		if(action==7) sql="SELECT * FROM  Employees WHERE DAYOFMONTH(`birthday`) = DAY(NOW()) AND MONTH(`birthday`) = MONTH(NOW());";
		
		// action==8 --- if we need to show Employees whose birthdays in the next 7 days
		if(action==8) sql="SELECT * FROM  Employees "
				+ "WHERE  DATE_ADD(birthday, INTERVAL YEAR(CURDATE())-YEAR(birthday) + "
				+ "IF(DAYOFYEAR(CURDATE()) > DAYOFYEAR(birthday),1,0) YEAR) "
				+ "BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY);";
		PreparedStatement statement=conn.prepareStatement(sql);
		ResultSet rs=statement.executeQuery();
		return parseEmployees(rs);
	}

	public boolean editRowById(Employees employees) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "UPDATE Employees "
				+ "SET `name`=?, `surname`=?, `phone`=?, `phoneICE`=?, `birthday`=STR_TO_DATE(?, '%d-%m-%Y'), `salary`=? "
				+ "WHERE `id`=?;";

		PreparedStatement statement = conn.prepareStatement(sql);
		return fillInfoSQL(statement, employees);
	}
	
	
	public boolean deleteElementById(Employees OpProj) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		
		String sql1 = "DELETE FROM `Employees` WHERE `id`="+OpProj.getId()+";";
		
		Statement statement1=conn.createStatement();
		
		// if the project is associated with an employee then we delete the association in table WorkDone
		if(OpProj.isEmployeeIdInWorkDone()) {
			String sql2 = "DELETE FROM `WorkDone` WHERE `employeeId`="+OpProj.getId()+";";
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

	@Override
	public boolean addOneRow(Employees employees) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "INSERT INTO Employees " 
				+ "(`name`, `surname`, `phone`, `phoneICE`, `birthday`, `salary`, `id`) "
				+ "VALUES(?,?,?,?,STR_TO_DATE(?, '%d-%m-%Y'),?,?);";

		PreparedStatement statement = conn.prepareStatement(sql);
		return fillInfoSQL(statement, employees);
	}
	
	@Override
	public Optional<Employees> getElementById(int id) throws SQLException, NonUniqueResultException {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement statement = conn.prepareStatement("SELECT * FROM `Employees` WHERE Id = ?");
        statement.setInt(1, id);
        ResultSet rs = statement.executeQuery();
        List<Employees> employee = parseEmployees(rs);
        
        if (employee.size() == 0) return Optional.empty();
        if (employee.size() == 1) {
        	if(WorkDoneDAO.IsElementByEmployeesId(employee.get(0).getId())) employee.get(0).setEmployeeIdInWorkDone(true);
        	else employee.get(0).setEmployeeIdInWorkDone(false);
        	return Optional.of(employee.get(0));
        }
        throw new NonUniqueResultException("Found multiple results for id: " + id);
	}
	
	
	private boolean fillInfoSQL(PreparedStatement statement, Employees employees) throws SQLException {
		statement.setString(1, employees.getName());
		statement.setString(2, employees.getSurname());
		statement.setString(3, employees.getPhone());
		statement.setString(4, employees.getPhoneICE());

		String strDate = null;
		if (employees.getBirthday() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			strDate = sdf.format(employees.getBirthday());
		}
		statement.setString(5, strDate);
		statement.setInt(6, employees.getSalary());
		statement.setInt(7, employees.getId());

		return statement.executeUpdate() > 0;
	}

	private List<Employees> parseEmployees(ResultSet rs) throws SQLException {
		List<Employees> resultList = new ArrayList<>();
		while (rs.next()) {
			Employees employees = new Employees();
			employees.setId(rs.getInt("id"));
			employees.setName(rs.getString("name"));
			employees.setSurname(rs.getString("surname"));
			employees.setPhone(rs.getString("phone"));
			employees.setPhoneICE(rs.getString("phoneICE"));
			employees.setBirthday(rs.getDate("birthday"));
			employees.setSalary(rs.getInt("salary"));
			resultList.add(employees);
		}

		return resultList;
	}

}
