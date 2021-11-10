package metadatos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EstructuraBBDD {

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
			rs = dbmd.getTables(null, null, null, null);
			
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void creaBBDD() {
		try {
			//establece la conexión
			connection = DriverManager.getConnection("jdbc:sqlite:bbdd/metadatos/departamentos.db");
			statement = connection.createStatement();
			
			//crea la tabla departamentos
			statement.executeUpdate("drop table if exists departamentos");
			statement.executeUpdate("create table departamentos (dept_no tinyint(2) not null primary key, dnombre varchar(15), loc varchar(15))");
			//mete registros a la tabla departamentos
			statement.executeUpdate("insert into departamentos values (10, 'Contabilidad', 'Sevilla')");
			statement.executeUpdate("insert into departamentos values (20, 'Investigación', 'Madrid')");
			statement.executeUpdate("insert into departamentos values (30, 'Ventas','Barcelona')");
			statement.executeUpdate("insert into departamentos values (40, 'Producción', 'Bilbao')");
			
			//crea la tabla empleados
			statement.executeUpdate("drop table if exists empleados");
			statement.executeUpdate("create table empleados (empno	SMALLINT(4) NOT NULL PRIMARY KEY,\r\n"
					+ "					apellido	VARCHAR(10),\r\n"
					+ "					oficio	VARCHAR(10),\r\n"
					+ "					dir	SMALLINT,\r\n"
					+ "					fecha_alt	DATE,\r\n"
					+ "					salario	FLOAT(6,2),\r\n"
					+ "					comision	FLOAT(6, 2),\r\n"
					+ "					dept_no	TINYINT(2) NOT NULL,\r\n"
					+ "					CONSTRAINT FK_dept_no FOREIGN KEY (dept_no) REFERENCES departamentos (deptno))");
			//mete registros a la tabla empleados
			statement.executeUpdate("insert into empleados values ('1', 'García', 'Director', '1', '2010/02/07', '1890', '10', '1');\r\n"
					+ "insert into empleados values ('2', 'Pérez', 'Operario', '1', '2015/07/07', '1100', '7', '1');\r\n"
					+ "insert into empleados values ('3', 'Gómez', 'Operario', '1', '2016/01/01', '1100', '7', '1');\r\n"
					+ "insert into empleados values ('4', 'Santiago', 'Director', '4', '2010/01/01', '2100', '12', '2');\r\n"
					+ "insert into empleados values ('5', 'Redondo', 'Auxiliar', '4', '2002/02/01', '1700', '9', '2');");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}

































