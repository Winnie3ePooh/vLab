package vlab.server_java.generate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rlcp.generate.GeneratingResult;
import rlcp.server.processor.generate.GenerateProcessor;
import vlab.server_java.model.GenerateCodeResult;

import javax.script.*;
import javax.script.ScriptEngine;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import vlab.server_java.model.GenerateInstructions;

/**
 * Simple GenerateProcessor implementation. Supposed to be changed as needed to
 * provide necessary Generate method support.
 */
public class GenerateProcessorImpl implements GenerateProcessor {
    @Override
    public GeneratingResult generate(String condition) {
        //do Generate logic here
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        String text = "Вариант загружен";
        String code = null;
        String instructions = null;
        GenerateInstructions inst = new GenerateInstructions();
        inst.testInput = new String[]{"5, 7, 13, 14, 18",
                "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25"};
        inst.expectedTestOutput = new String[]{"10,12,14,15,16,17,18,19,20,25,4,5,2,5,4,6,7,8,14,16,17,19,20,24",
        "6,7,8,9,10,11,15,16,17,19,20,22,23,24,4,5,8,12,13,15,16,18,20,21,22"};
        String taskText = "Добро пожаловать в GridLand. Все граждане здесь подключены через глобальную внутреннюю " +
                "сеть, потому что основным способом общения является электронная почта. Строительство нового района" +
                " всегда начинается с возведения вышки - районного центра. Все жители подключены к этой вышке" +
                ", чтобы иметь возможность отправлять и получать электронные письма. Все вышки GridLand подключены" +
                ", поэтому одна вышка может отправлять электронные письма между связанными вышками. Таким образом" +
                ", независимо от того, насколько велик город, все пользователи могут отправлять сообщения друг" +
                " другу до того момента, пока все вышки подключены.\n\nМэр GridLand использует эту сеть, чтобы в" +
                " случае необходимости быстро отправлять экстренные сообщения всем гражданам. Но система " +
                "не идеальна. В случае разрушения одной из вышек, все жители района, где она была возведена" +
                ", могут остаться отключенными от этих экстренных емейл сообщений. " +
                "Данное происшествие также может быть причиной повреждения связи в смежных районах, если их вышки" +
                " не имеют других способов подключения. Чтобы разрешить данную проблему мэр использует почтовых" +
                " голубей - старый метод отправки почты, который был изобретен до глобальной внутренней сети." +
                " Граждане, которые все еще подключены к сети, получают экстренные сообщения по электронной почте," +
                " но отключенные граждане получают их от этих голубей.\n\nВаша миссия - выяснить, сколько " +
                "нужно голубей, если некоторые из вышек разрушены.";
        String inputDataText = "\n\nВходные данные: Четыре параметра: " +
                "структура сети (в виде списка соединений между вышками), пользователи на каждой вышке " +
                "(в виде dict, где ключи - имя вышки, а значения - количество пользователей), " +
                "название вышки, отправляющей электронную почту, и список разрушенных вышек.\n";
        String outputDataText = "Выходные данные: Int. Количество пользователей, " +
                "которые не получат экстренные сообщения.";
        try {
//            String script = "function LightOnOrOff(p, i, j) {"+
//                    "  let adj = [[i, j - 1], [i - 1, j], [i, j], [i + 1, j], [i, j + 1]];"+
//                    "  adj.forEach(e => {"+
//                    "    if (p[e[0]] && p[e[0]][e[1]] !== undefined)"+
//                    "      p[e[0]][e[1]] = 1 - p[e[0]][e[1]];"+
//                    "  });"+
//                    "}"+
//                    "function wallKeeper(on_panels) {"+
//                    "  on_panels = on_panels.split(',').map(Number);"+
//                    "  let p = [];"+
//                    "  let answer = [];"+
//                    "  for (let i = 0; i < 5; i += 1) {"+
//                    "    let row = [];"+
//                    "    for (let j = 0; j < 5; j += 1)"+
//                    "      row.push(on_panels.indexOf(i * 5 + j + 1) > -1 ? 1 : 0);"+
//                    "    p.push(row);"+
//                    "  }"+
//                    "  while (p.some(row => row.some(e => e === 1))) {"+
//                    "    for (let i = 0; i < 4; i += 1) {"+
//                    "      let index = -1;"+
//                    "      while ((index = p[i].indexOf(1)) >= 0) {"+
//                    "        LightOnOrOff(p, i + 1, index);"+
//                    "        answer.push((i + 1) * 5 + index + 1);"+
//                    "      }"+
//                    "    }"+
//                    "    if (p[4][0] === 1) {"+
//                    "      LightOnOrOff(p, 0, 3);"+
//                    "      LightOnOrOff(p, 0, 4);"+
//                    "      answer.push(0 * 5 + 3 + 1);"+
//                    "      answer.push(0 * 5 + 4 + 1);"+
//                    "    }"+
//                    "    if (p[4][1] === 1) {"+
//                    "      LightOnOrOff(p, 0, 1);"+
//                    "      LightOnOrOff(p, 0, 4);"+
//                    "      answer.push(0 * 5 + 1 + 1);"+
//                    "      answer.push(0 * 5 + 4 + 1);"+
//                    "    }"+
//                    "    if (p[4][2] === 1) {"+
//                    "      LightOnOrOff(p, 0, 3);"+
//                    "      answer.push(0 * 5 + 3 + 1);"+
//                    "    }"+
//                    "  }"+
//                    "  return answer.join(',');"+
//                    "};";
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            instructions = gson.toJson(inst);
            text = "OK";
            code = gson.toJson(new GenerateCodeResult(taskText,
                    inputDataText,
                    outputDataText));
        } catch (Exception e) {
            text = "Ne ok";
        }
        return new GeneratingResult(text, code, instructions);
    }
}
