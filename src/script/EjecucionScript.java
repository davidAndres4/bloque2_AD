package script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EjecucionScript {

	private static Connection connection;
	private static Statement statement;
	private static DatabaseMetaData dbmd;
	private static ResultSet rs = null;

	public static void main(String[] args) {
		conectaBBDD();
		ejecutaScript();
		selectDptosEmpleados();
	}
	
	public static void selectDptosEmpleados() {
		int i = 0;
		
		try {
			System.out.println("\nDEPARTAMENTOS:");
			rs = statement.executeQuery("select * from departamentos");
			while(rs.next()){
		          
		          System.out.println("id = " + rs.getInt("deptno") + "\t" + rs.getString("dnombre") + " (" + rs.getString("loc") + ")");
		    }
			
			System.out.println("\nEMPLEADOS:");
			rs = statement.executeQuery("select * from empleados");
			while(rs.next()){      
		          System.out.println(rs.getInt("empno") + "\t" + rs.getString("apellido")
		          + "\t" + rs.getString("oficio") + "\t" + rs.getInt("dir") 
		          + "\t" + rs.getString("fecha_alt") + "\t" + rs.getInt("salario")
		          + "\t" + rs.getInt("comision") + "\t" + rs.getInt("dept_no"));
		    }
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	public static void ejecutaScript() {
		File archivo = new File("Script_HSQLDB_Datos.sql");
		StringBuilder script = new StringBuilder();
		String linea="";
		BufferedReader bf;
		try {

			bf = new BufferedReader(new FileReader(archivo));
			while((linea = bf.readLine()) != null) {
				//System.out.println(linea);
				//linea.concat(linea);

				script.append(linea + "\n");
			}

			System.out.println(script.toString());

			statement.executeUpdate(script.toString());

			bf.close();
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	public static void conectaBBDD() {
		try {
			//establece la conexión
			connection = DriverManager.getConnection("jdbc:hsqldb:file:bbdd/script/dosTablas/empresa");
			statement = connection.createStatement();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void cierraConexionBBDD() {
		if(connection != null)
			try {
				connection.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}























