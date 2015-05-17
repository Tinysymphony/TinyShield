package com.example.TinyShield;


        import java.io.File;
        import java.util.ArrayList;

/**
 * Created by LK on 2015/5/15.
 */
public class APKScan {


    public static void getFiles(String path, ArrayList<String> apkPath, ArrayList<String> MD5List) {
        // TODO Auto-generated method stub

        File file = new File(path);
        File[] files = file.listFiles();
        String MD5 = null;


        for (int j = 0; j < files.length; j++) {
            String name = files[j].getName();
            if (files[j].isDirectory()) {
                String dirPath = files[j].toString();
                // System.out.println(dirPath);
                getFiles(dirPath + "/", apkPath, MD5List);
            } else if (files[j].isFile() & name.endsWith(".apk")) {
                apkPath.add(files[j].toString());
                try {
                    MD5 = MD5get.getFileMD5String(files[j]);
                    MD5List.add(MD5);
                }catch(Exception e){

                    System.out.println("A error occur while getting MD5 from files");
                }
                System.out.println("FileName ==" + files[j]);
                if(MD5 != null) {
                    System.out.println("MD5: " + MD5);
                }
            }
        }
    }

}

