package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.repository.entity.User;
import com.bilgeadam.utility.HibernateUtility;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class PostRepository implements ICrud<Post> {

    Session session;
    Transaction transaction;
    @Override
    public Post save(Post post) {
        try {
            session= HibernateUtility.getSessionFactory().openSession();
            System.out.println("Oturum açıldı...");
            transaction=session.beginTransaction();
            session.save(post);
            transaction.commit();
            System.out.println("Kayıt başaırlı");
        }catch (Exception e){
            System.out.println("Bir hata olustu"+ e.toString());
            transaction.rollback();
        }finally {
            session.close();
        }
        return post;
    }

    @Override
    public Post update(Post post) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Post> findAll() {
        return null;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.empty();
    }

//    -- optional hql sorgusu post repostiroyde
//        1 nuramalı postu atan kullanıcıyı getiren sorgu ==> join

    public Optional<User> findByUserWithPostId(Long postId){
        String hql="select u from User u join Post p  on  u.id=p.userId where p.id="+postId;
        session=HibernateUtility.getSessionFactory().openSession();
        TypedQuery<User> typedQuery= session.createQuery(hql, User.class);
        User user=null;
        try {
            user=typedQuery.getSingleResult();
        }catch (Exception e){
            System.out.println("Kullanıcı bulunamadı");
        }
        return  Optional.ofNullable(user);
    }
}
