package com.wangrui.test;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.FileSystemFile;

import java.io.File;
import java.io.IOException;

public class SFTPUpload {

    public static void main(String[] args)
            throws IOException {
        SSHClient ssh = new SSHClient();
        ssh.loadKnownHosts();
        ssh.connect("168.100.101.51");
        
        try {
            //ssh.authPublickey(System.getProperty("user.name"));
            ssh.authPassword("hundsun", "hundsun");
            //final Session session = ssh.startSession();
            
            final String src = System.getProperty("user.home") + File.separator + "test_file";
            final SFTPClient sftp = ssh.newSFTPClient();
            try {
                sftp.put(new FileSystemFile(src), "/tmp");
            } finally {
                sftp.close();
            }
        } finally {
            ssh.disconnect();
        }
    }

}
