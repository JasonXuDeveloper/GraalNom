## What is the handler in ByteCodeReader.cpp

```cpp
if (handler != nullptr)
{
    handler->ReadClass(cls);
}
```

## Why does `NomClass` inherits `NomInterface`?

## Where is `NomClass->GetName()` implemented?

## Is Objects in MonNom more like Objects in JavaScript?
> https://github.com/oracle/graaljs/blob/master/graal-js/src/com.oracle.truffle.js/src/com/oracle/truffle/js/runtime/builtins/JSClass.java#L69


## For the metadata, is it possible to merge the fields in `xxx` with `xxxLoaded`

## Does `BytecodeReader::ReadInstruction()` contains all the instructions needed for the AST interpreter?

## Shape-wised (type-wised) if we make all classes "dynamic" (compatible with adding fields in runtime), isn't that just JavaScript objects?
> https://github.com/oracle/graaljs/blob/8e7bdbb39cd186b09cb8a39413bc8c5107ad22c8/graal-js/src/com.oracle.truffle.js/src/com/oracle/truffle/js/runtime/objects/JSObjectUtil.java#L317


## What does a `lambda` and a `struct` do in a `NomClass`? I.E. isn't a `lambda` more like a member (field) of the typed object and a `struct` more like an independent declaration of something like `NomStruct` (which somehow does not exist)? 