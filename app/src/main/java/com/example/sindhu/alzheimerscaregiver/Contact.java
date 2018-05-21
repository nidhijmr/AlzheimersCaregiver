package com.example.sindhu.alzheimerscaregiver;

import java.io.Serializable;


public class Contact implements Serializable{
    int userid;
    String name,uname,passwrd,emailid,phone_no,role;

    public void setUserid(int id)
    {
        this.userid=id;
    }

    public int getId()
    {
        return this.userid;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setUname(String uname)
    {
        this.uname=uname;
    }

    public String getUname()
    {
        return this.uname;
    }

    public void setPass(String pass)
    {
        this.passwrd=pass;
    }

    public String getPass()
    {
        return this.passwrd;
    }

    public void setEmailid(String emailid)
    {
        this.emailid=emailid;
    }
    public String getEmailid()
    {
        return this.emailid;
    }
    public void setPhone_no(String phone_no)
    {
        this.phone_no=phone_no;
    }
    public String  getPhone_no()
    {
        return this.phone_no;
    }
    public void setRole(String role)
    {
        this.role =role;
    }
    public String getRole()
    {
        return this.role;
    }

}
