//package textFileTest;
//
//import java.io.*;
//import java.util.*;
//
//public class TextFileTestWrite {
//    public static void main(String[] args) throws IOException {
//        Student[] staff = new Student[3];
//        staff[0] = new Student("Carl Cracker", 165.1, 1987, 12, 15);
//        staff[1] = new Student("Harry Hacker", 173.0, 1989, 10, 1);
//        staff[2] = new Student("Tony Tester", 67.1, 1990, 3, 15);
//        PrintWriter out = new PrintWriter("D:\\Programmes\\Student.txt");
//        out.println(staff.length);
//        for (Student e : staff) {
//            GregorianCalendar calendar = new GregorianCalendar();
//            calendar.setTime(e.getbirthday());
//            out.println(e.getName() + "|"
//                    + e.getHeight() + "|"
//                    + calendar.get(Calendar.YEAR) + "|"
//                    + (calendar.get(Calendar.MONTH) + 1) + "|"
//                    + calendar.get(Calendar.DAY_OF_MONTH));
//        }
//        out.close();
//    }
//}
