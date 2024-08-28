package com.nom.graalnom;

import org.graalvm.polyglot.Context;
import org.json.JSONObject;

import java.io.IOException;


public final class NomMain {
    public static void main(String[] args) throws IOException {
        System.out.println("== running on " + System.getProperty("java.version"));
        Context context = Context.newBuilder(NomLanguage.ID)
                .in(System.in).out(System.out).allowAllAccess(true).build();
        System.out.println("== running on " + context.getEngine());
        String manifest = args[0];
        String mainClass = args[1];
        int testCount = args.length > 2 ? Integer.parseInt(args[2]) : 5;
        for (int i = 0; i < testCount; i++) {
            System.out.println("== iteration " + i);
            context.eval(NomLanguage.ID,
                    GetTestString(manifest, mainClass, true, false, false));
        }

//        //find all subfolders in args[0] that starts with ".BM_"
//        String[] subFolders = new java.io.File(args[0])
//                .list((current, name) ->
//                        new java.io.File(current, name).isDirectory() && name.startsWith(".BM_")
//                                && (!name.contains("L")
//                                && !name.contains("K") && !name.contains("S"))
//                );
//        assert subFolders != null;
//        int testCount = args.length > 2 ? Integer.parseInt(args[2]) : 5;
//        for (int j = 0, subFoldersLength = subFolders.length; j < subFoldersLength; j++) {
//            String f = subFolders[j];
//            System.out.println("Testing " + f + " (" + (j + 1) + "/" + subFoldersLength + ")");
//            for (int i = 0; i < testCount; i++) {
//                System.out.println("== iteration " + i);
//                context.eval(NomLanguage.ID,
//                        GetTestString(Path.of(args[0], f, "Program.manifest").toAbsolutePath().toString(), args[1], true, false, false));
//            }
//        }
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
