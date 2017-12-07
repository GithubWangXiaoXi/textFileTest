//package textFileTest;
//
//import java.util.LinkedList;
//import java.util.Scanner;
//
///**
// * Created by Administrator on 2017/12/6 0006.
// */
//public class ComponentAction {
//
//    private FileEncode fileEncode;
//    private DatabaseConnection connection;
//
//
//    public ComponentAction() throws Exception
//    {
//        fileEncode = new FileEncode();
//        connection = new DatabaseConnection();
//    }
//
//    public void addAction()
//    {
//
//    }
//
//    public void deleteAction()
//    {
//
//    }
//
//    public void updateAction() throws Exception
//    {
//        System.out.println("批量修改文件，请输入文件所在位置");
//        Scanner input = new Scanner(System.in);
//        String url =input.nextLine();
//
//        /**    读入更新文件，待会返回一个学生链表,先删除数据库里的内容，
//         * 再添加进去，又因为增删的函数参数是文件地址，所以需要先把数据库的初次信息和修改后的信息写出
//         */
//        fileEncode.readDataUpdatedFile(url);
//
//        //原始学生数据所在的文件
//        String original_url = "Updated_IDNumber.txt";
//        this.dataDelete(original_url);
//
//        //更新后学生数据所在的文件
//        String ultimate_url = "UltimateUpdated.txt";
//        this.dataAdd(ultimate_url);
//
//        //fileEncode.clearIDNumberList();
//        fileEncode.clearStudentsNewList();
//        fileEncode.clearStudentsList();
//
//        System.out.println("是否选择继续更新数据: 是的话输入1，否则输入2");
//        int answer2 = input.nextInt();
//        input.nextLine();
//        if (2 == answer2) {
//            updateflag = false;
//        }
//    }
//}
