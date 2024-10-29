package abdellah.project.mycontact.dao;

import java.util.List;

public interface  Dao <T>  {
    public  Boolean create( T o);
    public  Boolean update(T o);
    public Boolean delete(T o);
    public List<T> findAll();
    public T findById(int id);
}
