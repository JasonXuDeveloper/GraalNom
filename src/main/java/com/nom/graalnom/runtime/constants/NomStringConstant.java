package com.nom.graalnom.runtime.constants;

import com.oracle.truffle.api.strings.TruffleString;

public class NomStringConstant extends NomConstant{
    public TruffleString Text;

    public NomStringConstant(TruffleString text) {
        super(NomConstantType.CTString);
        this.Text = text;
    }

    public TruffleString GetText() {
        return Text;
    }

    @Override
    public void Print(boolean resolve) {
        System.out.print("\"" + Text + "\"");
    }
}
