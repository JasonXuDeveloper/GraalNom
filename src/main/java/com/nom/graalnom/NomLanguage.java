package com.nom.graalnom;

import com.nom.graalnom.parser.ByteCodeReader;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.builtins.NomBuiltinNode;
import com.nom.graalnom.runtime.constants.NomSuperClassConstant;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.datatypes.NomObject;
import com.nom.graalnom.runtime.datatypes.NomTimer;
import com.nom.graalnom.runtime.nodes.NomRootNode;
import com.nom.graalnom.runtime.nodes.NomStatementNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomBasicBlockNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomFunctionBodyNode;
import com.nom.graalnom.runtime.nodes.controlflow.NomReturnNode;
import com.nom.graalnom.runtime.nodes.expression.NomExpressionNode;
import com.nom.graalnom.runtime.nodes.expression.NomInvokeNode;
import com.nom.graalnom.runtime.nodes.local.NomReadArgumentNode;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.nom.graalnom.runtime.reflections.NomInterface;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.object.Shape;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@TruffleLanguage.Registration(id = NomLanguage.ID, name = "GraalNom",
        defaultMimeType = NomLanguage.MIME_TYPE,
        characterMimeTypes = NomLanguage.MIME_TYPE,
        contextPolicy = TruffleLanguage.ContextPolicy.SHARED,
        fileTypeDetectors = NomFileDetector.class)
@ProvidedTags({StandardTags.CallTag.class, StandardTags.StatementTag.class, StandardTags.RootTag.class, StandardTags.RootBodyTag.class, StandardTags.ExpressionTag.class, DebuggerTags.AlwaysHalt.class,
        StandardTags.ReadVariableTag.class, StandardTags.WriteVariableTag.class})
public class NomLanguage extends TruffleLanguage<NomContext> {
    public static final String ID = "nom";
    public static final String MIME_TYPE = "application/xml";

    private static final HashSet<String> loadedManifests = new HashSet<>();
    private static final Shape initialObjectShape = Shape.newBuilder().layout(NomObject.class).build();

    public static NomObject createObject(NomClass cls) {
        if (cls == null)
            throw new RuntimeException("Class not found");
        return new NomObject(initialObjectShape, cls);
    }

    public static NomTimer createTimer() {
        return new NomTimer(initialObjectShape, System.currentTimeMillis());
    }

    @Override
    protected NomContext createContext(Env env) {
        return new NomContext(this, env);
    }

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        String req = request.getSource().getCharacters().toString();
        String manifestPathStr = null;
        String mainClass = null;
        boolean invokeMain = true;
        boolean debug = false;
        boolean ignoreErrorBytecode = false;
        //parse request
        JSONObject jo = new JSONObject(req);
        if (jo.has("manifestPath")) {
            manifestPathStr = jo.getString("manifestPath");
        }
        if (jo.has("mainClass")) {
            mainClass = jo.getString("mainClass");
        }
        if (jo.has("invokeMain")) {
            invokeMain = jo.getBoolean("invokeMain");
        }
        if (jo.has("debug")) {
            debug = jo.getBoolean("debug");
        }
        if (jo.has("ignoreErrorBytecode")) {
            ignoreErrorBytecode = jo.getBoolean("ignoreErrorBytecode");
        }
        if (debug) {
            NomContext.clear();
        }
        //if we are debugging, even if we preloaded it we still curious what the bytecodereader does
        if (!loadedManifests.contains(manifestPathStr) || debug) {
            loadedManifests.add(manifestPathStr);
            assert manifestPathStr != null;
            Path manifestPath = Path.of(manifestPathStr);
            Path dirPath = manifestPath.getParent();
            //load manifest file in manifestPath
            String manifest = Files.readString(manifestPath);
            manifest = manifest.strip();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            var builder = factory.newDocumentBuilder();
            var doc = builder.parse(new InputSource(new StringReader(manifest)));
            //load name from <mainclass name = "xxx" />
            if (mainClass == null) {
                mainClass = doc.getElementsByTagName("mainclass").item(0).getAttributes().getNamedItem("name").getNodeValue();
            }
            //load all <nominterface qname="xxx" file="xxx"/>
            var nomInterfaces = doc.getElementsByTagName("nominterface");
            for (int i = 0; i < nomInterfaces.getLength(); i++) {
                var nomInterface = nomInterfaces.item(i);
                //get info
                var qname = nomInterface.getAttributes().getNamedItem("qname").getNodeValue();
                var file = nomInterface.getAttributes().getNamedItem("file").getNodeValue();
                file = dirPath.resolve(file).toString();

                //load bytecode
                try {
                    if (debug)
                        System.out.println("Loading bytecode " + file);
                    ByteCodeReader.ReadBytecodeFile(this, file, debug);
                } catch (Exception e) {
                    if (!ignoreErrorBytecode) {
                        throw e;
                    }
                    System.out.println("Error loading bytecode " + file + ": " + e.getMessage());
                }
            }
            //load all <nomclass qname = "xxx" file = "xxx" />
            var nomClasses = doc.getElementsByTagName("nomclass");
            for (int i = 0; i < nomClasses.getLength(); i++) {
                var nomClass = nomClasses.item(i);
                //get info
                var qname = nomClass.getAttributes().getNamedItem("qname").getNodeValue();
                var file = nomClass.getAttributes().getNamedItem("file").getNodeValue();
                file = dirPath.resolve(file).toString();

                //load bytecode
                try {
                    if (debug)
                        System.out.println("Loading bytecode " + file);
                    ByteCodeReader.ReadBytecodeFile(this, file, debug);
                } catch (Exception e) {
                    if (!ignoreErrorBytecode) {
                        throw e;
                    }
                    System.out.println("Error loading bytecode " + file + ": " + e.getMessage());
                }
            }
        }

        for (NomInterface nomInterface : NomContext.classes.values()) {
            nomInterface.Register(this);
        }

        if (!invokeMain) {
            return new NomRootNode(
                    this, new FrameDescriptor(),
                    new NomFunctionBodyNode(new NomBasicBlockNode[]{
                            new NomBasicBlockNode(new NomStatementNode[]{
                                    new NomReturnNode(null)
                            }, "method entry")
                    }, 0),
                    "Invalid", 0).getCallTarget();
        }

        if (debug) {
            for (var cls : NomContext.classes.values()) {
                if (NomContext.functionsObject.get(cls) == null) continue;//interface has no method table
                for (var func : NomContext.functionsObject.get(cls).values()) {
                    System.out.println(func.getCallTarget().getRootNode().toString());
                    System.out.println();
                }
            }
        }

        NomClass main = (NomClass) NomContext.classes.get(mainClass);
        //compatibility
        if (main == null) {
            main = (NomClass) NomContext.classes.get(mainClass + "_0");
        }

        NomFunction mainFunc = null;
        for (var method : main.StaticMethods) {
            if (method.GetName().equals("Main")) {
                if (NomContext.functionsObject.get(main) != null)
                    mainFunc = NomContext.functionsObject.get(main).get(method.GetName());
                break;
            }
        }

        if (mainFunc == null) {
            throw new Exception("Main method not found");
        }

        return mainFunc.getCallTarget();
    }

    public static NomExpressionNode callCtorNode(NomSuperClassConstant superClass, int curMethodArgCount, int regIndex, int ctorArgLen, NomExpressionNode[] ctorArgs) {
        //check built in
        if (superClass.GetSuperClass().GetName().equals("Timer_0")) {
            return null;
        }
        return ByteCodeReader.WriteToFrame(curMethodArgCount, regIndex,
                new NomInvokeNode<>(false, superClass,
                        su -> NomContext.classes.get(su.GetSuperClass().GetName()).GetName() + ".ctor",
                        NomContext.classes.get(superClass.GetSuperClass().GetName()).GetName() + ".ctor",
                        su -> NomContext.ctorFunctions.get(su.GetSuperClass().GetTruffleName()).get(ctorArgLen), ctorArgs));
    }

    private final Map<NodeFactory<? extends NomBuiltinNode>, RootCallTarget> builtinTargets = new ConcurrentHashMap<>();

    public RootCallTarget lookupBuiltin(NodeFactory<? extends NomBuiltinNode> factory) {
        RootCallTarget target = builtinTargets.get(factory);
        if (target != null) {
            return target;
        }

        /*
         * The builtin node factory is a class that is automatically generated by the Truffle DSL.
         * The signature returned by the factory reflects the signature of the @Specialization
         *
         * methods in the builtin classes.
         */
        int argumentCount = factory.getExecutionSignature().size();
        NomExpressionNode[] argumentNodes = new NomExpressionNode[argumentCount];
        /*
         * Builtin functions are like normal functions, i.e., the arguments are passed in as an
         * Object[] array encapsulated in SLArguments. A SLReadArgumentNode extracts a parameter
         * from this array.
         */
        for (int i = 0; i < argumentCount; i++) {
            argumentNodes[i] = new NomReadArgumentNode(i);
        }
        /* Instantiate the builtin node. This node performs the actual functionality. */
        NomBuiltinNode builtinBodyNode = factory.createNode((Object) argumentNodes);
        /* The name of the builtin function is specified via an annotation on the node class. */
        String name = lookupNodeInfo(builtinBodyNode.getClass()).shortName();
        /* Wrap the builtin in a RootNode. Truffle requires all AST to start with a RootNode. */
        NomRootNode rootNode = new NomRootNode(this, new FrameDescriptor(), builtinBodyNode, name, argumentCount);

        /*
         * Register the builtin function in the builtin registry. Call targets for builtins may be
         * reused across multiple contexts.
         */
        RootCallTarget newTarget = rootNode.getCallTarget();
        RootCallTarget oldTarget = builtinTargets.putIfAbsent(factory, newTarget);
        if (oldTarget != null) {
            return oldTarget;
        }
        return newTarget;
    }

    public static NodeInfo lookupNodeInfo(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        NodeInfo info = clazz.getAnnotation(NodeInfo.class);
        if (info != null) {
            return info;
        } else {
            return lookupNodeInfo(clazz.getSuperclass());
        }
    }
}
