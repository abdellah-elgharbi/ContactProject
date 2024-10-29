package abdellah.project.mycontact.service;

import java.util.ArrayList;
import java.util.List;

import abdellah.project.mycontact.beans.Contact;
import abdellah.project.mycontact.dao.Dao;

public class Service implements Dao<Contact> {
    private List<Contact> repas;

    public Service() {
        this.repas = new ArrayList<Contact>();
    }

    @Override
    public Boolean create(Contact o) {
        return repas.add(o);
    }

    @Override
    public Boolean update(Contact o) {
        return null;
    }

    @Override
    public Boolean delete(Contact o) {
        return repas.remove(o);
    }

    @Override
    public List<Contact> findAll() {
        return repas;
    }

    @Override
    public Contact findById(int id) {
        for ( Contact r : repas ){
            if ( id==r.getId()){
                return r;
            }
        }
        return  null;
    }
}
