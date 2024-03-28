package com.nom.graalnom.runtime.constants;


public class NomStringConstant extends NomConstant{
    public String Text;

    public NomStringConstant(String text) {
        super(NomConstantType.CTString);
        this.Text = text;
    }

    public String Value() {
        return Text;
    }

    @Override
    public void Print(boolean resolve) {
        System.out.print("\"" + Text + "\"");
    }

    @Override
    public String toString() {
        return Text;
    }
}
