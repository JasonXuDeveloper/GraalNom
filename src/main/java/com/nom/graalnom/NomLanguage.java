package com.nom.graalnom;

import com.nom.graalnom.parser.ByteCodeReader;
import com.nom.graalnom.runtime.NomContext;
import com.nom.graalnom.runtime.datatypes.NomFunction;
import com.nom.graalnom.runtime.reflections.NomClass;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.strings.TruffleString;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

@TruffleLanguage.Registration(id = NomLanguage.ID, name = "GraalNom",
        defaultMimeType = NomLanguage.MIME_TYPE,
        characterMimeTypes = NomLanguage.MIME_TYPE,
        contextPolicy = TruffleLanguage.ContextPolicy.SHARED,
        fileTypeDetectors = NomFileDetector.class)
@ProvidedTags({StandardTags.CallTag.class, StandardTags.StatementTag.class, StandardTags.RootTag.class, StandardTags.RootBodyTag.class, StandardTags.ExpressionTag.class, DebuggerTags.AlwaysHalt.class,
        StandardTags.ReadVariableTag.class, StandardTags.WriteVariableTag.class})
public class NomLanguage extends TruffleLanguage<NomContext> {
    public static final TruffleString.Encoding STRING_ENCODING = TruffleString.Encoding.UTF_16LE;
    public static final String ID = "nom";
    public static final String MIME_TYPE = "application/xml";

    @Override
    protected NomContext createContext(Env env) {
        return new NomContext(this, env);
    }

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        String filePath = request.getSource().getCharacters().toString();
        Path manifestPath = Path.of(filePath);
        Path dirPath = manifestPath.getParent();
        //load manifest file in manifestPath
        String manifest = Files.readString(manifestPath);
        manifest = manifest.strip();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var doc = builder.parse(new InputSource(new StringReader(manifest)));
        //load name from <mainclass name = "xxx" />
        var mainClass = doc.getElementsByTagName("mainclass").item(0).getAttributes().getNamedItem("name").getNodeValue();
        //load all <nomclass qname = "xxx" file = "xxx" />
        var nomClasses = doc.getElementsByTagName("nomclass");
        for (int i = 0; i < nomClasses.getLength(); i++) {
            var nomClass = nomClasses.item(i);
            //get info
            var qname = nomClass.getAttributes().getNamedItem("qname").getNodeValue();
            var file = nomClass.getAttributes().getNamedItem("file").getNodeValue();
            file = dirPath.resolve(file).toString();

            //load bytecode
            ByteCodeReader.ReadBytecodeFile(this, file);
        }

        //TODO load NomClass from NomContext
        NomClass main = NomContext.classes.get(mainClass);

        //TODO get main method
        NomFunction mainFunc = null;
        for(var method : main.StaticMethods) {
            if (method.name.equals("Main")) {
                mainFunc = NomContext.functionsObject.get(main).get(method.name);
                break;
            }
        }

        //TODO resolve dependencies

        if(mainFunc == null) {
            throw new Exception("Main method not found");
        }

        return mainFunc.getCallTarget();
    }
}
