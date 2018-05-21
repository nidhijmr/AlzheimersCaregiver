package com.example.sindhu.alzheimerscaregiver;

public class ImageDetails {

    private int id;
    private String name;
    private String relationship;
    private byte[] image;

    public ImageDetails(String name, String relationship, byte[] image)
    {
        this.name= name;
        this.relationship = relationship;
        this.image = image;
        // this.id=id;
    }

    public int getId()
    {
        return id;

    }

    public void setId()
    {
        this.id=id;

    }

    public String getName()
    {
        return name;

    }

    public void setName()
    {
        this.name=name;

    }

    public String getRelationship()
    {
        return relationship;

    }

    public void setRelationship()
    {
        this.relationship=relationship;

    }

    public byte[] getImage()
    {
        return image;

    }

    public void setImage()
    {
        this.image=image;

    }


}
