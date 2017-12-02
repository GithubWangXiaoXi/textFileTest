package textFileTest;

import java.util.Scanner;

/**
 *   用户可以对数据库内容进行建表，对数据增删改查
 * Created by Administrator on 2017/12/1 0001.
 */
public class UserInterface {

    private DatabaseConnection connection;
    private String tableName = "lll";  //tablename会贯穿与增删改查中，又因为增删改查每个功能独立，所以将其定为一般属性
    public static boolean isSuccess = false;
    public static boolean addflag = false;
    public static boolean deleteflag = false;
    public static boolean updateflag = false;

    public UserInterface() throws Exception
    {
        connection = new DatabaseConnection();
    }

    public DatabaseConnection getConnection()
    {
        return connection;
    }

    public void tableCreate()
    {
        Scanner input = new Scanner(System.in);

        //用户可以建表
        System.out.print("你想建的表的名字: ");
        tableName = input.nextLine();
        try {
            connection.TableCreate(tableName);
            isSuccess = true;
        } catch (Exception e) {
            System.out.println(tableName+"之前已建立，请重新输入");
            isSuccess = false;
        }
    }

    public void dataAdd() throws Exception
    {
        Scanner input = new Scanner(System.in);
        while(addflag)
        {
            //这个输入操作应该放在写入文件里面，待会在这个页面呈现的应该是文件读取后的结果
            System.out.println("学生数据具体为：学生编号，姓名，性别，身高，生日，籍贯");

            System.out.println("请输入学生编号:");
            String ID =input.next();
            input.nextLine();  //用来读回车键

            System.out.println("请输入姓名:");
            String name = input.nextLine();

            System.out.println("请输入性别:");
            String sex = input.nextLine();

            System.out.println("请输入身高:");
            double height = input.nextDouble();
            input.nextLine();

            System.out.println("请输入生日:");
            int years = input.nextInt();
            int months = input.nextInt();
            int days = input.nextInt();
            input.nextLine();

            System.out.println("请输入籍贯:");
            String birth_place = input.nextLine();

            connection.DataAdd(tableName,ID,name,sex,height,years, months, days,birth_place);

            System.out.println("是否选择继续添加数据: 是的话输入1，否则输入2");
            int answer2 = input.nextInt();
            input.nextLine();
            if(2 == answer2)
            {
                addflag = false;
            }
        }
    }

    public void dataDelete()
    {
        //tableName = "Liang";

        Scanner input = new Scanner(System.in);
        while(deleteflag) {
            String sql = "Delete from " + tableName + " where ID=10002";   //不知为什么ID不存在不会报错
            try {
                connection.DataDelete(tableName, sql);
            } catch (Exception e) {
                System.out.println("删除错误：或许该ID不存在");
            }

            System.out.println("是否选择继续删除数据: 是的话输入1，否则输入2");
            int answer2 = input.nextInt();
            input.nextLine();
            if (2 == answer2) {
                deleteflag = false;
            }
        }
    }

    public void dataUpdate() throws Exception
    {
        while(updateflag)
        {
            String sql = "Update "+tableName+" set NAME = '王小希' where ID = 'asld;f'";
            connection.DataUpdate(tableName,sql);

            Scanner input = new Scanner(System.in);
            System.out.println("是否选择继续更新数据: 是的话输入1，否则输入2");
            int answer2 = input.nextInt();
            input.nextLine();
            if (2 == answer2) {
                updateflag = false;
            }
        }
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
        while(!isSuccess) {
            user.tableCreate();
        }

        //用户可以批量添加学生数据
        Scanner input = new Scanner(System.in);
        System.out.println("是否添加学生数据: 是的话输入1，否则输入2");
        int answer = input.nextInt();
        addflag = false;
        if(1 == answer)
        {
            addflag = true;
        }
        user.dataAdd();

        //用户可以删除学生数据
        //Scanner input = new Scanner(System.in);
        System.out.println("是否删除学生数据: 是的话输入1，否则输入2");
        int answer1 = input.nextInt();
        deleteflag = false;
        if(1 == answer1)
        {
            deleteflag = true;
        }
        user.dataDelete();


        //用户可以更新学生数据
        System.out.println("是否更改学生数据: 是的话输入1，否则输入2");
        int answer2 = input.nextInt();
        updateflag = false;
        if(1 == answer2)
        {
            updateflag = true;
        }
        user.dataUpdate();
        input.nextLine();

        System.out.println("是否查询学生数据: 是的话输入1，否则输入2");
        int answer3 = input.nextInt();
        input.nextLine();
        if(answer3 == 1)
        user.dataSearch();

    }
}
