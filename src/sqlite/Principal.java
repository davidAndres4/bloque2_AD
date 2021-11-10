package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {
	
	static Connection connection;
	static Statement statement;
	static Scanner sn;
	
	
	public static void estableceConexionBBDD() {
		try{
          // create a database connection
          connection = DriverManager.getConnection("jdbc:sqlite:bbdd/sqlite/departamentos.db");
          statement = connection.createStatement();
          statement.setQueryTimeout(30);  // set timeout to 30 sec.

          statement.executeUpdate("drop table if exists departamentos");
          statement.executeUpdate("create table departamentos (dept_no tinyint(2) not null primary key, dnombre varchar(15), loc varchar(15))");
          statement.executeUpdate("insert into departamentos values (10, 'Contabilidad', 'Sevilla')");
          statement.executeUpdate("insert into departamentos values (20, 'Investigación', 'Madrid')");
          statement.executeUpdate("insert into departamentos values (30, 'Ventas','Barcelona')");
          statement.executeUpdate("insert into departamentos values (40, 'Producción', 'Bilbao')");
        }catch(SQLException e){
        	// if the error message is "out of memory",
        	// it probably means no database file is found
        	System.err.println(e.getMessage());
        }
	}
	
	public static void cierraConexionBBDD() {
		if(connection != null)
			try {
				connection.close();
				sn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		
	
	public static void listaDptos() {
		ResultSet rs;
		try {
			rs = statement.executeQuery("select * from departamentos");
			while(rs.next()){
		          // read the result set
		          System.out.println("id = " + rs.getInt("dept_no") + "\t" + rs.getString("dnombre") + " (" + rs.getString("loc") + ")");
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Almacena el departamento cuyos datos son pasados como parámetros. Siempre que no exista otroo con su mismo código.
	 * @param dept_no código del departamento a añadir
	 * @param dnombre nombre del departamento a añadir
	 * @param loc localidad del departamento a añadir
	 * @return false si existe un departamento con el código indicado; true en caso de que el registro se inserte con éxito
	 */
	public static boolean almacenaDpto(int dept_no, String dnombre, String loc) {
		// Comprobamos que no existe un departamento con ese número
		try {
			statement.execute("select * from departamentos where dept_no=" + dept_no);
			// Si el resultSet no tiene resultados
			if (!statement.getResultSet().next() ) {
				// Si no existe añadirmos 
				PreparedStatement prepSentencia = connection.prepareStatement("insert into departamentos values (?, ?, ?)");
				prepSentencia.setInt(1, dept_no);
				prepSentencia.setString(2, dnombre);
				prepSentencia.setString(3, loc);
				prepSentencia.executeUpdate();
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	/**
	 * Borra el departamento cuyo código se le pasa como parámetro; comprobando previamente que existe.
	 * @param dept_no identificador del departamento a borrar
	 * @return true si el departamento se borró con éxito; false en caso contrario 
	 */
	
	public static boolean borraDpto(int dept_no) {
		// Comprobamos que existe un departamento con ese número
		try {
			if (statement.execute("select * from departamentos where dept_no =" + dept_no)) {
				statement.execute(("delete from departamentos where dept_no =" + dept_no));
				if (statement.getUpdateCount()==1) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false;
	}
	
	/**
	 * Modifica el departamento cuyo código coincide con el pasado como parámetro, comprobando previamente que existe.
	 * @param dept_no identificador del departamento a modificar
	 * @return true si el departamento se modificó con éxito; false en caso contrario 
	 */
	public static boolean modificaDpto(int dept_no, String dnombre, String loc) {
		// Comprobamos que existe un departamento con ese número
		try {
			if (statement.execute("select * from departamentos where dept_no =" + dept_no)) {
				statement.execute(("update departamentos set dnombre = '" + dnombre
									+ "', loc = '" + loc + "' where dept_no =" + dept_no + ";"));
				if (statement.getUpdateCount()==1) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false;
	}
	
	public static void menu() {
	
		sn = new Scanner(System.in);
	    boolean salir = false;
	    int opcion; //Guardaremos la opcion del usuario
	    int dept_no;
	    String dnombre, loc;

	    while (!salir) {
	        System.out.println("1. Añadir un departamento");
	        System.out.println("2. Borrar un departamento");
	        System.out.println("3. Modificar un departamento");
	        System.out.println("4. Mostrar todos los departamentos");
	        System.out.println("5. Salir");
	        try {
	            System.out.print("Selecciona una opción: ");
	            opcion = sn.nextInt();
	            switch (opcion) {
	                case 1:
	                	//Pedir datos del departamento a añadir
	                	System.out.println("AÑADIENDO DEPARTAMENTO");
	                	System.out.println("Introduzca los siguientes datos");
	                	System.out.println("Código: ");
	                	dept_no = sn.nextInt();
	                	System.out.println("Denominación: ");
	                	dnombre = sn.next("[A-Z][A-ZÁÉÍÓÚa-záéíóú]{1,14}");
	                	System.out.println("Localidad: ");
	                	loc = sn.next("[A-Z][A-ZÁÉÍÓÚa-záéíóú]{1,14}");
	                	
	                	if (almacenaDpto(dept_no, dnombre, loc )){
	                		System.out.println("El departamento se insertó con éxito");
	                	}else {
	                		System.out.println("Ya existe un departamento con esa clave");
	                	}
	                    break;
	                case 2:
	                	//Pedir datos del departamento a borrar
	                	System.out.println("BORRANDO DEPARTAMENTO");
	                	System.out.println("Introduzca los siguientes datos");
	                	System.out.println("Código: ");
	                	dept_no = sn.nextInt();
	                	if (borraDpto(dept_no)) {
	                		System.out.println("El departamento se borró con éxito");
	                	}else {
	                		System.out.println("NO existe un departamento con esa clave");
	                	}
	                    break;
	                case 3:
	                	//a partir del su código de identificación.
	                	System.out.println("MODIFICANDO DEPARTAMENTO");
	                	System.out.println("Introduzca los siguientes datos");
	                	System.out.println("Código del departamento que quieres modificar: ");
	                	dept_no = sn.nextInt();
	                	System.out.println("Denominación: ");
	                	dnombre = sn.next("[A-Z][A-ZÁÉÍÓÚa-záéíóú]{1,14}");
	                	System.out.println("Localidad: ");
	                	loc = sn.next("[A-Z][A-ZÁÉÍÓÚa-záéíóú]{1,14}");
	                	if (modificaDpto(dept_no, dnombre, loc)) {
	                		System.out.println("El departamento se modificó con éxito");
	                	}else {
	                		System.out.println("NO existe un departamento con esa clave");
	                	}
	                    break;
	                case 4:
	                	listaDptos();
	                    break;
	                case 5:
	                	salir = true;
	                	break;
	                default:
	                    System.out.println("Solo números entre 1 y 4");
	            }
	        } catch (InputMismatchException e) {
	        	System.out.println("Debes insertar un número del 1 al 5");
	        	sn.next();
	        }
	    }
	}
	
	public static void main(String[] args) {
		estableceConexionBBDD();
		menu();
		cierraConexionBBDD();
	}
}