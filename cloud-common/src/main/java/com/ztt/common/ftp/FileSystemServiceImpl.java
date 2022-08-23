package com.ztt.common.ftp;


import com.jcraft.jsch.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * @author jason.tang
 * @create 2019-03-07 13:33
 * @description
 */
@Service("fileSystemService")
public class FileSystemServiceImpl implements FileSystemService {

    @Autowired
    private SftpProperties config;

    // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
    private static final String SESSION_CONFIG_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";

    public static final String PATH_5G = "/chunk1/ccsftp/ltenoc/chunk1/ccsftp/5gnoc_test/";

    public static final String PATH_LTE = "/chunk1/ccsftp/ltenoc/";

    /**
     * 创建SFTP连接
     *
     * @return
     * @throws Exception
     */
    private ChannelSftp createSftp() throws Exception {
        JSch jsch = new JSch();
        System.out.println("Try to connect sftp[" + config.getUsername() + "@" + config.getHost() + "], use password[" + config.getPassword() + "]");

        Session session = createSession(jsch, config.getHost(), config.getUsername(), config.getPort());
        session.setPassword(config.getPassword());
        session.connect();

        System.out.println("Session connected to {}." + config.getHost());

        Channel channel = session.openChannel(config.getProtocol());
        channel.connect(config.getChannelConnectedTimeout());

        System.out.println("Channel created to {}." + config.getHost());

        return (ChannelSftp) channel;
    }

    /**
     * 加密秘钥方式登陆
     *
     * @return
     */
    private ChannelSftp connectByKey() throws Exception {
        JSch jsch = new JSch();

        // 设置密钥和密码 ,支持密钥的方式登陆
        if (StringUtils.isNotBlank(config.getPrivateKey())) {
            if (StringUtils.isNotBlank(config.getPassphrase())) {
                // 设置带口令的密钥
                jsch.addIdentity(config.getPrivateKey(), config.getPassphrase());
            } else {
                // 设置不带口令的密钥
                jsch.addIdentity(config.getPrivateKey());
            }
        }
        System.out.println("Try to connect sftp[" + config.getUsername() + "@" + config.getHost() + "], use private key[" + config.getPrivateKey()
                + "] with passphrase[" + config.getPassphrase() + "]");

        Session session = createSession(jsch, config.getHost(), config.getUsername(), config.getPort());
        // 设置登陆超时时间
        session.connect(config.getSessionConnectTimeout());
        System.out.println("Session connected to " + config.getHost() + ".");

        // 创建sftp通信通道
        Channel channel = session.openChannel(config.getProtocol());
        channel.connect(config.getChannelConnectedTimeout());
        System.out.println("Channel created to " + config.getHost() + ".");
        return (ChannelSftp) channel;
    }

    /**
     * 上传文件
     *
     * @param targetPath
     * @param inputStream
     * @return
     * @throws Exception
     */
    @Override
    public boolean uploadFile(String targetPath, InputStream inputStream) throws Exception {
        ChannelSftp sftp = this.createSftp();
        try {
            sftp.cd(config.getRoot());
            System.out.println("Change path to {}" + config.getRoot());

            int index = targetPath.lastIndexOf("/");
            String fileDir = targetPath.substring(0, index);
            String fileName = targetPath.substring(index + 1);
            boolean dirs = this.createDirs(fileDir, sftp);
            if (!dirs) {
                System.out.println("Remote path error. path:{}" + targetPath);
                throw new Exception("Upload File failure");
            }
            sftp.put(inputStream, fileName);
            return true;
        } catch (Exception e) {
            System.out.println("Upload file failure. TargetPath: {}" + targetPath);
            throw new Exception("Upload File failure");
        } finally {
            this.disconnect(sftp);
        }
    }

    /**
     * 上传文件
     *
     * @param targetPath
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public boolean uploadFile(String targetPath, File file) throws Exception {
        return this.uploadFile(targetPath, new FileInputStream(file));
    }

    /**
     * 下载文件
     *
     * @param targetPath
     * @return
     * @throws Exception
     */
    @Override
    public File downloadFile(String targetPath, String fileName) throws Exception {
        ChannelSftp sftp = this.createSftp();
        OutputStream outputStream = null;
        try {
            // 切换到根目录
            sftp.cd(config.getRoot());
            System.out.println("Change path to {}" + config.getRoot());
            String localPath = System.getProperty("user.dir") + File.separatorChar + "tmp";
            File localFile = createLocalFile(localPath, fileName);
            outputStream = new FileOutputStream(localFile);
            // 下载文件必须是路径加名称
            sftp.get(targetPath + fileName, outputStream);
            System.out.println("Download file success. TargetPath: {}" + targetPath);
            return localFile;
            // return localFile;
        } catch (Exception e) {
            System.out.println("Download file failure. TargetPath: {}" + targetPath);
            return null;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            this.disconnect(sftp);
        }
    }

    /**
     * 只获取比上次同步时间更新的数据
     *
     * @param dirPath 目录 /chunk1/ccsftp/ltenoc/chunk1/ccsftp/5gnoc_test/
     * @param data    上一次文件目录的时间 20200820
     * @return
     * @throws Exception
     */
    @SuppressWarnings("all")
    public String[] listNames(String dirPath, String data) throws Exception {
        ChannelSftp sftp = this.createSftp();
        sftp.cd(config.getRoot());
        ArrayList<String> list = new ArrayList<>();
        Vector<ChannelSftp.LsEntry> ls = sftp.ls(dirPath);
        ls.forEach(value -> {
            String filename = value.getFilename();
            String[] strings = filename.split("_");
            // 获取到文件名称里面的时间
            String replace = strings[strings.length - 1].replace(".txt", "");
            if (StringUtils.isNumeric(data) && StringUtils.isNumeric(replace)) {
                if (filename.endsWith(".txt") && Integer.parseInt(replace) > Integer.parseInt(data)) {
                    list.add(value.getFilename());
                }
            }
        });
        String[] strings = new String[list.size()];
        return list.toArray(strings);
    }

    /**
     * 下载文件
     *
     * @param targetPath
     * @return
     * @throws Exception
     */
    @Override
    public InputStream downloadFileInputStream(String targetPath) throws Exception {
        ChannelSftp sftp = this.createSftp();
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            sftp.cd(config.getRoot());
            System.out.println("Change path to {}" + config.getRoot());

            File file = new File(targetPath.substring(targetPath.lastIndexOf("/") + 1));

            inputStream = sftp.get(targetPath);
            System.out.println("Download file success. TargetPath: {}" + targetPath);
            return inputStream;
        } catch (Exception e) {
            System.out.println("Download file failure. TargetPath: {}" + targetPath);
            throw new Exception("Download File failure");
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            this.disconnect(sftp);
        }
    }

    /**
     * 删除文件
     *
     * @param targetPath
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteFile(String targetPath) throws Exception {
        ChannelSftp sftp = null;
        try {
            sftp = this.createSftp();
            sftp.cd(config.getRoot());
            sftp.rm(targetPath);
            return true;
        } catch (Exception e) {
            System.out.println("Delete file failure. TargetPath: {}" + targetPath);
            throw new Exception("Delete File failure");
        } finally {
            this.disconnect(sftp);
        }
    }

    /**
     * 新建本地文件
     *
     * @param path
     * @param fileName
     * @return
     */
    public File createLocalFile(String path, String fileName) {
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdir();
        }

        File file = new File(filePath, fileName);
//        file.deleteOnExit();
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {

            e.printStackTrace();
        }
        System.err.println(file.getAbsolutePath());
        return file;
    }

    /**
     * 创建一级或者多级目录
     *
     * @param dirPath
     * @param sftp
     * @return
     */
    private boolean createDirs(String dirPath, ChannelSftp sftp) {
        if (dirPath != null && !dirPath.isEmpty()
                && sftp != null) {
            String[] dirs = Arrays.stream(dirPath.split("/"))
                    .filter(StringUtils::isNotBlank)
                    .toArray(String[]::new);

            for (String dir : dirs) {
                try {
                    sftp.cd(dir);
                    System.out.println("Change directory {}" + dir);
                } catch (Exception e) {
                    try {
                        sftp.mkdir(dir);
                        System.out.println("Create directory {}" + dir);
                    } catch (SftpException e1) {
                        System.out.println("Create directory failure, directory:{}" + dir);
                        e1.printStackTrace();
                    }
                    try {
                        sftp.cd(dir);
                        System.out.println("Change directory {}" + dir);
                    } catch (SftpException e1) {
                        System.out.println("Change directory failure, directory:{}" + dir);
                        e1.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 创建session
     *
     * @param jsch
     * @param host
     * @param username
     * @param port
     * @return
     * @throws Exception
     */
    private Session createSession(JSch jsch, String host, String username, Integer port) throws Exception {
        Session session = null;

        if (port <= 0) {
            session = jsch.getSession(username, host);
        } else {
            session = jsch.getSession(username, host, port);
        }

        if (session == null) {
            throw new Exception(host + " session is null");
        }

        session.setConfig(SESSION_CONFIG_STRICT_HOST_KEY_CHECKING, config.getSessionStrictHostKeyChecking());
        return session;
    }

    /**
     * 关闭连接
     *
     * @param sftp
     */
    private void disconnect(ChannelSftp sftp) {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                } else if (sftp.isClosed()) {
                    System.out.println("sftp is closed already");
                }
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}