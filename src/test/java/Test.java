import bean.InParameter;
import bean.PermissionLog;
import tool.SimpleDT;

import java.sql.Date;

/**
 * Created by tianzhongqiu on 2018/7/4.
 */
public class Test {



    public static void main(String[] args) {
        PermissionLog permission=new PermissionLog();
        InParameter inParameter = new InParameter();
        String l="00:11:40.395 [http-nio-18088-exec-1] INFO  c.i.b.s.dc.api.user.impl.UserAPIImpl - #根据token获取用户信息接口#接口地址:user/getUserInfoFromAccessToken#入参:webAccountName=stone9069;#provinceCode=[B@7afc76db;provinceName=北京市;organizationName=;ESType=null;";



//        String a=split[1].substring(1,split[1].length()-1);
//        System.out.println(split.length);
//        System.out.println(a);
//        System.out.println(l.split(" ")[6]);
//        String timePrix = SimpleDT.formatDate(new Date())
        String[] permiLog = l.split(" ", -1);
        //存储 event 的 content
        String[] split = permiLog[6].split("#");
//        permission.setDateOperation(java.sql.Date.valueOf("2018-06-25"+" "+permiLog[0].split("\\.")[0]));
        //存储 event 的 create  +1 是要减去那个 ","
        System.out.println(java.sql.Date.valueOf("2018-06-25"));
        String fs="2018-06-22 20:15:36";
        permission.setHttpAndTelnum(permiLog[1].substring(1,permiLog[0].length()-1));
        if(permiLog[2].equals("INFO")){

            permission.setLogType(permiLog[2]);
            permission.setAPI(permiLog[4]);
            permission.setTraitName(split[2].split(":")[1]);
            permission.setTraitFeatures(split[1]);
            if (split[3].contains("出参")){
                permission.setOutParameter(permiLog[6].split(":")[2]);
            }else {
                permission.setInParameter(permiLog[6].split(":")[2]);
            }
        }else {
            permission.setLogType(permiLog[2]);
            permission.setAPI(permiLog[4]);
            permission.setErroReason(l.split("-")[5]);
        }

    }
}
