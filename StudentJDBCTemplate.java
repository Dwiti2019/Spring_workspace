package com.besant;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class StudentJDBCTemplate implements StudentDAO {
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public void create(Integer id,String name, Integer age, Integer marks, Integer year) {
		try {
			String SQL1 = "insert into DB.student(id, name, age)" +"values (?, ?, ?)";
			Object parm[]={id,name,age};
			jdbcTemplateObject.update(SQL1,parm);
			// Get the latest student id to be used in Marks table
			String SQL2 = "select max(id) from DB.student";
			int sid = jdbcTemplateObject.queryForInt(SQL2);
			String SQL3 = "insert into DB.marks(sid, marks, year) "
					+ "values (?, ?, ?)";
			Object parm1[]={sid, marks, year};
			jdbcTemplateObject.update(SQL3,parm1);
			System.out.println("Created Name = " + name + ", Age = " + age);
			// to simulate the exception.
			//throw new RuntimeException("simulate Error condition");
		} catch (DataAccessException e) {
			System.out.println("Error in creating record, rolling back");
			throw e;
		}
	}

	public List<StudentMarks> listStudents() {
		String SQL = "select * from DB.Student, DB.Marks where Student.id=Marks.sid";
		List <StudentMarks> studentMarks= jdbcTemplateObject.query(SQL,new StudentMarksMapper());
		return studentMarks;
	}
}
