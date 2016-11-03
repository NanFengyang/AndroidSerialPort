#串口通讯应用 


## 1 串口驱动Java层接口
串口驱动Java层接口调用了以下两个native方法：
- private native static FileDescriptor open(String path, int baudrate, int flags);
- public native void close();

java 提供了以下两个接口给上层应用使用：
- public InputStream getInputStream() 
- public OutputStream getOutputStream()

### 1.2 串口驱动
串口驱动在文件SerialPort.c中定义。
驱动程序提供了打开设备， 设置设备， 关闭设备的功能。

## 2 apk签名文件
由于访问串口， 程序必须有系统权限。 
编译有系统权限的apk需要提供android系统平台的密钥。密钥文件存储在build/target/product/security/下面的platform.pk8和platform.x509.pem。
android studio给apk签名时需要key store文件， 所以需要根据密钥文件产生key store文件。
为了编译方便， 在git源码的app/key_ah100路径下保存了key store文件： yytkeysystem.jks. 密码为123456.
查考生成系统签名文章：http://blog.csdn.net/zhixuan322145/article/details/51277921 -非常感谢作者。
资源文件见根目录下:single.zip

PC端测试工具可以使用:Serial Port Utility.见百度

联系QQ：2511575099.
