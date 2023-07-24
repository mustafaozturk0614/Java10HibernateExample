package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Name;
import com.bilgeadam.repository.entity.User;
import com.bilgeadam.utility.HibernateUtility;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/*
    findbyUsername metodunu hql sorgusu olarak yazalım
    findbyId metodunu yazalım
    databasede ki butun isimleri getirelim metod bize List<Name> listesi donecek
     databasede ki butun firtsnameleri getirelim
     dısarıdan bir harf gireceğiz  ismi bu harfile baslayan kullanıcıları getirelim



 */
public class UserRepository implements ICrud<User>{
    Session session;
    Transaction transaction;

    @Override
    public User save(User user) {
        try {
            session= HibernateUtility.getSessionFactory().openSession();
            System.out.println("Oturum açıldı...");
            transaction= session.beginTransaction();
           session.save(user);
            transaction.commit();
            System.out.println("Kayıt başarılı....");
        }catch (Exception e){
            e.printStackTrace();
            transaction.rollback();
            System.out.println("Kayıt başarısız!!!!!");
        }finally {
            System.out.println("oturum kapanıyor...");
            session.close();
        }
        return  user;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<User> findAll() {

      //  String sql=" select * from tbl_user";
        String hql=" select u from User as u";
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<User> typedQuery= session.createQuery(hql, User.class);
        List<User> userList=typedQuery.getResultList();
        return userList;
    }

    @Override
    public Optional<User> findById(Long id) {
        String hql="select u from User u where u.id=:id";
        //String hql2="select u from User u where u.id="+id;
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<User> typedQuery=session.createQuery(hql, User.class);
        typedQuery.setParameter("id",id);//select u from User u where u.id=1;
        User user=null;
        try {
            user=typedQuery.getSingleResult();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return Optional.ofNullable(user);
    }
    public Optional<User> findById2(Long id) {
        session=HibernateUtility.getSessionFactory().openSession();
        User user=session.find(User.class,id);
        if (user==null){
            System.out.println("Kullanıcı bulunamadı");
        }
       return  Optional.ofNullable(user);
    }




    public Optional<User> findByUsername(String username){
        String hql="select u from User as u where u.username=:myusername";
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<User> typedQuery=session.createQuery(hql, User.class);
        typedQuery.setParameter("myusername",username);
       /* Query query=session.createQuery(hql);
        query.setParameter("myusername",username);
        User user2= (User) query.getSingleResult();
        System.out.println("query==>"+user2);
        System.out.println("typed quert==>"+user1);*/
        // getSingleResult bulamadıgı zaman exception fırlatır ;
        User user=null;
        try {
            user=typedQuery.getSingleResult();
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return  Optional.ofNullable(user);
    }

    public List<Name> findAllName(){
        String hql="select u.name from User u";
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<Name> typedQuery= session.createQuery(hql, Name.class);
        List<Name> list=typedQuery.getResultList();
        return list;
    }

    public List<String> findAllFirstName(){
        String hql="select u.name.firstName from User u";
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<String> typedQuery= session.createQuery(hql, String.class);
        List<String> list=typedQuery.getResultList();
        return list;
    }
    // dısarıdan bir harf gireceğiz  ismi bu harfile baslayan kullanıcıları getirelim
    public List<User> findAllFirstNameStartWith(String value){
        String hql="select u from User u where u.name.firstName like:x";
      //  String hql2="select u from User u where u.name.firstName like "+"'"+value+"%'";
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<User> typedQuery= session.createQuery(hql, User.class);
        typedQuery.setParameter("x",value+"%");// M%
        return typedQuery.getResultList();
    }
    public List<User> findAllFirstNameStartWithAndGtPostCount(String value,int postCount){
        String hql="select u from User u where u.name.firstName like:x and u.postCount>:y";
        //  String hql2="select u from User u where u.name.firstName like "+"'"+value+"%'";
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<User> typedQuery= session.createQuery(hql, User.class);
        typedQuery.setParameter("x",value+"%");// M%
        typedQuery.setParameter("y",postCount);
        return typedQuery.getResultList();
    }

    // post countların toplamını bulalım
    public Long sumPostCount(){
        String hql="select sum(postCount) from User";
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<Long> typedQuery= session.createQuery(hql, Long.class);
        return typedQuery.getSingleResult();
    }
    public Double avgPostCount(){
        String hql="select avg(postCount) from User";
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<Double> typedQuery= session.createQuery(hql, Double.class);
        return typedQuery.getSingleResult();
    }

    //post counta gore gruplayalım
       // postCount ,kişi sayısı
       //long int  , kisi sayısı
    public List<Object[]> groupByPostCount(){
        String hql="select postCount,count(u.postCount) from User u  group by postCount";
        session =HibernateUtility.getSessionFactory().openSession();
        TypedQuery<Object[]> typedQuery=session.createQuery(hql,Object[].class);

        return typedQuery.getResultList();
    }

    /*
      en cok post atan kullanıcıyı bulalım
     Butunkullanıcıların  username gender ve postcount nu donen sorgu
     her cınsıyette ki kullanıclar ve toplam attıkları post sayısı
    -- optional hql sorgusu post repostiroyde
        1 nuramalı postu atan kullanıcıyı getiren sorgu ==> join
     */


   public Optional<User> mostPostingUSer(){
        String hql="select u from User u where postCount=(select max(postCount) from User)";
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<User> typedQuery=session.createQuery(hql,User.class);
        User user=null;
     List<User> list=  typedQuery.getResultList();
     if (!list.isEmpty()){
         user=list.get(0);
     }
     return  Optional.ofNullable(user);
    }

    public Optional<User> mostPostingUser2(){
        String hql="select u from User u order by postCount desc";
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<User> typedQuery=session.createQuery(hql,User.class);
        typedQuery.setMaxResults(2);
        User user=null;
        try {
            user=typedQuery.getResultList().get(0);
        }catch (Exception e){
            System.out.println(e.toString());
        }
        typedQuery.getResultList().forEach(System.out::println);

        return  Optional.ofNullable(user);
    }

//    Butunkullanıcıların  username gender ve postcount nu donen sorgu

        public List<Object[]> getUsernameGenderPostCount(){
        String hql=" select u.username,u.gender,u.postCount from User as u";
        session=HibernateUtility.getSessionFactory().openSession();
       TypedQuery<Object[]> typedQuery= session.createQuery(hql,Object[].class);
            List<Object[]> list= typedQuery.getResultList();
            if (list.isEmpty()){
                System.out.println("Herhangi bir sonuc bulunamadı");
            }
            return list;
        }



//    her cınsıyette ki kullanıclar ve toplam attıkları post sayısı

    public List<Object[]> getUserGendersWithTotalPost(){
       String hql="select gender,sum(postCount) from User  group by gender";
       session =HibernateUtility.getSessionFactory().openSession();
       TypedQuery<Object[]> typedQuery= session.createQuery(hql,Object[].class);
       List<Object[]> list=typedQuery.getResultList();
        if (list.isEmpty()){
            System.out.println("Herhangi bir sonuc bulunamadı");
        }
       return  list;
    }

}
