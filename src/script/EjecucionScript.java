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
		creaBBDD();
		ejecutaScript();

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

			statement.executeQuery(script.toString());

			bf.close();
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	public static void creaBBDD() {
		try {
			//establece la conexión
			connection = DriverManager.getConnection("jdbc:hsqldb:file:bbdd/script/dosTablas/empresa");
			statement = connection.createStatement();

			

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
