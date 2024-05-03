package com.nom.graalnom;

import org.graalvm.polyglot.Context;
import org.json.JSONObject;


public final class NomMain {
    public static void main(String[] args) {
        try (Context context = Context.newBuilder(NomLanguage.ID)
                .in(System.in).out(System.out).allowAllAccess(true).build()) {
            int i = 0;
            System.out.println("== running on " + context.getEngine());
            while (true) {
                System.in.read();
                System.out.println("== iteration " + i++);
                context.eval(NomLanguage.ID,
                        GetTestString(args[0], args[1], true, false, false));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } finally {
            System.exit(0);
        }
    }


    private static String GetTestString(String manifestPath, String mainClassName, boolean invokeMain,
                                        boolean debug, boolean ignoreErrorBytecode) {
        JSONObject jo = new JSONObject();
        jo.put("manifestPath", manifestPath);
        jo.put("mainClass", mainClassName);
        jo.put("invokeMain", invokeMain);
        jo.put("debug", debug);
        jo.put("ignoreErrorBytecode", ignoreErrorBytecode);
        return jo.toString(1);
    }
}
