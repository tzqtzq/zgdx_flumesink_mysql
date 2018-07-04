package flume.mysqlsink;
import bean.InParameter;
import bean.PermissionLog;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.SimpleDT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
/**
 * Created by tianzhongqiu on 2018/7/3.
 */
public class MysqlSink extends AbstractSink implements Configurable{

    private Logger LOG = LoggerFactory.getLogger(MysqlSink.class);
    private String hostname;
    private String port;
    private String databaseName;
    private String tableName;
    private String user;
    private String password;
    private PreparedStatement preparedStatement;
    private Connection conn;
    private int batchSize;

    public MysqlSink() {
        LOG.info("MysqlSink start...");
    }

    public void configure(Context context) {
        hostname = context.getString("hostname");
        Preconditions.checkNotNull(hostname, "hostname must be set!!");
        port = context.getString("port");
        Preconditions.checkNotNull(port, "port must be set!!");
        databaseName = context.getString("databaseName");
        Preconditions.checkNotNull(databaseName, "databaseName must be set!!");
        tableName = context.getString("tableName");
        Preconditions.checkNotNull(tableName, "tableName must be set!!");
        user = context.getString("user");
        Preconditions.checkNotNull(user, "user must be set!!");
        password = context.getString("password");
        Preconditions.checkNotNull(password, "password must be set!!");
        batchSize = context.getInteger("batchSize", 1000);
        Preconditions.checkNotNull(batchSize > 0, "batchSize must be a positive number!!");
    }

    @Override
    public void start() {
        super.start();
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + databaseName;
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象

        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
            //创建一个Statement对象
            preparedStatement = conn.prepareStatement("insert into " + tableName +
                    " (dateOperation,httpAndTelnum,logType,API,traitName,traitFeatures,inParameter,outParameter,erroReason) values (?,?,?,?,?,?,?,?,?)");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }


    @Override
    public void stop() {
        super.stop();
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Status process() throws EventDeliveryException {
        Status result = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();
        Event event;
        String content;

        List<PermissionLog> infos = Lists.newArrayList();
        transaction.begin();
        //实际的操作时间
//        java.sql.Date opTime=new java.sql.Date(SimpleDT.parse(timePrix.split(" ")[0]+" "+permiLog[0].split("\\.")[0]).getTime());
        //由于是实时的也可以用当时时间来代替
        java.sql.Date opTimeNow=new java.sql.Date(new Date().getTime());
        try {
            for (int i = 0; i < batchSize; i++) {
                event = channel.take();
                if (event != null) {//对事件进行处理
                    //event 的 body 为   "exec tail$i , abel"
                    content = new String(event.getBody());
                    PermissionLog permission=new PermissionLog();
                    InParameter inParameter = new InParameter();
//                    String timePrix = SimpleDT.formatDate(new Date());

                    try {
                        String[] permiLog = content.split(" ", -1);
                        String[] split = permiLog[6].split("#");

                        //存储 event 的 content
                        permission.setDateOperation(opTimeNow);
                        permission.setHttpAndTelnum(permiLog[1].substring(1,permiLog[1].length()-1));
                        permission.setLogType(permiLog[2]);
                        permission.setAPI(permiLog[4]);
                        if(permiLog[2].equals("INFO")){
                            permission.setTraitName(split[2].split(":")[1]);
                            permission.setTraitFeatures(split[1]);
                            if (split[3].contains("出参")){
                                permission.setOutParameter(permiLog[6].split(":")[2]);
                            }else {
                                permission.setInParameter(permiLog[6].split(":")[2]);
                            }
                        }else {
                            permission.setErroReason(content.split("-")[5]);
                        }
                    }catch (Exception e){
                        LOG.error(content);
                        LOG.error(e.toString());
                    }
                    infos.add(permission);
                } else {
                    result = Status.BACKOFF;
                    break;
                }
            }

            if (infos.size() > 0) {
                preparedStatement.clearBatch();
                for (PermissionLog per : infos) {
                    preparedStatement.setDate(1, per.getDateOperation());
                    preparedStatement.setString(2, per.getHttpAndTelnum());
                    preparedStatement.setString(3, per.getLogType());
                    preparedStatement.setString(4, per.getAPI());
                    preparedStatement.setString(5, per.getTraitName());
                    preparedStatement.setString(6, per.getTraitFeatures());
                    preparedStatement.setString(7, per.getInParameter());
                    preparedStatement.setString(8, per.getOutParameter());
                    preparedStatement.setString(9, per.getErroReason());
                    preparedStatement.addBatch();
                }

                preparedStatement.executeBatch();
                conn.commit();
            }
            transaction.commit();
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception e2) {
                LOG.error("Exception in rollback. Rollback might not have been" +
                        "successful.", e2);
            }
            LOG.error("Failed to commit transaction." +
                    "Transaction rolled back.", e);
            Throwables.propagate(e);
        } finally {
            transaction.close();
        }
        return result;
    }


}
