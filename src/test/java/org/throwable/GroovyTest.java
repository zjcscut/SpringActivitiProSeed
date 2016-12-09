package org.throwable;

import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import org.junit.Test;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author zhangjinci
 * @version 2016/12/9 9:39
 * @function
 */
public class GroovyTest {

    @Test
    public void Test1() throws Exception {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        //每次生成一个engine实例
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("groovy");
        assertNotNull(scriptEngine);
        System.out.println("scriptEngine = " + scriptEngine.toString());
        //javax.script.Bindings
        Bindings bindings = scriptEngine.createBindings();
        bindings.put("date", new Date());
        bindings.put("name", "zjc");
        //如果script文本来自文件,请首先获取文件内容
        Object timeScriRe = scriptEngine.eval("def getTime(){return date.getTime();}", bindings);
        System.out.println("timeScriRe = " + timeScriRe);
        Long time = (Long) ((Invocable) scriptEngine).invokeFunction("getTime");
        System.out.println("time = " + time);
        String msgScriptRe = (String) scriptEngine.eval("def sayHello(name,age){return 'Hello,I am ' + name + ',age' + age;}");
        System.out.println("msgScriptRe = " + msgScriptRe);
        String message = (String) ((Invocable) scriptEngine).invokeFunction("sayHello", "zhangsan", 12);
        System.out.println("message = " + message);
    }

    @Test
    public void Test2() throws Exception {
        String[] root = new String[] { "src/main/org/throwable/groovy/" };
        GroovyScriptEngine engine = new GroovyScriptEngine(root);
        GroovyObject groovyObject =
                (GroovyObject) engine.loadScriptByName("/TestScript.groovy").newInstance();
        String re = (String) groovyObject.invokeMethod("output","zjc");
        System.out.println(re);
    }
}
