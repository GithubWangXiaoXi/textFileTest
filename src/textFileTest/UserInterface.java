package textFileTest;

import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 *   用户可以对数据库内容进行建表，对数据增删改查
 * Created by Administrator on 2017/12/1 0001.
 */
public class UserInterface extends JFrame{

    //数据库基本操作
    private DatabaseConnection connection;
    private FileEncode fileEncode;
    public static boolean isSuccess = false;
    public static boolean addflag = false;
    public static boolean deleteflag = false;
    public static boolean updateflag = false;
    private Student student;
    //tablename会贯穿与增删改查中，又因为增删改查每个功能独立，所以将其定为一般属性
    private String tableName = "student";
    private LinkedList<String> texts;   //用来存文本框中输入的数据，最多为两次(1个是选择，1个文件路径)


    //UI界面
    private JPanel jPanel1;
    private JPanel jPanel2;

    private JLabel label1;

    private JTextField jTextField;
    private JTextArea jTextArea;
    private JButton jButton;
    private JButton jButton1;
    private JScrollPane jScrollPane;


    public UserInterface() throws Exception
    {
        connection = new DatabaseConnection();
        fileEncode = new FileEncode();
        student = new Student();
        this.initComponents();
    }


    private void initComponents()
    {
        texts = new LinkedList<>();

        jPanel1 = new JPanel();
        jPanel2 = new JPanel();

        jPanel1.setBorder(BorderFactory.createTitledBorder("数据库基本操作"));

        label1 = new JLabel();
        label1.setText("1,增加  2,删除  3,修改  4,建表  5,导出");

        jTextField = new JTextField(20);

        jButton = new JButton("确定");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //得到jTextFile里的内容，执行相对应的结果，再把提示性内容输入到jTextArea中
                try {
                    jButtonActionPerformed(e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        jButton1 = new JButton("重置");
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton1ActionPerformed();
            }
        });

        jTextArea = new JTextArea(8,35);
        jScrollPane = new JScrollPane();

        jPanel1.add(label1);

        jTextArea.setEditable(false);
        jScrollPane.setViewportView(jTextArea);
        jPanel1.add(jScrollPane);

        jPanel2.add(jTextField, BorderLayout.NORTH);
        jPanel2.add(jButton,BorderLayout.SOUTH);
        jPanel2.add(jButton1,BorderLayout.SOUTH);

        this.add(jPanel1,BorderLayout.NORTH);
        this.add(jPanel2,BorderLayout.SOUTH);

        this.setAlwaysOnTop(true);  //使界面始终停在上面
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(400,240);
        this.setResizable(false);
        //this.pack();
        this.setVisible(true);
    }

     //制作监听器
    private void jButtonActionPerformed(ActionEvent e) throws Exception
    {

        String text = jTextField.getText();
        texts.add(text);

        if (TrivialFunctions.isNumeric(texts.get(0))) {

            int answer1 = Integer.parseInt(texts.get(0));

            if (answer1 >= 1 && answer1 <= 5) {

                if(answer1 == 4)
                {
                    jTextField.setText("");
                    String str = "你想建的表的名字";
                    jTextArea.setText(str);
                }
                else if(answer1 == 5)
                {
                    jTextField.setText("");
                    String str = "请输入csv文件路径和籍贯(只能写三个字),格式为 a,b";
                    jTextArea.setText(str);
                }
                else{
                    jTextField.setText("");

                    String sentence1 = "请输入文件所在位置";

                    jTextArea.setText(sentence1);
                }

                if (texts.size() == 2) {
                    String answer2 = texts.get(1);

                    if (answer1 == 1) {

                        File file = new File(texts.get(1));

                        if(!file.exists())
                        {
                            String str = "文件不存在";
                            jTextArea.setText(str);
                        }
                        else
                        {
                            //读取文本框中文件的位置信息
                            fileEncode.readDataInsertedFile(texts.get(1));
                            LinkedList<Student> students = fileEncode.getStudents();

                            /** 将学生对象数据批量写入数据库中 */
                            int length = students.size();
                            int i = 0;
                            while (students != null && i < length) {
                                student = students.get(i++);
                                connection.DataAdd(tableName, student);
                            }

                            //清空链表内容，方便以后继续循环操作
                            fileEncode.clearStudentsList();

                            String str = texts.get(1) + " 文件数据插入成功";

                            Thread.sleep(500);

                            jTextArea.setText(str);


                            //添加成功，清除暂存的文本框消息，文本域和文本框的内容
                            texts.clear();
                            return;
                        }
                    }

                    if (answer1 == 2) {

                        File file = new File(texts.get(1));

                        if(!file.exists())
                        {
                            String str = "文件不存在";
                            jTextArea.setText(str);
                        }
                        else{
                            //读入删除的文件
                            fileEncode.readDataDeletedFile(texts.get(1));
                            LinkedList<String> IDNumbers = new LinkedList<>();

                            /**
                             * 删除文件是否存在的代码
                             */

                            IDNumbers = fileEncode.getIDNumber();
                            int rows = IDNumbers.size();

                            for (int i = 0; i < rows; i++) {
                                String sql = "Delete from " + tableName + " where ID='" + IDNumbers.get(i) + "'";

                                try {
                                    connection.DataDelete(tableName, sql);
                                } catch (Exception f) {
                                    //System.out.println("删除语句有误");
                                }
                            }

                            //清空链表内容，方便以后继续循环操作
                            fileEncode.clearIDNumberList();

                            String str = texts.get(1) + "文件数据删除成功";

                            Thread.sleep(500);
                            jTextArea.setText(str);


                            //删除成功，清除暂存的文本框消息，文本域和文本框的内容
                            texts.clear();
                            return;
                        }
                    }

                    if (answer1 == 3) {

                        File file = new File(texts.get(1));

                        if(!file.exists())
                        {
                            String str = "文件不存在";
                            jTextArea.setText(str);
                        }
                        else {
                            /**    读入更新文件，待会返回一个学生链表,先删除数据库里的内容，
                             * 再添加进去，又因为增删的函数参数是文件地址，所以需要先把数据库的初次信息和修改后的信息写出
                             */
                            fileEncode.readDataUpdatedFile(texts.get(1));

                            //原始学生数据所在的文件
                            String original_url = "Updated_IDNumber.txt";
                            this.dataDelete(original_url);

                            //更新后学生数据所在的文件
                            String ultimate_url = "UltimateUpdated.txt";
                            this.dataAdd(ultimate_url);

                            //fileEncode.clearIDNumberList();
                            fileEncode.clearStudentsNewList();
                            fileEncode.clearStudentsList();

                            String str = texts.get(1) + " 文件数据更新成功";

                            Thread.sleep(500);
                            jTextArea.setText(str);

                            //更新成功，清除暂存的文本框消息，文本域和文本框的内容
                            texts.clear();
                            return;
                        }
                    }

                    if(answer1 == 4)
                    {
                        tableName = texts.get(1);
                        try {
                            connection.TableCreate(tableName);
                            String str1 = texts.get(1)+"建表成功";
                            jTextArea.setText(str1);
                        } catch (Exception f) {
                            String str = "建表错误,该表之前已建立";
                            jTextArea.setText(str);
                        }

                        //建表成功，清除暂存的文本框消息，文本域和文本框的内容
                        texts.clear();
                        return;
                    }

                    if (answer1 == 5) {
                        TrivialFunctions a = new TrivialFunctions();

                        String token[] = texts.get(1).split(",");
                        String path = token[0];
                        String birth = token[1];

                        a.WriteFromDatabaseToCsv(path,birth);

                        String str1 = path+" 文件导出成功";
                        Thread.sleep(500);
                        jTextArea.setText(str1);

                        //导出成功，清除暂存的文本框消息，文本域和文本框的内容
                        texts.clear();
                        return;
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "请输入1~5的整数", "错误", JOptionPane.WARNING_MESSAGE);
                this.requestFocus();
                texts.clear();
                return;
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "请输入1~5的整数", "错误", JOptionPane.WARNING_MESSAGE);
            this.requestFocus();
            texts.clear();
            return;
        }
    }

    //重置按钮监听器
    private void jButton1ActionPerformed()
    {
        jTextArea.setText("");
        jTextField.setText("");
    }



    public DatabaseConnection getConnection()
    {
        return connection;
    }

    public void tableCreate()
    {


    }


    //用来帮助修改学生元组中属性的方法，增加修改后的元组
    public void dataAdd(String url) throws Exception
    {
        fileEncode.readDataInsertedFile(url);
        LinkedList<Student> students = fileEncode.getStudents();

        /** 将学生对象数据批量写入数据库中 */

        int length = students.size();
        int i = 0;
        while (students != null && i < length) {
            student = students.get(i++);
            connection.DataAdd(tableName, student);
        }

        //清空链表内容，方便以后继续循环操作
        fileEncode.clearStudentsList();
    }


    //用来帮助修改学生元组中属性的方法，删除原有的元组
    private void dataDelete(String url)
    {
        //读入删除的文件
        fileEncode.readDataDeletedFile(url);

        LinkedList<String> IDNumbers = new LinkedList<>();
        IDNumbers = fileEncode.getIDNumber();
        int rows = IDNumbers.size();

        for(int i = 0;i<rows;i++)
        {
            String sql = "Delete from " + tableName+ " where ID='"+IDNumbers.get(i)+"'";
            try {
                connection.DataDelete(tableName, sql);
            }catch(Exception e)
            {
                //System.out.println("删除语句有误");
            }
        }

        fileEncode.clearIDNumberList();
    }


    public void dataSearch() throws Exception
    {
        String sql = "select * from "+ tableName + " order by ID asc";
        connection.DataSearch(tableName,sql);
    }


    public static void main(String[] args) throws  Exception{

        UserInterface user = new UserInterface();

        //用户可以连接数据库，并且得到那把"钥匙"
        DatabaseConnection connection = user.getConnection();


        //建表不成功，则继续
//        while(!isSuccess) {
//            user.tableCreate();
//        }

    }
}