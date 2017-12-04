//package  textFileTest;
//
//import java.io.*;
//import java.util.*;
//
//public class TextFileRead {
//    public static void main(String[] args) throws IOException {
//        try {
//            Scanner in = new Scanner(new File("D:\\Programmes\\Student.txt"));
//            {
//                Student[] newStudent = readData(in);
//                for (Student e : newStudent)
//                    System.out.println(e);
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//            System.out.println("**************");
//            System.out.println(e.getMessage());
//            System.out.println("#####");
//        }
//    }
//
//    // 读入该数组的长度，然后读入每条记录
//    private static Student[] readData(Scanner in) {
//        int n = in.nextInt();// 不包括坠在行尾的换行字符
//        // System.out.println(n);
//        in.nextLine();// 处理这个换行字符，获得下一个输入
//        Student[] Students = new Student[n];
//        for (int i = 0; i < n; i++) {
//            Students[i] = readStudent(in);
//        }
//        return Students;
//    }
//
//    // 读入记录，每次读一行，然后分离所有的字段
//    public static Student readStudent(Scanner in) {
//        String line = in.nextLine();
//        String[] tokens = line.split("\\|");// 将这一行断开成一组标记
//        String name = tokens[0];
//        double Height = Double.parseDouble(tokens[1]);
//        int year = String.parseInt(tokens[2]);
//        int month = String.parseInt(tokens[3]);
//        int day = String.parseInt(tokens[4]);
//        return new Student(name, Height, year, month, day);
//    }
//}
