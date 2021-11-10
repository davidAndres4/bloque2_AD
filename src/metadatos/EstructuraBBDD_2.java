package metadatos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EstructuraBBDD_2 {

	private static Connection connection;
	private static Statement statement;
	private static DatabaseMetaData dbmd;
	private static ResultSet rs = null;

	public static void main(String[] args) {

		creaBBDD();
		muestraInfoBBDD();
	}

	public static void muestraInfoBBDD() {
		try {
			
			dbmd = connection.getMetaData();
			
			//imprime información de la BBDD
			System.out.println("INFORMACIÓN DE LA BASE DE DATOS");
			System.out.println("Nombre: " + dbmd.getDatabaseProductName());
			System.out.println("Driver: " + dbmd.getDriverName());
			System.out.println("URL: " + dbmd.getURL());
			System.out.println("Usuario: " + dbmd.getUserName());

			//obtiene toda la información de todas las tablas
			//rs = dbmd.getTables(null, null, null, null);
			
			String[] tipos = {"TABLE"};
			ResultSet rs = dbmd.getTables(null, null, null, tipos);
			
			ArrayList<String>nombresTablas = new ArrayList<>();
			System.out.println("\nLISTA DE TABLAS: ");
			while(rs.next()) {
				//recoge los datos de la columna del result set llamada TABLE_NAME, que contiene el nombre de las tablas
				String nombreTabla = rs.getString("TABLE_NAME");
				nombresTablas.add(nombreTabla);
				System.out.println(nombreTabla);
			}
			
			//datos de las columnas
			for(int i=0; i<nombresTablas.size(); i++) {
				System.out.println("\nLISTA DE COLUMNAS DE LA TABLA " + nombresTablas.get(i) + ":");
				//obtiene toda la información de las columnas de la tabla especificada
				rs = dbmd.getColumns(null, null, nombresTablas.get(i), null);
				while(rs.next()) {
					//imprime los datos de la columna
					System.out.println("Nombre: " + rs.getString("COLUMN_NAME") +
							"; Tipo: " + rs.getString("TYPE_NAME") +
							"; Tamaño: " + rs.getString("COLUMN_SIZE") +
							"; Puede ser nulo: " + rs.getString("NULLABLE"));	
				}
			}
			
			// Obtenemos la clave primaria de cada tabla
			for(int i=0; i<nombresTablas.size(); i++) {
				System.out.println("\nCLAVE PRIMARIA DE LA TABLA " + nombresTablas.get(i));
				rs = dbmd.getPrimaryKeys(null, null, nombresTablas.get(i));
				while(rs.next()) {
					System.out.println("Clave primaria: " +  rs.getString("COLUMN_NAME"));
				}
			}
			
			//claves foraneas
			for(int i=0; i<nombresTablas.size(); i++) {
				System.out.println("\nCLAVE AJENA DE LA TABLA " + nombresTablas.get(i));
				rs = dbmd.getImportedKeys(null, null, nombresTablas.get(i));
				while(rs.next()) {
					System.out.println("Clave ajena: " +  rs.getString("FK_NAME"));
				}
			}
			
			//procedures
			System.out.println("\nPROCEDURES");
			rs = dbmd.getProcedures(null, null, null);
			while(rs.next()) {
				System.out.println("Procedure: " +  rs.getString("PROCEDURE_NAME"));
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void creaBBDD() {
		try {
			//establece la conexión
			connection = DriverManager.getConnection("jdbc:hsqldb:file:bbdd/metadatos/dosTablas/empresa");
			statement = connection.createStatement();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}

































