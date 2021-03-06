package org.hope6537.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapred.JobConf;
import org.hope6537.hadoop.ConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class HdfsUtils {

    public static final String HADOOP2MASTER_DIR = "hdfs://hadoop2master:9000";
    public static final String HADOOP_NAMESERVICE_DIR = "hdfs://ns1";
    public static final String JICHUANG_MASTER_DIR = "hdfs://jichuang:9000";
    public Logger logger;
    private FileSystem fileSystem;
    private String username;

    private String hdfsDir;
    private Configuration configuration;

    public HdfsUtils(String hdfsDir, Configuration configuration, String username) {
        this.hdfsDir = hdfsDir;
        this.configuration = configuration;
        this.username = username;
        this.logger = LoggerFactory.getLogger(getClass());
        try {
            this.fileSystem = FileSystem.get(URI.create(hdfsDir), configuration, username);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public HdfsUtils(Configuration configuration) {

        this(HADOOP_NAMESERVICE_DIR, configuration);
    }

    public HdfsUtils() {

        this(HADOOP_NAMESERVICE_DIR, ConfigurationFactory.getConfiguration());
    }

    public HdfsUtils(String hdfsDir, Configuration configuration) {
        this(hdfsDir, configuration, "hope6537");
    }

    public static HdfsUtils getInstanceOfDistributed() {
        return new HdfsUtils(HADOOP_NAMESERVICE_DIR, ConfigurationFactory.getConfiguration());
    }

    public static HdfsUtils getInstanceOfPseudoDistributed(Configuration configuration) {
        return new HdfsUtils(HADOOP2MASTER_DIR, configuration);
    }

    public static HdfsUtils getInstanceOfJiChuang(Configuration configuration) {
        return new HdfsUtils(JICHUANG_MASTER_DIR, configuration);
    }


    public static JobConf config() {
        //设置Configuration配置,获取classpath下的配置文件
        JobConf conf = new JobConf(HdfsUtils.class);
        conf.setJobName("HdfsUtils");
        conf.addResource("classpath:/hadoop/core-site.xml");
        conf.addResource("classpath:/hadoop/hdfs-site.xml");
        conf.addResource("classpath:/hadoop/mapred-site.xml");
        return conf;
    }

    public FileSystem getFileSystem() {
        try {
            fileSystem.getStatus();
        } catch (Exception e) {
            System.out.println("restart fileSystem");
            logger.warn("restart fileSystem");
            try {
                this.fileSystem = null;
                this.fileSystem = FileSystem.get(URI.create(hdfsDir), configuration, username);
            } catch (Exception e1) {
                logger.error("create fileSystem error");
            }
        }
        return fileSystem;
    }

    /**
     * 获取文件信息对象，收到后只需要迭代取数据即可
     */
    public FileStatus[] ls(String folder) throws IOException {
        Path path = new Path(folder);
        FileSystem fs = getFileSystem();
        FileStatus[] list = fs.listStatus(path);
        return list;
    }

    public boolean mkdirs(String folder) {
        Path path = new Path(folder);
        FileSystem fs = getFileSystem();
        try {
            if (!fs.exists(path)) {
                fs.mkdirs(path);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("make directory error");
            return false;
        }
    }

    public boolean rename(String file, String newName) {
        Path path = new Path(file);
        Path newNamePath = new Path(newName);
        FileSystem fs = getFileSystem();
        try {
            fs.rename(path, newNamePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("make directory error");
            return false;
        }
    }

    public boolean rmr(String folder) {
        Path path = new Path(folder);
        FileSystem fs = getFileSystem();
        try {
            fs.delete(path, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("delete file error");
            return false;
        }
    }

    public boolean put(String local, String remote) {
        try {
            FileSystem fs = getFileSystem();
            fs.copyFromLocalFile(new Path(local), new Path(remote));
            logger.info("upload file " + local + " to " + remote);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("put file error");
            return false;
        }
    }

    public boolean put(InputStream local, String remote) {
        try {
            FileSystem fs = getFileSystem();
            OutputStream out = fs.create(new Path(remote), false);
            IOUtils.copyBytes(local, out, configuration, true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("put file error");
            return false;
        }
    }

    public OutputStream getHdfsOutPutStream(String path) {
        try {
            FileSystem fs = getFileSystem();
            OutputStream out = fs.create(new Path(path), false);
            return out;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream getHdfsInputStream(String path) {
        try {
            FileSystem fs = getFileSystem();
            InputStream in = fs.open(new Path(path));
            return in;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean get(String remote, String local) throws IOException {
        try {
            FileSystem fs = getFileSystem();
            fs.copyToLocalFile(new Path(remote), new Path(local));
            logger.info("download file " + remote + " from " + local);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("get file error");
            return false;
        }
    }

    public InputStream cat(String remoteFile) throws IOException {
        Path path = new Path(remoteFile);
        FSDataInputStream fsdis = null;
        FileSystem fs = getFileSystem();
        logger.info("cat: " + remoteFile);
        try {
            fsdis = fs.open(path);
            return fsdis;
        } finally {
            IOUtils.closeStream(fsdis);
        }
    }

    public void cat(String remoteFile, OutputStream out) throws IOException {
        InputStream fsdis = null;
        try {
            fsdis = cat(remoteFile);
            IOUtils.copyBytes(fsdis, out, 4096, false);
        } finally {
            IOUtils.closeStream(fsdis);
        }
    }

    public void catInConsole(String remoteFile) throws IOException {
        this.cat(remoteFile, System.out);

    }

    public void renameShowInConsole(String file, String newName) {
        System.out.println("File: " + file + "->" + newName);
        if (this.rename(file, newName)) {
            System.out.println("rename success!");
        } else {
            System.out.println("rename failed!");
        }
    }


    public void rmrShowInConsole(String folder) {
        boolean result = this.rmr(folder);
        if (result) {
            System.out.println("Delete: " + folder + " Success!");
        } else {
            System.out.println("Delete: " + folder + "Failed!");
        }
    }


    public void mkdirsShowInConsole(String folder) {
        boolean result = this.mkdirs(folder);
        if (result) {
            System.out.println("Create: " + folder + " Success!");
        } else {
            System.out.println("Create: " + folder + "Failed!");
        }
    }

    /**
     * 在控制台显示ls效果
     */
    public void lsShowInConsole(String folder) throws IOException {
        FileStatus[] list = this.ls(folder);
        System.out.println("the path of: " + folder);
        System.out.println("==========================================================");
        for (FileStatus f : list) {
            System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath(), f.isDirectory(), f.getLen());
        }
        System.out.println("==========================================================");
    }

    public void closeFileSystem() {
        try {
            this.fileSystem.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
