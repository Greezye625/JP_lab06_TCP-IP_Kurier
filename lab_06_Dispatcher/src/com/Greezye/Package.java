package com.Greezye;

public class Package {
    private String Clientnadwaca;
    private String size;

    public Package(String clientnadwaca, String size) {
        Clientnadwaca = clientnadwaca;
        this.size = size;
    }

    public String getClientnadwaca() {
        return Clientnadwaca;
    }

    public void setClientnadwaca(String clientnadwaca) {
        Clientnadwaca = clientnadwaca;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
