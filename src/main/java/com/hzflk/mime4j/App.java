package com.hzflk.mime4j;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println(MediaUtils.getMimeType("/Users/free6om/Downloads/hello.txt"));
        System.out.println(MediaUtils.getMimeType("/Users/free6om/Downloads/产品送检技术资料要求.doc"));
        System.out.println(MediaUtils.getMimeType("/Users/free6om/Downloads/王小波全集-bbc2.mobi"));
        System.out.println(MediaUtils.getMimeType("/Users/free6om/Downloads/how android application work.pdf"));
        System.out.println(MediaUtils.getMimeType("/Users/free6om/Downloads/drjava-stable-20140826-r5761.jar"));
        System.out.println(MediaUtils.getMimeType("/Users/free6om/Downloads/commons-codec-1.10-bin.tar.gz"));
        System.out.println(MediaUtils.getMimeType("/Users/free6om/Downloads/[www.poxiao.com破晓电影]大喜临门HD中英双字.rmvb"));
    }
}
