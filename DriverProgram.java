
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
			conn = DriverManager.getConnection("jdbc:mysql://cs.neiu.edu:3306/YOURDATABASENAME?" + "user=YOURUSERNAME&password=" + getPassword() ); //edit your password at line 966

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
									break;
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



	private static void Search(String tableChoose) throws SQLException {
		System.out.println();
		if(tableChoose.equals("Building")){
			System.out.println("Since this table only contains room numbers, so Which one do you want to find the available room that is not yet assgined?");
			System.out.println("1. Building_Course (Room that has been assigned for the Course)");
			System.out.println("2. Professor_Office (Room that has been assigned for the Professor as an office)");
			System.out.println("3. Exit");
			System.out.print("Your choice: ");
			int choice = scan.nextInt();
			System.out.println();
			if(choice == 1) {
				String sql = "SELECT t1.room_number FROM Building t1 LEFT JOIN Building_Course t2 ON t2.room_id = t1.room_id WHERE t2.room_id IS NULL";
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getInt("room_number"));
					found = true;
				}
				if(!found)
					System.out.println("There is no room available right now!");
			}else if(choice == 2) {
				String sql = "SELECT t1.room_number FROM Building t1 LEFT JOIN Professor_Office t2 ON t2.room_id = t1.room_id WHERE t2.room_id IS NULL";
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getInt("room_number"));
					found = true;
				}
				if(!found)
				System.out.println("There is no room available right now!");
			}
		}else if(tableChoose.equals("Professor")){
			System.out.println("How do you want to find the professor information?");
			System.out.println("1. By first name");
			System.out.println("2. By last name");
			System.out.println("3. By department");
			System.out.println("4. By year started to work");
			System.out.println("5. By all the information");
			System.out.println("6. Exit");
			System.out.print("Your choice: ");
			int choice = scan.nextInt();
			System.out.println();
			if(choice == 1) {
				System.out.print("Please enter the first name: ");
				String first = scan.next();
				String sql = "SELECT professor_first,professor_last,professor_department,professor_startedWork FROM Professor WHERE professor_first = " + addQuotes(first);
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getString("professor_first") + " " + rs.getString("professor_last") + " " + rs.getString("professor_department") + " " + rs.getString("professor_startedWork"));
					found = true;
				}
				if(!found) {
					System.out.println("There is no nothing match with the information you provided.");
				}
			}else if(choice == 2) {
				System.out.print("Please enter the last name: ");
				String last = scan.next();
				String sql = "SELECT professor_first,professor_last,professor_department,professor_startedWork FROM Professor WHERE professor_last = " + addQuotes(last);
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getString("professor_first") + " " + rs.getString("professor_last") + " " + rs.getString("professor_department") + " " + rs.getString("professor_startedWork"));
					found = true;
				}
				if(!found) {
					System.out.println("There is no nothing match with the information you provided.");
				}
			}else if(choice == 3) {
				System.out.print("Please enter the department: ");
				String department = scan.next();
				String sql = "SELECT professor_first,professor_last,professor_department,professor_startedWork FROM Professor WHERE professor_department = " + addQuotes(department);
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getString("professor_first") + " " + rs.getString("professor_last") + " " + rs.getString("professor_department") + " " + rs.getString("professor_startedWork"));
					found = true;
				}
				if(!found) {
					System.out.println("There is no nothing match with the information you provided.");
				}
			}else if(choice == 4) {
				System.out.print("Please enter the year: ");
				String year = scan.next();
				String sql = "SELECT professor_first,professor_last,professor_department,professor_startedWork FROM Professor WHERE professor_startedWork = " + addQuotes(year);
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getString("professor_first") + " " + rs.getString("professor_last") + " " + rs.getString("professor_department") + " " + rs.getString("professor_startedWork"));
					found = true;
				}
				if(!found) {
					System.out.println("There is no nothing match with the information you provided.");
				}
			}else if(choice == 5) {
				System.out.print("Please enter the first name: ");
				String first = scan.next();
				System.out.print("Please enter the last name: ");
				String last = scan.next();
				System.out.print("Please enter the department: ");
				String department = scan.next();
				System.out.print("Please enter the year: ");
				String year = scan.next();
				String sql = "SELECT professor_first,professor_last,professor_department,professor_startedWork FROM Professor WHERE professor_startedWork = " + addQuotes(year)
							+ " AND professor_department = " + addQuotes(department) + " AND professor_last = "+ addQuotes(last) + " AND professor_first = " + addQuotes(first);
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getString("professor_first") + " " + rs.getString("professor_last") + " " + rs.getString("professor_department") + " " + rs.getString("professor_startedWork"));
					found = true;
				}
				if(!found) {
					System.out.println("There is no nothing match with the information you provided.");
				}
			}


		}else if(tableChoose.equals("Courses")){

			System.out.println("How do you want to find the course information?");
			System.out.println("1. By course section");
			System.out.println("2. By course name");
			System.out.println("3. By course season");
			System.out.println("4. By all the information");
			System.out.println("5. Exit");
			System.out.print("Your choice: ");
			int choice = scan.nextInt();
			System.out.println();
			if(choice == 1) {
				System.out.print("Please enter the course section: ");
				String course_section = scan.next();
				String sql = "SELECT course_credit,course_section,course_name,course_season FROM Courses WHERE course_section = " + addQuotes(course_section);
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getString("course_credit") + " " + rs.getString("course_section") + " " + rs.getString("course_name") + " " + rs.getString("course_season"));
					found = true;
				}
				if(!found) {
					System.out.println("There is no nothing match with the information you provided.");
				}
			}else if(choice == 2) {
				System.out.print("Please enter the course name: ");
				String course_name = scan.next();
				String sql = "SELECT course_credit,course_section,course_name,course_season FROM Courses WHERE course_name = " + addQuotes(course_name);
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getString("course_credit") + " " + rs.getString("course_section") + " " + rs.getString("course_name") + " " + rs.getString("course_season"));
					found = true;
				}
				if(!found) {
					System.out.println("There is no nothing match with the information you provided.");
				}
			}else if(choice == 3) {
				System.out.print("Please enter the course season: ");
				String course_season = scan.next();
				String sql = "SELECT course_credit,course_section,course_name,course_season FROM Courses WHERE course_season = " + addQuotes(course_season);
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getString("course_credit") + " " + rs.getString("course_section") + " " + rs.getString("course_name") + " " + rs.getString("course_season"));
					found = true;
				}
				if(!found) {
					System.out.println("There is no nothing match with the information you provided.");
				}
			}else if(choice == 4) {
				System.out.print("Please enter the course section: ");
				String course_section = scan.next();
				System.out.print("Please enter the course name: ");
				String course_name = scan.next();
				System.out.print("Please enter the course season: ");
				String course_season = scan.next();
				String sql = "SELECT course_credit,course_section,course_name,course_season FROM Courses WHERE course_section = " + addQuotes(course_section)
							+ " AND course_name = " + addQuotes(course_name) + " AND course_season = "+ addQuotes(course_season);
				rs = stmt.executeQuery(sql);
				boolean found = false;
				while(rs.next()) {
					System.out.println(rs.getString("course_credit") + " " + rs.getString("course_section") + " " + rs.getString("course_name") + " " + rs.getString("course_season"));
					found = true;
				}
				if(!found) {
					System.out.println("There is no nothing match with the information you provided.");
				}
			}
			
			
		}else if(tableChoose.equals("Building_Course")){
			System.out.println("Since this table is a connection to show which course is assigned to which room. So which course do you want to find its room number?");
			System.out.print("Please enter the course section: ");
			String course_section = scan.next();
			System.out.println();
			String sqlCourse_id = "SELECT course_id FROM Courses WHERE course_section = " + addQuotes(course_section);
			int course_id = -1;
			rs= stmt.executeQuery(sqlCourse_id);
			while(rs.next())
				course_id = rs.getInt("course_id");

			String sqlRoom_id = "SELECT room_id FROM Building_Course WHERE course_id = " + course_id;
			int room_id = -1;
			rs= stmt.executeQuery(sqlRoom_id);
			while(rs.next())
				room_id = rs.getInt("room_id");
			
			boolean found = false;
			String sql = "SELECT room_number FROM Building WHERE room_id = " + room_id;
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				System.out.println("The room number for this course is: "+ rs.getInt("room_number"));
				found = true;
			}
			if(!found) {
				System.out.println("There is no nothing match with the information you provided.");
			}
			
			
		}else if(tableChoose.equals("Professor_Office")){
			System.out.println("Since this table is a connection to show which professor is assigned to which room. So who do you want to find the room number for?");
			System.out.print("Please enter the professor first name: ");
			String first = scan.next();
			System.out.print("Please enter the professor last name: ");
			String last = scan.next();
			System.out.println();
			String sqlProfessor_id = "SELECT professor_id FROM Professor WHERE professor_first = " + addQuotes(first) + " AND professor_last = " + addQuotes(last);
			int professor_id = -1;
			rs= stmt.executeQuery(sqlProfessor_id);
			while(rs.next())
				professor_id = rs.getInt("professor_id");

			String sqlRoom_id = "SELECT room_id FROM Professor_Office WHERE professor_id = " + professor_id;
			int room_id = -1;
			rs= stmt.executeQuery(sqlRoom_id);
			while(rs.next())
				room_id = rs.getInt("room_id");
			
			boolean found = false;
			String sql = "SELECT room_number FROM Building WHERE room_id = " + room_id;
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				System.out.println("The room number for this professor is: "+ rs.getInt("room_number"));
				found = true;
			}
			if(!found) {
				System.out.println("There is no nothing match with the information you provided.");
			}
			

		}

	}



	private static void Delete(String tableChoose) throws SQLException {
		if(tableChoose.equals("Building")){
			System.out.print("Please enter the room number that you want to delete: ");
			int room_number = scan.nextInt();
			String sqlSearchRoom = "SELECT room_id FROM Building WHERE room_number = "+room_number;
			ResultSet result = stmt.executeQuery(sqlSearchRoom);
			int room_id = -1;
			boolean found = false;
			while(result.next()) {
				room_id = result.getInt("room_id");
				found = true;
			}
			result.close();
			if(found) {
				boolean inBuilding_Course = false, inProfessor_Office = false;
				//find if the primary contains in Building_Course
				result = stmt.executeQuery("SELECT room_id FROM Building_Course WHERE room_id = "+room_id);
				while(result.next()) {
					inBuilding_Course = true;
				}
				result.close();

				//find if the primary contains in Professor_Office
				result = stmt.executeQuery("SELECT room_id FROM Professor_Office WHERE room_id = "+room_id);
				while(result.next()) {
					inProfessor_Office = true;
				}
				result.close();
				
				if(inBuilding_Course){
					System.out.println("You have to remove the room_id " + room_id + " in table Building_Course in order to remove this column");
				}
				if(inProfessor_Office){
					System.out.println("You have to remove the room_id " + room_id + " in table Professor_Office in order to remove this column");
				}
				if(!inBuilding_Course && !inProfessor_Office) {
					stmt.execute("DELETE FROM Building WHERE room_id = "+room_id);
					System.out.println("Delete Success");
				}else
					System.out.println("No Record Deleted");
				
			}else {
				System.out.println("There is no nothing match with the information you provided.");
				return;
			}
			
		}
		
		else if(tableChoose.equals("Professor")){
			System.out.print("Please enter the professor first name that you want to delete: ");
			String first = scan.next();
			System.out.print("Please enter the professor last name that you want to delete: ");
			String last = scan.next();
			String sqlSearchProf = "SELECT professor_id FROM Professor WHERE professor_first = "+addQuotes(first)+ " AND professor_last = " + addQuotes(last);
			ResultSet result = stmt.executeQuery(sqlSearchProf);
			int professor_id = -1;
			boolean found = false;
			while(result.next()) {
				professor_id = result.getInt("professor_id");
				found = true;
			}
			result.close();
			if(found) {
				boolean inCourses = false, inProfessor_Office = false;
				//find if the primary contains in Courses
				result = stmt.executeQuery("SELECT professor_id FROM Courses WHERE professor_id = "+professor_id);
				while(result.next()) {
					inCourses = true;
				}
				result.close();

				//find if the primary contains in Professor_Office
				result = stmt.executeQuery("SELECT professor_id FROM Professor_Office WHERE professor_id = "+professor_id);
				while(result.next()) {
					inProfessor_Office = true;
				}
				result.close();
				
				if(inCourses){
					System.out.println("You have to remove the professor_id " + professor_id + " in table Courses in order to remove this column");
				}
				if(inProfessor_Office){
					System.out.println("You have to remove the professor_id " + professor_id + " in table Professor_Office in order to remove this column");
				}
				if(!inCourses && !inProfessor_Office) {
					stmt.execute("DELETE FROM Professor WHERE professor_id = "+professor_id);
					System.out.println("Delete Success");
				}else
					System.out.println("No Record Deleted");
				
			}else {
				System.out.println("There is no nothing match with the information you provided.");
				return;
			}
			
		}
		
		else if(tableChoose.equals("Courses")){
			System.out.print("Please enter the course_section that you want to delete: ");
			String course_section = scan.next();
			String sqlSearchCourse = "SELECT course_id FROM Courses WHERE course_section = "+addQuotes(course_section);
			ResultSet result = stmt.executeQuery(sqlSearchCourse);
			int course_id = -1;
			boolean found = false;
			while(result.next()) {
				course_id = result.getInt("course_id");
				found = true;
			}
			result.close();
			if(found) {
				boolean inBuilding_Course = false;
				//find if the primary contains in Courses
				result = stmt.executeQuery("SELECT course_id FROM Building_Course WHERE course_id = "+course_id);
				while(result.next()) {
					inBuilding_Course = true;
				}
				result.close();

				if(inBuilding_Course){
					System.out.println("You have to remove the course_id " + course_id + " in table Building_Course in order to remove this column");
				}
				if(!inBuilding_Course) {
					stmt.execute("DELETE FROM Courses WHERE course_id = "+course_id);
					System.out.println("Delete Success");
				}else
					System.out.println("No Record Deleted");
				
			}else {
				System.out.println("There is no nothing match with the information you provided.");
				return;
			}
			
		}

		else if(tableChoose.equals("Building_Course")){
			System.out.print("Please enter the room_id that you want to delete: ");
			int room_id = scan.nextInt();
			System.out.print("Please enter the course_id that you want to delete: ");
			int course_id = scan.nextInt();
			String sqlSearch = "SELECT room_id,course_id FROM Building_Course WHERE room_id = "+room_id + " AND course_id = " + course_id;
			ResultSet result = stmt.executeQuery(sqlSearch);
			boolean found = false;
			while(result.next())
				found=true;
			if(found) {
				stmt.execute("DELETE FROM Building_Course WHERE room_id = "+room_id + " AND course_id = " + course_id);
				System.out.println("Delete Success");
			}else {
				System.out.println("There is no nothing match with the information you provided.");
				return;
			}
		}
		
		else if(tableChoose.equals("Professor_Office")){
			System.out.print("Please enter the room_id that you want to delete: ");
			int room_id = scan.nextInt();
			System.out.print("Please enter the professor_id that you want to delete: ");
			int professor_id = scan.nextInt();
			String sqlSearch = "SELECT room_id,professor_id FROM Professor_Office WHERE room_id = "+room_id + " AND professor_id = " + professor_id;
			ResultSet result = stmt.executeQuery(sqlSearch);
			boolean found = false;
			while(result.next())
				found=true;
			if(found) {
				stmt.execute("DELETE FROM Professor_Office WHERE room_id = "+room_id + " AND professor_id = " + professor_id);
				System.out.println("Delete Success");
			}else {
				System.out.println("There is no nothing match with the information you provided.");
				return;
			}
		}
	}



	private static void Update(String tableChoose) throws SQLException {
		if(tableChoose.equals("Building")){
			System.out.print("Please enter the room number that you want to modify: ");
			int oldRoom = scan.nextInt();
			
			String sqlSearchRoom = "SELECT room_id FROM Building WHERE room_number = "+oldRoom;
			ResultSet rs = stmt.executeQuery(sqlSearchRoom);
			boolean found = false;
			while(rs.next())
				found = true;
			rs.close();
			
			if(!found) {
				System.out.println("The room is you entered is not exist!");
				System.out.println("No record updated");
				return;
			}
			System.out.print("Please enter the new room number that you want to update: ");
			int newRoom = scan.nextInt();
			
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
							addQuotes(course_name) + ",course_season = " + addQuotes(course_season) +" WHERE course_id = "+ course_id;
					stmt.executeUpdate(query);
					System.out.println("Update success");
				}else 
					System.out.println("No Change");
			}
			
			
		}else if(tableChoose.equals("Building_Course")) {
			System.out.println("You requested to change the connection between Building and Courses! ");
			System.out.print("Please enter the room_id you would like to change: ");
			int room_id = scan.nextInt();
			System.out.print("Please enter the course_id you would like to change: ");
			int course_id = scan.nextInt();
			
			String sqlSearch = "SELECT room_id, course_id FROM Building_Course WHERE room_id = "+ room_id + " AND course_id= " + course_id;
			ResultSet result = stmt.executeQuery(sqlSearch);
			boolean found = false;
			while(result.next()) {
				found = true;
			}
			result.close();
			if(!found) {
				System.out.println("There is no connection match with the information you provided.");
				return;
			}else {
				System.out.print("Enter new room_id: ");
				int new_room_id = scan.nextInt();
				System.out.print("Enter new course_id: ");
				int new_course_id = scan.nextInt();
				System.out.println();
				System.out.print("Are you sure want to update this table? (1 is Yes and 2 is No): ");
				int choice = scan.nextInt();
				if(choice == 1) {
					String query = "UPDATE Building_Course SET room_id= "+ new_room_id + ", course_id= "+ new_course_id +
							" WHERE room_id = "+ room_id + " AND course_id= " + course_id;
					stmt.executeUpdate(query);
					System.out.println("Update success");
				}else 
					System.out.println("No Change");
			}
			
		}else if(tableChoose.equals("Professor_Office")) {
			System.out.println("You requested to change the connection between Building and Professor! ");
			System.out.print("Please enter the room_id you would like to change: ");
			int room_id = scan.nextInt();
			System.out.print("Please enter the professor_id you would like to change: ");
			int professor_id = scan.nextInt();
			
			String sqlSearch = "SELECT room_id, professor_id FROM Professor_Office WHERE room_id = "+ room_id + " AND professor_id= " + professor_id;
			ResultSet result = stmt.executeQuery(sqlSearch);
			boolean found = false;
			while(result.next()) {
				found = true;
			}
			result.close();
			if(!found) {
				System.out.println("There is no connection match with the information you provided.");
				return;
			}else {
				System.out.print("Enter new room_id: ");
				int new_room_id = scan.nextInt();
				System.out.print("Enter new professor_id: ");
				int new_professor_id = scan.nextInt();
				System.out.println();
				System.out.print("Are you sure want to update this table? (1 is Yes and 2 is No): ");
				int choice = scan.nextInt();
				if(choice == 1) {
					String query = "UPDATE Professor_Office SET room_id= "+ new_room_id + ", professor_id= "+ new_professor_id +
							" WHERE room_id = "+ room_id + " AND professor_id= " + professor_id;
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
