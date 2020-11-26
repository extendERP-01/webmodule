//package nirmalya.aathithya.webmodule.master.controller;
//
////import java.io.FileInputStream;
////import java.io.FileNotFoundException;
////import java.io.IOException;
////import java.sql.Connection;
////import java.sql.DriverManager;
////import java.sql.PreparedStatement;
////import java.sql.SQLException;
////import java.sql.Statement;
////import java.util.ArrayList;
////import java.util.Iterator;
////import java.util.List;
////import java.util.Properties;
////import org.apache.poi.ss.usermodel.Cell;
////import org.apache.poi.ss.usermodel.CellType;
////import org.apache.poi.ss.usermodel.Row;
////import org.apache.poi.xssf.usermodel.XSSFRow;
////import org.apache.poi.xssf.usermodel.XSSFSheet;
////import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import nirmalya.aathithya.webmodule.master.model.MasterCountryModel;
//
///**
// *
// * @author nidhi
// */
//public class POIex2 {
//
//    XSSFRow row;
//    MasterCountryModel e = new MasterCountryModel();
//
////    @SuppressWarnings("resource")
////	public List<MasterCountryModel> readFile(String fileName) throws FileNotFoundException, IOException {
////        FileInputStream fis;
////        List<MasterCountryModel> countryList = new ArrayList<MasterCountryModel>();
////        try {
////            System.out.println("-------------------------------READING THE SPREADSHEET-------------------------------------");
////            fis = new FileInputStream(fileName);
////            XSSFWorkbook workbookRead = new XSSFWorkbook(fis);
////            XSSFSheet spreadsheetRead = workbookRead.getSheetAt(0);
////
////            Iterator< Row> rowIterator = spreadsheetRead.iterator();
////            System.out.println("ROW: "+rowIterator.hasNext());
////            while (rowIterator.hasNext()) {
////                row = (XSSFRow) rowIterator.next();
////                Iterator< Cell> cellIterator = row.cellIterator();
////String id = "";
//////                String id = row.getCell(0).getStringCellValue();
////                String name = row.getCell(0).getStringCellValue();
////                String gender = row.getCell(1).getStringCellValue();
////                Double salary =  row.getCell(2).getNumericCellValue();
////                System.out.println("Id : "+id);
////                System.out.println("Name : "+name);
////                System.out.println("Gender : "+gender);
////                System.out.println("salary : "+salary);
////                MasterCountryModel moodels =  new MasterCountryModel(id ,name ,gender );
////                countryList.add(moodels);
////            }
////            fis.close();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return countryList;
////    }
//
////    public void InsertRowInDB(int empId, String empName, String gender, String salary) {
////
////        Float salaryDB = Float.parseFloat(salary);
////        try {
////
////            Properties properties = new Properties();
////            properties.setProperty("user", "root");
////            properties.setProperty("password", "root");
////            properties.setProperty("useSSL", "false");
////            properties.setProperty("autoReconnect", "true");
////
////            Class.forName("com.mysql.jdbc.Driver");
////            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/Attendance", properties);
////            Statement stmt = connect.createStatement();
////            PreparedStatement ps = null;
////            String sql = "INSERT INTO `Attendance`.`Employee_Master`\n"
////                    + "(`EmployeeId`,\n"
////                    + "`EmployeeName`,\n"
////                    + "`Gender`,\n"
////                    + "`Salary`)\n"
////                    + "VALUES(?,?,?,?)";
////            ps = connect.prepareStatement(sql);
////            ps.setInt(1, empId);
////            ps.setString(2, empName);
////            ps.setString(3, gender);
////            ps.setFloat(4, salaryDB);
////            ps.executeUpdate();
////            connect.close();
////        } catch (ClassNotFoundException | SQLException e) {
////            e.printStackTrace();
////        }
////    }
//}