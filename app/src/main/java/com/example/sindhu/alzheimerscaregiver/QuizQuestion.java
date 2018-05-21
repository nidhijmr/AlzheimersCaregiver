package com.example.sindhu.alzheimerscaregiver;


public class QuizQuestion {
    private String username;
    private String question;
    private String optiona;
    private String optionb;
    private String optionc;
    private String optiond;
    private String answer;

    public QuizQuestion(String name, String q, String oa, String ob, String oc, String od, String ans) {
        username = name;
        question = q;
        optiona = oa;
        optionb = ob;
        optionc = oc;
        optiond = od;
        answer = ans;
    }

    public QuizQuestion()
    {
        username = "";
        question = "";
        optiona = "";
        optionb = "";
        optionc = "";
        optiond = "";
        answer = "";
    }

    public String getUsername()
    {
        return username;
    }

    public String getQuestion()
    {
        return question;

    }

    public String getOptiona()
    {
        return optiona;

    }
    public String getOptionb(){
        return optionb;

    }

    public String getOptionc()
    {
        return optionc;

    }

    public String getOptiond()
    {
        return optiond;

    }

    public String getAnswer()
    {
        return answer;
    }

    public void setUsername(String name)
    {
        username=name;
    }

    public void setQuestion(String q)
    {
        question=q;
    }

    public void setOptiona(String oa)
    {
        optiona=oa;
    }

    public void setOptionb(String ob)
    {
        optionb=ob;
    }

    public void setOptionc(String oc)
    {
        optionc=oc;
    }

    public void setOptiond(String od)
    {
        optiond=od;
    }

    public void setAnswer(String ans)
    {
        answer=ans;
    }
}
