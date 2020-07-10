package dpp.aplication.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import dpp.aplication.model.WorkDone;

public class WorkDoneDAO implements DAO<WorkDone> {

	@Override
	public boolean addOneRow(WorkDone workDone) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		String sql = "INSERT INTO WorkDone " 
				+ "(`employeeId`, `projectId`, `date`, `hoursWorked`, `remarks`) "
				+ "VALUES(?, ?, STR_TO_DATE(?, '%d-%m-%Y'), ?,?);";

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
		return statement.executeUpdate() > 0;
	}

}
