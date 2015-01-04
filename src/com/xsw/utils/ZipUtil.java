package com.xsw.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @author lyodssoft.com
 * 
 * @creator Kevin
 * @version 1.0.0
 * @date 2014-10-10
 * @description 文件压缩类 - 创建
 *
 */

public class ZipUtil {
    /**
     * 将Object对象转换为压缩后的Base64编码字符串
     * 
     * @param obj 对象必须实现Serializable接口
     * @return 压缩编码后的数据
     */
    public static String gzipobj(Object obj) {
        ByteArrayOutputStream tmpbaos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(tmpbaos);
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gzip(Util.bytesToHexString(tmpbaos.toByteArray()));
    }

    /**
     * 将压缩编码后的obj字符串对象，反传为原对象
     * 
     * @param str 将压缩编码后的obj字符串
     * @return Object 原对象
     */
    public static Object gunzipobj(String str) {
        String objstr = gunzip(str);
        ByteArrayInputStream bais = new ByteArrayInputStream(Util.hexStringToBytes(objstr));
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将Object对象转换为压缩后的Base64编码字符串
     * 
     * @param obj 对象必须实现Serializable接口
     * @return 压缩编码后的数据
     */
    public static String zipobj(Object obj) {
        ByteArrayOutputStream tmpbaos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(tmpbaos);
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zip(Util.bytesToHexString(tmpbaos.toByteArray()));
    }

    /**
     * 将压缩编码后的obj字符串对象，反传为原对象
     * 
     * @param str 将压缩编码后的obj字符串
     * @return Object 原对象
     */
    public static Object unzipobj(String str) {
        String objstr = unzip(str);
        ByteArrayInputStream bais = new ByteArrayInputStream(Util.hexStringToBytes(objstr));
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用gzip进行压缩
     */
    public static String gzip(String primStr) {
        if (primStr == null || primStr.length() == 0) {
            return primStr;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(primStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new sun.misc.BASE64Encoder().encode(out.toByteArray()).replaceAll("\r\n", "").replace("=", "");
    }

    /**
     * 
     * <p>
     * Description:使用gzip进行解压缩
     * </p>
     * 
     * @param compressedStr
     * @return
     */
    public static String gunzip(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try {
            compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return decompressed;
    }

    /**
     * 使用zip进行压缩
     * 
     * @param str 压缩前的文本
     * @return 返回压缩后的文本
     */
    public static final String zip(String str) {
        if (str == null) {
            return null;
        }
        byte[] compressed;
        ByteArrayOutputStream out = null;
        ZipOutputStream zout = null;
        String compressedStr = null;
        try {
            out = new ByteArrayOutputStream();
            zout = new ZipOutputStream(out);
            zout.putNextEntry(new ZipEntry("0"));
            zout.write(str.getBytes());
            zout.closeEntry();
            compressed = out.toByteArray();
            compressedStr = new sun.misc.BASE64Encoder().encodeBuffer(compressed);
        } catch (IOException e) {
            compressed = null;
            e.printStackTrace();
        } finally {
            if (zout != null) {
                try {
                    zout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return compressedStr.replaceAll("\r\n", "").replace("=", "");
    }

    /**
     * 使用zip进行解压缩
     * 
     * @param compressed 压缩后的文本
     * @return 解压后的字符串
     */
    public static final String unzip(String compressedStr) {
        if (compressedStr == null) {
            return null;
        }
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        ZipInputStream zin = null;
        String decompressed = null;
        try {
            byte[] compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(compressed);
            zin = new ZipInputStream(in);
            zin.getNextEntry();
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = zin.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            decompressed = null;
            e.printStackTrace();
        } finally {
            if (zin != null) {
                try {
                    zin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return decompressed;
    }
}
