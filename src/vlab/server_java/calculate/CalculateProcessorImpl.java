package vlab.server_java.calculate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rlcp.calculate.CalculatingResult;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.calculate.CalculateProcessor;
import vlab.server_java.model.CalculateUserCode;
import vlab.server_java.model.GenerateCodeResult;
import vlab.server_java.model.GenerateInstructions;
import vlab.server_java.model.UserCodeTests;

import javax.script.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple CalculateProcessor implementation. Supposed to be changed as needed to provide necessary Calculate method support.
 */
public class CalculateProcessorImpl implements CalculateProcessor {
    @Override
    public CalculatingResult calculate(String condition, String instructions, GeneratingResult generatingResult) {
        //do calculate logic here
        String text = null;
        String code = null;
        System.out.println("-------------------DEBUG----------------------");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        System.out.println(condition);
        try {
            CalculateUserCode userCode = gson.fromJson(condition, CalculateUserCode.class);
            GenerateInstructions inst = gson.fromJson(generatingResult.getInstructions(), GenerateInstructions.class);
            List<UserCodeTests> uct = new ArrayList<UserCodeTests>();

            System.setProperty("nashorn.args", "--language=es6");
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
            Invocable invocable = (Invocable) engine;
            engine.eval(userCode.S);
            for(int i = 0; i < inst.testInput.length; i++) {
                System.out.println("=================================================");
                Object funcResult = invocable.invokeFunction("wallKeeper", inst.testInput[i].toString().replaceAll(" ",""));
                uct.add(new UserCodeTests(inst.testInput[i].toString().replaceAll(" ",""),
                        funcResult.toString(),inst.expectedTestOutput[i], funcResult.equals(inst.expectedTestOutput[i])));
                System.out.println(funcResult);
                System.out.println(inst.expectedTestOutput[i]);
                System.out.println(funcResult.equals(inst.expectedTestOutput[i]));
            };
            text = gson.toJson("Результаты проверки кода:");
            System.out.println(text);
            code = gson.toJson(uct);
        } catch (Exception e) {
            text = gson.toJson("Ошибка при выполнении кода:");
            ArrayList list = new ArrayList<>();
            list.add(true);
            list.add(unescapeParam(e.getMessage()));
            code = gson.toJson(list);
            System.out.println(e.getMessage());
        }

        return new CalculatingResult(text, code);
    }

    public static String escapeParam( String param ){
        String res = param.replaceAll( "&", "&amp;" );

        res = res.replaceAll( "<", "&#060;" );
        res = res.replaceAll(">", "&#062;");

        res = res.replaceAll( "\r\n", "<br/>" );
        res = res.replaceAll( "\r", "<br/>" );
        res = res.replaceAll( "\n", "<br/>" );

        res = res.replaceAll( "<", "&lt;" );
        res = res.replaceAll(">", "&gt;");
        res = res.replaceAll("-", "&#0045;");
        res = res.replaceAll( "\"", "&quot;" );
        res = res.replaceAll( "\'", "&apos;" );

        res = res.replace( "\\", "&#92;" );

        String xml10pattern = "[^"
                + "\u0009\r\n"
                + "\u0020-\uD7FF"
                + "\uE000-\uFFFD"
                + "\ud800\udc00-\udbff\udfff"
                + "]";

        res = res.replace( xml10pattern, "" );

        return res;
    }

    /**
     * метод для разэкранирования строки
     * @param param исходная экранированная строка
     * @return разэкранированная строка
     */
    public static String unescapeParam( String param ){
        String res = param.replaceAll( "&amp;", "&" );
        res = res.replaceAll( "&quot;", "\"" );
        res = res.replaceAll( "&lt;br/&gt;", "\r\n");
        res = res.replaceAll( "&lt;", "<" );
        res = res.replaceAll( "&gt;", ">" );
        res = res.replaceAll( "&#0045;", "-" );
        res = res.replaceAll( "&apos;", "\'" );

        res = res.replace("&minus;", "-");
        res = res.replace( "&#92;", "\\" );

        res = res.replaceAll( "<br/>", "\r\n");

        return res;
    }
}
