/* CS315 Database Management NEIU Spring 2019 */
// Required import statements
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DriverProgram
{
	// Class static variables
	public static Statement stmt;
	public static Connection conn;
	public static ResultSet rs;
	public static Scanner input = new Scanner( System.in );
	public static Scanner scan;

	public static void main( String[] args ) throws Exception
	{
		// Initialize the connection
		Connection conn = null;

		try
		{
			scan = new Scanner(System.in);
			// Establish a connection to mysql
			conn = DriverManager.getConnection("jdbc:mysql://cs.neiu.edu:3306/s19315_anvu?" + "user=s19315_anvu&password=" + getPassword() );

			// Use the connection object to initialize a statement object
			stmt = conn.createStatement();

			while(true) {
				System.out.println("Here are the list of tables name: ");
				System.out.println();
				String queryTables = "SELECT table_name FROM information_schema.tables where table_schema='s19315_anvu'";
				ResultSet rsQueryTables = stmt.executeQuery(queryTables);
				ArrayList<String> arrTable = new ArrayList<>();
				int counter = 1;
				while(rsQueryTables.next()) {
					arrTable.add(rsQueryTables.getString(1));
					System.out.println(counter +". "+ rsQueryTables.getString(1));
					counter++;
				}
				rsQueryTables.close();
				System.out.println();
				System.out.print("Choose table you would like to get in (-1 is exit): ");
				int userInput = scan.nextInt();
				System.out.println();
				while(true) {
					try {
						if(userInput != -1) {
						   	String tableChoose = arrTable.get(userInput-1);
							System.out.println();
							System.out.println("What do you want to do with this table?");
							System.out.println("1. Insert");
							System.out.println("2. Update");
							System.out.println("3. Delete");
							System.out.println("4. Show All Table Records");
							System.out.println("5. Search");
							System.out.println("6. Skip This Step");
							System.out.print("Enter your choice: ");
							int userOption =  scan.nextInt();
							
							switch(userOption) {
							case 1: 
								Insert(tableChoose);
								break;
							case 2: 
								Update(tableChoose);
								break;
							case 3: 
								Delete(tableChoose);
								break;
							case 4:
								{
									switch(userInput){
									case 1:
										tableBuilding(tableChoose);
										break;
									case 2:
										tableBuilding_Course(tableChoose);
										break;
									case 3:
										tableCourse(tableChoose);
										break;
									case 4:
										tableProfessor(tableChoose);
										break;
									case 5:
										tableProfessor_Office(tableChoose);
										break;
									}
								}
							case 5:
								Search(tableChoose);
								break;
							}
						}
						break;
					}catch(InputMismatchException | IndexOutOfBoundsException e) {
						System.out.print("Please enter the correct choice");
						userInput = scan.nextInt();
					}
				}
				if(userInput == -1) break;
				System.out.println();
				System.out.println("What else you would like to do?");
				System.out.println("1. Go back to the main menu");
				System.out.println("2. Exit");
				System.out.print("Enter your choice: ");
				int choice = scan.nextInt();
				System.out.println();
				if(choice == 2) break;
			}
			System.out.println("Program Exited");
			System.out.println("Bye");

//			String firstName = scan.nextLine();
//			System.out.print("Enter last name: ");
//			String lastName = scan.nextLine();
//			String query = "INSERT INTO Faculty (stu_first,stu_last,stu_major,stu_gpa,fac_no) VALUES ('" + firstName + "','" + lastName + "','Computer Science',3.8,3)";
//			stmt.executeUpdate(query);
//
//			System.out.print("Enter gpa value: ");
//			double gpa = scan.nextDouble();
//			System.out.print("Enter major: ");
//			String major = scan.nextLine();
//
//			if(major.equals("Business Management")){
//				System.out.print("Enter a other major: ");
//				major = scan.nextLine();
//			} else {
//				query = "UPDATE Faculty SET " + "stu_gpa = '" + gpa + "', stu_major = 'Chemistry' WHERE stu_first = 'Juliette' AND stu_last = 'Brewer'";
//
//				stmt.executeUpdate(query);
//			}






		} // End try block
		catch( SQLException sqle )
		{
			// Handle any errors
			System.out.println( "SQL Exception: " + sqle.getMessage() );
			System.out.println( "SQL State: " + sqle.getSQLState() );
			System.out.println( "Vendor Error: " + sqle.getErrorCode() );
			sqle.printStackTrace();
		} // End catch block
	} // End main()



	private static String Search(String tableChoose) {
		// TODO Auto-generated method stub
		return null;
	}



	private static void Delete(String tableChoose) {
		// TODO Auto-generated method stub
		
	}



	private static void Update(String tableChoose) throws SQLException {
		if(tableChoose.equals("Building")){
			System.out.print("Please enter the room number that you want to modify: ");
			int oldRoom = scan.nextInt();
			System.out.print("Please enter the new room number that you want to update: ");
			int newRoom = scan.nextInt();
			
			String sqlSearchRoom = "SELECT room_id FROM Building WHERE room_number = "+newRoom;
			ResultSet rs = stmt.executeQuery(sqlSearchRoom);
			boolean found = false;
			while(rs.next())
				found = true;
			rs.close();
			
			if(found) {
				System.out.println("The room is already existed!");
				System.out.println("No record updated");
				return;
			}
			
			System.out.println();
			System.out.print("Are you sure want to update this table? (1 is Yes and 2 is No): ");
			int choice = scan.nextInt();
			if(choice == 1) {
				String query = "UPDATE Building SET room_number = " + newRoom + " WHERE room_number =" + oldRoom;
				stmt.executeUpdate(query);
				System.out.println("Update success");
			}else 
				System.out.println("No Change");

		}else if(tableChoose.equals("Professor")){
			int professor_id = -1;
			System.out.println("You requested to change professor information! ");
			System.out.print("Please enter professor first name: ");
			String first = scan.next();
			System.out.print("Please enter professor last name: ");
			String last = scan.next();
			
			String sqlSearch = "SELECT professor_id FROM Professor WHERE professor_first = "+ addQuotes(first) + " AND professor_last = " + addQuotes(last);
			ResultSet result = stmt.executeQuery(sqlSearch);
			boolean found = false;
			while(result.next()) {
				found = true;
				professor_id = result.getInt("professor_id");
			}
			result.close();
			if(!found) {
				System.out.println("There is no professor match with the information you provided.");
				return;
			}else {
				System.out.print("Enter new first name: ");
				first = scan.next();
				System.out.print("Enter new last name: ");
				last = scan.next();
				System.out.print("Enter new department: ");
				String department = scan.next();
				System.out.print("Enter new year started: ");
				int year = scan.nextInt();
				System.out.println();
				System.out.print("Are you sure want to update this table? (1 is Yes and 2 is No): ");
				int choice = scan.nextInt();
				if(choice == 1) {
					String query = "UPDATE Professor SET professor_first= "+ addQuotes(first) + ", professor_last= "+addQuotes(last)+", professor_department= "+
							addQuotes(department) + ",professor_startedWork = " + year +" WHERE professor_id = "+ professor_id;
					stmt.executeUpdate(query);
					System.out.println("Update success");
				}else 
					System.out.println("No Change");
			}
			
		}else if(tableChoose.equals("Courses")) {
			int course_id = -1;
			System.out.println("You requested to change the courses information! ");
			System.out.print("Please enter the course section: ");
			String course_section = scan.next();
			
			String sqlSearch = "SELECT course_id FROM Courses WHERE course_section = "+ addQuotes(course_section);
			ResultSet result = stmt.executeQuery(sqlSearch);
			boolean found = false;
			while(result.next()) {
				found = true;
				course_id = result.getInt("course_id");
			}
			result.close();
			if(!found) {
				System.out.println("There is no course match with the information you provided.");
				return;
			}else {
				System.out.print("Enter new course credit: ");
				double course_credit = scan.nextDouble();
				System.out.print("Enter new course section: ");
				course_section = scan.next();
				System.out.print("Enter new course name: ");
				String course_name = scan.next();
				System.out.print("Enter new course season: ");
				String course_season = scan.next();
				System.out.println();
				System.out.print("Are you sure want to update this table? (1 is Yes and 2 is No): ");
				int choice = scan.nextInt();
				if(choice == 1) {
					String query = "UPDATE Courses SET course_credit= "+ course_credit + ", course_section= "+addQuotes(course_section)+", course_name= "+
							addQuotes(course_name) + ",course_season = " + course_season +" WHERE course_id = "+ course_id;
					stmt.executeUpdate(query);
					System.out.println("Update success");
				}else 
					System.out.println("No Change");
			}
		}
	}



	private static void Insert(String tableChoose) throws SQLException {
		if(tableChoose.equals("Building")){
			System.out.print("Please enter a room number you would like to add: ");
			int room = scan.nextInt();
			String sqlSearchRoom = "SELECT room_id FROM Building WHERE room_number = "+room;
			ResultSet rs = stmt.executeQuery(sqlSearchRoom);
			boolean found = false;
			while(rs.next())
				found = true;
			rs.close();
			
			if(found) {
				System.out.println("The room is already existed!");
				System.out.println("No record updated");
				return;
			}

			System.out.println();
			System.out.print("Are you sure want to insert to this table? (1 is Yes and 2 is No): ");
			int choice = scan.nextInt();
			if(choice == 1) {
				String query = "INSERT INTO Building (room_number) VALUES ("+room+")";
				stmt.executeUpdate(query);
				System.out.println("Insert success");
			}else 
				System.out.println("No Change");
		}else if(tableChoose.equals("Professor")){
			System.out.print("Enter first name: ");
			String first = scan.next();
			System.out.print("Enter last name: ");
			String last = scan.next();
			System.out.print("Enter department: ");
			String department = scan.next();
			System.out.print("Enter year started: ");
			int year = scan.nextInt();
			System.out.println();
			System.out.print("Are you sure want to insert to this table? (1 is Yes and 2 is No): ");
			int choice = scan.nextInt();
			if(choice == 1) {
				String query = "INSERT INTO Professor (professor_first,professor_last,professor_department,professor_startedWork) VALUES "
						+ "("+ addQuotes(first)+ ","+ addQuotes(last) + "," + addQuotes(department) +"," + year+")";
				stmt.executeUpdate(query);
				System.out.println("Insert success");
			}else 
				System.out.println("No Change");
		}else if(tableChoose.equals("Courses")) {
			System.out.print("Enter course credit: ");
			Double credit = scan.nextDouble();
			System.out.print("Enter course section: ");
			String section = scan.next();
			System.out.print("Enter course name: ");
			String name = scan.next();
			System.out.print("Enter course season: ");
			String season = scan.next();
			System.out.println();
			System.out.print("Are you sure want to insert to this table? (1 is Yes and 2 is No): ");
			int choice = scan.nextInt();
			if(choice == 1) {
				String query = "INSERT INTO Courses (course_credit,course_section,course_name,course_season) VALUES "
						+ "("+ credit + ","+ addQuotes(section) + "," + addQuotes(name) +"," + addQuotes(season) +")";
				stmt.executeUpdate(query);
				System.out.println("Insert success");
			}else 
				System.out.println("No Change");
		}else if(tableChoose.equals("Building_Course")) {
			int room = -1,course = -1;
			System.out.println("You have selected to insert the connection between Building and Course.");
			System.out.print("Enter the room number you want to assign to the course: ");
			int roomNumber = scan.nextInt();
			String sqlSearchRoom = "SELECT room_id FROM Building WHERE room_number ="+roomNumber;
			ResultSet rs = stmt.executeQuery(sqlSearchRoom);
			while(rs.next())
				room = rs.getInt("room_id");
			rs.close();
			
			System.out.print("Enter course section: ");
			String section = scan.next();
			String sqlSearchCourse = "SELECT course_id FROM Courses WHERE course_section ="+addQuotes(section);
			rs = stmt.executeQuery(sqlSearchCourse);
			while(rs.next())
				course = rs.getInt("course_id");
			rs.close();
			
			System.out.print("Are you sure want to insert to this table? (1 is Yes and 2 is No): ");
			int choice = scan.nextInt();
			if(choice == 1) {
				String query = "INSERT INTO Building_Course (room_id,course_id) VALUES "
						+ "("+ room + ","+ course +")";
				stmt.executeUpdate(query);
				System.out.println("Insert success");
			}else 
				System.out.println("No Change");
		}else if(tableChoose.equals("Professor_Office")) {
			int room = -1, prof = -1;
			String first = null, last = null;
			System.out.println("You have selected to insert the connection between Building and Professor.");
			System.out.print("Enter the room number you want to assign to the professor: ");
			int roomNumber = scan.nextInt();
			String sqlSearchRoom = "SELECT room_id FROM Building WHERE room_number ="+roomNumber;
			ResultSet rs = stmt.executeQuery(sqlSearchRoom);
			while(rs.next())
				room = rs.getInt("room_id");
			rs.close();

			System.out.print("Enter teacher first name: ");
			first = scan.next();
			System.out.print("Enter teacher last name: ");
			last = scan.next();
			String sqlSearchProf = "SELECT professor_id FROM Professor WHERE professor_first ="+addQuotes(first)+ " AND professor_last = "+addQuotes(last);
			rs = stmt.executeQuery(sqlSearchProf);
			while(rs.next())
				prof = rs.getInt("professor_id");
			rs.close();
			
			System.out.print("Are you sure want to insert to this table? (1 is Yes and 2 is No): ");
			int choice = scan.nextInt();
			if(choice == 1) {
				String query = "INSERT INTO Professor_Office (room_id,professor_id) VALUES "
						+ "("+ room + ","+ prof +")";
				stmt.executeUpdate(query);
				System.out.println("Insert success");
			}else 
				System.out.println("No Change");
		}
	}



	private static void tableProfessor_Office(String tableChoose) throws SQLException {
	   	 String queryTable = "SELECT * FROM " + tableChoose + "";
	        ResultSet rs = stmt.executeQuery(queryTable);
	        System.out.println("This table is the connector between table Building and table Professor.");
	        while(rs.next()) {
	       	 System.out.println("Room ID: " + rs.getInt(1) + " Professor ID: " + rs.getInt(2));
	        }
	        rs.close();
	}


	private static void tableProfessor(String tableChoose) throws SQLException {
	   	 String queryTable = "SELECT * FROM " + tableChoose + "";
	        ResultSet rs = stmt.executeQuery(queryTable);
	        System.out.println("This table list all Professors informations.");
	        while(rs.next()) {
	       	 System.out.println(rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getInt(5));
	        }
	        rs.close();
	}


	private static void tableCourse(String tableChoose) throws SQLException {
	   	 String queryTable = "SELECT * FROM " + tableChoose + "";
	        ResultSet rs = stmt.executeQuery(queryTable);
	        System.out.println("This table list all Courses informations.");
	        while(rs.next()) {
	       	 System.out.println(rs.getDouble(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5));
	        }
	        rs.close();
	}


	private static void tableBuilding_Course(String tableChoose) throws SQLException {
	   	 String queryTable = "SELECT * FROM " + tableChoose + "";
	        ResultSet rs = stmt.executeQuery(queryTable);
	        System.out.println("This table is the connector between table Building and table Courses.");
	        while(rs.next()) {
	       	 System.out.println("Room ID: " + rs.getInt(1) + " Course ID: " + rs.getInt(2));
	        }
	        rs.close();
	}


	private static void tableBuilding(String tableChoose) throws SQLException {
   	 String queryTable = "SELECT * FROM " + tableChoose + "";
        ResultSet rs = stmt.executeQuery(queryTable);
        System.out.println("This table list all the room number.");
        while(rs.next()) {
       	 System.out.println(rs.getInt(2));
        }
        rs.close();
	}

	public static String addQuotes(String a) {
		return "'"+a+"'";
	}






























	public static String getPassword()
	{
		String p = "633796";
		return p;
	} // End getPassword()

} // End class Lab11
