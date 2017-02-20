package ch.hsr.se.mas.fahrplanapp;

import java.io.File;
import java.io.*;
import java.io.IOException;
import java.util.*;
/**
 * Created by Hermann on 21.02.2017.
 */

public class Settings {

    private List<Transportation> transportation = new ArrayList<Transportation>();
    private String homeplace;
    private int classe;


    public Settings() throws IOException, ClassNotFoundException{
        File f = new File("settings.set");
        if(f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("settings.set"))) {
                Settings set1 = (Settings) ois.readObject();
                this.classe = set1.getClasse();
                this.transportation = set1.getTransportation();
                this.homeplace = set1.getHome();
                ois.close();
            } catch (FileNotFoundException fne) {
                fne.printStackTrace();
            }
        }
    }

    public List<Transportation> getTransportation() {
        return this.transportation;
    }

    public void setHome (String homestation) {
        this.homeplace = homestation;
    }

    public int getClasse() {
        return this.classe;
    }

    public void setClasse(int classe1) {
        this.classe = classe1;
    }

    public String getHome() {
        return this.homeplace;
    }

    public void addTransportation(Transportation transp) {
        if (!containstransp(transp)){
            this.transportation.add(transp);
        }
    }

    public void removeTransportation (Transportation transp) {
        for (Transportation t : transportation){
            if (t==transp){
                transportation.remove(t) ;
            }
        }
    }

    public boolean containstransp(Transportation transp){
        for (Transportation t : transportation){
            if (t == transp) return true ;
        }
        return false ;
    }


    public void serializeSettings() {

        try (ObjectOutputStream oos = new ObjectOutputStream(new  FileOutputStream("settings.set"))) {
            oos.writeObject(this);
            oos.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }



}
