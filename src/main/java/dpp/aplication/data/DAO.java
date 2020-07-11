package dpp.aplication.data;

import java.sql.SQLException;
import java.util.Optional;

import dpp.aplication.exseptions.NonUniqueResultException;


public interface DAO<T>  {
	public boolean addOneRow(T obj) throws SQLException;
	
	public boolean deleteElementById(int id) throws SQLException;
	
	public Optional<T> getElementById(int id) throws SQLException, NonUniqueResultException;
	
	
}
