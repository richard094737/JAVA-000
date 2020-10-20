import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MyClassLoader extends ClassLoader {


    private byte[] file2Bytes(String filePath) {

        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(new File(filePath));
            bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1];
            byte[] restoreBytes = new byte[1];
            byte b255 = (byte) 255;
            int n;
            while ((n = fis.read(b)) != -1) {
                restoreBytes[0] = (byte) (b255 - b[0]);

                bos.write(restoreBytes, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }


    protected Class<?> findClass(String name, String loadClassFilePath) {
        byte[] bytes = file2Bytes(loadClassFilePath);
        return defineClass(name, bytes, 0, bytes.length);
    }

    public static void main(String[] args) throws Exception {
        String loadClassFilePath = MyClassLoader.class.getResource("Hello.xlass").getPath();
        System.out.println(loadClassFilePath);
        try {
            Class<?> clz = new MyClassLoader().findClass("Hello", loadClassFilePath);

            Method hello = clz.getDeclaredMethod("hello");
            hello.setAccessible(true);
            hello.invoke(clz.newInstance());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
