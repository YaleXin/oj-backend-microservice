package top.yalexin.ojbackendcodesandbox.utils;

import org.springframework.util.StopWatch;
import top.yalexin.backendmodel.model.codesandbox.ExecuteMessage;


import java.io.*;

public class ProcessUtils {
    public static ExecuteMessage runAndGetOutput(Process process) throws Exception {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            int waitFor = process.waitFor();
            executeMessage.setExitCode(waitFor);
            // 正常退出
            if (waitFor == 0) {
                // 按行读取进程的输出信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                executeMessage.setMessage(stringBuffer.toString());
            } else {
                // 异常退出
                // 按行读取进程的输出信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                executeMessage.setMessage(stringBuffer.toString());
                // 收集错误信息
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                StringBuffer errorStringBuffer = new StringBuffer();
                while ((errorLine = errorBufferedReader.readLine()) != null) {
                    errorStringBuffer.append(errorLine);
                }
                executeMessage.setErrorMessage(errorStringBuffer.toString());
            }
        } catch (IOException | InterruptedException e) {
            throw new Exception(e);
        }
        return executeMessage;
    }


    public static ExecuteMessage runInterAndGetOutput(Process process, String args) throws Exception {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            InputStream inputStream = process.getInputStream();
            OutputStream outputStream = process.getOutputStream();
            InputStream errorStream = process.getErrorStream();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(args + "\n");
            outputStreamWriter.flush();
            // 从输入流中获取正常信息
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            executeMessage.setMessage(stringBuffer.toString());

            // 收集错误信息
            BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(errorStream));
            String errorLine;
            StringBuffer errorStringBuffer = new StringBuffer();
            while ((errorLine = errorBufferedReader.readLine()) != null) {
                errorStringBuffer.append(errorLine);
            }
            executeMessage.setErrorMessage(errorStringBuffer.toString());

            int waitFor = process.waitFor();
            executeMessage.setExitCode(waitFor);
            // 资源回收
            outputStreamWriter.close();
            inputStream.close();
            outputStream.close();
            errorBufferedReader.close();
            stopWatch.stop();
            long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
            executeMessage.setTimeCost(lastTaskTimeMillis);
        } catch (IOException e) {
            throw new Exception(e);
        }
        return executeMessage;
    }

}
