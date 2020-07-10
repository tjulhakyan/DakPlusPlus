package dpp.aplication.data;

import java.sql.SQLException;
//import java.util.List;

public interface DAO<T>  {
	public boolean addOneRow(T obj) throws SQLException;
	
//	public List<T> getAllLines() throws SQLException;
}
